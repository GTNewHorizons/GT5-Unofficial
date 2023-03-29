package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.ModIDs.*;
import static gregtech.api.util.GT_ModHandler.getModItem;

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

        this.primitiveBlastFurnaceRecipes();

        GT_Values.RA.addBlastRecipe(
                Materials.Gypsum.getDust(8),
                GT_Values.NI,
                GT_Values.NF,
                Materials.DilutedSulfuricAcid.getFluid(1500),
                Materials.Quicklime.getDust(1),
                GT_Values.NI,
                200,
                (int) TierEU.RECIPE_HV,
                3200);

        // Carbothermic Reduction
        // Depend on real amount except real ores
        int outputIngotAmount = GT_Mod.gregtechproxy.mMixedOreOnlyYieldsTwoThirdsOfPureOre ? 2 : 3;

        GT_Values.RA.addBlastRecipe(
                Materials.RoastedCopper.getDust(2),
                Materials.Carbon.getDust(1),
                GT_Values.NF,
                Materials.CarbonDioxide.getGas(1000),
                Materials.Copper.getIngots(outputIngotAmount),
                Materials.Ash.getDustTiny(2),
                240,
                (int) TierEU.RECIPE_MV,
                1200);
        GT_Values.RA.addBlastRecipe(
                Materials.RoastedAntimony.getDust(2),
                Materials.Carbon.getDust(1),
                GT_Values.NF,
                Materials.CarbonDioxide.getGas(1000),
                Materials.Antimony.getIngots(outputIngotAmount),
                Materials.Ash.getDustTiny(2),
                240,
                (int) TierEU.RECIPE_MV,
                1200);
        GT_Values.RA.addBlastRecipe(
                Materials.RoastedIron.getDust(2),
                Materials.Carbon.getDust(1),
                GT_Values.NF,
                Materials.CarbonDioxide.getGas(1000),
                Materials.Iron.getIngots(outputIngotAmount),
                Materials.Ash.getDustTiny(2),
                240,
                (int) TierEU.RECIPE_MV,
                1200);
        GT_Values.RA.addBlastRecipe(
                Materials.RoastedNickel.getDust(2),
                Materials.Carbon.getDust(1),
                GT_Values.NF,
                Materials.CarbonDioxide.getGas(1000),
                Materials.Nickel.getIngots(outputIngotAmount),
                Materials.Ash.getDustTiny(2),
                240,
                (int) TierEU.RECIPE_MV,
                1200);
        GT_Values.RA.addBlastRecipe(
                Materials.RoastedZinc.getDust(2),
                Materials.Carbon.getDust(1),
                GT_Values.NF,
                Materials.CarbonDioxide.getGas(1000),
                Materials.Zinc.getIngots(outputIngotAmount),
                Materials.Ash.getDustTiny(2),
                240,
                (int) TierEU.RECIPE_MV,
                1200);
        GT_Values.RA.addBlastRecipe(
                Materials.RoastedCobalt.getDust(2),
                Materials.Carbon.getDust(1),
                GT_Values.NF,
                Materials.CarbonDioxide.getGas(1000),
                Materials.Cobalt.getIngots(outputIngotAmount),
                Materials.Ash.getDustTiny(2),
                240,
                (int) TierEU.RECIPE_MV,
                1200);
        GT_Values.RA.addBlastRecipe(
                Materials.RoastedArsenic.getDust(2),
                Materials.Carbon.getDust(1),
                GT_Values.NF,
                Materials.CarbonDioxide.getGas(1000),
                Materials.Arsenic.getIngots(outputIngotAmount),
                Materials.Ash.getDustTiny(2),
                240,
                (int) TierEU.RECIPE_MV,
                1200);
        GT_Values.RA.addBlastRecipe(
                Materials.RoastedLead.getDust(2),
                Materials.Carbon.getDust(1),
                GT_Values.NF,
                Materials.CarbonDioxide.getGas(1000),
                Materials.Lead.getIngots(outputIngotAmount),
                Materials.Ash.getDustTiny(2),
                240,
                (int) TierEU.RECIPE_MV,
                1200);

        GT_Values.RA.addBlastRecipe(
                Materials.Malachite.getDust(2),
                Materials.Carbon.getDust(1),
                GT_Values.NF,
                Materials.CarbonDioxide.getGas(3000),
                Materials.Copper.getIngots(outputIngotAmount),
                Materials.Ash.getDustTiny(2),
                240,
                (int) TierEU.RECIPE_MV,
                1200);
        GT_Values.RA.addBlastRecipe(
                Materials.Magnetite.getDust(2),
                Materials.Carbon.getDust(1),
                GT_Values.NF,
                Materials.CarbonDioxide.getGas(1000),
                Materials.Iron.getIngots(outputIngotAmount),
                Materials.Ash.getDustTiny(2),
                240,
                (int) TierEU.RECIPE_MV,
                1200);
        GT_Values.RA.addBlastRecipe(
                Materials.YellowLimonite.getDust(2),
                Materials.Carbon.getDust(1),
                GT_Values.NF,
                Materials.CarbonDioxide.getGas(1000),
                Materials.Iron.getIngots(outputIngotAmount),
                Materials.Ash.getDustTiny(2),
                240,
                (int) TierEU.RECIPE_MV,
                1200);
        GT_Values.RA.addBlastRecipe(
                Materials.BrownLimonite.getDust(2),
                Materials.Carbon.getDust(1),
                GT_Values.NF,
                Materials.CarbonDioxide.getGas(1000),
                Materials.Iron.getIngots(outputIngotAmount),
                Materials.Ash.getDustTiny(2),
                240,
                (int) TierEU.RECIPE_MV,
                1200);
        GT_Values.RA.addBlastRecipe(
                Materials.BasalticMineralSand.getDust(2),
                Materials.Carbon.getDust(1),
                GT_Values.NF,
                Materials.CarbonDioxide.getGas(1000),
                Materials.Iron.getIngots(outputIngotAmount),
                Materials.Ash.getDustTiny(2),
                240,
                (int) TierEU.RECIPE_MV,
                1200);
        GT_Values.RA.addBlastRecipe(
                Materials.GraniticMineralSand.getDust(2),
                Materials.Carbon.getDust(1),
                GT_Values.NF,
                Materials.CarbonDioxide.getGas(1000),
                Materials.Iron.getIngots(outputIngotAmount),
                Materials.Ash.getDustTiny(2),
                240,
                (int) TierEU.RECIPE_MV,
                1200);

        GT_Values.RA.addBlastRecipe(
                Materials.Cassiterite.getDust(2),
                Materials.Carbon.getDust(1),
                GT_Values.NF,
                Materials.CarbonDioxide.getGas(1000),
                Materials.Tin.getIngots(outputIngotAmount),
                Materials.Ash.getDustTiny(2),
                240,
                (int) TierEU.RECIPE_MV,
                1200);
        GT_Values.RA.addBlastRecipe(
                Materials.CassiteriteSand.getDust(2),
                Materials.Carbon.getDust(1),
                GT_Values.NF,
                Materials.CarbonDioxide.getGas(1000),
                Materials.Tin.getIngots(outputIngotAmount),
                Materials.Ash.getDustTiny(2),
                240,
                (int) TierEU.RECIPE_MV,
                1200);

        GT_Values.RA.addBlastRecipe(
                Materials.SiliconDioxide.getDust(3),
                Materials.Carbon.getDust(2),
                GT_Values.NF,
                Materials.CarbonMonoxide.getGas(2000),
                Materials.Silicon.getIngots(1),
                Materials.Ash.getDustTiny(1),
                80,
                (int) TierEU.RECIPE_MV,
                1200);

        if (GT_Mod.gregtechproxy.mMixedOreOnlyYieldsTwoThirdsOfPureOre) {
            GT_Values.RA.addBlastRecipe(
                    Materials.CupricOxide.getDust(2),
                    Materials.Carbon.getDustSmall(4),
                    GT_Values.NF,
                    Materials.CarbonDioxide.getGas(1000),
                    Materials.Copper.getIngots(1),
                    Materials.Ash.getDustTiny(2),
                    240,
                    (int) TierEU.RECIPE_MV,
                    1200);
            GT_Values.RA.addBlastRecipe(
                    Materials.Malachite.getDust(2),
                    Materials.Carbon.getDustSmall(4),
                    GT_Values.NF,
                    Materials.CarbonDioxide.getGas(3000),
                    Materials.Copper.getIngots(outputIngotAmount),
                    Materials.Ash.getDustTiny(2),
                    240,
                    (int) TierEU.RECIPE_MV,
                    1200);
            GT_Values.RA.addBlastRecipe(
                    Materials.AntimonyTrioxide.getDust(5),
                    Materials.Carbon.getDustSmall(4),
                    GT_Values.NF,
                    Materials.CarbonDioxide.getGas(3000),
                    Materials.Antimony.getIngots(2),
                    Materials.Ash.getDustTiny(2),
                    240,
                    (int) TierEU.RECIPE_MV,
                    1200);
            GT_Values.RA.addBlastRecipe(
                    Materials.BandedIron.getDust(5),
                    Materials.Carbon.getDustSmall(4),
                    GT_Values.NF,
                    Materials.CarbonDioxide.getGas(1000),
                    Materials.Iron.getIngots(2),
                    Materials.Ash.getDustTiny(2),
                    240,
                    (int) TierEU.RECIPE_MV,
                    1200);
            GT_Values.RA.addBlastRecipe(
                    Materials.Magnetite.getDust(2),
                    Materials.Carbon.getDustSmall(4),
                    GT_Values.NF,
                    Materials.CarbonDioxide.getGas(1000),
                    Materials.Iron.getIngots(outputIngotAmount),
                    Materials.Ash.getDustTiny(2),
                    240,
                    (int) TierEU.RECIPE_MV,
                    1200);
            GT_Values.RA.addBlastRecipe(
                    Materials.YellowLimonite.getDust(2),
                    Materials.Carbon.getDustSmall(4),
                    GT_Values.NF,
                    Materials.CarbonDioxide.getGas(1000),
                    Materials.Iron.getIngots(outputIngotAmount),
                    Materials.Ash.getDustTiny(2),
                    240,
                    (int) TierEU.RECIPE_MV,
                    1200);
            GT_Values.RA.addBlastRecipe(
                    Materials.BrownLimonite.getDust(2),
                    Materials.Carbon.getDustSmall(4),
                    GT_Values.NF,
                    Materials.CarbonDioxide.getGas(1000),
                    Materials.Iron.getIngots(outputIngotAmount),
                    Materials.Ash.getDustTiny(2),
                    240,
                    (int) TierEU.RECIPE_MV,
                    1200);
            GT_Values.RA.addBlastRecipe(
                    Materials.BasalticMineralSand.getDust(2),
                    Materials.Carbon.getDustSmall(4),
                    GT_Values.NF,
                    Materials.CarbonDioxide.getGas(1000),
                    Materials.Iron.getIngots(outputIngotAmount),
                    Materials.Ash.getDustTiny(2),
                    240,
                    (int) TierEU.RECIPE_MV,
                    1200);
            GT_Values.RA.addBlastRecipe(
                    Materials.GraniticMineralSand.getDust(2),
                    Materials.Carbon.getDustSmall(4),
                    GT_Values.NF,
                    Materials.CarbonDioxide.getGas(1000),
                    Materials.Iron.getIngots(outputIngotAmount),
                    Materials.Ash.getDustTiny(2),
                    240,
                    (int) TierEU.RECIPE_MV,
                    1200);
            GT_Values.RA.addBlastRecipe(
                    Materials.Cassiterite.getDust(2),
                    Materials.Carbon.getDustSmall(4),
                    GT_Values.NF,
                    Materials.CarbonDioxide.getGas(1000),
                    Materials.Tin.getIngots(outputIngotAmount),
                    Materials.Ash.getDustTiny(2),
                    240,
                    (int) TierEU.RECIPE_MV,
                    1200);
            GT_Values.RA.addBlastRecipe(
                    Materials.CassiteriteSand.getDust(2),
                    Materials.Carbon.getDustSmall(4),
                    GT_Values.NF,
                    Materials.CarbonDioxide.getGas(1000),
                    Materials.Tin.getIngots(outputIngotAmount),
                    Materials.Ash.getDustTiny(2),
                    240,
                    (int) TierEU.RECIPE_MV,
                    1200);
            GT_Values.RA.addBlastRecipe(
                    Materials.Garnierite.getDust(2),
                    Materials.Carbon.getDustSmall(4),
                    GT_Values.NF,
                    Materials.CarbonDioxide.getGas(1000),
                    Materials.Nickel.getIngots(1),
                    Materials.Ash.getDustTiny(2),
                    240,
                    (int) TierEU.RECIPE_MV,
                    1200);
            GT_Values.RA.addBlastRecipe(
                    Materials.CobaltOxide.getDust(2),
                    Materials.Carbon.getDustSmall(4),
                    GT_Values.NF,
                    Materials.CarbonDioxide.getGas(1000),
                    Materials.Cobalt.getIngots(1),
                    Materials.Ash.getDustTiny(2),
                    240,
                    (int) TierEU.RECIPE_MV,
                    1200);
            GT_Values.RA.addBlastRecipe(
                    Materials.ArsenicTrioxide.getDust(5),
                    Materials.Carbon.getDustSmall(4),
                    GT_Values.NF,
                    Materials.CarbonDioxide.getGas(1000),
                    Materials.Arsenic.getIngots(2),
                    Materials.Ash.getDustTiny(2),
                    240,
                    (int) TierEU.RECIPE_MV,
                    1200);
            GT_Values.RA.addBlastRecipe(
                    Materials.Massicot.getDust(2),
                    Materials.Carbon.getDustSmall(4),
                    GT_Values.NF,
                    Materials.CarbonDioxide.getGas(1000),
                    Materials.Lead.getIngots(1),
                    Materials.Ash.getDustTiny(2),
                    240,
                    (int) TierEU.RECIPE_MV,
                    1200);
        }

        GT_Values.RA.addBlastRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 32),
                ItemList.GalliumArsenideCrystalSmallPart.get(1L),
                GT_Utility.getIntegratedCircuit(2),
                GT_Values.NI,
                GT_Values.NF,
                GT_Values.NF,
                ItemList.Circuit_Silicon_Ingot.get(1),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                9000,
                (int) TierEU.RECIPE_MV,
                1784);

        GT_Values.RA.addBlastRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 64),
                ItemList.GalliumArsenideCrystalSmallPart.get(2L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Phosphorus, 8),
                GT_Utility.getIntegratedCircuit(3),
                Materials.Nitrogen.getGas(8000),
                GT_Values.NF,
                ItemList.Circuit_Silicon_Ingot2.get(1),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                12000,
                (int) TierEU.RECIPE_HV,
                2484);

        GT_Values.RA.addBlastRecipe(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.SiliconSG, 16),
                ItemList.GalliumArsenideCrystal.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Naquadah, 1),
                GT_Utility.getIntegratedCircuit(3),
                Materials.Argon.getGas(8000),
                GT_Values.NF,
                ItemList.Circuit_Silicon_Ingot3.get(1),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                15000,
                (int) TierEU.RECIPE_EV,
                4484);

        GT_Values.RA.addBlastRecipe(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.SiliconSG, 32),
                ItemList.GalliumArsenideCrystal.get(2L),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Europium, 2),
                GT_Utility.getIntegratedCircuit(3),
                Materials.Radon.getGas(8000),
                null,
                ItemList.Circuit_Silicon_Ingot4.get(1),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                18000,
                (int) TierEU.RECIPE_IV,
                6484);

        GT_Values.RA.addBlastRecipe(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.SiliconSG, 64),
                ItemList.GalliumArsenideCrystal.get(4L),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Americium, 4),
                GT_Utility.getIntegratedCircuit(3),
                Materials.Radon.getGas(16000),
                GT_Values.NF,
                ItemList.Circuit_Silicon_Ingot5.get(1),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                21000,
                (int) TierEU.RECIPE_LuV,
                9000);

        // CaH2 + 2Si = CaSi2 + 2H
        GT_Values.RA.addBlastRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calciumhydride, 3),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 2),
                GT_Values.NF,
                Materials.Hydrogen.getGas(2000),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.CalciumDisilicide, 3),
                GT_Values.NI,
                300,
                (int) TierEU.RECIPE_MV,
                1273);

        GT_Values.RA.addBlastRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SuperconductorUEVBase, 1L),
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.SuperconductorUEVBase, 1L),
                GT_Values.NI,
                19660,
                (int) TierEU.RECIPE_UV,
                11800);

        GT_Values.RA.addBlastRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SuperconductorUEVBase, 1L),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Radon.getGas(1000L),
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.SuperconductorUEVBase, 1L),
                GT_Values.NI,
                8847,
                (int) TierEU.RECIPE_UV,
                11800); // 0.45 * 19660 = 8847

        GT_Values.RA.addBlastRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SuperconductorUIVBase, 1L),
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.SuperconductorUIVBase, 1L),
                GT_Values.NI,
                19660,
                (int) TierEU.RECIPE_UHV,
                12700);

        GT_Values.RA.addBlastRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SuperconductorUIVBase, 1L),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Radon.getGas(1000L),
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.SuperconductorUIVBase, 1L),
                GT_Values.NI,
                8847,
                (int) TierEU.RECIPE_UHV,
                12700); // 0.45 * 19660 = 8847

        GT_Values.RA.addBlastRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SuperconductorUMVBase, 1L),
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.SuperconductorUMVBase, 1L),
                GT_Values.NI,
                19660,
                (int) TierEU.RECIPE_UEV,
                13600);

        GT_Values.RA.addBlastRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SuperconductorUMVBase, 1L),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Radon.getGas(1000L),
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.SuperconductorUMVBase, 1L),
                GT_Values.NI,
                8847,
                (int) TierEU.RECIPE_UEV,
                13600); // 0.45 * 19660 = 8847

        // CaO + 3C = CaC2 + CO
        GT_Values.RA.addBlastRecipe(
                Materials.Quicklime.getDust(2),
                Materials.Carbon.getDust(3),
                GT_Values.NF,
                Materials.CarbonMonoxide.getGas(1000),
                MaterialsKevlar.CalciumCarbide.getDust(3),
                GT_Values.NI,
                600,
                (int) TierEU.RECIPE_HV,
                2573);
        // Ni + 3Al = NiAl3
        GT_Values.RA.addBlastRecipe(
                Materials.Nickel.getDust(1),
                Materials.Aluminium.getDust(3),
                GT_Values.NF,
                GT_Values.NF,
                MaterialsKevlar.NickelAluminide.getIngots(4),
                GT_Values.NI,
                900,
                (int) TierEU.RECIPE_HV,
                1688);

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

            GT_Values.RA.addBlastRecipe(
                    Materials.TranscendentMetal.getDust(1),
                    GT_Utility.getIntegratedCircuit(1),
                    Materials.Tungsten.getMolten(144),
                    new FluidStack(FluidRegistry.getFluid("molten.celestialtungsten"), 72),
                    GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.TranscendentMetal, 1L),
                    GT_Values.NI,
                    180 * 20,
                    32_000_000,
                    11701);

            // Rh + 3Cl = RhCl3
            GT_Values.RA.addBlastRecipe(
                    getModItem(BartWorks.modID, "gt.bwMetaGenerateddust", 1L, 78),
                    GT_Utility.getIntegratedCircuit(2),
                    Materials.Chlorine.getGas(3000L),
                    GT_Values.NF,
                    MaterialsKevlar.RhodiumChloride.getDust(4),
                    GT_Values.NI,
                    600,
                    (int) TierEU.RECIPE_HV,
                    573);
        }
    }

    public void primitiveBlastFurnaceRecipes() {
        GT_Values.RA.addPrimitiveBlastRecipe(
                Materials.Iron.getIngots(1),
                GT_Values.NI,
                4,
                Materials.Steel.getIngots(1),
                GT_Values.NI,
                7200);
        GT_Values.RA.addPrimitiveBlastRecipe(
                Materials.Iron.getDust(1),
                GT_Values.NI,
                4,
                Materials.Steel.getIngots(1),
                GT_Values.NI,
                7200);
        GT_Values.RA.addPrimitiveBlastRecipe(
                Materials.Iron.getBlocks(1),
                GT_Values.NI,
                36,
                Materials.Steel.getIngots(9),
                GT_Values.NI,
                64800);
        GT_Values.RA.addPrimitiveBlastRecipe(
                Materials.Steel.getDust(1),
                GT_Values.NI,
                2,
                Materials.Steel.getIngots(1),
                GT_Values.NI,
                7200);
    }
}
