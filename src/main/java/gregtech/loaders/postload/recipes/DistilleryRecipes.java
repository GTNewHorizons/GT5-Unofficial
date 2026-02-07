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
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeBuilder;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class DistilleryRecipes implements Runnable {

    @Override
    public void run() {

        distillationTowerRecipes();
        universalDistillationTowerRecipes();

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(Materials.Creosote.getFluid(25))
            .fluidOutputs(Materials.Lubricant.getFluid(8))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(Materials.SeedOil.getFluid(8))
            .fluidOutputs(Materials.Lubricant.getFluid(2))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(Materials.FishOil.getFluid(8))
            .fluidOutputs(Materials.Lubricant.getFluid(2))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(Materials.Oil.getFluid(12))
            .fluidOutputs(Materials.Lubricant.getFluid(6))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(Materials.OilLight.getFluid(12))
            .fluidOutputs(Materials.Lubricant.getFluid(3))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(Materials.OilMedium.getFluid(12))
            .fluidOutputs(Materials.Lubricant.getFluid(6))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(Materials.OilHeavy.getFluid(12))
            .fluidOutputs(Materials.Lubricant.getFluid(9))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(5)
            .fluidInputs(Materials.Biomass.getFluid(40))
            .fluidOutputs(Materials.Water.getFluid(12))
            .duration(16 * TICKS)
            .eut(24)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Water.getFluid(10))
            .fluidOutputs(GTModHandler.getDistilledWater(10))
            .duration(1 * SECONDS + 5 * TICKS)
            .eut(8)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(getFluidStack("potion.potatojuice", 2))
            .fluidOutputs(getFluidStack("potion.vodka", 1))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(getFluidStack("potion.lemonade", 2))
            .fluidOutputs(getFluidStack("potion.alcopops", 1))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(Materials.SluiceSand.getDust(1))
            .fluidInputs(Materials.SluiceJuice.getFluid(1_000))
            .fluidOutputs(Materials.Water.getFluid(500))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(6)
            .fluidInputs(Materials.WoodTar.getFluid(200))
            .fluidOutputs(Materials.IIIDimethylbenzene.getFluid(30))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(6)
            .fluidInputs(Materials.CharcoalByproducts.getGas(200))
            .fluidOutputs(Materials.IIIDimethylbenzene.getFluid(20))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(7)
            .fluidInputs(Materials.WoodTar.getFluid(200))
            .fluidOutputs(Materials.IVDimethylbenzene.getFluid(30))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(7)
            .fluidInputs(Materials.CharcoalByproducts.getGas(200))
            .fluidOutputs(Materials.IVDimethylbenzene.getFluid(20))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(distilleryRecipes);

        // (NaCl·H2O) = NaCl + H2O

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(Materials.Salt.getDust(2))
            .fluidInputs(Materials.SaltWater.getFluid(1_000))
            .fluidOutputs(GTModHandler.getDistilledWater(1_000))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(ItemList.IC2_Fertilizer.get(1))
            .fluidInputs(Materials.FermentedBiomass.getFluid(1_000))
            .fluidOutputs(Materials.AceticAcid.getFluid(25))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(8)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .itemOutputs(ItemList.IC2_Fertilizer.get(1))
            .fluidInputs(Materials.FermentedBiomass.getFluid(1_000))
            .fluidOutputs(Materials.Water.getFluid(375))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(8)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(3)
            .itemOutputs(ItemList.IC2_Fertilizer.get(1))
            .fluidInputs(Materials.FermentedBiomass.getFluid(1_000))
            .fluidOutputs(Materials.Ethanol.getFluid(150))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(8)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(4)
            .itemOutputs(ItemList.IC2_Fertilizer.get(1))
            .fluidInputs(Materials.FermentedBiomass.getFluid(1_000))
            .fluidOutputs(Materials.Methanol.getFluid(150))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(8)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(5)
            .itemOutputs(ItemList.IC2_Fertilizer.get(1))
            .fluidInputs(Materials.FermentedBiomass.getFluid(1_000))
            .fluidOutputs(Materials.Ammonia.getGas(100))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(8)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(6)
            .itemOutputs(ItemList.IC2_Fertilizer.get(1))
            .fluidInputs(Materials.FermentedBiomass.getFluid(1_000))
            .fluidOutputs(Materials.CarbonDioxide.getGas(400))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(8)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(7)
            .itemOutputs(ItemList.IC2_Fertilizer.get(1))
            .fluidInputs(Materials.FermentedBiomass.getFluid(1_000))
            .fluidOutputs(Materials.Methane.getGas(600))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(8)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(17)
            .itemOutputs(ItemList.IC2_Fertilizer.get(1))
            .fluidInputs(Materials.FermentedBiomass.getFluid(1_000))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("ic2biogas"), 1_800))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(8)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(Materials.Methane.getGas(100))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("ic2biogas"), 300))
            .duration(16 * TICKS)
            .eut(8)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(Materials.HeavyFuel.getFluid(100))
            .fluidOutputs(Materials.Benzene.getFluid(40))
            .duration(8 * SECONDS)
            .eut(24)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(3)
            .fluidInputs(Materials.HeavyFuel.getFluid(100))
            .fluidOutputs(Materials.Phenol.getFluid(25))
            .duration(8 * SECONDS)
            .eut(24)
            .addTo(distilleryRecipes);
        // Dimethylbenzene

        GTValues.RA.stdBuilder()
            .circuit(5)
            .fluidInputs(Materials.CharcoalByproducts.getGas(200))
            .fluidOutputs(Materials.Dimethylbenzene.getFluid(20))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(new FluidStack(ItemList.sOilExtraHeavy, 10))
            .fluidOutputs(Materials.OilHeavy.getFluid(15))
            .duration(16 * TICKS)
            .eut(24)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(Materials.HeavyFuel.getFluid(10))
            .fluidOutputs(new FluidStack(ItemList.sToluene, 4))
            .duration(16 * TICKS)
            .eut(24)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(new FluidStack(ItemList.sToluene, 30))
            .fluidOutputs(Materials.LightFuel.getFluid(30))
            .duration(16 * TICKS)
            .eut(24)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("ic2biomass"), 20))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("ic2biogas"), 32))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("ic2biomass"), 4))
            .fluidOutputs(Materials.Water.getFluid(2))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(getFluidStack("potion.wheatyjuice", 75))
            .fluidOutputs(getFluidStack("potion.scotch", 1))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distilleryRecipes);

        if (TinkerConstruct.isModLoaded()) {

            GTValues.RA.stdBuilder()
                .circuit(1)
                .fluidInputs(Materials.Glue.getFluid(8))
                .fluidOutputs(getFluidStack("glue", 8))
                .duration(1 * TICKS)
                .eut(24)
                .addTo(distilleryRecipes);

            GTValues.RA.stdBuilder()
                .circuit(1)
                .fluidInputs(getFluidStack("glue", 8))
                .fluidOutputs(Materials.Glue.getFluid(4))
                .duration(1 * TICKS)
                .eut(24)
                .addTo(distilleryRecipes);

        }
    }

    public void distillationTowerRecipes() {
        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(Materials.Creosote.getFluid(1_000))
            .fluidOutputs(Materials.Lubricant.getFluid(500))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(Materials.SeedOil.getFluid(1_400))
            .fluidOutputs(Materials.Lubricant.getFluid(500))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(Materials.FishOil.getFluid(1_200))
            .fluidOutputs(Materials.Lubricant.getFluid(500))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Wood, 2L))
            .fluidInputs(Materials.Biomass.getFluid(1_000))
            .fluidOutputs(Materials.Ethanol.getFluid(600), Materials.Water.getFluid(300))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(400)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Water.getFluid(1_000))
            .fluidOutputs(GTModHandler.getDistilledWater(1_000))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(Materials.OilLight.getFluid(1_000))
            .fluidOutputs(Materials.Lubricant.getFluid(250))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(Materials.OilMedium.getFluid(1_000))
            .fluidOutputs(Materials.Lubricant.getFluid(500))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(Materials.OilHeavy.getFluid(1_000))
            .fluidOutputs(Materials.Lubricant.getFluid(750))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        // C15H10N2O2(5HCl) = C15H10N2O2 + 5HCl

        GTValues.RA.stdBuilder()
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.DiphenylmethaneDiisocyanate, 29L))
            .fluidInputs(Materials.DiphenylmethaneDiisocyanateMixture.getFluid(1_000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(5_000))
            .duration(2 * MINUTES + 5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .itemOutputs(Materials.Charcoal.getDustSmall(1))
            .fluidInputs(Materials.CharcoalByproducts.getGas(1_000))
            .fluidOutputs(
                Materials.WoodTar.getFluid(250),
                Materials.WoodVinegar.getFluid(400),
                Materials.WoodGas.getGas(250),
                Materials.IIIDimethylbenzene.getFluid(100))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(Materials.WoodTar.getFluid(1_000))
            .fluidOutputs(
                Materials.Creosote.getFluid(250),
                Materials.Phenol.getFluid(100),
                Materials.Benzene.getFluid(400),
                Materials.Toluene.getFluid(100),
                Materials.IIIDimethylbenzene.getFluid(150))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(3)
            .fluidInputs(Materials.WoodTar.getFluid(1_000))
            .fluidOutputs(
                Materials.Creosote.getFluid(250),
                Materials.Phenol.getFluid(100),
                Materials.Benzene.getFluid(400),
                Materials.Toluene.getFluid(100),
                Materials.IVDimethylbenzene.getFluid(150))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(3)
            .itemOutputs(Materials.Charcoal.getDustSmall(1))
            .fluidInputs(Materials.CharcoalByproducts.getGas(1_000))
            .fluidOutputs(
                Materials.WoodTar.getFluid(250),
                Materials.WoodVinegar.getFluid(400),
                Materials.WoodGas.getGas(250),
                Materials.IVDimethylbenzene.getFluid(100))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(4)
            .itemOutputs(Materials.Charcoal.getDustSmall(1))
            .fluidInputs(Materials.CharcoalByproducts.getGas(1_000))
            .fluidOutputs(
                Materials.WoodTar.getFluid(250),
                Materials.WoodVinegar.getFluid(400),
                Materials.WoodGas.getGas(250),
                Materials.Dimethylbenzene.getFluid(20),
                Materials.IIIDimethylbenzene.getFluid(60),
                Materials.IVDimethylbenzene.getFluid(20))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(4)
            .fluidInputs(Materials.WoodTar.getFluid(1_000))
            .fluidOutputs(
                Materials.Creosote.getFluid(250),
                Materials.Phenol.getFluid(100),
                Materials.Benzene.getFluid(400),
                Materials.Toluene.getFluid(100),
                Materials.Dimethylbenzene.getFluid(30),
                Materials.IIIDimethylbenzene.getFluid(90),
                Materials.IVDimethylbenzene.getFluid(30))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(Materials.OilLight.getFluid(1_000))
            .fluidOutputs(
                Materials.SulfuricHeavyFuel.getFluid(70),
                Materials.SulfuricLightFuel.getFluid(130),
                Materials.SulfuricNaphtha.getFluid(200),
                Materials.NaphthenicAcid.getFluid(15),
                Materials.SulfuricGas.getGas(1600))
            .duration(1 * SECONDS + 1 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(Materials.OilMedium.getFluid(1_000))
            .fluidOutputs(
                Materials.SulfuricHeavyFuel.getFluid(100),
                Materials.SulfuricLightFuel.getFluid(500),
                Materials.SulfuricNaphtha.getFluid(1_500),
                Materials.NaphthenicAcid.getFluid(25),
                Materials.SulfuricGas.getGas(600))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(Materials.Oil.getFluid(1_000))
            .fluidOutputs(
                Materials.SulfuricHeavyFuel.getFluid(300),
                Materials.SulfuricLightFuel.getFluid(1_000),
                Materials.SulfuricNaphtha.getFluid(400),
                Materials.NaphthenicAcid.getFluid(50),
                Materials.SulfuricGas.getGas(1_200))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(Materials.OilHeavy.getFluid(1_000))
            .fluidOutputs(
                Materials.SulfuricHeavyFuel.getFluid(1_000),
                Materials.SulfuricLightFuel.getFluid(450),
                Materials.SulfuricNaphtha.getFluid(150),
                Materials.NaphthenicAcid.getFluid(50),
                Materials.SulfuricGas.getGas(600))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(distillationTowerRecipes);

        // 9C5H12O = 4C6H14O + 5CH4O + 4C4H8

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.MTBEMixture.getGas(900))
            .fluidOutputs(
                Materials.AntiKnock.getFluid(400),
                Materials.Methanol.getFluid(500),
                Materials.Butene.getGas(400))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.MTBEMixtureAlt.getGas(900))
            .fluidOutputs(
                Materials.AntiKnock.getFluid(400),
                Materials.Methanol.getFluid(500),
                Materials.Butane.getGas(400))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(ItemList.IC2_Fertilizer.get(1))
            .fluidInputs(Materials.FermentedBiomass.getFluid(1_000))
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
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("ic2biomass"), 3_000))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("ic2biogas"), 8_000), Materials.Water.getFluid(125))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(distillationTowerRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(new FluidStack(ItemList.sOilExtraHeavy, 1_000))
            .fluidOutputs(Materials.OilHeavy.getFluid(1_500))
            .duration(16 * TICKS)
            .eut(2400)
            .addTo(distillationTowerRecipes);
    }

    public void universalDistillationTowerRecipes() {
        addUniversalDistillationRecipewithCircuit(
            Materials.CharcoalByproducts.getGas(1_000),
            1,
            new FluidStack[] { Materials.WoodTar.getFluid(250), Materials.WoodVinegar.getFluid(400),
                Materials.WoodGas.getGas(250), Materials.Dimethylbenzene.getFluid(100) },
            Materials.Charcoal.getDustSmall(1),
            40,
            256);

        addUniversalDistillationRecipewithCircuit(
            Materials.WoodGas.getGas(1_000),
            1,
            new FluidStack[] { Materials.CarbonDioxide.getGas(390), Materials.Ethylene.getGas(120),
                Materials.Methane.getGas(130), Materials.CarbonMonoxide.getGas(240), Materials.Hydrogen.getGas(120) },
            GTValues.NI,
            40,
            256);
        addUniversalDistillationRecipewithCircuit(
            Materials.WoodVinegar.getFluid(1_000),
            1,
            new FluidStack[] { Materials.AceticAcid.getFluid(100), Materials.Water.getFluid(500),
                Materials.Ethanol.getFluid(10), Materials.Methanol.getFluid(300), Materials.Acetone.getFluid(50),
                Materials.MethylAcetate.getFluid(10) },
            GTValues.NI,
            40,
            256);
        addUniversalDistillationRecipewithCircuit(
            Materials.WoodTar.getFluid(1_000),
            1,
            new FluidStack[] { Materials.Creosote.getFluid(250), Materials.Phenol.getFluid(100),
                Materials.Benzene.getFluid(400), Materials.Toluene.getFluid(100),
                Materials.Dimethylbenzene.getFluid(150) },
            GTValues.NI,
            40,
            256);

        addUniversalDistillationRecipewithCircuit(
            Materials.OilLight.getFluid(100),
            1,
            new FluidStack[] { Materials.SulfuricHeavyFuel.getFluid(7), Materials.SulfuricLightFuel.getFluid(13),
                Materials.SulfuricNaphtha.getFluid(20), Materials.SulfuricGas.getGas(160) },
            null,
            10,
            96);
        addUniversalDistillationRecipewithCircuit(
            Materials.OilMedium.getFluid(100),
            1,
            new FluidStack[] { Materials.SulfuricHeavyFuel.getFluid(10), Materials.SulfuricLightFuel.getFluid(50),
                Materials.SulfuricNaphtha.getFluid(150), Materials.SulfuricGas.getGas(60) },
            null,
            20,
            96);
        addUniversalDistillationRecipewithCircuit(
            Materials.Oil.getFluid(100),
            1,
            new FluidStack[] { Materials.SulfuricHeavyFuel.getFluid(30), Materials.SulfuricLightFuel.getFluid(100),
                Materials.SulfuricNaphtha.getFluid(40), Materials.SulfuricGas.getGas(120) },
            null,
            30,
            96);
        addUniversalDistillationRecipewithCircuit(
            Materials.OilHeavy.getFluid(100),
            1,
            new FluidStack[] { Materials.SulfuricHeavyFuel.getFluid(100), Materials.SulfuricLightFuel.getFluid(45),
                Materials.SulfuricNaphtha.getFluid(15), Materials.SulfuricGas.getGas(60) },
            null,
            40,
            288);

        // 2 0.5HCl(Diluted) = HCl + H2O
        addUniversalDistillationRecipe(
            Materials.DilutedHydrochloricAcid.getFluid(2_000),
            new FluidStack[] { Materials.Water.getFluid(1_000), Materials.HydrochloricAcid.getFluid(1_000) },
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
            Materials.CalciumAcetateSolution.getFluid(1_000),
            new FluidStack[] { Materials.Acetone.getFluid(1_000), Materials.CarbonDioxide.getGas(1_000) },
            Materials.Quicklime.getDust(2),
            80,
            480);

        addUniversalDistillationRecipe(
            Materials.DilutedSulfuricAcid.getFluid(3_000),
            new FluidStack[] { Materials.SulfuricAcid.getFluid(2_000), Materials.Water.getFluid(1_000) },
            GTValues.NI,
            600,
            120);

        // C3H6O = C2H2O + CH4
        addUniversalDistillationRecipe(
            Materials.Acetone.getFluid(1_000),
            new FluidStack[] { Materials.Ethenone.getGas(1_000), Materials.Methane.getGas(1_000) },
            GTValues.NI,
            80,
            640);

        addUniversalDistillationRecipe(
            Materials.Gas.getGas(1_000),
            new FluidStack[] { Materials.Butane.getGas(60), Materials.Propane.getGas(70), Materials.Ethane.getGas(100),
                Materials.Methane.getGas(750), Materials.Helium.getGas(20) },
            GTValues.NI,
            240,
            120);

        addUniversalDistillationRecipe(
            Materials.Ethylene.getLightlyHydroCracked(1_000),
            new FluidStack[] { Materials.Ethane.getGas(1_000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Ethylene.getModeratelyHydroCracked(1_000),
            new FluidStack[] { Materials.Methane.getGas(2_000) },
            null,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Ethylene.getSeverelyHydroCracked(1_000),
            new FluidStack[] { Materials.Methane.getGas(2_000), Materials.Hydrogen.getGas(2_000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Ethylene.getLightlySteamCracked(1_000),
            new FluidStack[] { Materials.Methane.getGas(1_000) },
            Materials.Carbon.getDust(1),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Ethylene.getModeratelySteamCracked(1_000),
            new FluidStack[] { Materials.Methane.getGas(1_000) },
            Materials.Carbon.getDust(1),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Ethylene.getSeverelySteamCracked(1_000),
            new FluidStack[] { Materials.Methane.getGas(1_000) },
            Materials.Carbon.getDust(1),
            120,
            120);

        addUniversalDistillationRecipe(
            Materials.Ethane.getLightlyHydroCracked(1_000),
            new FluidStack[] { Materials.Methane.getGas(2_000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Ethane.getModeratelyHydroCracked(1_000),
            new FluidStack[] { Materials.Methane.getGas(2_000), Materials.Hydrogen.getGas(2_000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Ethane.getSeverelyHydroCracked(1_000),
            new FluidStack[] { Materials.Methane.getGas(2_000), Materials.Hydrogen.getGas(4_000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Ethane.getLightlySteamCracked(1_000),
            new FluidStack[] { Materials.Ethylene.getGas(250), Materials.Methane.getGas(1_250) },
            Materials.Carbon.getDustSmall(1),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Ethane.getModeratelySteamCracked(1_000),
            new FluidStack[] { Materials.Ethylene.getGas(125), Materials.Methane.getGas(1_375) },
            Materials.Carbon.getDustTiny(6),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Ethane.getSeverelySteamCracked(1_000),
            new FluidStack[] { Materials.Methane.getGas(1_500) },
            Materials.Carbon.getDustSmall(2),
            120,
            120);

        addUniversalDistillationRecipe(
            Materials.Propene.getLightlyHydroCracked(1_000),
            new FluidStack[] { Materials.Propane.getGas(500), Materials.Ethylene.getGas(500),
                Materials.Methane.getGas(500) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Propene.getModeratelyHydroCracked(1_000),
            new FluidStack[] { Materials.Ethane.getGas(1_000), Materials.Methane.getGas(1_000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Propene.getSeverelyHydroCracked(1_000),
            new FluidStack[] { Materials.Methane.getGas(3_000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Propene.getLightlySteamCracked(1_000),
            new FluidStack[] { Materials.Ethylene.getGas(1_000), Materials.Methane.getGas(500) },
            Materials.Carbon.getDustSmall(2),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Propene.getModeratelySteamCracked(1_000),
            new FluidStack[] { Materials.Ethylene.getGas(750), Materials.Methane.getGas(750) },
            Materials.Carbon.getDustSmall(3),
            180,
            120);
        addUniversalDistillationRecipe(
            Materials.Propene.getSeverelySteamCracked(1_000),
            new FluidStack[] { Materials.Methane.getGas(1_500) },
            Materials.Carbon.getDustSmall(6),
            180,
            120);

        addUniversalDistillationRecipe(
            Materials.Propane.getLightlyHydroCracked(1_000),
            new FluidStack[] { Materials.Ethane.getGas(1_000), Materials.Methane.getGas(1_000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Propane.getModeratelyHydroCracked(1_000),
            new FluidStack[] { Materials.Methane.getGas(3_000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Propane.getSeverelyHydroCracked(1_000),
            new FluidStack[] { Materials.Methane.getGas(3_000), Materials.Hydrogen.getGas(2_000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Propane.getLightlySteamCracked(1_000),
            new FluidStack[] { Materials.Ethylene.getGas(750), Materials.Methane.getGas(1_250) },
            Materials.Carbon.getDustTiny(2),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Propane.getModeratelySteamCracked(1_000),
            new FluidStack[] { Materials.Ethylene.getGas(500), Materials.Methane.getGas(1_500) },
            Materials.Carbon.getDustSmall(1),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Propane.getSeverelySteamCracked(1_000),
            new FluidStack[] { Materials.Ethylene.getGas(250), Materials.Methane.getGas(1_750) },
            Materials.Carbon.getDustTiny(4),
            120,
            120);

        addUniversalDistillationRecipe(
            Materials.Butadiene.getLightlyHydroCracked(1_000),
            new FluidStack[] { Materials.Butene.getGas(667), Materials.Ethylene.getGas(667) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butadiene.getModeratelyHydroCracked(1_000),
            new FluidStack[] { Materials.Butane.getGas(223), Materials.Propene.getGas(223),
                Materials.Ethane.getGas(400), Materials.Ethylene.getGas(445), Materials.Methane.getGas(223) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butadiene.getSeverelyHydroCracked(1_000),
            new FluidStack[] { Materials.Propane.getGas(260), Materials.Ethane.getGas(926),
                Materials.Ethylene.getGas(389), Materials.Methane.getGas(2_667) },
            GTValues.NI,
            112,
            120);
        addUniversalDistillationRecipe(
            Materials.Butadiene.getLightlySteamCracked(1_000),
            new FluidStack[] { Materials.Propene.getGas(750), Materials.Ethylene.getGas(188),
                Materials.Methane.getGas(188) },
            Materials.Carbon.getDustSmall(3),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butadiene.getModeratelySteamCracked(1_000),
            new FluidStack[] { Materials.Propene.getGas(125), Materials.Ethylene.getGas(1_125),
                Materials.Methane.getGas(188) },
            Materials.Carbon.getDustSmall(3),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butadiene.getSeverelySteamCracked(1_000),
            new FluidStack[] { Materials.Propene.getGas(125), Materials.Ethylene.getGas(188),
                Materials.Methane.getGas(1_125) },
            Materials.Carbon.getDust(1),
            120,
            120);

        addUniversalDistillationRecipe(
            Materials.Butene.getLightlyHydroCracked(1_000),
            new FluidStack[] { Materials.Butane.getGas(334), Materials.Propene.getGas(334),
                Materials.Ethane.getGas(334), Materials.Ethylene.getGas(334), Materials.Methane.getGas(334) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butene.getModeratelyHydroCracked(1_000),
            new FluidStack[] { Materials.Propane.getGas(389), Materials.Ethane.getGas(556),
                Materials.Ethylene.getGas(334), Materials.Methane.getGas(1_056) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butene.getSeverelyHydroCracked(1_000),
            new FluidStack[] { Materials.Ethane.getGas(1_000), Materials.Methane.getGas(2_000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butene.getLightlySteamCracked(1_000),
            new FluidStack[] { Materials.Propene.getGas(750), Materials.Ethylene.getGas(500),
                Materials.Methane.getGas(250) },
            Materials.Carbon.getDustSmall(1),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butene.getModeratelySteamCracked(1_000),
            new FluidStack[] { Materials.Propene.getGas(200), Materials.Ethylene.getGas(1_300),
                Materials.Methane.getGas(400) },
            Materials.Carbon.getDustSmall(1),
            192,
            120);
        addUniversalDistillationRecipe(
            Materials.Butene.getSeverelySteamCracked(1_000),
            new FluidStack[] { Materials.Propene.getGas(125), Materials.Ethylene.getGas(313),
                Materials.Methane.getGas(1_500) },
            Materials.Carbon.getDustSmall(6),
            120,
            120);

        addUniversalDistillationRecipe(
            Materials.Butane.getLightlyHydroCracked(1_000),
            new FluidStack[] { Materials.Propane.getGas(667), Materials.Ethane.getGas(667),
                Materials.Methane.getGas(667) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butane.getModeratelyHydroCracked(1_000),
            new FluidStack[] { Materials.Ethane.getGas(1_000), Materials.Methane.getGas(2_000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butane.getSeverelyHydroCracked(1_000),
            new FluidStack[] { Materials.Methane.getGas(1_000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butane.getLightlySteamCracked(1_000),
            new FluidStack[] { Materials.Propane.getGas(750), Materials.Ethane.getGas(125),
                Materials.Ethylene.getGas(125), Materials.Methane.getGas(1_063) },
            Materials.Carbon.getDustTiny(2),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butane.getModeratelySteamCracked(1_000),
            new FluidStack[] { Materials.Propane.getGas(125), Materials.Ethane.getGas(750),
                Materials.Ethylene.getGas(750), Materials.Methane.getGas(438) },
            Materials.Carbon.getDustTiny(2),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butane.getSeverelySteamCracked(1_000),
            new FluidStack[] { Materials.Propane.getGas(125), Materials.Ethane.getGas(125),
                Materials.Ethylene.getGas(125), Materials.Methane.getGas(2_000) },
            Materials.Carbon.getDustTiny(11),
            120,
            120);

        addUniversalDistillationRecipe(
            Materials.Gas.getLightlyHydroCracked(1_000),
            new FluidStack[] { Materials.Methane.getGas(1_300), Materials.Hydrogen.getGas(1_500),
                Materials.Helium.getGas(100) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Gas.getModeratelyHydroCracked(1_000),
            new FluidStack[] { Materials.Methane.getGas(1_400), Materials.Hydrogen.getGas(3_000),
                Materials.Helium.getGas(150) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Gas.getSeverelyHydroCracked(1_000),
            new FluidStack[] { Materials.Methane.getGas(1_500), Materials.Hydrogen.getGas(4_000),
                Materials.Helium.getGas(200) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Gas.getLightlySteamCracked(1_000),
            new FluidStack[] { Materials.Propene.getGas(50), Materials.Ethane.getGas(10),
                Materials.Ethylene.getGas(100), Materials.Methane.getGas(500), Materials.Helium.getGas(50) },
            Materials.Carbon.getDustTiny(1),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Gas.getModeratelySteamCracked(1_000),
            new FluidStack[] { Materials.Propene.getGas(10), Materials.Ethane.getGas(50),
                Materials.Ethylene.getGas(200), Materials.Methane.getGas(600), Materials.Helium.getGas(70) },
            Materials.Carbon.getDustTiny(1),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Gas.getSeverelySteamCracked(1_000),
            new FluidStack[] { Materials.Propene.getGas(10), Materials.Ethane.getGas(10),
                Materials.Ethylene.getGas(300), Materials.Methane.getGas(700), Materials.Helium.getGas(100) },
            Materials.Carbon.getDustTiny(1),
            120,
            120);

        addUniversalDistillationRecipe(
            Materials.Naphtha.getLightlyHydroCracked(1_000),
            new FluidStack[] { Materials.Butane.getGas(800), Materials.Propane.getGas(300),
                Materials.Ethane.getGas(250), Materials.Methane.getGas(250) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Naphtha.getModeratelyHydroCracked(1_000),
            new FluidStack[] { Materials.Butane.getGas(200), Materials.Propane.getGas(1_100),
                Materials.Ethane.getGas(400), Materials.Methane.getGas(400) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Naphtha.getSeverelyHydroCracked(1_000),
            new FluidStack[] { Materials.Butane.getGas(125), Materials.Propane.getGas(125),
                Materials.Ethane.getGas(1_500), Materials.Methane.getGas(1_500) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Naphtha.getLightlySteamCracked(1_000),
            new FluidStack[] { Materials.HeavyFuel.getFluid(75), Materials.LightFuel.getFluid(150),
                Materials.Toluene.getFluid(40), Materials.Benzene.getFluid(150), Materials.Butene.getGas(80),
                Materials.Butadiene.getGas(150), Materials.Propane.getGas(15), Materials.Propene.getGas(200),
                Materials.Ethane.getGas(35), Materials.Ethylene.getGas(200), Materials.Methane.getGas(200) },
            Materials.Carbon.getDustTiny(1),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Naphtha.getModeratelySteamCracked(1_000),
            new FluidStack[] { Materials.HeavyFuel.getFluid(50), Materials.LightFuel.getFluid(100),
                Materials.Toluene.getFluid(30), Materials.Benzene.getFluid(125), Materials.Butene.getGas(65),
                Materials.Butadiene.getGas(100), Materials.Propane.getGas(30), Materials.Propene.getGas(400),
                Materials.Ethane.getGas(50), Materials.Ethylene.getGas(350), Materials.Methane.getGas(350) },
            Materials.Carbon.getDustTiny(2),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Naphtha.getSeverelySteamCracked(1_000),
            new FluidStack[] { Materials.HeavyFuel.getFluid(25), Materials.LightFuel.getFluid(50),
                Materials.Toluene.getFluid(20), Materials.Benzene.getFluid(100), Materials.Butene.getGas(50),
                Materials.Butadiene.getGas(50), Materials.Propane.getGas(15), Materials.Propene.getGas(300),
                Materials.Ethane.getGas(65), Materials.Ethylene.getGas(500), Materials.Methane.getGas(500) },
            Materials.Carbon.getDustTiny(3),
            120,
            120);

        addUniversalDistillationRecipe(
            Materials.LightFuel.getLightlyHydroCracked(1_000),
            new FluidStack[] { Materials.Naphtha.getFluid(800), Materials.Octane.getFluid(100),
                Materials.Butane.getGas(150), Materials.Propane.getGas(200), Materials.Ethane.getGas(125),
                Materials.Methane.getGas(125) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.LightFuel.getModeratelyHydroCracked(1_000),
            new FluidStack[] { Materials.Naphtha.getFluid(500), Materials.Octane.getFluid(50),
                Materials.Butane.getGas(200), Materials.Propane.getGas(1_100), Materials.Ethane.getGas(400),
                Materials.Methane.getGas(400) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.LightFuel.getSeverelyHydroCracked(1_000),
            new FluidStack[] { Materials.Naphtha.getFluid(200), Materials.Octane.getFluid(20),
                Materials.Butane.getGas(125), Materials.Propane.getGas(125), Materials.Ethane.getGas(1_500),
                Materials.Methane.getGas(1_500) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.LightFuel.getLightlySteamCracked(1_000),
            new FluidStack[] { Materials.HeavyFuel.getFluid(150), Materials.Naphtha.getFluid(400),
                Materials.Toluene.getFluid(40), Materials.Benzene.getFluid(200), Materials.Butene.getGas(75),
                Materials.Butadiene.getGas(60), Materials.Propane.getGas(20), Materials.Propene.getGas(150),
                Materials.Ethane.getGas(10), Materials.Ethylene.getGas(50), Materials.Methane.getGas(50) },
            Materials.Carbon.getDustTiny(1),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.LightFuel.getModeratelySteamCracked(1_000),
            new FluidStack[] { Materials.HeavyFuel.getFluid(100), Materials.Naphtha.getFluid(250),
                Materials.Toluene.getFluid(50), Materials.Benzene.getFluid(300), Materials.Butene.getGas(90),
                Materials.Butadiene.getGas(75), Materials.Propane.getGas(35), Materials.Propene.getGas(200),
                Materials.Ethane.getGas(30), Materials.Ethylene.getGas(150), Materials.Methane.getGas(150) },
            Materials.Carbon.getDustTiny(2),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.LightFuel.getSeverelySteamCracked(1_000),
            new FluidStack[] { Materials.HeavyFuel.getFluid(50), Materials.Naphtha.getFluid(100),
                Materials.Toluene.getFluid(30), Materials.Benzene.getFluid(150), Materials.Butene.getGas(65),
                Materials.Butadiene.getGas(50), Materials.Propane.getGas(50), Materials.Propene.getGas(250),
                Materials.Ethane.getGas(50), Materials.Ethylene.getGas(250), Materials.Methane.getGas(250) },
            Materials.Carbon.getDustTiny(3),
            120,
            120);

        addUniversalDistillationRecipe(
            Materials.HeavyFuel.getLightlyHydroCracked(1_000),
            new FluidStack[] { Materials.LightFuel.getFluid(600), Materials.Naphtha.getFluid(100),
                Materials.Butane.getGas(100), Materials.Propane.getGas(100), Materials.Ethane.getGas(75),
                Materials.Methane.getGas(75) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.HeavyFuel.getModeratelyHydroCracked(1_000),
            new FluidStack[] { Materials.LightFuel.getFluid(400), Materials.Naphtha.getFluid(400),
                Materials.Butane.getGas(150), Materials.Propane.getGas(150), Materials.Ethane.getGas(100),
                Materials.Methane.getGas(100) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.HeavyFuel.getSeverelyHydroCracked(1_000),
            new FluidStack[] { Materials.LightFuel.getFluid(200), Materials.Naphtha.getFluid(250),
                Materials.Butane.getGas(300), Materials.Propane.getGas(300), Materials.Ethane.getGas(175),
                Materials.Methane.getGas(175) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.HeavyFuel.getLightlySteamCracked(1_000),
            new FluidStack[] { Materials.LightFuel.getFluid(300), Materials.Naphtha.getFluid(50),
                Materials.Toluene.getFluid(25), Materials.Benzene.getFluid(125), Materials.Butene.getGas(25),
                Materials.Butadiene.getGas(15), Materials.Propane.getGas(3), Materials.Propene.getGas(30),
                Materials.Ethane.getGas(5), Materials.Ethylene.getGas(50), Materials.Methane.getGas(50) },
            Materials.Carbon.getDustTiny(1),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.HeavyFuel.getModeratelySteamCracked(1_000),
            new FluidStack[] { Materials.LightFuel.getFluid(200), Materials.Naphtha.getFluid(200),
                Materials.Toluene.getFluid(40), Materials.Benzene.getFluid(200), Materials.Butene.getGas(40),
                Materials.Butadiene.getGas(25), Materials.Propane.getGas(5), Materials.Propene.getGas(50),
                Materials.Ethane.getGas(7), Materials.Ethylene.getGas(75), Materials.Methane.getGas(75) },
            Materials.Carbon.getDustTiny(2),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.HeavyFuel.getSeverelySteamCracked(1_000),
            new FluidStack[] { Materials.LightFuel.getFluid(100), Materials.Naphtha.getFluid(125),
                Materials.Toluene.getFluid(80), Materials.Benzene.getFluid(400), Materials.Butene.getGas(80),
                Materials.Butadiene.getGas(50), Materials.Propane.getGas(10), Materials.Propene.getGas(100),
                Materials.Ethane.getGas(15), Materials.Ethylene.getGas(150), Materials.Methane.getGas(150) },
            Materials.Carbon.getDustTiny(3),
            120,
            120);
    }

    public void addUniversalDistillationRecipewithCircuit(FluidStack aInput, int aCircuit, FluidStack[] aOutputs,
        ItemStack aOutput2, int aDuration, int aEUt) {
        GTRecipeBuilder buildDT = GTValues.RA.stdBuilder()
            .circuit(aCircuit);
        if (aOutput2 != GTValues.NI) {
            buildDT.itemOutputs(aOutput2);
        }
        buildDT.fluidInputs(aInput)
            .fluidOutputs(aOutputs)
            .duration(aDuration)
            .eut(aEUt)
            .addTo(distillationTowerRecipes);

        for (int i = 0; i < Math.min(aOutputs.length, 11); i++) {
            GTRecipeBuilder buildDistillation = GTValues.RA.stdBuilder()
                .circuit(i + 1);
            int ratio = getRatioForDistillery(aInput, aOutputs[i], aOutput2);

            FluidStack aInputDivided = new FluidStack(aInput, Math.max(1, aInput.amount / ratio));
            FluidStack aOutputDivided = new FluidStack(aOutputs[i], Math.max(1, aOutputs[i].amount / ratio));
            ItemStack aOutput2Divided;
            aOutput2Divided = aOutput2;

            if (aOutput2Divided != GTValues.NI) {
                aOutput2Divided.stackSize /= ratio;
                buildDistillation.itemOutputs(aOutput2Divided);
            }

            buildDistillation.fluidInputs(aInputDivided)
                .fluidOutputs(aOutputDivided)
                .duration(2 * aDuration / ratio)
                .eut(aEUt / 4)
                .addTo(distilleryRecipes);
        }
    }

    public void addUniversalDistillationRecipe(FluidStack aInput, FluidStack[] aOutputs, ItemStack aOutput2,
        int aDuration, int aEUt) {
        GTRecipeBuilder buildDT = GTValues.RA.stdBuilder();
        if (aOutput2 != GTValues.NI) {
            buildDT.itemOutputs(aOutput2);
        }
        buildDT.fluidInputs(aInput)
            .fluidOutputs(aOutputs)
            .duration(aDuration)
            .eut(aEUt)
            .addTo(distillationTowerRecipes);

        for (int i = 0; i < Math.min(aOutputs.length, 11); i++) {
            GTRecipeBuilder buildDistillation = GTValues.RA.stdBuilder()
                .circuit(i + 1);
            int ratio = getRatioForDistillery(aInput, aOutputs[i], aOutput2);

            FluidStack aInputDivided = new FluidStack(aInput, Math.max(1, aInput.amount / ratio));
            FluidStack aOutputDivided = new FluidStack(aOutputs[i], Math.max(1, aOutputs[i].amount / ratio));
            ItemStack aOutput2Divided;
            aOutput2Divided = aOutput2;

            if (aOutput2Divided != GTValues.NI) {
                aOutput2Divided.stackSize /= ratio;
                buildDistillation.itemOutputs(aOutput2Divided);
            }

            buildDistillation.fluidInputs(aInputDivided)
                .fluidOutputs(aOutputDivided)
                .duration(2 * aDuration / ratio)
                .eut(aEUt / 4)
                .addTo(distilleryRecipes);
        }
    }

    private static int getRatioForDistillery(FluidStack aInput, FluidStack aOutput, ItemStack aOutput2) {
        int[] divisors = new int[] { 2, 5, 10, 25, 50 };
        int ratio = -1;

        for (int divisor : divisors) {

            if (!isFluidStackDivisibleForDistillery(aInput, divisor)) continue;

            if (!isFluidStackDivisibleForDistillery(aOutput, divisor)) continue;

            if (aOutput2 != null && aOutput2.stackSize % divisor != 0) continue;

            ratio = divisor;
        }

        return Math.max(1, ratio);
    }

    private static boolean isFluidStackDivisibleForDistillery(FluidStack fluidStack, int divisor) {
        return fluidStack.amount % divisor == 0 && fluidStack.amount / divisor >= 25;
    }
}
