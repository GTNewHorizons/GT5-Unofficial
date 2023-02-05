package gregtech.loaders.postload.recipes;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsOreAlum;

public class ElectromagneticSeparatorRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.addElectromagneticSeparatorRecipe(
                MaterialsOreAlum.SluiceSand.getDust(1),
                Materials.Iron.getDust(1),
                Materials.Neodymium.getDust(1),
                Materials.Chrome.getDust(1),
                new int[] { 4000, 2000, 2000 },
                200,
                240);
    }
}
