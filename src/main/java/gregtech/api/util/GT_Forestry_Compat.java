package gregtech.api.util;

import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import forestry.api.recipes.ICentrifugeRecipe;
import forestry.api.recipes.ISqueezerRecipe;
import forestry.api.recipes.RecipeManagers;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.recipe.RecipeMaps;

public class GT_Forestry_Compat {

    public static void populateFakeNeiRecipes() {
        if (ItemList.FR_Bee_Drone.get(1L) != null) {
            RecipeMaps.scannerFakeRecipes.addFakeRecipe(
                false,
                new ItemStack[] { ItemList.FR_Bee_Drone.getWildcard(1L) },
                new ItemStack[] { ItemList.FR_Bee_Drone.getWithName(1L, "Scanned Drone") },
                null,
                new FluidStack[] { Materials.Honey.getFluid(100L) },
                null,
                500,
                2,
                0);
        }
        if (ItemList.FR_Bee_Princess.get(1L) != null) {
            RecipeMaps.scannerFakeRecipes.addFakeRecipe(
                false,
                new ItemStack[] { ItemList.FR_Bee_Princess.getWildcard(1L) },
                new ItemStack[] { ItemList.FR_Bee_Princess.getWithName(1L, "Scanned Princess") },
                null,
                new FluidStack[] { Materials.Honey.getFluid(100L) },
                null,
                500,
                2,
                0);
        }
        if (ItemList.FR_Bee_Queen.get(1L) != null) {
            RecipeMaps.scannerFakeRecipes.addFakeRecipe(
                false,
                new ItemStack[] { ItemList.FR_Bee_Queen.getWildcard(1L) },
                new ItemStack[] { ItemList.FR_Bee_Queen.getWithName(1L, "Scanned Queen") },
                null,
                new FluidStack[] { Materials.Honey.getFluid(100L) },
                null,
                500,
                2,
                0);
        }
        if (ItemList.FR_Tree_Sapling.get(1L) != null) {
            RecipeMaps.scannerFakeRecipes.addFakeRecipe(
                false,
                new ItemStack[] { ItemList.FR_Tree_Sapling.getWildcard(1L) },
                new ItemStack[] { ItemList.FR_Tree_Sapling.getWithName(1L, "Scanned Sapling") },
                null,
                new FluidStack[] { Materials.Honey.getFluid(100L) },
                null,
                500,
                2,
                0);
        }
        if (ItemList.FR_Butterfly.get(1L) != null) {
            RecipeMaps.scannerFakeRecipes.addFakeRecipe(
                false,
                new ItemStack[] { ItemList.FR_Butterfly.getWildcard(1L) },
                new ItemStack[] { ItemList.FR_Butterfly.getWithName(1L, "Scanned Butterfly") },
                null,
                new FluidStack[] { Materials.Honey.getFluid(100L) },
                null,
                500,
                2,
                0);
        }
        if (ItemList.FR_Larvae.get(1L) != null) {
            RecipeMaps.scannerFakeRecipes.addFakeRecipe(
                false,
                new ItemStack[] { ItemList.FR_Larvae.getWildcard(1L) },
                new ItemStack[] { ItemList.FR_Larvae.getWithName(1L, "Scanned Larvae") },
                null,
                new FluidStack[] { Materials.Honey.getFluid(100L) },
                null,
                500,
                2,
                0);
        }
        if (ItemList.FR_Serum.get(1L) != null) {
            RecipeMaps.scannerFakeRecipes.addFakeRecipe(
                false,
                new ItemStack[] { ItemList.FR_Serum.getWildcard(1L) },
                new ItemStack[] { ItemList.FR_Serum.getWithName(1L, "Scanned Serum") },
                null,
                new FluidStack[] { Materials.Honey.getFluid(100L) },
                null,
                500,
                2,
                0);
        }
        if (ItemList.FR_Caterpillar.get(1L) != null) {
            RecipeMaps.scannerFakeRecipes.addFakeRecipe(
                false,
                new ItemStack[] { ItemList.FR_Caterpillar.getWildcard(1L) },
                new ItemStack[] { ItemList.FR_Caterpillar.getWithName(1L, "Scanned Caterpillar") },
                null,
                new FluidStack[] { Materials.Honey.getFluid(100L) },
                null,
                500,
                2,
                0);
        }
        if (ItemList.FR_PollenFertile.get(1L) != null) {
            RecipeMaps.scannerFakeRecipes.addFakeRecipe(
                false,
                new ItemStack[] { ItemList.FR_PollenFertile.getWildcard(1L) },
                new ItemStack[] { ItemList.FR_PollenFertile.getWithName(1L, "Scanned Pollen") },
                null,
                new FluidStack[] { Materials.Honey.getFluid(100L) },
                null,
                500,
                2,
                0);
        }
    }

    public static void transferCentrifugeRecipes() {
        try {
            for (ICentrifugeRecipe tRecipe : RecipeManagers.centrifugeManager.recipes()) {
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
                RecipeMaps.centrifugeRecipes.addRecipe(
                    true,
                    new ItemStack[] { tRecipe.getInput() },
                    tOutputs,
                    null,
                    tChances,
                    null,
                    null,
                    128,
                    5,
                    0);
                RecipeMaps.centrifugeNonCellRecipes.addRecipe(
                    true,
                    new ItemStack[] { tRecipe.getInput() },
                    tOutputs,
                    null,
                    tChances,
                    null,
                    null,
                    128,
                    5,
                    0);
            }
        } catch (Throwable e) {
            if (GT_Values.D1) {
                e.printStackTrace(GT_Log.err);
            }
        }
    }

    public static void transferSqueezerRecipes() {
        try {
            for (ISqueezerRecipe tRecipe : RecipeManagers.squeezerManager.recipes()) {
                if ((tRecipe.getResources().length == 1) && (tRecipe.getFluidOutput() != null)) {
                    RecipeMaps.fluidExtractionRecipes.addRecipe(
                        true,
                        new ItemStack[] { tRecipe.getResources()[0] },
                        new ItemStack[] { tRecipe.getRemnants() },
                        null,
                        new int[] { (int) (tRecipe.getRemnantsChance() * 10000) },
                        null,
                        new FluidStack[] { tRecipe.getFluidOutput() },
                        32,
                        8,
                        0);
                }
            }
        } catch (Throwable e) {
            if (GT_Values.D1) {
                e.printStackTrace(GT_Log.err);
            }
        }
    }
}
