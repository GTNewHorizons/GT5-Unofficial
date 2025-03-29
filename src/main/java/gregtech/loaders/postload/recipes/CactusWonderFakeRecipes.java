package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.cactusWonderFakeRecipes;

import net.minecraftforge.fluids.FluidRegistry;

import gregtech.api.enums.GTValues;
import gregtech.api.recipe.metadata.SteamAmount;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class CactusWonderFakeRecipes implements Runnable {

    private static final SteamAmount OFFER_VALUE = SteamAmount.INSTANCE;

    @Override
    public void run() {

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.CactusCharcoal.get(1))
            .fluidOutputs(FluidUtils.getSteam(64000))
            .metadata(OFFER_VALUE, 8000l)
            .duration(20)
            .eut(0)
            .addTo(cactusWonderFakeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.BlockCactusCharcoal.get(1))
            .fluidOutputs(FluidUtils.getSteam(64000))
            .metadata(OFFER_VALUE, 90000l)
            .duration(20)
            .eut(0)
            .addTo(cactusWonderFakeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.CompressedCactusCharcoal.get(1))
            .fluidOutputs(FluidUtils.getSteam(64000))
            .metadata(OFFER_VALUE, 1012500l)
            .duration(20)
            .eut(0)
            .addTo(cactusWonderFakeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.DoubleCompressedCactusCharcoal.get(1))
            .fluidOutputs(FluidUtils.getSuperHeatedSteam(128000))
            .metadata(OFFER_VALUE, 11390625l)
            .duration(20)
            .eut(0)
            .addTo(cactusWonderFakeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.TripleCompressedCactusCharcoal.get(1))
            .fluidOutputs(FluidUtils.getSuperHeatedSteam(128000))
            .metadata(OFFER_VALUE, 128144531l)
            .duration(20)
            .eut(0)
            .addTo(cactusWonderFakeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.QuadrupleCompressedCactusCharcoal.get(1))
            .fluidOutputs(FluidRegistry.getFluidStack("supercriticalsteam", 256000))
            .metadata(OFFER_VALUE, 1441625977l)
            .duration(20)
            .eut(0)
            .addTo(cactusWonderFakeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.CactusCharcoal.get(1))
            .fluidOutputs(FluidRegistry.getFluidStack("supercriticalsteam", 256000))
            .metadata(OFFER_VALUE, 16218292236l)
            .duration(20)
            .eut(0)
            .addTo(cactusWonderFakeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.CactusCoke.get(1))
            .fluidOutputs(FluidUtils.getSteam(64000))
            .metadata(OFFER_VALUE, 16000l)
            .duration(20)
            .eut(0)
            .addTo(cactusWonderFakeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.BlockCactusCoke.get(1))
            .fluidOutputs(FluidUtils.getSteam(64000))
            .metadata(OFFER_VALUE, 180000l)
            .duration(20)
            .eut(0)
            .addTo(cactusWonderFakeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.CompressedCactusCoke.get(1))
            .fluidOutputs(FluidUtils.getSteam(64000))
            .metadata(OFFER_VALUE, 2025000l)
            .duration(20)
            .eut(0)
            .addTo(cactusWonderFakeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.DoubleCompressedCactusCoke.get(1))
            .fluidOutputs(FluidUtils.getSuperHeatedSteam(128000))
            .metadata(OFFER_VALUE, 22781250l)
            .duration(20)
            .eut(0)
            .addTo(cactusWonderFakeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.TripleCompressedCactusCoke.get(1))
            .fluidOutputs(FluidUtils.getSuperHeatedSteam(128000))
            .metadata(OFFER_VALUE, 256289063l)
            .duration(20)
            .eut(0)
            .addTo(cactusWonderFakeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.QuadrupleCompressedCactusCoke.get(1))
            .fluidOutputs(FluidRegistry.getFluidStack("supercriticalsteam", 256000))
            .metadata(OFFER_VALUE, 2883251953l)
            .duration(20)
            .eut(0)
            .addTo(cactusWonderFakeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.QuintupleCompressedCactusCoke.get(1))
            .fluidOutputs(FluidRegistry.getFluidStack("supercriticalsteam", 256000))
            .metadata(OFFER_VALUE, 32436584473l)
            .duration(20)
            .eut(0)
            .addTo(cactusWonderFakeRecipes);

    }
}
