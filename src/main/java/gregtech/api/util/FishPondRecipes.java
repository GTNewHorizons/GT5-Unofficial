package gregtech.api.util;

import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import java.util.ArrayList;

import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraftforge.common.FishingHooks;

import gregtech.api.enums.GTValues;
import gregtech.mixin.interfaces.accessors.WeightedRandomFishableAccessor;
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
            generateRecipes(MTEIndustrialFishingPond.FISH_MODE, fishList, 0.85);
            generateRecipes(MTEIndustrialFishingPond.JUNK_MODE, junkList, 1.35);
            generateRecipes(MTEIndustrialFishingPond.TREASURE_MODE, treasureList, 20D);
        } catch (Exception e) {
            Logger.INFO("Error reading the vanilla fishing loot table.");
            e.printStackTrace();
        }
    }

    /**
     * @param circuitType      Programmed Circuit for the recipe
     * @param lootTable        Fishing loot table values and items are drawn from
     * @param chanceMultiplier Arbitrary multiplier to the whole table of chances for balancing purposes
     */
    private static void generateRecipes(int circuitType, ArrayList<WeightedRandomFishable> lootTable,
        double chanceMultiplier) {
        int[] chances = new int[lootTable.size()];
        ItemStack[] outputs = new ItemStack[lootTable.size()];
        for (int i = 0; i < lootTable.size(); i++) {
            final WeightedRandomFishable fishable = lootTable.get(i);
            chances[i] = (int) (fishable.itemWeight * 100 * chanceMultiplier);
            ItemStack output = ((WeightedRandomFishableAccessor) fishable).gt5u$getLoot();
            // Pufferfish check to keep Poisonous Brew outputs the same between the old implementation and current
            if (output.getItem() instanceof ItemFishFood) {
                ItemFishFood.FishType fishtype = ItemFishFood.FishType.func_150978_a(output);
                if (fishtype == ItemFishFood.FishType.PUFFERFISH) {
                    chances[i] = chances[i] * 3;
                }
            }
            outputs[i] = output;
        }

        GTValues.RA.stdBuilder()
            .circuit(circuitType)
            .itemOutputs(outputs, chances)
            .duration(10 * SECONDS)
            .eut(16)
            .ignoreCollision()
            .addTo(GTPPRecipeMaps.fishPondRecipes);
    }
}
