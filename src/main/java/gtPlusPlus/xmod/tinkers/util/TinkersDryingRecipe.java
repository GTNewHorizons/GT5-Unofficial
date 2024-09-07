package gtPlusPlus.xmod.tinkers.util;

import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTUtility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import tconstruct.library.crafting.DryingRackRecipes;

public class TinkersDryingRecipe {

    public static AutoMap<TinkersDryingRecipe> recipes = new AutoMap<>();

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
                    .itemInputs(GTUtility.getIntegratedCircuit(16), r.input)
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
        AutoMap<DryingRackRecipes.DryingRecipe> aData = new AutoMap<>();
        int aCount = 0;
        for (DryingRackRecipes.DryingRecipe recipe : DryingRackRecipes.recipes) {
            aData.put(recipe);
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
                input.setTagCompound((NBTTagCompound) null);
            }
        }
        return ItemStack.areItemStacksEqual(this.input, input);
    }

    public ItemStack getResult() {
        return this.result.copy();
    }
}
