package com.github.bartimaeusnek.bartworks.common.loaders;

import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.common.ConfigHandler;
import com.github.bartimaeusnek.bartworks.common.blocks.BW_Blocks;
import com.github.bartimaeusnek.bartworks.common.items.*;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static com.github.bartimaeusnek.bartworks.MainMod.BWT;
import static com.github.bartimaeusnek.bartworks.MainMod.GT2;

public class ItemRegistry implements Runnable {

    public static final Item Destructopack = new GT_Destructopack_Item();
    public static final Item Teslastaff = new GT_Teslastaff_Item();
    public static final Item RockcutterLV = new GT_Rockcutter_Item(1);
    public static final Item RockcutterMV = new GT_Rockcutter_Item(2);
    public static final Item RockcutterHV = new GT_Rockcutter_Item(3);
    public static ItemStack[] Diode2A= new ItemStack[GT_Values.VN.length];
    public static ItemStack[] Diode4A= new ItemStack[GT_Values.VN.length];
    public static ItemStack[] Diode8A= new ItemStack[GT_Values.VN.length];
    public static ItemStack[] Diode12A= new ItemStack[GT_Values.VN.length];
    public static ItemStack[] Diode16A= new ItemStack[GT_Values.VN.length];
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
                    MainMod.modID+":DEHP_Casing",
                    MainMod.modID+":DEHP_Casing_Base"
            },BWT),

    };


    @Override
    public void run() {

        GameRegistry.registerBlock(BW_BLOCKS[0], BW_ItemBlocks.class,"BW_ItemBlocks");
        GameRegistry.registerBlock(BW_BLOCKS[1], BW_ItemBlocks.class,"GT_LESU_CASING");
        GameRegistry.registerBlock(BW_BLOCKS[2], BW_ItemBlocks.class,"BW_Machinery_Casings");

        GT_OreDictUnificator.registerOre(OrePrefixes.block, Materials.NickelZincFerrite,new ItemStack(BW_BLOCKS[2]));

        if (ConfigHandler.teslastaff)
            GameRegistry.registerItem(Teslastaff,Teslastaff.getUnlocalizedName());

        GameRegistry.registerItem(RockcutterLV,RockcutterLV.getUnlocalizedName());
        GameRegistry.registerItem(RockcutterMV,RockcutterMV.getUnlocalizedName());
        GameRegistry.registerItem(RockcutterHV,RockcutterHV.getUnlocalizedName());
        GameRegistry.registerItem(tab,"tabIconGT2");
    }
}
