/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package bartworks.common.loaders;

import static gregtech.api.enums.MetaTileEntityIDs.AcidGeneratorEV;
import static gregtech.api.enums.MetaTileEntityIDs.AcidGeneratorHV;
import static gregtech.api.enums.MetaTileEntityIDs.AcidGeneratorLV;
import static gregtech.api.enums.MetaTileEntityIDs.AcidGeneratorMV;
import static gregtech.api.enums.MetaTileEntityIDs.CircuitAssemblyLine;
import static gregtech.api.enums.MetaTileEntityIDs.CompressedFluidHatch;
import static gregtech.api.enums.MetaTileEntityIDs.DeepEarthHeatingPump;
import static gregtech.api.enums.MetaTileEntityIDs.Diode12A_EV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode12A_HV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode12A_IV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode12A_LV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode12A_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode12A_MAX;
import static gregtech.api.enums.MetaTileEntityIDs.Diode12A_MV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode12A_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode12A_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode12A_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode12A_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode12A_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode12A_UV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode12A_UXV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode12A_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.Diode16A_EV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode16A_HV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode16A_IV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode16A_LV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode16A_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode16A_MAX;
import static gregtech.api.enums.MetaTileEntityIDs.Diode16A_MV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode16A_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode16A_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode16A_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode16A_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode16A_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode16A_UV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode16A_UXV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode16A_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.Diode2A_EV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode2A_HV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode2A_IV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode2A_LV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode2A_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode2A_MAX;
import static gregtech.api.enums.MetaTileEntityIDs.Diode2A_MV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode2A_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode2A_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode2A_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode2A_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode2A_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode2A_UV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode2A_UXV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode2A_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.Diode4A_EV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode4A_HV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode4A_IV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode4A_LV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode4A_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode4A_MAX;
import static gregtech.api.enums.MetaTileEntityIDs.Diode4A_MV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode4A_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode4A_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode4A_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode4A_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode4A_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode4A_UV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode4A_UXV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode4A_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.Diode8A_EV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode8A_HV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode8A_IV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode8A_LV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode8A_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode8A_MAX;
import static gregtech.api.enums.MetaTileEntityIDs.Diode8A_MV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode8A_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode8A_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode8A_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode8A_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode8A_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode8A_UV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode8A_UXV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode8A_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.ElectricImplosionCompressor;
import static gregtech.api.enums.MetaTileEntityIDs.EnergyDistributor_EV;
import static gregtech.api.enums.MetaTileEntityIDs.EnergyDistributor_HV;
import static gregtech.api.enums.MetaTileEntityIDs.EnergyDistributor_IV;
import static gregtech.api.enums.MetaTileEntityIDs.EnergyDistributor_LV;
import static gregtech.api.enums.MetaTileEntityIDs.EnergyDistributor_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.EnergyDistributor_MAX;
import static gregtech.api.enums.MetaTileEntityIDs.EnergyDistributor_MV;
import static gregtech.api.enums.MetaTileEntityIDs.EnergyDistributor_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.EnergyDistributor_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.EnergyDistributor_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.EnergyDistributor_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.EnergyDistributor_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.EnergyDistributor_UV;
import static gregtech.api.enums.MetaTileEntityIDs.EnergyDistributor_UXV;
import static gregtech.api.enums.MetaTileEntityIDs.EnergyDistributor_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.GiantOutputHatch;
import static gregtech.api.enums.MetaTileEntityIDs.HighTemperatureGasCooledReactor;
import static gregtech.api.enums.MetaTileEntityIDs.HumongousInputHatch;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserConverter128A_EV;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserConverter128A_IV;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserConverter128A_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserConverter128A_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserConverter32A_EV;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserConverter32A_IV;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserConverter32A_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserConverter32A_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserConverter64A_EV;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserConverter64A_IV;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserConverter64A_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserConverter64A_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserConverter96A_EV;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserConverter96A_IV;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserConverter96A_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserConverter96A_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserPipe;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserSourceHatch128A_EV;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserSourceHatch128A_IV;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserSourceHatch128A_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserSourceHatch128A_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserSourceHatch32A_EV;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserSourceHatch32A_IV;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserSourceHatch32A_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserSourceHatch32A_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserSourceHatch64A_EV;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserSourceHatch64A_IV;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserSourceHatch64A_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserSourceHatch64A_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserSourceHatch96A_EV;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserSourceHatch96A_IV;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserSourceHatch96A_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserSourceHatch96A_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserTargetHatch128A_EV;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserTargetHatch128A_IV;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserTargetHatch128A_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserTargetHatch128A_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserTargetHatch32A_EV;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserTargetHatch32A_IV;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserTargetHatch32A_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserTargetHatch32A_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserTargetHatch64A_EV;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserTargetHatch64A_IV;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserTargetHatch64A_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserTargetHatch64A_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserTargetHatch96A_EV;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserTargetHatch96A_IV;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserTargetHatch96A_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.LowPowerLaserTargetHatch96A_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.MegaBlastFurnace;
import static gregtech.api.enums.MetaTileEntityIDs.MegaChemicalReactor;
import static gregtech.api.enums.MetaTileEntityIDs.MegaDistillationTower;
import static gregtech.api.enums.MetaTileEntityIDs.MegaOilCracker;
import static gregtech.api.enums.MetaTileEntityIDs.MegaVacuumFreezer;
import static gregtech.api.enums.MetaTileEntityIDs.ThoriumHighTemperatureReactor;
import static gregtech.api.enums.MetaTileEntityIDs.VoidMinerI;
import static gregtech.api.enums.MetaTileEntityIDs.VoidMinerII;
import static gregtech.api.enums.MetaTileEntityIDs.VoidMinerIII;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import bartworks.API.BorosilicateGlass;
import bartworks.MainMod;
import bartworks.common.blocks.BWBlocks;
import bartworks.common.blocks.BWBlocksGlass;
import bartworks.common.blocks.BWBlocksGlass2;
import bartworks.common.blocks.BWMachineBlockContainer;
import bartworks.common.blocks.BWTileEntityContainer;
import bartworks.common.configs.ConfigHandler;
import bartworks.common.items.BWItemBlocks;
import bartworks.common.items.ItemCircuitProgrammer;
import bartworks.common.items.ItemRockCutter;
import bartworks.common.items.ItemSimpleWindMeter;
import bartworks.common.items.ItemStonageRotors;
import bartworks.common.items.ItemTeslaStaff;
import bartworks.common.items.SimpleIconItem;
import bartworks.common.items.SimpleSubItemClass;
import bartworks.common.tileentities.classic.TileEntityHeatedWaterPump;
import bartworks.common.tileentities.classic.TileEntityRotorBlock;
import bartworks.common.tileentities.debug.MTECreativeScanner;
import bartworks.common.tileentities.multis.MTECircuitAssemblyLine;
import bartworks.common.tileentities.multis.MTEDeepEarthHeatingPump;
import bartworks.common.tileentities.multis.MTEElectricImplosionCompressor;
import bartworks.common.tileentities.multis.MTEHighTempGasCooledReactor;
import bartworks.common.tileentities.multis.MTEThoriumHighTempReactor;
import bartworks.common.tileentities.multis.mega.MTEMegaBlastFurnace;
import bartworks.common.tileentities.multis.mega.MTEMegaChemicalReactor;
import bartworks.common.tileentities.multis.mega.MTEMegaDistillTower;
import bartworks.common.tileentities.multis.mega.MTEMegaOilCracker;
import bartworks.common.tileentities.multis.mega.MTEMegaVacuumFreezer;
import bartworks.common.tileentities.tiered.MTEAcidGenerator;
import bartworks.common.tileentities.tiered.MTECompressedFluidHatch;
import bartworks.common.tileentities.tiered.MTEDiode;
import bartworks.common.tileentities.tiered.MTEEnergyDistributor;
import bartworks.common.tileentities.tiered.MTEGiantOutputHatch;
import bartworks.common.tileentities.tiered.MTEHumongousInputHatch;
import bartworks.system.material.WerkstoffLoader;
import bwcrossmod.galacticgreg.MTEVoidMiners;
import bwcrossmod.tectech.tileentites.tiered.MTELowPowerLaserBox;
import bwcrossmod.tectech.tileentites.tiered.MTELowPowerLaserDynamo;
import bwcrossmod.tectech.tileentites.tiered.MTELowPowerLaserHatch;
import bwcrossmod.tectech.tileentites.tiered.MTELowPowerLaserPipe;
import cpw.mods.fml.common.registry.GameRegistry;
import fox.spiteful.avaritia.blocks.LudicrousBlocks;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MetaTileEntityIDs;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import ic2.api.item.IKineticRotor;

