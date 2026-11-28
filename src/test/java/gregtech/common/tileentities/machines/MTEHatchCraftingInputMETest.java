package gregtech.common.tileentities.machines;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.fluids.FluidStack;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.glodblock.github.common.item.ItemFluidPacket;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

class MTEHatchCraftingInputMETest {

    @Test
    void interfaceTerminalReceivesTranslationKeyWhenThereIsNoCraftingIcon() {
        MTEHatchCraftingInputME hatch = mock(MTEHatchCraftingInputME.class);
        doCallRealMethod().when(hatch)
            .getRawName();
        when(hatch.getLocalNameKey()).thenReturn("gt.blockmachines.hatch.crafting_input.me.item_only.name");
        when(hatch.getLocalName()).thenReturn("Crafting Input Bus (ME)");

        assertEquals("gt.blockmachines.hatch.crafting_input.me.item_only.name", hatch.getRawName());
    }

    @Test
    void wailaResolvesTheHatchNameInTheViewersLanguage() {
        String key = "gt.blockmachines.hatch.crafting_input.me.item_only.name";
        MTEHatchCraftingInputME hatch = mock(MTEHatchCraftingInputME.class);
        doCallRealMethod().when(hatch)
            .getWailaNameData();
        when(hatch.getLocalNameKey()).thenReturn(key);

        try (MockedStatic<StatCollector> translations = mockStatic(StatCollector.class)) {
            var nameData = hatch.getWailaNameData();
            translations.verifyNoInteractions();

            translations.when(() -> StatCollector.translateToLocal(key))
                .thenReturn("Crafting Input Bus (ME)", "\u0412\u0445\u043e\u0434\u043d\u043e\u0439 \u043b\u044e\u043a");
            assertEquals("Crafting Input Bus (ME)", MTEHatchCraftingInputME.getWailaDisplayName(nameData));
            assertEquals(
                "\u0412\u0445\u043e\u0434\u043d\u043e\u0439 \u043b\u044e\u043a",
                MTEHatchCraftingInputME.getWailaDisplayName(nameData));
        }
    }

    @Test
    void wailaResolvesTheCraftingMachineNameFromItsIcon() {
        MTEHatchCraftingInputME hatch = mock(MTEHatchCraftingInputME.class);
        doCallRealMethod().when(hatch)
            .getWailaNameData();
        when(hatch.getLocalNameKey()).thenReturn("gt.blockmachines.hatch.crafting_input.me.item_only.name");
        ItemStack serverIcon = mock(ItemStack.class);
        ItemStack clientIcon = mock(ItemStack.class);
        NBTTagCompound iconData = new NBTTagCompound();
        when(hatch.getCrafterIcon()).thenReturn(serverIcon);
        when(serverIcon.writeToNBT(any(NBTTagCompound.class))).thenReturn(iconData);
        when(clientIcon.getDisplayName()).thenReturn("Промышленный экструдер");

        try (MockedStatic<ItemStack> stacks = mockStatic(ItemStack.class)) {
            stacks.when(() -> ItemStack.loadItemStackFromNBT(iconData))
                .thenReturn(clientIcon);
            assertEquals(
                "Промышленный экструдер",
                MTEHatchCraftingInputME.getWailaDisplayName(hatch.getWailaNameData()));
            verify(serverIcon, times(0)).getDisplayName();
        }
    }

    @Test
    void wailaKeepsThePlayersCustomNameVerbatim() {
        String name = "gt.blockmachines.hatch.crafting_input.me.item_only.name";
        MTEHatchCraftingInputME hatch = mock(MTEHatchCraftingInputME.class);
        doCallRealMethod().when(hatch)
            .getWailaNameData();
        when(hatch.hasCustomName()).thenReturn(true);
        when(hatch.getCustomName()).thenReturn(name);

        try (MockedStatic<StatCollector> translations = mockStatic(StatCollector.class)) {
            assertEquals(name, MTEHatchCraftingInputME.getWailaDisplayName(hatch.getWailaNameData()));
            translations.verifyNoInteractions();
        }
    }

    @Test
    void droppingFluidThenRemovingPatternDoesNotDropItAgain() throws Exception {
        FluidStack fluid = mock(FluidStack.class);
        fluid.amount = 1000;
        MTEHatchCraftingInputME parent = mock(MTEHatchCraftingInputME.class);
        IGregTechTileEntity tile = mock(IGregTechTileEntity.class);
        World world = mock(World.class);
        Field providerField = World.class.getDeclaredField("provider");
        providerField.setAccessible(true);
        providerField.set(world, mock(WorldProvider.class));
        when(parent.getBaseMetaTileEntity()).thenReturn(tile);
        when(tile.getWorld()).thenReturn(world);

        MTEHatchCraftingInputME.PatternSlot<?> slot = mock(
            MTEHatchCraftingInputME.PatternSlot.class,
            CALLS_REAL_METHODS);
        Field parentField = MTEHatchCraftingInputME.PatternSlot.class.getDeclaredField("parentMTE");
        parentField.setAccessible(true);
        parentField.set(slot, parent);

        try (MockedStatic<ItemFluidPacket> packets = mockStatic(ItemFluidPacket.class)) {
            packets.when(() -> ItemFluidPacket.newStack(any(FluidStack.class)))
                .thenReturn(mock(ItemStack.class));

            // Block destruction drops the fluid, then pattern removal tries the same slot again.
            slot.dropFluid(fluid);
            slot.dropFluid(fluid);

            verify(world, times(1)).spawnEntityInWorld(any(Entity.class));
            assertEquals(0, fluid.amount);
        }
    }
}
