package gregtech.loaders.postload.recipes;

import static gregtech.loaders.postload.GT_MachineRecipeLoader.isGTPPLoaded;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;

public class VacuumFreezerRecipes implements Runnable {

    @Override
    public void run() {
        // reactor parts vacuum
        // reactor heat switch
        GT_Values.RA.addVacuumFreezerRecipe(
                GT_ModHandler.getIC2Item("reactorHeatSwitch", 1L, 32767),
                GT_ModHandler.getIC2Item("reactorHeatSwitch", 1L, 1),
                100);
        GT_Values.RA.addVacuumFreezerRecipe(
                GT_ModHandler.getIC2Item("reactorHeatSwitchCore", 1L, 32767),
                GT_ModHandler.getIC2Item("reactorHeatSwitchCore", 1L, 1),
                100);
        GT_Values.RA.addVacuumFreezerRecipe(
                GT_ModHandler.getIC2Item("reactorHeatSwitchSpread", 1L, 32767),
                GT_ModHandler.getIC2Item("reactorHeatSwitchSpread", 1L, 1),
                100);
        GT_Values.RA.addVacuumFreezerRecipe(
                GT_ModHandler.getIC2Item("reactorHeatSwitchDiamond", 1L, 32767),
                GT_ModHandler.getIC2Item("reactorHeatSwitchDiamond", 1L, 1),
                100);
        // reactor vent
        GT_Values.RA.addVacuumFreezerRecipe(
                GT_ModHandler.getIC2Item("reactorVent", 1L, 32767),
                GT_ModHandler.getIC2Item("reactorVent", 1L, 1),
                100);
        GT_Values.RA.addVacuumFreezerRecipe(
                GT_ModHandler.getIC2Item("reactorVentCore", 1L, 32767),
                GT_ModHandler.getIC2Item("reactorVentCore", 1L, 1),
                100);
        GT_Values.RA.addVacuumFreezerRecipe(
                GT_ModHandler.getIC2Item("reactorVentGold", 1L, 32767),
                GT_ModHandler.getIC2Item("reactorVentGold", 1L, 1),
                100);
        GT_Values.RA.addVacuumFreezerRecipe(
                GT_ModHandler.getIC2Item("reactorVentDiamond", 1L, 32767),
                GT_ModHandler.getIC2Item("reactorVentDiamond", 1L, 1),
                100);
        // reactor vent spread
        GT_Values.RA.addVacuumFreezerRecipe(
                GT_ModHandler.getIC2Item("reactorVentSpread", 1L, 32767),
                GT_ModHandler.getIC2Item("reactorVentSpread", 1L, 0),
                100);
        // reactor coolant
        GT_Values.RA.addVacuumFreezerRecipe(
                GT_ModHandler.getIC2Item("reactorCoolantSimple", 1L, 32767),
                GT_ModHandler.getIC2Item("reactorCoolantSimple", 1L, 1),
                100);
        GT_Values.RA.addVacuumFreezerRecipe(
                GT_ModHandler.getIC2Item("reactorCoolantTriple", 1L, 32767),
                GT_ModHandler.getIC2Item("reactorCoolantTriple", 1L, 1),
                300);
        GT_Values.RA.addVacuumFreezerRecipe(
                GT_ModHandler.getIC2Item("reactorCoolantSix", 1L, 32767),
                GT_ModHandler.getIC2Item("reactorCoolantSix", 1L, 1),
                600);
        GT_Values.RA.addVacuumFreezerRecipe(
                ItemList.Reactor_Coolant_He_1.getWildcard(1L),
                ItemList.Reactor_Coolant_He_1.get(1L),
                600);
        GT_Values.RA.addVacuumFreezerRecipe(
                ItemList.Reactor_Coolant_He_3.getWildcard(1L),
                ItemList.Reactor_Coolant_He_3.get(1L),
                1800);
        GT_Values.RA.addVacuumFreezerRecipe(
                ItemList.Reactor_Coolant_He_6.getWildcard(1L),
                ItemList.Reactor_Coolant_He_6.get(1L),
                3600);
        GT_Values.RA.addVacuumFreezerRecipe(
                ItemList.Reactor_Coolant_NaK_1.getWildcard(1L),
                ItemList.Reactor_Coolant_NaK_1.get(1L),
                600);
        GT_Values.RA.addVacuumFreezerRecipe(
                ItemList.Reactor_Coolant_NaK_3.getWildcard(1L),
                ItemList.Reactor_Coolant_NaK_3.get(1L),
                1800);
        GT_Values.RA.addVacuumFreezerRecipe(
                ItemList.Reactor_Coolant_NaK_6.getWildcard(1L),
                ItemList.Reactor_Coolant_NaK_6.get(1L),
                3600);
        GT_Values.RA.addVacuumFreezerRecipe(
                ItemList.neutroniumHeatCapacitor.getWildcard(1L),
                ItemList.neutroniumHeatCapacitor.get(1L),
                10000000);
        GT_Values.RA.addVacuumFreezerRecipe(
                ItemList.Reactor_Coolant_Sp_1.getWildcard(1L),
                ItemList.Reactor_Coolant_Sp_1.get(1L),
                1800);
        GT_Values.RA.addVacuumFreezerRecipe(
                ItemList.Reactor_Coolant_Sp_2.getWildcard(1L),
                ItemList.Reactor_Coolant_Sp_2.get(1L),
                3600);
        GT_Values.RA.addVacuumFreezerRecipe(
                ItemList.Reactor_Coolant_Sp_3.getWildcard(1L),
                ItemList.Reactor_Coolant_Sp_3.get(1L),
                5400);
        GT_Values.RA.addVacuumFreezerRecipe(
                ItemList.Reactor_Coolant_Sp_6.getWildcard(1L),
                ItemList.Reactor_Coolant_Sp_6.get(1L),
                10800);

        // fluid vacuum
        GT_Values.RA.addVacuumFreezerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1L),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Ice, 1L),
                50);
        GT_Values.RA.addVacuumFreezerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1L),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.LiquidOxygen, 1L),
                1200,
                480);
        GT_Values.RA.addVacuumFreezerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Nitrogen, 1L),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.LiquidNitrogen, 1L),
                1200,
                480);
        GT_Values.RA.addVacuumFreezerRecipe(
                GT_ModHandler.getIC2Item("airCell", 1L),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.LiquidAir, 1L),
                28,
                480);

        GT_Values.RA.addVacuumFreezerRecipe(
                ItemList.Reactor_Coolant_Sp_1.getWildcard(1L),
                ItemList.Reactor_Coolant_Sp_1.get(1L),
                1800);
        GT_Values.RA.addVacuumFreezerRecipe(
                ItemList.Reactor_Coolant_Sp_2.getWildcard(1L),
                ItemList.Reactor_Coolant_Sp_2.get(1L),
                3600);
        GT_Values.RA.addVacuumFreezerRecipe(
                ItemList.Reactor_Coolant_Sp_3.getWildcard(1L),
                ItemList.Reactor_Coolant_Sp_3.get(1L),
                5400);
        GT_Values.RA.addVacuumFreezerRecipe(
                ItemList.Reactor_Coolant_Sp_6.getWildcard(1L),
                ItemList.Reactor_Coolant_Sp_6.get(1L),
                10800);

        // Freeze superconductors.
        GT_Values.RA.addVacuumFreezerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.Pentacadmiummagnesiumhexaoxid, 1L),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Pentacadmiummagnesiumhexaoxid, 1L),
                200,
                120);
        GT_Values.RA.addVacuumFreezerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.Titaniumonabariumdecacoppereikosaoxid, 1L),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Titaniumonabariumdecacoppereikosaoxid, 1L),
                200,
                480);
        GT_Values.RA.addVacuumFreezerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.Uraniumtriplatinid, 1L),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Uraniumtriplatinid, 1L),
                200,
                1920);
        GT_Values.RA.addVacuumFreezerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.Vanadiumtriindinid, 1L),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Vanadiumtriindinid, 1L),
                200,
                7680);
        GT_Values.RA.addVacuumFreezerRecipe(
                GT_OreDictUnificator.get(
                        OrePrefixes.ingotHot,
                        Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid,
                        1L),
                GT_OreDictUnificator.get(
                        OrePrefixes.ingot,
                        Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid,
                        1L),
                400,
                30720);
        GT_Values.RA.addVacuumFreezerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.Tetranaquadahdiindiumhexaplatiumosminid, 1L),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Tetranaquadahdiindiumhexaplatiumosminid, 1L),
                400,
                122880);
        GT_Values.RA.addVacuumFreezerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.Longasssuperconductornameforuvwire, 1L),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Longasssuperconductornameforuvwire, 1L),
                800,
                491520);
        GT_Values.RA.addVacuumFreezerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.Longasssuperconductornameforuhvwire, 1L),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Longasssuperconductornameforuhvwire, 1L),
                1600,
                1966080);
        GT_Values.RA.addVacuumFreezerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.SuperconductorUEVBase, 1L),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.SuperconductorUEVBase, 1L),
                3200,
                7864320);
        GT_Values.RA.addVacuumFreezerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.SuperconductorUIVBase, 1L),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.SuperconductorUIVBase, 1L),
                3200,
                30198988);
        GT_Values.RA.addVacuumFreezerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.SuperconductorUMVBase, 1L),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.SuperconductorUMVBase, 1L),
                3200,
                120795955);

        // Plasma Freezing
        GT_Values.RA.addVacuumFreezerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cellPlasma, Materials.Americium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Americium, 1L),
                20,
                30720);
        GT_Values.RA.addVacuumFreezerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cellPlasma, Materials.Helium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Helium, 1L),
                5,
                120);
        GT_Values.RA.addVacuumFreezerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cellPlasma, Materials.Nitrogen, 1L),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Nitrogen, 1L),
                28,
                120);
        GT_Values.RA.addVacuumFreezerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cellPlasma, Materials.Oxygen, 1L),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1L),
                32,
                120);
        GT_Values.RA.addVacuumFreezerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cellPlasma, Materials.Radon, 1L),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Radon, 1L),
                110,
                480);
        GT_Values.RA.addVacuumFreezerRecipe(Materials.Boron.getPlasma(144L), Materials.Boron.getMolten(144L), 20, 120);

        if (isGTPPLoaded) {
            GT_Values.RA.addVacuumFreezerRecipe(
                    new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.TranscendentMetal, 1L) },
                    new FluidStack[] { new FluidStack(FluidRegistry.getFluid("molten.titansteel"), 144),
                            Materials.SuperCoolant.getFluid(1000) },
                    new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.TranscendentMetal, 1L) },
                    new FluidStack[] { GT_Values.NF },
                    50 * 20,
                    32_000_000);
        }
    }
}
