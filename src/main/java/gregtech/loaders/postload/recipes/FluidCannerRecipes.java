package gregtech.loaders.postload.recipes;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;

public class FluidCannerRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.addFluidCannerRecipe(
                ItemList.Battery_Hull_LV.get(1),
                ItemList.IC2_ReBattery.get(1),
                Materials.Redstone.getMolten(288),
                GT_Values.NF);
        GT_Values.RA.addFluidCannerRecipe(
                ItemList.Battery_Hull_LV.get(1),
                ItemList.Battery_SU_LV_Mercury.getWithCharge(1, Integer.MAX_VALUE),
                Materials.Mercury.getFluid(1000),
                GT_Values.NF);
        GT_Values.RA.addFluidCannerRecipe(
                ItemList.Battery_Hull_MV.get(1),
                ItemList.Battery_SU_MV_Mercury.getWithCharge(1, Integer.MAX_VALUE),
                Materials.Mercury.getFluid(4000),
                GT_Values.NF);
        GT_Values.RA.addFluidCannerRecipe(
                ItemList.Battery_Hull_HV.get(1),
                ItemList.Battery_SU_HV_Mercury.getWithCharge(1, Integer.MAX_VALUE),
                Materials.Mercury.getFluid(16000),
                GT_Values.NF);
        GT_Values.RA.addFluidCannerRecipe(
                ItemList.Battery_Hull_LV.get(1),
                ItemList.Battery_SU_LV_SulfuricAcid.getWithCharge(1, Integer.MAX_VALUE),
                Materials.SulfuricAcid.getFluid(1000),
                GT_Values.NF);
        GT_Values.RA.addFluidCannerRecipe(
                ItemList.Battery_Hull_MV.get(1),
                ItemList.Battery_SU_MV_SulfuricAcid.getWithCharge(1, Integer.MAX_VALUE),
                Materials.SulfuricAcid.getFluid(4000),
                GT_Values.NF);
        GT_Values.RA.addFluidCannerRecipe(
                ItemList.Battery_Hull_HV.get(1),
                ItemList.Battery_SU_HV_SulfuricAcid.getWithCharge(1, Integer.MAX_VALUE),
                Materials.SulfuricAcid.getFluid(16000),
                GT_Values.NF);
        GT_Values.RA.addFluidCannerRecipe(
                ItemList.TF_Vial_FieryTears.get(1),
                ItemList.Bottle_Empty.get(1),
                GT_Values.NF,
                Materials.FierySteel.getFluid(250));
    }
}
