package gregtech.common.items.matterManipulator;

import java.util.Objects;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import gregtech.common.items.matterManipulator.BlockAnalyzer.IBlockApplyContext;

public class InventoryAnalysis {

    public IItemProvider[] mItems;

    public InventoryAnalysis() {}

    public static InventoryAnalysis fromInventory(IInventory inv) {
        InventoryAnalysis analysis = new InventoryAnalysis();

        analysis.mItems = new IItemProvider[inv.getSizeInventory()];

        for (int i = 0; i < analysis.mItems.length; i++) {
            analysis.mItems[i] = getProviderFor(inv.getStackInSlot(i));
        }

        return analysis;
    }

    private static IItemProvider getProviderFor(ItemStack stack) {
        if (stack == null || stack.getItem() == null) return null;

        AECellItemProvider cell = AECellItemProvider.fromWorkbenchItem(stack);

        if (cell != null) return cell;

        return new PortableItemStack(stack);
    }

    public boolean apply(IBlockApplyContext context, IInventory inv) {
        if (inv.getSizeInventory() != mItems.length) {
            context.error("inventory was the wrong size");
            return false;
        }

        boolean didSomething = false;

        for (int i = 0; i < mItems.length; i++) {
            IItemProvider target = mItems[i];
            IItemProvider actual = getProviderFor(inv.getStackInSlot(i));

            if (!Objects.equals(target, actual)) {
                ItemStack stack = inv.getStackInSlot(i);
                if (stack != null) {
                    inv.setInventorySlotContents(i, null);
                    context.givePlayerItems(stack);
                    didSomething = true;
                }

                if (target != null) {
                    ItemStack toInsert = target.getStack(context);

                    if (toInsert == null) {
                        context.warn("could not put item in inventory: " + target.toString());
                    } else {
                        inv.setInventorySlotContents(i, toInsert);
                        didSomething = true;
                    }
                }
            }
        }

        if (didSomething) inv.markDirty();

        return true;
    }
}
