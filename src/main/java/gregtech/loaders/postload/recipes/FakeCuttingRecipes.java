package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.cutterFakeRecipes;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.minecraft.util.StatCollector;
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
            if (recipe.mFakeRecipe) continue;
            if (recipe.mHidden) continue;
            if (recipe.mInputs == null) continue;
            if (recipe.mOutputs == null) continue;

            String key = buildRecipeKey(recipe);
            groups.computeIfAbsent(key, k -> new ArrayList<>())
                .add(recipe);
        }

        for (List<GTRecipe> group : groups.values()) {
            if (group.isEmpty()) continue;

            GTRecipe template = group.get(0);
            List<Integer> durations = new ArrayList<>();
            List<FluidStack> fluids = new ArrayList<>();

            for (GTRecipe recipe : group) {
                mergeFluidVariants(recipe, fluids);
                durations.add(recipe.mDuration);
            }

            String durationInfo = durations.stream()
                .map(
                    t -> new BigDecimal(String.format("%.3f", (double) t / SECONDS)).stripTrailingZeros()
                        .toPlainString())
                .collect(Collectors.joining("/"));

            GTRecipeBuilder builder = GTValues.RA.stdBuilder()
                .itemInputs(template.mInputs)
                .itemOutputs(template.mOutputs)
                .eut(template.mEUt)
                .duration(template.mDuration)
                .setNEIDesc(StatCollector.translateToLocalFormatted("GT5U.gui.text.time_line", durationInfo))
                .fake();

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

    private void mergeFluidVariants(GTRecipe recipe, List<FluidStack> fluids) {

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
    }
}
