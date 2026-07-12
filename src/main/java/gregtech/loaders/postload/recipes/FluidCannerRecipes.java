package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.cannerRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;

public class FluidCannerRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Hull_LV.get(1))
            .itemOutputs(ItemList.IC2_ReBattery.get(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Redstone, Materials2FluidShapes.fluidMolten, (int) (2 * INGOTS)))
            .duration(4)
            .eut(1)
            .addTo(cannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Hull_LV.get(1))
            .itemOutputs(ItemList.Battery_SU_LV_Mercury.getWithCharge(1, Integer.MAX_VALUE))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Mercury, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(16)
            .eut(1)
            .addTo(cannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Hull_MV.get(1))
            .itemOutputs(ItemList.Battery_SU_MV_Mercury.getWithCharge(1, Integer.MAX_VALUE))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Mercury, Materials2FluidShapes.fluidLiquid, (int) (4_000)))
            .duration(64)
            .eut(1)
            .addTo(cannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Hull_HV.get(1))
            .itemOutputs(ItemList.Battery_SU_HV_Mercury.getWithCharge(1, Integer.MAX_VALUE))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Mercury, Materials2FluidShapes.fluidLiquid, (int) (16_000)))
            .duration(258)
            .eut(1)
            .addTo(cannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Hull_LV.get(1))
            .itemOutputs(ItemList.Battery_SU_LV_SulfuricAcid.getWithCharge(1, Integer.MAX_VALUE))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(16)
            .eut(1)
            .addTo(cannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Hull_MV.get(1))
            .itemOutputs(ItemList.Battery_SU_MV_SulfuricAcid.getWithCharge(1, Integer.MAX_VALUE))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.fluidLiquid, (int) (4_000)))
            .duration(64)
            .eut(1)
            .addTo(cannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Hull_HV.get(1))
            .itemOutputs(ItemList.Battery_SU_HV_SulfuricAcid.getWithCharge(1, Integer.MAX_VALUE))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.fluidLiquid, (int) (16_000)))
            .duration(258)
            .eut(1)
            .addTo(cannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.TF_Vial_FieryTears.get(1))
            .itemOutputs(ItemList.Bottle_Empty.get(1))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.FierySteel, Materials2FluidShapes.fluidLiquid, (int) (250)))
            .duration(4)
            .eut(1)
            .addTo(cannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Spray_Color_Remover_Empty.get(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Acetone, Materials2FluidShapes.fluidLiquid, (int) (4_000)))
            .itemOutputs(ItemList.Spray_Color_Remover.get(1))
            .duration(74)
            .eut(1)
            .addTo(cannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Spray_Color_Remover.get(1))
            .itemOutputs(ItemList.Spray_Color_Remover_Empty.get(1))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Acetone, Materials2FluidShapes.fluidLiquid, (int) (4_000)))
            .duration(74)
            .eut(1)
            .addTo(cannerRecipes);
    }
}
