package common.recipeLoaders;

import static goodgenerator.loader.Loaders.huiCircuit;
import static gregtech.api.enums.Mods.Avaritia;
import static gregtech.api.enums.Mods.EnderIO;
import static gregtech.api.enums.Mods.EternalSingularity;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.UniversalSingularities;
import static gregtech.api.util.GT_RecipeBuilder.HOURS;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeConstants.AssemblyLine;
import static gregtech.api.util.GT_RecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GT_RecipeConstants.RESEARCH_TIME;
import static gtPlusPlus.core.material.ELEMENT.STANDALONE.HYPOGEN;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import common.Blocks;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;

public class AssemblyLine implements Runnable {

    @Override
    public void run() {
        final Fluid radoxPoly = FluidRegistry.getFluid("molten.radoxpoly") != null
            ? FluidRegistry.getFluid("molten.radoxpoly")
            : FluidRegistry.getFluid("molten.polybenzimidazole");

        final Fluid solderIndalloy = FluidRegistry.getFluid("molten.indalloy140") != null
            ? FluidRegistry.getFluid("molten.indalloy140")
            : FluidRegistry.getFluid("molten.solderingalloy");

        // TFFTStorageField6
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, new ItemStack(Blocks.tfftStorageField, 1, 5))
            .metadata(RESEARCH_TIME, 1 * HOURS + 6 * MINUTES + 40 * SECONDS)
            .itemInputs(
                ItemList.Casing_Tank_7.get(4),
                GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.CrystallinePinkSlime, 6),
                GT_OreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.Naquadah, 3),
                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.ChromeBars", 6),
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Quantium, 6),
                ItemList.Field_Generator_EV.get(8),
                ItemList.FluidRegulator_LuV.get(1),
                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.EngineeringProcessorFluidEmeraldCore", 4))
            .fluidInputs(Materials.Enderium.getMolten(1440), Materials.Polybenzimidazole.getMolten(1584))
            .itemOutputs(new ItemStack(Blocks.tfftStorageField, 1, 6))
            .eut(TierEU.RECIPE_LuV)
            .duration(30 * SECONDS)
            .addTo(AssemblyLine);

        // TFFTStorageField7
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, new ItemStack(Blocks.tfftStorageField, 1, 6))
            .metadata(RESEARCH_TIME, 1 * HOURS + 6 * MINUTES + 40 * SECONDS)
            .itemInputs(
                ItemList.Casing_Tank_10.get(16),
                GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.MelodicAlloy, 6),
                GT_OreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.NetherStar, 3),
                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.OsmiumBars", 6),
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.MysteriousCrystal, 6),
                ItemList.Field_Generator_IV.get(16),
                ItemList.Field_Generator_LuV.get(4),
                ItemList.FluidRegulator_UV.get(1),
                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.EngineeringProcessorFluidEmeraldCore", 16))
            .fluidInputs(Materials.CrystallineAlloy.getMolten(2880), Materials.Polybenzimidazole.getMolten(2016))
            .itemOutputs(new ItemStack(Blocks.tfftStorageField, 1, 7))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(AssemblyLine);

        // TFFTStorageField8
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, new ItemStack(Blocks.tfftStorageField, 1, 7))
            .metadata(RESEARCH_TIME, 2 * HOURS + 40 * MINUTES)
            .itemInputs(
                ItemList.Quantum_Tank_IV.get(1),
                GT_ModHandler.getModItem(Avaritia.ID, "Neutronium_Compressor", 1),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.StellarAlloy, 6),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.StellarAlloy, 6),
                GT_OreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.DraconiumAwakened, 3),
                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.NeutroniumBars", 6),
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.InfinityCatalyst, 6),
                ItemList.Field_Generator_ZPM.get(16),
                ItemList.Field_Generator_UV.get(4),
                new ItemStack(huiCircuit, 4, 2),
                GT_ModHandler.getModItem(UniversalSingularities.ID, "universal.tinkersConstruct.singularity", 1, 4))
            .fluidInputs(Materials.CrystallinePinkSlime.getMolten(4320), new FluidStack(radoxPoly, 2880))
            .itemOutputs(new ItemStack(Blocks.tfftStorageField, 1, 8))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(AssemblyLine);

        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, new ItemStack(Blocks.tfftStorageField, 1, 8))
            .metadata(RESEARCH_TIME, 2 * HOURS + 13 * MINUTES + 20 * SECONDS)
            .itemInputs(
                ItemList.Quantum_Tank_IV.get(4),
                GT_ModHandler.getModItem(Avaritia.ID, "Neutronium_Compressor", 2),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.TranscendentMetal, 6),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.TranscendentMetal, 6),
                GT_OreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.Infinity, 3),
                ItemList.EnergisedTesseract.get(1),
                HYPOGEN.getRotor(6),
                ItemList.Field_Generator_UHV.get(16),
                ItemList.Field_Generator_UEV.get(4),
                new ItemStack(huiCircuit, 4, 3),
                GT_ModHandler.getModItem(UniversalSingularities.ID, "universal.tinkersConstruct.singularity", 1, 4))
            .fluidInputs(Materials.MelodicAlloy.getMolten(5760), new FluidStack(radoxPoly, 3456))
            .itemOutputs(new ItemStack(Blocks.tfftStorageField, 1, 9))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UMV)
            .addTo(AssemblyLine);

        // TFFTStorageField10
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, new ItemStack(Blocks.tfftStorageField, 1, 9))
            .metadata(RESEARCH_TIME, 2 * HOURS + 46 * MINUTES + 40 * SECONDS)
            .itemInputs(
                ItemList.Quantum_Tank_IV.get(16),
                GT_ModHandler.getModItem(Avaritia.ID, "Neutronium_Compressor", 4),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.SpaceTime, 6),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.SpaceTime, 6),
                GT_OreDictUnificator.get(OrePrefixes.pipeNonuple, MaterialsUEVplus.SpaceTime, 3),
                ItemList.EnergisedTesseract.get(6),
                GT_OreDictUnificator.get(OrePrefixes.rotor, MaterialsUEVplus.SpaceTime, 6),
                ItemList.Field_Generator_UEV.get(16),
                ItemList.Field_Generator_UIV.get(4),
                new ItemStack(huiCircuit, 4, 4),
                new ItemStack(huiCircuit, 4, 4),
                GT_ModHandler.getModItem(EnderIO.ID, "itemBasicCapacitor", 64, 6),
                GT_ModHandler.getModItem(EternalSingularity.ID, "eternal_singularity", 1))
            .fluidInputs(Materials.StellarAlloy.getMolten(7200), new FluidStack(radoxPoly, 4608))
            .itemOutputs(new ItemStack(Blocks.tfftStorageField, 1, 10))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UXV)
            .addTo(AssemblyLine);

        // LuV Capacitor
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 1))
            .metadata(RESEARCH_TIME, 4 * HOURS)
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Osmiridium, 4),
                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Osmiridium, 24),
                ItemList.Circuit_Board_Elite.get(1),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.NaquadahAlloy, 64),
                new Object[] { OrePrefixes.circuit.get(Materials.LuV), 4 },
                ItemList.Circuit_Parts_Crystal_Chip_Master.get(36),
                ItemList.Circuit_Parts_Crystal_Chip_Master.get(36),
                ItemList.Circuit_Chip_HPIC.get(64),
                ItemList.Circuit_Parts_DiodeASMD.get(8),
                ItemList.Circuit_Parts_CapacitorASMD.get(8),
                ItemList.Circuit_Parts_ResistorASMD.get(8),
                ItemList.Circuit_Parts_TransistorASMD.get(8),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Platinum, 64))
            .fluidInputs(new FluidStack(solderIndalloy, 720))
            .itemOutputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 2))
            .duration(50 * SECONDS)
            .eut(80_000)
            .addTo(AssemblyLine);

        // ZPM Capacitor
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 2))
            .metadata(RESEARCH_TIME, 4 * HOURS)
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 4),
                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.NaquadahAlloy, 24),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Europium, 16L),
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 1 },
                ItemList.Energy_LapotronicOrb2.get(8L),
                ItemList.Field_Generator_LuV.get(2),
                ItemList.Circuit_Wafer_SoC2.get(64),
                ItemList.Circuit_Wafer_SoC2.get(64),
                ItemList.Circuit_Parts_DiodeASMD.get(8),
                GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Naquadah, 32))
            .fluidInputs(
                new FluidStack(solderIndalloy, 2880),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 16000))
            .itemOutputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 3))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(100_000)
            .addTo(AssemblyLine);

        // UV Capacitor
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 3))
            .metadata(RESEARCH_TIME, 4 * HOURS)
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 4),
                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Neutronium, 24),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Americium, 32L),
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 1 },
                ItemList.Energy_Module.get(8L),
                ItemList.Field_Generator_ZPM.get(2),
                ItemList.Circuit_Wafer_HPIC.get(64),
                ItemList.Circuit_Wafer_HPIC.get(64),
                ItemList.Circuit_Parts_DiodeASMD.get(16),
                GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.NaquadahAlloy, 32))
            .fluidInputs(
                new FluidStack(solderIndalloy, 2880),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 16000))
            .itemOutputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 4))
            .eut(200_000)
            .duration(1 * MINUTES + 40 * SECONDS)
            .addTo(AssemblyLine);
    }
}
