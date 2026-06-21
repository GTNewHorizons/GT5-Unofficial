package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.cutterFakeRecipes;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import gregtech.api.recipe.RecipeMetadataKey;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.objects.SubstituteFluidStack;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;

public class FakeCuttingRecipes implements Runnable {

    @Override
    public void run() {

        Collection<GTRecipe> recipes = cutterRecipes.getBackend()
            .getAllRecipes();
        Map<String, List<GTRecipe>> groups = new LinkedHashMap<>();

        for (GTRecipe recipe : recipes) {
            if (!recipe.mEnabled) continue;
            if (recipe.mHidden) continue;
            if (recipe.mInputs == null) continue;
            if (recipe.mOutputs == null) continue;

            String key = buildRecipeKey(recipe);
            groups.computeIfAbsent(key, k -> new ArrayList<>())
                .add(recipe);
        }

        for (List<GTRecipe> group : groups.values()) {

            GTRecipe template = group.getFirst();
            List<FluidStack> fluids = new ArrayList<>();
            Map<RecipeMetadataKey<?>, Object> metadata = new LinkedHashMap<>();
            FakeCuttingSpecialInfo specialInfo = new FakeCuttingSpecialInfo();

            for (GTRecipe recipe : group) {
                mergeFluidVariants(recipe, fluids, specialInfo);
                mergeRecipeMetadata(recipe, metadata);
            }

            GTRecipeBuilder builder = GTValues.RA.stdBuilder()
                .itemInputs(template.mInputs)
                .itemOutputs(template.mOutputs)
                .eut(template.mEUt)
                .duration(template.mDuration)
                .special(specialInfo)
                .fake();

            for (Map.Entry<RecipeMetadataKey<?>, Object> entry : metadata.entrySet()) {
                @SuppressWarnings("unchecked")
                RecipeMetadataKey<Object> key = (RecipeMetadataKey<Object>) entry.getKey();
                builder.metadata(key, entry.getValue());
            }

            if (!fluids.isEmpty()) {
                builder.fluidInputs(new SubstituteFluidStack(fluids.toArray(new FluidStack[0])));
            }

            builder.addTo(cutterFakeRecipes);
        }
    }

    private static String buildRecipeKey(GTRecipe recipe) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < recipe.mInputs.length; i++) {
            sb.append(":IN")
                .append(i)
                .append(':')
                .append(GTUtility.persistentHash(recipe.mInputs[i], true, true));
        }

        for (int i = 0; i < recipe.mOutputs.length; i++) {
            sb.append(":OUT")
                .append(i)
                .append(':')
                .append(GTUtility.persistentHash(recipe.mOutputs[i], true, true));
        }

        return sb.toString();
    }

    private void mergeFluidVariants(GTRecipe recipe, List<FluidStack> fluids, FakeCuttingSpecialInfo specialInfo) {

        List<FluidStack> candidates = new ArrayList<>();

        if (recipe.mFluidInputs.length > 0 && recipe.mFluidInputs[0] != null) {
            candidates.add(recipe.mFluidInputs[0]);
        }

        if (recipe.mAltFluidInputs != null) {
            for (FluidStack[] alt : recipe.mAltFluidInputs) {
                Collections.addAll(candidates, alt);
            }
        }

        for (FluidStack candidate : candidates) {
            boolean isDuplicate = fluids.stream()
                .anyMatch(f -> f.isFluidStackIdentical(candidate));

            if (!isDuplicate) {
                fluids.add(candidate);
                specialInfo.add(candidate, recipe.mDuration);
            }
        }
    }

    // Surely no one will add "Liquid Cleanroom skipper 9000" right?
    private void mergeRecipeMetadata(GTRecipe recipe, Map<RecipeMetadataKey<?>, Object> metadata) {
        for (Map.Entry<RecipeMetadataKey<?>, Object> entry : recipe.getMetadataStorage().getEntries()) {
            @SuppressWarnings("unchecked")
            RecipeMetadataKey<Object> key = (RecipeMetadataKey<Object>) entry.getKey();
            metadata.put(key, entry.getValue());
        }
    }
}
