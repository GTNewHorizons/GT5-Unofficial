package gtPlusPlus.xmod.tinkers.util;

import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;
import gtPlusPlus.api.objects.Logger;
import tconstruct.library.crafting.DryingRackRecipes;

public class TinkersDryingRecipe {

    public static ArrayList<TinkersDryingRecipe> recipes = new ArrayList<>();

    public final int time;
    public final ItemStack input;
    public final ItemStack result;

    public TinkersDryingRecipe(final ItemStack input, final int time, final ItemStack result) {
        this.time = time;
        this.input = input;
        this.result = result;
        Logger.INFO(
            "Generating Drying Recipe. Input: " + input.getDisplayName() + ", Output: " + result.getDisplayName());
    }

    public static void generateAllDryingRecipes() {
        List<DryingRackRecipes.DryingRecipe> aRecipes = TinkersDryingRecipe.getDryingRecipes();
        if (!aRecipes.isEmpty()) {
            for (DryingRackRecipes.DryingRecipe recipe : aRecipes) {
                Logger.INFO(
                    "Trying to generate recipe using object of type " + recipe.getClass()
                        .getSimpleName());
                generateFromTinkersRecipe(recipe);
            }
        } else {
            Logger.INFO("Error generating Drying recipes, map was empty. Size: " + aRecipes.size());
        }
        if (!recipes.isEmpty()) {
            Logger.INFO("Adding " + recipes.size() + " drying rack recipes to the dehydrator.");
            for (TinkersDryingRecipe r : recipes) {
                GTValues.RA.stdBuilder()
                    .itemInputs(r.input)
                    .circuit(16)
                    .itemOutputs(r.result)
                    .eut(TierEU.RECIPE_LV)
                    .duration(r.time / 10)
                    .addTo(chemicalDehydratorRecipes);
            }
        }
    }

    private static void generateFromTinkersRecipe(DryingRackRecipes.DryingRecipe recipe) {
        recipes.add(new TinkersDryingRecipe(recipe.input, recipe.time, recipe.result));
    }

    private static List<DryingRackRecipes.DryingRecipe> getDryingRecipes() {
        ArrayList<DryingRackRecipes.DryingRecipe> aData = new ArrayList<>();
        int aCount = 0;
        for (DryingRackRecipes.DryingRecipe recipe : DryingRackRecipes.recipes) {
            aData.add(recipe);
            aCount++;
        }
        Logger.INFO("Found " + aCount + " Tinkers drying rack recipes.");
        return aData;
    }

    public boolean matches(ItemStack input) {
        if (input.hasTagCompound()) {
            input = input.copy();
            input.getTagCompound()
                .removeTag("frypanKill");
            if (input.getTagCompound()
                .hasNoTags()) {
                input.setTagCompound(null);
            }
        }
        return ItemStack.areItemStacksEqual(this.input, input);
    }

    public ItemStack getResult() {
        return this.result.copy();
    }
}
