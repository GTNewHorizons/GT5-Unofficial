package gregtech.loaders.postload.recipes;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sFluidCannerRecipes;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;

public class FluidCannerRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.stdBuilder().itemInputs(ItemList.Battery_Hull_LV.get(1)).itemOutputs(ItemList.IC2_ReBattery.get(1))
                .fluidInputs(Materials.Redstone.getMolten(288)).noFluidOutputs().duration(4).eut(1)
                .addTo(sFluidCannerRecipes);

        GT_Values.RA.stdBuilder().itemInputs(ItemList.Battery_Hull_LV.get(1))
                .itemOutputs(ItemList.Battery_SU_LV_Mercury.getWithCharge(1, Integer.MAX_VALUE))
                .fluidInputs(Materials.Mercury.getFluid(1000)).noFluidOutputs().duration(16).eut(1)
                .addTo(sFluidCannerRecipes);

        GT_Values.RA.stdBuilder().itemInputs(ItemList.Battery_Hull_MV.get(1))
                .itemOutputs(ItemList.Battery_SU_MV_Mercury.getWithCharge(1, Integer.MAX_VALUE))
                .fluidInputs(Materials.Mercury.getFluid(4000)).noFluidOutputs().duration(64).eut(1)
                .addTo(sFluidCannerRecipes);

        GT_Values.RA.stdBuilder().itemInputs(ItemList.Battery_Hull_HV.get(1))
                .itemOutputs(ItemList.Battery_SU_HV_Mercury.getWithCharge(1, Integer.MAX_VALUE))
                .fluidInputs(Materials.Mercury.getFluid(16000)).noFluidOutputs().duration(258).eut(1)
                .addTo(sFluidCannerRecipes);

        GT_Values.RA.stdBuilder().itemInputs(ItemList.Battery_Hull_LV.get(1))
                .itemOutputs(ItemList.Battery_SU_LV_SulfuricAcid.getWithCharge(1, Integer.MAX_VALUE))
                .fluidInputs(Materials.SulfuricAcid.getFluid(1000)).noFluidOutputs().duration(16).eut(1)
                .addTo(sFluidCannerRecipes);

        GT_Values.RA.stdBuilder().itemInputs(ItemList.Battery_Hull_MV.get(1))
                .itemOutputs(ItemList.Battery_SU_MV_SulfuricAcid.getWithCharge(1, Integer.MAX_VALUE))
                .fluidInputs(Materials.SulfuricAcid.getFluid(4000)).noFluidOutputs().duration(64).eut(1)
                .addTo(sFluidCannerRecipes);

        GT_Values.RA.stdBuilder().itemInputs(ItemList.Battery_Hull_HV.get(1))
                .itemOutputs(ItemList.Battery_SU_HV_SulfuricAcid.getWithCharge(1, Integer.MAX_VALUE))
                .fluidInputs(Materials.SulfuricAcid.getFluid(16000)).noFluidOutputs().duration(258).eut(1)
                .addTo(sFluidCannerRecipes);

        GT_Values.RA.stdBuilder().itemInputs(ItemList.TF_Vial_FieryTears.get(1))
                .itemOutputs(ItemList.Bottle_Empty.get(1)).noFluidInputs()
                .fluidOutputs(Materials.FierySteel.getFluid(250)).duration(4).eut(1).addTo(sFluidCannerRecipes);
    }
}
