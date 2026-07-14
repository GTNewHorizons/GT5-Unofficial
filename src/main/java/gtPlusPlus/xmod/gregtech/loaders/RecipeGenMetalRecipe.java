package gtPlusPlus.xmod.gregtech.loaders;

import static gregtech.api.recipe.RecipeMaps.cutterRecipes;

import java.util.HashSet;
import java.util.Set;

import gregtech.api.enums.GTValues;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;

/// Rod-to-bolt cutter recipes for gtPlusPlus-reconstructed materials. The ingot-to-block compressor,
/// ingot-to-rod and bolt-to-screw lathe, and rod/long-rod hammer and cutter recipes formerly
/// generated here are covered by the canonical autogen (`ProcessingStick`, dispatched by
/// `gregtech.loaders.oreprocessing`). The remaining `rod -> bolt` cutter recipe below is also
/// produced by `ProcessingStick` for the same materials, under a mass-scaled fluid-amount formula
/// rather than this class's fixed amounts, so it is a replacement candidate rather than a retired
/// duplicate.
public class RecipeGenMetalRecipe extends RecipeGenBase {

    public static final Set<Runnable> mRecipeGenMap = new HashSet<>();

    static {
        MaterialGenerator.mRecipeMapsToGenerate.add(mRecipeGenMap);
    }

    public RecipeGenMetalRecipe(final Material M) {
        this.toGenerate = M;
        mRecipeGenMap.add(this);
    }

    @Override
    public void run() {
        generateRecipes(this.toGenerate);
    }

    private void generateRecipes(final Material material) {

        if (material.getRod(1) != null && material.getBolt(1) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(material.getRod(1))
                .itemOutputs(material.getBolt(4))
                .duration(Math.max(material.getMass() * 2L, 1L))
                .eut(material.vVoltageMultiplier)
                .addTo(cutterRecipes);
        }
    }
}
