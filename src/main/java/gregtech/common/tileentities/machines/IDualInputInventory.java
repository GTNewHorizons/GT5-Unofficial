package gregtech.common.tileentities.machines;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface IDualInputInventory {

    ItemStack[] getItemInputs();

    FluidStack[] getFluidInputs();
}
