package com.github.bartimaeusnek.bartworks.common.loaders;

import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.common.ConfigHandler;
import com.github.bartimaeusnek.bartworks.common.blocks.BW_Blocks;
import com.github.bartimaeusnek.bartworks.common.items.*;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class ItemRegistry implements Runnable {

    public static final Item Destructopack = new GT_Destructopack_Item();
    public static final Item Teslastaff = new GT_Teslastaff_Item();
    public static final Item RockcutterLV = new GT_Rockcutter_Item(1);
    public static final Item RockcutterMV = new GT_Rockcutter_Item(2);
    public static final Item RockcutterHV = new GT_Rockcutter_Item(3);
    public static final Item tab = new SimpleIconItem("GT2Coin");
    public static final Block[] BW_BLOCKS = {
            new BW_Blocks("BW_ItemBlocks", new String[]
            {
                    MainMod.modID+":EtchedLapisCell",
                    MainMod.modID+":PlatedLapisCell",
             }),

            new BW_Blocks("GT_LESU_CASING", new String[]{
                    MainMod.modID+":LESU_CELL",
            }),

    };


    @Override
    public void run() {

        GameRegistry.registerBlock(BW_BLOCKS[0], BW_ItemBlocks.class,"BW_ItemBlocks");

        GameRegistry.registerBlock(BW_BLOCKS[1],BW_ItemBlocks.class,"GT_LESU_CASING");

        if (ConfigHandler.teslastaff)
            GameRegistry.registerItem(Teslastaff,Teslastaff.getUnlocalizedName());

        GameRegistry.registerItem(RockcutterLV,RockcutterLV.getUnlocalizedName());
        GameRegistry.registerItem(RockcutterMV,RockcutterMV.getUnlocalizedName());
        GameRegistry.registerItem(RockcutterHV,RockcutterHV.getUnlocalizedName());
        GameRegistry.registerItem(tab,"tabIconGT2");
    }
}
