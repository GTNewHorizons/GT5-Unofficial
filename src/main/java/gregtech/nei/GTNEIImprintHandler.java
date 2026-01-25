package gregtech.nei;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import bartworks.API.enums.CircuitImprint;
import bartworks.common.loaders.ItemRegistry;
import codechicken.nei.recipe.ShapelessRecipeHandler;
import gregtech.api.util.GTUtility;
import gregtech.common.recipes.CALImprintRecipe;

public class GTNEIImprintHandler extends ShapelessRecipeHandler {

    @Override
    public String getOverlayIdentifier() {
        return "gt.recipe.cal-imprinting";
    }

    @Override
    public String getRecipeTabName() {
        return getRecipeName();
    }

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal(getOverlayIdentifier());
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("crafting") || outputId.equals(getOverlayIdentifier())) {
            loadAllRecipes(null);
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        CircuitImprint circuitImprint = CALImprintRecipe.getCircuitImprintFromCAL(result);

        if (circuitImprint != null) {
            loadRecipe(null, circuitImprint);
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        if (CALImprintRecipe.isCAL(ingredient) && CALImprintRecipe.getCircuitImprintFromCAL(ingredient) == null) {
            loadAllRecipes(GTUtility.copyAmount(1, ingredient));
        }

        CircuitImprint circuitImprint = CircuitImprint.findCircuitImprintByImprintStack(ingredient);
        if (circuitImprint != null) {
            loadRecipe(null, circuitImprint);
        }
    }

    private void loadAllRecipes(@Nullable ItemStack cal) {
        for (CircuitImprint entry : CircuitImprint.values()) {
            if (entry.sourceMod.isModLoaded()) loadRecipe(cal, entry); // Avoid the handler to crash in dev
        }
    }

    private void loadRecipe(@Nullable ItemStack cal, @NotNull CircuitImprint circuitImprint) {
        ItemStack inputCAL = GTUtility.copyAmount(1, (cal == null) ? ItemRegistry.cal : cal);
        ItemStack outputCAL = CALImprintRecipe.installImprint(inputCAL, circuitImprint);

        arecipes.add(new CachedShapelessRecipe(new Object[] { inputCAL, circuitImprint.imprint.get(1) }, outputCAL));
    }
}
