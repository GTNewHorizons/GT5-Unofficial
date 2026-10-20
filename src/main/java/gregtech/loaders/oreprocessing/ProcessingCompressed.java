package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.implosionRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.ADDITIVE_AMOUNT;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.GTMaterialIcons;
import gregtech.api.material.GTMaterialTextures;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

/// Generates the recipes of another mod's compressed plate. The plate's own recipes are removed, it becomes a
/// decorative cover, and it is made by imploding the material's triple plate.
public class ProcessingCompressed implements PartRecipeGenerator {

    public static final ProcessingCompressed INSTANCE = new ProcessingCompressed();

    private ProcessingCompressed() {}

    @Override
    public void generate(OrePrefixes prefix, Material material, Shape shape, ItemStack stack) {
        GTModHandler.removeRecipeByOutputDelayed(stack);
        CoverRegistry.registerDecorativeCover(
            stack,
            GTMaterialTextures.stored(GTMaterialIcons.block("block2", material), () -> MaterialUtils.rgba(material)));

        ItemStack triplePlate = MaterialLibAPI.findStack(material, Shapes.plateTriple, 1);
        if (triplePlate == null) return;
        GTValues.RA.stdBuilder()
            .itemInputs(triplePlate)
            .itemOutputs(GTUtility.copyAmount(1, stack), MaterialLibAPI.getStack(Materials.AshDark, Shapes.dustTiny, 1))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .metadata(ADDITIVE_AMOUNT, 2)
            .addTo(implosionRecipes);
    }
}
