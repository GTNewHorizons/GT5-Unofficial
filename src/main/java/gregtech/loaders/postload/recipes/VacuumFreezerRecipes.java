package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.recipe.RecipeMaps.vacuumRecipes;
import static gregtech.api.util.GT_RecipeBuilder.HOURS;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;

public class VacuumFreezerRecipes implements Runnable {

    @Override
    public void run() {
        // reactor parts vacuum
        {
            // reactor heat switch
            {
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_ModHandler.getIC2Item("reactorHeatSwitch", 1L, 32767))
                    .itemOutputs(GT_ModHandler.getIC2Item("reactorHeatSwitch", 1L, 1))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_ModHandler.getIC2Item("reactorHeatSwitchCore", 1L, 32767))
                    .itemOutputs(GT_ModHandler.getIC2Item("reactorHeatSwitchCore", 1L, 1))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_ModHandler.getIC2Item("reactorHeatSwitchSpread", 1L, 32767))
                    .itemOutputs(GT_ModHandler.getIC2Item("reactorHeatSwitchSpread", 1L, 1))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_ModHandler.getIC2Item("reactorHeatSwitchDiamond", 1L, 32767))
                    .itemOutputs(GT_ModHandler.getIC2Item("reactorHeatSwitchDiamond", 1L, 1))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumRecipes);
            }

            // reactor vent
            {
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_ModHandler.getIC2Item("reactorVent", 1L, 32767))
                    .itemOutputs(GT_ModHandler.getIC2Item("reactorVent", 1L, 1))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_ModHandler.getIC2Item("reactorVentCore", 1L, 32767))
                    .itemOutputs(GT_ModHandler.getIC2Item("reactorVentCore", 1L, 1))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_ModHandler.getIC2Item("reactorVentGold", 1L, 32767))
                    .itemOutputs(GT_ModHandler.getIC2Item("reactorVentGold", 1L, 1))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_ModHandler.getIC2Item("reactorVentDiamond", 1L, 32767))
                    .itemOutputs(GT_ModHandler.getIC2Item("reactorVentDiamond", 1L, 1))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumRecipes);
            }

            // reactor vent spread
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_ModHandler.getIC2Item("reactorVentSpread", 1L, 32767))
                .itemOutputs(GT_ModHandler.getIC2Item("reactorVentSpread", 1L, 0))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(vacuumRecipes);

            // reactor coolant
            {

                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_ModHandler.getIC2Item("reactorCoolantSimple", 1L, 32767))
                    .itemOutputs(GT_ModHandler.getIC2Item("reactorCoolantSimple", 1L, 1))
                    .duration(10 * TICKS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_ModHandler.getIC2Item("reactorCoolantTriple", 1L, 32767))
                    .itemOutputs(GT_ModHandler.getIC2Item("reactorCoolantTriple", 1L, 1))
                    .duration(1 * SECONDS + 10 * TICKS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_ModHandler.getIC2Item("reactorCoolantSix", 1L, 32767))
                    .itemOutputs(GT_ModHandler.getIC2Item("reactorCoolantSix", 1L, 1))
                    .duration(3 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Reactor_Coolant_He_1.getWildcard(1L))
                    .itemOutputs(ItemList.Reactor_Coolant_He_1.get(1L))
                    .duration(3 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Reactor_Coolant_He_3.getWildcard(1L))
                    .itemOutputs(ItemList.Reactor_Coolant_He_3.get(1L))
                    .duration(9 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Reactor_Coolant_He_6.getWildcard(1L))
                    .itemOutputs(ItemList.Reactor_Coolant_He_6.get(1L))
                    .duration(18 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Reactor_Coolant_NaK_1.getWildcard(1L))
                    .itemOutputs(ItemList.Reactor_Coolant_NaK_1.get(1L))
                    .duration(3 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Reactor_Coolant_NaK_3.getWildcard(1L))
                    .itemOutputs(ItemList.Reactor_Coolant_NaK_3.get(1L))
                    .duration(9 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Reactor_Coolant_NaK_6.getWildcard(1L))
                    .itemOutputs(ItemList.Reactor_Coolant_NaK_6.get(1L))
                    .duration(18 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.neutroniumHeatCapacitor.getWildcard(1L))
                    .itemOutputs(ItemList.neutroniumHeatCapacitor.get(1L))
                    .duration(13 * HOURS + 53 * MINUTES + 20 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Reactor_Coolant_Sp_1.getWildcard(1L))
                    .itemOutputs(ItemList.Reactor_Coolant_Sp_1.get(1L))
                    .duration(9 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Reactor_Coolant_Sp_2.getWildcard(1L))
                    .itemOutputs(ItemList.Reactor_Coolant_Sp_2.get(1L))
                    .duration(18 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Reactor_Coolant_Sp_3.getWildcard(1L))
                    .itemOutputs(ItemList.Reactor_Coolant_Sp_3.get(1L))
                    .duration(27 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Reactor_Coolant_Sp_6.getWildcard(1L))
                    .itemOutputs(ItemList.Reactor_Coolant_Sp_6.get(1L))
                    .duration(54 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumRecipes);

            }
        }

        // fluid vacuum
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1L))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Ice, 1L))
                .duration(2 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_MV)
                .addTo(vacuumRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1L))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.LiquidOxygen, 1L))
                .duration(1 * MINUTES)
                .eut(TierEU.RECIPE_HV)
                .addTo(vacuumRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Nitrogen, 1L))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.LiquidNitrogen, 1L))
                .duration(1 * MINUTES)
                .eut(TierEU.RECIPE_HV)
                .addTo(vacuumRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(GT_ModHandler.getIC2Item("airCell", 1L))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.LiquidAir, 1L))
                .duration(1 * SECONDS + 8 * TICKS)
                .eut(TierEU.RECIPE_HV)
                .addTo(vacuumRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(ItemList.Reactor_Coolant_Sp_1.getWildcard(1L))
                .itemOutputs(ItemList.Reactor_Coolant_Sp_1.get(1L))
                .duration(1 * MINUTES + 30 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(vacuumRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(ItemList.Reactor_Coolant_Sp_2.getWildcard(1L))
                .itemOutputs(ItemList.Reactor_Coolant_Sp_2.get(1L))
                .duration(3 * MINUTES)
                .eut(TierEU.RECIPE_MV)
                .addTo(vacuumRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(ItemList.Reactor_Coolant_Sp_3.getWildcard(1L))
                .itemOutputs(ItemList.Reactor_Coolant_Sp_3.get(1L))
                .duration(4 * MINUTES + 30 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(vacuumRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(ItemList.Reactor_Coolant_Sp_6.getWildcard(1L))
                .itemOutputs(ItemList.Reactor_Coolant_Sp_6.get(1L))
                .duration(9 * MINUTES)
                .eut(TierEU.RECIPE_MV)
                .addTo(vacuumRecipes);
        }

        // Freeze superconductors.
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.Pentacadmiummagnesiumhexaoxid, 1L))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Pentacadmiummagnesiumhexaoxid, 1L))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(vacuumRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.Titaniumonabariumdecacoppereikosaoxid, 1L))
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Titaniumonabariumdecacoppereikosaoxid, 1L))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(vacuumRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.Uraniumtriplatinid, 1L))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Uraniumtriplatinid, 1L))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_EV)
                .addTo(vacuumRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.Vanadiumtriindinid, 1L))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Vanadiumtriindinid, 1L))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(vacuumRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(
                        OrePrefixes.ingotHot,
                        Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid,
                        1L))
                .itemOutputs(
                    GT_OreDictUnificator.get(
                        OrePrefixes.ingot,
                        Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid,
                        1L))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(vacuumRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator
                        .get(OrePrefixes.ingotHot, Materials.Tetranaquadahdiindiumhexaplatiumosminid, 1L))
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Tetranaquadahdiindiumhexaplatiumosminid, 1L))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(vacuumRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.Longasssuperconductornameforuvwire, 1L))
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Longasssuperconductornameforuvwire, 1L))
                .duration(40 * SECONDS)
                .eut(TierEU.RECIPE_UV)
                .addTo(vacuumRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.Longasssuperconductornameforuhvwire, 1L))
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Longasssuperconductornameforuhvwire, 1L))
                .duration(1 * MINUTES + 20 * SECONDS)
                .eut(TierEU.RECIPE_UHV)
                .addTo(vacuumRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.SuperconductorUEVBase, 1L))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.SuperconductorUEVBase, 1L))
                .duration(2 * MINUTES + 40 * SECONDS)
                .eut(TierEU.RECIPE_UEV)
                .addTo(vacuumRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.SuperconductorUIVBase, 1L))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.SuperconductorUIVBase, 1L))
                .duration(2 * MINUTES + 40 * SECONDS)
                .eut(TierEU.RECIPE_UIV)
                .addTo(vacuumRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.SuperconductorUMVBase, 1L))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.SuperconductorUMVBase, 1L))
                .duration(2 * MINUTES + 40 * SECONDS)
                .eut(TierEU.RECIPE_UMV)
                .addTo(vacuumRecipes);
        }

        // Plasma Freezing
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(OrePrefixes.cellPlasma, Materials.Americium, 1L))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Americium, 1L))
                .duration(1 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(vacuumRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(OrePrefixes.cellPlasma, Materials.Helium, 1L))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Helium, 1L))
                .duration(5 * TICKS)
                .eut(TierEU.RECIPE_MV)
                .addTo(vacuumRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(OrePrefixes.cellPlasma, Materials.Nitrogen, 1L))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Nitrogen, 1L))
                .duration(1 * SECONDS + 8 * TICKS)
                .eut(TierEU.RECIPE_MV)
                .addTo(vacuumRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(OrePrefixes.cellPlasma, Materials.Oxygen, 1L))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1L))
                .duration(1 * SECONDS + 12 * TICKS)
                .eut(TierEU.RECIPE_MV)
                .addTo(vacuumRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(OrePrefixes.cellPlasma, Materials.Radon, 1L))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Radon, 1L))
                .duration(5 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_HV)
                .addTo(vacuumRecipes);

            GT_Values.RA.stdBuilder()
                .fluidInputs(Materials.Boron.getPlasma(144L))
                .fluidOutputs(Materials.Boron.getMolten(144L))
                .duration(1 * SECONDS)
                .eut(12)
                .addTo(vacuumRecipes);
        }

        if (GTPlusPlus.isModLoaded()) {
            // hot transcendent metal ingot cooling
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(OrePrefixes.ingotHot, MaterialsUEVplus.TranscendentMetal, 1L))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.ingot, MaterialsUEVplus.TranscendentMetal, 1L))
                .fluidInputs(
                    new FluidStack(FluidRegistry.getFluid("molten.titansteel"), 144),
                    Materials.SuperCoolant.getFluid(1000))
                .duration(1 * SECONDS)
                .eut(TierEU.RECIPE_UIV)
                .addTo(vacuumRecipes);
        }
    }
}
