package gtPlusPlus.xmod.tinkers.util;

import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.gtnhlib.item.ItemStackNBT;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;
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
    }

    public static void generateAllDryingRecipes() {
        List<DryingRackRecipes.DryingRecipe> aRecipes = TinkersDryingRecipe.getDryingRecipes();
        if (!aRecipes.isEmpty()) {
            for (DryingRackRecipes.DryingRecipe recipe : aRecipes) {
                generateFromTinkersRecipe(recipe);
            }
        }
        if (!recipes.isEmpty()) {
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
        return new ArrayList<>(DryingRackRecipes.recipes);
    }

    public boolean matches(ItemStack stack) {
        if (stack.hasTagCompound()) {
            stack = stack.copy();
            ItemStackNBT.removeTag(stack, "frypanKill");
        }
        return ItemStack.areItemStacksEqual(this.input, stack);
    }

    public ItemStack getResult() {
        return this.result.copy();
    }
}
