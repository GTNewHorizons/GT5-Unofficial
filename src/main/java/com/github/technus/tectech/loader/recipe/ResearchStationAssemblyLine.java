package com.github.technus.tectech.loader.recipe;

import static com.github.technus.tectech.loader.recipe.BaseRecipeLoader.getItemContainer;
import static com.google.common.math.LongMath.pow;
import static gregtech.api.enums.Mods.Avaritia;
import static gregtech.api.enums.Mods.AvaritiaAddons;
import static gregtech.api.enums.Mods.BartWorks;
import static gregtech.api.enums.Mods.BloodMagic;
import static gregtech.api.enums.Mods.DraconicEvolution;
import static gregtech.api.enums.Mods.ElectroMagicTools;
import static gregtech.api.enums.Mods.EternalSingularity;
import static gregtech.api.enums.Mods.ExtraUtilities;
import static gregtech.api.enums.Mods.GTNHIntergalactic;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.GTPlusPlusEverglades;
import static gregtech.api.enums.Mods.GalaxySpace;
import static gregtech.api.enums.Mods.GoodGenerator;
import static gregtech.api.enums.Mods.GraviSuite;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.enums.Mods.KekzTech;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.SGCraft;
import static gregtech.api.enums.Mods.SuperSolarPanels;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.enums.Mods.TinkerConstruct;
import static gregtech.api.enums.Mods.TinkersGregworks;
import static gregtech.api.util.GT_ModHandler.getModItem;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.github.technus.tectech.recipe.TT_recipeAdder;
import com.github.technus.tectech.thing.CustomItemList;

import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import vexatos.tgregworks.reference.PartTypes;
import vexatos.tgregworks.util.TGregUtils;

public class ResearchStationAssemblyLine implements Runnable {

