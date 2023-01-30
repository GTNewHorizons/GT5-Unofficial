package gregtech.loaders.postload.recipes;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;

public class FluidCannerRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.addFluidCannerRecipe(
                ItemList.Battery_Hull_LV.get(1L),
                ItemList.IC2_ReBattery.get(1L),
                Materials.Redstone.getMolten(288L),
                GT_Values.NF);
        GT_Values.RA.addFluidCannerRecipe(
                ItemList.Battery_Hull_LV.get(1L),
                ItemList.Battery_SU_LV_Mercury.getWithCharge(1L, Integer.MAX_VALUE),
                Materials.Mercury.getFluid(1000L),
                GT_Values.NF);
        GT_Values.RA.addFluidCannerRecipe(
                ItemList.Battery_Hull_MV.get(1L),
                ItemList.Battery_SU_MV_Mercury.getWithCharge(1L, Integer.MAX_VALUE),
                Materials.Mercury.getFluid(4000L),
                GT_Values.NF);
        GT_Values.RA.addFluidCannerRecipe(
                ItemList.Battery_Hull_HV.get(1L),
                ItemList.Battery_SU_HV_Mercury.getWithCharge(1L, Integer.MAX_VALUE),
                Materials.Mercury.getFluid(16000L),
                GT_Values.NF);
        GT_Values.RA.addFluidCannerRecipe(
                ItemList.Battery_Hull_LV.get(1L),
                ItemList.Battery_SU_LV_SulfuricAcid.getWithCharge(1L, Integer.MAX_VALUE),
                Materials.SulfuricAcid.getFluid(1000L),
                GT_Values.NF);
        GT_Values.RA.addFluidCannerRecipe(
                ItemList.Battery_Hull_MV.get(1L),
                ItemList.Battery_SU_MV_SulfuricAcid.getWithCharge(1L, Integer.MAX_VALUE),
                Materials.SulfuricAcid.getFluid(4000L),
                GT_Values.NF);
        GT_Values.RA.addFluidCannerRecipe(
                ItemList.Battery_Hull_HV.get(1L),
                ItemList.Battery_SU_HV_SulfuricAcid.getWithCharge(1L, Integer.MAX_VALUE),
                Materials.SulfuricAcid.getFluid(16000L),
                GT_Values.NF);
        GT_Values.RA.addFluidCannerRecipe(
                ItemList.TF_Vial_FieryTears.get(1L),
                ItemList.Bottle_Empty.get(1L),
                GT_Values.NF,
                Materials.FierySteel.getFluid(250L));
    }
}
