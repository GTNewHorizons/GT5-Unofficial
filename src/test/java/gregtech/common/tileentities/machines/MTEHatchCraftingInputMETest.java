package gregtech.common.tileentities.machines;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.fluids.FluidStack;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.glodblock.github.common.item.ItemFluidPacket;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

class MTEHatchCraftingInputMETest {

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