    @Override
    public void run() {
        itemPartsUHVAsslineRecipes();
        itemPartsUEVAsslineRecipes();
        itemPartsUIVAsslineRecipes();
        itemPartsUMVAsslineRecipes();
        itemPartsUXVAsslineRecipes();

        addWirelessEnergyRecipes();

        if (TinkersGregworks.isModLoaded()) {
            addEOHRecipes();
        }

        Fluid solderUEV = FluidRegistry.getFluid("molten.mutatedlivingsolder") != null
                ? FluidRegistry.getFluid("molten.mutatedlivingsolder")
                : FluidRegistry.getFluid("molten.solderingalloy");

        Fluid solderIndalloy = FluidRegistry.getFluid("molten.indalloy140") != null
                ? FluidRegistry.getFluid("molten.indalloy140")
                : FluidRegistry.getFluid("molten.solderingalloy");

        // Infinite Oil Rig
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.OilDrill4.get(1),
                16777216,
                2048,
                2000000,
                4,
                new Object[] { ItemList.OilDrill4.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 4),
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 4L },
                        ItemList.Electric_Motor_UHV.get(4), ItemList.Electric_Pump_UHV.get(4),
                        GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Neutronium, 4),
                        ItemList.Sensor_UHV.get(3), ItemList.Field_Generator_UHV.get(3),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Neutronium, 12) },
                new FluidStack[] { new FluidStack(solderUEV, 1296), Materials.Neutronium.getMolten(576) },
                ItemList.OilDrillInfinite.get(1),
                6000,
                2000000);

        // Infinity Coil
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Casing_Coil_AwakenedDraconium.get(1),
                16_777_216,
                2048,
                8_000_000,
                1,
                new Object[] { new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 1L },
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Infinity, 8),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Infinity, 8),
                        getModItem(NewHorizonsCoreMod.ID, "item.MicaInsulatorFoil", 64, 0),
                        getModItem(NewHorizonsCoreMod.ID, "item.MicaInsulatorFoil", 32, 0) },
                new FluidStack[] { Materials.DraconiumAwakened.getMolten(576), },
                ItemList.Casing_Coil_Infinity.get(1),
                60 * 20,
                8_000_000);

        if (GTPlusPlus.isModLoaded()) {
            // Hypogen Coil
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Casing_Coil_Infinity.get(1),
                    16_777_216 * 2,
                    2048 * 2,
                    32_000_000,
                    1,
                    new Object[] { new Object[] { OrePrefixes.circuit.get(Materials.Bio), 1L },
                            GT_OreDictUnificator.get("wireGt02Hypogen", 8L),
                            getModItem(GTPlusPlus.ID, "itemScrewHypogen", 8, 0),
                            getModItem(NewHorizonsCoreMod.ID, "item.MicaInsulatorFoil", 64, 0),
                            getModItem(NewHorizonsCoreMod.ID, "item.MicaInsulatorFoil", 64, 0),
                            getModItem(NewHorizonsCoreMod.ID, "item.MicaInsulatorFoil", 64, 0), },
                    new FluidStack[] { Materials.Infinity.getMolten(576), },
                    ItemList.Casing_Coil_Hypogen.get(1),
                    60 * 20,
                    8000000 * 4);

            // Eternal coil
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Casing_Coil_Hypogen.get(1),
                    16_777_216 * 4,
                    8_192,
                    128_000_000,
                    1,
                    new Object[] { new Object[] { OrePrefixes.circuit.get(Materials.Optical), 1L },
                            GT_OreDictUnificator.get(OrePrefixes.wireGt02, MaterialsUEVplus.SpaceTime, 8),
                            GT_OreDictUnificator.get(OrePrefixes.screw, MaterialsUEVplus.SpaceTime, 8),
                            getModItem(EternalSingularity.ID, "eternal_singularity", 1L),
                            getModItem(NewHorizonsCoreMod.ID, "item.MicaInsulatorFoil", 64, 0),
                            getModItem(NewHorizonsCoreMod.ID, "item.MicaInsulatorFoil", 64, 0),
                            getModItem(NewHorizonsCoreMod.ID, "item.MicaInsulatorFoil", 64, 0),
                            getModItem(NewHorizonsCoreMod.ID, "item.MicaInsulatorFoil", 64, 0),
                            getModItem(NewHorizonsCoreMod.ID, "item.MicaInsulatorFoil", 64, 0),
                            getModItem(NewHorizonsCoreMod.ID, "item.MicaInsulatorFoil", 64, 0), },
                    new FluidStack[] { new FluidStack(FluidRegistry.getFluid("molten.hypogen"), 576), },
                    ItemList.Casing_Coil_Eternal.get(1),
                    60 * 20,
                    8_000_000 * 16);
        }

        // Matter Junction
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                CustomItemList.Machine_Multi_Switch.get(1),
                8000,
                32,
                500000,
                4,
                new Object[] { CustomItemList.Machine_Multi_Transformer.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Naquadah, 4),
                        ItemList.Robot_Arm_LuV.get(2), ItemList.Electric_Piston_LuV.get(2),
                        new Object[] { "circuitSuperconductor", 2 },
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUHV, 4), },
                new FluidStack[] { Materials.UUMatter.getFluid(1000), Materials.Naquadah.getMolten(1296),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000), Materials.Osmium.getMolten(1296), },
                CustomItemList.Machine_Multi_EMjunction.get(1),
                12000,
                100000);

        // Matter Quantizer
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Hatch_Input_UV.get(1),
                12000,
                32,
                500000,
                6,
                new Object[] { CustomItemList.Machine_Multi_Transformer.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Naquadah, 4),
                        ItemList.Emitter_UV.get(2), new Object[] { "circuitSuperconductor", 1 },
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUHV, 2), },
                new FluidStack[] { Materials.UUMatter.getFluid(1000), Materials.Naquadah.getMolten(1296),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000), Materials.Osmium.getMolten(1296), },
                CustomItemList.Machine_Multi_MatterToEM.get(1),
                12000,
                100000);

        // Matter DeQuantizer
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Hatch_Output_UV.get(1),
                12000,
                32,
                500000,
                6,
                new Object[] { CustomItemList.Machine_Multi_Transformer.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Naquadah, 4),
                        ItemList.Sensor_UV.get(2), new Object[] { "circuitSuperconductor", 1 },
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUHV, 2), },
                new FluidStack[] { Materials.UUMatter.getFluid(1000), Materials.Naquadah.getMolten(1296),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000), Materials.Osmium.getMolten(1296), },
                CustomItemList.Machine_Multi_EMToMatter.get(1),
                12000,
                100000);

        // Essentia Quantizer
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                CustomItemList.Machine_Multi_MatterToEM.get(1),
                15000,
                32,
                500000,
                8,
                new Object[] { CustomItemList.Machine_Multi_MatterToEM.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Neutronium, 4),
                        ItemList.Emitter_UV.get(2), new Object[] { "circuitSuperconductor", 1 },
                        GT_OreDictUnificator.get(OrePrefixes.cableGt02, Materials.Draconium, 2), },
                new FluidStack[] { Materials.UUMatter.getFluid(2000), Materials.Void.getMolten(2592),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 4000), Materials.Osmium.getMolten(1296), },
                CustomItemList.Machine_Multi_EssentiaToEM.get(1),
                24000,
                500000);

        // Essentia DeQuantizer
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                CustomItemList.Machine_Multi_EMToMatter.get(1),
                15000,
                32,
                500000,
                8,
                new Object[] { CustomItemList.Machine_Multi_EMToMatter.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Neutronium, 4),
                        ItemList.Sensor_UV.get(2), new Object[] { "circuitSuperconductor", 1 },
                        GT_OreDictUnificator.get(OrePrefixes.cableGt02, Materials.Draconium, 2), },
                new FluidStack[] { Materials.UUMatter.getFluid(2000), Materials.Void.getMolten(2592),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 4000), Materials.Osmium.getMolten(1296), },
                CustomItemList.Machine_Multi_EMToEssentia.get(1),
                24000,
                500000);

        // EM Scanner
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                CustomItemList.Machine_Multi_Research.get(1),
                150000,
                128,
                500000,
                16,
                new Object[] { CustomItemList.Machine_Multi_EMjunction.get(1), CustomItemList.eM_Computer_Bus.get(4),
                        ItemList.Field_Generator_UV.get(4), ItemList.Sensor_UV.get(4),
                        new Object[] { OrePrefixes.circuit.get(Materials.Optical), 4L },
                        getItemContainer("MysteriousCrystalLens").get(4),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt02, Materials.Draconium, 4), },
                new FluidStack[] { Materials.UUMatter.getFluid(2000), Materials.Neutronium.getMolten(2592),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 4000),
                        Materials.Osmiridium.getMolten(1296), },
                CustomItemList.Machine_Multi_Scanner.get(1),
                24000,
                500000);

        // UHV-UMV Energy Hatch & Dynamo
        {
            // Energy Hatches
            {
                // Energy Hatch UHV
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                        ItemList.Hatch_Energy_UV.get(1L),
                        24000,
                        16,
                        50000,
                        2,
                        new Object[] { ItemList.Hull_MAX.get(1L),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 2L),
                                ItemList.Circuit_Chip_QPIC.get(2L),
                                new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 2L },
                                ItemList.UHV_Coil.get(2L),
                                new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1L),
                                        ItemList.Reactor_Coolant_NaK_6.get(1L), ItemList.Reactor_Coolant_Sp_2.get(1L) },
                                new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1L),
                                        ItemList.Reactor_Coolant_NaK_6.get(1L), ItemList.Reactor_Coolant_Sp_2.get(1L) },
                                new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1L),
                                        ItemList.Reactor_Coolant_NaK_6.get(1L), ItemList.Reactor_Coolant_Sp_2.get(1L) },
                                new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1L),
                                        ItemList.Reactor_Coolant_NaK_6.get(1L), ItemList.Reactor_Coolant_Sp_2.get(1L) },
                                new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1L),
                                        ItemList.Reactor_Coolant_NaK_6.get(1L), ItemList.Reactor_Coolant_Sp_2.get(1L) },
                                new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1L),
                                        ItemList.Reactor_Coolant_NaK_6.get(1L), ItemList.Reactor_Coolant_Sp_2.get(1L) },
                                new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1L),
                                        ItemList.Reactor_Coolant_NaK_6.get(1L), ItemList.Reactor_Coolant_Sp_2.get(1L) },
                                new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1L),
                                        ItemList.Reactor_Coolant_NaK_6.get(1L), ItemList.Reactor_Coolant_Sp_2.get(1L) },
                                ItemList.Electric_Pump_UHV.get(1L) },
                        new FluidStack[] { new FluidStack(FluidRegistry.getFluid("ic2coolant"), 16000),
                                new FluidStack(solderIndalloy, 40 * 144) },
                        ItemList.Hatch_Energy_MAX.get(1L),
                        1000,
                        2000000);

                // Energy Hatch UEV
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                        ItemList.Hatch_Energy_MAX.get(1L),
                        48000,
                        32,
                        100000,
                        4,
                        new Object[] { getItemContainer("Hull_UEV").get(1L),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUEV, 2L),
                                ItemList.Circuit_Chip_QPIC.get(4L),
                                new Object[] { OrePrefixes.circuit.get(Materials.Bio), 2L }, ItemList.UHV_Coil.get(4L),
                                ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                                ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Electric_Pump_UEV.get(1L) },
                        new FluidStack[] { new FluidStack(FluidRegistry.getFluid("ic2coolant"), 32000),
                                new FluidStack(solderUEV, 20 * 144), Materials.UUMatter.getFluid(8000L) },
                        getItemContainer("Hatch_Energy_UEV").get(1L),
                        1000,
                        8000000);

                // Energy Hatch UIV
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                        getItemContainer("Hatch_Energy_UEV").get(1L),
                        96_000,
                        64,
                        200_000,
                        8,
                        new Object[] { getItemContainer("Hull_UIV").get(1L),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUIV, 2L),
                                ItemList.Circuit_Chip_QPIC.get(4L),
                                new Object[] { OrePrefixes.circuit.get(Materials.Optical), 2L },
                                ItemList.UHV_Coil.get(8L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                                ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                                ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                                ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Electric_Pump_UIV.get(1L) },
                        new FluidStack[] { Materials.SuperCoolant.getFluid(16_000L),
                                new FluidStack(solderUEV, 20 * 144), Materials.UUMatter.getFluid(16_000L) },
                        getItemContainer("Hatch_Energy_UIV").get(1L),
                        1000,
                        32_000_000);

                // Energy Hatch UMV
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                        getItemContainer("Hatch_Energy_UIV").get(1L),
                        192000,
                        128,
                        400000,
                        16,
                        new Object[] { getItemContainer("Hull_UMV").get(1L),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUMV, 2L),
                                ItemList.Circuit_Chip_QPIC.get(4L), getItemContainer("PikoCircuit").get(2),
                                ItemList.UHV_Coil.get(16L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                                ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                                ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                                ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                                ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                                ItemList.Electric_Pump_UMV.get(1L) },
                        new FluidStack[] { Materials.SuperCoolant.getFluid(32_000L),
                                new FluidStack(solderUEV, 40 * 144), Materials.UUMatter.getFluid(32000L) },
                        getItemContainer("Hatch_Energy_UMV").get(1L),
                        1000,
                        128_000_000);

                // Energy Hatch UXV
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                        getItemContainer("Hatch_Energy_UMV").get(1L),
                        384000,
                        256,
                        800000,
                        32,
                        new Object[] { getItemContainer("Hull_UXV").get(1L),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.SuperconductorUMV, 4L),
                                ItemList.Circuit_Chip_QPIC.get(16L), getItemContainer("QuantumCircuit").get(2),
                                ItemList.UHV_Coil.get(32L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                                ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                                ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                                ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                                ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                                ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Electric_Pump_UXV.get(1L) },
                        new FluidStack[] { Materials.SuperCoolant.getFluid(64_000L),
                                new FluidStack(solderUEV, 80 * 144), Materials.UUMatter.getFluid(64000L) },
                        getItemContainer("Hatch_Energy_UXV").get(1L),
                        1000,
                        512_000_000);
            }

            // Dynamo Hatch
            {
                // Dynamo Hatch UHV
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                        ItemList.Hatch_Dynamo_UV.get(1L),
                        48000,
                        32,
                        100000,
                        4,
                        new Object[] { ItemList.Hull_MAX.get(1L),
                                GT_OreDictUnificator
                                        .get(OrePrefixes.spring, Materials.Longasssuperconductornameforuhvwire, 8L),
                                ItemList.Circuit_Chip_QPIC.get(2L),
                                new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 2L },
                                ItemList.UHV_Coil.get(2L),
                                new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1L),
                                        ItemList.Reactor_Coolant_NaK_6.get(1L), ItemList.Reactor_Coolant_Sp_2.get(1L) },
                                new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1L),
                                        ItemList.Reactor_Coolant_NaK_6.get(1L), ItemList.Reactor_Coolant_Sp_2.get(1L) },
                                new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1L),
                                        ItemList.Reactor_Coolant_NaK_6.get(1L), ItemList.Reactor_Coolant_Sp_2.get(1L) },
                                new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1L),
                                        ItemList.Reactor_Coolant_NaK_6.get(1L), ItemList.Reactor_Coolant_Sp_2.get(1L) },
                                new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1L),
                                        ItemList.Reactor_Coolant_NaK_6.get(1L), ItemList.Reactor_Coolant_Sp_2.get(1L) },
                                new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1L),
                                        ItemList.Reactor_Coolant_NaK_6.get(1L), ItemList.Reactor_Coolant_Sp_2.get(1L) },
                                new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1L),
                                        ItemList.Reactor_Coolant_NaK_6.get(1L), ItemList.Reactor_Coolant_Sp_2.get(1L) },
                                new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1L),
                                        ItemList.Reactor_Coolant_NaK_6.get(1L), ItemList.Reactor_Coolant_Sp_2.get(1L) },
                                ItemList.Electric_Pump_UHV.get(1L) },
                        new FluidStack[] { new FluidStack(FluidRegistry.getFluid("ic2coolant"), 16000),
                                new FluidStack(solderIndalloy, 40 * 144) },
                        ItemList.Hatch_Dynamo_MAX.get(1L),
                        1000,
                        2000000);

                // Dynamo Hatch UEV
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                        ItemList.Hatch_Dynamo_MAX.get(1L),
                        96000,
                        64,
                        200000,
                        8,
                        new Object[] { getItemContainer("Hull_UEV").get(1L),
                                GT_OreDictUnificator.get(OrePrefixes.spring, Materials.SuperconductorUEVBase, 8L),
                                ItemList.Circuit_Chip_QPIC.get(4L),
                                new Object[] { OrePrefixes.circuit.get(Materials.Bio), 2L }, ItemList.UHV_Coil.get(4L),
                                ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                                ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Electric_Pump_UEV.get(1L) },
                        new FluidStack[] { new FluidStack(FluidRegistry.getFluid("ic2coolant"), 32000),
                                new FluidStack(solderUEV, 20 * 144), Materials.UUMatter.getFluid(8000L) },
                        getItemContainer("Hatch_Dynamo_UEV").get(1L),
                        1000,
                        8000000);

                // Dynamo Hatch UIV
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                        getItemContainer("Hatch_Dynamo_UEV").get(1L),
                        192_000,
                        128,
                        400_000,
                        16,
                        new Object[] { getItemContainer("Hull_UIV").get(1L),
                                GT_OreDictUnificator.get(OrePrefixes.spring, Materials.SuperconductorUIVBase, 8L),
                                ItemList.Circuit_Chip_QPIC.get(4L),
                                new Object[] { OrePrefixes.circuit.get(Materials.Optical), 2L },
                                ItemList.UHV_Coil.get(8L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                                ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                                ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                                ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Electric_Pump_UIV.get(1L) },
                        new FluidStack[] { Materials.SuperCoolant.getFluid(16_000L),
                                new FluidStack(solderUEV, 20 * 144), Materials.UUMatter.getFluid(16_000L) },
                        getItemContainer("Hatch_Dynamo_UIV").get(1L),
                        1000,
                        32_000_000);

                // Dynamo Hatch UMV
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                        getItemContainer("Hatch_Dynamo_UIV").get(1L),
                        384000,
                        256,
                        800000,
                        32,
                        new Object[] { getItemContainer("Hull_UMV").get(1L),
                                GT_OreDictUnificator.get(OrePrefixes.spring, Materials.SuperconductorUMVBase, 8L),
                                ItemList.Circuit_Chip_QPIC.get(4L), getItemContainer("PikoCircuit").get(2),
                                ItemList.UHV_Coil.get(16L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                                ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                                ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                                ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                                ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                                ItemList.Electric_Pump_UMV.get(1L) },
                        new FluidStack[] { Materials.SuperCoolant.getFluid(32_000L),
                                new FluidStack(solderUEV, 40 * 144), Materials.UUMatter.getFluid(32000L) },
                        getItemContainer("Hatch_Dynamo_UMV").get(1L),
                        1000,
                        128_000_000);

                // Dynamo Hatch UXV
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                        getItemContainer("Hatch_Dynamo_UMV").get(1L),
                        384000,
                        256,
                        800000,
                        32,
                        new Object[] { getItemContainer("Hull_UXV").get(1L),
                                GT_OreDictUnificator.get(OrePrefixes.spring, Materials.SuperconductorUMVBase, 16L),
                                ItemList.Circuit_Chip_QPIC.get(16L), getItemContainer("QuantumCircuit").get(2),
                                ItemList.UHV_Coil.get(32L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                                ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                                ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                                ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                                ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                                ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Electric_Pump_UXV.get(1L) },
                        new FluidStack[] { Materials.SuperCoolant.getFluid(64_000L),
                                new FluidStack(solderUEV, 80 * 144), Materials.UUMatter.getFluid(64000L) },
                        getItemContainer("Hatch_Dynamo_UXV").get(1L),
                        1000,
                        512_000_000);
            }
        }

        // UHV Circuit Wetwaremainframe
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Circuit_Wetwaresupercomputer.get(1L),
                24000,
                64,
                50000,
                4,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Tritanium, 2),
                        ItemList.Circuit_Wetwaresupercomputer.get(2L),
                        new ItemStack[] { ItemList.Circuit_Parts_InductorASMD.get(16L),
                                ItemList.Circuit_Parts_InductorXSMD.get(4L) },
                        new ItemStack[] { ItemList.Circuit_Parts_CapacitorASMD.get(16L),
                                ItemList.Circuit_Parts_CapacitorXSMD.get(4L) },
                        new ItemStack[] { ItemList.Circuit_Parts_ResistorASMD.get(16L),
                                ItemList.Circuit_Parts_ResistorXSMD.get(4L) },
                        new ItemStack[] { ItemList.Circuit_Parts_TransistorASMD.get(16L),
                                ItemList.Circuit_Parts_TransistorXSMD.get(4L) },
                        new ItemStack[] { ItemList.Circuit_Parts_DiodeASMD.get(16L),
                                ItemList.Circuit_Parts_DiodeXSMD.get(4L) },
                        ItemList.Circuit_Chip_Ram.get(48L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 64L),
                        new Object[] { OrePrefixes.foil.get(Materials.AnySyntheticRubber), 64L }, },
                new FluidStack[] { new FluidStack(solderIndalloy, 2880),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 10000), Materials.Radon.getGas(2500L), },
                ItemList.Circuit_Wetwaremainframe.get(1L),
                2000,
                300000);

        // Bioware SuperComputer
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Circuit_Biowarecomputer.get(1L),
                48000,
                128,
                500000,
                8,
                new Object[] { ItemList.Circuit_Board_Bio_Ultra.get(2L), ItemList.Circuit_Biowarecomputer.get(2L),
                        new ItemStack[] { ItemList.Circuit_Parts_TransistorASMD.get(16L),
                                ItemList.Circuit_Parts_TransistorXSMD.get(4L) },
                        new ItemStack[] { ItemList.Circuit_Parts_ResistorASMD.get(16L),
                                ItemList.Circuit_Parts_ResistorXSMD.get(4L) },
                        new ItemStack[] { ItemList.Circuit_Parts_CapacitorASMD.get(16L),
                                ItemList.Circuit_Parts_CapacitorXSMD.get(4L) },
                        new ItemStack[] { ItemList.Circuit_Parts_DiodeASMD.get(16L),
                                ItemList.Circuit_Parts_DiodeXSMD.get(4L) },
                        ItemList.Circuit_Chip_NOR.get(32L), ItemList.Circuit_Chip_Ram.get(64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.NiobiumTitanium, 32L),
                        new Object[] { OrePrefixes.foil.get(Materials.AnySyntheticRubber), 64L }, },
                new FluidStack[] { new FluidStack(solderUEV, 1440), Materials.BioMediumSterilized.getFluid(1440L),
                        Materials.SuperCoolant.getFluid(10_000L), },
                ItemList.Circuit_Biowaresupercomputer.get(1L),
                4000,
                500000);

        // Bio Mainframe
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Circuit_Biowaresupercomputer.get(1L),
                96000,
                256,
                1000000,
                16,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Tritanium, 4L),
                        ItemList.Circuit_Biowaresupercomputer.get(2L),
                        new ItemStack[] { ItemList.Circuit_Parts_InductorASMD.get(24L),
                                ItemList.Circuit_Parts_InductorXSMD.get(6L) },
                        new ItemStack[] { ItemList.Circuit_Parts_TransistorASMD.get(24L),
                                ItemList.Circuit_Parts_TransistorXSMD.get(6L) },
                        new ItemStack[] { ItemList.Circuit_Parts_ResistorASMD.get(24L),
                                ItemList.Circuit_Parts_ResistorXSMD.get(6L) },
                        new ItemStack[] { ItemList.Circuit_Parts_CapacitorASMD.get(24L),
                                ItemList.Circuit_Parts_CapacitorXSMD.get(6L) },
                        new ItemStack[] { ItemList.Circuit_Parts_DiodeASMD.get(24L),
                                ItemList.Circuit_Parts_DiodeXSMD.get(6L) },
                        ItemList.Circuit_Chip_Ram.get(64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 64),
                        new Object[] { OrePrefixes.foil.get(Materials.AnySyntheticRubber), 64L },
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole, 64) },
                new FluidStack[] { new FluidStack(solderUEV, 2880), Materials.BioMediumSterilized.getFluid(2880L),
                        Materials.SuperCoolant.getFluid(20_000L), },
                ItemList.Circuit_Biomainframe.get(1L),
                6000,
                2000000);

        // Optical Assembly
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Circuit_OpticalProcessor.get(1L),
                192_000,
                256,
                2_000_000,
                16,
                new Object[] { ItemList.Circuit_Board_Optical.get(1L), ItemList.Circuit_OpticalProcessor.get(2L),
                        ItemList.Circuit_Parts_InductorXSMD.get(16L), ItemList.Circuit_Parts_CapacitorXSMD.get(20L),
                        ItemList.Circuit_Parts_ResistorXSMD.get(20L), ItemList.Circuit_Chip_NOR.get(32L),
                        ItemList.Circuit_Chip_Ram.get(64L),
                        GT_ModHandler.getModItem(BartWorks.ID, "gt.bwMetaGeneratedwireFine", 24L, 10101), // Fine
                        // Lumiium
                        // Wire
                        new Object[] { OrePrefixes.foil.get(Materials.AnySyntheticRubber), 64L }, },
                new FluidStack[] { new FluidStack(solderUEV, 1440), Materials.Radon.getPlasma(1440L),
                        Materials.SuperCoolant.getFluid(10_000L),
                        new FluidStack(FluidRegistry.getFluid("oganesson"), 500) },
                ItemList.Circuit_OpticalAssembly.get(1L),
                20 * 20,
                2_000_000);

        // Optical Computer
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Circuit_OpticalAssembly.get(1L),
                384_000,
                512,
                4_000_000,
                32,
                new Object[] { ItemList.Circuit_Board_Optical.get(2L), ItemList.Circuit_OpticalAssembly.get(2L),
                        ItemList.Circuit_Parts_TransistorXSMD.get(24L), ItemList.Circuit_Parts_ResistorXSMD.get(24L),
                        ItemList.Circuit_Parts_CapacitorXSMD.get(24L), ItemList.Circuit_Parts_DiodeXSMD.get(24L),
                        ItemList.Circuit_Chip_NOR.get(64L), ItemList.Circuit_Chip_SoC2.get(32L),
                        GT_ModHandler.getModItem(BartWorks.ID, "gt.bwMetaGeneratedwireFine", 32L, 10101), // Fine
                        // Lumiium
                        // Wire
                        new Object[] { OrePrefixes.foil.get(Materials.AnySyntheticRubber), 64L },
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole, 64), },
                new FluidStack[] { new FluidStack(solderUEV, 1440 * 2), Materials.Radon.getPlasma(1440L * 2),
                        Materials.SuperCoolant.getFluid(10_000L * 2),
                        new FluidStack(FluidRegistry.getFluid("oganesson"), 500 * 2) },
                ItemList.Circuit_OpticalComputer.get(1L),
                200 * 20,
                2_000_000);

        // Optical Mainframe
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Circuit_OpticalComputer.get(1L),
                768_000,
                1024,
                8_000_000,
                64,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Tritanium, 8),
                        ItemList.Circuit_OpticalComputer.get(2L), ItemList.Circuit_Parts_InductorXSMD.get(32L),
                        ItemList.Circuit_Parts_TransistorXSMD.get(32L), ItemList.Circuit_Parts_ResistorXSMD.get(32L),
                        ItemList.Circuit_Parts_CapacitorXSMD.get(32L), ItemList.Circuit_Parts_DiodeXSMD.get(32L),
                        ItemList.Circuit_Chip_SoC2.get(64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 64),
                        new Object[] { OrePrefixes.foil.get(Materials.AnySyntheticRubber), 64L },
                        new Object[] { OrePrefixes.foil.get(Materials.AnySyntheticRubber), 64L },
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole, 64),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole, 64) },
                new FluidStack[] { new FluidStack(solderUEV, 1440 * 4), Materials.Radon.getPlasma(1440L * 4),
                        Materials.SuperCoolant.getFluid(10_000L * 4),
                        new FluidStack(FluidRegistry.getFluid("oganesson"), 500 * 4) },
                ItemList.Circuit_OpticalMainframe.get(1L),
                300 * 20,
                8_000_000);

        // Piko Circuit
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Circuit_OpticalMainframe.get(1L),
                384000,
                1024,
                4000000,
                64,
                new Object[] { ItemList.Circuit_Board_Optical.get(1L), getItemContainer("PicoWafer").get(4L),
                        new Object[] { OrePrefixes.circuit.get(Materials.Optical), 2L },
                        ItemList.Circuit_Parts_TransistorXSMD.get(48L), ItemList.Circuit_Parts_ResistorXSMD.get(48L),
                        ItemList.Circuit_Parts_CapacitorXSMD.get(48L), ItemList.Circuit_Parts_DiodeXSMD.get(48L),
                        ItemList.Circuit_Chip_PPIC.get(64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.NiobiumTitanium, 16),
                        GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Osmium, 32),
                        GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Neutronium, 16),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Lanthanum, 64) },
                new FluidStack[] { new FluidStack(solderUEV, 3744), Materials.UUMatter.getFluid(8000L),
                        Materials.Osmium.getMolten(1152L) },
                getItemContainer("PikoCircuit").get(1L),
                10000,
                128_000_000);

        // Quantum Circuit
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                getItemContainer("PikoCircuit").get(1L),
                720000,
                2048,
                8000000,
                128,
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 16),
                        getItemContainer("PikoCircuit").get(2L), ItemList.Circuit_Parts_CapacitorXSMD.get(64L),
                        ItemList.Circuit_Parts_DiodeXSMD.get(64L), ItemList.Circuit_Parts_TransistorXSMD.get(64L),
                        ItemList.Circuit_Parts_ResistorXSMD.get(64L), ItemList.Circuit_Chip_QPIC.get(64L),
                        GT_OreDictUnificator.get("foilShirabon", 64),
                        GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Indium, 64),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, MaterialsUEVplus.SpaceTime, 8),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Lanthanum, 16) },
                new FluidStack[] { new FluidStack(solderUEV, 3744), Materials.UUMatter.getFluid(24000L),
                        Materials.Osmium.getMolten(2304L) },
                getItemContainer("QuantumCircuit").get(1L),
                20000,
                512_000_000);

        // Transcendent Plasma Mixer - TPM.
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                CustomItemList.eM_energyTunnel7_UV.get(1),
                32_000_000,
                4096,
                32_000_000,
                1,
                new Object[] { CustomItemList.eM_energyTunnel7_UV.get(32),
                        new Object[] { OrePrefixes.circuit.get(Materials.Piko), 32L },
                        ItemList.Electric_Pump_UIV.get(16),
                        GT_OreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.TranscendentMetal, 64),

                        GT_OreDictUnificator.get(OrePrefixes.gearGt, MaterialsUEVplus.TranscendentMetal, 16),
                        GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, MaterialsUEVplus.TranscendentMetal, 64),
                        GT_OreDictUnificator.get(OrePrefixes.screw, MaterialsUEVplus.TranscendentMetal, 64),
                        ItemList.EnergisedTesseract.get(32),

                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L), },
                new FluidStack[] { MaterialsUEVplus.ExcitedDTCC.getFluid(2_048_000),
                        MaterialsUEVplus.ExcitedDTPC.getFluid(2_048_000),
                        MaterialsUEVplus.ExcitedDTRC.getFluid(2_048_000),
                        MaterialsUEVplus.ExcitedDTEC.getFluid(2_048_000), },
                ItemList.Machine_Multi_TranscendentPlasmaMixer.get(1),
                36_000,
                32_000_000);

        // Stargate Recipes
        if (EternalSingularity.isModLoaded() && SGCraft.isModLoaded()) {

            final int baseStargateTime = 125_000 * 20;

            // Stargate shield foil
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    GT_OreDictUnificator
                            .get(OrePrefixes.plate, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 1L),
                    (int) TierEU.RECIPE_MAX,
                    32768,
                    (int) TierEU.RECIPE_UXV,
                    64,
                    new ItemStack[] { ItemList.Casing_Dim_Bridge.get(64),
                            CustomItemList.StabilisationFieldGeneratorTier8.get(64),
                            GT_OreDictUnificator.get("blockShirabon", 64L),
                            GT_OreDictUnificator.get("blockShirabon", 64L),
                            GT_OreDictUnificator.get(OrePrefixes.block, MaterialsUEVplus.SpaceTime, 64L),
                            GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Quantum, 16L),
                            GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.SpaceTime, 8L),
                            GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.Universium, 8L),
                            GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Infinity, 8L),
                            GT_OreDictUnificator.get("plateDenseShirabon", 8L), ItemList.Sensor_UXV.get(16L),
                            ItemList.Emitter_UXV.get(16L),
                            getModItem(EternalSingularity.ID, "eternal_singularity", 16L),
                            MaterialsUEVplus.Universium.getNanite(16), MaterialsUEVplus.BlackDwarfMatter.getNanite(16),
                            MaterialsUEVplus.WhiteDwarfMatter.getNanite(16) },
                    new FluidStack[] { Materials.Neutronium.getMolten(32_768_000L),
                            MaterialsUEVplus.SpaceTime.getMolten(4 * 36864L),
                            Materials.SuperconductorUMVBase.getMolten(4 * 36864L),
                            MaterialsUEVplus.ExcitedDTEC.getFluid(4 * 36864L) },
                    getItemContainer("StargateShieldingFoil").get(1L),
                    baseStargateTime,
                    (int) TierEU.RECIPE_UMV);

            // Stargate chevron
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    getItemContainer("StargateShieldingFoil").get(1L),
                    (int) TierEU.RECIPE_MAX,
                    32_768,
                    (int) TierEU.RECIPE_UXV,
                    64,
                    new ItemStack[] {
                            GT_OreDictUnificator.get(OrePrefixes.block, MaterialsUEVplus.TranscendentMetal, 64L),
                            GT_OreDictUnificator.get("blockShirabon", 64),
                            CustomItemList.EOH_Reinforced_Spatial_Casing.get(64),
                            CustomItemList.EOH_Reinforced_Spatial_Casing.get(64),

                            GT_OreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.SpaceTime, 16L),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.frameGt,
                                    MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
                                    16L),
                            GT_OreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Ruby, 16L),
                            GT_OreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Jasper, 16L),

                            GT_OreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Opal, 16L),
                            GT_OreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Sapphire, 16L),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.plateDense,
                                    MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
                                    8L),
                            GT_OreDictUnificator.get("plateDenseShirabon", 8),

                            ItemList.Electric_Motor_UXV.get(64L), ItemList.Electric_Piston_UXV.get(64L),
                            ItemList.Field_Generator_UXV.get(16L),
                            getItemContainer("QuantumCircuit").get(1L).splitStack(32) },
                    new FluidStack[] { Materials.Neutronium.getMolten(32_768_000L),
                            MaterialsUEVplus.SpaceTime.getMolten(4 * 36864L),
                            MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(36864L),
                            MaterialsUEVplus.ExcitedDTEC.getFluid(4 * 36864L) },
                    getItemContainer("StargateChevron").get(1L),
                    baseStargateTime,
                    (int) TierEU.RECIPE_UMV);

            // Stargate Frame Part
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.SpaceTime, 1L),
                    (int) TierEU.RECIPE_MAX,
                    32_768,
                    500_000_000,
                    64,
                    new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Infinity, 64L),
                            GT_OreDictUnificator.get(OrePrefixes.stickLong, MaterialsUEVplus.SpaceTime, 64L),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.stickLong,
                                    MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
                                    64L),
                            GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Neutronium, 64L),

                            GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Osmiridium, 64L),
                            GT_OreDictUnificator.get("stickLongShirabon", 64),
                            getModItem(BartWorks.ID, "gt.bwMetaGeneratedstickLong", 64L, 39),
                            getModItem(GTPlusPlus.ID, "itemRodLongQuantum", 64L),

                            getModItem(GTPlusPlus.ID, "itemRodLongHypogen", 64L),
                            getModItem(GTPlusPlus.ID, "itemRodLongCelestialTungsten", 64L),
                            getModItem(BartWorks.ID, "gt.bwMetaGeneratedstickLong", 64L, 10106),
                            getModItem(GTPlusPlus.ID, "itemRodLongAstralTitanium", 64L),

                            GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.SuperconductorUMVBase, 64L),
                            GT_OreDictUnificator.get(OrePrefixes.stickLong, MaterialsUEVplus.Universium, 64L),
                            getModItem(GTPlusPlus.ID, "itemRodLongAbyssalAlloy", 64L),
                            GT_OreDictUnificator.get(OrePrefixes.stickLong, MaterialsUEVplus.TranscendentMetal, 64L), },
                    new FluidStack[] { Materials.Neutronium.getMolten(32_768_000L),
                            MaterialsUEVplus.SpaceTime.getMolten(4 * 36864L),
                            MaterialsUEVplus.Universium.getMolten(4 * 36864L),
                            MaterialsUEVplus.ExcitedDTEC.getFluid(4 * 36864L) },
                    getItemContainer("StargateFramePart").get(1L),
                    baseStargateTime,
                    (int) TierEU.RECIPE_UMV);
        }

        // Dimensionally Transcendent Plasma Forge (DTPF)
        if (EternalSingularity.isModLoaded()) {

            // DTPF Controller.
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Casing_Dim_Bridge.get(1),
                    32_000_000,
                    4096,
                    32_000_000,
                    1,
                    new Object[] { ItemList.Casing_Dim_Bridge.get(4),
                            getModItem(GregTech.ID, "gt.blockmachines", 16L, 12730),
                            getItemContainer("Hatch_Energy_UEV").get(4L),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUEV, 6),
                            ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                            ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                            new Object[] { OrePrefixes.circuit.get(Materials.Optical), 20L },
                            ItemList.Field_Generator_UEV.get(4),
                            getModItem(EternalSingularity.ID, "eternal_singularity", 4L),
                            getModItem(GTPlusPlus.ID, "MU-metaitem.01", 1L, 32105),
                            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 64L),
                            ItemList.Electric_Pump_UEV.get(4), ItemList.ZPM3.get(1),
                            getModItem(IndustrialCraft2.ID, "blockMachine2", 1, 0) },
                    new FluidStack[] { new FluidStack(FluidRegistry.getFluid("oganesson"), 128000),
                            new FluidStack(solderUEV, 36864 * 2),
                            new FluidStack(FluidRegistry.getFluid("molten.californium"), 36864),
                            Materials.NaquadahEnriched.getMolten(36864L) },
                    ItemList.Machine_Multi_PlasmaForge.get(1),
                    72000,
                    32_000_000);

            // Dimensional bridge.
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Casing_Dim_Injector.get(1),
                    8_000_000,
                    4096,
                    32_000_000,
                    1,
                    new Object[] { ItemList.Casing_Dim_Trans.get(1), ItemList.MicroTransmitter_UV.get(1),
                            new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 2L },
                            getModItem(Avaritia.ID, "Singularity", 2L, 0),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 6),
                            getModItem(NewHorizonsCoreMod.ID, "item.PicoWafer", 2, 0),
                            ItemList.Field_Generator_UHV.get(1L) },
                    new FluidStack[] { new FluidStack(FluidRegistry.getFluid("oganesson"), 8000),
                            new FluidStack(solderUEV, 1152 * 8), Materials.NaquadahEnriched.getMolten(1296L) },
                    ItemList.Casing_Dim_Bridge.get(1),
                    240 * 20,
                    32_000_000);

            // Dimensional injection casing.
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Casing_Dim_Trans.get(1),
                    2_000_000,
                    2048,
                    32_000_000,
                    1,
                    new Object[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 4),
                            GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Ledox, 1),
                            GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.CallistoIce, 1),
                            ItemList.Reactor_Coolant_Sp_6.get(1L),
                            getModItem(GTPlusPlus.ID, "itemScrewLaurenium", 12, 0),
                            new Object[] { OrePrefixes.circuit.get(Materials.Elite), 2L },
                            GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 2),
                            ItemList.Super_Chest_IV.get(1), ItemList.Super_Tank_IV.get(1),
                            getModItem(NewHorizonsCoreMod.ID, "item.PicoWafer", 1, 0), },
                    new FluidStack[] { new FluidStack(FluidRegistry.getFluid("oganesson"), 1000),
                            new FluidStack(solderUEV, 576), Materials.NaquadahEnriched.getMolten(288L) },
                    ItemList.Casing_Dim_Injector.get(1),
                    20 * 20,
                    32_000_000);

            // Dimensionally Transcendent Casing.
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    getModItem(Avaritia.ID, "Singularity", 1L, 0),
                    2_000_000,
                    2048,
                    32_000_000,
                    1,
                    new Object[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 6),
                            getModItem(GTPlusPlus.ID, "itemScrewLaurenium", 12, 0),
                            ItemList.Reactor_Coolant_Sp_6.get(1L),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 1), },
                    new FluidStack[] { new FluidStack(FluidRegistry.getFluid("oganesson"), 500),
                            new FluidStack(solderUEV, 288), Materials.NaquadahEnriched.getMolten(144L) },
                    ItemList.Casing_Dim_Trans.get(1),
                    20 * 20,
                    32_000_000);
        }

        // Deep Dark Portal
        if (BartWorks.isModLoaded()) {
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    getModItem(NewHorizonsCoreMod.ID, "item.HeavyDutyPlateTier8", 1, 0),
                    16_777_216,
                    2048,
                    2_000_000,
                    64,
                    new Object[] { getModItem(ExtraUtilities.ID, "cobblestone_compressed", 1, 7),
                            getModItem(IndustrialCraft2.ID, "blockMachine2", 1, 0),
                            GT_OreDictUnificator.get(OrePrefixes.block, Materials.Infinity, 4L),
                            new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 1 },
                            new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 1 },
                            new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 1 },
                            new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 1 },
                            getModItem(NewHorizonsCoreMod.ID, "item.PicoWafer", 32, 0), ItemList.Robot_Arm_UMV.get(4),
                            ItemList.Emitter_UMV.get(4), ItemList.Sensor_UMV.get(4), },
                    new FluidStack[] { new FluidStack(FluidRegistry.getFluid("oganesson"), 50000),
                            Materials.Infinity.getMolten(144L * 512), Materials.Cheese.getMolten(232000L), },
                    ItemList.Block_BedrockiumCompressed.get(1),
                    10000,
                    5000000);
        }

        // Batteries
        {
            // Alternate Energy Module Recipe
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Energy_LapotronicOrb2.get(1L),
                    128_000,
                    64,
                    2_000_000,
                    16,
                    new Object[] { ItemList.Circuit_Board_Wetware_Extreme.get(1),
                            new Object[] { OrePrefixes.foil.get(Materials.Bedrockium), 64L },
                            new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 4 },
                            ItemList.Circuit_Parts_Crystal_Chip_Master.get(64L),
                            ItemList.Circuit_Parts_Crystal_Chip_Master.get(64L), ItemList.Circuit_Chip_UHPIC.get(64L),
                            new ItemStack[] { ItemList.Circuit_Parts_DiodeASMD.get(64L),
                                    ItemList.Circuit_Parts_DiodeXSMD.get(8L) },
                            new ItemStack[] { ItemList.Circuit_Parts_CapacitorASMD.get(64L),
                                    ItemList.Circuit_Parts_CapacitorXSMD.get(8L) },
                            new ItemStack[] { ItemList.Circuit_Parts_ResistorASMD.get(64L),
                                    ItemList.Circuit_Parts_ResistorXSMD.get(8L) },
                            new ItemStack[] { ItemList.Circuit_Parts_TransistorASMD.get(64L),
                                    ItemList.Circuit_Parts_TransistorXSMD.get(8L) },
                            getModItem(GTPlusPlus.ID, "itemFineWireHypogen", 48, 0) },
                    new FluidStack[] { new FluidStack(solderUEV, 720) },
                    ItemList.Energy_Module.get(1),
                    50 * 20,
                    320_000);

            // Alternate Energy Cluster Recipe
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Energy_Module.get(1L),
                    576_000,
                    256,
                    8_000_000,
                    32,
                    new Object[] { ItemList.Circuit_Board_Bio_Ultra.get(1),
                            new Object[] { OrePrefixes.foil.get(Materials.CosmicNeutronium), 64L },
                            new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 4L },
                            ItemList.Circuit_Parts_Crystal_Chip_Master.get(64L),
                            ItemList.Circuit_Parts_Crystal_Chip_Master.get(64L),
                            ItemList.Circuit_Parts_Crystal_Chip_Master.get(64L),
                            ItemList.Circuit_Parts_Crystal_Chip_Master.get(64L), ItemList.Circuit_Chip_NPIC.get(64L),
                            ItemList.Circuit_Parts_DiodeXSMD.get(32L), ItemList.Circuit_Parts_CapacitorXSMD.get(32L),
                            ItemList.Circuit_Parts_ResistorXSMD.get(32L),
                            ItemList.Circuit_Parts_TransistorXSMD.get(32L),
                            new Object[] { OrePrefixes.wireGt01.get(MaterialsUEVplus.SpaceTime), 12L } },
                    new FluidStack[] { new FluidStack(solderUEV, 1440) },
                    ItemList.Energy_Cluster.get(1),
                    50 * 20,
                    (int) TierEU.RECIPE_UHV);

            // Ultimate Battery
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Energy_Cluster.get(1L),
                    12000,
                    16,
                    100000,
                    3,
                    new Object[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Tritanium, 64L),
                            new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 1L },
                            new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 1L },
                            new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 1L },
                            new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 1L },
                            ItemList.Energy_Cluster.get(8L), ItemList.Field_Generator_UV.get(2),
                            ItemList.Circuit_Wafer_HPIC.get(64), ItemList.Circuit_Wafer_HPIC.get(64),
                            ItemList.Circuit_Parts_DiodeASMD.get(32),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 32), },
                    new FluidStack[] { new FluidStack(solderIndalloy, 2880),
                            new FluidStack(FluidRegistry.getFluid("ic2coolant"), 16000) },
                    ItemList.ZPM2.get(1),
                    3000,
                    400000);

            // Alternate Ultimate Battery Recipe
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Circuit_Chip_PPIC.get(1L),
                    2_304_000,
                    1024,
                    32_000_000,
                    64,
                    new Object[] { ItemList.Circuit_Board_Optical.get(1), GT_OreDictUnificator.get("foilShirabon", 64),
                            new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 4 },
                            ItemList.Circuit_Parts_Crystal_Chip_Master.get(64L),
                            ItemList.Circuit_Parts_Crystal_Chip_Master.get(64L),
                            ItemList.Circuit_Parts_Crystal_Chip_Master.get(64L),
                            ItemList.Circuit_Parts_Crystal_Chip_Master.get(64L),
                            ItemList.Circuit_Parts_Crystal_Chip_Master.get(64L),
                            ItemList.Circuit_Parts_Crystal_Chip_Master.get(64L),
                            ItemList.Circuit_Parts_Crystal_Chip_Master.get(64L), ItemList.Circuit_Chip_PPIC.get(64L),
                            ItemList.Circuit_Parts_DiodeXSMD.get(64L), ItemList.Circuit_Parts_CapacitorXSMD.get(64L),
                            ItemList.Circuit_Parts_ResistorXSMD.get(64L),
                            ItemList.Circuit_Parts_TransistorXSMD.get(64L),
                            new Object[] {
                                    OrePrefixes.bolt.get(MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter),
                                    4L } },
                    new FluidStack[] { new FluidStack(solderUEV, 2880),
                            MaterialsUEVplus.WhiteDwarfMatter.getMolten(576),
                            MaterialsUEVplus.BlackDwarfMatter.getMolten(576) },
                    ItemList.ZPM2.get(1),
                    50 * 20,
                    (int) TierEU.RECIPE_UEV);

            // Really Ultimate Battery
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.ZPM2.get(1L),
                    24000,
                    64,
                    200000,
                    6,
                    new Object[] { GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Neutronium, 32L),
                            GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Neutronium, 32L),
                            new Object[] { OrePrefixes.circuit.get(Materials.Bio), 1L },
                            new Object[] { OrePrefixes.circuit.get(Materials.Bio), 1L },
                            new Object[] { OrePrefixes.circuit.get(Materials.Bio), 1L },
                            new Object[] { OrePrefixes.circuit.get(Materials.Bio), 1L }, ItemList.ZPM2.get(8),
                            ItemList.Field_Generator_UHV.get(4), ItemList.Circuit_Wafer_UHPIC.get(64),
                            ItemList.Circuit_Wafer_UHPIC.get(64), ItemList.Circuit_Wafer_SoC2.get(32),
                            ItemList.Circuit_Parts_DiodeASMD.get(64),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUHV, 64), },
                    new FluidStack[] { new FluidStack(solderUEV, 4608), Materials.Naquadria.getMolten(9216),
                            new FluidStack(FluidRegistry.getFluid("ic2coolant"), 32000) },
                    ItemList.ZPM3.get(1),
                    4000,
                    1600000);

            // Extremely Ultimate Battery
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.ZPM3.get(1L),
                    1_200_000,
                    128,
                    8_000_000,
                    16,
                    new Object[] { GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.InfinityCatalyst, 32L),
                            GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.InfinityCatalyst, 32L),
                            new Object[] { OrePrefixes.circuit.get(Materials.Optical), 1L },
                            new Object[] { OrePrefixes.circuit.get(Materials.Optical), 1L },
                            new Object[] { OrePrefixes.circuit.get(Materials.Optical), 1L },
                            new Object[] { OrePrefixes.circuit.get(Materials.Optical), 1L }, ItemList.ZPM3.get(8),
                            ItemList.Field_Generator_UEV.get(4), ItemList.Circuit_Wafer_PPIC.get(64),
                            ItemList.Circuit_Wafer_PPIC.get(64), ItemList.Circuit_Wafer_SoC2.get(64),
                            ItemList.Circuit_Parts_DiodeXSMD.get(64),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUEV, 64), },
                    new FluidStack[] { new FluidStack(solderUEV, 9216), Materials.Quantium.getMolten(18_432),
                            Materials.Naquadria.getMolten(9_216 * 2), Materials.SuperCoolant.getFluid(64_000) },
                    ItemList.ZPM4.get(1),
                    250 * 20,
                    6_400_000);

            if (GTPlusPlusEverglades.isModLoaded()) {
                // Insanely Ultimate Battery
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                        ItemList.ZPM4.get(1),
                        24_000_000,
                        1_280,
                        32_000_000,
                        32,
                        new Object[] { ELEMENT.STANDALONE.HYPOGEN.getPlateDouble(32),
                                ELEMENT.STANDALONE.HYPOGEN.getPlateDouble(32),
                                new Object[] { OrePrefixes.circuit.get(Materials.Piko), 1 },
                                new Object[] { OrePrefixes.circuit.get(Materials.Piko), 1 },
                                new Object[] { OrePrefixes.circuit.get(Materials.Piko), 1 },
                                new Object[] { OrePrefixes.circuit.get(Materials.Piko), 1 }, ItemList.ZPM4.get(8L),
                                ItemList.Field_Generator_UIV.get(4), ItemList.Circuit_Wafer_QPIC.get(64),
                                ItemList.Circuit_Wafer_QPIC.get(64),
                                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.RawPicoWafer", 64),
                                ItemList.Circuit_Parts_DiodeXSMD.get(64), ItemList.Circuit_Parts_InductorXSMD.get(32),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.SuperconductorUIV, 64) },
                        new FluidStack[] { new FluidStack(solderUEV, 18_432),
                                ELEMENT.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(18_432),
                                Materials.Quantium.getMolten(18_432), Materials.SuperCoolant.getFluid(128_000) },
                        ItemList.ZPM5.get(1),
                        300 * 20,
                        (int) TierEU.RECIPE_UIV);

                // Mega Ultimate Battery
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                        ItemList.ZPM5.get(1L),
                        480_000_000,
                        12_288,
                        128_000_000,
                        64,
                        new Object[] { ELEMENT.STANDALONE.DRAGON_METAL.getPlateDouble(32),
                                ELEMENT.STANDALONE.DRAGON_METAL.getPlateDouble(32),
                                new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 1 },
                                new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 1 },
                                new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 1 },
                                new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 1 }, ItemList.ZPM5.get(8L),
                                ItemList.Field_Generator_UMV.get(4), ItemList.Circuit_Wafer_QPIC.get(64),
                                ItemList.Circuit_Wafer_QPIC.get(64),
                                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.PicoWafer", 64),
                                ItemList.Circuit_Parts_DiodeXSMD.get(64), ItemList.Circuit_Parts_InductorXSMD.get(64),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUMV, 64) },
                        new FluidStack[] { new FluidStack(solderUEV, 36_864),
                                ELEMENT.STANDALONE.ASTRAL_TITANIUM.getFluidStack(36_864),
                                ELEMENT.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(36_864),
                                Materials.SuperCoolant.getFluid(256_000) },
                        ItemList.ZPM6.get(1),
                        350 * 20,
                        (int) TierEU.RECIPE_UMV);
            }
        }

        if (GTPlusPlus.isModLoaded()) {
            // MK4 Computer
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    GregtechItemList.Compressed_Fusion_Reactor.get(1),
                    320000,
                    512,
                    2000000,
                    1,
                    new Object[] { GregtechItemList.Casing_Fusion_Internal.get(1),
                            new Object[] { OrePrefixes.circuit.get(Materials.Bio), 1L },
                            new Object[] { OrePrefixes.circuit.get(Materials.Bio), 1L },
                            new Object[] { OrePrefixes.circuit.get(Materials.Bio), 1L },
                            new Object[] { OrePrefixes.circuit.get(Materials.Bio), 1L },
                            GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 4),
                            ItemList.Field_Generator_UHV.get(2), ItemList.Circuit_Wafer_QPIC.get(64),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 32) },
                    new FluidStack[] { Materials.UUMatter.getFluid(50000), ALLOY.CINOBITE.getFluidStack(9216),
                            ALLOY.OCTIRON.getFluidStack(9216),
                            ELEMENT.STANDALONE.ASTRAL_TITANIUM.getFluidStack(9216), },
                    GregtechItemList.FusionComputer_UV2.get(1),
                    6000,
                    2000000);

            // MK4 Coils
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Casing_Fusion_Coil.get(1L),
                    160000,
                    512,
                    2000000,
                    1,
                    new Object[] { ItemList.Energy_LapotronicOrb2.get(16L),
                            new Object[] { OrePrefixes.circuit.get(Materials.Master), 16L },
                            new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 8L },
                            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 8),
                            ItemList.Emitter_UHV.get(1), ItemList.Sensor_UHV.get(1),
                            ItemList.Casing_Fusion_Coil.get(1L), },
                    new FluidStack[] { Materials.UUMatter.getFluid(8000L), ALLOY.CINOBITE.getFluidStack(2304),
                            ALLOY.OCTIRON.getFluidStack(2304),
                            ELEMENT.STANDALONE.ASTRAL_TITANIUM.getFluidStack(2304), },
                    GregtechItemList.Casing_Fusion_Internal.get(1),
                    1200,
                    2000000);

            // MK4 Casing
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Casing_Fusion2.get(1L),
                    80000,
                    512,
                    2000000,
                    1,
                    new Object[] { new Object[] { OrePrefixes.circuit.get(Materials.Data), 16L },
                            new Object[] { OrePrefixes.circuit.get(Materials.Elite), 8L },
                            GT_OreDictUnificator.get(OrePrefixes.block, Materials.TungstenCarbide, 8),
                            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 8),
                            ItemList.Electric_Motor_UHV.get(2), ItemList.Electric_Piston_UHV.get(1),
                            ItemList.Casing_Fusion2.get(1L), },
                    new FluidStack[] { Materials.UUMatter.getFluid(1000L), ALLOY.CINOBITE.getFluidStack(576),
                            ALLOY.OCTIRON.getFluidStack(576), ELEMENT.STANDALONE.ASTRAL_TITANIUM.getFluidStack(576), },
                    GregtechItemList.Casing_Fusion_External.get(1),
                    300,
                    2000000);

            // MK5 Computer
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    GregtechItemList.FusionComputer_UV2.get(1),
                    2560000,
                    4096,
                    (int) TierEU.RECIPE_UEV,
                    8,
                    new Object[] { GregtechItemList.Casing_Fusion_Internal2.get(1),
                            new Object[] { OrePrefixes.circuit.get(Materials.Optical), 1L },
                            new Object[] { OrePrefixes.circuit.get(Materials.Optical), 1L },
                            new Object[] { OrePrefixes.circuit.get(Materials.Optical), 1L },
                            new Object[] { OrePrefixes.circuit.get(Materials.Optical), 1L },
                            GT_OreDictUnificator.get("plateDenseMetastableOganesson", 4),
                            ItemList.Field_Generator_UEV.get(2), getItemContainer("PicoWafer").get(64L),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUEV, 32) },
                    new FluidStack[] { ELEMENT.getInstance().CURIUM.getFluidStack(9216),
                            ELEMENT.STANDALONE.CHRONOMATIC_GLASS.getFluidStack(9216), ALLOY.ABYSSAL.getFluidStack(9216),
                            ELEMENT.STANDALONE.DRAGON_METAL.getFluidStack(9216) },
                    GregtechItemList.FusionComputer_UV3.get(1),
                    6000,
                    (int) TierEU.RECIPE_UEV);

            // MK5 Coils
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    GregtechItemList.Casing_Fusion_Internal.get(1),
                    2560000,
                    4096,
                    (int) TierEU.RECIPE_UEV,
                    8,
                    new Object[] { ItemList.Energy_Module.get(16),
                            new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 16L },
                            new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 8L },
                            ELEMENT.STANDALONE.RHUGNOR.getPlate(8), ItemList.Emitter_UEV.get(1),
                            ItemList.Sensor_UEV.get(1), getModItem(GoodGenerator.ID, "compactFusionCoil", 1, 2) },
                    new FluidStack[] { ELEMENT.getInstance().NEPTUNIUM.getFluidStack(2304),
                            ELEMENT.STANDALONE.CHRONOMATIC_GLASS.getFluidStack(2304), ALLOY.ABYSSAL.getFluidStack(2304),
                            ELEMENT.STANDALONE.DRAGON_METAL.getFluidStack(2304) },
                    GregtechItemList.Casing_Fusion_Internal2.get(1),
                    1200,
                    (int) TierEU.RECIPE_UEV);

            // MK5 Casing
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    GregtechItemList.Casing_Fusion_External.get(1L),
                    2560000,
                    4096,
                    (int) TierEU.RECIPE_UEV,
                    8,
                    new Object[] { new Object[] { OrePrefixes.circuit.get(Materials.Elite), 16L },
                            new Object[] { OrePrefixes.circuit.get(Materials.Master), 8L },
                            GT_OreDictUnificator.get(OrePrefixes.block, Materials.NaquadahAlloy, 8),
                            ELEMENT.STANDALONE.CHRONOMATIC_GLASS.getPlate(8), ItemList.Electric_Motor_UEV.get(2),
                            ItemList.Electric_Piston_UEV.get(1), GregtechItemList.Casing_Fusion_External.get(1L) },
                    new FluidStack[] { ELEMENT.getInstance().FERMIUM.getFluidStack(1152),
                            ELEMENT.STANDALONE.CHRONOMATIC_GLASS.getFluidStack(1152), ALLOY.ABYSSAL.getFluidStack(1152),
                            ELEMENT.STANDALONE.DRAGON_METAL.getFluidStack(1152) },
                    GregtechItemList.Casing_Fusion_External2.get(1),
                    300,
                    (int) TierEU.RECIPE_UEV);
        }

        // Draconic Evolution Fusion Crafter Controller
        if (BloodMagic.isModLoaded() && ElectroMagicTools.isModLoaded()) {
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    getModItem(ElectroMagicTools.ID, "EMT_GTBLOCK_CASEING", 1, 8),
                    16_777_216,
                    1024,
                    2_000_000,
                    8,
                    new Object[] { getModItem(GregTech.ID, "gt.blockmachines", 1, 10783),
                            getModItem(ElectroMagicTools.ID, "EMT_GTBLOCK_CASEING", 1, 8),
                            GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsBotania.GaiaSpirit, 1L),
                            ItemList.Casing_Coil_AwakenedDraconium.get(8L), ItemList.Electric_Motor_UHV.get(8L),
                            ItemList.Robot_Arm_UHV.get(4L),
                            new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 4 },
                            ItemList.Gravistar.get(4, new Object() {}),
                            getModItem(Thaumcraft.ID, "ItemEldritchObject", 1, 3),
                            getModItem(BloodMagic.ID, "bloodMagicBaseItems", 8, 29),
                            getModItem(BloodMagic.ID, "bloodMagicBaseItems", 8, 28), },
                    new FluidStack[] { new FluidStack(solderIndalloy, 2880), Materials.Void.getMolten(2880L),
                            Materials.DraconiumAwakened.getMolten(1440), },
                    getModItem(GregTech.ID, "gt.blockmachines", 1, 5001),
                    1500,
                    8_000_000);

            if (DraconicEvolution.isModLoaded()) {
                // DE Schematics Cores Tier 1
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                        getModItem(ElectroMagicTools.ID, "EMT_GTBLOCK_CASEING", 1, 9),
                        5_000_000,
                        512,
                        1_000_000,
                        4,
                        new Object[] { getModItem(DraconicEvolution.ID, "draconicCore", 1, 0),
                                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Draconium, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Ichorium, 1L),
                                new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 1 }, },
                        new FluidStack[] { Materials.Sunnarium.getMolten(14400L), Materials.Void.getMolten(28800L), },
                        getModItem(ElectroMagicTools.ID, "EMTItems", 1, 16),
                        6000,
                        500_000);

                // DE Schematics Cores Tier 2
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                        getModItem(DraconicEvolution.ID, "draconicCore", 1, 0),
                        10_000_000,
                        1024,
                        4_000_000,
                        8,
                        new Object[] { getModItem(DraconicEvolution.ID, "draconicCore", 4, 0),
                                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Draconium, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 1L),
                                new Object[] { OrePrefixes.circuit.get(Materials.Bio), 1 }, },
                        new FluidStack[] { Materials.Neutronium.getMolten(14400L), Materials.Void.getMolten(57600L), },
                        getModItem(ElectroMagicTools.ID, "EMTItems", 1, 17),
                        12000,
                        2_000_000);

                // DE Schematics Cores Tier 3
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                        getModItem(DraconicEvolution.ID, "wyvernCore", 1, 0),
                        20_000_000,
                        2048,
                        16_000_000,
                        16,
                        new Object[] { getModItem(DraconicEvolution.ID, "wyvernCore", 4, 0),
                                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.DraconiumAwakened, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 1L),
                                new Object[] { OrePrefixes.circuit.get(Materials.Optical), 1L } },
                        new FluidStack[] { Materials.Infinity.getMolten(14400L), Materials.Void.getMolten(115200L), },
                        getModItem(ElectroMagicTools.ID, "EMTItems", 1, 18),
                        24000,
                        8_000_000);

                // DE Schematics Cores Tier 4
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                        getModItem(DraconicEvolution.ID, "awakenedCore", 1, 0),
                        40_000_000,
                        4096,
                        64_000_000,
                        64,
                        new Object[] { getModItem(DraconicEvolution.ID, "awakenedCore", 8, 0),
                                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.DraconiumAwakened, 4L),
                                GT_OreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.SpaceTime, 1L),
                                new Object[] { OrePrefixes.circuit.get(Materials.Piko), 1 }, },
                        new FluidStack[] { MaterialsUEVplus.SpaceTime.getMolten(14400L),
                                Materials.Void.getMolten(230400L), },
                        getModItem(ElectroMagicTools.ID, "EMTItems", 1, 19),
                        36000,
                        32_000_000);
            }
        }

        // Debug maintenance hatch
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Hatch_AutoMaintenance.get(1L),
                2764800,
                128,
                500000,
                6,
                new Object[] { ItemList.Hatch_AutoMaintenance.get(1L), ItemList.Robot_Arm_UV.get(1L),
                        ItemList.Electric_Pump_UV.get(1L), ItemList.Conveyor_Module_UV.get(1L),
                        new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 4L },
                        ItemList.Energy_LapotronicOrb2.get(1L), ItemList.Duct_Tape.get(64L),
                        ItemList.Duct_Tape.get(64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64L), },
                new FluidStack[] { Materials.Lubricant.getFluid(256000), new FluidStack(solderIndalloy, 1296), },
                CustomItemList.hatch_CreativeMaintenance.get(1),
                6000,
                500000);

    }

    private void itemPartsUHVAsslineRecipes() {

        // ----------------------------------------------------------------------
        // ------------------------- Set up information -------------------------
        // ----------------------------------------------------------------------

        Fluid solderIndalloy = FluidRegistry.getFluid("molten.indalloy140") != null
                ? FluidRegistry.getFluid("molten.indalloy140")
                : FluidRegistry.getFluid("molten.solderingalloy");

        int total_computation = 24000;
        int comp_per_second = 32;
        int research_eu_per_tick = 500_000;
        int research_amperage = 1;

        FluidStack fluid_0 = Materials.Naquadria.getMolten(2592);
        FluidStack fluid_1 = new FluidStack(solderIndalloy, 2592);
        FluidStack fluid_2 = Materials.Lubricant.getFluid(4000);

        int crafting_time_in_ticks = 1000;
        int crafting_eu_per_tick = 500_000;

        // -------------------------------------------------------------

        // ------------------------- UHV Motor -------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Electric_Motor_UV.get(1L),
                total_computation,
                comp_per_second,
                research_eu_per_tick,
                research_amperage,
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.SamariumMagnetic, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.CosmicNeutronium, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.ring, Materials.CosmicNeutronium, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.round, Materials.CosmicNeutronium, 32L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 2L) },
                new FluidStack[] { fluid_0, fluid_1, fluid_2 },
                ItemList.Electric_Motor_UHV.get(1L),
                crafting_time_in_ticks,
                crafting_eu_per_tick);

        // -------------------------------------------------------------

        // --------------------- UHV Electric Pump ---------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Electric_Pump_UV.get(1L),
                total_computation,
                comp_per_second,
                research_eu_per_tick,
                research_amperage,
                new Object[] { ItemList.Electric_Motor_UHV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Neutronium, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CosmicNeutronium, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.CosmicNeutronium, 16L),
                        new Object[] { OrePrefixes.ring.get(Materials.AnySyntheticRubber), 32L },
                        GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.CosmicNeutronium, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 2L) },
                new FluidStack[] { fluid_0, fluid_1, fluid_2 },
                ItemList.Electric_Pump_UHV.get(1),
                crafting_time_in_ticks,
                crafting_eu_per_tick);

        // -------------------------------------------------------------

        // ----------------------- UHV Conveyor ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Conveyor_Module_UV.get(1L),
                total_computation,
                comp_per_second,
                research_eu_per_tick,
                research_amperage,
                new Object[] { ItemList.Electric_Motor_UHV.get(2L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CosmicNeutronium, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.ring, Materials.CosmicNeutronium, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.round, Materials.CosmicNeutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 2L),
                        new Object[] { OrePrefixes.plate.get(Materials.AnySyntheticRubber), 40L } },
                new FluidStack[] { fluid_0, fluid_1, fluid_2 },
                ItemList.Conveyor_Module_UHV.get(1),
                crafting_time_in_ticks,
                crafting_eu_per_tick);

        // -------------------------------------------------------------

        // -------------------- UHV Robot Arm --------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Robot_Arm_UV.get(1L),
                total_computation,
                comp_per_second,
                research_eu_per_tick,
                research_amperage,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.CosmicNeutronium, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.gear, Materials.CosmicNeutronium, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.CosmicNeutronium, 6L),
                        ItemList.Electric_Motor_UHV.get(2L), ItemList.Electric_Piston_UHV.get(1L),
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 2L },
                        new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 4L },
                        new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 8L },
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 6L) },
                new FluidStack[] { fluid_0, fluid_1, fluid_2 },
                ItemList.Robot_Arm_UHV.get(1L),
                crafting_time_in_ticks,
                crafting_eu_per_tick);

        // -------------------------------------------------------------

        // -------------------- UHV Electric Piston --------------------
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Electric_Piston_UV.get(1L),
                total_computation,
                comp_per_second,
                research_eu_per_tick,
                research_amperage,
                new ItemStack[] { ItemList.Electric_Motor_UHV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CosmicNeutronium, 6L),
                        GT_OreDictUnificator.get(OrePrefixes.ring, Materials.CosmicNeutronium, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.round, Materials.CosmicNeutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.CosmicNeutronium, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.gear, Materials.CosmicNeutronium, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.CosmicNeutronium, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 4L) },
                new FluidStack[] { fluid_0, fluid_1, fluid_2 },
                ItemList.Electric_Piston_UHV.get(1),
                crafting_time_in_ticks,
                crafting_eu_per_tick);

        // -------------------------------------------------------------

        // ------------------------ UHV Emitter ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Emitter_UV.get(1L),
                total_computation,
                comp_per_second,
                research_eu_per_tick,
                research_amperage,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 1L),
                        ItemList.Electric_Motor_UHV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.CosmicNeutronium, 8L),
                        ItemList.Gravistar.get(8L), new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 4L },
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.ElectrumFlux, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.ElectrumFlux, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.ElectrumFlux, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.ElectrumFlux, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 7L) },
                new FluidStack[] { fluid_0, fluid_1 },
                ItemList.Emitter_UHV.get(1L),
                crafting_time_in_ticks,
                crafting_eu_per_tick);

        // -------------------------------------------------------------

        // ------------------------ UHV Sensor ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Sensor_UV.get(1L),
                total_computation,
                comp_per_second,
                research_eu_per_tick,
                research_amperage,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 1L),
                        ItemList.Electric_Motor_UHV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CosmicNeutronium, 8L),
                        ItemList.Gravistar.get(8L), new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 4L },
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.ElectrumFlux, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.ElectrumFlux, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.ElectrumFlux, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.ElectrumFlux, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 7L) },
                new FluidStack[] { fluid_0, fluid_1 },
                ItemList.Sensor_UHV.get(1L),
                crafting_time_in_ticks,
                crafting_eu_per_tick);

        // ---------------------------------------------------------------------

        // ------------------------ UHV Field Generator ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Field_Generator_UV.get(1),
                total_computation,
                comp_per_second,
                research_eu_per_tick,
                research_amperage,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CosmicNeutronium, 6L),
                        ItemList.Gravistar.get(4L), ItemList.Emitter_UHV.get(4L),
                        new Object[] { OrePrefixes.circuit.get(Materials.Bio), 4L },
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 8L) },
                new FluidStack[] { fluid_0, fluid_1 },
                ItemList.Field_Generator_UHV.get(1L),
                crafting_time_in_ticks,
                crafting_eu_per_tick);
    }

    private void itemPartsUEVAsslineRecipes() {

        // ----------------------------------------------------------------------
        // ------------------------- Set up information -------------------------
        // ----------------------------------------------------------------------

        Fluid mutated_living_solder = FluidRegistry.getFluid("molten.mutatedlivingsolder") != null
                ? FluidRegistry.getFluid("molten.mutatedlivingsolder")
                : FluidRegistry.getFluid("molten.solderingalloy");

        int total_computation = 48_000;
        int comp_per_second = 64;
        int research_eu_per_tick = 2_000_000;
        int research_amperage = 1;

        FluidStack fluid_0 = Materials.Quantium.getMolten(2592);
        FluidStack fluid_1 = new FluidStack(mutated_living_solder, 2592);
        FluidStack fluid_2 = Materials.Lubricant.getFluid(4000);

        int crafting_time_in_ticks = 1000;
        int crafting_eu_per_tick = 2_000_000;

        // -------------------------------------------------------------

        // ------------------------- UEV Motor -------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Electric_Motor_UHV.get(1L),
                total_computation,
                comp_per_second,
                research_eu_per_tick,
                research_amperage,
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.TengamAttuned, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Infinity, 16L),
                        GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Infinity, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.round, Materials.Infinity, 32L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 2L) },
                new FluidStack[] { fluid_0, fluid_1, fluid_2 },
                ItemList.Electric_Motor_UEV.get(1L),
                crafting_time_in_ticks,
                crafting_eu_per_tick);

        // -------------------------------------------------------------

        // --------------------- UEV Electric Pump ---------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Electric_Pump_UHV.get(1L),
                total_computation,
                comp_per_second,
                research_eu_per_tick,
                research_amperage,
                new Object[] { ItemList.Electric_Motor_UEV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.NetherStar, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Infinity, 16L),
                        new Object[] { OrePrefixes.ring.get(Materials.AnySyntheticRubber), 64L },
                        GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Infinity, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 2L) },
                new FluidStack[] { fluid_0, fluid_1, fluid_2 },
                ItemList.Electric_Pump_UEV.get(1),
                crafting_time_in_ticks,
                crafting_eu_per_tick);

        // -------------------------------------------------------------

        // ----------------------- UEV Conveyor ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Conveyor_Module_UHV.get(1L),
                total_computation,
                comp_per_second,
                research_eu_per_tick,
                research_amperage,
                new Object[] { ItemList.Electric_Motor_UEV.get(2L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Infinity, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.round, Materials.Infinity, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 2L),
                        new Object[] { OrePrefixes.plate.get(Materials.AnySyntheticRubber), 64L },
                        new Object[] { OrePrefixes.plate.get(Materials.AnySyntheticRubber), 16L } },
                new FluidStack[] { fluid_0, fluid_1, fluid_2 },
                ItemList.Conveyor_Module_UEV.get(1),
                crafting_time_in_ticks,
                crafting_eu_per_tick);

        // -------------------------------------------------------------

        // -------------------- UEV Robot Arm --------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Robot_Arm_UHV.get(1L),
                total_computation,
                comp_per_second,
                research_eu_per_tick,
                research_amperage,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Infinity, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.gear, Materials.Infinity, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Infinity, 6L),
                        ItemList.Electric_Motor_UEV.get(2L), ItemList.Electric_Piston_UEV.get(1L),
                        new Object[] { OrePrefixes.circuit.get(Materials.Bio), 2L },
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 4L },
                        new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 8L },
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 6L) },
                new FluidStack[] { fluid_0, fluid_1, fluid_2 },
                ItemList.Robot_Arm_UEV.get(1L),
                crafting_time_in_ticks,
                crafting_eu_per_tick);

        // -------------------------------------------------------------

        // -------------------- UEV Electric Piston --------------------
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Electric_Piston_UHV.get(1L),
                total_computation,
                comp_per_second,
                research_eu_per_tick,
                research_amperage,
                new ItemStack[] { ItemList.Electric_Motor_UEV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 6L),
                        GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Infinity, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.round, Materials.Infinity, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Infinity, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.gear, Materials.Infinity, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Infinity, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 4L) },
                new FluidStack[] { fluid_0, fluid_1, fluid_2 },
                ItemList.Electric_Piston_UEV.get(1),
                crafting_time_in_ticks,
                crafting_eu_per_tick);

        // -------------------------------------------------------------

        // ------------------------ UEV Emitter ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Emitter_UHV.get(1L),
                total_computation,
                comp_per_second,
                research_eu_per_tick,
                research_amperage,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1L),
                        ItemList.Electric_Motor_UEV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Infinity, 16L),
                        ItemList.Gravistar.get(16L), new Object[] { OrePrefixes.circuit.get(Materials.Bio), 4L },
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.InfinityCatalyst, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.InfinityCatalyst, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.InfinityCatalyst, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.InfinityCatalyst, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 7L) },
                new FluidStack[] { fluid_0, fluid_1 },
                ItemList.Emitter_UEV.get(1L),
                crafting_time_in_ticks,
                crafting_eu_per_tick);

        // -------------------------------------------------------------

        // ------------------------ UEV Sensor ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Sensor_UHV.get(1L),
                total_computation,
                comp_per_second,
                research_eu_per_tick,
                research_amperage,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1L),
                        ItemList.Electric_Motor_UEV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 8L), ItemList.Gravistar.get(16),
                        new Object[] { OrePrefixes.circuit.get(Materials.Bio), 4L },
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.InfinityCatalyst, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.InfinityCatalyst, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.InfinityCatalyst, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.InfinityCatalyst, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 7L) },
                new FluidStack[] { fluid_0, fluid_1 },
                ItemList.Sensor_UEV.get(1L),
                crafting_time_in_ticks,
                crafting_eu_per_tick);

        // ---------------------------------------------------------------------

        // ------------------------ UEV Field Generator ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Field_Generator_UHV.get(1),
                total_computation,
                comp_per_second,
                research_eu_per_tick,
                research_amperage,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 6L), ItemList.Gravistar.get(8L),
                        ItemList.Emitter_UEV.get(4L), new Object[] { OrePrefixes.circuit.get(Materials.Optical), 4L },
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 8L) },
                new FluidStack[] { fluid_0, fluid_1 },
                ItemList.Field_Generator_UEV.get(1L),
                crafting_time_in_ticks,
                crafting_eu_per_tick);
    }

    private void itemPartsUIVAsslineRecipes() {

        // ----------------------------------------------------------------------
        // ------------------------- Set up information -------------------------
        // ----------------------------------------------------------------------

        Fluid mutated_living_solder = FluidRegistry.getFluid("molten.mutatedlivingsolder") != null
                ? FluidRegistry.getFluid("molten.mutatedlivingsolder")
                : FluidRegistry.getFluid("molten.solderingalloy");
        Fluid celestialTungsten = FluidRegistry.getFluid("molten.celestialtungsten");

        int total_computation = 96_000;
        int comp_per_second = 128;
        int research_eu_per_tick = 8_000_000;
        int research_amperage = 1;

        FluidStack fluid_0 = celestialTungsten != null ? new FluidStack(celestialTungsten, 576) : null;
        FluidStack fluid_1 = new FluidStack(mutated_living_solder, 2592);
        FluidStack fluid_2 = Materials.Lubricant.getFluid(4000);

        int crafting_time_in_ticks = 1000;
        int crafting_eu_per_tick = 8_000_000;

        // -------------------------------------------------------------

        // ------------------------- UIV Motor -------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Electric_Motor_UEV.get(1L),
                total_computation,
                comp_per_second,
                research_eu_per_tick,
                research_amperage,
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.TengamAttuned, 16L),
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, MaterialsUEVplus.TranscendentMetal, 16L),
                        GT_OreDictUnificator.get(OrePrefixes.ring, MaterialsUEVplus.TranscendentMetal, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.round, MaterialsUEVplus.TranscendentMetal, 32L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Infinity, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Infinity, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Infinity, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Infinity, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Infinity, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Infinity, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Infinity, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Infinity, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NetherStar, 2L) },
                new FluidStack[] { fluid_0, fluid_1, fluid_2 },
                ItemList.Electric_Motor_UIV.get(1L),
                crafting_time_in_ticks,
                crafting_eu_per_tick);

        // -------------------------------------------------------------

        // --------------------- UIV Electric Pump ---------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Electric_Pump_UEV.get(1L),
                total_computation,
                comp_per_second,
                research_eu_per_tick,
                research_amperage,
                new Object[] { ItemList.Electric_Motor_UIV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.DraconiumAwakened, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.TranscendentMetal, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.screw, MaterialsUEVplus.TranscendentMetal, 16L),
                        new Object[] { OrePrefixes.ring.get(Materials.AnySyntheticRubber), 64L },
                        GT_OreDictUnificator.get(OrePrefixes.rotor, MaterialsUEVplus.TranscendentMetal, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NetherStar, 2L) },
                new FluidStack[] { fluid_0, fluid_1, fluid_2 },
                ItemList.Electric_Pump_UIV.get(1),
                crafting_time_in_ticks,
                crafting_eu_per_tick);

        // -------------------------------------------------------------

        // ----------------------- UIV Conveyor ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Conveyor_Module_UEV.get(1L),
                total_computation,
                comp_per_second,
                research_eu_per_tick,
                research_amperage,
                new Object[] { ItemList.Electric_Motor_UIV.get(2L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.TranscendentMetal, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.ring, MaterialsUEVplus.TranscendentMetal, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.round, MaterialsUEVplus.TranscendentMetal, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NetherStar, 2L),
                        new Object[] { OrePrefixes.plate.get(Materials.AnySyntheticRubber), 64L },
                        new Object[] { OrePrefixes.plate.get(Materials.AnySyntheticRubber), 16L } },
                new FluidStack[] { fluid_0, fluid_1, fluid_2 },
                ItemList.Conveyor_Module_UIV.get(1),
                crafting_time_in_ticks,
                crafting_eu_per_tick);

        // -------------------------------------------------------------

        // -------------------- UIV Robot Arm --------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Robot_Arm_UEV.get(1L),
                total_computation,
                comp_per_second,
                research_eu_per_tick,
                research_amperage,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.stickLong, MaterialsUEVplus.TranscendentMetal, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.gear, MaterialsUEVplus.TranscendentMetal, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, MaterialsUEVplus.TranscendentMetal, 6L),
                        ItemList.Electric_Motor_UIV.get(2L), ItemList.Electric_Piston_UIV.get(1L),
                        new Object[] { OrePrefixes.circuit.get(Materials.Optical), 2L },
                        new Object[] { OrePrefixes.circuit.get(Materials.Bio), 4L },
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 8L },
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NetherStar, 6L) },
                new FluidStack[] { fluid_0, fluid_1, fluid_2 },
                ItemList.Robot_Arm_UIV.get(1L),
                crafting_time_in_ticks,
                crafting_eu_per_tick);

        // -------------------------------------------------------------

        // -------------------- UIV Electric Piston --------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Electric_Piston_UEV.get(1L),
                total_computation,
                comp_per_second,
                research_eu_per_tick,
                research_amperage,
                new ItemStack[] { ItemList.Electric_Motor_UIV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.TranscendentMetal, 6L),
                        GT_OreDictUnificator.get(OrePrefixes.ring, MaterialsUEVplus.TranscendentMetal, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.round, MaterialsUEVplus.TranscendentMetal, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, MaterialsUEVplus.TranscendentMetal, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.gear, MaterialsUEVplus.TranscendentMetal, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, MaterialsUEVplus.TranscendentMetal, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NetherStar, 4L) },
                new FluidStack[] { fluid_0, fluid_1, fluid_2 },
                ItemList.Electric_Piston_UIV.get(1),
                crafting_time_in_ticks,
                crafting_eu_per_tick);

        // -------------------------------------------------------------

        // ------------------------ UIV Emitter ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Emitter_UEV.get(1L),
                total_computation,
                comp_per_second,
                research_eu_per_tick,
                research_amperage,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.TranscendentMetal, 1L),
                        ItemList.Electric_Motor_UIV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, MaterialsUEVplus.TranscendentMetal, 16L),
                        ItemList.Gravistar.get(32L), new Object[] { OrePrefixes.circuit.get(Materials.Optical), 4L },
                        getModItem(GTPlusPlus.ID, "itemFoilArceusAlloy2B", 64, 0),
                        getModItem(GTPlusPlus.ID, "itemFoilLafiumCompound", 64, 0),
                        getModItem(GTPlusPlus.ID, "itemFoilCinobiteA243", 64, 0),
                        getModItem(GTPlusPlus.ID, "itemFoilPikyonium64B", 64, 0),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NetherStar, 7L) },
                new FluidStack[] { fluid_0, fluid_1 },
                ItemList.Emitter_UIV.get(1L),
                crafting_time_in_ticks,
                crafting_eu_per_tick);

        // -------------------------------------------------------------

        // ------------------------ UIV Sensor ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Sensor_UEV.get(1L),
                total_computation,
                comp_per_second,
                research_eu_per_tick,
                research_amperage,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.TranscendentMetal, 1L),
                        ItemList.Electric_Motor_UIV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.TranscendentMetal, 8L),
                        ItemList.Gravistar.get(32), new Object[] { OrePrefixes.circuit.get(Materials.Optical), 4L },
                        getModItem(GTPlusPlus.ID, "itemFoilArceusAlloy2B", 64, 0),
                        getModItem(GTPlusPlus.ID, "itemFoilLafiumCompound", 64, 0),
                        getModItem(GTPlusPlus.ID, "itemFoilCinobiteA243", 64, 0),
                        getModItem(GTPlusPlus.ID, "itemFoilPikyonium64B", 64, 0),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NetherStar, 7L) },
                new FluidStack[] { fluid_0, fluid_1 },
                ItemList.Sensor_UIV.get(1L),
                crafting_time_in_ticks,
                crafting_eu_per_tick);

        // ---------------------------------------------------------------------

        // ------------------------ UIV Field Generator ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Field_Generator_UEV.get(1),
                total_computation,
                comp_per_second,
                research_eu_per_tick,
                research_amperage,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.TranscendentMetal, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.TranscendentMetal, 6L),
                        ItemList.Gravistar.get(16L), ItemList.Emitter_UIV.get(4L),
                        new Object[] { OrePrefixes.circuit.get(Materials.Piko), 4 },
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Infinity, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Infinity, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Infinity, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Infinity, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Infinity, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Infinity, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Infinity, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Infinity, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NetherStar, 8L) },
                new FluidStack[] { fluid_0, fluid_1 },
                ItemList.Field_Generator_UIV.get(1L),
                crafting_time_in_ticks,
                crafting_eu_per_tick);

        // ---------------------------------------------------------------------

    }

    private void itemPartsUMVAsslineRecipes() {

        // ----------------------------------------------------------------------
        // ------------------------- Set up information -------------------------
        // ----------------------------------------------------------------------

        Fluid mutated_living_solder = FluidRegistry.getFluid("molten.mutatedlivingsolder") != null
                ? FluidRegistry.getFluid("molten.mutatedlivingsolder")
                : FluidRegistry.getFluid("molten.solderingalloy");
        Fluid hypogen = FluidRegistry.getFluid("molten.hypogen");
        Fluid celestialTungsten = FluidRegistry.getFluid("molten.celestialtungsten");

        int total_computation = 192_000;
        int comp_per_second = 256;
        int research_eu_per_tick = 32_000_000;
        int research_amperage = 1;

        FluidStack fluid_0 = hypogen != null ? new FluidStack(hypogen, 576) : null;
        FluidStack fluid_1 = celestialTungsten != null ? new FluidStack(celestialTungsten, 576) : null;
        FluidStack fluid_2 = new FluidStack(mutated_living_solder, 2592);
        FluidStack fluid_3 = Materials.Lubricant.getFluid(4000);

        int crafting_time_in_ticks = 1000;
        int crafting_eu_per_tick = 32_000_000;

        // -------------------------------------------------------------

        // ------------------------- UMV Motor -------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Electric_Motor_UIV.get(1L),
                total_computation,
                comp_per_second,
                research_eu_per_tick,
                research_amperage,
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.TengamAttuned, 32L),
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, MaterialsUEVplus.SpaceTime, 16L),
                        GT_OreDictUnificator.get(OrePrefixes.ring, MaterialsUEVplus.SpaceTime, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.round, MaterialsUEVplus.SpaceTime, 32L),
                        getModItem(GTPlusPlus.ID, "itemFineWireHypogen", 64, 0),
                        getModItem(GTPlusPlus.ID, "itemFineWireHypogen", 64, 0),
                        getModItem(GTPlusPlus.ID, "itemFineWireHypogen", 64, 0),
                        getModItem(GTPlusPlus.ID, "itemFineWireHypogen", 64, 0),
                        getModItem(GTPlusPlus.ID, "itemFineWireHypogen", 64, 0),
                        getModItem(GTPlusPlus.ID, "itemFineWireHypogen", 64, 0),
                        getModItem(GTPlusPlus.ID, "itemFineWireHypogen", 64, 0),
                        getModItem(GTPlusPlus.ID, "itemFineWireHypogen", 64, 0),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Quantium, 2L) },
                new FluidStack[] { fluid_0, fluid_1, fluid_2, fluid_3 },
                ItemList.Electric_Motor_UMV.get(1L),
                crafting_time_in_ticks,
                crafting_eu_per_tick);

        // -------------------------------------------------------------

        // --------------------- UMV Electric Pump ---------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Electric_Pump_UIV.get(1L),
                total_computation,
                comp_per_second,
                research_eu_per_tick,
                research_amperage,
                new Object[] { ItemList.Electric_Motor_UMV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Infinity, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.SpaceTime, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.screw, MaterialsUEVplus.SpaceTime, 16L),
                        new Object[] { OrePrefixes.ring.get(Materials.AnySyntheticRubber), 64L },
                        GT_OreDictUnificator.get(OrePrefixes.rotor, MaterialsUEVplus.SpaceTime, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Quantium, 2L) },
                new FluidStack[] { fluid_0, fluid_1, fluid_2, fluid_3 },
                ItemList.Electric_Pump_UMV.get(1),
                crafting_time_in_ticks,
                crafting_eu_per_tick);

        // -------------------------------------------------------------

        // ----------------------- UMV Conveyor ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Conveyor_Module_UIV.get(1L),
                total_computation,
                comp_per_second,
                research_eu_per_tick,
                research_amperage,
                new Object[] { ItemList.Electric_Motor_UMV.get(2L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.SpaceTime, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.ring, MaterialsUEVplus.SpaceTime, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.round, MaterialsUEVplus.SpaceTime, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Quantium, 2L),
                        new Object[] { OrePrefixes.plate.get(Materials.AnySyntheticRubber), 64L },
                        new Object[] { OrePrefixes.plate.get(Materials.AnySyntheticRubber), 16L } },
                new FluidStack[] { fluid_0, fluid_1, fluid_2, fluid_3 },
                ItemList.Conveyor_Module_UMV.get(1),
                crafting_time_in_ticks,
                crafting_eu_per_tick);

        // -------------------------------------------------------------

        // -------------------- UMV Robot Arm --------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Robot_Arm_UIV.get(1L),
                total_computation,
                comp_per_second,
                research_eu_per_tick,
                research_amperage,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.stickLong, MaterialsUEVplus.SpaceTime, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.gear, MaterialsUEVplus.SpaceTime, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, MaterialsUEVplus.SpaceTime, 6L),
                        ItemList.Electric_Motor_UMV.get(2L), ItemList.Electric_Piston_UMV.get(1L),
                        new Object[] { OrePrefixes.circuit.get(Materials.Piko), 2L },
                        new Object[] { OrePrefixes.circuit.get(Materials.Optical), 4L },
                        new Object[] { OrePrefixes.circuit.get(Materials.Bio), 8L },
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Quantium, 6L) },
                new FluidStack[] { fluid_0, fluid_1, fluid_2, fluid_3 },
                ItemList.Robot_Arm_UMV.get(1L),
                crafting_time_in_ticks,
                crafting_eu_per_tick);

        // -------------------------------------------------------------

        // -------------------- UMV Electric Piston --------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Electric_Piston_UIV.get(1L),
                total_computation,
                comp_per_second,
                research_eu_per_tick,
                research_amperage,
                new ItemStack[] { ItemList.Electric_Motor_UMV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.SpaceTime, 6L),
                        GT_OreDictUnificator.get(OrePrefixes.ring, MaterialsUEVplus.SpaceTime, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.round, MaterialsUEVplus.SpaceTime, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, MaterialsUEVplus.SpaceTime, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.gear, MaterialsUEVplus.SpaceTime, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, MaterialsUEVplus.SpaceTime, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Quantium, 4L) },
                new FluidStack[] { fluid_0, fluid_1, fluid_2, fluid_3 },
                ItemList.Electric_Piston_UMV.get(1),
                crafting_time_in_ticks,
                crafting_eu_per_tick);

        // -------------------------------------------------------------

        // ------------------------ UMV Emitter ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Emitter_UIV.get(1L),
                total_computation,
                comp_per_second,
                research_eu_per_tick,
                research_amperage,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.SpaceTime, 1L),
                        ItemList.Electric_Motor_UMV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, MaterialsUEVplus.SpaceTime, 16L),
                        ItemList.Gravistar.get(64), new Object[] { OrePrefixes.circuit.get(Materials.Piko), 4L },
                        getModItem(GTPlusPlus.ID, "itemFoilCelestialTungsten", 64, 0),
                        getModItem(GTPlusPlus.ID, "itemFoilQuantum", 64, 0),
                        getModItem(GTPlusPlus.ID, "itemFoilAstralTitanium", 64, 0),
                        getModItem(GTPlusPlus.ID, "itemFoilTitansteel", 64, 0),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Quantium, 7L) },
                new FluidStack[] { fluid_0, fluid_1, fluid_2 },
                ItemList.Emitter_UMV.get(1L),
                crafting_time_in_ticks,
                crafting_eu_per_tick);

        // -------------------------------------------------------------

        // ------------------------ UMV Sensor ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Sensor_UIV.get(1L),
                total_computation,
                comp_per_second,
                research_eu_per_tick,
                research_amperage,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.SpaceTime, 1L),
                        ItemList.Electric_Motor_UMV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.SpaceTime, 8L),
                        ItemList.Gravistar.get(64), new Object[] { OrePrefixes.circuit.get(Materials.Piko), 4L },
                        getModItem(GTPlusPlus.ID, "itemFoilCelestialTungsten", 64, 0),
                        getModItem(GTPlusPlus.ID, "itemFoilQuantum", 64, 0),
                        getModItem(GTPlusPlus.ID, "itemFoilAstralTitanium", 64, 0),
                        getModItem(GTPlusPlus.ID, "itemFoilTitansteel", 64, 0),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Quantium, 7L) },
                new FluidStack[] { fluid_0, fluid_1, fluid_2 },
                ItemList.Sensor_UMV.get(1L),
                crafting_time_in_ticks,
                crafting_eu_per_tick);

        // ---------------------------------------------------------------------

        // ------------------------ UMV Field Generator ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Field_Generator_UIV.get(1),
                total_computation,
                comp_per_second,
                research_eu_per_tick,
                research_amperage,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.SpaceTime, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.SpaceTime, 6L),
                        ItemList.Gravistar.get(32L), ItemList.Emitter_UMV.get(4L),
                        new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 4 },
                        getModItem(GTPlusPlus.ID, "itemFineWireHypogen", 64, 0),
                        getModItem(GTPlusPlus.ID, "itemFineWireHypogen", 64, 0),
                        getModItem(GTPlusPlus.ID, "itemFineWireHypogen", 64, 0),
                        getModItem(GTPlusPlus.ID, "itemFineWireHypogen", 64, 0),
                        getModItem(GTPlusPlus.ID, "itemFineWireHypogen", 64, 0),
                        getModItem(GTPlusPlus.ID, "itemFineWireHypogen", 64, 0),
                        getModItem(GTPlusPlus.ID, "itemFineWireHypogen", 64, 0),
                        getModItem(GTPlusPlus.ID, "itemFineWireHypogen", 64, 0),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Quantium, 8L) },
                new FluidStack[] { fluid_0, fluid_1, fluid_2 },
                ItemList.Field_Generator_UMV.get(1L),
                crafting_time_in_ticks,
                crafting_eu_per_tick);

        // ---------------------------------------------------------------------

    }

    private void itemPartsUXVAsslineRecipes() {

        // ----------------------------------------------------------------------
        // ------------------------- Set up information -------------------------
        // ----------------------------------------------------------------------

        Fluid mutatedLivingSolder = FluidRegistry.getFluid("molten.mutatedlivingsolder") != null
                ? FluidRegistry.getFluid("molten.mutatedlivingsolder")
                : FluidRegistry.getFluid("molten.solderingalloy");

        FluidStack moltenMHDCSM_576 = MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(576);
        FluidStack moltenSpaceTime_576 = MaterialsUEVplus.SpaceTime.getMolten(576);
        FluidStack moltenUniversium_576 = MaterialsUEVplus.Universium.getMolten(576);
        FluidStack lubricantFluid_8000 = Materials.Lubricant.getFluid(8000);
        FluidStack solderingAlloy_14_400 = new FluidStack(mutatedLivingSolder, 14_400);

        int totalComputation = 384_000;
        int compPerSecond = 512;
        int researchEuPerTick = 64_000_000;
        int researchAmperage = 2;

        int craftingTimeInTicks = 2000;
        int craftingEuPerTick = (int) TierEU.RECIPE_UXV;

        // -------------------------------------------------------------

        // ------------------------- UXV Motor -------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Electric_Motor_UMV.get(1L),
                totalComputation,
                compPerSecond,
                researchEuPerTick,
                researchAmperage,
                new ItemStack[] { ItemList.EnergisedTesseract.get(1), GT_OreDictUnificator
                        .get(OrePrefixes.stickLong, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 16L),
                        GT_OreDictUnificator.get(
                                OrePrefixes.ring,
                                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
                                8L),
                        GT_OreDictUnificator.get(
                                OrePrefixes.round,
                                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
                                32L),

                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.SuperconductorUMVBase, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.SuperconductorUMVBase, 64L),

                        GT_OreDictUnificator.get(
                                OrePrefixes.wireFine,
                                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
                                64L),
                        GT_OreDictUnificator.get(
                                OrePrefixes.wireFine,
                                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
                                64L),

                        GT_OreDictUnificator.get(OrePrefixes.wireFine, MaterialsUEVplus.Universium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, MaterialsUEVplus.Universium, 64L),

                        GT_OreDictUnificator.get("wireFineShirabon", 64L),
                        GT_OreDictUnificator.get("wireFineShirabon", 64L),

                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, MaterialsUEVplus.SpaceTime, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Infinity, 2L),
                        Materials.Neutronium.getNanite(4) },
                new FluidStack[] { moltenMHDCSM_576, moltenSpaceTime_576, moltenUniversium_576, lubricantFluid_8000 },
                ItemList.Electric_Motor_UXV.get(1L),
                craftingTimeInTicks,
                craftingEuPerTick);

        // -------------------------------------------------------------

        // --------------------- UXV Electric Pump ---------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Electric_Pump_UMV.get(1L),
                totalComputation,
                compPerSecond,
                researchEuPerTick,
                researchAmperage,
                new Object[] { ItemList.Electric_Motor_UXV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.pipeLarge, MaterialsUEVplus.SpaceTime, 2L),
                        GT_OreDictUnificator.get(
                                OrePrefixes.plate,
                                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
                                4L),
                        GT_OreDictUnificator.get(
                                OrePrefixes.screw,
                                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
                                16L),
                        GT_OreDictUnificator.get(OrePrefixes.ring, MaterialsKevlar.Kevlar, 64L),
                        GT_OreDictUnificator.get("ringRadoxPoly", 64L),
                        GT_OreDictUnificator.get(
                                OrePrefixes.rotor,
                                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
                                4L),
                        GT_OreDictUnificator.get("rotorShirabon", 4),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, MaterialsUEVplus.SpaceTime, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Infinity, 2L),
                        Materials.Neutronium.getNanite(4) },
                new FluidStack[] { moltenMHDCSM_576, moltenSpaceTime_576, moltenUniversium_576, lubricantFluid_8000 },
                ItemList.Electric_Pump_UXV.get(1),
                craftingTimeInTicks,
                craftingEuPerTick);

        // -------------------------------------------------------------

        // ----------------------- UXV Conveyor ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Conveyor_Module_UMV.get(1L),
                totalComputation,
                compPerSecond,
                researchEuPerTick,
                researchAmperage,
                new Object[] { ItemList.Electric_Motor_UXV.get(2L), GT_OreDictUnificator
                        .get(OrePrefixes.plate, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 2L),
                        GT_OreDictUnificator.get(
                                OrePrefixes.ring,
                                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
                                8L),
                        GT_OreDictUnificator.get(
                                OrePrefixes.round,
                                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
                                64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, MaterialsUEVplus.SpaceTime, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Infinity, 2L),
                        MaterialsKevlar.Kevlar.getPlates(64), MaterialsKevlar.Kevlar.getPlates(16),
                        GT_OreDictUnificator.get("plateRadoxPoly", 64L),
                        GT_OreDictUnificator.get("plateRadoxPoly", 16L), Materials.Neutronium.getNanite(4) },
                new FluidStack[] { moltenMHDCSM_576, moltenSpaceTime_576, moltenUniversium_576, lubricantFluid_8000 },
                ItemList.Conveyor_Module_UXV.get(1),
                craftingTimeInTicks,
                craftingEuPerTick);

        // -------------------------------------------------------------

        // -------------------- UXV Robot Arm --------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Robot_Arm_UMV.get(1L),
                totalComputation,
                compPerSecond,
                researchEuPerTick,
                researchAmperage,
                new Object[] { GT_OreDictUnificator
                        .get(OrePrefixes.stickLong, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 8L),
                        GT_OreDictUnificator.get(
                                OrePrefixes.gear,
                                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
                                2L),
                        GT_OreDictUnificator.get("gearGtShirabon", 2L),
                        GT_OreDictUnificator.get(
                                OrePrefixes.gearGtSmall,
                                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
                                6L),
                        GT_OreDictUnificator.get("gearGtSmallShirabon", 6L), ItemList.Electric_Motor_UXV.get(2L),
                        ItemList.Electric_Piston_UXV.get(1L),
                        new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 2L },
                        new Object[] { OrePrefixes.circuit.get(Materials.Piko), 4L },
                        new Object[] { OrePrefixes.circuit.get(Materials.Optical), 8L },
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, MaterialsUEVplus.SpaceTime, 6L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Infinity, 6L),
                        Materials.Neutronium.getNanite(8) },
                new FluidStack[] { moltenMHDCSM_576, moltenSpaceTime_576, moltenUniversium_576, lubricantFluid_8000 },
                ItemList.Robot_Arm_UXV.get(1L),
                craftingTimeInTicks,
                craftingEuPerTick);

        // -------------------------------------------------------------

        // -------------------- UXV Electric Piston --------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Electric_Piston_UMV.get(1L),
                totalComputation,
                compPerSecond,
                researchEuPerTick,
                researchAmperage,
                new ItemStack[] { ItemList.Electric_Motor_UXV.get(1L), GT_OreDictUnificator
                        .get(OrePrefixes.plate, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 6L),
                        GT_OreDictUnificator.get(
                                OrePrefixes.ring,
                                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
                                8L),
                        GT_OreDictUnificator.get(
                                OrePrefixes.round,
                                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
                                64L),
                        GT_OreDictUnificator.get(
                                OrePrefixes.stick,
                                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
                                8L),
                        GT_OreDictUnificator.get(
                                OrePrefixes.gear,
                                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
                                2L),
                        GT_OreDictUnificator.get("gearGtShirabon", 2L),
                        GT_OreDictUnificator.get(
                                OrePrefixes.gearGtSmall,
                                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
                                4L),
                        GT_OreDictUnificator.get("gearGtSmallShirabon", 4L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, MaterialsUEVplus.SpaceTime, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Infinity, 4L),
                        Materials.Neutronium.getNanite(4) },
                new FluidStack[] { moltenMHDCSM_576, moltenSpaceTime_576, moltenUniversium_576, lubricantFluid_8000 },
                ItemList.Electric_Piston_UXV.get(1),
                craftingTimeInTicks,
                craftingEuPerTick);

        // -------------------------------------------------------------

        // ------------------------ UXV Emitter ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Emitter_UMV.get(1L),
                totalComputation,
                compPerSecond,
                researchEuPerTick,
                researchAmperage,
                new Object[] { GT_OreDictUnificator
                        .get(OrePrefixes.frameGt, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 1L),
                        ItemList.Electric_Motor_UXV.get(1L),
                        GT_OreDictUnificator.get(
                                OrePrefixes.stick,
                                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
                                16L),
                        ItemList.NuclearStar.get(16), new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 4L },
                        GT_OreDictUnificator.get(
                                OrePrefixes.foil,
                                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
                                64),
                        GT_OreDictUnificator.get("foilShirabon", 64),
                        GT_OreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.SpaceTime, 64),
                        GT_OreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.Universium, 64),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, MaterialsUEVplus.SpaceTime, 7L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Infinity, 7L),
                        Materials.Neutronium.getNanite(8)

                },
                new FluidStack[] { moltenMHDCSM_576, moltenSpaceTime_576, moltenUniversium_576, solderingAlloy_14_400 },
                ItemList.Emitter_UXV.get(1L),
                craftingTimeInTicks,
                craftingEuPerTick);

        // -------------------------------------------------------------

        // ------------------------ UXV Sensor ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Sensor_UMV.get(1L),
                totalComputation,
                compPerSecond,
                researchEuPerTick,
                researchAmperage,
                new Object[] { GT_OreDictUnificator
                        .get(OrePrefixes.frameGt, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 1L),
                        ItemList.Electric_Motor_UXV.get(1L),
                        GT_OreDictUnificator.get(
                                OrePrefixes.plate,
                                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
                                8L),
                        ItemList.NuclearStar.get(16), new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 4L },
                        GT_OreDictUnificator.get(
                                OrePrefixes.foil,
                                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
                                64),
                        GT_OreDictUnificator.get("foilShirabon", 64),
                        GT_OreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.SpaceTime, 64),
                        GT_OreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.Universium, 64),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, MaterialsUEVplus.SpaceTime, 7L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Infinity, 7L),
                        Materials.Neutronium.getNanite(8) },
                new FluidStack[] { moltenMHDCSM_576, moltenSpaceTime_576, moltenUniversium_576, solderingAlloy_14_400 },
                ItemList.Sensor_UXV.get(1L),
                craftingTimeInTicks,
                craftingEuPerTick);

        // ---------------------------------------------------------------------

        // ------------------------ UXV Field Generator ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Field_Generator_UMV.get(1),
                totalComputation,
                compPerSecond,
                researchEuPerTick,
                researchAmperage,
                new Object[] { GT_OreDictUnificator
                        .get(OrePrefixes.frameGt, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 1L),
                        GT_OreDictUnificator.get(
                                OrePrefixes.plate,
                                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
                                6L),
                        ItemList.NuclearStar.get(64L), ItemList.Emitter_UXV.get(4L),
                        new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 8 },

                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.SuperconductorUMVBase, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.SuperconductorUMVBase, 64L),
                        GT_OreDictUnificator.get(
                                OrePrefixes.wireFine,
                                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
                                64L),
                        GT_OreDictUnificator.get(
                                OrePrefixes.wireFine,
                                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
                                64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, MaterialsUEVplus.Universium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, MaterialsUEVplus.Universium, 64L),
                        GT_OreDictUnificator.get("wireFineShirabon", 64L),
                        GT_OreDictUnificator.get("wireFineShirabon", 64L),

                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, MaterialsUEVplus.SpaceTime, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Infinity, 8L),
                        Materials.Neutronium.getNanite(12) },
                new FluidStack[] { moltenMHDCSM_576, moltenSpaceTime_576, moltenUniversium_576, solderingAlloy_14_400 },
                ItemList.Field_Generator_UXV.get(1L),
                craftingTimeInTicks,
                craftingEuPerTick);

        // ---------------------------------------------------------------------

    }

    private void addEOHRecipes() {

        Fluid solderUEV = FluidRegistry.getFluid("molten.mutatedlivingsolder") != null
                ? FluidRegistry.getFluid("molten.mutatedlivingsolder")
                : FluidRegistry.getFluid("molten.solderingalloy");

        ItemStack largeShirabonPlate = TinkersGregworks.isModLoaded()
                ? TGregUtils.newItemStack(Materials.get("Shirabon"), PartTypes.LargePlate, 1)
                : GT_OreDictUnificator.get("plateDenseShirabon", 1);

        final FluidStack[] specialFluid = new FluidStack[] { MaterialsUEVplus.SpaceTime.getMolten(1_440),
                MaterialsUEVplus.SpaceTime.getMolten(1_440), MaterialsUEVplus.SpaceTime.getMolten(1_440),
                MaterialsUEVplus.SpaceTime.getMolten(1_440), MaterialsUEVplus.SpaceTime.getMolten(1_440),
                MaterialsUEVplus.SpaceTime.getMolten(1_440), MaterialsUEVplus.SpaceTime.getMolten(1_440),
                MaterialsUEVplus.SpaceTime.getMolten(1_440), MaterialsUEVplus.SpaceTime.getMolten(1_440) };

        final ItemStack[] plateList = new ItemStack[] {
                // Dense Shirabon plate.
                GT_OreDictUnificator.get("boltShirabon", 2),
                GT_OreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.WhiteDwarfMatter, 2),
                GT_OreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.WhiteDwarfMatter, 8),
                GT_OreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.WhiteDwarfMatter, 32),
                GT_OreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.BlackDwarfMatter, 2),
                GT_OreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.BlackDwarfMatter, 8),
                GT_OreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.BlackDwarfMatter, 32),
                GT_OreDictUnificator
                        .get(OrePrefixes.bolt, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 2),
                GT_OreDictUnificator
                        .get(OrePrefixes.bolt, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 8) };

        // EOH Controller Recipe.
        {
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Machine_Multi_PlasmaForge.get(1),
                    512_000_000, // total comp
                    2 * 16_384, // comp/s
                    (int) TierEU.RECIPE_MAX, // eu/t
                    64, // amperage
                    new Object[] {
                            // Space elevator controller.
                            getModItem(GregTech.ID, "gt.blockmachines", 16, 14003),
                            ItemList.Machine_Multi_PlasmaForge.get(4),

                            CustomItemList.EOH_Infinite_Energy_Casing.get(1),
                            CustomItemList.TimeAccelerationFieldGeneratorTier0.get(1),
                            CustomItemList.SpacetimeCompressionFieldGeneratorTier0.get(1),
                            CustomItemList.StabilisationFieldGeneratorTier0.get(1),

                            CustomItemList.Machine_Multi_Computer.get(64),
                            // Ultimate Time Anomaly.
                            getModItem(GregTech.ID, "gt.blockmachines", 64, 11107), ItemList.Quantum_Chest_IV.get(64),
                            // Void miner III.
                            getModItem(GregTech.ID, "gt.blockmachines", 64, 12739),

                            ItemList.Field_Generator_UMV.get(16), ItemList.Robot_Arm_UMV.get(16), ItemList.ZPM4.get(4),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUMV, 64) },
                    new FluidStack[] { MaterialsUEVplus.Time.getMolten(144_000),
                            MaterialsUEVplus.Space.getMolten(144_000),
                            FluidUtils.getFluidStack("molten.metastable oganesson", 144 * 256 * 4),
                            FluidUtils.getFluidStack("molten.shirabon", 144 * 256 * 4), },
                    CustomItemList.Machine_Multi_EyeOfHarmony.get(1),
                    1_000_000,
                    (int) TierEU.RECIPE_UMV);
        }

        // EOH Spatial Individual Casing
        {
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    // Dyson Swarm Module Deployment Unit Base Casing
                    getModItem(GalaxySpace.ID, "dysonswarmparts", 1, 2),
                    256_000_000, // total comp
                    16_384, // comp/s
                    (int) TierEU.RECIPE_MAX, // eu/t
                    32, // amperage
                    new Object[] {
                            // Space elevator blocks.
                            getModItem(GTNHIntergalactic.ID, "gt.blockcasingsSE", 64, 0),
                            // Cosmic neutronium block.
                            getModItem(Avaritia.ID, "Resource_Block", 64, 0),
                            GT_OreDictUnificator.get(OrePrefixes.block, Materials.Neutronium, 64),
                            GT_OreDictUnificator.get(OrePrefixes.nanite, Materials.Neutronium, 48),

                            // Large Bedrockium Plate
                            getModItem(TinkerConstruct.ID, "heavyPlate", 1, 315),
                            // Large Cosmic Neutronium Plate
                            getModItem(TinkerConstruct.ID, "heavyPlate", 1, 1671),
                            // Large Shirabon Plate
                            largeShirabonPlate,
                            // Large Infinity Plate
                            getModItem(TinkerConstruct.ID, "heavyPlate", 1, 1669),

                            // UV Solar panel
                            getModItem(SuperSolarPanels.ID, "PhotonicSolarPanel", 1, 0),
                            ItemList.Quantum_Chest_IV.get(1),
                            // Gravitation Engine
                            getModItem(GraviSuite.ID, "itemSimpleItem", 64, 3), ItemList.EnergisedTesseract.get(1) },

                    new FluidStack[] { Materials.Neutronium.getMolten(144 * 256 * 4),
                            Materials.CosmicNeutronium.getMolten(144 * 256 * 4),
                            new FluidStack(solderUEV, 144 * 256 * 2), MaterialsUEVplus.Space.getMolten(1_440) },
                    CustomItemList.EOH_Reinforced_Spatial_Casing.get(4),
                    10_000,
                    (int) TierEU.RECIPE_UMV);
        }

        // EOH Spacetime Compression
        {
            // ME Digital singularity.
            final ItemStack ME_Singularity = getModItem(
                    "appliedenergistics2",
                    "item.ItemExtremeStorageCell.Singularity",
                    1);
            final ItemStack baseCasing = CustomItemList.EOH_Reinforced_Spatial_Casing.get(1);

            int baseCompPerSec = 16_384;

            int set;
            int tier;
            int absoluteTier;

            // T0 - Shirabon
            // T1 - White Dwarf Matter
            // T2 - White Dwarf Matter
            // T3 - White Dwarf Matter
            // T4 - Black Dwarf Matter
            // T5 - Black Dwarf Matter
            // T6 - Black Dwarf Matter
            // T7 - Black Dwarf Matter
            // T8 - MHDCSM.

            {
                tier = 1;
                set = 1;

                absoluteTier = 0;
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                        CustomItemList.EOH_Reinforced_Spatial_Casing.get(1),
                        (absoluteTier + 1) * 48_000_000, // total comp
                        (absoluteTier + 1) * baseCompPerSec, // comp/s
                        (int) TierEU.RECIPE_MAX, // eu/t
                        (absoluteTier + 1) * 8, // amperage
                        new Object[] { baseCasing,
                                // T7 Yotta cell.
                                getModItem(GoodGenerator.ID, "yottaFluidTankCells", tier, (5 + set)),
                                // quantum tank V (max tier)
                                ItemList.Quantum_Tank_IV.get(4 * (1 + absoluteTier)),
                                // Inf chest
                                getModItem(AvaritiaAddons.ID, "InfinityChest", absoluteTier + 1),
                                // Cosmic fabric manipulator
                                getModItem(GTPlusPlus.ID, "gtplusplus.blockcasings.5", tier, 8), ME_Singularity,
                                plateList[absoluteTier], getItemContainer("QuantumCircuit").get(set) },
                        new FluidStack[] { new FluidStack(solderUEV, (int) (2_880 * pow(2L, absoluteTier))),
                                MaterialsUEVplus.Space.getMolten(1_440 * (absoluteTier + 1)),
                                specialFluid[absoluteTier] },
                        CustomItemList.SpacetimeCompressionFieldGeneratorTier0.get(1),
                        (absoluteTier + 1) * 4_000 * 20,
                        (int) TierEU.RECIPE_UMV);

                tier++;
                absoluteTier = 1;
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                        CustomItemList.SpacetimeCompressionFieldGeneratorTier0.get(1),
                        (absoluteTier + 1) * 48_000_000, // total comp
                        (absoluteTier + 1) * baseCompPerSec, // comp/s
                        (int) TierEU.RECIPE_MAX, // eu/t
                        (absoluteTier + 1) * 8, // amperage
                        new Object[] { baseCasing,
                                // T7 Yotta cell.
                                getModItem(GoodGenerator.ID, "yottaFluidTankCells", tier, (5 + set)),
                                // quantum tank V (max tier)
                                ItemList.Quantum_Tank_IV.get(4 * (1 + absoluteTier)),
                                // Inf chest
                                getModItem(AvaritiaAddons.ID, "InfinityChest", absoluteTier + 1),
                                // Cosmic fabric manipulator
                                getModItem(GTPlusPlus.ID, "gtplusplus.blockcasings.5", tier, 8), ME_Singularity,
                                ME_Singularity, plateList[absoluteTier], getItemContainer("QuantumCircuit").get(set) },
                        new FluidStack[] { new FluidStack(solderUEV, (int) (2_880 * pow(2L, absoluteTier))),
                                MaterialsUEVplus.Space.getMolten(1_440 * (absoluteTier + 1)),
                                specialFluid[absoluteTier] },
                        CustomItemList.SpacetimeCompressionFieldGeneratorTier1.get(1),
                        (absoluteTier + 1) * 4_000 * 20,
                        (int) TierEU.RECIPE_UMV);

                tier++;
                absoluteTier = 2;
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                        CustomItemList.SpacetimeCompressionFieldGeneratorTier1.get(1),
                        (absoluteTier + 1) * 48_000_000, // total comp
                        (absoluteTier + 1) * baseCompPerSec, // comp/s
                        (int) TierEU.RECIPE_MAX, // eu/t
                        (absoluteTier + 1) * 8, // amperage
                        new Object[] { baseCasing,
                                // T7 Yotta cell.
                                getModItem(GoodGenerator.ID, "yottaFluidTankCells", tier, (5 + set)),
                                // quantum tank V (max tier)
                                ItemList.Quantum_Tank_IV.get(4 * (1 + absoluteTier)),
                                // Inf chest
                                getModItem(AvaritiaAddons.ID, "InfinityChest", absoluteTier + 1),
                                // Cosmic fabric manipulator
                                getModItem(GTPlusPlus.ID, "gtplusplus.blockcasings.5", tier, 8), ME_Singularity,
                                ME_Singularity, ME_Singularity, plateList[absoluteTier],
                                getItemContainer("QuantumCircuit").get(set) },
                        new FluidStack[] { new FluidStack(solderUEV, (int) (2_880 * pow(2L, absoluteTier))),
                                MaterialsUEVplus.Space.getMolten(1_440 * (absoluteTier + 1)),
                                specialFluid[absoluteTier], },
                        CustomItemList.SpacetimeCompressionFieldGeneratorTier2.get(1),
                        (absoluteTier + 1) * 4_000 * 20,
                        (int) TierEU.RECIPE_UMV);
            }

            {
                tier = 1;
                set = 2;
                absoluteTier = 3;
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                        CustomItemList.SpacetimeCompressionFieldGeneratorTier2.get(1),
                        (absoluteTier + 1) * 48_000_000, // total comp
                        (absoluteTier + 1) * baseCompPerSec, // comp/s
                        (int) TierEU.RECIPE_MAX, // eu/t
                        (absoluteTier + 1) * 8, // amperage
                        new Object[] { baseCasing,
                                // T8 Yotta cell.
                                getModItem(GoodGenerator.ID, "yottaFluidTankCells", tier, (5 + set)),
                                // quantum tank V (max tier)
                                ItemList.Quantum_Tank_IV.get(4 * (1 + absoluteTier)),
                                // Inf chest
                                getModItem(AvaritiaAddons.ID, "InfinityChest", absoluteTier + 1),
                                // Infinity infused manipulator
                                getModItem(GTPlusPlus.ID, "gtplusplus.blockcasings.5", tier, 9), ME_Singularity,
                                ME_Singularity, ME_Singularity, ME_Singularity, plateList[absoluteTier],
                                getItemContainer("QuantumCircuit").get(set) },
                        new FluidStack[] { new FluidStack(solderUEV, (int) (2_880 * pow(2L, absoluteTier))),
                                MaterialsUEVplus.Space.getMolten(1_440 * (absoluteTier + 1)),
                                specialFluid[absoluteTier], },
                        CustomItemList.SpacetimeCompressionFieldGeneratorTier3.get(1),
                        (absoluteTier + 1) * 4_000 * 20,
                        (int) TierEU.RECIPE_UMV);

                tier++;
                absoluteTier = 4;
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                        CustomItemList.SpacetimeCompressionFieldGeneratorTier3.get(1),
                        (absoluteTier + 1) * 48_000_000, // total comp
                        (absoluteTier + 1) * baseCompPerSec, // comp/s
                        (int) TierEU.RECIPE_MAX, // eu/t
                        (absoluteTier + 1) * 8, // amperage
                        new Object[] { baseCasing,
                                // T8 Yotta cell.
                                getModItem(GoodGenerator.ID, "yottaFluidTankCells", tier, (5 + set)),
                                // quantum tank V (max tier)
                                ItemList.Quantum_Tank_IV.get(4 * (1 + absoluteTier)),
                                // Inf chest
                                getModItem(AvaritiaAddons.ID, "InfinityChest", absoluteTier + 1),
                                // Infinity infused manipulator
                                getModItem(GTPlusPlus.ID, "gtplusplus.blockcasings.5", tier, 9), ME_Singularity,
                                ME_Singularity, ME_Singularity, ME_Singularity, ME_Singularity, plateList[absoluteTier],
                                getItemContainer("QuantumCircuit").get(set) },
                        new FluidStack[] { new FluidStack(solderUEV, (int) (2_880 * pow(2L, absoluteTier))),
                                MaterialsUEVplus.Space.getMolten(1_440 * (absoluteTier + 1)),
                                specialFluid[absoluteTier], },
                        CustomItemList.SpacetimeCompressionFieldGeneratorTier4.get(1),
                        (absoluteTier + 1) * 4_000 * 20,
                        (int) TierEU.RECIPE_UMV);

                tier++;
                absoluteTier = 5;
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                        CustomItemList.SpacetimeCompressionFieldGeneratorTier4.get(1),
                        (absoluteTier + 1) * 48_000_000, // total comp
                        (absoluteTier + 1) * baseCompPerSec, // comp/s
                        (int) TierEU.RECIPE_MAX, // eu/t
                        (absoluteTier + 1) * 8, // amperage
                        new Object[] { baseCasing,
                                // T8 Yotta cell.
                                getModItem(GoodGenerator.ID, "yottaFluidTankCells", tier, (5 + set)),
                                // quantum tank V (max tier)
                                ItemList.Quantum_Tank_IV.get(4 * (1 + absoluteTier)),
                                // Inf chest
                                getModItem(AvaritiaAddons.ID, "InfinityChest", absoluteTier + 1),
                                // Infinity infused manipulator
                                getModItem(GTPlusPlus.ID, "gtplusplus.blockcasings.5", tier, 9), ME_Singularity,
                                ME_Singularity, ME_Singularity, ME_Singularity, ME_Singularity, ME_Singularity,
                                plateList[absoluteTier], getItemContainer("QuantumCircuit").get(set) },
                        new FluidStack[] { new FluidStack(solderUEV, (int) (2_880 * pow(2L, absoluteTier))),
                                MaterialsUEVplus.Space.getMolten(1_440 * (absoluteTier + 1)),
                                specialFluid[absoluteTier], },
                        CustomItemList.SpacetimeCompressionFieldGeneratorTier5.get(1),
                        (absoluteTier + 1) * 4_000 * 20,
                        (int) TierEU.RECIPE_UMV);
            }

            {
                tier = 1;
                set = 3;
                absoluteTier = 6;
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                        CustomItemList.SpacetimeCompressionFieldGeneratorTier5.get(1),
                        (absoluteTier + 1) * 48_000_000, // total comp
                        (absoluteTier + 1) * baseCompPerSec, // comp/s
                        (int) TierEU.RECIPE_MAX, // eu/t
                        (absoluteTier + 1) * 8, // amperage
                        new Object[] { baseCasing,
                                // T9 Yotta cell.
                                getModItem(GoodGenerator.ID, "yottaFluidTankCells", tier, (5 + set)),
                                // quantum tank V (max tier)
                                ItemList.Quantum_Tank_IV.get(4 * (1 + absoluteTier)),
                                // Inf chest
                                getModItem(AvaritiaAddons.ID, "InfinityChest", absoluteTier + 1),
                                // Spacetime continuum ripper
                                getModItem(GTPlusPlus.ID, "gtplusplus.blockcasings.5", tier, 10), ME_Singularity,
                                ME_Singularity, ME_Singularity, ME_Singularity, ME_Singularity, ME_Singularity,
                                ME_Singularity, plateList[absoluteTier], getItemContainer("QuantumCircuit").get(set) },
                        new FluidStack[] { new FluidStack(solderUEV, (int) (2_880 * pow(2L, absoluteTier))),
                                MaterialsUEVplus.Space.getMolten(1_440 * (absoluteTier + 1)),
                                specialFluid[absoluteTier], },
                        CustomItemList.SpacetimeCompressionFieldGeneratorTier6.get(1),
                        (absoluteTier + 1) * 4_000 * 20,
                        (int) TierEU.RECIPE_UMV);

                tier++;
                absoluteTier = 7;
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                        CustomItemList.SpacetimeCompressionFieldGeneratorTier6.get(1),
                        (absoluteTier + 1) * 48_000_000, // total comp
                        (absoluteTier + 1) * baseCompPerSec, // comp/s
                        (int) TierEU.RECIPE_MAX, // eu/t
                        (absoluteTier + 1) * 8, // amperage
                        new Object[] { baseCasing,
                                // T9 Yotta cell.
                                getModItem(GoodGenerator.ID, "yottaFluidTankCells", tier, (5 + set)),
                                // quantum tank V (max tier)
                                ItemList.Quantum_Tank_IV.get(4 * (1 + absoluteTier)),
                                // Inf chest
                                getModItem(AvaritiaAddons.ID, "InfinityChest", absoluteTier + 1),
                                // Spacetime continuum ripper
                                getModItem(GTPlusPlus.ID, "gtplusplus.blockcasings.5", tier, 10), ME_Singularity,
                                ME_Singularity, ME_Singularity, ME_Singularity, ME_Singularity, ME_Singularity,
                                ME_Singularity, ME_Singularity, plateList[absoluteTier],
                                getItemContainer("QuantumCircuit").get(set) },
                        new FluidStack[] { new FluidStack(solderUEV, (int) (2_880 * pow(2L, absoluteTier))),
                                MaterialsUEVplus.Space.getMolten(1_440 * (absoluteTier + 1)),
                                specialFluid[absoluteTier], },
                        CustomItemList.SpacetimeCompressionFieldGeneratorTier7.get(1),
                        (absoluteTier + 1) * 4_000 * 20,
                        (int) TierEU.RECIPE_UMV);

                tier++;
                absoluteTier = 8;
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                        CustomItemList.SpacetimeCompressionFieldGeneratorTier7.get(1),
                        (absoluteTier + 1) * 48_000_000, // total comp
                        (absoluteTier + 1) * baseCompPerSec, // comp/s
                        (int) TierEU.RECIPE_MAX, // eu/t
                        (absoluteTier + 1) * 8, // amperage
                        new Object[] { baseCasing,
                                // T9 Yotta cell.
                                getModItem(GoodGenerator.ID, "yottaFluidTankCells", tier, (5 + set)),
                                // quantum tank V (max tier)
                                ItemList.Quantum_Tank_IV.get(4 * (1 + absoluteTier)),
                                // Inf chest
                                getModItem(AvaritiaAddons.ID, "InfinityChest", absoluteTier + 1),
                                // Spacetime continuum ripper
                                getModItem(GTPlusPlus.ID, "gtplusplus.blockcasings.5", tier, 10), ME_Singularity,
                                ME_Singularity, ME_Singularity, ME_Singularity, ME_Singularity, ME_Singularity,
                                ME_Singularity, ME_Singularity, ME_Singularity, plateList[absoluteTier],
                                getItemContainer("QuantumCircuit").get(set) },
                        new FluidStack[] { new FluidStack(solderUEV, (int) (2_880 * pow(2L, absoluteTier))),
                                MaterialsUEVplus.Space.getMolten(1_440 * (absoluteTier + 1)),
                                specialFluid[absoluteTier], },
                        CustomItemList.SpacetimeCompressionFieldGeneratorTier8.get(1),
                        (absoluteTier + 1) * 4_000 * 20,
                        (int) TierEU.RECIPE_UMV);
            }
        }

        // EOH Time Dilation Field Generators.
        {
            final ItemStack baseCasing = CustomItemList.EOH_Reinforced_Temporal_Casing.get(1);

            int baseCompPerSec = 16_384;

            // T0 - Shirabon
            // T1 - White Dwarf Matter
            // T2 - White Dwarf Matter
            // T3 - White Dwarf Matter
            // T4 - Black Dwarf Matter
            // T5 - Black Dwarf Matter
            // T6 - Black Dwarf Matter
            // T7 - Black Dwarf Matter
            // T8 - MHDCSM.

            final ItemStack[] fusionReactors = new ItemStack[] { ItemList.FusionComputer_ZPMV.get(1),
                    ItemList.FusionComputer_ZPMV.get(2), ItemList.FusionComputer_ZPMV.get(3),
                    ItemList.FusionComputer_UV.get(1), ItemList.FusionComputer_UV.get(2),
                    ItemList.FusionComputer_UV.get(3),
                    // MK4 Fusion Computer.
                    getModItem(GregTech.ID, "gt.blockmachines", 1, 965),
                    getModItem(GregTech.ID, "gt.blockmachines", 2, 965),
                    getModItem(GregTech.ID, "gt.blockmachines", 3, 965) };

            final ItemStack[] fusionCoils = new ItemStack[] { getModItem(GoodGenerator.ID, "compactFusionCoil", 1, 1),
                    getModItem(GoodGenerator.ID, "compactFusionCoil", 2, 1),
                    getModItem(GoodGenerator.ID, "compactFusionCoil", 3, 1),
                    getModItem(GoodGenerator.ID, "compactFusionCoil", 1, 2),
                    getModItem(GoodGenerator.ID, "compactFusionCoil", 2, 2),
                    getModItem(GoodGenerator.ID, "compactFusionCoil", 3, 2),
                    getModItem(GoodGenerator.ID, "compactFusionCoil", 1, 3),
                    getModItem(GoodGenerator.ID, "compactFusionCoil", 2, 3),
                    getModItem(GoodGenerator.ID, "compactFusionCoil", 3, 3) };

            final ItemStack[] researchStuff = new ItemStack[] { baseCasing,
                    CustomItemList.TimeAccelerationFieldGeneratorTier0.get(1),
                    CustomItemList.TimeAccelerationFieldGeneratorTier1.get(1),
                    CustomItemList.TimeAccelerationFieldGeneratorTier2.get(1),
                    CustomItemList.TimeAccelerationFieldGeneratorTier3.get(1),
                    CustomItemList.TimeAccelerationFieldGeneratorTier4.get(1),
                    CustomItemList.TimeAccelerationFieldGeneratorTier5.get(1),
                    CustomItemList.TimeAccelerationFieldGeneratorTier6.get(1),
                    CustomItemList.TimeAccelerationFieldGeneratorTier7.get(1),
                    CustomItemList.TimeAccelerationFieldGeneratorTier8.get(1) };

            for (int absoluteTier = 0; absoluteTier < 9; absoluteTier++) {

                TT_recipeAdder.addResearchableAssemblylineRecipe(
                        researchStuff[absoluteTier],
                        (absoluteTier + 1) * 48_000_000, // total comp
                        (absoluteTier + 1) * baseCompPerSec, // comp/s
                        (int) TierEU.RECIPE_MAX, // eu/t
                        (absoluteTier + 1) * 8, // amperage
                        new Object[] { baseCasing, fusionReactors[absoluteTier], fusionCoils[absoluteTier],
                                // UV Solar panel
                                getModItem(SuperSolarPanels.ID, "PhotonicSolarPanel", absoluteTier + 1, 0),

                                getItemContainer("QuantumCircuit").get(absoluteTier + 1),
                                // Red Spectral Component
                                getModItem(SuperSolarPanels.ID, "redcomponent", 64),
                                // Green Spectral Component
                                getModItem(SuperSolarPanels.ID, "greencomponent", 64),
                                // Blue Spectral Component
                                getModItem(SuperSolarPanels.ID, "bluecomponent", 64),

                                plateList[absoluteTier],
                                // Dyson Swarm Module Deployment Unit Base Casing
                                getModItem(GalaxySpace.ID, "dysonswarmparts", (absoluteTier + 1) * 4, 2),
                                // Dyson Swarm Energy Receiver Dish Block
                                getModItem(GalaxySpace.ID, "dysonswarmparts", (absoluteTier + 1) * 4, 1),
                                // Ultimate Time Anomaly.
                                getModItem(GregTech.ID, "gt.blockmachines", (absoluteTier + 1) * 4, 11107),

                                ItemList.Energy_Module.get(absoluteTier + 1), GT_OreDictUnificator
                                        .get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, (absoluteTier + 1) * 4),

                        },
                        new FluidStack[] { new FluidStack(solderUEV, (int) (2_880 * pow(2L, absoluteTier))),
                                MaterialsUEVplus.Time.getMolten(1_440 * (absoluteTier + 1)),
                                specialFluid[absoluteTier] },
                        researchStuff[absoluteTier + 1],
                        (absoluteTier + 1) * 4_000 * 20,
                        (int) TierEU.RECIPE_UMV);
            }

        }

        // EOH Stabilisation Field Generators.
        {
            final ItemStack baseCasing = CustomItemList.EOH_Infinite_Energy_Casing.get(1);

            int baseCompPerSec = 16_384;

            // T0 - Shirabon
            // T1 - White Dwarf Matter
            // T2 - White Dwarf Matter
            // T3 - White Dwarf Matter
            // T4 - Black Dwarf Matter
            // T5 - Black Dwarf Matter
            // T6 - Black Dwarf Matter
            // T7 - Black Dwarf Matter
            // T8 - MHDCSM.

            final ItemStack[] researchStuff = new ItemStack[] { baseCasing,
                    CustomItemList.StabilisationFieldGeneratorTier0.get(1),
                    CustomItemList.StabilisationFieldGeneratorTier1.get(1),
                    CustomItemList.StabilisationFieldGeneratorTier2.get(1),
                    CustomItemList.StabilisationFieldGeneratorTier3.get(1),
                    CustomItemList.StabilisationFieldGeneratorTier4.get(1),
                    CustomItemList.StabilisationFieldGeneratorTier5.get(1),
                    CustomItemList.StabilisationFieldGeneratorTier6.get(1),
                    CustomItemList.StabilisationFieldGeneratorTier7.get(1),
                    CustomItemList.StabilisationFieldGeneratorTier8.get(1) };

            final ItemStack[] timeCasings = new ItemStack[] { CustomItemList.TimeAccelerationFieldGeneratorTier0.get(1),
                    CustomItemList.TimeAccelerationFieldGeneratorTier1.get(1),
                    CustomItemList.TimeAccelerationFieldGeneratorTier2.get(1),
                    CustomItemList.TimeAccelerationFieldGeneratorTier3.get(1),
                    CustomItemList.TimeAccelerationFieldGeneratorTier4.get(1),
                    CustomItemList.TimeAccelerationFieldGeneratorTier5.get(1),
                    CustomItemList.TimeAccelerationFieldGeneratorTier6.get(1),
                    CustomItemList.TimeAccelerationFieldGeneratorTier7.get(1),
                    CustomItemList.TimeAccelerationFieldGeneratorTier8.get(1) };

            final ItemStack[] spatialCasings = new ItemStack[] {
                    CustomItemList.SpacetimeCompressionFieldGeneratorTier0.get(1),
                    CustomItemList.SpacetimeCompressionFieldGeneratorTier1.get(1),
                    CustomItemList.SpacetimeCompressionFieldGeneratorTier2.get(1),
                    CustomItemList.SpacetimeCompressionFieldGeneratorTier3.get(1),
                    CustomItemList.SpacetimeCompressionFieldGeneratorTier4.get(1),
                    CustomItemList.SpacetimeCompressionFieldGeneratorTier5.get(1),
                    CustomItemList.SpacetimeCompressionFieldGeneratorTier6.get(1),
                    CustomItemList.SpacetimeCompressionFieldGeneratorTier7.get(1),
                    CustomItemList.SpacetimeCompressionFieldGeneratorTier8.get(1) };

            for (int absoluteTier = 0; absoluteTier < 9; absoluteTier++) {

                // spotless:off
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                        researchStuff[absoluteTier],
                        (absoluteTier + 1) * 48_000_000, // total comp
                        (absoluteTier + 1) * baseCompPerSec, // comp/s
                        (int) TierEU.RECIPE_MAX, // eu/t
                        (absoluteTier + 1) * 8, // amperage
                        new Object[] {
                                timeCasings[absoluteTier],
                                spatialCasings[absoluteTier],
                                baseCasing,
                                // Dyson Swarm Module.
                                getModItem(GalaxySpace.ID, "item.DysonSwarmParts", 4 * (absoluteTier + 1), 0),

                                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.SuperconductorUMVBase, 4 * (absoluteTier + 1)),
                                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.SuperconductorUIVBase, 4 * (absoluteTier + 1)),
                                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.SuperconductorUEVBase, 4 * (absoluteTier + 1)),
                                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Longasssuperconductornameforuhvwire, 4 * (absoluteTier + 1)),

                                // Gravitation Engine
                                getModItem(GraviSuite.ID, "itemSimpleItem", 64, 3),
                                getModItem(GraviSuite.ID, "itemSimpleItem", 64, 3),
                                getModItem(GraviSuite.ID, "itemSimpleItem", 64, 3),
                                getModItem(GraviSuite.ID, "itemSimpleItem", 64, 3),

                                plateList[absoluteTier],
                                getItemContainer("QuantumCircuit").get(2 * (absoluteTier + 1)),
                                GT_OreDictUnificator.get(OrePrefixes.gearGt, MaterialsUEVplus.SpaceTime, absoluteTier + 1),
                                GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, MaterialsUEVplus.SpaceTime, absoluteTier + 1)


                        },
                        new FluidStack[] { new FluidStack(solderUEV, (int) (2_880 * pow(2L, absoluteTier))),
                                MaterialsUEVplus.Time.getMolten(1_440 * (absoluteTier + 1)),
                                MaterialsUEVplus.Space.getMolten(1_440 * (absoluteTier + 1)),
                                specialFluid[absoluteTier] },
                        researchStuff[absoluteTier + 1],
                        (absoluteTier + 1) * 4_000 * 20,
                        (int) TierEU.RECIPE_UMV);
                // spotless:on
            }

        }

        // EOH Reinforced Temporal casings
        {
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    // Ultimate Time Anomaly.
                    getModItem(GregTech.ID, "gt.blockmachines", 1, 11107),
                    256_000_000, // total comp
                    16_384, // comp/s
                    (int) TierEU.RECIPE_MAX, // eu/t
                    32, // amperage
                    new Object[] {
                            // Space elevator blocks.
                            getModItem(GTNHIntergalactic.ID, "gt.blockcasingsSE", 64, 0),
                            // Cosmic neutronium block.
                            getModItem(Avaritia.ID, "Resource_Block", 64, 0),
                            GT_OreDictUnificator.get(OrePrefixes.block, Materials.Neutronium, 64),
                            GT_OreDictUnificator.get(OrePrefixes.nanite, Materials.Neutronium, 48),

                            // Large Bedrockium Plate
                            getModItem(TinkerConstruct.ID, "heavyPlate", 1, 315),
                            // Large Cosmic Neutronium Plate
                            getModItem(TinkerConstruct.ID, "heavyPlate", 1, 1671),
                            // Large Shirabon Plate
                            largeShirabonPlate,
                            // Large Infinity Plate
                            getModItem(TinkerConstruct.ID, "heavyPlate", 1, 1669),

                            // UV Solar panel
                            getModItem(SuperSolarPanels.ID, "PhotonicSolarPanel", 1, 0),
                            // Ultimate Time Anomaly.
                            getModItem(GregTech.ID, "gt.blockmachines", 4, 11107),
                            // Gravitation Engine.
                            getModItem(GraviSuite.ID, "itemSimpleItem", 64, 3), ItemList.EnergisedTesseract.get(1) },

                    new FluidStack[] { Materials.Neutronium.getMolten(144 * 256 * 4),
                            Materials.CosmicNeutronium.getMolten(144 * 256 * 4),
                            new FluidStack(solderUEV, 144 * 256 * 2), MaterialsUEVplus.Time.getMolten(1_440) },
                    CustomItemList.EOH_Reinforced_Temporal_Casing.get(4),
                    10_000,
                    (int) TierEU.RECIPE_UMV);
        }

        // EOH Infinite Spacetime Energy Boundary Casing
        {
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUMV, 1),
                    256_000_000, // total comp
                    16_384, // comp/s
                    (int) TierEU.RECIPE_MAX, // eu/t
                    32, // amperage
                    new Object[] { getModItem(GregTech.ID, "gt.blockmachines", 1, 13106),
                            // UV Solar panel
                            getModItem(SuperSolarPanels.ID, "PhotonicSolarPanel", 1, 0),
                            // UHV Capacitor block
                            getModItem(KekzTech.ID, "kekztech_lapotronicenergyunit_block", 1, 5),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUMV, 4),

                            CustomItemList.Machine_Multi_Transformer.get(16), ItemList.Wireless_Hatch_Energy_UMV.get(4),
                            CustomItemList.eM_energyTunnel5_UMV.get(1),
                            // High Energy Flow Circuit.
                            getModItem(NewHorizonsCoreMod.ID, "item.HighEnergyFlowCircuit", 64, 0),

                            // Metastable Oganesson Plate.
                            GT_OreDictUnificator.get("plateMetastableOganesson", 6),
                            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BlueTopaz, 6),
                            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CallistoIce, 6),
                            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Ledox, 6),

                            // Metastable Oganesson Screw.
                            GT_OreDictUnificator.get("screwMetastableOganesson", 6),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.BlueTopaz, 6),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.CallistoIce, 6),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Ledox, 6), },

                    new FluidStack[] { Materials.Neutronium.getMolten(144 * 256 * 16),
                            Materials.CosmicNeutronium.getMolten(144 * 256 * 16),
                            new FluidStack(solderUEV, 144 * 256 * 8), MaterialsUEVplus.SpaceTime.getMolten(16_000) },
                    CustomItemList.EOH_Infinite_Energy_Casing.get(1),
                    10_000,
                    (int) TierEU.RECIPE_UMV);
        }

    }

    private void addWirelessEnergyRecipes() {

        Fluid solderUEV = FluidRegistry.getFluid("molten.mutatedlivingsolder") != null
                ? FluidRegistry.getFluid("molten.mutatedlivingsolder")
                : FluidRegistry.getFluid("molten.solderingalloy");

        int recipeDurationTicks = 20 * 20;
        int recipeEuPerTick = 128_000_000;

        int researchEuPerTick = 128_000_000;
        int researchAmperage = 4;
        int compPerSecond = 2000;
        int totalComputation = 500_000;

        ItemStack[] energyHatches = { ItemList.Hatch_Energy_ULV.get(1), ItemList.Hatch_Energy_LV.get(1),
                ItemList.Hatch_Energy_MV.get(1), ItemList.Hatch_Energy_HV.get(1), ItemList.Hatch_Energy_EV.get(1),
                ItemList.Hatch_Energy_IV.get(1), ItemList.Hatch_Energy_LuV.get(1), ItemList.Hatch_Energy_ZPM.get(1),
                ItemList.Hatch_Energy_UV.get(1), ItemList.Hatch_Energy_MAX.get(1),
                getItemContainer("Hatch_Energy_UEV").get(1L), getItemContainer("Hatch_Energy_UIV").get(1L),
                getItemContainer("Hatch_Energy_UMV").get(1L), getItemContainer("Hatch_Energy_UXV").get(1L) };

        ItemStack[] energyHatches_4A = { CustomItemList.eM_energyMulti4_EV.get(1),
                CustomItemList.eM_energyMulti4_IV.get(1), CustomItemList.eM_energyMulti4_LuV.get(1),
                CustomItemList.eM_energyMulti4_ZPM.get(1), CustomItemList.eM_energyMulti4_UV.get(1),
                CustomItemList.eM_energyMulti4_UHV.get(1), CustomItemList.eM_energyMulti4_UEV.get(1),
                CustomItemList.eM_energyMulti4_UIV.get(1), CustomItemList.eM_energyMulti4_UMV.get(1),
                CustomItemList.eM_energyMulti4_UXV.get(1) };

        ItemStack[] energyHatches_16A = { CustomItemList.eM_energyMulti16_EV.get(1),
                CustomItemList.eM_energyMulti16_IV.get(1), CustomItemList.eM_energyMulti16_LuV.get(1),
                CustomItemList.eM_energyMulti16_ZPM.get(1), CustomItemList.eM_energyMulti16_UV.get(1),
                CustomItemList.eM_energyMulti16_UHV.get(1), CustomItemList.eM_energyMulti16_UEV.get(1),
                CustomItemList.eM_energyMulti16_UIV.get(1), CustomItemList.eM_energyMulti16_UMV.get(1),
                CustomItemList.eM_energyMulti16_UXV.get(1) };

        ItemStack[] energyHatches_64A = { CustomItemList.eM_energyMulti64_EV.get(1),
                CustomItemList.eM_energyMulti64_IV.get(1), CustomItemList.eM_energyMulti64_LuV.get(1),
                CustomItemList.eM_energyMulti64_ZPM.get(1), CustomItemList.eM_energyMulti64_UV.get(1),
                CustomItemList.eM_energyMulti64_UHV.get(1), CustomItemList.eM_energyMulti64_UEV.get(1),
                CustomItemList.eM_energyMulti64_UIV.get(1), CustomItemList.eM_energyMulti64_UMV.get(1),
                CustomItemList.eM_energyMulti64_UXV.get(1) };

        ItemStack[] dynamoHatches = { ItemList.Hatch_Dynamo_ULV.get(1), ItemList.Hatch_Dynamo_LV.get(1),
                ItemList.Hatch_Dynamo_MV.get(1), ItemList.Hatch_Dynamo_HV.get(1), ItemList.Hatch_Dynamo_EV.get(1),
                ItemList.Hatch_Dynamo_IV.get(1), ItemList.Hatch_Dynamo_LuV.get(1), ItemList.Hatch_Dynamo_ZPM.get(1),
                ItemList.Hatch_Dynamo_UV.get(1), ItemList.Hatch_Dynamo_MAX.get(1),
                getItemContainer("Hatch_Dynamo_UEV").get(1L), getItemContainer("Hatch_Dynamo_UIV").get(1L),
                getItemContainer("Hatch_Dynamo_UMV").get(1L), getItemContainer("Hatch_Dynamo_UXV").get(1L) };

        Object[] circuitsTierPlusTwo = { new Object[] { OrePrefixes.circuit.get(Materials.Good), 1L }, // MV
                new Object[] { OrePrefixes.circuit.get(Materials.Advanced), 1L }, // HV
                new Object[] { OrePrefixes.circuit.get(Materials.Data), 1L }, // EV
                new Object[] { OrePrefixes.circuit.get(Materials.Elite), 1L }, // IV
                new Object[] { OrePrefixes.circuit.get(Materials.Master), 1L }, // LuV
                new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 1L }, // ZPM
                new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 1L }, // UV
                new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 1L }, // UHV
                new Object[] { OrePrefixes.circuit.get(Materials.Bio), 1L }, // UEV
                new Object[] { OrePrefixes.circuit.get(Materials.Optical), 1L }, // UIV
                new Object[] { OrePrefixes.circuit.get(Materials.Piko), 1L }, // UMV
                new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 1L }, // UXV
                new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 4L }, // MAX (Technically not MAX, can be
                // changed once MAX circuits become
                // craftable)
                new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 16L } // MAX (Technically not MAX, can be
                // changed once MAX circuits become
                // craftable)
        };

        ItemStack[] wirelessHatches = { ItemList.Wireless_Hatch_Energy_ULV.get(1),
                ItemList.Wireless_Hatch_Energy_LV.get(1), ItemList.Wireless_Hatch_Energy_MV.get(1),
                ItemList.Wireless_Hatch_Energy_HV.get(1), ItemList.Wireless_Hatch_Energy_EV.get(1),
                ItemList.Wireless_Hatch_Energy_IV.get(1), ItemList.Wireless_Hatch_Energy_LuV.get(1),
                ItemList.Wireless_Hatch_Energy_ZPM.get(1), ItemList.Wireless_Hatch_Energy_UV.get(1),
                ItemList.Wireless_Hatch_Energy_UHV.get(1), ItemList.Wireless_Hatch_Energy_UEV.get(1),
                ItemList.Wireless_Hatch_Energy_UIV.get(1), ItemList.Wireless_Hatch_Energy_UMV.get(1),
                ItemList.Wireless_Hatch_Energy_UXV.get(1) };

        ItemStack[] wirelessHatches_4A = { CustomItemList.eM_energyWirelessMulti4_EV.get(1),
                CustomItemList.eM_energyWirelessMulti4_IV.get(1), CustomItemList.eM_energyWirelessMulti4_LuV.get(1),
                CustomItemList.eM_energyWirelessMulti4_ZPM.get(1), CustomItemList.eM_energyWirelessMulti4_UV.get(1),
                CustomItemList.eM_energyWirelessMulti4_UHV.get(1), CustomItemList.eM_energyWirelessMulti4_UEV.get(1),
                CustomItemList.eM_energyWirelessMulti4_UIV.get(1), CustomItemList.eM_energyWirelessMulti4_UMV.get(1),
                CustomItemList.eM_energyWirelessMulti4_UXV.get(1) };

        ItemStack[] wirelessHatches_16A = { CustomItemList.eM_energyWirelessMulti16_EV.get(1),
                CustomItemList.eM_energyWirelessMulti16_IV.get(1), CustomItemList.eM_energyWirelessMulti16_LuV.get(1),
                CustomItemList.eM_energyWirelessMulti16_ZPM.get(1), CustomItemList.eM_energyWirelessMulti16_UV.get(1),
                CustomItemList.eM_energyWirelessMulti16_UHV.get(1), CustomItemList.eM_energyWirelessMulti16_UEV.get(1),
                CustomItemList.eM_energyWirelessMulti16_UIV.get(1), CustomItemList.eM_energyWirelessMulti16_UMV.get(1),
                CustomItemList.eM_energyWirelessMulti16_UXV.get(1) };

        ItemStack[] wirelessHatches_64A = { CustomItemList.eM_energyWirelessMulti64_EV.get(1),
                CustomItemList.eM_energyWirelessMulti64_IV.get(1), CustomItemList.eM_energyWirelessMulti64_LuV.get(1),
                CustomItemList.eM_energyWirelessMulti64_ZPM.get(1), CustomItemList.eM_energyWirelessMulti64_UV.get(1),
                CustomItemList.eM_energyWirelessMulti64_UHV.get(1), CustomItemList.eM_energyWirelessMulti64_UEV.get(1),
                CustomItemList.eM_energyWirelessMulti64_UIV.get(1), CustomItemList.eM_energyWirelessMulti64_UMV.get(1),
                CustomItemList.eM_energyWirelessMulti64_UXV.get(1) };

        ItemStack[] wirelessDynamos = { ItemList.Wireless_Dynamo_Energy_ULV.get(1),
                ItemList.Wireless_Dynamo_Energy_LV.get(1), ItemList.Wireless_Dynamo_Energy_MV.get(1),
                ItemList.Wireless_Dynamo_Energy_HV.get(1), ItemList.Wireless_Dynamo_Energy_EV.get(1),
                ItemList.Wireless_Dynamo_Energy_IV.get(1), ItemList.Wireless_Dynamo_Energy_LuV.get(1),
                ItemList.Wireless_Dynamo_Energy_ZPM.get(1), ItemList.Wireless_Dynamo_Energy_UV.get(1),
                ItemList.Wireless_Dynamo_Energy_UHV.get(1), ItemList.Wireless_Dynamo_Energy_UEV.get(1),
                ItemList.Wireless_Dynamo_Energy_UIV.get(1), ItemList.Wireless_Dynamo_Energy_UMV.get(1),
                ItemList.Wireless_Dynamo_Energy_UXV.get(1) };

        // ------------------------ Wireless EU hatches ------------------------

        for (int i = 0; i < wirelessHatches.length; i++) {

            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    (i == 0) ? ItemList.Tesseract.get(1) : wirelessHatches[i - 1],
                    totalComputation,
                    compPerSecond,
                    researchEuPerTick,
                    researchAmperage,
                    new Object[] { energyHatches[i], getModItem(GoodGenerator.ID, "compactFusionCoil", 1),
                            ItemList.Casing_Coil_Superconductor.get(1), CustomItemList.Machine_Multi_Transformer.get(1),
                            CustomItemList.eM_Power.get(2),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt01, MaterialsUEVplus.SpaceTime, 2),
                            GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Infinity, 1),
                            circuitsTierPlusTwo[i], ItemList.EnergisedTesseract.get(1) },
                    new FluidStack[] { new FluidStack(solderUEV, 1296), MaterialsUEVplus.ExcitedDTEC.getFluid(500L) },
                    wirelessHatches[i],
                    recipeDurationTicks,
                    recipeEuPerTick);
        }

        // ------------------------ 4A Wireless EU hatches ------------------------

        for (int i = 0; i < wirelessHatches_4A.length; i++) {

            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    energyHatches_4A[i],
                    totalComputation * 4,
                    compPerSecond * 4,
                    researchEuPerTick,
                    researchAmperage * 2,
                    new Object[] { energyHatches_4A[i], getModItem(GoodGenerator.ID, "compactFusionCoil", 1, 1),
                            ItemList.Casing_Coil_Superconductor.get(1), CustomItemList.Machine_Multi_Transformer.get(1),
                            CustomItemList.eM_Power.get(4),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt01, MaterialsUEVplus.SpaceTime, 4),
                            GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.WhiteDwarfMatter, 1),
                            circuitsTierPlusTwo[i + 4], ItemList.EnergisedTesseract.get(1) },
                    new FluidStack[] { new FluidStack(solderUEV, 1_296 * 4),
                            MaterialsUEVplus.ExcitedDTEC.getFluid(500L * 4) },
                    wirelessHatches_4A[i],
                    recipeDurationTicks,
                    recipeEuPerTick);
        }

        // ------------------------ 16A Wireless EU hatches ------------------------

        for (int i = 0; i < wirelessHatches_16A.length; i++) {

            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    energyHatches_16A[i],
                    totalComputation * 16,
                    compPerSecond * 16,
                    researchEuPerTick,
                    researchAmperage * 4,
                    new Object[] { energyHatches_16A[i], getModItem(GoodGenerator.ID, "compactFusionCoil", 1, 2),
                            ItemList.Casing_Coil_Superconductor.get(1), CustomItemList.Machine_Multi_Transformer.get(1),
                            CustomItemList.eM_Power.get(16),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt01, MaterialsUEVplus.SpaceTime, 16),
                            GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.BlackDwarfMatter, 1),
                            circuitsTierPlusTwo[i + 4], ItemList.EnergisedTesseract.get(1) },
                    new FluidStack[] { new FluidStack(solderUEV, 1_296 * 16),
                            MaterialsUEVplus.ExcitedDTEC.getFluid(500L * 16) },
                    wirelessHatches_16A[i],
                    recipeDurationTicks,
                    recipeEuPerTick);
        }

        // ------------------------ 64A Wireless EU hatches ------------------------

        for (int i = 0; i < wirelessHatches_64A.length; i++) {

            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    energyHatches_64A[i],
                    totalComputation * 64,
                    compPerSecond * 64,
                    researchEuPerTick,
                    researchAmperage * 8,
                    new Object[] { energyHatches_64A[i], getModItem(GoodGenerator.ID, "compactFusionCoil", 1, 3),
                            ItemList.Casing_Coil_Superconductor.get(1), CustomItemList.Machine_Multi_Transformer.get(1),
                            CustomItemList.eM_Power.get(64),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt01, MaterialsUEVplus.SpaceTime, 64),
                            GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.Universium, 1),
                            circuitsTierPlusTwo[i + 4], ItemList.EnergisedTesseract.get(1) },
                    new FluidStack[] { new FluidStack(solderUEV, 1_296 * 64),
                            MaterialsUEVplus.ExcitedDTEC.getFluid(500L * 64) },
                    wirelessHatches_64A[i],
                    recipeDurationTicks,
                    recipeEuPerTick);
        }

        // ------------------------ Wireless EU dynamos ------------------------

        for (int i = 0; i < wirelessHatches.length; i++) {

            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    (i == 0) ? ItemList.EnergisedTesseract.get(1) : wirelessDynamos[i - 1],
                    totalComputation,
                    compPerSecond,
                    researchEuPerTick,
                    researchAmperage,
                    new Object[] { dynamoHatches[i], getModItem(GoodGenerator.ID, "compactFusionCoil", 1),
                            ItemList.Casing_Coil_Superconductor.get(1), CustomItemList.Machine_Multi_Transformer.get(1),
                            CustomItemList.eM_Power.get(2),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt01, MaterialsUEVplus.SpaceTime, 2),
                            GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Infinity, 1),
                            circuitsTierPlusTwo[i], ItemList.EnergisedTesseract.get(1) },
                    new FluidStack[] { new FluidStack(solderUEV, 1296), MaterialsUEVplus.ExcitedDTEC.getFluid(500L) },
                    wirelessDynamos[i],
                    recipeDurationTicks,
                    recipeEuPerTick);
        }
    }
}
