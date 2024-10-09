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

public class FishPondFakeRecipe {

    public static final ArrayList<ItemStack> fish = new ArrayList<>();
    public static final ArrayList<ItemStack> junk = new ArrayList<>();
    public static final ArrayList<ItemStack> treasure = new ArrayList<>();

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
            generateRecipesFor(MTEIndustrialFishingPond.FISH_MODE, fish, fishList, stackField);
            generateRecipesFor(MTEIndustrialFishingPond.JUNK_MODE, junk, junkList, stackField);
            generateRecipesFor(MTEIndustrialFishingPond.TREASURE_MODE, treasure, treasureList, stackField);
        } catch (Exception e) {
            Logger.INFO("Error reading the vanilla fishing loot table.");
            e.printStackTrace();
        }
    }

    private static void generateRecipesFor(int circuitType, ArrayList<ItemStack> listToFill,
        ArrayList<WeightedRandomFishable> lootTable, Field stackField) {
        for (WeightedRandomFishable fishable : lootTable) {
            try {
                ItemStack stack = (ItemStack) stackField.get(fishable);
                listToFill.add(stack.copy());
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.getIntegratedCircuit(circuitType))
                    .itemOutputs(stack)
                    .duration(5 * SECONDS)
                    .eut(0)
                    .ignoreCollision()
                    .addTo(GTPPRecipeMaps.fishPondRecipes);
            } catch (IllegalArgumentException | IllegalAccessException e1) {
                Logger.INFO("Error generating Fish Pond Recipes");
                e1.printStackTrace();
            }
        }
        listToFill.trimToSize();
    }
}
