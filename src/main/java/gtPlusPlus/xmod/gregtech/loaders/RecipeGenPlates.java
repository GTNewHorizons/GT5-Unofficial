package gtPlusPlus.xmod.gregtech.loaders;

import static gregtech.api.recipe.RecipeMaps.cutterRecipes;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;

/// Block-to-plate cutter recipes for gtPlusPlus-reconstructed materials. The forge hammer, bender,
/// alloy smelter, and compressor plate/foil-shaping recipes formerly generated here (including the
/// foil decorative cover) are covered by the canonical autogen (`ProcessingPlate`/`ProcessingFoil`,
/// dispatched by `gregtech.loaders.oreprocessing`). The remaining `block -> plate9` cutter recipe is
/// also produced by `ProcessingBlock` for the same materials, under a mass-scaled fluid-amount
/// formula rather than this class's fixed amounts, so it is a replacement candidate rather than a
/// retired duplicate.
public class RecipeGenPlates extends RecipeGenBase {

    public static final Set<Runnable> mRecipeGenMap = new HashSet<>();

    static {
        MaterialGenerator.mRecipeMapsToGenerate.add(mRecipeGenMap);
    }

    public RecipeGenPlates(final Material M) {
        this.toGenerate = M;
        mRecipeGenMap.add(this);
    }

    @Override
    public void run() {
        generateRecipes(this.toGenerate);
    }

    private void generateRecipes(final Material material) {

        final ItemStack block = material.getBlock(1);
        final ItemStack plate_SingleNine = material.getPlate(9);

        if (block != null && plate_SingleNine != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(block)
                .itemOutputs(plate_SingleNine)
                .duration(Math.max(material.getMass() * 10L, 1L))
                .eut(material.vVoltageMultiplier)
                .addTo(cutterRecipes);
        }
    }
}
