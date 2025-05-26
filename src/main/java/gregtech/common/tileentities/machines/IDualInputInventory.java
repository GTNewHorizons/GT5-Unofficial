package gregtech.common.tileentities.machines;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface IDualInputInventory {

    boolean isEmpty();

    /** The list of real items stored in this sandboxed inventory. */
    ItemStack[] getItemInputs();

    /** The list of real fluids stored in this sandboxed inventory. */
    FluidStack[] getFluidInputs();

}
