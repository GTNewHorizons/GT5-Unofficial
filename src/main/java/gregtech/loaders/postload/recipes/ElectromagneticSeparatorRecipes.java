package gregtech.loaders.postload.recipes;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sElectroMagneticSeparatorRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsOreAlum;
import gregtech.api.enums.TierEU;

public class ElectromagneticSeparatorRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.stdBuilder()
            .itemInputs(MaterialsOreAlum.SluiceSand.getDust(1))
            .itemOutputs(Materials.Iron.getDust(1), Materials.Neodymium.getDust(1), Materials.Chrome.getDust(1))
            .outputChances(4000, 2000, 2000)
            .noFluidInputs()
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(sElectroMagneticSeparatorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.TengamRaw.getDust(1))
            .itemOutputs(
                Materials.TengamPurified.getDust(1),
                Materials.NeodymiumMagnetic.getDust(1),
                Materials.SamariumMagnetic.getDust(1))
            .outputChances(10000, 1000, 1000)
            .noFluidInputs()
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(sElectroMagneticSeparatorRecipes);
    }
}
