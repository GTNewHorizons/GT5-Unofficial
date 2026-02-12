package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.primitiveBlastRecipes;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.ADDITIVE_AMOUNT;
import static gregtech.api.util.GTRecipeConstants.BlastFurnaceWithGas;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.NO_GAS;
import static gregtech.api.util.GTRecipeConstants.NO_GAS_CIRCUIT_CONFIG;

import net.minecraft.item.ItemStack;

import bartworks.system.material.WerkstoffLoader;
import goodgenerator.items.GGMaterial;
import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialsElements;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class BlastFurnaceRecipes implements Runnable {

    @Override
    public void run() {
        this.registerBlastFurnaceRecipes();
        this.registerPrimitiveBlastFurnaceRecipes();
    }

    public void registerBlastFurnaceRecipes() {
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Gypsum.getDust(8))
            .itemOutputs(Materials.Quicklime.getDust(1))
            .fluidOutputs(Materials.DilutedSulfuricAcid.getFluid(1_500))
            .duration(10 * SECONDS)
            .eut((int) TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 3200)
            .addTo(blastFurnaceRecipes);

        // Carbothermic Reduction
        // Depend on real amount except real ores
        int outputIngotAmount = GTMod.proxy.mMixedOreOnlyYieldsTwoThirdsOfPureOre ? 2 : 3;

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.RoastedCopper.getDust(2), Materials.Carbon.getDust(1))
            .itemOutputs(Materials.Copper.getIngots(outputIngotAmount), Materials.Ash.getDust(1))
            .outputChances(10000, 2222)
            .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.RoastedAntimony.getDust(2), Materials.Carbon.getDust(1))
            .itemOutputs(Materials.Antimony.getIngots(outputIngotAmount), Materials.Ash.getDust(1))
            .outputChances(10000, 2222)
            .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.RoastedIron.getDust(2), Materials.Carbon.getDust(1))
            .itemOutputs(Materials.Iron.getIngots(outputIngotAmount), Materials.Ash.getDust(1))
            .outputChances(10000, 2222)
            .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.RoastedNickel.getDust(2), Materials.Carbon.getDust(1))
            .itemOutputs(Materials.Nickel.getIngots(outputIngotAmount), Materials.Ash.getDust(1))
            .outputChances(10000, 2222)
            .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.RoastedZinc.getDust(2), Materials.Carbon.getDust(1))
            .itemOutputs(Materials.Zinc.getIngots(outputIngotAmount), Materials.Ash.getDust(1))
            .outputChances(10000, 2222)
            .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.RoastedCobalt.getDust(2), Materials.Carbon.getDust(1))
            .itemOutputs(Materials.Cobalt.getIngots(outputIngotAmount), Materials.Ash.getDust(1))
            .outputChances(10000, 2222)
            .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.RoastedArsenic.getDust(2), Materials.Carbon.getDust(1))
            .itemOutputs(Materials.Arsenic.getIngots(outputIngotAmount), Materials.Ash.getDust(1))
            .outputChances(10000, 2222)
            .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.RoastedLead.getDust(2), Materials.Carbon.getDust(1))
            .itemOutputs(Materials.Lead.getIngots(outputIngotAmount), Materials.Ash.getDust(1))
            .outputChances(10000, 2222)
            .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Malachite.getDust(2), Materials.Carbon.getDust(1))
            .itemOutputs(Materials.Copper.getIngots(outputIngotAmount), Materials.Ash.getDust(1))
            .outputChances(10000, 2222)
            .fluidOutputs(Materials.CarbonDioxide.getGas(3_000))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.BandedIron.getDust(2), Materials.Carbon.getDust(1))
            .itemOutputs(Materials.Iron.getIngots(outputIngotAmount), Materials.Ash.getDust(1))
            .outputChances(10000, 2222)
            .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Garnierite.getDust(2), Materials.Carbon.getDust(1))
            .itemOutputs(Materials.Nickel.getIngots(outputIngotAmount), Materials.Ash.getDust(1))
            .outputChances(10000, 2222)
            .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Magnetite.getDust(2), Materials.Carbon.getDust(1))
            .itemOutputs(Materials.Iron.getIngots(outputIngotAmount), Materials.Ash.getDust(1))
            .outputChances(10000, 2222)
            .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.YellowLimonite.getDust(2), Materials.Carbon.getDust(1))
            .itemOutputs(Materials.Iron.getIngots(outputIngotAmount), Materials.Ash.getDust(1))
            .outputChances(10000, 2222)
            .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.BrownLimonite.getDust(2), Materials.Carbon.getDust(1))
            .itemOutputs(Materials.Iron.getIngots(outputIngotAmount), Materials.Ash.getDust(1))
            .outputChances(10000, 2222)
            .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.BasalticMineralSand.getDust(2), Materials.Carbon.getDust(1))
            .itemOutputs(Materials.Iron.getIngots(outputIngotAmount), Materials.Ash.getDust(1))
            .outputChances(10000, 2222)
            .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.GraniticMineralSand.getDust(2), Materials.Carbon.getDust(1))
            .itemOutputs(Materials.Iron.getIngots(outputIngotAmount), Materials.Ash.getDust(1))
            .outputChances(10000, 2222)
            .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Cassiterite.getDust(2), Materials.Carbon.getDust(1))
            .itemOutputs(Materials.Tin.getIngots(outputIngotAmount), Materials.Ash.getDust(1))
            .outputChances(10000, 2222)
            .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.CassiteriteSand.getDust(2), Materials.Carbon.getDust(1))
            .itemOutputs(Materials.Tin.getIngots(outputIngotAmount), Materials.Ash.getDust(1))
            .outputChances(10000, 2222)
            .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.SiliconDioxide.getDust(3), Materials.Carbon.getDust(2))
            .itemOutputs(Materials.Silicon.getIngots(1), Materials.Ash.getDust(1))
            .outputChances(10000, 1111)
            .fluidOutputs(Materials.CarbonMonoxide.getGas(2_000))
            .duration(4 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        if (GTMod.proxy.mMixedOreOnlyYieldsTwoThirdsOfPureOre) {
            GTValues.RA.stdBuilder()
                .itemInputs(Materials.CupricOxide.getDust(2), Materials.Carbon.getDustSmall(4))
                .itemOutputs(Materials.Copper.getIngots(1), Materials.Ash.getDust(1))
                .outputChances(10000, 2222)
                .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(Materials.Malachite.getDust(2), Materials.Carbon.getDustSmall(4))
                .itemOutputs(Materials.Copper.getIngots(outputIngotAmount), Materials.Ash.getDust(1))
                .outputChances(10000, 2222)
                .fluidOutputs(Materials.CarbonDioxide.getGas(3_000))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(Materials.AntimonyTrioxide.getDust(5), Materials.Carbon.getDustSmall(4))
                .itemOutputs(Materials.Antimony.getIngots(2), Materials.Ash.getDust(1))
                .outputChances(10000, 2222)
                .fluidOutputs(Materials.CarbonDioxide.getGas(3_000))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(Materials.BandedIron.getDust(5), Materials.Carbon.getDustSmall(4))
                .itemOutputs(Materials.Iron.getIngots(2), Materials.Ash.getDust(1))
                .outputChances(10000, 2222)
                .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(Materials.Magnetite.getDust(2), Materials.Carbon.getDustSmall(4))
                .itemOutputs(Materials.Iron.getIngots(outputIngotAmount), Materials.Ash.getDust(1))
                .outputChances(10000, 2222)
                .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(Materials.YellowLimonite.getDust(2), Materials.Carbon.getDustSmall(4))
                .itemOutputs(Materials.Iron.getIngots(outputIngotAmount), Materials.Ash.getDust(1))
                .outputChances(10000, 2222)
                .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(Materials.BrownLimonite.getDust(2), Materials.Carbon.getDustSmall(4))
                .itemOutputs(Materials.Iron.getIngots(outputIngotAmount), Materials.Ash.getDust(1))
                .outputChances(10000, 2222)
                .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(Materials.BasalticMineralSand.getDust(2), Materials.Carbon.getDustSmall(4))
                .itemOutputs(Materials.Iron.getIngots(outputIngotAmount), Materials.Ash.getDust(1))
                .outputChances(10000, 2222)
                .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(Materials.GraniticMineralSand.getDust(2), Materials.Carbon.getDustSmall(4))
                .itemOutputs(Materials.Iron.getIngots(outputIngotAmount), Materials.Ash.getDust(1))
                .outputChances(10000, 2222)
                .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(Materials.Cassiterite.getDust(2), Materials.Carbon.getDustSmall(4))
                .itemOutputs(Materials.Tin.getIngots(outputIngotAmount), Materials.Ash.getDust(1))
                .outputChances(10000, 2222)
                .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(Materials.CassiteriteSand.getDust(2), Materials.Carbon.getDustSmall(4))
                .itemOutputs(Materials.Tin.getIngots(outputIngotAmount), Materials.Ash.getDust(1))
                .outputChances(10000, 2222)
                .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(Materials.Garnierite.getDust(2), Materials.Carbon.getDustSmall(4))
                .itemOutputs(Materials.Nickel.getIngots(1), Materials.Ash.getDust(1))
                .outputChances(10000, 2222)
                .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(Materials.CobaltOxide.getDust(2), Materials.Carbon.getDustSmall(4))
                .itemOutputs(Materials.Cobalt.getIngots(1), Materials.Ash.getDust(1))
                .outputChances(10000, 2222)
                .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(Materials.ArsenicTrioxide.getDust(5), Materials.Carbon.getDustSmall(4))
                .itemOutputs(Materials.Arsenic.getIngots(2), Materials.Ash.getDust(1))
                .outputChances(10000, 2222)
                .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(Materials.Massicot.getDust(2), Materials.Carbon.getDustSmall(4))
                .itemOutputs(Materials.Lead.getIngots(1), Materials.Ash.getDust(1))
                .outputChances(10000, 2222)
                .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 32),
                ItemList.GalliumArsenideCrystalSmallPart.get(1))
            .circuit(2)
            .itemOutputs(ItemList.Circuit_Silicon_Ingot.get(1))
            .duration(7 * MINUTES + 30 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1784)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 64),
                ItemList.GalliumArsenideCrystalSmallPart.get(2),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Phosphorus, 8))
            .circuit(3)
            .itemOutputs(ItemList.Circuit_Silicon_Ingot2.get(1))
            .duration(10 * MINUTES)
            .eut((int) TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 2484)
            .metadata(ADDITIVE_AMOUNT, 8000)
            .addTo(BlastFurnaceWithGas);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.SiliconSG, 16),
                ItemList.GalliumArsenideCrystal.get(1),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Naquadah, 1))
            .circuit(3)
            .itemOutputs(ItemList.Circuit_Silicon_Ingot3.get(1))
            .duration(12 * MINUTES + 30 * SECONDS)
            .eut((int) TierEU.RECIPE_EV)
            .metadata(COIL_HEAT, 4484)
            .metadata(ADDITIVE_AMOUNT, 8000)
            .addTo(BlastFurnaceWithGas);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.SiliconSG, 32),
                ItemList.GalliumArsenideCrystal.get(2),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Europium, 2))
            .circuit(3)
            .itemOutputs(ItemList.Circuit_Silicon_Ingot4.get(1))
            .duration(15 * MINUTES)
            .eut((int) TierEU.RECIPE_IV)
            .metadata(COIL_HEAT, 6484)
            .metadata(ADDITIVE_AMOUNT, 8000)
            .addTo(BlastFurnaceWithGas);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.SiliconSG, 64),
                ItemList.GalliumArsenideCrystal.get(4),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Americium, 4))
            .circuit(3)
            .itemOutputs(ItemList.Circuit_Silicon_Ingot5.get(1))
            .duration(17 * MINUTES + 30 * SECONDS)
            .eut((int) TierEU.RECIPE_LuV)
            .metadata(COIL_HEAT, 9000)
            .metadata(ADDITIVE_AMOUNT, 16000)
            .addTo(BlastFurnaceWithGas);

        // CaH2 + 2Si = CaSi2 + 2H

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Calciumhydride, 3),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 2))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.CalciumDisilicide, 3))
            .fluidOutputs(Materials.Hydrogen.getGas(2_000))
            .duration(15 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1273)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.SuperconductorUEVBase, 1))
            .circuit(11)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingotHot, Materials.SuperconductorUEVBase, 1))
            .duration(13 * MINUTES + 6 * SECONDS)
            .eut((int) TierEU.RECIPE_UV)
            .metadata(COIL_HEAT, 11800)
            .metadata(ADDITIVE_AMOUNT, 1000)
            .metadata(NO_GAS, true)
            .metadata(NO_GAS_CIRCUIT_CONFIG, 1)
            .addTo(BlastFurnaceWithGas);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.SuperconductorUIVBase, 1))
            .circuit(11)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingotHot, Materials.SuperconductorUIVBase, 1))
            .duration(13 * MINUTES + 6 * SECONDS)
            .eut((int) TierEU.RECIPE_UHV)
            .metadata(COIL_HEAT, 12700)
            .metadata(ADDITIVE_AMOUNT, 1000)
            .metadata(NO_GAS, true)
            .metadata(NO_GAS_CIRCUIT_CONFIG, 1)
            .addTo(BlastFurnaceWithGas);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.SuperconductorUMVBase, 1))
            .circuit(11)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingotHot, Materials.SuperconductorUMVBase, 1))
            .duration(13 * MINUTES + 6 * SECONDS)
            .eut((int) TierEU.RECIPE_UEV)
            .metadata(COIL_HEAT, 13600)
            .metadata(ADDITIVE_AMOUNT, 1000)
            .metadata(NO_GAS, true)
            .metadata(NO_GAS_CIRCUIT_CONFIG, 1)
            .addTo(BlastFurnaceWithGas);

        // CaO + 3C = CaC2 + CO

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Quicklime.getDust(2), Materials.Carbon.getDust(3))
            .itemOutputs(Materials.CalciumCarbide.getDust(3))
            .fluidOutputs(Materials.CarbonMonoxide.getGas(1_000))
            .duration(30 * SECONDS)
            .eut((int) TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 2573)
            .addTo(blastFurnaceRecipes);

        // Ni + 3Al = NiAl3

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Nickel.getDust(1), Materials.Aluminium.getDust(3))
            .itemOutputs(Materials.NickelAluminide.getIngots(4))
            .duration(45 * SECONDS)
            .eut((int) TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 1688)
            .addTo(blastFurnaceRecipes);

        ItemStack[] tSiliconDioxide = new ItemStack[] { Materials.SiliconDioxide.getDust(3),
            Materials.NetherQuartz.getDust(3), Materials.CertusQuartz.getDust(3), Materials.Quartzite.getDust(6) };

        // Roasting

        for (ItemStack silicon : tSiliconDioxide) {
            GTValues.RA.stdBuilder()
                .itemInputs(Materials.Chalcopyrite.getDust(1), silicon)
                .itemOutputs(Materials.RoastedCopper.getDust(1), Materials.Ferrosilite.getDust(5))
                .fluidInputs(Materials.Oxygen.getGas(3_000))
                .fluidOutputs(Materials.SulfurDioxide.getGas(2_000))
                .duration(6 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Tetrahedrite.getDust(1))
            .itemOutputs(Materials.RoastedCopper.getDust(1), Materials.RoastedAntimony.getDustTiny(3))
            .fluidInputs(Materials.Oxygen.getGas(3_000))
            .fluidOutputs(Materials.SulfurDioxide.getGas(2_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Pyrite.getDust(1))
            .itemOutputs(Materials.RoastedIron.getDust(1), Materials.Ash.getDust(1))
            .outputChances(10000, 1111)
            .fluidInputs(Materials.Oxygen.getGas(3_000))
            .fluidOutputs(Materials.SulfurDioxide.getGas(2_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Pentlandite.getDust(1))
            .itemOutputs(Materials.RoastedNickel.getDust(1), Materials.Ash.getDust(1))
            .outputChances(10000, 1111)
            .fluidInputs(Materials.Oxygen.getGas(3_000))
            .fluidOutputs(Materials.SulfurDioxide.getGas(2_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Sphalerite.getDust(1))
            .itemOutputs(Materials.RoastedZinc.getDust(1), Materials.Ash.getDust(1))
            .outputChances(10000, 1111)
            .fluidInputs(Materials.Oxygen.getGas(3_000))
            .fluidOutputs(Materials.SulfurDioxide.getGas(1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Cobaltite.getDust(1))
            .itemOutputs(Materials.RoastedCobalt.getDust(1), Materials.RoastedArsenic.getDust(1))
            .fluidInputs(Materials.Oxygen.getGas(3_000))
            .fluidOutputs(Materials.SulfurDioxide.getGas(1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Stibnite.getDust(1))
            .itemOutputs(Materials.RoastedAntimony.getDust(1), Materials.Ash.getDust(1))
            .outputChances(10000, 1111)
            .fluidInputs(Materials.Oxygen.getGas(3_000))
            .fluidOutputs(Materials.SulfurDioxide.getGas(1_500))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Galena.getDust(1))
            .itemOutputs(Materials.RoastedLead.getDust(1), Materials.Ash.getDust(1))
            .outputChances(10000, 1111)
            .fluidInputs(Materials.Oxygen.getGas(3_000))
            .fluidOutputs(Materials.SulfurDioxide.getGas(1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.TranscendentMetal.getDust(1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingotHot, Materials.TranscendentMetal, 1))
            .fluidInputs(Materials.Tungsten.getMolten(1 * INGOTS))
            .fluidOutputs(MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(1 * HALF_INGOTS))
            .duration(3 * MINUTES)
            .eut(TierEU.RECIPE_UIV)
            .metadata(COIL_HEAT, 11701)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Phononic_Seed_Crystal.get(1),
                Materials.SixPhasedCopper.getNanite(1),
                Materials.Dilithium.getDust(16))
            .circuit(2)
            .fluidInputs(Materials.Mellion.getMolten(48 * INGOTS))
            .fluidOutputs(Materials.PhononCrystalSolution.getFluid(1_000))
            .duration(300 * SECONDS)
            .eut((int) TierEU.RECIPE_UIV)
            .metadata(COIL_HEAT, 17000)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Phononic_Seed_Crystal.get(2),
                Materials.Eternity.getDust(8),
                GGMaterial.shirabon.get(OrePrefixes.dust, 8))
            .circuit(2)
            .fluidInputs(Materials.PhononMedium.getFluid(10L))
            .fluidOutputs(Materials.PhononCrystalSolution.getFluid(3_000))
            .duration(200 * SECONDS)
            .eut((int) TierEU.RECIPE_UXV)
            .metadata(COIL_HEAT, 50000)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Mellion.getDust(1))
            .circuit(11)
            .itemOutputs(ItemList.Harmonic_Compound.get(2))
            .fluidInputs(Materials.Creon.getPlasma(1 * INGOTS))
            .duration(30 * SECONDS)
            .eut((int) TierEU.RECIPE_UMV)
            .metadata(COIL_HEAT, 14000)
            .addTo(blastFurnaceRecipes);
    }

    public void registerPrimitiveBlastFurnaceRecipes() {
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Iron.getIngots(1))
            .itemOutputs(Materials.Steel.getIngots(1))
            .duration(6 * MINUTES)
            .metadata(ADDITIVE_AMOUNT, 4)
            .addTo(primitiveBlastRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Iron.getDust(1))
            .itemOutputs(Materials.Steel.getIngots(1))
            .duration(6 * MINUTES)
            .metadata(ADDITIVE_AMOUNT, 4)
            .addTo(primitiveBlastRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Iron.getBlocks(1))
            .itemOutputs(Materials.Steel.getIngots(9))
            .duration(54 * MINUTES)
            .metadata(ADDITIVE_AMOUNT, 36)
            .addTo(primitiveBlastRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Steel.getDust(1))
            .itemOutputs(Materials.Steel.getIngots(1))
            .duration(6 * MINUTES)
            .metadata(ADDITIVE_AMOUNT, 2)
            .addTo(primitiveBlastRecipes);
    }
}
