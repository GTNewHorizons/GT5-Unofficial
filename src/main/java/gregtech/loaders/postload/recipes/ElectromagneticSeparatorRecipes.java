package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.electroMagneticSeparatorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;

public class ElectromagneticSeparatorRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.SluiceSand.getDust(1))
            .itemOutputs(Materials.Iron.getDust(1), Materials.Neodymium.getDust(1), Materials.Chrome.getDust(1))
            .outputChances(4000, 2000, 2000)
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(electroMagneticSeparatorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.TengamRaw.getDust(1))
            .itemOutputs(
                Materials.TengamPurified.getDust(1),
                Materials.NeodymiumMagnetic.getDust(1),
                Materials.SamariumMagnetic.getDust(1))
            .outputChances(10000, 1000, 1000)
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(electroMagneticSeparatorRecipes);
    }
}
