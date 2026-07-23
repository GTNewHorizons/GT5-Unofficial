package gregtech.loaders.oreprocessing;

import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MU;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTUtility;

public class ProcessingIceOre implements IOreRecipeRegistrator {

    public static ProcessingIceOre INSTANCE;

    public ProcessingIceOre() {
        INSTANCE = this;
        OrePrefixes.ore.add(this);
        OrePrefixes.rawOre.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Materials material, String oredictName, String modName,
        ItemStack stack) {
        registerOre(prefix, MU.material(material), oredictName, modName, stack);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oredictName, String modName,
        ItemStack stack) {
        if (!MU.hasFlag(material, GTMaterialFlag.ICE_ORE)) return;

        Integer oreMultiplierProp = material.getProperty(GTMaterialProperties.ORE_MULTIPLIER);
        int oreMultiplier = oreMultiplierProp == null ? 1 : oreMultiplierProp;
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, stack))
            .fluidOutputs(MU.gas(material, 1000L * oreMultiplier))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(RecipeMaps.fluidExtractionRecipes);
    }
}
