package gregtech.common.covers;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.junit.jupiter.api.Test;

import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;

import cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry;
import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.tileentity.ICoverable;

class CoverChestMigrationTest {

    @Test
    void preservesSlotsWhenCapacityChanges() throws Exception {
        Item item = new Item();
        Method register = FMLControlledNamespacedRegistry.class
            .getDeclaredMethod("addObjectRaw", int.class, String.class, Object.class);
        register.setAccessible(true);
        register.invoke(Item.itemRegistry, 30002, "gregtech:cover_chest_migration_test", item);
        if (Items.feather == null) {
            MethodHandles.privateLookupIn(Items.class, MethodHandles.lookup())
                .unreflectVarHandle(Items.class.getDeclaredField("feather"))
                .set(item);
        }

        for (int[] capacities : new int[][] { { 12, 9 }, { 9, 12 } }) {
            int oldCapacity = capacities[0];
            int newCapacity = capacities[1];
            ItemStack coverItem = new ItemStack(item);
            CoverChest savedCover = createCover(coverItem, oldCapacity);
            ItemStack contents = new ItemStack(item, 1, 7);
            NBTTagCompound contentsTag = new NBTTagCompound();
            contentsTag.setString("payload", "preserve across resize");
            contents.setTagCompound(contentsTag);
            IItemHandlerModifiable savedInventory = savedCover.getItems();
            savedInventory.setStackInSlot(0, contents);

            CoverChest resizedCover = createCover(coverItem, newCapacity);
            resizedCover.readFromNbt(savedCover.writeToNBT(new NBTTagCompound()));
            resizedCover.doCoverThings((byte) 0, 0);

            IItemHandlerModifiable inventory = resizedCover.getItems();
            assertTrue(ItemStack.areItemStacksEqual(contents, inventory.getStackInSlot(0)));
            for (int i = oldCapacity; i < newCapacity; i++) {
                assertNull(inventory.getStackInSlot(i));
            }
        }
    }

    private static CoverChest createCover(ItemStack coverItem, int slots) {
        return new CoverChest(
            new CoverContext(coverItem, ForgeDirection.NORTH, org.mockito.Mockito.mock(ICoverable.class)),
            slots,
            null);
    }
}
