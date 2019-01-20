/*
 * Copyright (c) 2019 bartimaeusnek
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

import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.common.blocks.BW_Blocks;
import com.github.bartimaeusnek.bartworks.common.blocks.BW_TileEntityContainer;
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.common.items.*;
import com.github.bartimaeusnek.bartworks.common.tileentities.*;
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

import static com.github.bartimaeusnek.bartworks.MainMod.BWT;
import static com.github.bartimaeusnek.bartworks.MainMod.GT2;
import static com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler.newStuff;

public class ItemRegistry {

    public static final Item DESTRUCTOPACK = new GT_Destructopack_Item();
    public static final Item TESLASTAFF = new GT_Teslastaff_Item();
    public static final Item ROCKCUTTER_LV = new GT_Rockcutter_Item(1);
    public static final Item ROCKCUTTER_MV = new GT_Rockcutter_Item(2);
    public static final Item ROCKCUTTER_HV = new GT_Rockcutter_Item(3);
    public static final Item CIRCUIT_PROGRAMMER = new Circuit_Programmer();
    public static final Block ROTORBLOCK = new BW_TileEntityContainer(Material.wood, BW_RotorBlock.class, "BWRotorBlock");
    public static final Item LEATHER_ROTOR = new BW_Stonage_Rotors(5, 0.15f, 15, 30, 2400, IKineticRotor.GearboxType.WIND, new ResourceLocation(MainMod.modID, "textures/items/rotors/rotorLeather.png"), "BW_LeatherRotor", "rotors/itemRotorLeather");
    public static final Item WOOL_ROTOR = new BW_Stonage_Rotors(7, 0.18f, 10, 20, 1600, IKineticRotor.GearboxType.WIND, new ResourceLocation(MainMod.modID, "textures/items/rotors/rotorWool.png"), "BW_WoolRotor", "rotors/itemRotorWool");
    public static final Item PAPER_ROTOR = new BW_Stonage_Rotors(9, 0.2f, 1, 10, 800, IKineticRotor.GearboxType.WIND, new ResourceLocation(MainMod.modID, "textures/items/rotors/rotorPaper.png"), "BW_PaperRotor", "rotors/itemRotorPaper");
    public static final Item COMBINED_ROTOR = new BW_Stonage_Rotors(11, 0.22f, 1, 50, 5800, IKineticRotor.GearboxType.WIND, new ResourceLocation(MainMod.modID, "textures/items/rotors/rotorCombined.png"), "BW_CombinedRotor", "rotors/itemRotorCombined");
    public static final Item CRAFTING_PARTS = new SimpleSubItemClass(new String[]{"grindstone_top", "grindstone_bottom", "completed_grindstone", "rotors/leatherParts", "rotors/woolParts", "rotors/paperParts", "rotors/combinedParts" });
    public static final Item TAB = new SimpleIconItem("GT2Coin");
    public static final Item WINDMETER = new BW_SimpleWindMeter();
    public static ItemStack dehp;
    public static final Block[] BW_BLOCKS = {
            new BW_Blocks("BW_ItemBlocks", new String[]
                    {
                            MainMod.modID + ":EtchedLapisCell",
                            MainMod.modID + ":PlatedLapisCell",
                    }, GT2),

            new BW_Blocks("GT_LESU_CASING", new String[]{
                    MainMod.modID + ":LESU_CELL",
            }, GT2),
            new BW_Blocks("BW_Machinery_Casings", new String[]{
                    MainMod.modID + ":NickelFerriteBlocks",
                    MainMod.modID + ":TransformerCoil",
                    // MainMod.modID+":DEHP_Casing",
                    // MainMod.modID+":DEHP_Casing_Base"
            }, BWT),

    };

    public static ItemStack[] diode2A = new ItemStack[GT_Values.VN.length];
    public static ItemStack[] diode4A = new ItemStack[GT_Values.VN.length];
    public static ItemStack[] diode8A = new ItemStack[GT_Values.VN.length];
    public static ItemStack[] diode12A = new ItemStack[GT_Values.VN.length];
    public static ItemStack[] diode16A = new ItemStack[GT_Values.VN.length];
    public static ItemStack[] energyDistributor = new ItemStack[GT_Values.VN.length];
    public static ItemStack[] acidGens = new ItemStack[3];

    public static void run() {

        if (newStuff) {
            GameRegistry.registerBlock(BW_BLOCKS[2], BW_ItemBlocks.class, "BW_Machinery_Casings");
            GT_OreDictUnificator.registerOre(OrePrefixes.block, Materials.NickelZincFerrite, new ItemStack(BW_BLOCKS[2]));
            GameRegistry.registerItem(LEATHER_ROTOR, "BW_LeatherRotor");
            GameRegistry.registerItem(WOOL_ROTOR, "BW_WoolRotor");
            GameRegistry.registerItem(PAPER_ROTOR, "BW_PaperRotor");
            GameRegistry.registerItem(COMBINED_ROTOR, "BW_CombinedRotor");
            GameRegistry.registerItem(CRAFTING_PARTS, "craftingParts");
            GameRegistry.registerTileEntity(BW_RotorBlock.class, "BWRotorBlockTE");
            GameRegistry.registerBlock(ROTORBLOCK, "BWRotorBlock");
            GameRegistry.registerItem(WINDMETER, "BW_SimpleWindMeter");

            for (int i = 0; i < GT_Values.VN.length; i++) {
                ItemRegistry.diode2A[i] = new GT_MetaTileEntity_Diode(ConfigHandler.IDOffset + GT_Values.VN.length + 1 + i, "Cable Diode 2A " + GT_Values.VN[i], "Cable Diode 2A " + GT_Values.VN[i], i, 2).getStackForm(1L);
                ItemRegistry.diode4A[i] = new GT_MetaTileEntity_Diode(ConfigHandler.IDOffset + GT_Values.VN.length * 2 + 1 + i, "Cable Diode 4A " + GT_Values.VN[i], "Cable Diode 4A " + GT_Values.VN[i], i, 4).getStackForm(1L);
                ItemRegistry.diode8A[i] = new GT_MetaTileEntity_Diode(ConfigHandler.IDOffset + GT_Values.VN.length * 3 + 1 + i, "Cable Diode 8A " + GT_Values.VN[i], "Cable Diode 8A " + GT_Values.VN[i], i, 8).getStackForm(1L);
                ItemRegistry.diode12A[i] = new GT_MetaTileEntity_Diode(ConfigHandler.IDOffset + GT_Values.VN.length * 4 + 1 + i, "Cable Diode 12A " + GT_Values.VN[i], "Cable Diode 12A " + GT_Values.VN[i], i, 12).getStackForm(1L);
                ItemRegistry.diode16A[i] = new GT_MetaTileEntity_Diode(ConfigHandler.IDOffset + GT_Values.VN.length * 5 + 1 + i, "Cable Diode 16A " + GT_Values.VN[i], "Cable Diode 16A " + GT_Values.VN[i], i, 16).getStackForm(1L);
                ItemRegistry.energyDistributor[i] = new GT_MetaTileEntity_EnergyDistributor(ConfigHandler.IDOffset + 1 + i, "Energy Distributor " + GT_Values.VN[i], "Energy Distributor " + GT_Values.VN[i], i, "Splits Amperage into several Sides").getStackForm(1L);

            }
            for (int i = 0; i < 3; i++) {
                ItemRegistry.acidGens[i] = new GT_MetaTileEntity_AcidGenerator(ConfigHandler.IDOffset + GT_Values.VN.length * 8 - 2 + i, "Acid Generator " + GT_Values.VN[i + 2], "Acid Generator " + GT_Values.VN[i + 2], i + 2, new String[]{"An Acid Generator", "Creates Power from Chemical Energy Potentials." }).getStackForm(1);
            }
            dehp = new GT_TileEntity_DEHP(ConfigHandler.IDOffset + GT_Values.VN.length * 8 + 1, 1, "DEHP", "Deep Earth Heating Pump").getStackForm(1L);
        }


        //GT2 stuff
        GameRegistry.registerBlock(BW_BLOCKS[0], BW_ItemBlocks.class, "BW_ItemBlocks");
        GameRegistry.registerBlock(BW_BLOCKS[1], BW_ItemBlocks.class, "GT_LESU_CASING");
        if (ConfigHandler.teslastaff)
            GameRegistry.registerItem(TESLASTAFF, TESLASTAFF.getUnlocalizedName());

        GameRegistry.registerItem(ROCKCUTTER_LV, ROCKCUTTER_LV.getUnlocalizedName());
        GameRegistry.registerItem(ROCKCUTTER_MV, ROCKCUTTER_MV.getUnlocalizedName());
        GameRegistry.registerItem(ROCKCUTTER_HV, ROCKCUTTER_HV.getUnlocalizedName());
        GameRegistry.registerItem(TAB, "tabIconGT2");
    }
}
