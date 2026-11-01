package gregtech.common.covers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.RETURNS_SELF;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.cleanroommc.modularui.utils.item.IItemHandlerModifiable;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Flow;

import cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry;
import gregtech.api.covers.CoverContext;
import gregtech.api.metatileentity.CoverableTileEntity;
import gregtech.common.gui.modularui.cover.CoverChestGui;

class CoverChestTest {

    @Test
    void keepsInventoryThroughRemovalReattachmentAndMachineDrop() throws Exception {
        Item item = new Item();
        // Register without starting FML's LaunchClassLoader-only mod loader in the unit-test JVM.
        Method register = FMLControlledNamespacedRegistry.class
            .getDeclaredMethod("addObjectRaw", int.class, String.class, Object.class);
        register.setAccessible(true);
        register.invoke(Item.itemRegistry, 30000, "gregtech:cover_chest_test", item);
        if (Items.feather == null) {
            MethodHandles.privateLookupIn(Items.class, MethodHandles.lookup())
                .unreflectVarHandle(Items.class.getDeclaredField("feather"))
                .set(item);
        }
        ItemStack coverItem = new ItemStack(item);

        for (int slots : new int[] { 9, 12, 15 }) {
            CoverableTileEntity tile = mock(
                CoverableTileEntity.class,
                withSettings().useConstructor()
                    .defaultAnswer(CALLS_REAL_METHODS));
            CoverChest cover = new CoverChest(new CoverContext(coverItem, ForgeDirection.NORTH, tile), slots, null);
            tile.attachCover(cover);
            assertTrue(cover.isAttached());
            PanelSyncManager syncManager = mock(PanelSyncManager.class);
            EntityPlayer player = mock(EntityPlayer.class);
            when(syncManager.getPlayer()).thenReturn(player);
            new CoverChestGui(cover).addUIWidgets(syncManager, mock(Flow.class, RETURNS_SELF), null);
            ArgumentCaptor<Runnable> uiTick = ArgumentCaptor.forClass(Runnable.class);
            verify(syncManager).onServerTick(uiTick.capture());
            uiTick.getValue()
                .run();
            verify(player, never()).closeScreen();
            assertFalse(
                cover.asItemStack()
                    .hasTagCompound());
            List<String> tooltip = new ArrayList<>();
            CoverChest.addContentsTooltip(cover.writeToNBT(new NBTTagCompound()), tooltip, true);
            assertTrue(tooltip.isEmpty());

            ItemStack contents = new ItemStack(item, 1, 7);
            NBTTagCompound contentsTag = new NBTTagCompound();
            contentsTag.setString("payload", "keep nested item NBT");
            contents.setTagCompound(contentsTag);
            contents.setStackDisplayName("Stored tool");
            IItemHandlerModifiable inventory = cover.getItems();
            inventory.setStackInSlot(slots - 1, contents);
            NBTTagCompound expected = cover.writeToNBT(new NBTTagCompound());

            // A broken machine must reject pending GUI transactions before the next UI tick.
            when(tile.isDead()).thenReturn(true);
            assertFalse(cover.isAttached());
            assertFalse(inventory.isItemValid(0, contents));
            assertSame(contents, inventory.insertItem(0, contents, false));
            assertNull(inventory.extractItem(slots - 1, 1, false));
            when(tile.isDead()).thenReturn(false);
            assertTrue(inventory.isItemValid(0, contents));
            assertEquals(contents, inventory.extractItem(slots - 1, 1, true));

            ItemStack removed = tile.detachCover(ForgeDirection.NORTH);
            uiTick.getValue()
                .run();
            verify(player).closeScreen();
            assertFalse(cover.isAttached());
            assertNull(inventory.getStackInSlot(slots - 1));
            assertNull(inventory.extractItem(slots - 1, 1, false));
            assertFalse(inventory.isItemValid(0, contents));
            assertSame(contents, inventory.insertItem(0, contents, false));
            assertTrue(removed.hasTagCompound());
            CoverChest.addItemTooltip(removed, tooltip, false);
            assertEquals(1, tooltip.size());
            assertFalse(
                tooltip.get(0)
                    .contains("Stored tool"));
            tooltip.clear();
            CoverChest.addItemTooltip(removed, tooltip, true);
            assertEquals(2, tooltip.size());
            assertEquals("  1 x Stored tool", tooltip.get(1));
            CoverChest reattached = new CoverChest(new CoverContext(removed, ForgeDirection.NORTH, tile), slots, null);
            tile.attachCover(reattached);
            reattached.onPlayerAttach(null, removed);
            assertTrue(reattached.isAttached());
            assertFalse(cover.isAttached());
            assertSame(contents, inventory.insertItem(0, contents, false));
            assertEquals(expected, reattached.writeToNBT(new NBTTagCompound()));

            NBTTagCompound machineDrop = reattached.writeToNBT(new NBTTagCompound());
            tooltip.clear();
            CoverChest.addContentsTooltip(machineDrop, tooltip, true);
            assertEquals("  1 x Stored tool", tooltip.get(1));
            reattached.onBaseTEDestroyed();
            CoverChest restored = createCover(coverItem, slots);
            restored.readFromNbt(machineDrop);
            assertEquals(expected, restored.writeToNBT(new NBTTagCompound()));
        }
    }

    private static CoverChest createCover(ItemStack item, int slots) {
        return new CoverChest(new CoverContext(item, ForgeDirection.NORTH, null), slots, null);
    }
}
