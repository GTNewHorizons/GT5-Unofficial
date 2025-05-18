package gregtech.api.objects;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GTDualInputPattern {

    public ItemStack[] inputItems;
    public FluidStack[] inputFluid;

    public GTDualInputPattern() {}

    public GTDualInputPattern(ItemStack[] inputItems, FluidStack[] inputFluid) {
        this.inputItems = inputItems;
        this.inputFluid = inputFluid;
    }
}