public class ItemRegistry {

    public static final Item TESLASTAFF = new ItemTeslaStaff();
    public static final Item ROCKCUTTER_LV = new ItemRockCutter(1);
    public static final Item ROCKCUTTER_MV = new ItemRockCutter(2);
    public static final Item ROCKCUTTER_HV = new ItemRockCutter(3);
    public static final Item CIRCUIT_PROGRAMMER = new ItemCircuitProgrammer();
    public static final Block ROTORBLOCK = new BWMachineBlockContainer(
        Material.wood,
        TileEntityRotorBlock.class,
        "BWRotorBlock");
    public static final Item LEATHER_ROTOR = new ItemStonageRotors(
        7,
        0.15f,
        5,
        1.15f,
        1,
        50,
        10000000,
        IKineticRotor.GearboxType.WIND,
        new ResourceLocation(MainMod.MOD_ID, "textures/items/rotors/rotorLeather.png"),
        "BW_LeatherRotor",
        "rotors/itemRotorLeather");
    public static final Item WOOL_ROTOR = new ItemStonageRotors(
        7,
        0.18f,
        3,
        1.35f,
        1,
        50,
        9000000,
        IKineticRotor.GearboxType.WIND,
        new ResourceLocation(MainMod.MOD_ID, "textures/items/rotors/rotorWool.png"),
        "BW_WoolRotor",
        "rotors/itemRotorWool");
    public static final Item PAPER_ROTOR = new ItemStonageRotors(
        7,
        0.2f,
        12,
        0.9f,
        1,
        50,
        800000,
        IKineticRotor.GearboxType.WIND,
        new ResourceLocation(MainMod.MOD_ID, "textures/items/rotors/rotorPaper.png"),
        "BW_PaperRotor",
        "rotors/itemRotorPaper");
    public static final Item COMBINED_ROTOR = new ItemStonageRotors(
        9,
        0.22f,
        7,
        1.05f,
        1,
        50,
        6000000,
        IKineticRotor.GearboxType.WIND,
        new ResourceLocation(MainMod.MOD_ID, "textures/items/rotors/rotorCombined.png"),
        "BW_CombinedRotor",
        "rotors/itemRotorCombined");
    public static final Item CRAFTING_PARTS = new SimpleSubItemClass(
        "grindstone_top",
        "grindstone_bottom",
        "completed_grindstone",
        "rotors/leatherParts",
        "rotors/woolParts",
        "rotors/paperParts",
        "rotors/combinedParts");
    public static final Item TAB = new SimpleIconItem("GT2Coin");
    public static final Item WINDMETER = new ItemSimpleWindMeter();
    public static final Block PUMPBLOCK = new BWTileEntityContainer(
        Material.anvil,
        TileEntityHeatedWaterPump.class,
        "BWHeatedWaterPump");
    public static final Item PUMPPARTS = new SimpleSubItemClass("BWrawtube", "BWmotor");

