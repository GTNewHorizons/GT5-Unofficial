package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.cutterFakeRecipes;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.objects.SubstituteFluidStack;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;

public class FakeCuttingRecipes implements Runnable {

    @Override
    public void run() {

        Collection<GTRecipe> recipes = cutterRecipes.getBackend()
            .getAllRecipes();
        Map<String, List<GTRecipe>> groups = new LinkedHashMap<>();

        for (GTRecipe recipe : recipes) {
            if (!recipe.mEnabled) continue;
            if (recipe.mFakeRecipe) continue;
            if (recipe.mHidden) continue;
            if (recipe.mInputs == null) continue;
            if (recipe.mOutputs == null) continue;

            String key = buildGroupingKey(recipe);
            groups.computeIfAbsent(key, k -> new ArrayList<>())
                .add(recipe);
        }

        for (List<GTRecipe> group : groups.values()) {
            if (group.isEmpty()) continue;

            int maxDuration = 0;
            GTRecipe base = group.get(0);
            List<FluidStack> fluids = new ArrayList<>();

            for (GTRecipe recipe : group) {
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

                    if (!isDuplicate) fluids.add(candidate);
                }

                maxDuration = Math.max(maxDuration, recipe.mDuration);
            }

            if (fluids.size() <= 1) {
                GTValues.RA.stdBuilder()
                    .itemInputs(base.mInputs)
                    .itemOutputs(base.mOutputs)
                    .fluidInputs(fluids.isEmpty() ? new FluidStack[0] : new FluidStack[] { fluids.get(0) })
                    .eut(base.mEUt)
                    .duration(maxDuration)
                    .fake()
                    .addTo(cutterFakeRecipes);
                continue;
            }

            GTValues.RA.stdBuilder()
                .itemInputs(base.mInputs)
                .itemOutputs(base.mOutputs)
                .fluidInputs(new SubstituteFluidStack(fluids.toArray(new FluidStack[0])))
                .eut(base.mEUt)
                .duration(maxDuration)
                .fake()
                .addTo(cutterFakeRecipes);
        }
    }

    private static String buildGroupingKey(GTRecipe recipe) {
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
}
