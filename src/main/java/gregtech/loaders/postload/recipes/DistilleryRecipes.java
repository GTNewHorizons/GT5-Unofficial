package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.TinkerConstruct;
import static gregtech.api.recipe.RecipeMaps.distillationTowerRecipes;
import static gregtech.api.recipe.RecipeMaps.distilleryRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsKevlar;
import gregtech.api.enums.MaterialsOreAlum;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;

public class DistilleryRecipes implements Runnable {

    @Override
    public void run() {

        distillationTowerRecipes();
        universalDistillationTowerRecipes();

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(24))
            .fluidInputs(Materials.Creosote.getFluid(100L))
            .fluidOutputs(Materials.Lubricant.getFluid(32L))
            .duration(12 * SECONDS)
            .eut(30)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(24))
            .fluidInputs(Materials.SeedOil.getFluid(32L))
            .fluidOutputs(Materials.Lubricant.getFluid(8L))
            .duration(4 * SECONDS)
            .eut(30)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(24))
            .fluidInputs(Materials.FishOil.getFluid(32L))
            .fluidOutputs(Materials.Lubricant.getFluid(8L))
            .duration(4 * SECONDS)
            .eut(30)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(24))
            .fluidInputs(Materials.Oil.getFluid(120L))
            .fluidOutputs(Materials.Lubricant.getFluid(60L))
            .duration(8 * SECONDS)
            .eut(30)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(24))
            .fluidInputs(Materials.OilLight.getFluid(120L))
            .fluidOutputs(Materials.Lubricant.getFluid(30L))
            .duration(8 * SECONDS)
            .eut(30)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(24))
            .fluidInputs(Materials.OilMedium.getFluid(120L))
            .fluidOutputs(Materials.Lubricant.getFluid(60L))
            .duration(8 * SECONDS)
            .eut(30)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(24))
            .fluidInputs(Materials.OilHeavy.getFluid(120L))
            .fluidOutputs(Materials.Lubricant.getFluid(90L))
            .duration(8 * SECONDS)
            .eut(30)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .fluidInputs(Materials.Biomass.getFluid(40L))
            .fluidOutputs(Materials.Ethanol.getFluid(12L))
            .duration(16 * TICKS)
            .eut(24)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(5))
            .fluidInputs(Materials.Biomass.getFluid(40L))
            .fluidOutputs(Materials.Water.getFluid(12L))
            .duration(16 * TICKS)
            .eut(24)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(5))
            .fluidInputs(Materials.Water.getFluid(5L))
            .fluidOutputs(GTModHandler.getDistilledWater(5L))
            .duration(16 * TICKS)
            .eut(10)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .fluidInputs(getFluidStack("potion.potatojuice", 2))
            .fluidOutputs(getFluidStack("potion.vodka", 1))
            .duration(16 * TICKS)
            .eut(16)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .fluidInputs(getFluidStack("potion.lemonade", 2))
            .fluidOutputs(getFluidStack("potion.alcopops", 1))
            .duration(16 * TICKS)
            .eut(16)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .itemOutputs(MaterialsOreAlum.SluiceSand.getDust(1))
            .fluidInputs(MaterialsOreAlum.SluiceJuice.getFluid(1000))
            .fluidOutputs(Materials.Water.getFluid(500))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(6))
            .fluidInputs(Materials.WoodTar.getFluid(200))
            .fluidOutputs(MaterialsKevlar.IIIDimethylbenzene.getFluid(30))
            .duration(16 * TICKS)
            .eut(64)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(6))
            .fluidInputs(Materials.CharcoalByproducts.getGas(200))
            .fluidOutputs(MaterialsKevlar.IIIDimethylbenzene.getFluid(20))
            .duration(5 * SECONDS)
            .eut(64)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(7))
            .fluidInputs(Materials.WoodTar.getFluid(200))
            .fluidOutputs(MaterialsKevlar.IVDimethylbenzene.getFluid(30))
            .duration(16 * TICKS)
            .eut(64)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(7))
            .fluidInputs(Materials.CharcoalByproducts.getGas(200))
            .fluidOutputs(MaterialsKevlar.IVDimethylbenzene.getFluid(20))
            .duration(5 * SECONDS)
            .eut(64)
            .addTo(distilleryRecipes);
        // (NaCl·H2O) = NaCl + H2O

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .itemOutputs(Materials.Salt.getDust(2))
            .fluidInputs(Materials.SaltWater.getFluid(1000))
            .fluidOutputs(GTModHandler.getDistilledWater(1000))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(30)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.IC2_Fertilizer.get(1))
            .fluidInputs(Materials.FermentedBiomass.getFluid(1000))
            .fluidOutputs(Materials.AceticAcid.getFluid(25))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(8)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(2))
            .itemOutputs(ItemList.IC2_Fertilizer.get(1))
            .fluidInputs(Materials.FermentedBiomass.getFluid(1000))
            .fluidOutputs(Materials.Water.getFluid(375))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(8)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(3))
            .itemOutputs(ItemList.IC2_Fertilizer.get(1))
            .fluidInputs(Materials.FermentedBiomass.getFluid(1000))
            .fluidOutputs(Materials.Ethanol.getFluid(150))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(8)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(4))
            .itemOutputs(ItemList.IC2_Fertilizer.get(1))
            .fluidInputs(Materials.FermentedBiomass.getFluid(1000))
            .fluidOutputs(Materials.Methanol.getFluid(150))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(8)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(5))
            .itemOutputs(ItemList.IC2_Fertilizer.get(1))
            .fluidInputs(Materials.FermentedBiomass.getFluid(1000))
            .fluidOutputs(Materials.Ammonia.getGas(100))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(8)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(6))
            .itemOutputs(ItemList.IC2_Fertilizer.get(1))
            .fluidInputs(Materials.FermentedBiomass.getFluid(1000))
            .fluidOutputs(Materials.CarbonDioxide.getGas(400))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(8)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(7))
            .itemOutputs(ItemList.IC2_Fertilizer.get(1))
            .fluidInputs(Materials.FermentedBiomass.getFluid(1000))
            .fluidOutputs(Materials.Methane.getGas(600))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(8)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(17))
            .itemOutputs(ItemList.IC2_Fertilizer.get(1))
            .fluidInputs(Materials.FermentedBiomass.getFluid(1000))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("ic2biogas"), 1800))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(8)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .fluidInputs(Materials.Methane.getGas(1000))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("ic2biogas"), 3000))
            .duration(8 * SECONDS)
            .eut(8)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(2))
            .fluidInputs(Materials.HeavyFuel.getFluid(100))
            .fluidOutputs(Materials.Benzene.getFluid(40))
            .duration(8 * SECONDS)
            .eut(24)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(3))
            .fluidInputs(Materials.HeavyFuel.getFluid(100))
            .fluidOutputs(Materials.Phenol.getFluid(25))
            .duration(8 * SECONDS)
            .eut(24)
            .addTo(distilleryRecipes);
        // Dimethylbenzene

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(5))
            .fluidInputs(Materials.WoodTar.getFluid(200))
            .fluidOutputs(Materials.Dimethylbenzene.getFluid(30))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(5))
            .fluidInputs(Materials.CharcoalByproducts.getGas(200))
            .fluidOutputs(Materials.Dimethylbenzene.getFluid(20))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .fluidInputs(new FluidStack(ItemList.sOilExtraHeavy, 10))
            .fluidOutputs(Materials.OilHeavy.getFluid(15))
            .duration(16 * TICKS)
            .eut(24)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .fluidInputs(Materials.HeavyFuel.getFluid(10L))
            .fluidOutputs(new FluidStack(ItemList.sToluene, 4))
            .duration(16 * TICKS)
            .eut(24)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .fluidInputs(new FluidStack(ItemList.sToluene, 30))
            .fluidOutputs(Materials.LightFuel.getFluid(30L))
            .duration(16 * TICKS)
            .eut(24)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("ic2biomass"), 20))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("ic2biogas"), 32))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(2))
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("ic2biomass"), 4))
            .fluidOutputs(Materials.Water.getFluid(2))
            .duration(4 * SECONDS)
            .eut(30)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .fluidInputs(getFluidStack("potion.wheatyjuice", 75))
            .fluidOutputs(getFluidStack("potion.scotch", 1))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distilleryRecipes);

        if (TinkerConstruct.isModLoaded()) {

            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.getIntegratedCircuit(1))
                .fluidInputs(Materials.Glue.getFluid(8L))
                .fluidOutputs(getFluidStack("glue", 8))
                .duration(1 * TICKS)
                .eut(24)
                .addTo(distilleryRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.getIntegratedCircuit(1))
                .fluidInputs(getFluidStack("glue", 8))
                .fluidOutputs(Materials.Glue.getFluid(4L))
                .duration(1 * TICKS)
                .eut(24)
                .addTo(distilleryRecipes);

        }
    }

    public void distillationTowerRecipes() {
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(2))
            .fluidInputs(Materials.Creosote.getFluid(1000L))
            .fluidOutputs(Materials.Lubricant.getFluid(500L))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(2))
            .fluidInputs(Materials.SeedOil.getFluid(1400L))
            .fluidOutputs(Materials.Lubricant.getFluid(500L))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(2))
            .fluidInputs(Materials.FishOil.getFluid(1200L))
            .fluidOutputs(Materials.Lubricant.getFluid(500L))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Wood, 2L))
            .fluidInputs(Materials.Biomass.getFluid(1000L))
            .fluidOutputs(Materials.Ethanol.getFluid(600L), Materials.Water.getFluid(300L))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(400)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Water.getFluid(1000L))
            .fluidOutputs(GTModHandler.getDistilledWater(1000L))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(2))
            .fluidInputs(Materials.OilLight.getFluid(1000L))
            .fluidOutputs(Materials.Lubricant.getFluid(250L))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(2))
            .fluidInputs(Materials.OilMedium.getFluid(1000L))
            .fluidOutputs(Materials.Lubricant.getFluid(500L))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(2))
            .fluidInputs(Materials.OilHeavy.getFluid(1000L))
            .fluidOutputs(Materials.Lubricant.getFluid(750L))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);
        // C15H10N2O2(5HCl) = C15H10N2O2 + 5HCl

        GTValues.RA.stdBuilder()
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, MaterialsKevlar.DiphenylmethaneDiisocyanate, 29L))
            .fluidInputs(MaterialsKevlar.DiphenylmethaneDiisocyanateMixture.getFluid(1000L))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(5000L))
            .duration(2 * MINUTES + 5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(2))
            .itemOutputs(Materials.Charcoal.getDustSmall(1))
            .fluidInputs(Materials.CharcoalByproducts.getGas(1000))
            .fluidOutputs(
                Materials.WoodTar.getFluid(250),
                Materials.WoodVinegar.getFluid(400),
                Materials.WoodGas.getGas(250),
                MaterialsKevlar.IIIDimethylbenzene.getFluid(100))
            .duration(2 * SECONDS)
            .eut(256)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(2))
            .fluidInputs(Materials.WoodTar.getFluid(1000))
            .fluidOutputs(
                Materials.Creosote.getFluid(250),
                Materials.Phenol.getFluid(100),
                Materials.Benzene.getFluid(400),
                Materials.Toluene.getFluid(100),
                MaterialsKevlar.IIIDimethylbenzene.getFluid(150))
            .duration(2 * SECONDS)
            .eut(256)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(3))
            .itemOutputs(Materials.Charcoal.getDustSmall(1))
            .fluidInputs(Materials.CharcoalByproducts.getGas(1000))
            .fluidOutputs(
                Materials.WoodTar.getFluid(250),
                Materials.WoodVinegar.getFluid(400),
                Materials.WoodGas.getGas(250),
                MaterialsKevlar.IVDimethylbenzene.getFluid(100))
            .duration(2 * SECONDS)
            .eut(256)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(4))
            .itemOutputs(Materials.Charcoal.getDustSmall(1))
            .fluidInputs(Materials.CharcoalByproducts.getGas(1000))
            .fluidOutputs(
                Materials.WoodTar.getFluid(250),
                Materials.WoodVinegar.getFluid(400),
                Materials.WoodGas.getGas(250),
                Materials.Dimethylbenzene.getFluid(20),
                MaterialsKevlar.IIIDimethylbenzene.getFluid(60),
                MaterialsKevlar.IVDimethylbenzene.getFluid(20))
            .duration(2 * SECONDS)
            .eut(256)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(4))
            .fluidInputs(Materials.WoodTar.getFluid(1000))
            .fluidOutputs(
                Materials.Creosote.getFluid(250),
                Materials.Phenol.getFluid(100),
                Materials.Benzene.getFluid(400),
                Materials.Toluene.getFluid(100),
                Materials.Dimethylbenzene.getFluid(30),
                MaterialsKevlar.IIIDimethylbenzene.getFluid(90),
                MaterialsKevlar.IVDimethylbenzene.getFluid(30))
            .duration(2 * SECONDS)
            .eut(256)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(9))
            .fluidInputs(Materials.OilLight.getFluid(1000))
            .fluidOutputs(
                Materials.SulfuricHeavyFuel.getFluid(70),
                Materials.SulfuricLightFuel.getFluid(130),
                Materials.SulfuricNaphtha.getFluid(200),
                MaterialsKevlar.NaphthenicAcid.getFluid(15),
                Materials.SulfuricGas.getGas(1600))
            .duration(1 * SECONDS + 1 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(9))
            .fluidInputs(Materials.OilMedium.getFluid(1000))
            .fluidOutputs(
                Materials.SulfuricHeavyFuel.getFluid(100),
                Materials.SulfuricLightFuel.getFluid(500),
                Materials.SulfuricNaphtha.getFluid(1500),
                MaterialsKevlar.NaphthenicAcid.getFluid(25),
                Materials.SulfuricGas.getGas(600))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(9))
            .fluidInputs(Materials.Oil.getFluid(1000L))
            .fluidOutputs(
                Materials.SulfuricHeavyFuel.getFluid(300),
                Materials.SulfuricLightFuel.getFluid(1000),
                Materials.SulfuricNaphtha.getFluid(400),
                MaterialsKevlar.NaphthenicAcid.getFluid(50),
                Materials.SulfuricGas.getGas(1200))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(9))
            .fluidInputs(Materials.OilHeavy.getFluid(1000))
            .fluidOutputs(
                Materials.SulfuricHeavyFuel.getFluid(450),
                Materials.SulfuricLightFuel.getFluid(150),
                Materials.SulfuricNaphtha.getFluid(300),
                MaterialsKevlar.NaphthenicAcid.getFluid(50),
                Materials.SulfuricGas.getGas(600))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(distillationTowerRecipes);
        // 9C5H12O = 4C6H14O + 5CH4O + 4C4H8

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.MTBEMixture.getGas(900L))
            .fluidOutputs(
                Materials.AntiKnock.getFluid(400L),
                Materials.Methanol.getFluid(500L),
                Materials.Butene.getGas(400L))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.MTBEMixtureAlt.getGas(900L))
            .fluidOutputs(
                Materials.AntiKnock.getFluid(400L),
                Materials.Methanol.getFluid(500L),
                Materials.Butane.getGas(400L))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(ItemList.IC2_Fertilizer.get(1))
            .fluidInputs(Materials.FermentedBiomass.getFluid(1000))
            .fluidOutputs(
                Materials.AceticAcid.getFluid(25),
                Materials.Water.getFluid(375),
                Materials.Ethanol.getFluid(150),
                Materials.Methanol.getFluid(150),
                Materials.Ammonia.getGas(100),
                Materials.CarbonDioxide.getGas(400),
                Materials.Methane.getGas(600))
            .duration(3 * SECONDS + 15 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(ItemList.IC2_Fertilizer.get(1))
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("ic2biomass"), 3000))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("ic2biogas"), 8000), Materials.Water.getFluid(125L))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(distillationTowerRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(new FluidStack(ItemList.sOilExtraHeavy, 1000))
            .fluidOutputs(Materials.OilHeavy.getFluid(1500))
            .duration(16 * TICKS)
            .eut(2400)
            .addTo(distillationTowerRecipes);
    }

    public void universalDistillationTowerRecipes() {
        addUniversalDistillationRecipewithCircuit(
            Materials.WoodTar.getFluid(1000),
            new ItemStack[] { GTUtility.getIntegratedCircuit(3) },
            new FluidStack[] { Materials.Creosote.getFluid(250), Materials.Phenol.getFluid(100),
                Materials.Benzene.getFluid(400), Materials.Toluene.getFluid(100),
                MaterialsKevlar.IVDimethylbenzene.getFluid(150) },
            GTValues.NI,
            40,
            256);

        addUniversalDistillationRecipewithCircuit(
            Materials.CharcoalByproducts.getGas(1000),
            new ItemStack[] { GTUtility.getIntegratedCircuit(1) },
            new FluidStack[] { Materials.WoodTar.getFluid(250), Materials.WoodVinegar.getFluid(400),
                Materials.WoodGas.getGas(250), Materials.Dimethylbenzene.getFluid(100) },
            Materials.Charcoal.getDustSmall(1),
            40,
            256);

        addUniversalDistillationRecipewithCircuit(
            Materials.WoodGas.getGas(1000),
            new ItemStack[] { GTUtility.getIntegratedCircuit(1) },
            new FluidStack[] { Materials.CarbonDioxide.getGas(390), Materials.Ethylene.getGas(120),
                Materials.Methane.getGas(130), Materials.CarbonMonoxide.getGas(240), Materials.Hydrogen.getGas(120) },
            GTValues.NI,
            40,
            256);
        addUniversalDistillationRecipewithCircuit(
            Materials.WoodVinegar.getFluid(1000),
            new ItemStack[] { GTUtility.getIntegratedCircuit(1) },
            new FluidStack[] { Materials.AceticAcid.getFluid(100), Materials.Water.getFluid(500),
                Materials.Ethanol.getFluid(10), Materials.Methanol.getFluid(300), Materials.Acetone.getFluid(50),
                Materials.MethylAcetate.getFluid(10) },
            GTValues.NI,
            40,
            256);
        addUniversalDistillationRecipewithCircuit(
            Materials.WoodTar.getFluid(1000),
            new ItemStack[] { GTUtility.getIntegratedCircuit(1) },
            new FluidStack[] { Materials.Creosote.getFluid(250), Materials.Phenol.getFluid(100),
                Materials.Benzene.getFluid(400), Materials.Toluene.getFluid(100),
                Materials.Dimethylbenzene.getFluid(150) },
            GTValues.NI,
            40,
            256);

        addUniversalDistillationRecipewithCircuit(
            Materials.OilLight.getFluid(100),
            new ItemStack[] { GTUtility.getIntegratedCircuit(1) },
            new FluidStack[] { Materials.SulfuricHeavyFuel.getFluid(7), Materials.SulfuricLightFuel.getFluid(13),
                Materials.SulfuricNaphtha.getFluid(20), Materials.SulfuricGas.getGas(160) },
            null,
            10,
            96);
        addUniversalDistillationRecipewithCircuit(
            Materials.OilMedium.getFluid(100),
            new ItemStack[] { GTUtility.getIntegratedCircuit(1) },
            new FluidStack[] { Materials.SulfuricHeavyFuel.getFluid(10), Materials.SulfuricLightFuel.getFluid(50),
                Materials.SulfuricNaphtha.getFluid(150), Materials.SulfuricGas.getGas(60) },
            null,
            20,
            96);
        addUniversalDistillationRecipewithCircuit(
            Materials.Oil.getFluid(100L),
            new ItemStack[] { GTUtility.getIntegratedCircuit(1) },
            new FluidStack[] { Materials.SulfuricHeavyFuel.getFluid(30), Materials.SulfuricLightFuel.getFluid(100),
                Materials.SulfuricNaphtha.getFluid(40), Materials.SulfuricGas.getGas(120) },
            null,
            30,
            96);
        addUniversalDistillationRecipewithCircuit(
            Materials.OilHeavy.getFluid(100),
            new ItemStack[] { GTUtility.getIntegratedCircuit(1) },
            new FluidStack[] { Materials.SulfuricHeavyFuel.getFluid(100), Materials.SulfuricLightFuel.getFluid(45),
                Materials.SulfuricNaphtha.getFluid(15), Materials.SulfuricGas.getGas(60) },
            null,
            40,
            288);

        // 2 0.5HCl(Diluted) = HCl + H2O
        addUniversalDistillationRecipe(
            Materials.DilutedHydrochloricAcid.getFluid(2000),
            new FluidStack[] { Materials.Water.getFluid(1000), Materials.HydrochloricAcid.getFluid(1000) },
            GTValues.NI,
            600,
            64);

        addUniversalDistillationRecipe(
            getFluidStack("potion.vinegar", 40),
            new FluidStack[] { Materials.AceticAcid.getFluid(5), Materials.Water.getFluid(35) },
            GTValues.NI,
            20,
            64);

        addUniversalDistillationRecipe(
            Materials.CalciumAcetateSolution.getFluid(1000),
            new FluidStack[] { Materials.Acetone.getFluid(1000), Materials.CarbonDioxide.getGas(1000) },
            Materials.Quicklime.getDust(2),
            80,
            480);

        addUniversalDistillationRecipe(
            Materials.DilutedSulfuricAcid.getFluid(3000),
            new FluidStack[] { Materials.SulfuricAcid.getFluid(2000), Materials.Water.getFluid(1000) },
            GTValues.NI,
            600,
            120);

        // C3H6O = C2H2O + CH4
        addUniversalDistillationRecipe(
            Materials.Acetone.getFluid(1000),
            new FluidStack[] { Materials.Ethenone.getGas(1000), Materials.Methane.getGas(1000) },
            GTValues.NI,
            80,
            640);

        addUniversalDistillationRecipe(
            Materials.Gas.getGas(1000),
            new FluidStack[] { Materials.Butane.getGas(60), Materials.Propane.getGas(70), Materials.Ethane.getGas(100),
                Materials.Methane.getGas(750), Materials.Helium.getGas(20) },
            GTValues.NI,
            240,
            120);

        addUniversalDistillationRecipe(
            Materials.Ethylene.getLightlyHydroCracked(1000),
            new FluidStack[] { Materials.Ethane.getGas(1000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Ethylene.getModeratelyHydroCracked(1000),
            new FluidStack[] { Materials.Methane.getGas(2000) },
            null,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Ethylene.getSeverelyHydroCracked(1000),
            new FluidStack[] { Materials.Methane.getGas(2000), Materials.Hydrogen.getGas(2000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Ethylene.getLightlySteamCracked(1000),
            new FluidStack[] { Materials.Methane.getGas(1000) },
            Materials.Carbon.getDust(1),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Ethylene.getModeratelySteamCracked(1000),
            new FluidStack[] { Materials.Methane.getGas(1000) },
            Materials.Carbon.getDust(1),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Ethylene.getSeverelySteamCracked(1000),
            new FluidStack[] { Materials.Methane.getGas(1000) },
            Materials.Carbon.getDust(1),
            120,
            120);

        addUniversalDistillationRecipe(
            Materials.Ethane.getLightlyHydroCracked(1000),
            new FluidStack[] { Materials.Methane.getGas(2000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Ethane.getModeratelyHydroCracked(1000),
            new FluidStack[] { Materials.Methane.getGas(2000), Materials.Hydrogen.getGas(2000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Ethane.getSeverelyHydroCracked(1000),
            new FluidStack[] { Materials.Methane.getGas(2000), Materials.Hydrogen.getGas(4000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Ethane.getLightlySteamCracked(1000),
            new FluidStack[] { Materials.Ethylene.getGas(250), Materials.Methane.getGas(1250) },
            Materials.Carbon.getDustSmall(1),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Ethane.getModeratelySteamCracked(1000),
            new FluidStack[] { Materials.Ethylene.getGas(125), Materials.Methane.getGas(1375) },
            Materials.Carbon.getDustTiny(6),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Ethane.getSeverelySteamCracked(1000),
            new FluidStack[] { Materials.Methane.getGas(1500) },
            Materials.Carbon.getDustSmall(2),
            120,
            120);

        addUniversalDistillationRecipe(
            Materials.Propene.getLightlyHydroCracked(1000),
            new FluidStack[] { Materials.Propane.getGas(500), Materials.Ethylene.getGas(500),
                Materials.Methane.getGas(500) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Propene.getModeratelyHydroCracked(1000),
            new FluidStack[] { Materials.Ethane.getGas(1000), Materials.Methane.getGas(1000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Propene.getSeverelyHydroCracked(1000),
            new FluidStack[] { Materials.Methane.getGas(3000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Propene.getLightlySteamCracked(1000),
            new FluidStack[] { Materials.Ethylene.getGas(1000), Materials.Methane.getGas(500) },
            Materials.Carbon.getDustSmall(2),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Propene.getModeratelySteamCracked(1000),
            new FluidStack[] { Materials.Ethylene.getGas(750), Materials.Methane.getGas(750) },
            Materials.Carbon.getDustSmall(3),
            180,
            120);
        addUniversalDistillationRecipe(
            Materials.Propene.getSeverelySteamCracked(1000),
            new FluidStack[] { Materials.Methane.getGas(1500) },
            Materials.Carbon.getDustSmall(6),
            180,
            120);

        addUniversalDistillationRecipe(
            Materials.Propane.getLightlyHydroCracked(1000),
            new FluidStack[] { Materials.Ethane.getGas(1000), Materials.Methane.getGas(1000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Propane.getModeratelyHydroCracked(1000),
            new FluidStack[] { Materials.Methane.getGas(3000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Propane.getSeverelyHydroCracked(1000),
            new FluidStack[] { Materials.Methane.getGas(3000), Materials.Hydrogen.getGas(2000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Propane.getLightlySteamCracked(1000),
            new FluidStack[] { Materials.Ethylene.getGas(750), Materials.Methane.getGas(1250) },
            Materials.Carbon.getDustTiny(2),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Propane.getModeratelySteamCracked(1000),
            new FluidStack[] { Materials.Ethylene.getGas(500), Materials.Methane.getGas(1500) },
            Materials.Carbon.getDustSmall(1),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Propane.getSeverelySteamCracked(1000),
            new FluidStack[] { Materials.Ethylene.getGas(250), Materials.Methane.getGas(1750) },
            Materials.Carbon.getDustTiny(4),
            120,
            120);

        addUniversalDistillationRecipe(
            Materials.Butadiene.getLightlyHydroCracked(1000),
            new FluidStack[] { Materials.Butene.getGas(667), Materials.Ethylene.getGas(667) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butadiene.getModeratelyHydroCracked(1000),
            new FluidStack[] { Materials.Butane.getGas(223), Materials.Propene.getGas(223),
                Materials.Ethane.getGas(400), Materials.Ethylene.getGas(445), Materials.Methane.getGas(223) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butadiene.getSeverelyHydroCracked(1000),
            new FluidStack[] { Materials.Propane.getGas(260), Materials.Ethane.getGas(926),
                Materials.Ethylene.getGas(389), Materials.Methane.getGas(2667) },
            GTValues.NI,
            112,
            120);
        addUniversalDistillationRecipe(
            Materials.Butadiene.getLightlySteamCracked(1000),
            new FluidStack[] { Materials.Propene.getGas(750), Materials.Ethylene.getGas(188),
                Materials.Methane.getGas(188) },
            Materials.Carbon.getDustSmall(3),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butadiene.getModeratelySteamCracked(1000),
            new FluidStack[] { Materials.Propene.getGas(125), Materials.Ethylene.getGas(1125),
                Materials.Methane.getGas(188) },
            Materials.Carbon.getDustSmall(3),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butadiene.getSeverelySteamCracked(1000),
            new FluidStack[] { Materials.Propene.getGas(125), Materials.Ethylene.getGas(188),
                Materials.Methane.getGas(1125) },
            Materials.Carbon.getDust(1),
            120,
            120);

        addUniversalDistillationRecipe(
            Materials.Butene.getLightlyHydroCracked(1000),
            new FluidStack[] { Materials.Butane.getGas(334), Materials.Propene.getGas(334),
                Materials.Ethane.getGas(334), Materials.Ethylene.getGas(334), Materials.Methane.getGas(334) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butene.getModeratelyHydroCracked(1000),
            new FluidStack[] { Materials.Propane.getGas(389), Materials.Ethane.getGas(556),
                Materials.Ethylene.getGas(334), Materials.Methane.getGas(1056) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butene.getSeverelyHydroCracked(1000),
            new FluidStack[] { Materials.Ethane.getGas(1000), Materials.Methane.getGas(2000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butene.getLightlySteamCracked(1000),
            new FluidStack[] { Materials.Propene.getGas(750), Materials.Ethylene.getGas(500),
                Materials.Methane.getGas(250) },
            Materials.Carbon.getDustSmall(1),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butene.getModeratelySteamCracked(1000),
            new FluidStack[] { Materials.Propene.getGas(200), Materials.Ethylene.getGas(1300),
                Materials.Methane.getGas(400) },
            Materials.Carbon.getDustSmall(1),
            192,
            120);
        addUniversalDistillationRecipe(
            Materials.Butene.getSeverelySteamCracked(1000),
            new FluidStack[] { Materials.Propene.getGas(125), Materials.Ethylene.getGas(313),
                Materials.Methane.getGas(1500) },
            Materials.Carbon.getDustSmall(6),
            120,
            120);

        addUniversalDistillationRecipe(
            Materials.Butane.getLightlyHydroCracked(1000),
            new FluidStack[] { Materials.Propane.getGas(667), Materials.Ethane.getGas(667),
                Materials.Methane.getGas(667) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butane.getModeratelyHydroCracked(1000),
            new FluidStack[] { Materials.Ethane.getGas(1000), Materials.Methane.getGas(2000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butane.getSeverelyHydroCracked(1000),
            new FluidStack[] { Materials.Methane.getGas(1000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butane.getLightlySteamCracked(1000),
            new FluidStack[] { Materials.Propane.getGas(750), Materials.Ethane.getGas(125),
                Materials.Ethylene.getGas(125), Materials.Methane.getGas(1063) },
            Materials.Carbon.getDustTiny(2),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butane.getModeratelySteamCracked(1000),
            new FluidStack[] { Materials.Propane.getGas(125), Materials.Ethane.getGas(750),
                Materials.Ethylene.getGas(750), Materials.Methane.getGas(438) },
            Materials.Carbon.getDustTiny(2),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butane.getSeverelySteamCracked(1000),
            new FluidStack[] { Materials.Propane.getGas(125), Materials.Ethane.getGas(125),
                Materials.Ethylene.getGas(125), Materials.Methane.getGas(2000) },
            Materials.Carbon.getDustTiny(11),
            120,
            120);

        addUniversalDistillationRecipe(
            Materials.Gas.getLightlyHydroCracked(1000),
            new FluidStack[] { Materials.Methane.getGas(1300), Materials.Hydrogen.getGas(1500),
                Materials.Helium.getGas(100) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Gas.getModeratelyHydroCracked(1000),
            new FluidStack[] { Materials.Methane.getGas(1400), Materials.Hydrogen.getGas(3000),
                Materials.Helium.getGas(150) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Gas.getSeverelyHydroCracked(1000),
            new FluidStack[] { Materials.Methane.getGas(1500), Materials.Hydrogen.getGas(4000),
                Materials.Helium.getGas(200) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Gas.getLightlySteamCracked(1000),
            new FluidStack[] { Materials.Propene.getGas(50), Materials.Ethane.getGas(10),
                Materials.Ethylene.getGas(100), Materials.Methane.getGas(500), Materials.Helium.getGas(50) },
            Materials.Carbon.getDustTiny(1),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Gas.getModeratelySteamCracked(1000),
            new FluidStack[] { Materials.Propene.getGas(10), Materials.Ethane.getGas(50),
                Materials.Ethylene.getGas(200), Materials.Methane.getGas(600), Materials.Helium.getGas(70) },
            Materials.Carbon.getDustTiny(1),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Gas.getSeverelySteamCracked(1000),
            new FluidStack[] { Materials.Propene.getGas(10), Materials.Ethane.getGas(10),
                Materials.Ethylene.getGas(300), Materials.Methane.getGas(700), Materials.Helium.getGas(100) },
            Materials.Carbon.getDustTiny(1),
            120,
            120);

        addUniversalDistillationRecipe(
            Materials.Naphtha.getLightlyHydroCracked(1000),
            new FluidStack[] { Materials.Butane.getGas(800), Materials.Propane.getGas(300),
                Materials.Ethane.getGas(250), Materials.Methane.getGas(250) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Naphtha.getModeratelyHydroCracked(1000),
            new FluidStack[] { Materials.Butane.getGas(200), Materials.Propane.getGas(1100),
                Materials.Ethane.getGas(400), Materials.Methane.getGas(400) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Naphtha.getSeverelyHydroCracked(1000),
            new FluidStack[] { Materials.Butane.getGas(125), Materials.Propane.getGas(125),
                Materials.Ethane.getGas(1500), Materials.Methane.getGas(1500) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Naphtha.getLightlySteamCracked(1000),
            new FluidStack[] { Materials.HeavyFuel.getFluid(75), Materials.LightFuel.getFluid(150),
                Materials.Toluene.getFluid(40), Materials.Benzene.getFluid(150), Materials.Butene.getGas(80),
                Materials.Butadiene.getGas(150), Materials.Propane.getGas(15), Materials.Propene.getGas(200),
                Materials.Ethane.getGas(35), Materials.Ethylene.getGas(200), Materials.Methane.getGas(200) },
            Materials.Carbon.getDustTiny(1),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Naphtha.getModeratelySteamCracked(1000),
            new FluidStack[] { Materials.HeavyFuel.getFluid(50), Materials.LightFuel.getFluid(100),
                Materials.Toluene.getFluid(30), Materials.Benzene.getFluid(125), Materials.Butene.getGas(65),
                Materials.Butadiene.getGas(100), Materials.Propane.getGas(30), Materials.Propene.getGas(400),
                Materials.Ethane.getGas(50), Materials.Ethylene.getGas(350), Materials.Methane.getGas(350) },
            Materials.Carbon.getDustTiny(2),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Naphtha.getSeverelySteamCracked(1000),
            new FluidStack[] { Materials.HeavyFuel.getFluid(25), Materials.LightFuel.getFluid(50),
                Materials.Toluene.getFluid(20), Materials.Benzene.getFluid(100), Materials.Butene.getGas(50),
                Materials.Butadiene.getGas(50), Materials.Propane.getGas(15), Materials.Propene.getGas(300),
                Materials.Ethane.getGas(65), Materials.Ethylene.getGas(500), Materials.Methane.getGas(500) },
            Materials.Carbon.getDustTiny(3),
            120,
            120);

        addUniversalDistillationRecipe(
            Materials.LightFuel.getLightlyHydroCracked(1000),
            new FluidStack[] { Materials.Naphtha.getFluid(800), Materials.Octane.getFluid(100),
                Materials.Butane.getGas(150), Materials.Propane.getGas(200), Materials.Ethane.getGas(125),
                Materials.Methane.getGas(125) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.LightFuel.getModeratelyHydroCracked(1000),
            new FluidStack[] { Materials.Naphtha.getFluid(500), Materials.Octane.getFluid(50),
                Materials.Butane.getGas(200), Materials.Propane.getGas(1100), Materials.Ethane.getGas(400),
                Materials.Methane.getGas(400) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.LightFuel.getSeverelyHydroCracked(1000),
            new FluidStack[] { Materials.Naphtha.getFluid(200), Materials.Octane.getFluid(20),
                Materials.Butane.getGas(125), Materials.Propane.getGas(125), Materials.Ethane.getGas(1500),
                Materials.Methane.getGas(1500) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.LightFuel.getLightlySteamCracked(1000),
            new FluidStack[] { Materials.HeavyFuel.getFluid(150), Materials.Naphtha.getFluid(400),
                Materials.Toluene.getFluid(40), Materials.Benzene.getFluid(200), Materials.Butene.getGas(75),
                Materials.Butadiene.getGas(60), Materials.Propane.getGas(20), Materials.Propene.getGas(150),
                Materials.Ethane.getGas(10), Materials.Ethylene.getGas(50), Materials.Methane.getGas(50) },
            Materials.Carbon.getDustTiny(1),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.LightFuel.getModeratelySteamCracked(1000),
            new FluidStack[] { Materials.HeavyFuel.getFluid(100), Materials.Naphtha.getFluid(250),
                Materials.Toluene.getFluid(50), Materials.Benzene.getFluid(300), Materials.Butene.getGas(90),
                Materials.Butadiene.getGas(75), Materials.Propane.getGas(35), Materials.Propene.getGas(200),
                Materials.Ethane.getGas(30), Materials.Ethylene.getGas(150), Materials.Methane.getGas(150) },
            Materials.Carbon.getDustTiny(2),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.LightFuel.getSeverelySteamCracked(1000),
            new FluidStack[] { Materials.HeavyFuel.getFluid(50), Materials.Naphtha.getFluid(100),
                Materials.Toluene.getFluid(30), Materials.Benzene.getFluid(150), Materials.Butene.getGas(65),
                Materials.Butadiene.getGas(50), Materials.Propane.getGas(50), Materials.Propene.getGas(250),
                Materials.Ethane.getGas(50), Materials.Ethylene.getGas(250), Materials.Methane.getGas(250) },
            Materials.Carbon.getDustTiny(3),
            120,
            120);

        addUniversalDistillationRecipe(
            Materials.HeavyFuel.getLightlyHydroCracked(1000),
            new FluidStack[] { Materials.LightFuel.getFluid(600), Materials.Naphtha.getFluid(100),
                Materials.Butane.getGas(100), Materials.Propane.getGas(100), Materials.Ethane.getGas(75),
                Materials.Methane.getGas(75) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.HeavyFuel.getModeratelyHydroCracked(1000),
            new FluidStack[] { Materials.LightFuel.getFluid(400), Materials.Naphtha.getFluid(400),
                Materials.Butane.getGas(150), Materials.Propane.getGas(150), Materials.Ethane.getGas(100),
                Materials.Methane.getGas(100) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.HeavyFuel.getSeverelyHydroCracked(1000),
            new FluidStack[] { Materials.LightFuel.getFluid(200), Materials.Naphtha.getFluid(250),
                Materials.Butane.getGas(300), Materials.Propane.getGas(300), Materials.Ethane.getGas(175),
                Materials.Methane.getGas(175) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.HeavyFuel.getLightlySteamCracked(1000),
            new FluidStack[] { Materials.LightFuel.getFluid(300), Materials.Naphtha.getFluid(50),
                Materials.Toluene.getFluid(25), Materials.Benzene.getFluid(125), Materials.Butene.getGas(25),
                Materials.Butadiene.getGas(15), Materials.Propane.getGas(3), Materials.Propene.getGas(30),
                Materials.Ethane.getGas(5), Materials.Ethylene.getGas(50), Materials.Methane.getGas(50) },
            Materials.Carbon.getDustTiny(1),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.HeavyFuel.getModeratelySteamCracked(1000),
            new FluidStack[] { Materials.LightFuel.getFluid(200), Materials.Naphtha.getFluid(200),
                Materials.Toluene.getFluid(40), Materials.Benzene.getFluid(200), Materials.Butene.getGas(40),
                Materials.Butadiene.getGas(25), Materials.Propane.getGas(5), Materials.Propene.getGas(50),
                Materials.Ethane.getGas(7), Materials.Ethylene.getGas(75), Materials.Methane.getGas(75) },
            Materials.Carbon.getDustTiny(2),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.HeavyFuel.getSeverelySteamCracked(1000),
            new FluidStack[] { Materials.LightFuel.getFluid(100), Materials.Naphtha.getFluid(125),
                Materials.Toluene.getFluid(80), Materials.Benzene.getFluid(400), Materials.Butene.getGas(80),
                Materials.Butadiene.getGas(50), Materials.Propane.getGas(10), Materials.Propene.getGas(100),
                Materials.Ethane.getGas(15), Materials.Ethylene.getGas(150), Materials.Methane.getGas(150) },
            Materials.Carbon.getDustTiny(3),
            120,
            120);
    }

    public void addUniversalDistillationRecipewithCircuit(FluidStack aInput, ItemStack[] aCircuit,
        FluidStack[] aOutputs, ItemStack aOutput2, int aDuration, int aEUt) {
        for (int i = 0; i < Math.min(aOutputs.length, 11); i++) {
            GTRecipeBuilder buildDistillation = GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.getIntegratedCircuit(i + 1));
            if (aOutput2 != GTValues.NI) {
                buildDistillation.itemOutputs(aOutput2);
            }
            buildDistillation.fluidInputs(aInput)
                .fluidOutputs(aOutputs[i])
                .duration(2 * aDuration)
                .eut(aEUt / 4)
                .addTo(distilleryRecipes);
        }
        GTRecipeBuilder buildDT = GTValues.RA.stdBuilder()
            .itemInputs(aCircuit);
        if (aOutput2 != GTValues.NI) {
            buildDT.itemOutputs(aOutput2);
        }
        buildDT.fluidInputs(aInput)
            .fluidOutputs(aOutputs)
            .duration(aDuration)
            .eut(aEUt)
            .addTo(distillationTowerRecipes);
    }

    public void addUniversalDistillationRecipe(FluidStack aInput, FluidStack[] aOutputs, ItemStack aOutput2,
        int aDuration, int aEUt) {
        for (int i = 0; i < Math.min(aOutputs.length, 11); i++) {
            GTRecipeBuilder buildDistillation = GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.getIntegratedCircuit(i + 1));
            if (aOutput2 != GTValues.NI) {
                buildDistillation.itemOutputs(aOutput2);
            }
            buildDistillation.fluidInputs(aInput)
                .fluidOutputs(aOutputs[i])
                .duration(2 * aDuration)
                .eut(aEUt / 4)
                .addTo(distilleryRecipes);
        }
        GTRecipeBuilder buildDT = GTValues.RA.stdBuilder();
        if (aOutput2 != GTValues.NI) {
            buildDT.itemOutputs(aOutput2);
        }
        buildDT.fluidInputs(aInput)
            .fluidOutputs(aOutputs)
            .duration(aDuration)
            .eut(aEUt)
            .addTo(distillationTowerRecipes);
    }
}
