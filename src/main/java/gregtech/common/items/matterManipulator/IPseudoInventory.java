package gregtech.common.items.matterManipulator;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * Something that can accept and provide items/fluids.
 */
public interface IPseudoInventory {

    public boolean tryConsumeItems(ItemStack... items);

    public void givePlayerItems(ItemStack... items);

    public void givePlayerFluids(FluidStack... fluids);
}
