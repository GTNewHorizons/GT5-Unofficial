package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.fluidCannerRecipes;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;

public class FluidCannerRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Hull_LV.get(1))
            .itemOutputs(ItemList.IC2_ReBattery.get(1))
            .fluidInputs(Materials.Redstone.getMolten(288))
            .duration(4)
            .eut(1)
            .addTo(fluidCannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Hull_LV.get(1))
            .itemOutputs(ItemList.Battery_SU_LV_Mercury.getWithCharge(1, Integer.MAX_VALUE))
            .fluidInputs(Materials.Mercury.getFluid(1000))
            .duration(16)
            .eut(1)
            .addTo(fluidCannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Hull_MV.get(1))
            .itemOutputs(ItemList.Battery_SU_MV_Mercury.getWithCharge(1, Integer.MAX_VALUE))
            .fluidInputs(Materials.Mercury.getFluid(4000))
            .duration(64)
            .eut(1)
            .addTo(fluidCannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Hull_HV.get(1))
            .itemOutputs(ItemList.Battery_SU_HV_Mercury.getWithCharge(1, Integer.MAX_VALUE))
            .fluidInputs(Materials.Mercury.getFluid(16000))
            .duration(258)
            .eut(1)
            .addTo(fluidCannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Hull_LV.get(1))
            .itemOutputs(ItemList.Battery_SU_LV_SulfuricAcid.getWithCharge(1, Integer.MAX_VALUE))
            .fluidInputs(Materials.SulfuricAcid.getFluid(1000))
            .duration(16)
            .eut(1)
            .addTo(fluidCannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Hull_MV.get(1))
            .itemOutputs(ItemList.Battery_SU_MV_SulfuricAcid.getWithCharge(1, Integer.MAX_VALUE))
            .fluidInputs(Materials.SulfuricAcid.getFluid(4000))
            .duration(64)
            .eut(1)
            .addTo(fluidCannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Hull_HV.get(1))
            .itemOutputs(ItemList.Battery_SU_HV_SulfuricAcid.getWithCharge(1, Integer.MAX_VALUE))
            .fluidInputs(Materials.SulfuricAcid.getFluid(16000))
            .duration(258)
            .eut(1)
            .addTo(fluidCannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.TF_Vial_FieryTears.get(1))
            .itemOutputs(ItemList.Bottle_Empty.get(1))
            .fluidOutputs(Materials.FierySteel.getFluid(250))
            .duration(4)
            .eut(1)
            .addTo(fluidCannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Spray_Color_Remover_Empty.get(1))
            .fluidInputs(Materials.Acetone.getFluid(4000))
            .itemOutputs(ItemList.Spray_Color_Remover.get(1))
            .duration(74)
            .eut(1)
            .addTo(fluidCannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Spray_Color_Remover.get(1))
            .itemOutputs(ItemList.Spray_Color_Remover_Empty.get(1))
            .fluidOutputs(Materials.Acetone.getFluid(4000))
            .duration(74)
            .eut(1)
            .addTo(fluidCannerRecipes);
    }
}
