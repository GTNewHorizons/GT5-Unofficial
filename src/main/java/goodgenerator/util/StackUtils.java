package goodgenerator.util;

import gregtech.api.util.GT_Utility;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import net.minecraft.item.ItemStack;

public class StackUtils {

    /**
     * Multiplies one ItemStack by a multiplier, and splits it into as many full stacks as it needs to.
     * @param stack The ItemStack you want to multiply
     * @param multiplier The number the stack is multiplied by
     * @return A List of stacks that, in total, are the same as the input ItemStack after it has been multiplied.
     */
    public static List<ItemStack> multiplyAndSplitIntoStacks(ItemStack stack, int multiplier) {
        int totalItems = stack.stackSize * multiplier;
        ArrayList<ItemStack> stacks = new ArrayList<>();
        if (totalItems >= 64) {
            for (int i = 0; i < totalItems / 64; i++) {
                stacks.add(GT_Utility.copyAmount(64, stack));
            }
        }
        if (totalItems % 64 > 0) {
            stacks.add(GT_Utility.copyAmount(totalItems % 64, stack));
        }
        return stacks;
    }

    /**
     * Merges the ItemStacks in the array into full stacks.
     * */
    public static ArrayList<ItemStack> mergeStacks(List<ItemStack> stacks) {
        ArrayList<ItemStack> output = new ArrayList<>();
        for (int index = 0; index < stacks.size(); index++) {
            ItemStack i = stacks.get(index);
            boolean hasDupe = false;
            int newSize = i.stackSize;
            for (int j = index + 1; j < stacks.size(); j++) {
                ItemStack is2 = stacks.get(j);
                if (GT_Utility.areStacksEqual(i, is2)) {
                    hasDupe = true;
                    newSize += is2.stackSize;
                    stacks.remove(j);
                    j--;
                }
            }
            if (hasDupe) {
                if (newSize >= 64) {
                    for (int k = 0; k < newSize / 64; k++) {
                        output.add(GT_Utility.copyAmount(64, i));
                    }
                }
                if (newSize % 64 > 0) {
                    output.add(GT_Utility.copyAmount(newSize > 64 ? newSize % 64 : newSize, i));
                }
            } else output.add(i);
        }
        return output;
    }

    public static HashMap<ItemStack, Integer> getTotalItems(List<ItemStack> items) {
        HashMap<ItemStack, Integer> totals = new HashMap<>();
        itemLoop:
        for (ItemStack item : items) {
            int t = items.stream()
                    .filter(i2 -> GT_Utility.areStacksEqual(item, i2))
                    .mapToInt(i -> i.stackSize)
                    .sum();
            for (ItemStack i2 : totals.keySet()) if (GT_Utility.areStacksEqual(item, i2)) continue itemLoop;
            totals.put(GT_Utility.copyAmount(1, item), t);
        }
        return totals;
    }

    public static HashMap<ItemStack, Integer> getTotalItems(ItemStack[] items) {
        return getTotalItems(Arrays.asList(items));
    }
}
