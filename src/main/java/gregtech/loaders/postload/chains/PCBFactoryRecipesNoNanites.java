package gregtech.loaders.postload.chains;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.metadata.PCBFactoryTierKey;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTRecipeConstants;

public class PCBFactoryRecipesNoNanites {

    private static final PCBFactoryTierKey TIER = PCBFactoryTierKey.INSTANCE;

    public static void load() {
        for (GTRecipe recipe : RecipeMaps.pcbFactoryRecipes.getAllRecipes()) {
            List<ItemStack> itemInputs = new ArrayList<>();

            Materials naniteMaterial = null;
            for (int i = 0; i < recipe.mInputs.length; i++) {
                ItemStack stack = recipe.getRepresentativeInput(i);
                if (stack == null) continue;
                ItemData data = GTOreDictUnificator.getAssociation(stack);
                if (data != null && data.mPrefix != null && data.mPrefix.equals(OrePrefixes.nanite)) {
                    naniteMaterial = data.mMaterial.mMaterial;
                    continue;
                }
                itemInputs.add(stack);
            }

            GTRecipeBuilder builder = GTValues.RA.stdBuilder()
                .noOptimize()
                .itemInputs(itemInputs.toArray(new ItemStack[0]))
                .itemOutputs(recipe.mOutputs)
                .fluidInputs(recipe.mFluidInputs)
                .duration(recipe.mDuration)
                .eut(recipe.mEUt)
                .hidden()
                .metadata(TIER, recipe.getMetadata(TIER));

            if (naniteMaterial != null) {
                builder.metadata(GTRecipeConstants.PCB_NANITE_MATERIAL, naniteMaterial);
            }
            builder.addTo(RecipeMaps.pcbFactoryRecipesNoNanites);
        }
    }
}
