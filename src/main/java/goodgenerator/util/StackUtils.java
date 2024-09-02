package goodgenerator.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import appeng.api.AEApi;
import appeng.api.storage.data.IAEFluidStack;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.util.GTUtility;

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
                stacks.add(GTUtility.copyAmount(64, stack));
            }
        }
        if (totalItems % 64 > 0) {
            stacks.add(GTUtility.copyAmount(totalItems % 64, stack));
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
            int newSize = i.stackSize;
            for (int j = index + 1; j < stacks.size(); j++) {
                ItemStack is2 = stacks.get(j);
                if (GTUtility.areStacksEqual(i, is2)) {
                    newSize += is2.stackSize;
                    stacks.remove(j);
                    j--;
                }
            }
            output.add(GTUtility.copyAmountUnsafe(newSize, i));
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
            int t = items.stream()
                .filter(i2 -> GTUtility.areStacksEqual(item, i2))
                .mapToInt(i -> i.stackSize)
                .sum();
            for (ItemStack i2 : totals.keySet()) if (GTUtility.areStacksEqual(item, i2)) continue itemLoop;
            totals.put(GTUtility.copyAmount(1, item), t);
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
                return GGMaterial.zircaloy4.getMolten(aAmount);
            case 4: // EV
                return GGMaterial.incoloy903.getMolten(aAmount);
            case 5: // IV
                return GGMaterial.titaniumBetaC.getMolten(aAmount);
            case 6: // LuV
                return GGMaterial.artheriumSn.getMolten(aAmount);
            case 7: // ZPM
                return GGMaterial.dalisenite.getMolten(aAmount);
            case 8: // UV
                return GGMaterial.tairitsu.getMolten(aAmount);
            case 9: // UHV
                return GGMaterial.preciousMetalAlloy.getMolten(aAmount);
            case 10: // UEV
                return GGMaterial.enrichedNaquadahAlloy.getMolten(aAmount);
            case 11: // UIV
                return GGMaterial.metastableOganesson.getMolten(aAmount);
            case 12: // UMV
                return MaterialsUEVplus.SpaceTime.getMolten(aAmount);
            default:
                return GGMaterial.shirabon.getMolten(aAmount);
        }
    }

    // === Copied from AE2FC to avoid hard dep ===

    public static IAEFluidStack createAEFluidStack(Fluid fluid) {
        return createAEFluidStack(new FluidStack(fluid, FluidContainerRegistry.BUCKET_VOLUME));
    }

    public static IAEFluidStack createAEFluidStack(Fluid fluid, long amount) {
        return createAEFluidStack(fluid.getID(), amount);
    }

    public static IAEFluidStack createAEFluidStack(FluidStack fluid) {
        return AEApi.instance()
            .storage()
            .createFluidStack(fluid);
    }

    public static IAEFluidStack createAEFluidStack(int fluidId, long amount) {
        return createAEFluidStack(new FluidStack(FluidRegistry.getFluid(fluidId), 1)).setStackSize(amount);
    }

}
