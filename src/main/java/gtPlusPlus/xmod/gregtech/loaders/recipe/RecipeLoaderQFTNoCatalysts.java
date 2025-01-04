package gtPlusPlus.xmod.gregtech.loaders.recipe;

import static gregtech.api.util.GTRecipeConstants.QFT_CATALYST_META;
import static gregtech.api.util.GTRecipeConstants.QFT_FOCUS_TIER;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.quantumForceTransformerRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.quantumForceTransformerRecipesNoCatalysts;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeBuilder;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class RecipeLoaderQFTNoCatalysts {

    public static void generate() {
        for (GTRecipe recipe : quantumForceTransformerRecipes.getAllRecipes()) {
            List<ItemStack> itemInputs = new ArrayList<>();

            int meta = -1;
            for (int i = 0; i < recipe.mInputs.length; i++) {
                ItemStack stack = recipe.getRepresentativeInput(i);
                if (stack == null) continue;
                if (ItemUtils.isCatalyst(stack)) {
                    meta = stack.getItemDamage();
                    continue;
                }
                itemInputs.add(stack);
            }

            GTRecipeBuilder builder = GTValues.RA.stdBuilder()
                .noOptimize()
                .itemInputs(itemInputs.toArray(new ItemStack[0]))
                .itemOutputs(recipe.mOutputs)
                .fluidInputs(recipe.mFluidInputs)
                .fluidOutputs(recipe.mFluidOutputs)
                .duration(recipe.mDuration)
                .eut(recipe.mEUt)
                .hidden()
                .metadata(QFT_FOCUS_TIER, recipe.getMetadata(QFT_FOCUS_TIER));

            if (meta != -1) {
                builder.metadata(QFT_CATALYST_META, meta);
            }
            builder.addTo(quantumForceTransformerRecipesNoCatalysts);
        }
    }
}
