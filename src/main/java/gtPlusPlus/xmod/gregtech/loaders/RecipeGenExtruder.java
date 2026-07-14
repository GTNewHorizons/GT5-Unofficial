package gtPlusPlus.xmod.gregtech.loaders;

import static gregtech.api.recipe.RecipeMaps.extruderRecipes;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;

/// Block-to-ingot extruder recipes for gtPlusPlus-reconstructed materials. The block, plate, ring,
/// gear, small gear, rod, bolt, and rotor extruder recipes formerly generated here are covered by
/// the canonical autogen (`ProcessingShaping`, dispatched by `gregtech.loaders.oreprocessing`),
/// which does not itself provide a `block -> ingot` decompression path, so that recipe remains here.
public class RecipeGenExtruder extends RecipeGenBase {

    public static final Set<Runnable> mRecipeGenMap = new HashSet<>();

    static {
        MaterialGenerator.mRecipeMapsToGenerate.add(mRecipeGenMap);
    }

    public RecipeGenExtruder(final Material M) {
        this.toGenerate = M;
        mRecipeGenMap.add(this);
    }

    @Override
    public void run() {
        generateRecipes(this.toGenerate);
    }

    private void generateRecipes(final Material material) {

        final ItemStack shape_Ingot = ItemList.Shape_Extruder_Ingot.get(0);

        if (material.getIngot(1) != null && material.getBlock(1) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(material.getBlock(1), shape_Ingot)
                .itemOutputs(material.getIngot(9))
                .duration((int) Math.max(material.getMass() * 2L * 1, 1))
                .eut(material.vVoltageMultiplier)
                .addTo(extruderRecipes);
        }
    }
}