    public static final Block bw_realglas = new BWBlocksGlass(
        "BW_GlasBlocks",
        new String[] { MainMod.MOD_ID + ":BoronSilicateGlassBlock",
            MainMod.MOD_ID + ":TitaniumReinforcedBoronSilicateGlassBlock",
            MainMod.MOD_ID + ":TungstenSteelReinforcedBoronSilicateGlassBlock",
            MainMod.MOD_ID + ":LuVTierMaterialReinforcedBoronSilicateGlassBlock",
            MainMod.MOD_ID + ":IridiumReinforcedBoronSilicateGlassBlock",
            MainMod.MOD_ID + ":OsmiumReinforcedBoronSilicateGlassBlock",
            MainMod.MOD_ID + ":ColoredBoronSilicateGlassBlock1", MainMod.MOD_ID + ":ColoredBoronSilicateGlassBlock2",
            MainMod.MOD_ID + ":ColoredBoronSilicateGlassBlock3", MainMod.MOD_ID + ":ColoredBoronSilicateGlassBlock4",
            MainMod.MOD_ID + ":ColoredBoronSilicateGlassBlock5", MainMod.MOD_ID + ":ColoredBoronSilicateGlassBlock6",
            MainMod.MOD_ID + ":ThoriumYttriumGlass", MainMod.MOD_ID + ":NeutroniumReinforcedBoronSilicateGlassBlock",
            MainMod.MOD_ID + ":CosmicNeutroniumReinforcedBoronSilicateGlassBlock",
            MainMod.MOD_ID + ":InfinityReinforcedBoronSilicateGlassBlock", },
        new short[][] { Materials.BorosilicateGlass.getRGBA(), Materials.Titanium.getRGBA(),
            Materials.TungstenSteel.getRGBA(), Materials.Chrome.getRGBA(), Materials.Iridium.getRGBA(),
            Materials.Osmium.getRGBA(), new short[] { 0xff, 0, 0 }, new short[] { 0, 0xff, 0 },
            new short[] { 0x80, 0, 0xff }, new short[] { 0xff, 0xff, 0 }, new short[] { 0, 0xff, 0x80 },
            new short[] { 0x80, 0x33, 0 }, WerkstoffLoader.YttriumOxide.getRGBA(), Materials.Neutronium.getRGBA(),
            Materials.CosmicNeutronium.getRGBA(), new short[] { 0xda, 0xeb, 0xff }, },
        MainMod.BIO_TAB,
        true,
        false);
    public static final Block bw_realglas2 = new BWBlocksGlass2(
        "BW_GlasBlocks2",
        new String[] { MainMod.MOD_ID + ":TranscendentallyReinforcedBoronSilicateGlassBlock" },
        new short[][] { new short[] { 50, 50, 50 } },
        MainMod.BIO_TAB,
        true,
        false);

