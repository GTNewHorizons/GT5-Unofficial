/*
 * Copyright (c) 2018-2020 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.common.loaders;

import static com.github.bartimaeusnek.bartworks.MainMod.BWT;
import static com.github.bartimaeusnek.bartworks.MainMod.GT2;
import static com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler.newStuff;

import com.github.bartimaeusnek.bartworks.API.LoaderReference;
import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.common.blocks.BW_Blocks;
import com.github.bartimaeusnek.bartworks.common.blocks.BW_GlasBlocks;
import com.github.bartimaeusnek.bartworks.common.blocks.BW_TileEntityContainer;
import com.github.bartimaeusnek.bartworks.common.blocks.BW_TileEntityContainer_MachineBlock;
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.common.items.*;
import com.github.bartimaeusnek.bartworks.common.tileentities.classic.BW_RotorBlock;
import com.github.bartimaeusnek.bartworks.common.tileentities.classic.BW_TileEntity_ExperimentalFloodGate;
import com.github.bartimaeusnek.bartworks.common.tileentities.classic.BW_TileEntity_HeatedWaterPump;
import com.github.bartimaeusnek.bartworks.common.tileentities.debug.CreativeScanner;
import com.github.bartimaeusnek.bartworks.common.tileentities.multis.*;
import com.github.bartimaeusnek.bartworks.common.tileentities.multis.mega.*;
import com.github.bartimaeusnek.bartworks.common.tileentities.tiered.*;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.crossmod.galacticgreg.GT_TileEntity_VoidMiners;
import com.github.bartimaeusnek.crossmod.tectech.tileentites.tiered.TT_MetaTileEntity_LowPowerLaserBox;
import com.github.bartimaeusnek.crossmod.tectech.tileentites.tiered.TT_MetaTileEntity_LowPowerLaserDynamo;
import com.github.bartimaeusnek.crossmod.tectech.tileentites.tiered.TT_MetaTileEntity_LowPowerLaserHatch;
import com.github.bartimaeusnek.crossmod.tectech.tileentites.tiered.TT_MetaTileEntity_Pipe_Energy_LowPower;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import ic2.api.item.IKineticRotor;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class ItemRegistry {

    public static final Item DESTRUCTOPACK = new GT_Destructopack_Item();
    public static final Item TESLASTAFF = new GT_Teslastaff_Item();
    public static final Item ROCKCUTTER_LV = new GT_Rockcutter_Item(1);
    public static final Item ROCKCUTTER_MV = new GT_Rockcutter_Item(2);
    public static final Item ROCKCUTTER_HV = new GT_Rockcutter_Item(3);
    public static final Item CIRCUIT_PROGRAMMER = new Circuit_Programmer();
    public static final Block ROTORBLOCK =
            new BW_TileEntityContainer_MachineBlock(Material.wood, BW_RotorBlock.class, "BWRotorBlock");
    public static final Item LEATHER_ROTOR = new BW_Stonage_Rotors(
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
    public static final Item WOOL_ROTOR = new BW_Stonage_Rotors(
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
    public static final Item PAPER_ROTOR = new BW_Stonage_Rotors(
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
    public static final Item COMBINED_ROTOR = new BW_Stonage_Rotors(
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
    public static final Item WINDMETER = new BW_SimpleWindMeter();
    public static final Block PUMPBLOCK =
            new BW_TileEntityContainer(Material.anvil, BW_TileEntity_HeatedWaterPump.class, "BWHeatedWaterPump");
    public static final Item PUMPPARTS = new SimpleSubItemClass("BWrawtube", "BWmotor");
    public static final Block EXPPUMP =
            new BW_TileEntityContainer(Material.coral, BW_TileEntity_ExperimentalFloodGate.class, "ExpReversePump");

    public static final Block bw_realglas = new BW_GlasBlocks(
            "BW_GlasBlocks",
            new String[] {
                MainMod.MOD_ID + ":BoronSilicateGlassBlock",
                MainMod.MOD_ID + ":TitaniumReinforcedBoronSilicateGlassBlock",
                MainMod.MOD_ID + ":TungstenSteelReinforcedBoronSilicateGlassBlock",
                MainMod.MOD_ID + ":LuVTierMaterialReinforcedBoronSilicateGlassBlock",
                MainMod.MOD_ID + ":IridiumReinforcedBoronSilicateGlassBlock",
                MainMod.MOD_ID + ":OsmiumReinforcedBoronSilicateGlassBlock",
                MainMod.MOD_ID + ":ColoredBoronSilicateGlassBlock1",
                MainMod.MOD_ID + ":ColoredBoronSilicateGlassBlock2",
                MainMod.MOD_ID + ":ColoredBoronSilicateGlassBlock3",
                MainMod.MOD_ID + ":ColoredBoronSilicateGlassBlock4",
                MainMod.MOD_ID + ":ColoredBoronSilicateGlassBlock5",
                MainMod.MOD_ID + ":ColoredBoronSilicateGlassBlock6",
                MainMod.MOD_ID + ":ThoriumYttriumGlass",
                MainMod.MOD_ID + ":NeutroniumReinforcedBoronSilicateGlassBlock",
                MainMod.MOD_ID + ":CosmicNeutroniumReinforcedBoronSilicateGlassBlock",
            },
            new short[][] {
                Materials.BorosilicateGlass.getRGBA(),
                Materials.Titanium.getRGBA(),
                Materials.TungstenSteel.getRGBA(),
                Materials.Chrome.getRGBA(),
                Materials.Iridium.getRGBA(),
                Materials.Osmium.getRGBA(),
                new short[] {0xff, 0, 0},
                new short[] {0, 0xff, 0},
                new short[] {0x80, 0, 0xff},
                new short[] {0xff, 0xff, 0},
                new short[] {0, 0xff, 0x80},
                new short[] {0x80, 0x33, 0},
                WerkstoffLoader.YttriumOxide.getRGBA(),
                Materials.Neutronium.getRGBA(),
                Materials.CosmicNeutronium.getRGBA()
            },
            MainMod.BIO_TAB,
            true,
            false);

    public static final Block[] bw_glasses = {bw_realglas};
    public static final Block bw_fake_glasses = new BW_GlasBlocks(
            "BW_GlasBlocks", new String[] {MainMod.MOD_ID + ":BoronSilicateGlassBlockRandlos"}, null, null, true, true);
    public static final Block[] BW_BLOCKS = {
        new BW_Blocks(
                "BW_ItemBlocks",
                new String[] {
                    MainMod.MOD_ID + ":EtchedLapisCell", MainMod.MOD_ID + ":PlatedLapisCell",
                },
                GT2),
        new BW_Blocks(
                "GT_LESU_CASING",
                new String[] {
                    MainMod.MOD_ID + ":LESU_CELL",
                },
                GT2),
        new BW_Blocks(
                "BW_Machinery_Casings",
                new String[] {
                    MainMod.MOD_ID + ":NickelFerriteBlocks", MainMod.MOD_ID + ":TransformerCoil",
                    // MainMod.MOD_ID+":DEHP_Casing",
                    // MainMod.MOD_ID+":DEHP_Casing_Base"
                },
                BWT),
    };

    public static ItemStack[] diode2A = new ItemStack[GT_Values.VN.length];
    public static ItemStack[] diode4A = new ItemStack[GT_Values.VN.length];
    public static ItemStack[] diode8A = new ItemStack[GT_Values.VN.length];
    public static ItemStack[] diode12A = new ItemStack[GT_Values.VN.length];
    public static ItemStack[] diode16A = new ItemStack[GT_Values.VN.length];
    public static ItemStack[] energyDistributor = new ItemStack[GT_Values.VN.length];
    public static ItemStack[] acidGens = new ItemStack[3];
    public static ItemStack acidGensLV;
    public static ItemStack[] megaMachines = new ItemStack[5];
    public static ItemStack dehp;
    public static ItemStack[] voidminer = new ItemStack[3];
    public static ItemStack THTR;
    public static ItemStack HTGR;
    public static ItemStack EIG;
    public static ItemStack eic;
    public static ItemStack cal;
    public static ItemStack compressedHatch;
    public static ItemStack giantOutputHatch;

    public static ItemStack[][][] TecTechLaserAdditions = new ItemStack[3][4][4];
    public static ItemStack TecTechPipeEnergyLowPower;

    public static void run() {
        if (newStuff) {
            GameRegistry.registerBlock(ItemRegistry.bw_fake_glasses, "BW_FakeGlasBlock");
            GameRegistry.registerBlock(ItemRegistry.BW_BLOCKS[2], BW_ItemBlocks.class, "BW_Machinery_Casings");
            GameRegistry.registerItem(ItemRegistry.LEATHER_ROTOR, "BW_LeatherRotor");
            GameRegistry.registerItem(ItemRegistry.WOOL_ROTOR, "BW_WoolRotor");
            GameRegistry.registerItem(ItemRegistry.PAPER_ROTOR, "BW_PaperRotor");
            GameRegistry.registerItem(ItemRegistry.COMBINED_ROTOR, "BW_CombinedRotor");
            GameRegistry.registerItem(ItemRegistry.CRAFTING_PARTS, "craftingParts");
            GameRegistry.registerTileEntity(BW_RotorBlock.class, "BWRotorBlockTE");
            GameRegistry.registerBlock(ItemRegistry.ROTORBLOCK, BW_ItemBlocks.class, "BWRotorBlock");
            GameRegistry.registerTileEntity(BW_TileEntity_HeatedWaterPump.class, "BWHeatedWaterPumpTE");
            GameRegistry.registerBlock(ItemRegistry.PUMPBLOCK, BW_ItemBlocks.class, "BWHeatedWaterPumpBlock");
            GameRegistry.registerItem(ItemRegistry.PUMPPARTS, "BWPumpParts");
            GameRegistry.registerItem(ItemRegistry.WINDMETER, "BW_SimpleWindMeter");
            GameRegistry.registerTileEntity(BW_TileEntity_ExperimentalFloodGate.class, "BWExpReversePump");
            GameRegistry.registerBlock(ItemRegistry.EXPPUMP, BW_ItemBlocks.class, "BWExpReversePumpBlock");
        }

        // GT2 stuff
        GameRegistry.registerBlock(ItemRegistry.BW_BLOCKS[0], BW_ItemBlocks.class, "BW_ItemBlocks");
        GameRegistry.registerBlock(ItemRegistry.BW_BLOCKS[1], BW_ItemBlocks.class, "GT_LESU_CASING");
        if (ConfigHandler.teslastaff)
            GameRegistry.registerItem(ItemRegistry.TESLASTAFF, ItemRegistry.TESLASTAFF.getUnlocalizedName());

        GameRegistry.registerItem(ItemRegistry.ROCKCUTTER_LV, ItemRegistry.ROCKCUTTER_LV.getUnlocalizedName());
        GameRegistry.registerItem(ItemRegistry.ROCKCUTTER_MV, ItemRegistry.ROCKCUTTER_MV.getUnlocalizedName());
        GameRegistry.registerItem(ItemRegistry.ROCKCUTTER_HV, ItemRegistry.ROCKCUTTER_HV.getUnlocalizedName());
        GameRegistry.registerItem(ItemRegistry.TAB, "tabIconGT2");
        if (newStuff) {
            if (ConfigHandler.creativeScannerID != 0)
                new CreativeScanner(
                        ConfigHandler.creativeScannerID, "Creative Debug Scanner", "Creative Debug Scanner", 20);
            ItemRegistry.eic = new GT_TileEntity_ElectricImplosionCompressor(
                            ConfigHandler.IDOffset + GT_Values.VN.length * 8 + 6,
                            "ElectricImplosionCompressor",
                            "Electric Implosion Compressor")
                    .getStackForm(1L);
            ItemRegistry.THTR = new GT_TileEntity_THTR(
                            ConfigHandler.IDOffset + GT_Values.VN.length * 8 + 5,
                            "THTR",
                            "Thorium High Temperature Reactor")
                    .getStackForm(1L);
            GT_TileEntity_THTR.THTRMaterials.registeraTHR_Materials();
            ItemRegistry.HTGR = new GT_TileEntity_HTGR(
                            ConfigHandler.IDOffset + GT_Values.VN.length * 8 + 15 + 48,
                            "HTGR",
                            "High Temperature Gas-cooled Reactor")
                    .getStackForm(1L);
            GT_TileEntity_HTGR.HTGRMaterials.registeraTHR_Materials();
            ItemRegistry.EIG = new GT_TileEntity_ExtremeIndustrialGreenhouse(
                            ConfigHandler.IDOffset + GT_Values.VN.length * 8 + 15 + 49,
                            "EIG",
                            "Extreme Industrial Greenhouse")
                    .getStackForm(1L);
            GT_OreDictUnificator.add(
                    OrePrefixes.block, Materials.BorosilicateGlass, new ItemStack(ItemRegistry.bw_glasses[0], 1, 0));
            GT_OreDictUnificator.registerOre(
                    OrePrefixes.block, Materials.NickelZincFerrite, new ItemStack(ItemRegistry.BW_BLOCKS[2]));
            for (int i = 0; i < GT_Values.VN.length - 1; i++) {
                ItemRegistry.diode2A[i] = new GT_MetaTileEntity_Diode(
                                ConfigHandler.IDOffset + GT_Values.VN.length + 1 + i,
                                "diode" + "2A" + GT_Values.VN[i],
                                StatCollector.translateToLocal("tile.diode.name") + " 2A " + GT_Values.VN[i],
                                i)
                        .getStackForm(1L);
                ItemRegistry.diode4A[i] = new GT_MetaTileEntity_Diode(
                                ConfigHandler.IDOffset + GT_Values.VN.length * 2 + 1 + i,
                                "diode" + "4A" + GT_Values.VN[i],
                                StatCollector.translateToLocal("tile.diode.name") + " 4A " + GT_Values.VN[i],
                                i)
                        .getStackForm(1L);
                ItemRegistry.diode8A[i] = new GT_MetaTileEntity_Diode(
                                ConfigHandler.IDOffset + GT_Values.VN.length * 3 + 1 + i,
                                "diode" + "8A" + GT_Values.VN[i],
                                StatCollector.translateToLocal("tile.diode.name") + " 8A " + GT_Values.VN[i],
                                i)
                        .getStackForm(1L);
                ItemRegistry.diode12A[i] = new GT_MetaTileEntity_Diode(
                                ConfigHandler.IDOffset + GT_Values.VN.length * 4 + 1 + i,
                                "diode" + "12A" + GT_Values.VN[i],
                                StatCollector.translateToLocal("tile.diode.name") + " 12A " + GT_Values.VN[i],
                                i)
                        .getStackForm(1L);
                ItemRegistry.diode16A[i] = new GT_MetaTileEntity_Diode(
                                ConfigHandler.IDOffset + GT_Values.VN.length * 5 + 1 + i,
                                "diode" + "16A" + GT_Values.VN[i],
                                StatCollector.translateToLocal("tile.diode.name") + " 16A " + GT_Values.VN[i],
                                i)
                        .getStackForm(1L);
                ItemRegistry.energyDistributor[i] = new GT_MetaTileEntity_EnergyDistributor(
                                ConfigHandler.IDOffset + 1 + i,
                                "energydistributor" + GT_Values.VN[i],
                                StatCollector.translateToLocal("tile.energydistributor.name") + " " + GT_Values.VN[i],
                                i)
                        .getStackForm(1L);
            }
            for (int i = 0; i < 3; i++) {
                ItemRegistry.acidGens[i] = new GT_MetaTileEntity_AcidGenerator(
                                ConfigHandler.IDOffset + GT_Values.VN.length * 8 - 2 + i,
                                "acidgenerator" + GT_Values.VN[i + 2],
                                StatCollector.translateToLocal("tile.acidgenerator.name") + " " + GT_Values.VN[i + 2],
                                i + 2)
                        .getStackForm(1);
            }

            ItemRegistry.acidGensLV = new GT_MetaTileEntity_AcidGenerator(
                            ConfigHandler.IDOffset + GT_Values.VN.length * 8 + 65,
                            "acidgenerator" + GT_Values.VN[1],
                            StatCollector.translateToLocal("tile.acidgenerator.name") + " " + GT_Values.VN[1],
                            +1)
                    .getStackForm(1L);

            ItemRegistry.dehp = new GT_TileEntity_DEHP(
                            ConfigHandler.IDOffset + GT_Values.VN.length * 8 + 1, 1, "DEHP", "Deep Earth Heating Pump")
                    .getStackForm(1L);
            ItemRegistry.megaMachines[0] = new GT_TileEntity_MegaBlastFurnace(
                            ConfigHandler.IDOffset + GT_Values.VN.length * 8 + 2,
                            "MegaBlastFurnace",
                            StatCollector.translateToLocal("tile.bw.mbf.name"))
                    .getStackForm(1L);
            ItemRegistry.megaMachines[1] = new GT_TileEntity_MegaVacuumFreezer(
                            ConfigHandler.IDOffset + GT_Values.VN.length * 8 + 3,
                            "MegaVacuumFreezer",
                            StatCollector.translateToLocal("tile.bw.mvf.name"))
                    .getStackForm(1L);
            ItemRegistry.cal = new GT_TileEntity_CircuitAssemblyLine(
                            ConfigHandler.IDOffset + GT_Values.VN.length * 8 + 7,
                            "CircuitAssemblyLine",
                            "Circuit Assembly Line")
                    .getStackForm(1L);
            ItemRegistry.compressedHatch = new GT_MetaTileEntity_CompressedFluidHatch(
                            ConfigHandler.IDOffset + GT_Values.VN.length * 8 + 8,
                            "CompressedFluidHatch",
                            "Liquid Air Fluid Hatch")
                    .getStackForm(1L);
            ItemRegistry.giantOutputHatch = new GT_MetaTileEntity_GiantOutputHatch(
                            ConfigHandler.IDOffset + GT_Values.VN.length * 8 + 9,
                            "GiantOutputHatch",
                            "Giant Output Hatch")
                    .getStackForm(1L);
            ItemRegistry.megaMachines[2] = new GT_TileEntity_MegaDistillTower(
                            ConfigHandler.IDOffset + GT_Values.VN.length * 8 + 10,
                            "MegaDistillationTower",
                            "Mega Distillation Tower")
                    .getStackForm(1L);
            ItemRegistry.megaMachines[3] = new GT_TileEntity_MegaChemicalReactor(
                            ConfigHandler.IDOffset + GT_Values.VN.length * 8 + 638,
                            "MegaChemicalReactor",
                            "Mega Chemical Reactor")
                    .getStackForm(1L);
            ItemRegistry.megaMachines[4] = new GT_TileEntity_MegaOilCracker(
                            ConfigHandler.IDOffset + GT_Values.VN.length * 8 + 639,
                            "MegaOilCracker",
                            "Mega Oil Cracker")
                    .getStackForm(1L);

            if (LoaderReference.galacticgreg && WerkstoffLoader.gtnhGT) {
                ItemRegistry.voidminer[2] = new GT_TileEntity_VoidMiners.VMUV(
                                ConfigHandler.IDOffset + GT_Values.VN.length * 8 + 11, "VoidMiner3", "Void Miner III")
                        .getStackForm(1L);
                ItemRegistry.voidminer[1] = new GT_TileEntity_VoidMiners.VMZPM(
                                ConfigHandler.IDOffset + GT_Values.VN.length * 8 + 12, "VoidMiner2", "Void Miner II")
                        .getStackForm(1L);
                ItemRegistry.voidminer[0] = new GT_TileEntity_VoidMiners.VMLUV(
                                ConfigHandler.IDOffset + GT_Values.VN.length * 8 + 13, "VoidMiner1", "Void Miner I")
                        .getStackForm(1L);
            }
            if (LoaderReference.tectech) {
                TecTechPipeEnergyLowPower = new TT_MetaTileEntity_Pipe_Energy_LowPower(
                                ConfigHandler.IDOffset + GT_Values.VN.length * 8 + 14,
                                "pipe.lowpowerlaser",
                                "Low Power Laser Pipe")
                        .getStackForm(1L);
                int startID = ConfigHandler.IDOffset + GT_Values.VN.length * 8 + 15;
                for (int amps = 32; amps <= 128; amps += 32) {
                    for (int tier = 4; tier < 8; tier++) {
                        TecTechLaserAdditions[0][amps / 32 - 1][tier - 4] = new TT_MetaTileEntity_LowPowerLaserBox(
                                        startID++,
                                        GT_Values.VN[tier] + "_LPLaser_Converter_" + amps,
                                        GT_Values.VN[tier] + " " + amps + "A/t" + " Low Power Laser Converter",
                                        tier,
                                        amps)
                                .getStackForm(1L);
                    }
                }
                for (int amps = 32; amps <= 128; amps += 32) {
                    for (int tier = 4; tier < 8; tier++) {
                        TecTechLaserAdditions[1][amps / 32 - 1][tier - 4] = new TT_MetaTileEntity_LowPowerLaserHatch(
                                        startID++,
                                        GT_Values.VN[tier] + "_LPLaser_Hatch_" + amps,
                                        GT_Values.VN[tier] + " " + amps + "A/t" + " Low Power Laser Target Hatch",
                                        tier,
                                        amps)
                                .getStackForm(1L);
                    }
                }
                for (int amps = 32; amps <= 128; amps += 32) {
                    for (int tier = 4; tier < 8; tier++) {
                        TecTechLaserAdditions[2][amps / 32 - 1][tier - 4] = new TT_MetaTileEntity_LowPowerLaserDynamo(
                                        startID++,
                                        GT_Values.VN[tier] + "_LPLaser_Dynamo_" + amps,
                                        GT_Values.VN[tier] + " " + amps + "A/t" + " Low Power Laser Source Hatch",
                                        tier,
                                        amps)
                                .getStackForm(1L);
                    }
                }
            }
        }
    }
}
