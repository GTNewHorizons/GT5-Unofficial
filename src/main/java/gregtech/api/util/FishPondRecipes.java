package gregtech.api.util;

import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import java.lang.reflect.Field;
import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraftforge.common.FishingHooks;

import gregtech.api.enums.GTValues;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.MTEIndustrialFishingPond;

public class FishPondRecipes {

    @SuppressWarnings("unchecked")
    public static void generateFishPondRecipes() {
        try {
            ArrayList<WeightedRandomFishable> fishList = (ArrayList<WeightedRandomFishable>) GTUtility
                .getField(FishingHooks.class, "fish")
                .get(null);
            ArrayList<WeightedRandomFishable> junkList = (ArrayList<WeightedRandomFishable>) GTUtility
                .getField(FishingHooks.class, "junk")
                .get(null);
            ArrayList<WeightedRandomFishable> treasureList = (ArrayList<WeightedRandomFishable>) GTUtility
                .getField(FishingHooks.class, "treasure")
                .get(null);
            final Field stackField = GTUtility.getField(WeightedRandomFishable.class, "field_150711_b");
            generateRecipeForFishable(MTEIndustrialFishingPond.FISH_MODE, fishList, stackField, 0.8);
            generateRecipeForFishable(MTEIndustrialFishingPond.JUNK_MODE, junkList, stackField, 1.35);
            generateRecipeForFishable(MTEIndustrialFishingPond.TREASURE_MODE, treasureList, stackField, 20D);
        } catch (Exception e) {
            Logger.INFO("Error reading the vanilla fishing loot table.");
            e.printStackTrace();
        }
    }

    private static void generateRecipeForFishable(int circuitType, ArrayList<WeightedRandomFishable> lootTable,
        Field stackField, double chanceMultiplier) {
        int[] chances = new int[lootTable.size()];
        ItemStack[] outputs = new ItemStack[lootTable.size()];

        for (int i = 0; i < lootTable.size(); i++) {
            chances[i] = (int) (lootTable.get(i).itemWeight * 100 * chanceMultiplier);
            try {
                outputs[i] = (ItemStack) stackField.get(lootTable.get(i));
            } catch (IllegalArgumentException | IllegalAccessException e1) {
                Logger.INFO("Error generating Fish Pond Recipes");
                e1.printStackTrace();
            }
        }

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(circuitType))
            .itemOutputs(outputs, chances)
            .duration(10 * SECONDS)
            .eut(16)
            .ignoreCollision()
            .addTo(GTPPRecipeMaps.fishPondRecipes);
    }
}