    public static final Block[] bw_glasses = { bw_realglas, bw_realglas2 };
    public static final Block bw_fake_glasses = new BWBlocksGlass(
        "BW_GlasBlocks",
        new String[] { MainMod.MOD_ID + ":BoronSilicateGlassBlockRandlos" },
        null,
        null,
        true,
        true);
    public static final Block bw_fake_glasses2 = new BWBlocksGlass2(
        "BW_GlasBlocks2",
        new String[] { MainMod.MOD_ID + ":BoronSilicateGlassBlockRandlos" },
        null,
        null,
        true,
        true);
    public static final Block[] BW_BLOCKS = {
        new BWBlocks(
            "BW_ItemBlocks",
            new String[] { MainMod.MOD_ID + ":EtchedLapisCell", MainMod.MOD_ID + ":PlatedLapisCell", },
            MainMod.GT2),
        new BWBlocks("GT_LESU_CASING", new String[] { MainMod.MOD_ID + ":LESU_CELL", }, MainMod.GT2),
        new BWBlocks(
            "BW_Machinery_Casings",
            new String[] { MainMod.MOD_ID + ":NickelFerriteBlocks", MainMod.MOD_ID + ":TransformerCoil",
            // MainMod.MOD_ID+":DEHP_Casing",
            // MainMod.MOD_ID+":DEHP_Casing_Base"
            },
            MainMod.BWT), };

    public static ItemStack[] diode2A = new ItemStack[GTValues.VN.length];
    public static ItemStack[] diode4A = new ItemStack[GTValues.VN.length];
    public static ItemStack[] diode8A = new ItemStack[GTValues.VN.length];
    public static ItemStack[] diode12A = new ItemStack[GTValues.VN.length];
    public static ItemStack[] diode16A = new ItemStack[GTValues.VN.length];
    public static ItemStack[] energyDistributor = new ItemStack[GTValues.VN.length];
    public static ItemStack[] acidGens = new ItemStack[3];
    public static ItemStack acidGensLV;
    public static ItemStack[] megaMachines = new ItemStack[5];
    public static ItemStack dehp;
    public static ItemStack[] voidminer = new ItemStack[3];
    public static ItemStack THTR;
    public static ItemStack HTGR;
    public static ItemStack eic;
    public static ItemStack cal;
    public static ItemStack compressedHatch;
    public static ItemStack giantOutputHatch;
    public static ItemStack humongousInputHatch;

    public static ItemStack[][][] TecTechLaserAdditions = new ItemStack[3][4][4];
    public static ItemStack TecTechPipeEnergyLowPower;

