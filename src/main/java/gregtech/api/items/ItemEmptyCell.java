package gregtech.api.items;

import gregtech.api.enums.Materials;
import gregtech.api.util.GTUtility;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

public class ItemEmptyCell extends GTGenericItem implements IFluidContainerItem {

    private final int capacity = 1000;

    public ItemEmptyCell() {
        super("emptyCell", StatCollector.translateToLocal("gt.item.fluid_cell.empty.name"), "gt.item.fluid_cell.empty.tooltip");
        this.setMaxStackSize(64);
        setNoRepair();
    }

    @Override
    public FluidStack getFluid(ItemStack container) {
        return null;
    }

    @Override
    public int getCapacity(ItemStack container) {
        return capacity;
    }

    @Override
    public int fill(ItemStack container, FluidStack fluid, boolean doFill) {
        if (container.stackSize != 1) return 0;

        ItemStack filledCell = GTUtility.fillFluidContainer(fluid, container, false, false);
        if (filledCell != null) {
            container.func_150996_a/*setItem*/(filledCell.getItem());
            container.setItemDamage(filledCell.getItemDamage());
            return GTUtility.getFluidForFilledItem(filledCell, false).amount;
        }
        return 0;
    }

    @Override
    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
        return null;
    }
}
