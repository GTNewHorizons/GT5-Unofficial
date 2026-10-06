package gregtech.loaders.oreprocessing;

import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.Materials;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialUtils;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTUtility;

public class ProcessingRawOre implements PartRecipeGenerator {

    public static final ProcessingRawOre INSTANCE = new ProcessingRawOre();

    private ProcessingRawOre() {}

    @Override
    public void generate(OrePrefixes prefix, Material material, String oreDictName, ItemStack stack) {
        if (material == null) return;

        if (MaterialUtils.hasFlag(material, GTMaterialFlag.NO_ORE_PROCESSING)) {
            return;
        }

        if (MaterialUtils.hasFlag(material, GTMaterialFlag.ICE_ORE)) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, stack))
                .fluidOutputs(MaterialUtils.gas(material, 1000L * MaterialUtils.oreMultiplier(material)))
                .duration(2 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(RecipeMaps.fluidExtractionRecipes);
        } else if (material == Materials.Oilsands) {
            ProcessingOre.addOilsandsRecipe(stack, false);
        } else {
            ProcessingOre.addOreRecipes(prefix, material, GTUtility.copyAmount(1, stack), 1, true);
        }
    }
}
