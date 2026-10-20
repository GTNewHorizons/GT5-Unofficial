package gregtech.loaders.oreprocessing;

import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialUtils;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTUtility;

public class ProcessingIceOre implements PartRecipeGenerator {

    public static final ProcessingIceOre INSTANCE = new ProcessingIceOre();

    private ProcessingIceOre() {}

    @Override
    public void generate(OrePrefixes prefix, Material material, Shape shape, ItemStack stack) {
        if (prefix != OrePrefixes.ore && prefix != OrePrefixes.rawOre) return;
        if (!MaterialUtils.hasFlag(material, GTMaterialFlag.ICE_ORE)) return;

        int oreMultiplier = MaterialUtils.oreMultiplier(material);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, stack))
            .fluidOutputs(MaterialUtils.gas(material, 1000L * oreMultiplier))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(RecipeMaps.fluidExtractionRecipes);
    }
}