    public static void run() {
        GameRegistry.registerBlock(ItemRegistry.bw_fake_glasses, "BW_FakeGlasBlock");
        GameRegistry.registerBlock(ItemRegistry.bw_fake_glasses2, "BW_FakeGlasBlocks2");
        BorosilicateGlass.registerBorosilicateGlass();

        GameRegistry.registerBlock(ItemRegistry.BW_BLOCKS[2], BWItemBlocks.class, "BW_Machinery_Casings");
        GameRegistry.registerItem(ItemRegistry.LEATHER_ROTOR, "BW_LeatherRotor");
        GameRegistry.registerItem(ItemRegistry.WOOL_ROTOR, "BW_WoolRotor");
        GameRegistry.registerItem(ItemRegistry.PAPER_ROTOR, "BW_PaperRotor");
        GameRegistry.registerItem(ItemRegistry.COMBINED_ROTOR, "BW_CombinedRotor");
        GameRegistry.registerItem(ItemRegistry.CRAFTING_PARTS, "craftingParts");
        GameRegistry.registerTileEntity(TileEntityRotorBlock.class, "BWRotorBlockTE");
        GameRegistry.registerBlock(ItemRegistry.ROTORBLOCK, BWItemBlocks.class, "BWRotorBlock");
        GameRegistry.registerTileEntity(TileEntityHeatedWaterPump.class, "BWHeatedWaterPumpTE");
        GameRegistry.registerBlock(ItemRegistry.PUMPBLOCK, BWItemBlocks.class, "BWHeatedWaterPumpBlock");
        GameRegistry.registerItem(ItemRegistry.PUMPPARTS, "BWPumpParts");
        GameRegistry.registerItem(ItemRegistry.WINDMETER, "BW_SimpleWindMeter");

        // GT2 stuff
        GameRegistry.registerBlock(ItemRegistry.BW_BLOCKS[0], BWItemBlocks.class, "BW_ItemBlocks");
        GameRegistry.registerBlock(ItemRegistry.BW_BLOCKS[1], BWItemBlocks.class, "GT_LESU_CASING");
        if (ConfigHandler.teslastaff)
            GameRegistry.registerItem(ItemRegistry.TESLASTAFF, ItemRegistry.TESLASTAFF.getUnlocalizedName());

        GameRegistry.registerItem(ItemRegistry.ROCKCUTTER_LV, ItemRegistry.ROCKCUTTER_LV.getUnlocalizedName());
        GameRegistry.registerItem(ItemRegistry.ROCKCUTTER_MV, ItemRegistry.ROCKCUTTER_MV.getUnlocalizedName());
        GameRegistry.registerItem(ItemRegistry.ROCKCUTTER_HV, ItemRegistry.ROCKCUTTER_HV.getUnlocalizedName());
        GameRegistry.registerItem(ItemRegistry.TAB, "tabIconGT2");

        new MTECreativeScanner(
            MetaTileEntityIDs.CreativeScanner.ID,
            "Creative Debug Scanner",
            "Creative Debug Scanner",
            14);
        ItemRegistry.eic = new MTEElectricImplosionCompressor(
            ElectricImplosionCompressor.ID,
            "ElectricImplosionCompressor",
            "Electric Implosion Compressor").getStackForm(1L);

        // EIC depend on neutronium block to pass on structure updates
        int bitmask = GregTechAPI.sMachineIDs.getOrDefault(GregTechAPI.sBlockMetal5, 0) | 1 << 2;
        GregTechAPI.registerMachineBlock(GregTechAPI.sBlockMetal5, bitmask);

        // Also spacetime, transcendent metal, and universium
        bitmask = GregTechAPI.sMachineIDs.getOrDefault(GregTechAPI.sBlockMetal9, 0) | 1 << 3 | 1 << 4 | 1 << 8;
        GregTechAPI.registerMachineBlock(GregTechAPI.sBlockMetal9, bitmask);

        if (Mods.Avaritia.isModLoaded()) {
            // Also infinity
            bitmask = GregTechAPI.sMachineIDs.getOrDefault(LudicrousBlocks.resource_block, 0) | 1 << 1;
            GregTechAPI.registerMachineBlock(LudicrousBlocks.resource_block, bitmask);
        }

        ItemRegistry.THTR = new MTEThoriumHighTempReactor(
            ThoriumHighTemperatureReactor.ID,
            "THTR",
            "Thorium High Temperature Reactor").getStackForm(1L);
        MTEThoriumHighTempReactor.THTRMaterials.registeraTHR_Materials();
        ItemRegistry.HTGR = new MTEHighTempGasCooledReactor(
            HighTemperatureGasCooledReactor.ID,
            "HTGR",
            "High Temperature Gas-cooled Reactor").getStackForm(1L);
        MTEHighTempGasCooledReactor.HTGRMaterials.registeraTHR_Materials();

        // ID 12728 + 15 + 49 IS TAKEN !!! (12792)

        GTOreDictUnificator
            .add(OrePrefixes.block, Materials.BorosilicateGlass, new ItemStack(ItemRegistry.bw_glasses[0], 1, 0));
        GTOreDictUnificator
            .registerOre(OrePrefixes.block, Materials.NickelZincFerrite, new ItemStack(ItemRegistry.BW_BLOCKS[2]));

        int[] Diode2A = new int[] { Diode2A_ULV.ID, Diode2A_LV.ID, Diode2A_MV.ID, Diode2A_HV.ID, Diode2A_EV.ID,
            Diode2A_IV.ID, Diode2A_LuV.ID, Diode2A_ZPM.ID, Diode2A_UV.ID, Diode2A_UHV.ID, Diode2A_UEV.ID,
            Diode2A_UIV.ID, Diode2A_UMV.ID, Diode2A_UXV.ID, Diode2A_MAX.ID };
        int[] Diode4A = new int[] { Diode4A_ULV.ID, Diode4A_LV.ID, Diode4A_MV.ID, Diode4A_HV.ID, Diode4A_EV.ID,
            Diode4A_IV.ID, Diode4A_LuV.ID, Diode4A_ZPM.ID, Diode4A_UV.ID, Diode4A_UHV.ID, Diode4A_UEV.ID,
            Diode4A_UIV.ID, Diode4A_UMV.ID, Diode4A_UXV.ID, Diode4A_MAX.ID };
        int[] Diode8A = new int[] { Diode8A_ULV.ID, Diode8A_LV.ID, Diode8A_MV.ID, Diode8A_HV.ID, Diode8A_EV.ID,
            Diode8A_IV.ID, Diode8A_LuV.ID, Diode8A_ZPM.ID, Diode8A_UV.ID, Diode8A_UHV.ID, Diode8A_UEV.ID,
            Diode8A_UIV.ID, Diode8A_UMV.ID, Diode8A_UXV.ID, Diode8A_MAX.ID };
        int[] Diode12A = new int[] { Diode12A_ULV.ID, Diode12A_LV.ID, Diode12A_MV.ID, Diode12A_HV.ID, Diode12A_EV.ID,
            Diode12A_IV.ID, Diode12A_LuV.ID, Diode12A_ZPM.ID, Diode12A_UV.ID, Diode12A_UHV.ID, Diode12A_UEV.ID,
            Diode12A_UIV.ID, Diode12A_UMV.ID, Diode12A_UXV.ID, Diode12A_MAX.ID };
        int[] Diode16A = new int[] { Diode16A_ULV.ID, Diode16A_LV.ID, Diode16A_MV.ID, Diode16A_HV.ID, Diode16A_EV.ID,
            Diode16A_IV.ID, Diode16A_LuV.ID, Diode16A_ZPM.ID, Diode16A_UV.ID, Diode16A_UHV.ID, Diode16A_UEV.ID,
            Diode16A_UIV.ID, Diode16A_UMV.ID, Diode16A_UXV.ID, Diode16A_MAX.ID };
        int[] EnergyDistributors = new int[] { EnergyDistributor_ULV.ID, EnergyDistributor_LV.ID,
            EnergyDistributor_MV.ID, EnergyDistributor_HV.ID, EnergyDistributor_EV.ID, EnergyDistributor_IV.ID,
            EnergyDistributor_LuV.ID, EnergyDistributor_ZPM.ID, EnergyDistributor_UV.ID, EnergyDistributor_UHV.ID,
            EnergyDistributor_UEV.ID, EnergyDistributor_UIV.ID, EnergyDistributor_UMV.ID, EnergyDistributor_UXV.ID,
            EnergyDistributor_MAX.ID };

        for (int i = 0; i < GTValues.VN.length - 1; i++) {
            ItemRegistry.diode2A[i] = new MTEDiode(
                Diode2A[i],
                "diode" + "2A" + GTValues.VN[i],
                StatCollector.translateToLocal("tile.diode.name") + " 2A " + GTValues.VN[i],
                i).getStackForm(1L);
            ItemRegistry.diode4A[i] = new MTEDiode(
                Diode4A[i],
                "diode" + "4A" + GTValues.VN[i],
                StatCollector.translateToLocal("tile.diode.name") + " 4A " + GTValues.VN[i],
                i).getStackForm(1L);
            ItemRegistry.diode8A[i] = new MTEDiode(
                Diode8A[i],
                "diode" + "8A" + GTValues.VN[i],
                StatCollector.translateToLocal("tile.diode.name") + " 8A " + GTValues.VN[i],
                i).getStackForm(1L);
            ItemRegistry.diode12A[i] = new MTEDiode(
                Diode12A[i],
                "diode" + "12A" + GTValues.VN[i],
                StatCollector.translateToLocal("tile.diode.name") + " 12A " + GTValues.VN[i],
                i).getStackForm(1L);
            ItemRegistry.diode16A[i] = new MTEDiode(
                Diode16A[i],
                "diode" + "16A" + GTValues.VN[i],
                StatCollector.translateToLocal("tile.diode.name") + " 16A " + GTValues.VN[i],
                i).getStackForm(1L);
            ItemRegistry.energyDistributor[i] = new MTEEnergyDistributor(
                EnergyDistributors[i],
                "energydistributor" + GTValues.VN[i],
                StatCollector.translateToLocal("tile.energydistributor.name") + " " + GTValues.VN[i],
                i).getStackForm(1L);
        }

        ItemRegistry.acidGens[0] = new MTEAcidGenerator(
            AcidGeneratorMV.ID,
            "acidgenerator" + GTValues.VN[2],
            StatCollector.translateToLocal("tile.acidgenerator.name") + " " + GTValues.VN[2],
            2).getStackForm(1);
        ItemRegistry.acidGens[1] = new MTEAcidGenerator(
            AcidGeneratorHV.ID,
            "acidgenerator" + GTValues.VN[3],
            StatCollector.translateToLocal("tile.acidgenerator.name") + " " + GTValues.VN[3],
            3).getStackForm(1);
        ItemRegistry.acidGens[2] = new MTEAcidGenerator(
            AcidGeneratorEV.ID,
            "acidgenerator" + GTValues.VN[4],
            StatCollector.translateToLocal("tile.acidgenerator.name") + " " + GTValues.VN[4],
            4).getStackForm(1);

        ItemRegistry.acidGensLV = new MTEAcidGenerator(
            AcidGeneratorLV.ID,
            "acidgenerator" + GTValues.VN[1],
            StatCollector.translateToLocal("tile.acidgenerator.name") + " " + GTValues.VN[1],
            +1).getStackForm(1L);

        ItemRegistry.dehp = new MTEDeepEarthHeatingPump(DeepEarthHeatingPump.ID, 1, "DEHP", "Deep Earth Heating Pump")
            .getStackForm(1L);
        ItemRegistry.megaMachines[0] = new MTEMegaBlastFurnace(
            MegaBlastFurnace.ID,
            "MegaBlastFurnace",
            StatCollector.translateToLocal("tile.bw.mbf.name")).getStackForm(1L);
        ItemRegistry.megaMachines[1] = new MTEMegaVacuumFreezer(
            MegaVacuumFreezer.ID,
            "MegaVacuumFreezer",
            StatCollector.translateToLocal("tile.bw.mvf.name")).getStackForm(1L);
        ItemRegistry.cal = new MTECircuitAssemblyLine(
            CircuitAssemblyLine.ID,
            "CircuitAssemblyLine",
            "Circuit Assembly Line").getStackForm(1L);
        ItemRegistry.compressedHatch = new MTECompressedFluidHatch(
            CompressedFluidHatch.ID,
            "CompressedFluidHatch",
            "Liquid Air Fluid Hatch").getStackForm(1L);
        ItemRegistry.giantOutputHatch = new MTEGiantOutputHatch(
            GiantOutputHatch.ID,
            "GiantOutputHatch",
            "Giant Output Hatch").getStackForm(1L);
        ItemRegistry.humongousInputHatch = new MTEHumongousInputHatch(
            HumongousInputHatch.ID,
            "HumongousInputHatch",
            "Humongous Input Hatch").getStackForm(1L);
        ItemRegistry.megaMachines[2] = new MTEMegaDistillTower(
            MegaDistillationTower.ID,
            "MegaDistillationTower",
            "Mega Distillation Tower").getStackForm(1L);
        ItemRegistry.megaMachines[3] = new MTEMegaChemicalReactor(
            MegaChemicalReactor.ID,
            "MegaChemicalReactor",
            "Mega Chemical Reactor").getStackForm(1L);
        ItemRegistry.megaMachines[4] = new MTEMegaOilCracker(MegaOilCracker.ID, "MegaOilCracker", "Mega Oil Cracker")
            .getStackForm(1L);

        ItemRegistry.voidminer[2] = new MTEVoidMiners.VMUV(VoidMinerIII.ID, "VoidMiner3", "Void Miner III")
            .getStackForm(1L);
        ItemRegistry.voidminer[1] = new MTEVoidMiners.VMZPM(VoidMinerII.ID, "VoidMiner2", "Void Miner II")
            .getStackForm(1L);
        ItemRegistry.voidminer[0] = new MTEVoidMiners.VMLUV(VoidMinerI.ID, "VoidMiner1", "Void Miner I")
            .getStackForm(1L);

        TecTechPipeEnergyLowPower = new MTELowPowerLaserPipe(
            LowPowerLaserPipe.ID,
            "pipe.lowpowerlaser",
            "Low Power Laser Pipe").getStackForm(1L);

        int[] LowPowerLaserConverter32A = new int[] { LowPowerLaserConverter32A_EV.ID, LowPowerLaserConverter32A_IV.ID,
            LowPowerLaserConverter32A_LuV.ID, LowPowerLaserConverter32A_ZPM.ID };
        int[] LowPowerLaserConverter64A = new int[] { LowPowerLaserConverter64A_EV.ID, LowPowerLaserConverter64A_IV.ID,
            LowPowerLaserConverter64A_LuV.ID, LowPowerLaserConverter64A_ZPM.ID };
        int[] LowPowerLaserConverter96A = new int[] { LowPowerLaserConverter96A_EV.ID, LowPowerLaserConverter96A_IV.ID,
            LowPowerLaserConverter96A_LuV.ID, LowPowerLaserConverter96A_ZPM.ID };
        int[] LowPowerLaserConverter128A = new int[] { LowPowerLaserConverter128A_EV.ID,
            LowPowerLaserConverter128A_IV.ID, LowPowerLaserConverter128A_LuV.ID, LowPowerLaserConverter128A_ZPM.ID };
        int[][] LowPowerLaserConverter = new int[][] { LowPowerLaserConverter32A, LowPowerLaserConverter64A,
            LowPowerLaserConverter96A, LowPowerLaserConverter128A };

        for (int amps = 32; amps <= 128; amps += 32) {
            for (int tier = 4; tier < 8; tier++) {
                TecTechLaserAdditions[0][amps / 32 - 1][tier - 4] = new MTELowPowerLaserBox(
                    LowPowerLaserConverter[amps / 32 - 1][tier - 4],
                    GTValues.VN[tier] + "_LPLaser_Converter_" + amps,
                    GTValues.VN[tier] + " " + amps + "A" + " Low Power Laser Converter",
                    tier,
                    amps).getStackForm(1L);
            }
        }

        int[] LowPowerLaserTargetHatch32A = new int[] { LowPowerLaserTargetHatch32A_EV.ID,
            LowPowerLaserTargetHatch32A_IV.ID, LowPowerLaserTargetHatch32A_LuV.ID, LowPowerLaserTargetHatch32A_ZPM.ID };
        int[] LowPowerLaserTargetHatch64A = new int[] { LowPowerLaserTargetHatch64A_EV.ID,
            LowPowerLaserTargetHatch64A_IV.ID, LowPowerLaserTargetHatch64A_LuV.ID, LowPowerLaserTargetHatch64A_ZPM.ID };
        int[] LowPowerLaserTargetHatch96A = new int[] { LowPowerLaserTargetHatch96A_EV.ID,
            LowPowerLaserTargetHatch96A_IV.ID, LowPowerLaserTargetHatch96A_LuV.ID, LowPowerLaserTargetHatch96A_ZPM.ID };
        int[] LowPowerLaserTargetHatch128A = new int[] { LowPowerLaserTargetHatch128A_EV.ID,
            LowPowerLaserTargetHatch128A_IV.ID, LowPowerLaserTargetHatch128A_LuV.ID,
            LowPowerLaserTargetHatch128A_ZPM.ID };
        int[][] LowPowerLaserTargetHatch = new int[][] { LowPowerLaserTargetHatch32A, LowPowerLaserTargetHatch64A,
            LowPowerLaserTargetHatch96A, LowPowerLaserTargetHatch128A };
        for (int amps = 32; amps <= 128; amps += 32) {
            for (int tier = 4; tier < 8; tier++) {
                TecTechLaserAdditions[1][amps / 32 - 1][tier - 4] = new MTELowPowerLaserHatch(
                    LowPowerLaserTargetHatch[amps / 32 - 1][tier - 4],
                    GTValues.VN[tier] + "_LPLaser_Hatch_" + amps,
                    GTValues.VN[tier] + " " + amps + "A" + " Low Power Laser Target Hatch",
                    tier,
                    amps).getStackForm(1L);
            }
        }

        int[] LowPowerLaserSourceHatch32A = new int[] { LowPowerLaserSourceHatch32A_EV.ID,
            LowPowerLaserSourceHatch32A_IV.ID, LowPowerLaserSourceHatch32A_LuV.ID, LowPowerLaserSourceHatch32A_ZPM.ID };
        int[] LowPowerLaserSourceHatch64A = new int[] { LowPowerLaserSourceHatch64A_EV.ID,
            LowPowerLaserSourceHatch64A_IV.ID, LowPowerLaserSourceHatch64A_LuV.ID, LowPowerLaserSourceHatch64A_ZPM.ID };
        int[] LowPowerLaserSourceHatch96A = new int[] { LowPowerLaserSourceHatch96A_EV.ID,
            LowPowerLaserSourceHatch96A_IV.ID, LowPowerLaserSourceHatch96A_LuV.ID, LowPowerLaserSourceHatch96A_ZPM.ID };
        int[] LowPowerLaserSourceHatch128A = new int[] { LowPowerLaserSourceHatch128A_EV.ID,
            LowPowerLaserSourceHatch128A_IV.ID, LowPowerLaserSourceHatch128A_LuV.ID,
            LowPowerLaserSourceHatch128A_ZPM.ID };
        int[][] LowPowerLaserSourceHatch = new int[][] { LowPowerLaserSourceHatch32A, LowPowerLaserSourceHatch64A,
            LowPowerLaserSourceHatch96A, LowPowerLaserSourceHatch128A };
        for (int amps = 32; amps <= 128; amps += 32) {
            for (int tier = 4; tier < 8; tier++) {
                TecTechLaserAdditions[2][amps / 32 - 1][tier - 4] = new MTELowPowerLaserDynamo(
                    LowPowerLaserSourceHatch[amps / 32 - 1][tier - 4],
                    GTValues.VN[tier] + "_LPLaser_Dynamo_" + amps,
                    GTValues.VN[tier] + " " + amps + "A" + " Low Power Laser Source Hatch",
                    tier,
                    amps).getStackForm(1L);
            }
        }
    }
}
