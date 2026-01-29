package gregtech.loaders.postload.chains;

import static goodgenerator.api.recipe.GoodGeneratorRecipeMaps.preciseAssemblerRecipes;
import static gregtech.api.enums.Mods.AE2FluidCraft;
import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.OpenComputers;
import static gregtech.api.enums.Mods.UniversalSingularities;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.formingPressRecipes;
import static gregtech.api.recipe.RecipeMaps.laserEngraverRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.PRECISE_ASSEMBLER_CASING_TIER;
import static gtnhintergalactic.recipe.IGRecipeMaps.spaceAssemblerRecipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import bartworks.common.loaders.ItemRegistry;
import bartworks.system.material.WerkstoffLoader;
import ggfab.GGItemList;
import goodgenerator.util.ItemRefer;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtnhintergalactic.recipe.IGRecipeMaps;
import gtnhlanth.common.register.LanthItemList;
import gtnhlanth.common.register.WerkstoffMaterialPool;
import tectech.recipe.TTRecipeAdder;
import tectech.thing.CustomItemList;

// todo move to coremod
public class NACRecipes implements Runnable {

    @Override
    public void run() {
        // Nanochip Firewall Projection Casing
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.ReinforcementNanochipCasing.get(1),
            16777216,
            4096,
            (int) TierEU.RECIPE_UHV,
            4,
            new Object[] { ItemList.ReinforcementNanochipCasing.get(1), ItemList.MobRep_UV.get(4),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.SuperconductorUHVBase, 8),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.TengamAttuned, 8),
                ItemList.Emitter_UEV.get(2), ItemList.Sensor_UEV.get(2),
                getModItem(UniversalSingularities.ID, "universal.general.singularity", 1, 24), // Nether Star
                                                                                               // Singularity
                ItemList.Field_Generator_UHV.get(1) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(128 * INGOTS),
                WerkstoffLoader.Oganesson.getFluidOrGas(16000), Materials.SuperconductorUEVBase.getMolten(16 * INGOTS),
                Materials.RadoxPolymer.getMolten(4 * INGOTS) },
            ItemList.FirewallProjectionNanochipCasing.get(1),
            120 * SECONDS,
            (int) TierEU.RECIPE_UEV);

        // Nanochip Computational Matrix Casing
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.MeshInterfaceNanochipCasing.get(1),
            16777216,
            4096,
            (int) TierEU.RECIPE_UHV,
            4,
            new Object[] { ItemList.MeshInterfaceNanochipCasing.get(4), CustomItemList.rack_Hatch.get(1),
                getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1, 60), // 16384k Item Component
                getModItem(AE2FluidCraft.ID, "fluid_part", 1, 7), // 16384k Fluid Component
                getModItem(OpenComputers.ID, "item", 1, 69), // Server Tier 4
                ItemList.Optically_Perfected_CPU.get(2), ItemList.Optically_Compatible_Memory.get(2),
                CustomItemList.DATApipe.get(16) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(16 * INGOTS),
                Materials.Xenoxene.getFluid(16 * INGOTS), Materials.TengamPurified.getMolten(16 * INGOTS), },
            ItemList.ComputationalMatrixNanochipCasing.get(4),
            60 * SECONDS,
            (int) TierEU.RECIPE_UHV);

        // Nanochip Complex Glass
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(ItemRegistry.bw_realglas, 8, 14),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.RoseGold, 64),
                GregtechItemList.Laser_Lens_Special.get(0))
            .fluidInputs(WerkstoffMaterialPool.FluoroformOxygenMix.getFluidOrGas(2000))
            .itemOutputs(ItemList.ComplexNanochipGlass.get(8))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(laserEngraverRecipes);

        // Nanochip Reinforcement Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Bedrockium, 8),
                MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFoil(48),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Naquadah, 4))
            .fluidInputs(Materials.Neutronium.getMolten(8 * INGOTS))
            .itemOutputs(ItemList.ReinforcementNanochipCasing.get(8))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(formingPressRecipes);

        // Nanochip Mesh Interface Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Iridium, 8),
                WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.foil, 48),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Adamantium, 4),
                ItemList.VacuumConveyorPipe.get(8))
            .fluidInputs(
                Materials.BlackPlutonium.getMolten(8 * INGOTS),
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(8 * INGOTS))
            .itemOutputs(ItemList.MeshInterfaceNanochipCasing.get(8))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(PRECISE_ASSEMBLER_CASING_TIER, 3)
            .addTo(preciseAssemblerRecipes);

        // Vacuum Conveyor Input
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.MeshInterfaceNanochipCasing.get(1),
                ItemList.Hatch_Input_Bus_MAX.get(1),
                ItemList.Conveyor_Module_UEV.get(1),
                ItemList.VacuumConveyorPipe.get(8))
            .fluidInputs(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(4 * INGOTS))
            .itemOutputs(ItemList.Hatch_VacuumConveyor_Input.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);

        // Vacuum Conveyor Output
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.MeshInterfaceNanochipCasing.get(1),
                ItemList.Hatch_Output_Bus_MAX.get(1),
                ItemList.Conveyor_Module_UEV.get(1),
                ItemList.VacuumConveyorPipe.get(8))
            .fluidInputs(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(4 * INGOTS))
            .itemOutputs(ItemList.Hatch_VacuumConveyor_Output.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);

        // Vacuum Conveyor Pipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.pipeSmall, Materials.BlackPlutonium, 1),
                MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getFoil(4))
            .fluidInputs(Materials.Kevlar.getMolten(1 * INGOTS))
            .itemOutputs(ItemList.VacuumConveyorPipe.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);

        // Splitter Redstone Hatch
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_UEV.get(1),
                ItemList.Cover_AdvancedRedstoneReceiver.get(1),
                ItemList.Sensor_UHV.get(1),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.RedstoneAlloy, 4))
            .fluidInputs(Materials.Redstone.getMolten(64 * INGOTS))
            .itemOutputs(ItemList.Hatch_Splitter_Level.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);

        // Nanochip Assembly Complex
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Circuit_OpticalMainframe.get(1), // todo check that this isn't already used
            16777216,
            4096,
            (int) TierEU.RECIPE_UHV,
            4,
            new Object[] { GTUtility.copyAmount(64, ItemRegistry.cal), CustomItemList.Machine_Multi_Computer.get(8),
                CustomItemList.Machine_Multi_Research.get(8), CustomItemList.Machine_Multi_DataBank.get(8),
                ItemList.MeshInterfaceNanochipCasing.get(16), ItemList.ReinforcementNanochipCasing.get(16),
                ItemList.ComputationalMatrixNanochipCasing.get(8), ItemList.FirewallProjectionNanochipCasing.get(4),
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 8 },
                new Object[] { OrePrefixes.circuit.get(Materials.UHV), 16 },
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 32 }, ItemList.ZPM2.get(1),
                ItemList.Sensor_UEV.get(4), ItemList.Emitter_UEV.get(4), getModItem(OpenComputers.ID, "screen3", 1, 0),
                getModItem(OpenComputers.ID, "keyboard", 1, 0) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(256 * INGOTS),
                new FluidStack(MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getPlasma(), 64 * INGOTS),
                Materials.Americium.getPlasma(64 * INGOTS),
                new FluidStack(MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getPlasma(), 64 * INGOTS) },
            ItemList.Machine_Multi_NanochipAssemblyComplex.get(1),
            120 * SECONDS,
            (int) TierEU.RECIPE_UIV);

        // Assembly Matrix
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.MeshInterfaceNanochipCasing.get(1),
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 4 },
                ItemList.VacuumConveyorPipe.get(16),
                ItemList.Robot_Arm_UEV.get(1),
                ItemList.SpaceElevatorModuleAssemblerT1.get(1),
                ItemRefer.Precise_Assembler.get(1),
                GGItemList.AdvAssLine.get(1),
                GTUtility.copyAmount(1, ItemRegistry.cal),
                ItemRefer.Compassline_Casing_UEV.get(4),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.SuperconductorUHVBase, 32),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Infinity, 4),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Silver, 1))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(32 * INGOTS),
                Materials.NaquadahAlloy.getMolten(32 * INGOTS),
                Materials.Infinity.getMolten(32 * INGOTS))
            .itemOutputs(ItemList.NanoChipModule_AssemblyMatrix.get(1))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(IGRecipeMaps.MODULE_TIER, 1)
            .addTo(spaceAssemblerRecipes);

        // Etching Array
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.MeshInterfaceNanochipCasing.get(1),
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 4 },
                ItemList.VacuumConveyorPipe.get(16),
                ItemList.Sensor_UEV.get(1),
                GTUtility.copyAmount(1, LanthItemList.TARGET_CHAMBER),
                ItemList.Machine_Multi_Autoclave.get(1),
                ItemList.Circuit_Parts_Crystal_Chip_Wetware.get(16),
                ItemList.Circuit_Parts_Crystal_Chip_Master.get(16))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(32 * INGOTS),
                Materials.EnrichedHolmium.getMolten(32 * INGOTS))
            .itemOutputs(ItemList.NanoChipModule_EtchingArray.get(1))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(IGRecipeMaps.MODULE_TIER, 1)
            .addTo(spaceAssemblerRecipes);

        // SMD Processor
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.MeshInterfaceNanochipCasing.get(1),
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 4 },
                ItemList.VacuumConveyorPipe.get(16),
                ItemList.Conveyor_Module_UEV.get(1),
                ItemRefer.Precise_Assembler.get(1),
                GregtechItemList.GT4_Multi_Crafter.get(1),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Polyethylene, 64),
                ItemList.Circuit_Parts_InductorXSMD.get(64))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(32 * INGOTS),
                Materials.RadoxPolymer.getMolten(32 * INGOTS))
            .itemOutputs(ItemList.NanoChipModule_SMDProcessor.get(1))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(IGRecipeMaps.MODULE_TIER, 1)
            .addTo(spaceAssemblerRecipes);

        // Board Processor
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.MeshInterfaceNanochipCasing.get(1),
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 4 },
                ItemList.VacuumConveyorPipe.get(16),
                ItemList.FluidRegulator_UEV.get(1),
                ItemList.PCBFactory.get(1),
                GregtechItemList.Industrial_WashPlant.get(1),
                ItemList.Circuit_Board_Bio_Ultra.get(16),
                ItemList.Circuit_Board_Wetware_Extreme.get(16))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(32 * INGOTS),
                MaterialsAlloy.OCTIRON.getFluidStack(32 * INGOTS))
            .itemOutputs(ItemList.NanoChipModule_BoardProcessor.get(1))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(IGRecipeMaps.MODULE_TIER, 1)
            .addTo(spaceAssemblerRecipes);

        // Biological Coordinator
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.MeshInterfaceNanochipCasing.get(1),
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 4 },
                ItemList.VacuumConveyorPipe.get(16),
                ItemList.Electric_Pump_UEV.get(1),
                ItemList.PCBBioChamber.get(1),
                GTUtility.copyAmount(1, ItemRegistry.vat),
                ItemList.Circuit_Chip_BioCPU.get(16),
                ItemList.Circuit_Chip_NeuroCPU.get(16))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(32 * INGOTS),
                Materials.Tritanium.getMolten(32 * INGOTS))
            .itemOutputs(ItemList.NanoChipModule_BiologicalCoordinator.get(1))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(IGRecipeMaps.MODULE_TIER, 1)
            .addTo(spaceAssemblerRecipes);

        // Cutting Chamber
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.MeshInterfaceNanochipCasing.get(1),
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 4 },
                ItemList.VacuumConveyorPipe.get(16),
                ItemList.Robot_Arm_UEV.get(1),
                ItemList.Machine_Multi_IndustrialLaserEngraver.get(1),
                GregtechItemList.Industrial_CuttingFactoryController.get(1),
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1)
            // todo chromatic glass lens (coremod)
            )
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(32 * INGOTS),
                Materials.Neutronium.getMolten(32 * INGOTS))
            .itemOutputs(ItemList.NanoChipModule_CuttingChamber.get(1))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(IGRecipeMaps.MODULE_TIER, 1)
            .addTo(spaceAssemblerRecipes);

        // Wire Tracer
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.MeshInterfaceNanochipCasing.get(1),
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 4 },
                ItemList.VacuumConveyorPipe.get(16),
                ItemList.Electric_Motor_UEV.get(1),
                GregtechItemList.Industrial_WireFactory.get(1),
                GregtechItemList.Industrial_PlatePress.get(1), // forming press specifically
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Draconium, 16),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.BlackPlutonium, 16))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(32 * INGOTS),
                Materials.SuperconductorUHVBase.getMolten(32 * INGOTS))
            .itemOutputs(ItemList.NanoChipModule_WireTracer.get(1))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(IGRecipeMaps.MODULE_TIER, 1)
            .addTo(spaceAssemblerRecipes);

        // Splitter
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.MeshInterfaceNanochipCasing.get(1),
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 4 },
                ItemList.VacuumConveyorPipe.get(16),
                ItemList.Robot_Arm_UEV.get(1),
                ItemList.Hatch_VacuumConveyor_Input.get(1),
                ItemList.Hatch_VacuumConveyor_Output.get(1),
                ItemList.Automation_ItemDistributor_MAX.get(1),
                ItemList.Sensor_UEV.get(1))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(32 * INGOTS),
                Materials.Kevlar.getMolten(32 * INGOTS))
            .itemOutputs(ItemList.NanoChipModule_Splitter.get(1))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(IGRecipeMaps.MODULE_TIER, 1)
            .addTo(spaceAssemblerRecipes);

        // Superconductor Splitter
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.MeshInterfaceNanochipCasing.get(1),
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 4 },
                ItemList.VacuumConveyorPipe.get(16),
                ItemList.Emitter_UEV.get(1),
                ItemList.Machine_Multi_IndustrialElectromagneticSeparator.get(1),
                GregtechItemList.Industrial_Cryogenic_Freezer.get(1),
                ItemList.Electromagnet_Tengam.get(1),
                ItemList.Reactor_Coolant_Sp_6.get(1))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(32 * INGOTS),
                Materials.Naquadria.getMolten(32 * INGOTS))
            .itemOutputs(ItemList.NanoChipModule_SuperconductorSplitter.get(1))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(IGRecipeMaps.MODULE_TIER, 1)
            .addTo(spaceAssemblerRecipes);

        // Optical Organizer
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.MeshInterfaceNanochipCasing.get(1),
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 4 },
                ItemList.VacuumConveyorPipe.get(16),
                ItemList.Electric_Pump_UEV.get(1),
                ItemList.SpaceElevatorModuleAssemblerT1.get(1),
                ItemList.Machine_Multi_PurificationPlant.get(1),
                ItemList.Optically_Perfected_CPU.get(16),
                ItemList.Optically_Compatible_Memory.get(16))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(32 * INGOTS),
                Materials.DraconiumAwakened.getMolten(32 * INGOTS))
            .itemOutputs(ItemList.NanoChipModule_OpticalOrganizer.get(1))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(IGRecipeMaps.MODULE_TIER, 1)
            .addTo(spaceAssemblerRecipes);

        // Encasement Wrapper
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.MeshInterfaceNanochipCasing.get(1),
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 4 },
                ItemList.VacuumConveyorPipe.get(16),
                ItemList.Electric_Piston_UEV.get(1),
                GregtechItemList.Amazon_Warehouse_Controller.get(1),
                ItemList.LATEX.get(1),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Tritanium, 16),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole, 64))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(32 * INGOTS),
                Materials.Quantium.getMolten(32 * INGOTS))
            .itemOutputs(ItemList.NanoChipModule_EncasementWrapper.get(1))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(IGRecipeMaps.MODULE_TIER, 1)
            .addTo(spaceAssemblerRecipes);
    }
}
