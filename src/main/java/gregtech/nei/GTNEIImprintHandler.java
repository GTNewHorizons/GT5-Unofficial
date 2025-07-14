package gregtech.nei;

import java.util.HashSet;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import bartworks.API.recipe.BartWorksRecipeMaps;
import bartworks.common.loaders.ItemRegistry;
import codechicken.nei.recipe.ShapelessRecipeHandler;
import gregtech.api.util.GTRecipe;
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
        ItemStack circuit = CALImprintRecipe.getCircuitFromCAL(result);

        if (circuit != null) {
            loadRecipe(null, CALImprintRecipe.getImprintForCircuit(circuit));
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        if (CALImprintRecipe.isCAL(ingredient) && CALImprintRecipe.getCircuitFromCAL(ingredient) == null) {
            loadAllRecipes(ingredient);
        }

        if (CALImprintRecipe.getCircuitFromImprint(ingredient) != null) {
            loadRecipe(null, ingredient);
        }
    }

    private void loadAllRecipes(@Nullable ItemStack cal) {
        HashSet<GTUtility.ItemId> addedImprints = new HashSet<>();

        for (GTRecipe recipe : BartWorksRecipeMaps.circuitAssemblyLineRecipes.getAllRecipes()) {
            ItemStack imprint = (ItemStack) recipe.mSpecialItems;
            ItemStack circuit = CALImprintRecipe.getCircuitFromImprint(imprint);

            if (imprint == null) continue;
            if (circuit == null) continue;

            if (!addedImprints.add(GTUtility.ItemId.create(circuit))) continue;

            loadRecipe(cal, imprint);
        }
    }

    private void loadRecipe(@Nullable ItemStack cal, @NotNull ItemStack imprint) {
        if (cal == null) cal = ItemRegistry.cal;

        arecipes.add(
            new CachedShapelessRecipe(
                new Object[] { GTUtility.copyAmount(1, cal), GTUtility.copyAmount(1, imprint), },
                CALImprintRecipe.installImprint(GTUtility.copyAmount(1, cal), imprint)));
    }
}
