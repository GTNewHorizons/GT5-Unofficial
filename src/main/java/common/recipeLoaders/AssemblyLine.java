package common.recipeLoaders;

import com.github.bartimaeusnek.bartworks.util.BW_Util;
import com.github.technus.tectech.recipe.TT_recipeAdder;
import common.Blocks;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class AssemblyLine implements Runnable{
    @Override
    public void run() {
        final Fluid radoxPoly = FluidRegistry.getFluid("molten.radoxpoly") != null
            ? FluidRegistry.getFluid("molten.radoxpoly")
            : FluidRegistry.getFluid("molten.polybenzimidazole");

        final Fluid solderIndalloy = FluidRegistry.getFluid("molten.indalloy140") != null
            ? FluidRegistry.getFluid("molten.indalloy140")
            : FluidRegistry.getFluid("molten.solderingalloy");

        final Fluid solderUEV = FluidRegistry.getFluid("molten.mutatedlivingsolder") != null
            ? FluidRegistry.getFluid("molten.mutatedlivingsolder")
            : FluidRegistry.getFluid("molten.solderingalloy");

        // TFFTStorageField6
        GT_Values.RA.addAssemblylineRecipe(
            new ItemStack(Blocks.tfftStorageField, 1, 5),
            40000,
            new ItemStack[] { ItemList.Casing_Tank_7.get(4),
                GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.CrystallinePinkSlime, 6),
                GT_OreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.Naquadah, 3),
                GT_ModHandler.getModItem("dreamcraft", "item.ChromeBars", 6),
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Quantium, 6),
                ItemList.Field_Generator_EV.get(8), ItemList.FluidRegulator_LuV.get(1),
                GT_ModHandler.getModItem("dreamcraft", "item.EngineeringProcessorFluidEmeraldCore", 4) },
            new FluidStack[] { Materials.Enderium.getMolten(1440), Materials.Polybenzimidazole.getMolten(1584) },
            new ItemStack(Blocks.tfftStorageField, 1, 6),
            600,
            BW_Util.getMachineVoltageFromTier(6));

        // TFFTStorageField7
        GT_Values.RA.addAssemblylineRecipe(
            new ItemStack(Blocks.tfftStorageField, 1, 6),
            80000,
            new ItemStack[] { ItemList.Casing_Tank_10.get(16),
                GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.MelodicAlloy, 6),
                GT_OreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.NetherStar, 3),
                GT_ModHandler.getModItem("dreamcraft", "item.OsmiumBars", 6),
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.MysteriousCrystal, 6),
                ItemList.Field_Generator_IV.get(16), ItemList.Field_Generator_LuV.get(4),
                ItemList.FluidRegulator_UV.get(1),
                GT_ModHandler.getModItem("dreamcraft", "item.EngineeringProcessorFluidEmeraldCore", 16) },
            new FluidStack[] { Materials.CrystallineAlloy.getMolten(2880),
                Materials.Polybenzimidazole.getMolten(2016) },
            new ItemStack(Blocks.tfftStorageField, 1, 7),
            600,
            BW_Util.getMachineVoltageFromTier(8));

        // TFFTStorageField8
        GT_Values.RA.addAssemblylineRecipe(
            new ItemStack(Blocks.tfftStorageField, 1, 7),
            120000,
            new ItemStack[] { ItemList.Quantum_Tank_IV.get(1),
                GT_ModHandler.getModItem("Avaritia", "Neutronium_Compressor", 1),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.StellarAlloy, 6),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.StellarAlloy, 6),
                GT_OreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.DraconiumAwakened, 3),
                GT_ModHandler.getModItem("dreamcraft", "item.NeutroniumBars", 6),
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.InfinityCatalyst, 6),
                ItemList.Field_Generator_ZPM.get(16), ItemList.Field_Generator_UV.get(4),
                GT_ModHandler.getModItem("GoodGenerator", "huiCircuit", 4, 2),
                GT_ModHandler
                    .getModItem("universalsingularities", "universal.tinkersConstruct.singularity", 1, 4) },
            new FluidStack[] { Materials.CrystallinePinkSlime.getMolten(4320), new FluidStack(radoxPoly, 2880) },
            new ItemStack(Blocks.tfftStorageField, 1, 8),
            600,
            BW_Util.getMachineVoltageFromTier(10));

        // TFFTStorageField9
        GT_Values.RA.addAssemblylineRecipe(
            new ItemStack(Blocks.tfftStorageField, 1, 8),
            160000,
            new ItemStack[] { ItemList.Quantum_Tank_IV.get(4),
                GT_ModHandler.getModItem("Avaritia", "Neutronium_Compressor", 2),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.TranscendentMetal, 6),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.TranscendentMetal, 6),
                GT_OreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.Infinity, 3),
                ItemList.EnergisedTesseract.get(1),
                GT_ModHandler.getModItem("miscutils", "itemRotorHypogen", 6),
                ItemList.Field_Generator_UHV.get(16), ItemList.Field_Generator_UEV.get(4),
                GT_ModHandler.getModItem("GoodGenerator", "huiCircuit", 4, 3),
                GT_ModHandler
                    .getModItem("universalsingularities", "universal.tinkersConstruct.singularity", 1, 4) },
            new FluidStack[] { Materials.MelodicAlloy.getMolten(5760), new FluidStack(radoxPoly, 3456) },
            new ItemStack(Blocks.tfftStorageField, 1, 9),
            600,
            BW_Util.getMachineVoltageFromTier(12));

        // TFFTStorageField10
        GT_Values.RA.addAssemblylineRecipe(
            new ItemStack(Blocks.tfftStorageField, 1, 9),
            200000,
            new ItemStack[] { ItemList.Quantum_Tank_IV.get(16),
                GT_ModHandler.getModItem("Avaritia", "Neutronium_Compressor", 4),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.SpaceTime, 6),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.SpaceTime, 6),
                GT_OreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.SpaceTime, 3),
                ItemList.EnergisedTesseract.get(6),
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.SpaceTime, 6),
                ItemList.Field_Generator_UEV.get(16), ItemList.Field_Generator_UIV.get(4),
                GT_ModHandler.getModItem("GoodGenerator", "huiCircuit", 4, 4),
                GT_ModHandler.getModItem("GoodGenerator", "huiCircuit", 4, 4),
                GT_ModHandler.getModItem("EnderIO", "itemBasicCapacitor", 64, 6),
                GT_ModHandler.getModItem("eternalsingularity", "eternal_singularity", 1) },
            new FluidStack[] { Materials.StellarAlloy.getMolten(7200), new FluidStack(radoxPoly, 4608) },
            new ItemStack(Blocks.tfftStorageField, 1, 10),
            600,
            BW_Util.getMachineVoltageFromTier(13));

        // LuV Capacitor
        GT_Values.RA.addAssemblylineRecipe(
            new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 1),
            288000,
            new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Osmiridium, 4),
                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Osmiridium, 24),
                ItemList.Circuit_Board_Elite.get(1),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.NaquadahAlloy, 64),
                new Object[] { OrePrefixes.circuit.get(Materials.Master), 4 },
                ItemList.Circuit_Parts_Crystal_Chip_Master.get(36),
                ItemList.Circuit_Parts_Crystal_Chip_Master.get(36), ItemList.Circuit_Chip_HPIC.get(64),
                ItemList.Circuit_Parts_DiodeASMD.get(8), ItemList.Circuit_Parts_CapacitorASMD.get(8),
                ItemList.Circuit_Parts_ResistorASMD.get(8), ItemList.Circuit_Parts_TransistorASMD.get(8),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Platinum, 64) },
            new FluidStack[] { new FluidStack(solderIndalloy, 720) },
            new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 2),
            1000,
            80000);

        // ZPM Capacitor
        GT_Values.RA.addAssemblylineRecipe(
            new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 2),
            288000,
            new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 4),
                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.NaquadahAlloy, 24),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Europium, 16L),
                new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 1 },
                ItemList.Energy_LapotronicOrb2.get(8L), ItemList.Field_Generator_LuV.get(2),
                ItemList.Circuit_Wafer_SoC2.get(64), ItemList.Circuit_Wafer_SoC2.get(64),
                ItemList.Circuit_Parts_DiodeASMD.get(8),
                GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Naquadah, 32) },
            new FluidStack[] { new FluidStack(solderIndalloy, 2880),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 16000) },
            new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 3),
            2000,
            100000);

        // UV Capacitor
        GT_Values.RA.addAssemblylineRecipe(
            new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 3),
            288000,
            new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 4),
                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Neutronium, 24),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Americium, 32L),
                new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 1 },
                ItemList.Energy_Module.get(8L), ItemList.Field_Generator_ZPM.get(2),
                ItemList.Circuit_Wafer_HPIC.get(64), ItemList.Circuit_Wafer_HPIC.get(64),
                ItemList.Circuit_Parts_DiodeASMD.get(16),
                GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.NaquadahAlloy, 32) },
            new FluidStack[] { new FluidStack(solderIndalloy, 2880),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 16000) },
            new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 4),
            2000,
            200000);

        // Ultimate Capacitor (UHV)
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 4),
            12000,
            16,
            300000,
            3,
            new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 4),
                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.CosmicNeutronium, 24),
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Neutronium, 32L),
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Neutronium, 32L),
                new Object[] { OrePrefixes.circuit.get(Materials.Bio), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.Bio), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.Bio), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.Bio), 1 }, ItemList.ZPM2.get(8L),
                ItemList.Field_Generator_UHV.get(4), ItemList.Circuit_Wafer_UHPIC.get(64),
                ItemList.Circuit_Wafer_UHPIC.get(64), ItemList.Circuit_Wafer_SoC2.get(32),
                ItemList.Circuit_Parts_DiodeASMD.get(64),
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUHV, 64) },
            new FluidStack[] { new FluidStack(solderUEV, 4608), Materials.Naquadria.getMolten(9216),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 32000) },
            new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 5),
            4000,
            1600000);
    }
}
