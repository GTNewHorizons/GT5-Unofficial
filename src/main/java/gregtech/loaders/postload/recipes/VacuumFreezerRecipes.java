package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.GTValues.M;
import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;
import static gregtech.api.util.GTRecipeBuilder.HOURS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.CellShapes;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class VacuumFreezerRecipes implements Runnable {

    @Override
    public void run() {
        // reactor parts vacuum
        {
            // reactor heat switch
            {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTModHandler.getIC2Item("reactorHeatSwitch", 1L, 32767))
                    .itemOutputs(GTModHandler.getIC2Item("reactorHeatSwitch", 1L, 1))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumFreezerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTModHandler.getIC2Item("reactorHeatSwitchCore", 1L, 32767))
                    .itemOutputs(GTModHandler.getIC2Item("reactorHeatSwitchCore", 1L, 1))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumFreezerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTModHandler.getIC2Item("reactorHeatSwitchSpread", 1L, 32767))
                    .itemOutputs(GTModHandler.getIC2Item("reactorHeatSwitchSpread", 1L, 1))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumFreezerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTModHandler.getIC2Item("reactorHeatSwitchDiamond", 1L, 32767))
                    .itemOutputs(GTModHandler.getIC2Item("reactorHeatSwitchDiamond", 1L, 1))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumFreezerRecipes);
            }

            // reactor vent
            {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTModHandler.getIC2Item("reactorVent", 1L, 32767))
                    .itemOutputs(GTModHandler.getIC2Item("reactorVent", 1L, 1))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumFreezerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTModHandler.getIC2Item("reactorVentCore", 1L, 32767))
                    .itemOutputs(GTModHandler.getIC2Item("reactorVentCore", 1L, 1))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumFreezerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTModHandler.getIC2Item("reactorVentGold", 1L, 32767))
                    .itemOutputs(GTModHandler.getIC2Item("reactorVentGold", 1L, 1))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumFreezerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTModHandler.getIC2Item("reactorVentDiamond", 1L, 32767))
                    .itemOutputs(GTModHandler.getIC2Item("reactorVentDiamond", 1L, 1))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumFreezerRecipes);
            }

            // reactor vent spread
            GTValues.RA.stdBuilder()
                .itemInputs(GTModHandler.getIC2Item("reactorVentSpread", 1L, 32767))
                .itemOutputs(GTModHandler.getIC2Item("reactorVentSpread", 1L, 0))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(vacuumFreezerRecipes);

            // reactor coolant
            {

                GTValues.RA.stdBuilder()
                    .itemInputs(GTModHandler.getIC2Item("reactorCoolantSimple", 1L, 32767))
                    .itemOutputs(GTModHandler.getIC2Item("reactorCoolantSimple", 1L, 1))
                    .duration(10 * TICKS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumFreezerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTModHandler.getIC2Item("reactorCoolantTriple", 1L, 32767))
                    .itemOutputs(GTModHandler.getIC2Item("reactorCoolantTriple", 1L, 1))
                    .duration(1 * SECONDS + 10 * TICKS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumFreezerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTModHandler.getIC2Item("reactorCoolantSix", 1L, 32767))
                    .itemOutputs(GTModHandler.getIC2Item("reactorCoolantSix", 1L, 1))
                    .duration(3 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumFreezerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.Reactor_Coolant_He_1.getWildcard(1L))
                    .itemOutputs(ItemList.Reactor_Coolant_He_1.get(1L))
                    .duration(3 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumFreezerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.Reactor_Coolant_He_3.getWildcard(1L))
                    .itemOutputs(ItemList.Reactor_Coolant_He_3.get(1L))
                    .duration(9 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumFreezerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.Reactor_Coolant_He_6.getWildcard(1L))
                    .itemOutputs(ItemList.Reactor_Coolant_He_6.get(1L))
                    .duration(18 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumFreezerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.Reactor_Coolant_NaK_1.getWildcard(1L))
                    .itemOutputs(ItemList.Reactor_Coolant_NaK_1.get(1L))
                    .duration(3 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumFreezerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.Reactor_Coolant_NaK_3.getWildcard(1L))
                    .itemOutputs(ItemList.Reactor_Coolant_NaK_3.get(1L))
                    .duration(9 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumFreezerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.Reactor_Coolant_NaK_6.getWildcard(1L))
                    .itemOutputs(ItemList.Reactor_Coolant_NaK_6.get(1L))
                    .duration(18 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumFreezerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.neutroniumHeatCapacitor.getWildcard(1L))
                    .itemOutputs(ItemList.neutroniumHeatCapacitor.get(1L))
                    .duration(13 * HOURS + 53 * MINUTES + 20 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumFreezerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.Reactor_Coolant_Sp_1.getWildcard(1L))
                    .itemOutputs(ItemList.Reactor_Coolant_Sp_1.get(1L))
                    .duration(9 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumFreezerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.Reactor_Coolant_Sp_2.getWildcard(1L))
                    .itemOutputs(ItemList.Reactor_Coolant_Sp_2.get(1L))
                    .duration(18 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumFreezerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.Reactor_Coolant_Sp_3.getWildcard(1L))
                    .itemOutputs(ItemList.Reactor_Coolant_Sp_3.get(1L))
                    .duration(27 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumFreezerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.Reactor_Coolant_Sp_6.getWildcard(1L))
                    .itemOutputs(ItemList.Reactor_Coolant_Sp_6.get(1L))
                    .duration(54 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumFreezerRecipes);

            }
        }

        // fluid vacuum
        {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1L))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Ice, 1L))
                .duration(2 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_MV)
                .addTo(vacuumFreezerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(MaterialParts.requireCell(Materials.Oxygen, 1))
                .itemOutputs(MaterialParts.requireCell(Materials.LiquidOxygen, 1))
                .duration(1 * MINUTES)
                .eut(TierEU.RECIPE_HV)
                .addTo(vacuumFreezerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(MaterialParts.requireCell(Materials.Nitrogen, 1))
                .itemOutputs(MaterialParts.requireCell(Materials.LiquidNitrogen, 1))
                .duration(1 * MINUTES)
                .eut(TierEU.RECIPE_HV)
                .addTo(vacuumFreezerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Cell_Air.get(1))
                .itemOutputs(MaterialLibAPI.getStack(Materials.LiquidAir, CellShapes.cell, 1))
                .duration(1 * SECONDS + 8 * TICKS)
                .eut(TierEU.RECIPE_HV)
                .addTo(vacuumFreezerRecipes);
        }

        // Freeze superconductors.
        {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.ingotHot, Materials.Pentacadmiummagnesiumhexaoxid, 1L))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Pentacadmiummagnesiumhexaoxid, 1L))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(vacuumFreezerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.ingotHot, Materials.Titaniumonabariumdecacoppereikosaoxid, 1L))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Titaniumonabariumdecacoppereikosaoxid, 1L))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(vacuumFreezerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.ingotHot, Materials.Uraniumtriplatinid, 1L))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Uraniumtriplatinid, 1L))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_EV)
                .addTo(vacuumFreezerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.ingotHot, Materials.Vanadiumtriindinid, 1L))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Vanadiumtriindinid, 1L))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(vacuumFreezerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(
                        OrePrefixes.ingotHot,
                        Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid,
                        1L))
                .itemOutputs(
                    GTOreDictUnificator.get(
                        OrePrefixes.ingot,
                        Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid,
                        1L))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(vacuumFreezerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator
                        .get(OrePrefixes.ingotHot, Materials.Tetranaquadahdiindiumhexaplatiumosminid, 1L))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Tetranaquadahdiindiumhexaplatiumosminid, 1L))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(vacuumFreezerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.ingotHot, Materials.Longasssuperconductornameforuvwire, 1L))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Longasssuperconductornameforuvwire, 1L))
                .duration(40 * SECONDS)
                .eut(TierEU.RECIPE_UV)
                .addTo(vacuumFreezerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.ingotHot, Materials.Longasssuperconductornameforuhvwire, 1L))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Longasssuperconductornameforuhvwire, 1L))
                .duration(1 * MINUTES + 20 * SECONDS)
                .eut(TierEU.RECIPE_UHV)
                .addTo(vacuumFreezerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(Materials.SuperconductorUEVBase, Shapes.ingotHot, 1))
                .itemOutputs(MaterialLibAPI.getStack(Materials.SuperconductorUEVBase, Shapes.ingot, 1))
                .duration(2 * MINUTES + 40 * SECONDS)
                .eut(TierEU.RECIPE_UEV)
                .addTo(vacuumFreezerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(Materials.SuperconductorUIVBase, Shapes.ingotHot, 1))
                .itemOutputs(MaterialLibAPI.getStack(Materials.SuperconductorUIVBase, Shapes.ingot, 1))
                .duration(2 * MINUTES + 40 * SECONDS)
                .eut(TierEU.RECIPE_UIV)
                .addTo(vacuumFreezerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(Materials.SuperconductorUMVBase, Shapes.ingotHot, 1))
                .itemOutputs(MaterialLibAPI.getStack(Materials.SuperconductorUMVBase, Shapes.ingot, 1))
                .duration(2 * MINUTES + 40 * SECONDS)
                .eut(TierEU.RECIPE_UMV)
                .addTo(vacuumFreezerRecipes);
        }

        // Plasma Freezing
        {
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(Materials.Americium, CellShapes.cellPlasma, 1))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.Americium, 1L))
                .duration(1 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(vacuumFreezerRecipes);

            GTValues.RA.stdBuilder()
                .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Boron, FluidShapes.fluidPlasma, INGOTS))
                .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Boron, FluidShapes.fluidMolten, INGOTS))
                .duration(1 * SECONDS)
                .eut(12)
                .addTo(vacuumFreezerRecipes);
        }

        // hot transcendent metal ingot cooling
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.TranscendentMetal, Shapes.ingotHot, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.TranscendentMetal, Shapes.ingot, 1))
            .fluidInputs(
                new FluidStack(FluidRegistry.getFluid("molten.titansteel"), 1 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.SuperCoolant, FluidShapes.fluidLiquid, 1_000))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .addTo(vacuumFreezerRecipes);

        // Proto-Halkonite
        this.addProtoHalkonitePartRecipe(OrePrefixes.frameGt, 1);
        this.addProtoHalkonitePartRecipe(OrePrefixes.ingot, 1);
        this.addProtoHalkonitePartRecipe(OrePrefixes.plate, 1);
        this.addProtoHalkonitePartRecipe(OrePrefixes.plateDouble, 1);
        this.addProtoHalkonitePartRecipe(OrePrefixes.plateDense, 1);
        this.addProtoHalkonitePartRecipe(OrePrefixes.stick, 2);
        this.addProtoHalkonitePartRecipe(OrePrefixes.round, 8);
        this.addProtoHalkonitePartRecipe(OrePrefixes.bolt, 8);
        this.addProtoHalkonitePartRecipe(OrePrefixes.screw, 8);
        this.addProtoHalkonitePartRecipe(OrePrefixes.ring, 4);
        this.addProtoHalkonitePartRecipe(OrePrefixes.foil, 8);
        this.addProtoHalkonitePartRecipe(OrePrefixes.gearGtSmall, 1);
        this.addProtoHalkonitePartRecipe(OrePrefixes.rotor, 1);
        this.addProtoHalkonitePartRecipe(OrePrefixes.stickLong, 1);
        this.addProtoHalkonitePartRecipe(OrePrefixes.gearGt, 1);
        this.addProtoHalkonitePartRecipe(OrePrefixes.wireFine, 8);
        this.addProtoHalkonitePartRecipe(OrePrefixes.plateSuperdense, 1);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Harmonic_Compound.get(2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.AtomicSeparationCatalyst, FluidShapes.fluidMolten, INGOTS))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Mellion, Shapes.ingot, 1))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Creon, FluidShapes.fluidMolten, INGOTS))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .addTo(vacuumFreezerRecipes);
    }

    private void addProtoHalkonitePartRecipe(OrePrefixes prefix, final int multiplier) {
        final int partFraction = (int) (prefix.getMaterialAmount() * INGOTS / M);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(prefix, Materials.hotprotohalkonite, multiplier))
            .itemOutputs(GTOreDictUnificator.get(prefix, Materials.protohalkonite, multiplier))
            .fluidInputs(
                MaterialUtils.fluid(Materials.dimensionallyshiftedsuperfluid, (long) partFraction * multiplier / 4),
                MaterialLibAPI.getFluidStack(
                    Materials.SuperCoolant,
                    FluidShapes.fluidLiquid,
                    (int) (partFraction * multiplier * 4)))
            .duration((int) (multiplier * (SECONDS * partFraction / (float) INGOTS)))
            .eut(TierEU.RECIPE_UIV)
            .addTo(vacuumFreezerRecipes);

    }
}
