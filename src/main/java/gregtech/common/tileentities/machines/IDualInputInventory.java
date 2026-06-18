package gregtech.common.tileentities.machines;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface IDualInputInventory {

    boolean isEmpty();

    ItemStack[] getItemInputs();

    FluidStack[] getFluidInputs();

}
