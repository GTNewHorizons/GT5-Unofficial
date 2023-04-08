package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.*;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sBlastRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sPrimitiveBlastRecipes;
import static gregtech.api.util.GT_RecipeBuilder.*;
import static gregtech.api.util.GT_RecipeConstants.ADDITIVE_AMOUNT;
import static gregtech.api.util.GT_RecipeConstants.COIL_HEAT;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.GT_Mod;
import gregtech.api.enums.*;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class BlastFurnaceRecipes implements Runnable {

    @Override
    public void run() {
        this.registerBlastFurnaceRecipes();
        this.registerPrimitiveBlastFurnaceRecipes();
    }

    public void registerBlastFurnaceRecipes() {
        GT_Values.RA.stdBuilder()
                    .itemInputs(Materials.Gypsum.getDust(8))
                    .itemOutputs(Materials.Quicklime.getDust(1))
                    .noFluidInputs()
                    .fluidOutputs(Materials.DilutedSulfuricAcid.getFluid(1500))
                    .duration(10 * SECONDS)
                    .eut((int) TierEU.RECIPE_HV)
                    .metadata(COIL_HEAT, 3200)
                    .addTo(sBlastRecipes);

        // Carbothermic Reduction
        // Depend on real amount except real ores
        int outputIngotAmount = GT_Mod.gregtechproxy.mMixedOreOnlyYieldsTwoThirdsOfPureOre ? 2 : 3;

        GT_Values.RA.stdBuilder()
                    .itemInputs(Materials.RoastedCopper.getDust(2), Materials.Carbon.getDust(1))
                    .itemOutputs(Materials.Copper.getIngots(outputIngotAmount), Materials.Ash.getDustTiny(2))
                    .noFluidInputs()
                    .fluidOutputs(Materials.CarbonDioxide.getGas(1000))
                    .duration(12 * SECONDS)
                    .eut((int) TierEU.RECIPE_MV)
                    .metadata(COIL_HEAT, 1200)
                    .addTo(sBlastRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(Materials.RoastedAntimony.getDust(2), Materials.Carbon.getDust(1))
                    .itemOutputs(Materials.Antimony.getIngots(outputIngotAmount), Materials.Ash.getDustTiny(2))
                    .noFluidInputs()
                    .fluidOutputs(Materials.CarbonDioxide.getGas(1000))
                    .duration(12 * SECONDS)
                    .eut((int) TierEU.RECIPE_MV)
                    .metadata(COIL_HEAT, 1200)
                    .addTo(sBlastRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(Materials.RoastedIron.getDust(2), Materials.Carbon.getDust(1))
                    .itemOutputs(Materials.Iron.getIngots(outputIngotAmount), Materials.Ash.getDustTiny(2))
                    .noFluidInputs()
                    .fluidOutputs(Materials.CarbonDioxide.getGas(1000))
                    .duration(12 * SECONDS)
                    .eut((int) TierEU.RECIPE_MV)
                    .metadata(COIL_HEAT, 1200)
                    .addTo(sBlastRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(Materials.RoastedNickel.getDust(2), Materials.Carbon.getDust(1))
                    .itemOutputs(Materials.Nickel.getIngots(outputIngotAmount), Materials.Ash.getDustTiny(2))
                    .noFluidInputs()
                    .fluidOutputs(Materials.CarbonDioxide.getGas(1000))
                    .duration(12 * SECONDS)
                    .eut((int) TierEU.RECIPE_MV)
                    .metadata(COIL_HEAT, 1200)
                    .addTo(sBlastRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(Materials.RoastedZinc.getDust(2), Materials.Carbon.getDust(1))
                    .itemOutputs(Materials.Zinc.getIngots(outputIngotAmount), Materials.Ash.getDustTiny(2))
                    .noFluidInputs()
                    .fluidOutputs(Materials.CarbonDioxide.getGas(1000))
                    .duration(12 * SECONDS)
                    .eut((int) TierEU.RECIPE_MV)
                    .metadata(COIL_HEAT, 1200)
                    .addTo(sBlastRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(Materials.RoastedCobalt.getDust(2), Materials.Carbon.getDust(1))
                    .itemOutputs(Materials.Cobalt.getIngots(outputIngotAmount), Materials.Ash.getDustTiny(2))
                    .noFluidInputs()
                    .fluidOutputs(Materials.CarbonDioxide.getGas(1000))
                    .duration(12 * SECONDS)
                    .eut((int) TierEU.RECIPE_MV)
                    .metadata(COIL_HEAT, 1200)
                    .addTo(sBlastRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(Materials.RoastedArsenic.getDust(2), Materials.Carbon.getDust(1))
                    .itemOutputs(Materials.Arsenic.getIngots(outputIngotAmount), Materials.Ash.getDustTiny(2))
                    .noFluidInputs()
                    .fluidOutputs(Materials.CarbonDioxide.getGas(1000))
                    .duration(12 * SECONDS)
                    .eut((int) TierEU.RECIPE_MV)
                    .metadata(COIL_HEAT, 1200)
                    .addTo(sBlastRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(Materials.RoastedLead.getDust(2), Materials.Carbon.getDust(1))
                    .itemOutputs(Materials.Lead.getIngots(outputIngotAmount), Materials.Ash.getDustTiny(2))
                    .noFluidInputs()
                    .fluidOutputs(Materials.CarbonDioxide.getGas(1000))
                    .duration(12 * SECONDS)
                    .eut((int) TierEU.RECIPE_MV)
                    .metadata(COIL_HEAT, 1200)
                    .addTo(sBlastRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(Materials.Malachite.getDust(2), Materials.Carbon.getDust(1))
                    .itemOutputs(Materials.Copper.getIngots(outputIngotAmount), Materials.Ash.getDustTiny(2))
                    .noFluidInputs()
                    .fluidOutputs(Materials.CarbonDioxide.getGas(3000))
                    .duration(12 * SECONDS)
                    .eut((int) TierEU.RECIPE_MV)
                    .metadata(COIL_HEAT, 1200)
                    .addTo(sBlastRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(Materials.Magnetite.getDust(2), Materials.Carbon.getDust(1))
                    .itemOutputs(Materials.Iron.getIngots(outputIngotAmount), Materials.Ash.getDustTiny(2))
                    .noFluidInputs()
                    .fluidOutputs(Materials.CarbonDioxide.getGas(1000))
                    .duration(12 * SECONDS)
                    .eut((int) TierEU.RECIPE_MV)
                    .metadata(COIL_HEAT, 1200)
                    .addTo(sBlastRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(Materials.YellowLimonite.getDust(2), Materials.Carbon.getDust(1))
                    .itemOutputs(Materials.Iron.getIngots(outputIngotAmount), Materials.Ash.getDustTiny(2))
                    .noFluidInputs()
                    .fluidOutputs(Materials.CarbonDioxide.getGas(1000))
                    .duration(12 * SECONDS)
                    .eut((int) TierEU.RECIPE_MV)
                    .metadata(COIL_HEAT, 1200)
                    .addTo(sBlastRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(Materials.BrownLimonite.getDust(2), Materials.Carbon.getDust(1))
                    .itemOutputs(Materials.Iron.getIngots(outputIngotAmount), Materials.Ash.getDustTiny(2))
                    .noFluidInputs()
                    .fluidOutputs(Materials.CarbonDioxide.getGas(1000))
                    .duration(12 * SECONDS)
                    .eut((int) TierEU.RECIPE_MV)
                    .metadata(COIL_HEAT, 1200)
                    .addTo(sBlastRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(Materials.BasalticMineralSand.getDust(2), Materials.Carbon.getDust(1))
                    .itemOutputs(Materials.Iron.getIngots(outputIngotAmount), Materials.Ash.getDustTiny(2))
                    .noFluidInputs()
                    .fluidOutputs(Materials.CarbonDioxide.getGas(1000))
                    .duration(12 * SECONDS)
                    .eut((int) TierEU.RECIPE_MV)
                    .metadata(COIL_HEAT, 1200)
                    .addTo(sBlastRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(Materials.GraniticMineralSand.getDust(2), Materials.Carbon.getDust(1))
                    .itemOutputs(Materials.Iron.getIngots(outputIngotAmount), Materials.Ash.getDustTiny(2))
                    .noFluidInputs()
                    .fluidOutputs(Materials.CarbonDioxide.getGas(1000))
                    .duration(12 * SECONDS)
                    .eut((int) TierEU.RECIPE_MV)
                    .metadata(COIL_HEAT, 1200)
                    .addTo(sBlastRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(Materials.Cassiterite.getDust(2), Materials.Carbon.getDust(1))
                    .itemOutputs(Materials.Tin.getIngots(outputIngotAmount), Materials.Ash.getDustTiny(2))
                    .noFluidInputs()
                    .fluidOutputs(Materials.CarbonDioxide.getGas(1000))
                    .duration(12 * SECONDS)
                    .eut((int) TierEU.RECIPE_MV)
                    .metadata(COIL_HEAT, 1200)
                    .addTo(sBlastRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(Materials.CassiteriteSand.getDust(2), Materials.Carbon.getDust(1))
                    .itemOutputs(Materials.Tin.getIngots(outputIngotAmount), Materials.Ash.getDustTiny(2))
                    .noFluidInputs()
                    .fluidOutputs(Materials.CarbonDioxide.getGas(1000))
                    .duration(12 * SECONDS)
                    .eut((int) TierEU.RECIPE_MV)
                    .metadata(COIL_HEAT, 1200)
                    .addTo(sBlastRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(Materials.SiliconDioxide.getDust(3), Materials.Carbon.getDust(2))
                    .itemOutputs(Materials.Silicon.getIngots(1), Materials.Ash.getDustTiny(1))
                    .noFluidInputs()
                    .fluidOutputs(Materials.CarbonMonoxide.getGas(2000))
                    .duration(4 * SECONDS)
                    .eut((int) TierEU.RECIPE_MV)
                    .metadata(COIL_HEAT, 1200)
                    .addTo(sBlastRecipes);

        if (GT_Mod.gregtechproxy.mMixedOreOnlyYieldsTwoThirdsOfPureOre) {
            GT_Values.RA.stdBuilder()
                        .itemInputs(Materials.CupricOxide.getDust(2), Materials.Carbon.getDustSmall(4))
                        .itemOutputs(Materials.Copper.getIngots(1), Materials.Ash.getDustTiny(2))
                        .noFluidInputs()
                        .fluidOutputs(Materials.CarbonDioxide.getGas(1000))
                        .duration(12 * SECONDS)
                        .eut((int) TierEU.RECIPE_MV)
                        .metadata(COIL_HEAT, 1200)
                        .addTo(sBlastRecipes);

            GT_Values.RA.stdBuilder()
                        .itemInputs(Materials.Malachite.getDust(2), Materials.Carbon.getDustSmall(4))
                        .itemOutputs(Materials.Copper.getIngots(outputIngotAmount), Materials.Ash.getDustTiny(2))
                        .noFluidInputs()
                        .fluidOutputs(Materials.CarbonDioxide.getGas(3000))
                        .duration(12 * SECONDS)
                        .eut((int) TierEU.RECIPE_MV)
                        .metadata(COIL_HEAT, 1200)
                        .addTo(sBlastRecipes);

            GT_Values.RA.stdBuilder()
                        .itemInputs(Materials.AntimonyTrioxide.getDust(5), Materials.Carbon.getDustSmall(4))
                        .itemOutputs(Materials.Antimony.getIngots(2), Materials.Ash.getDustTiny(2))
                        .noFluidInputs()
                        .fluidOutputs(Materials.CarbonDioxide.getGas(3000))
                        .duration(12 * SECONDS)
                        .eut((int) TierEU.RECIPE_MV)
                        .metadata(COIL_HEAT, 1200)
                        .addTo(sBlastRecipes);

            GT_Values.RA.stdBuilder()
                        .itemInputs(Materials.BandedIron.getDust(5), Materials.Carbon.getDustSmall(4))
                        .itemOutputs(Materials.Iron.getIngots(2), Materials.Ash.getDustTiny(2))
                        .noFluidInputs()
                        .fluidOutputs(Materials.CarbonDioxide.getGas(1000))
                        .duration(12 * SECONDS)
                        .eut((int) TierEU.RECIPE_MV)
                        .metadata(COIL_HEAT, 1200)
                        .addTo(sBlastRecipes);

            GT_Values.RA.stdBuilder()
                        .itemInputs(Materials.Magnetite.getDust(2), Materials.Carbon.getDustSmall(4))
                        .itemOutputs(Materials.Iron.getIngots(outputIngotAmount), Materials.Ash.getDustTiny(2))
                        .noFluidInputs()
                        .fluidOutputs(Materials.CarbonDioxide.getGas(1000))
                        .duration(12 * SECONDS)
                        .eut((int) TierEU.RECIPE_MV)
                        .metadata(COIL_HEAT, 1200)
                        .addTo(sBlastRecipes);

            GT_Values.RA.stdBuilder()
                        .itemInputs(Materials.YellowLimonite.getDust(2), Materials.Carbon.getDustSmall(4))
                        .itemOutputs(Materials.Iron.getIngots(outputIngotAmount), Materials.Ash.getDustTiny(2))
                        .noFluidInputs()
                        .fluidOutputs(Materials.CarbonDioxide.getGas(1000))
                        .duration(12 * SECONDS)
                        .eut((int) TierEU.RECIPE_MV)
                        .metadata(COIL_HEAT, 1200)
                        .addTo(sBlastRecipes);

            GT_Values.RA.stdBuilder()
                        .itemInputs(Materials.BrownLimonite.getDust(2), Materials.Carbon.getDustSmall(4))
                        .itemOutputs(Materials.Iron.getIngots(outputIngotAmount), Materials.Ash.getDustTiny(2))
                        .noFluidInputs()
                        .fluidOutputs(Materials.CarbonDioxide.getGas(1000))
                        .duration(12 * SECONDS)
                        .eut((int) TierEU.RECIPE_MV)
                        .metadata(COIL_HEAT, 1200)
                        .addTo(sBlastRecipes);

            GT_Values.RA.stdBuilder()
                        .itemInputs(Materials.BasalticMineralSand.getDust(2), Materials.Carbon.getDustSmall(4))
                        .itemOutputs(Materials.Iron.getIngots(outputIngotAmount), Materials.Ash.getDustTiny(2))
                        .noFluidInputs()
                        .fluidOutputs(Materials.CarbonDioxide.getGas(1000))
                        .duration(12 * SECONDS)
                        .eut((int) TierEU.RECIPE_MV)
                        .metadata(COIL_HEAT, 1200)
                        .addTo(sBlastRecipes);

            GT_Values.RA.stdBuilder()
                        .itemInputs(Materials.GraniticMineralSand.getDust(2), Materials.Carbon.getDustSmall(4))
                        .itemOutputs(Materials.Iron.getIngots(outputIngotAmount), Materials.Ash.getDustTiny(2))
                        .noFluidInputs()
                        .fluidOutputs(Materials.CarbonDioxide.getGas(1000))
                        .duration(12 * SECONDS)
                        .eut((int) TierEU.RECIPE_MV)
                        .metadata(COIL_HEAT, 1200)
                        .addTo(sBlastRecipes);

            GT_Values.RA.stdBuilder()
                        .itemInputs(Materials.Cassiterite.getDust(2), Materials.Carbon.getDustSmall(4))
                        .itemOutputs(Materials.Tin.getIngots(outputIngotAmount), Materials.Ash.getDustTiny(2))
                        .noFluidInputs()
                        .fluidOutputs(Materials.CarbonDioxide.getGas(1000))
                        .duration(12 * SECONDS)
                        .eut((int) TierEU.RECIPE_MV)
                        .metadata(COIL_HEAT, 1200)
                        .addTo(sBlastRecipes);

            GT_Values.RA.stdBuilder()
                        .itemInputs(Materials.CassiteriteSand.getDust(2), Materials.Carbon.getDustSmall(4))
                        .itemOutputs(Materials.Tin.getIngots(outputIngotAmount), Materials.Ash.getDustTiny(2))
                        .noFluidInputs()
                        .fluidOutputs(Materials.CarbonDioxide.getGas(1000))
                        .duration(12 * SECONDS)
                        .eut((int) TierEU.RECIPE_MV)
                        .metadata(COIL_HEAT, 1200)
                        .addTo(sBlastRecipes);

            GT_Values.RA.stdBuilder()
                        .itemInputs(Materials.Garnierite.getDust(2), Materials.Carbon.getDustSmall(4))
                        .itemOutputs(Materials.Nickel.getIngots(1), Materials.Ash.getDustTiny(2))
                        .noFluidInputs()
                        .fluidOutputs(Materials.CarbonDioxide.getGas(1000))
                        .duration(12 * SECONDS)
                        .eut((int) TierEU.RECIPE_MV)
                        .metadata(COIL_HEAT, 1200)
                        .addTo(sBlastRecipes);

            GT_Values.RA.stdBuilder()
                        .itemInputs(Materials.CobaltOxide.getDust(2), Materials.Carbon.getDustSmall(4))
                        .itemOutputs(Materials.Cobalt.getIngots(1), Materials.Ash.getDustTiny(2))
                        .noFluidInputs()
                        .fluidOutputs(Materials.CarbonDioxide.getGas(1000))
                        .duration(12 * SECONDS)
                        .eut((int) TierEU.RECIPE_MV)
                        .metadata(COIL_HEAT, 1200)
                        .addTo(sBlastRecipes);

            GT_Values.RA.stdBuilder()
                        .itemInputs(Materials.ArsenicTrioxide.getDust(5), Materials.Carbon.getDustSmall(4))
                        .itemOutputs(Materials.Arsenic.getIngots(2), Materials.Ash.getDustTiny(2))
                        .noFluidInputs()
                        .fluidOutputs(Materials.CarbonDioxide.getGas(1000))
                        .duration(12 * SECONDS)
                        .eut((int) TierEU.RECIPE_MV)
                        .metadata(COIL_HEAT, 1200)
                        .addTo(sBlastRecipes);

            GT_Values.RA.stdBuilder()
                        .itemInputs(Materials.Massicot.getDust(2), Materials.Carbon.getDustSmall(4))
                        .itemOutputs(Materials.Lead.getIngots(1), Materials.Ash.getDustTiny(2))
                        .noFluidInputs()
                        .fluidOutputs(Materials.CarbonDioxide.getGas(1000))
                        .duration(12 * SECONDS)
                        .eut((int) TierEU.RECIPE_MV)
                        .metadata(COIL_HEAT, 1200)
                        .addTo(sBlastRecipes);
        }

        GT_Values.RA.stdBuilder()
                    .itemInputs(
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 32),
                            ItemList.GalliumArsenideCrystalSmallPart.get(1),
                            GT_Utility.getIntegratedCircuit(2))
                    .itemOutputs(ItemList.Circuit_Silicon_Ingot.get(1))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(7 * MINUTES + 30 * SECONDS)
                    .eut((int) TierEU.RECIPE_MV)
                    .metadata(COIL_HEAT, 1784)
                    .addTo(sBlastRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 64),
                            ItemList.GalliumArsenideCrystalSmallPart.get(2),
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Phosphorus, 8),
                            GT_Utility.getIntegratedCircuit(3))
                    .itemOutputs(ItemList.Circuit_Silicon_Ingot2.get(1))
                    .fluidInputs(Materials.Nitrogen.getGas(8000))
                    .noFluidOutputs()
                    .duration(10 * MINUTES)
                    .eut((int) TierEU.RECIPE_HV)
                    .metadata(COIL_HEAT, 2484)
                    .addTo(sBlastRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(
                            GT_OreDictUnificator.get(OrePrefixes.block, Materials.SiliconSG, 16),
                            ItemList.GalliumArsenideCrystal.get(1),
                            GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Naquadah, 1),
                            GT_Utility.getIntegratedCircuit(3))
                    .itemOutputs(ItemList.Circuit_Silicon_Ingot3.get(1))
                    .fluidInputs(Materials.Argon.getGas(8000))
                    .noFluidOutputs()
                    .duration(12 * MINUTES + 30 * SECONDS)
                    .eut((int) TierEU.RECIPE_EV)
                    .metadata(COIL_HEAT, 4484)
                    .addTo(sBlastRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(
                            GT_OreDictUnificator.get(OrePrefixes.block, Materials.SiliconSG, 32),
                            ItemList.GalliumArsenideCrystal.get(2),
                            GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Europium, 2),
                            GT_Utility.getIntegratedCircuit(3))
                    .itemOutputs(ItemList.Circuit_Silicon_Ingot4.get(1))
                    .fluidInputs(Materials.Radon.getGas(8000))
                    .noFluidOutputs()
                    .duration(15 * MINUTES)
                    .eut((int) TierEU.RECIPE_IV)
                    .metadata(COIL_HEAT, 6484)
                    .addTo(sBlastRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(
                            GT_OreDictUnificator.get(OrePrefixes.block, Materials.SiliconSG, 64),
                            ItemList.GalliumArsenideCrystal.get(4),
                            GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Americium, 4),
                            GT_Utility.getIntegratedCircuit(3))
                    .itemOutputs(ItemList.Circuit_Silicon_Ingot5.get(1))
                    .fluidInputs(Materials.Radon.getGas(16000))
                    .noFluidOutputs()
                    .duration(17 * MINUTES + 30 * SECONDS)
                    .eut((int) TierEU.RECIPE_LuV)
                    .metadata(COIL_HEAT, 9000)
                    .addTo(sBlastRecipes);

        // CaH2 + 2Si = CaSi2 + 2H

        GT_Values.RA.stdBuilder()
                    .itemInputs(
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calciumhydride, 3),
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 2))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.CalciumDisilicide, 3))
                    .noFluidInputs()
                    .fluidOutputs(Materials.Hydrogen.getGas(2000))
                    .duration(15 * SECONDS)
                    .eut((int) TierEU.RECIPE_MV)
                    .metadata(COIL_HEAT, 1273)
                    .addTo(sBlastRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SuperconductorUEVBase, 1),
                            GT_Utility.getIntegratedCircuit(1))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.SuperconductorUEVBase, 1))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(16 * MINUTES + 23 * SECONDS)
                    .eut((int) TierEU.RECIPE_UV)
                    .metadata(COIL_HEAT, 11800)
                    .addTo(sBlastRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SuperconductorUEVBase, 1),
                            GT_Utility.getIntegratedCircuit(11))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.SuperconductorUEVBase, 1))
                    .fluidInputs(Materials.Radon.getGas(1000))
                    .noFluidOutputs()
                    .duration(7 * MINUTES + 22 * SECONDS + 7 * TICKS)
                    .eut((int) TierEU.RECIPE_UV)
                    .metadata(COIL_HEAT, 11800)
                    .addTo(sBlastRecipes);

        // 0.45 * 19660 = 8847

        GT_Values.RA.stdBuilder()
                    .itemInputs(
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SuperconductorUIVBase, 1),
                            GT_Utility.getIntegratedCircuit(1))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.SuperconductorUIVBase, 1))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(16 * MINUTES + 23 * SECONDS)
                    .eut((int) TierEU.RECIPE_UHV)
                    .metadata(COIL_HEAT, 12700)
                    .addTo(sBlastRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SuperconductorUIVBase, 1),
                            GT_Utility.getIntegratedCircuit(11))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.SuperconductorUIVBase, 1))
                    .fluidInputs(Materials.Radon.getGas(1000))
                    .noFluidOutputs()
                    .duration(7 * MINUTES + 22 * SECONDS + 7 * TICKS)
                    .eut((int) TierEU.RECIPE_UHV)
                    .metadata(COIL_HEAT, 12700)
                    .addTo(sBlastRecipes);

        // 0.45 * 19660 = 8847

        GT_Values.RA.stdBuilder()
                    .itemInputs(
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SuperconductorUMVBase, 1),
                            GT_Utility.getIntegratedCircuit(1))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.SuperconductorUMVBase, 1))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(16 * MINUTES + 23 * SECONDS)
                    .eut((int) TierEU.RECIPE_UEV)
                    .metadata(COIL_HEAT, 13600)
                    .addTo(sBlastRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SuperconductorUMVBase, 1),
                            GT_Utility.getIntegratedCircuit(11))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.SuperconductorUMVBase, 1))
                    .fluidInputs(Materials.Radon.getGas(1000))
                    .noFluidOutputs()
                    .duration(7 * MINUTES + 22 * SECONDS + 7 * TICKS)
                    .eut((int) TierEU.RECIPE_UEV)
                    .metadata(COIL_HEAT, 13600)
                    .addTo(sBlastRecipes);

        // 0.45 * 19660 = 8847
        // CaO + 3C = CaC2 + CO

        GT_Values.RA.stdBuilder()
                    .itemInputs(Materials.Quicklime.getDust(2), Materials.Carbon.getDust(3))
                    .itemOutputs(MaterialsKevlar.CalciumCarbide.getDust(3))
                    .noFluidInputs()
                    .fluidOutputs(Materials.CarbonMonoxide.getGas(1000))
                    .duration(30 * SECONDS)
                    .eut((int) TierEU.RECIPE_HV)
                    .metadata(COIL_HEAT, 2573)
                    .addTo(sBlastRecipes);

        // Ni + 3Al = NiAl3

        GT_Values.RA.stdBuilder()
                    .itemInputs(Materials.Nickel.getDust(1), Materials.Aluminium.getDust(3))
                    .itemOutputs(MaterialsKevlar.NickelAluminide.getIngots(4))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(45 * SECONDS)
                    .eut((int) TierEU.RECIPE_HV)
                    .metadata(COIL_HEAT, 1688)
                    .addTo(sBlastRecipes);

        ItemStack[] tSiliconDioxide = new ItemStack[] { Materials.SiliconDioxide.getDust(3),
                Materials.NetherQuartz.getDust(3), Materials.CertusQuartz.getDust(3), Materials.Quartzite.getDust(6) };

        // Roasting

        for (ItemStack silicon : tSiliconDioxide) {
            GT_Values.RA.addBlastRecipe(
                    Materials.Chalcopyrite.getDust(1),
                    silicon,
                    Materials.Oxygen.getGas(3000),
                    Materials.SulfurDioxide.getGas(2000),
                    Materials.RoastedCopper.getDust(1),
                    Materials.Ferrosilite.getDust(5),
                    120,
                    (int) TierEU.RECIPE_MV,
                    1200);
        }

        GT_Values.RA.addBlastRecipe(
                Materials.Tetrahedrite.getDust(1),
                GT_Values.NI,
                Materials.Oxygen.getGas(3000),
                Materials.SulfurDioxide.getGas(2000),
                Materials.RoastedCopper.getDust(1),
                Materials.RoastedAntimony.getDustTiny(3),
                120,
                (int) TierEU.RECIPE_MV,
                1200);

        GT_Values.RA.addBlastRecipe(
                Materials.Pyrite.getDust(1),
                GT_Values.NI,
                Materials.Oxygen.getGas(3000),
                Materials.SulfurDioxide.getGas(2000),
                Materials.RoastedIron.getDust(1),
                Materials.Ash.getDustTiny(1),
                120,
                (int) TierEU.RECIPE_MV,
                1200);

        GT_Values.RA.addBlastRecipe(
                Materials.Pentlandite.getDust(1),
                GT_Values.NI,
                Materials.Oxygen.getGas(3000),
                Materials.SulfurDioxide.getGas(1000),
                Materials.RoastedNickel.getDust(1),
                Materials.Ash.getDustTiny(1),
                120,
                (int) TierEU.RECIPE_MV,
                1200);

        GT_Values.RA.addBlastRecipe(
                Materials.Sphalerite.getDust(1),
                GT_Values.NI,
                Materials.Oxygen.getGas(3000),
                Materials.SulfurDioxide.getGas(1000),
                Materials.RoastedZinc.getDust(1),
                Materials.Ash.getDustTiny(1),
                120,
                (int) TierEU.RECIPE_MV,
                1200);

        GT_Values.RA.addBlastRecipe(
                Materials.Cobaltite.getDust(1),
                GT_Values.NI,
                Materials.Oxygen.getGas(3000),
                Materials.SulfurDioxide.getGas(1000),
                Materials.RoastedCobalt.getDust(1),
                Materials.RoastedArsenic.getDust(1),
                120,
                (int) TierEU.RECIPE_MV,
                1200);

        GT_Values.RA.addBlastRecipe(
                Materials.Stibnite.getDust(1),
                GT_Values.NI,
                Materials.Oxygen.getGas(3000),
                Materials.SulfurDioxide.getGas(1500),
                Materials.RoastedAntimony.getDust(1),
                Materials.Ash.getDustTiny(1),
                120,
                (int) TierEU.RECIPE_MV,
                1200);

        GT_Values.RA.addBlastRecipe(
                Materials.Galena.getDust(1),
                GT_Values.NI,
                Materials.Oxygen.getGas(3000),
                Materials.SulfurDioxide.getGas(1000),
                Materials.RoastedLead.getDust(1),
                Materials.Ash.getDustTiny(1),
                120,
                (int) TierEU.RECIPE_MV,
                1200);

        if (GTNHLanthanides.isModLoaded() && GTPlusPlus.isModLoaded()) {

            GT_Values.RA.stdBuilder()
                        .itemInputs(Materials.TranscendentMetal.getDust(1), GT_Utility.getIntegratedCircuit(1))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.TranscendentMetal, 1))
                        .fluidInputs(Materials.Tungsten.getMolten(144))
                        .fluidOutputs(new FluidStack(FluidRegistry.getFluid("molten.celestialtungsten"), 72))
                        .duration(3 * MINUTES)
                        .eut(32000000)
                        .metadata(COIL_HEAT, 11701)
                        .addTo(sBlastRecipes);

            // Rh + 3Cl = RhCl3

            GT_Values.RA.stdBuilder()
                        .itemInputs(
                                getModItem(BartWorks.ID, "gt.bwMetaGenerateddust", 1L, 78),
                                GT_Utility.getIntegratedCircuit(2))
                        .itemOutputs(MaterialsKevlar.RhodiumChloride.getDust(4))
                        .fluidInputs(Materials.Chlorine.getGas(3000))
                        .noFluidOutputs()
                        .duration(30 * SECONDS)
                        .eut((int) TierEU.RECIPE_HV)
                        .metadata(COIL_HEAT, 573)
                        .addTo(sBlastRecipes);
        }
    }

    public void registerPrimitiveBlastFurnaceRecipes() {
        GT_Values.RA.stdBuilder()
                    .itemInputs(Materials.Iron.getIngots(1))
                    .itemOutputs(Materials.Steel.getIngots(1))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(6 * MINUTES)
                    .eut(0)
                    .metadata(ADDITIVE_AMOUNT, 4)
                    .addTo(sPrimitiveBlastRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(Materials.Iron.getDust(1))
                    .itemOutputs(Materials.Steel.getIngots(1))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(6 * MINUTES)
                    .eut(0)
                    .metadata(ADDITIVE_AMOUNT, 4)
                    .addTo(sPrimitiveBlastRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(Materials.Iron.getBlocks(1))
                    .itemOutputs(Materials.Steel.getIngots(9))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(54 * MINUTES)
                    .eut(0)
                    .metadata(ADDITIVE_AMOUNT, 36)
                    .addTo(sPrimitiveBlastRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(Materials.Steel.getDust(1))
                    .itemOutputs(Materials.Steel.getIngots(1))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(6 * MINUTES)
                    .eut(0)
                    .metadata(ADDITIVE_AMOUNT, 2)
                    .addTo(sPrimitiveBlastRecipes);
    }
}
