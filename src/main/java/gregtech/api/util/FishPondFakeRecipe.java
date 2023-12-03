package gregtech.api.util;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraftforge.common.FishingHooks;
import net.minecraftforge.fluids.FluidStack;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;

public class FishPondFakeRecipe {

    public static ArrayList<WeightedRandomFishable> fish = new ArrayList<>();
    public static ArrayList<WeightedRandomFishable> junk = new ArrayList<>();
    public static ArrayList<WeightedRandomFishable> treasure = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public static boolean generateFishPondRecipes() {

        try {
            fish = (ArrayList<WeightedRandomFishable>) ReflectionUtils.getField(FishingHooks.class, "fish").get(null);
            junk = (ArrayList<WeightedRandomFishable>) ReflectionUtils.getField(FishingHooks.class, "junk").get(null);
            treasure = (ArrayList<WeightedRandomFishable>) ReflectionUtils.getField(FishingHooks.class, "treasure")
                    .get(null);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            Logger.INFO("Error generating Fish Pond Recipes. [1]");
            e.printStackTrace();
        }

        AutoMap<ArrayList<WeightedRandomFishable>> mega = new AutoMap<>();
        mega.put(fish);
        mega.put(junk);
        mega.put(treasure);

        int mType = 14;
        for (ArrayList<WeightedRandomFishable> f : mega.values()) {
            for (WeightedRandomFishable weightedRandomFishable : f) {
                if (weightedRandomFishable != null) {
                    WeightedRandomFishable u = weightedRandomFishable;
                    try {
                        ItemStack t = (ItemStack) ReflectionUtils
                                .getField(WeightedRandomFishable.class, "field_150711_b").get(u);
                        addNewFishPondLoot(mType, new ItemStack[] { t }, new int[] { 10000 });
                    } catch (IllegalArgumentException | IllegalAccessException e1) {
                        Logger.INFO("Error generating Fish Pond Recipes. [2]");
                        e1.printStackTrace();
                    }
                }
            }
            mType++;
        }

        return true;
    }

    public static void addNewFishPondLoot(int circuit, ItemStack[] outputItems, int[] chances) {
        GT_Recipe x = new GT_Recipe(
                true,
                new ItemStack[] { CI.getNumberedCircuit(circuit) },
                outputItems,
                null,
                chances,
                new FluidStack[] { null },
                new FluidStack[] { null },
                100, // 1 Tick
                0, // No Eu produced
                0);
        Logger.INFO("Fishing [" + circuit + "]: " + ItemUtils.getArrayStackNames(outputItems));
        GTPPRecipeMaps.fishPondRecipes.addRecipe(x, false, false, false);
    }
}
