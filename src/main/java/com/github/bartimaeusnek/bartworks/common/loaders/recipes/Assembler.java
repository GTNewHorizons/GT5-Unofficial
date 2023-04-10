package com.github.bartimaeusnek.bartworks.common.loaders.recipes;

import static gregtech.api.enums.Mods.GregTech;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;

import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.bartworks.util.BWRecipes;
import com.github.bartimaeusnek.bartworks.util.BW_Util;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

public class Assembler implements Runnable {

    @Override
    public void run() {
        Materials[] cables = { // Cable material used in the acid gen, diode and energy distributor below
                Materials.Lead, // ULV
                Materials.Tin, // LV
                Materials.AnnealedCopper, // MV
                Materials.Gold, // HV
                Materials.Aluminium, // EV
                Materials.Tungsten, // IV
                Materials.VanadiumGallium, // LuV
                Materials.Naquadah, // ZPM
                Materials.NaquadahAlloy, // UV
                Materials.SuperconductorUV // UHV
        };

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 0), Materials.Lapis.getPlates(9),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 2L),
                        GT_Utility.getIntegratedCircuit(17) },
                FluidRegistry.getFluidStack("ic2coolant", 1000),
                new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 1),
                100,
                BW_Util.getMachineVoltageFromTier(3));
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 1), Materials.Lapis.getBlocks(8),
                        GT_Utility.getIntegratedCircuit(17) },
                GT_Values.NF,
                new ItemStack(ItemRegistry.BW_BLOCKS[1]),
                100,
                BW_Util.getMachineVoltageFromTier(3));

        GT_Values.RA.addAssemblerRecipe(
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 1000),
                GT_Utility.getIntegratedCircuit(17),
                Materials.SolderingAlloy.getMolten(9216),
                ItemRegistry.megaMachines[0],
                72000,
                BW_Util.getMachineVoltageFromTier(3));
        GT_Values.RA.addAssemblerRecipe(
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 1002),
                GT_Utility.getIntegratedCircuit(17),
                Materials.SolderingAlloy.getMolten(9216),
                ItemRegistry.megaMachines[1],
                72000,
                BW_Util.getMachineVoltageFromTier(3));
        GT_Values.RA.addAssemblerRecipe(
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 1126),
                GT_Utility.getIntegratedCircuit(17),
                Materials.SolderingAlloy.getMolten(9216),
                ItemRegistry.megaMachines[2],
                72000,
                BW_Util.getMachineVoltageFromTier(3));
        GT_Values.RA.addAssemblerRecipe(
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 1169),
                GT_Utility.getIntegratedCircuit(17),
                Materials.SolderingAlloy.getMolten(9216),
                ItemRegistry.megaMachines[3],
                72000,
                BW_Util.getMachineVoltageFromTier(3));
        GT_Values.RA.addAssemblerRecipe(
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 1160),
                GT_Utility.getIntegratedCircuit(17),
                Materials.SolderingAlloy.getMolten(9216),
                ItemRegistry.megaMachines[4],
                72000,
                BW_Util.getMachineVoltageFromTier(3));

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.AnnealedCopper, 64L),
                        GT_Utility.getIntegratedCircuit(17) },
                Materials.Plastic.getMolten(1152L),
                new ItemStack(ItemRegistry.BW_BLOCKS[2], 1, 1),
                20,
                BW_Util.getMachineVoltageFromTier(3));

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Good, 1L),
                        Materials.Aluminium.getPlates(1), ItemList.Circuit_Board_Plastic.get(1L),
                        ItemList.Battery_RE_LV_Lithium.get(1L) },
                Materials.SolderingAlloy.getMolten(288L),
                new ItemStack(ItemRegistry.CIRCUIT_PROGRAMMER),
                600,
                BW_Util.getMachineVoltageFromTier(2));

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Circuit_Parts_GlassFiber.get(32),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Electrum, 8),
                        WerkstoffLoader.CubicZirconia.get(OrePrefixes.gemExquisite, 2) },
                Materials.Polytetrafluoroethylene.getMolten(72),
                new ItemStack(
                        ItemRegistry.TecTechPipeEnergyLowPower.getItem(),
                        1,
                        ItemRegistry.TecTechPipeEnergyLowPower.getItemDamage()),
                200,
                BW_Util.getMachineVoltageFromTier(4));

        ItemStack[][] converters = ItemRegistry.TecTechLaserAdditions[0];
        ItemStack[][] input = ItemRegistry.TecTechLaserAdditions[1];
        ItemStack[][] dynamo = ItemRegistry.TecTechLaserAdditions[2];

        ItemList[] emitters = { ItemList.Emitter_EV, ItemList.Emitter_IV, ItemList.Emitter_LuV, ItemList.Emitter_ZPM };

        ItemList[] sensors = { ItemList.Sensor_EV, ItemList.Sensor_IV, ItemList.Sensor_LuV, ItemList.Sensor_ZPM };

        OrePrefixes[] prefixes = { OrePrefixes.cableGt04, OrePrefixes.cableGt08, OrePrefixes.cableGt12,
                OrePrefixes.cableGt16 };

        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                ItemStack converter = converters[j][i];
                ItemStack eInput = input[j][i];
                ItemStack eDynamo = dynamo[j][i];

                int solderingAmount = Math.max(144 * i, 72) * (j + 1);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] {
                                new ItemStack(
                                        ItemRegistry.TecTechPipeEnergyLowPower.getItem(),
                                        ((j + 1) * 16),
                                        ItemRegistry.TecTechPipeEnergyLowPower.getItemDamage()),
                                WerkstoffLoader.CubicZirconia.get(OrePrefixes.lens),
                                GT_OreDictUnificator.get(prefixes[j], cables[i + 4], 8), emitters[i].get(2 * (j + 1)),
                                sensors[i].get(2 * (j + 1)), ItemList.TRANSFORMERS[4 + i].get(2 * (j + 1)), },
                        Materials.SolderingAlloy.getMolten(solderingAmount),
                        converter,
                        200 * (j + 1),
                        BW_Util.getMachineVoltageFromTier(4 + i));
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] {
                                new ItemStack(
                                        ItemRegistry.TecTechPipeEnergyLowPower.getItem(),
                                        ((j + 1) * 16),
                                        ItemRegistry.TecTechPipeEnergyLowPower.getItemDamage()),
                                WerkstoffLoader.CubicZirconia.get(OrePrefixes.lens),
                                GT_OreDictUnificator.get(prefixes[j], cables[i + 4], 8), sensors[i].get(2 * (j + 1)),
                                ItemList.HATCHES_ENERGY[4 + i].get(2 * (j + 1)), },
                        Materials.SolderingAlloy.getMolten(solderingAmount),
                        eInput,
                        200 * (j + 1),
                        BW_Util.getMachineVoltageFromTier(4 + i));
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] {
                                new ItemStack(
                                        ItemRegistry.TecTechPipeEnergyLowPower.getItem(),
                                        ((j + 1) * 16),
                                        ItemRegistry.TecTechPipeEnergyLowPower.getItemDamage()),
                                WerkstoffLoader.CubicZirconia.get(OrePrefixes.lens),
                                GT_OreDictUnificator.get(prefixes[j], cables[i + 4], 8), emitters[i].get(2 * (j + 1)),
                                ItemList.HATCHES_DYNAMO[4 + i].get(2 * (j + 1)), },
                        Materials.SolderingAlloy.getMolten(solderingAmount),
                        eDynamo,
                        200 * (j + 1),
                        BW_Util.getMachineVoltageFromTier(4 + i));
            }
        }

        GT_Recipe.GT_Recipe_Map.sAssemblerRecipes.add(
                new BWRecipes.DynamicGTRecipe(
                        false,
                        new ItemStack[] { ItemList.Hatch_Input_HV.get(64), Materials.LiquidAir.getCells(1),
                                GT_Utility.getIntegratedCircuit(17) },
                        new ItemStack[] { ItemRegistry.compressedHatch.copy() },
                        null,
                        null,
                        null,
                        null,
                        300,
                        BW_Util.getMachineVoltageFromTier(3),
                        0));
        GT_Recipe.GT_Recipe_Map.sAssemblerRecipes.add(
                new BWRecipes.DynamicGTRecipe(
                        false,
                        new ItemStack[] { ItemList.Hatch_Output_HV.get(64), GT_Utility.getIntegratedCircuit(17) },
                        new ItemStack[] { ItemRegistry.giantOutputHatch.copy() },
                        null,
                        null,
                        null,
                        null,
                        300,
                        BW_Util.getMachineVoltageFromTier(3),
                        0));

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Lead, 6),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 1) },
                Materials.Concrete.getMolten(1296),
                new ItemStack(GregTech_API.sBlockCasings3, 1, 12),
                40,
                BW_Util.getMachineVoltageFromTier(5));

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { new ItemStack(GregTech_API.sBlockCasings3, 1, 12),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Europium, 6),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Europium, 24) },
                Materials.Lead.getMolten(864),
                new ItemStack(GregTech_API.sBlockCasings8, 1, 5),
                200,
                BW_Util.getMachineVoltageFromTier(6));
    }
}
