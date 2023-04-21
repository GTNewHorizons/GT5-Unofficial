package goodgenerator.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import goodgenerator.items.MyMaterial;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.util.GT_Utility;

public class StackUtils {

    /**
     * Multiplies one ItemStack by a multiplier, and splits it into as many full stacks as it needs to.
     *
     * @param stack      The ItemStack you want to multiply
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
     */
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

    /**
     * Turns the {@code items} List into a {@code HashMap} containing each unique {@code ItemStack} as the key, and a
     * value representing the total amount of the respective {@code ItemStack} in the List.
     */
    public static HashMap<ItemStack, Integer> getTotalItems(List<ItemStack> items) {
        HashMap<ItemStack, Integer> totals = new HashMap<>();
        itemLoop: for (ItemStack item : items) {
            int t = items.stream().filter(i2 -> GT_Utility.areStacksEqual(item, i2)).mapToInt(i -> i.stackSize).sum();
            for (ItemStack i2 : totals.keySet()) if (GT_Utility.areStacksEqual(item, i2)) continue itemLoop;
            totals.put(GT_Utility.copyAmount(1, item), t);
        }
        return totals;
    }

    /**
     * Turns the {@code items} Array into a {@code HashMap} containing each unique {@code ItemStack} as the key, and a
     * value representing the total amount of the respective {@code ItemStack} in the Array.
     */
    public static HashMap<ItemStack, Integer> getTotalItems(ItemStack[] items) {
        return getTotalItems(Arrays.asList(items));
    }

    public static FluidStack getTieredFluid(int aTier, int aAmount) {
        switch (aTier) {
            case 0: // ULV
                return Materials.RedAlloy.getMolten(aAmount);
            case 1: // LV
                return Materials.TinAlloy.getMolten(aAmount);
            case 2: // MV
                return Materials.RoseGold.getMolten(aAmount);
            case 3: // HV
                return MyMaterial.zircaloy4.getMolten(aAmount);
            case 4: // EV
                return MyMaterial.incoloy903.getMolten(aAmount);
            case 5: // IV
                return MyMaterial.titaniumBetaC.getMolten(aAmount);
            case 6: // LuV
                return MyMaterial.artheriumSn.getMolten(aAmount);
            case 7: // ZPM
                return MyMaterial.dalisenite.getMolten(aAmount);
            case 8: // UV
                return MyMaterial.tairitsu.getMolten(aAmount);
            case 9: // UHV
                return MyMaterial.preciousMetalAlloy.getMolten(aAmount);
            case 10: // UEV
                return MyMaterial.enrichedNaquadahAlloy.getMolten(aAmount);
            case 11: // UIV
                return MyMaterial.metastableOganesson.getMolten(aAmount);
            case 12: // UMV
                return MaterialsUEVplus.SpaceTime.getMolten(aAmount);
            default:
                return MyMaterial.shirabon.getMolten(aAmount);
        }
    }
}
