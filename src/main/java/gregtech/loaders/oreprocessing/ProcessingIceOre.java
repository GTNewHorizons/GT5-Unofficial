package gregtech.loaders.oreprocessing;

import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTUtility;

public class ProcessingIceOre implements IOreRecipeRegistrator {

    public ProcessingIceOre() {
        OrePrefixes.ore.add(this);
        OrePrefixes.rawOre.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Materials material, String oredictName, String modName,
        ItemStack stack) {
        if (!material.contains(SubTag.ICE_ORE)) return;

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, stack))
            .fluidOutputs(material.getGas(1000L * material.mOreMultiplier))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(RecipeMaps.fluidExtractionRecipes);
    }
}
