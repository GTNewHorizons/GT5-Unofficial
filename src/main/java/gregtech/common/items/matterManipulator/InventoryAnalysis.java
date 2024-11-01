package gregtech.common.items.matterManipulator;

import java.util.Objects;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import gregtech.common.items.matterManipulator.BlockAnalyzer.IBlockApplyContext;

/**
 * An analysis for an inventory.
 */
public class InventoryAnalysis {

    public boolean mFuzzy;
    public IItemProvider[] mItems;

    public InventoryAnalysis() {}

    /**
     * Gets an analysis for an inventory.
     * 
     * @param fuzzy When true, NBT will be ignored and items will be fuzzily-retrieved.
     */
    public static InventoryAnalysis fromInventory(IInventory inv, boolean fuzzy) {
        InventoryAnalysis analysis = new InventoryAnalysis();
        analysis.mFuzzy = fuzzy;

        analysis.mItems = new IItemProvider[inv.getSizeInventory()];

        for (int i = 0; i < analysis.mItems.length; i++) {
            analysis.mItems[i] = getProviderFor(inv.getStackInSlot(i), fuzzy);
        }

        return analysis;
    }

    private static IItemProvider getProviderFor(ItemStack stack, boolean fuzzy) {
        if (stack == null || stack.getItem() == null) return null;

        if (!fuzzy) {
            AECellItemProvider cell = AECellItemProvider.fromWorkbenchItem(stack);

            if (cell != null) return cell;
        }

        PatternItemProvider pattern = PatternItemProvider.fromPattern(stack);

        if (pattern != null) return pattern;

        return fuzzy ? new PortableItemStack(stack) : PortableItemStack.withNBT(stack);
    }

    /**
     * Applies the analysis
     * 
     * @param consume  When true, items will be consumed
     * @param simulate When true, the inventory will not be modified in any way
     * @return True when the inventory was successfully updated
     */
    public boolean apply(IBlockApplyContext context, IInventory inv, boolean consume, boolean simulate) {
        if (inv.getSizeInventory() != mItems.length) {
            context.error("inventory was the wrong size");
            return false;
        }

        boolean didSomething = false;

        for (int i = 0; i < mItems.length; i++) {
            IItemProvider target = mItems[i];
            IItemProvider actual = getProviderFor(inv.getStackInSlot(i), mFuzzy);

            if (!Objects.equals(target, actual)) {
                ItemStack stack = inv.getStackInSlot(i);
                if (stack != null) {
                    if (!simulate) {
                        inv.setInventorySlotContents(i, null);
                        didSomething = true;
                    }
                    if (consume) context.givePlayerItems(stack);
                }

                if (target != null) {
                    ItemStack toInsert = target.getStack(context, consume);

                    if (toInsert == null) {
                        context.warn("could not gather item for inventory: " + target.toString());
                    } else {
                        if (!simulate) {
                            inv.setInventorySlotContents(i, toInsert);
                            didSomething = true;
                        }
                    }
                }
            }
        }

        if (didSomething) inv.markDirty();

        return true;
    }
}
