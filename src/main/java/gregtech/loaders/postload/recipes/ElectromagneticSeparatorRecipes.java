package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.electroMagneticSeparatorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;

public class ElectromagneticSeparatorRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.SluiceSand, Materials2Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Neodymium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Chrome, Materials2Shapes.dust, 1))
            .outputChances(4000, 2000, 2000)
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(electroMagneticSeparatorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.TengamRaw, Materials2Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.TengamPurified, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.NeodymiumMagnetic, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.SamariumMagnetic, Materials2Shapes.dust, 1))
            .outputChances(10000, 1000, 1000)
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(electroMagneticSeparatorRecipes);
    }
}
