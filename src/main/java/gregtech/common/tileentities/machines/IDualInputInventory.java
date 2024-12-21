package gregtech.common.tileentities.machines;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.objects.GTDualInputs;

public interface IDualInputInventory {

    boolean isEmpty();

    ItemStack[] getItemInputs();

    FluidStack[] getFluidInputs();

    GTDualInputs getPatternInputs();

}
