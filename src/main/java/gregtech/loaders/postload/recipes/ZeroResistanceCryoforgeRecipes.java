package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.zeroResistanceCryoforgeRecipes;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;

public class ZeroResistanceCryoforgeRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Electric_Pump_UV.get(1),
                GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Neutronium, 14))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUV, 21))
            .fluidInputs(
                Materials.SuperconductorUVBase.getMolten(21 * HALF_INGOTS),
                WerkstoffLoader.LiquidHelium.getFluidOrGas(20_000))
            .duration(1 * MINUTES + 4 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(zeroResistanceCryoforgeRecipes);
    }
}
