package com.github.bartimaeusnek.bartworks.common.loaders;

import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.common.ConfigHandler;
import com.github.bartimaeusnek.bartworks.common.blocks.BW_Blocks;
import com.github.bartimaeusnek.bartworks.common.blocks.BW_TileEntityContainer;
import com.github.bartimaeusnek.bartworks.common.items.*;
import com.github.bartimaeusnek.bartworks.common.tileentities.BW_RotorBlock;
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
import static com.github.bartimaeusnek.bartworks.common.ConfigHandler.newStuff;

public class ItemRegistry implements Runnable {

    public static final Item Destructopack = new GT_Destructopack_Item();
    public static final Item Teslastaff = new GT_Teslastaff_Item();
    public static final Item RockcutterLV = new GT_Rockcutter_Item(1);
    public static final Item RockcutterMV = new GT_Rockcutter_Item(2);
    public static final Item RockcutterHV = new GT_Rockcutter_Item(3);
    public static final Item CircuitProgrammer = new Circuit_Programmer();
    public static ItemStack[] Diode2A= new ItemStack[GT_Values.VN.length];
    public static ItemStack[] Diode4A= new ItemStack[GT_Values.VN.length];
    public static ItemStack[] Diode8A= new ItemStack[GT_Values.VN.length];
    public static ItemStack[] Diode12A= new ItemStack[GT_Values.VN.length];
    public static ItemStack[] Diode16A= new ItemStack[GT_Values.VN.length];
    public static final Block ROTORBLOCK = new BW_TileEntityContainer(Material.wood, BW_RotorBlock.class,"BWRotorBlock");

    public static final Item LeatherRotor = new BW_Stonage_Rotors(5, 0.15f, 15, 30,2400, IKineticRotor.GearboxType.WIND,new ResourceLocation(MainMod.modID, "textures/items/rotors/rotorLeather.png"),"BW_LeatherRotor","rotors/itemRotorLeather");
    public static final Item WoolRotor = new BW_Stonage_Rotors(7, 0.18f, 10, 20,1600, IKineticRotor.GearboxType.WIND,new ResourceLocation(MainMod.modID, "textures/items/rotors/rotorWool.png"),"BW_WoolRotor","rotors/itemRotorWool");
    public static final Item PaperRotor = new BW_Stonage_Rotors(9, 0.2f, 1, 10,800, IKineticRotor.GearboxType.WIND,new ResourceLocation(MainMod.modID, "textures/items/rotors/rotorPaper.png"),"BW_PaperRotor","rotors/itemRotorPaper");
    public static final Item CombinedRotor = new BW_Stonage_Rotors(11, 0.22f, 1, 50,5800, IKineticRotor.GearboxType.WIND,new ResourceLocation(MainMod.modID, "textures/items/rotors/rotorCombined.png"),"BW_CombinedRotor","rotors/itemRotorCombined");
    public static final Item craftingParts = new SimpleSubItemClass(new String[]{"grindstone_top","grindstone_bottom","completed_grindstone","rotors/leatherParts","rotors/woolParts","rotors/paperParts","rotors/combinedParts"});


    public static final Item tab = new SimpleIconItem("GT2Coin");
    public static final Block[] BW_BLOCKS = {
            new BW_Blocks("BW_ItemBlocks", new String[]
            {
                    MainMod.modID+":EtchedLapisCell",
                    MainMod.modID+":PlatedLapisCell",
            },GT2),

            new BW_Blocks("GT_LESU_CASING", new String[]{
                    MainMod.modID+":LESU_CELL",
            },GT2),
            new BW_Blocks("BW_Machinery_Casings",new String[]{
                    MainMod.modID+":NickelFerriteBlocks",
                    MainMod.modID+":TransformerCoil",
                   // MainMod.modID+":DEHP_Casing",
                   // MainMod.modID+":DEHP_Casing_Base"
            },BWT),

    };


    @Override
    public void run() {

        if (newStuff) {
            GameRegistry.registerBlock(BW_BLOCKS[2], BW_ItemBlocks.class, "BW_Machinery_Casings");
            GT_OreDictUnificator.registerOre(OrePrefixes.block, Materials.NickelZincFerrite, new ItemStack(BW_BLOCKS[2]));

            GameRegistry.registerItem(LeatherRotor,"BW_LeatherRotor");
            GameRegistry.registerItem(WoolRotor,"BW_WoolRotor");
            GameRegistry.registerItem(PaperRotor,"BW_PaperRotor");
            GameRegistry.registerItem(CombinedRotor,"BW_CombinedRotor");
            GameRegistry.registerItem(craftingParts,"craftingParts");
            GameRegistry.registerTileEntity(BW_RotorBlock.class,"BWRotorBlockTE");
            GameRegistry.registerBlock(ROTORBLOCK,"BWRotorBlock");
        }


        //GT2 stuff
        GameRegistry.registerBlock(BW_BLOCKS[0], BW_ItemBlocks.class,"BW_ItemBlocks");
        GameRegistry.registerBlock(BW_BLOCKS[1], BW_ItemBlocks.class,"GT_LESU_CASING");
        if (ConfigHandler.teslastaff)
            GameRegistry.registerItem(Teslastaff,Teslastaff.getUnlocalizedName());

        GameRegistry.registerItem(RockcutterLV,RockcutterLV.getUnlocalizedName());
        GameRegistry.registerItem(RockcutterMV,RockcutterMV.getUnlocalizedName());
        GameRegistry.registerItem(RockcutterHV,RockcutterHV.getUnlocalizedName());
        GameRegistry.registerItem(tab,"tabIconGT2");
    }
}
