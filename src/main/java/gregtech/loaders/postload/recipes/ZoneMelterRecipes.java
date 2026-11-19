package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.zoneMelterRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.util.GTUtility;

public final class ZoneMelterRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Crude_Silicon_Boule.get(1), Materials.SiliconSG.getDust(1))
            .itemOutputs(ItemList.Precise_Silicon_Boule.get(1), Materials.SiliconDioxide.getDust(3))
            .inputChances(10_000, 5_000)
            .outputChances(10_000, 5_000)
            .duration(30 * SECONDS)
            .eut(480)
            .addTo(zoneMelterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Crude_Microwaved_Silicon_Boule.get(1), ItemList.Microwaved_Silicon_Dust.get(1))
            .itemOutputs(ItemList.Precise_Microwaved_Silicon_Boule.get(1), Materials.SiliconDioxide.getDust(3))
            .inputChances(10_000, 5_000)
            .outputChances(10_000, 5_000)
            .duration(30 * SECONDS)
            .eut(122_880)
            .addTo(zoneMelterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Crude_AlGaAs_Boule.get(1), GTUtility.copyAmount(2, ItemList.AlGaAs_Dust.get(1)))
            .itemOutputs(
                ItemList.Precise_AlGaAs_Boule.get(1),
                Materials.Aluminiumoxide.getDust(5),
                Materials.ArsenicTrioxide.getDust(5))
            .inputChances(10_000, 2_500)
            .outputChances(10_000, 1_250, 1_250)
            .duration(30 * SECONDS)
            .eut(7_864_320)
            .addTo(zoneMelterRecipes);
    }
}
