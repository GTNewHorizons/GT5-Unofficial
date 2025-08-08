package gregtech.api.util;

import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.scannerFakeRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import java.util.Map;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import forestry.api.recipes.ICentrifugeRecipe;
import forestry.api.recipes.ISqueezerRecipe;
import forestry.api.recipes.RecipeManagers;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.recipe.RecipeMaps;
import gregtech.common.items.CombType;
import gregtech.loaders.misc.GTBees;

public class GTForestryCompat {

    public static void populateFakeNeiRecipes() {
        if (ItemList.FR_Bee_Drone.get(1L) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.FR_Bee_Drone.getWildcard(1L))
                .itemOutputs(ItemList.FR_Bee_Drone.getWithName(1L, "Scanned Drone"))
                .fluidInputs(Materials.Honey.getFluid(100L))
                .duration(25 * SECONDS)
                .eut(2)
                .ignoreCollision()
                .fake()
                .addTo(scannerFakeRecipes);
        }
        if (ItemList.FR_Bee_Princess.get(1L) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.FR_Bee_Princess.getWildcard(1L))
                .itemOutputs(ItemList.FR_Bee_Princess.getWithName(1L, "Scanned Princess"))
                .fluidInputs(Materials.Honey.getFluid(100L))
                .duration(25 * SECONDS)
                .eut(2)
                .ignoreCollision()
                .fake()
                .addTo(scannerFakeRecipes);
        }
        if (ItemList.FR_Bee_Queen.get(1L) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.FR_Bee_Queen.getWildcard(1L))
                .itemOutputs(ItemList.FR_Bee_Queen.getWithName(1L, "Scanned Queen"))
                .fluidInputs(Materials.Honey.getFluid(100L))
                .duration(25 * SECONDS)
                .eut(2)
                .ignoreCollision()
                .fake()
                .addTo(scannerFakeRecipes);
        }
        if (ItemList.FR_Tree_Sapling.get(1L) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.FR_Tree_Sapling.getWildcard(1L))
                .itemOutputs(ItemList.FR_Tree_Sapling.getWithName(1L, "Scanned Sapling"))
                .fluidInputs(Materials.Honey.getFluid(100L))
                .duration(25 * SECONDS)
                .eut(2)
                .ignoreCollision()
                .fake()
                .addTo(scannerFakeRecipes);
        }
        if (ItemList.FR_Butterfly.get(1L) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.FR_Butterfly.getWildcard(1L))
                .itemOutputs(ItemList.FR_Butterfly.getWithName(1L, "Scanned Butterfly"))
                .fluidInputs(Materials.Honey.getFluid(100L))
                .duration(25 * SECONDS)
                .eut(2)
                .ignoreCollision()
                .fake()
                .addTo(scannerFakeRecipes);
        }
        if (ItemList.FR_Larvae.get(1L) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.FR_Larvae.getWildcard(1L))
                .itemOutputs(ItemList.FR_Larvae.getWithName(1L, "Scanned Larvae"))
                .fluidInputs(Materials.Honey.getFluid(100L))
                .duration(25 * SECONDS)
                .eut(2)
                .ignoreCollision()
                .fake()
                .addTo(scannerFakeRecipes);
        }
        if (ItemList.FR_Serum.get(1L) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.FR_Serum.getWildcard(1L))
                .itemOutputs(ItemList.FR_Serum.getWithName(1L, "Scanned Serum"))
                .fluidInputs(Materials.Honey.getFluid(100L))
                .duration(25 * SECONDS)
                .eut(2)
                .ignoreCollision()
                .fake()
                .addTo(scannerFakeRecipes);
        }
        if (ItemList.FR_Caterpillar.get(1L) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.FR_Caterpillar.getWildcard(1L))
                .itemOutputs(ItemList.FR_Caterpillar.getWithName(1L, "Scanned Caterpillar"))
                .fluidInputs(Materials.Honey.getFluid(100L))
                .duration(25 * SECONDS)
                .eut(2)
                .ignoreCollision()
                .fake()
                .addTo(scannerFakeRecipes);
        }
        if (ItemList.FR_PollenFertile.get(1L) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.FR_PollenFertile.getWildcard(1L))
                .itemOutputs(ItemList.FR_PollenFertile.getWithName(1L, "Scanned Pollen"))
                .fluidInputs(Materials.Honey.getFluid(100L))
                .duration(25 * SECONDS)
                .eut(2)
                .ignoreCollision()
                .fake()
                .addTo(scannerFakeRecipes);
        }
    }

    public static void transferCentrifugeRecipes() {
        // Dumb exceptions
        ItemStack irradiatedComb = GTModHandler.getModItem(Mods.Forestry.ID, "beeCombs", 1, 9);
        ItemStack DOBComb = GTBees.combs.getStackForType(CombType.DOB);

        for (ICentrifugeRecipe tRecipe : RecipeManagers.centrifugeManager.recipes()) {
            ItemStack input = tRecipe.getInput();

            // Don't transfer GT recipes to centrifuge, those recipes are made already by ItemComb
            if (input.getUnlocalizedName()
                .contains("gt.comb") && !input.isItemEqual(DOBComb)) continue;
            if (irradiatedComb != null && input.isItemEqual(irradiatedComb)) continue;
            Map<ItemStack, Float> outputs = tRecipe.getAllProducts();
            ItemStack[] tOutputs = new ItemStack[outputs.size()];
            int[] tChances = new int[outputs.size()];
            int i = 0;
            for (Map.Entry<ItemStack, Float> entry : outputs.entrySet()) {
                tChances[i] = (int) (entry.getValue() * 10000);
                tOutputs[i] = entry.getKey()
                    .copy();
                i++;
            }
            GTValues.RA.stdBuilder()
                .itemInputs(tRecipe.getInput())
                .itemOutputs(tOutputs)
                .outputChances(tChances)
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(5)
                .addTo(centrifugeRecipes);
        }
    }

    public static void transferSqueezerRecipes() {
        for (ISqueezerRecipe tRecipe : RecipeManagers.squeezerManager.recipes()) {
            ItemStack[] resources = tRecipe.getResources();
            if ((resources.length == 1) && (tRecipe.getFluidOutput() != null) && (resources[0] != null)) {
                Item input = resources[0].getItem();
                if (input == Items.pumpkin_seeds || input == Items.melon_seeds || input == Items.wheat_seeds) continue;
                GTRecipeBuilder recipeBuilder = GTValues.RA.stdBuilder();
                recipeBuilder.itemInputs(resources[0]);
                if (tRecipe.getRemnants() != null) {
                    recipeBuilder.itemOutputs(tRecipe.getRemnants())
                        .outputChances((int) (tRecipe.getRemnantsChance() * 10000));
                }
                recipeBuilder.fluidOutputs(tRecipe.getFluidOutput())
                    .duration(1 * SECONDS + 12 * TICKS)
                    .eut(8)
                    .addTo(RecipeMaps.fluidExtractionRecipes);
            }
        }
    }
}
