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
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.common.items.BW_ItemBlocks;
import com.github.bartimaeusnek.bartworks.common.tileentities.classic.BW_RotorBlock;
import com.github.bartimaeusnek.bartworks.common.tileentities.classic.BW_TileEntity_ExperimentalFloodGate;
import com.github.bartimaeusnek.bartworks.common.tileentities.classic.BW_TileEntity_HeatedWaterPump;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.util.GT_OreDictUnificator;
import ic2.core.Ic2Items;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;

import static com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler.newStuff;

public class BeforeGTPreload implements Runnable {
    @Override
    public void run() {
        Field fieldModController = FieldUtils.getDeclaredField(Loader.class,"modController",true);
        LoadController modController = null;
        try {
            modController = (LoadController) fieldModController.get(Loader.instance());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            FMLCommonHandler.instance().exitJava(-1,true);
        }
        ModContainer bartworks = null;
        ModContainer gregtech = null;
        assert modController != null;
        for (ModContainer mod : modController.getActiveModList()){
            if (mod.getModId().equals(MainMod.MOD_ID)) {
                bartworks = mod;
            }
            if (mod.getModId().equals("gregtech"))
                gregtech=mod;
            if (bartworks!= null && gregtech != null)
                break;
        }
        if (bartworks == null || gregtech == null)
            FMLCommonHandler.instance().exitJava(-1,true);
        Field activeContainer = FieldUtils.getDeclaredField(LoadController.class,"activeContainer",true);

        try {
            activeContainer.set(modController,bartworks);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            FMLCommonHandler.instance().exitJava(-1,true);
        }

        if (newStuff) {
            GameRegistry.registerBlock(ItemRegistry.bw_glasses[0], BW_ItemBlocks.class, "BW_GlasBlocks");
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

        //GT2 stuff
        GameRegistry.registerBlock(ItemRegistry.BW_BLOCKS[0], BW_ItemBlocks.class, "BW_ItemBlocks");
        GameRegistry.registerBlock(ItemRegistry.BW_BLOCKS[1], BW_ItemBlocks.class, "GT_LESU_CASING");
        if (ConfigHandler.teslastaff)
            GameRegistry.registerItem(ItemRegistry.TESLASTAFF, ItemRegistry.TESLASTAFF.getUnlocalizedName());

        GameRegistry.registerItem(ItemRegistry.ROCKCUTTER_LV, ItemRegistry.ROCKCUTTER_LV.getUnlocalizedName());
        GameRegistry.registerItem(ItemRegistry.ROCKCUTTER_MV, ItemRegistry.ROCKCUTTER_MV.getUnlocalizedName());
        GameRegistry.registerItem(ItemRegistry.ROCKCUTTER_HV, ItemRegistry.ROCKCUTTER_HV.getUnlocalizedName());
        GameRegistry.registerItem(ItemRegistry.TAB, "tabIconGT2");

        OreDictionary.registerOre("blockGlassHV",new ItemStack(Blocks.glass,1,Short.MAX_VALUE));
        OreDictionary.registerOre("blockGlassHV",new ItemStack(ItemRegistry.bw_glasses[0],1,0));
        OreDictionary.registerOre("blockGlassEV", Ic2Items.reinforcedGlass);
        OreDictionary.registerOre("blockGlassEV",new ItemStack(ItemRegistry.bw_glasses[0],1,1));
        OreDictionary.registerOre("blockGlassIV",new ItemStack(ItemRegistry.bw_glasses[0],1,2));
        OreDictionary.registerOre("blockGlassIV",new ItemStack(ItemRegistry.bw_glasses[0],1,12));
        OreDictionary.registerOre("blockGlassLuV",new ItemStack(ItemRegistry.bw_glasses[0],1,3));
        OreDictionary.registerOre("blockGlassZPM",new ItemStack(ItemRegistry.bw_glasses[0],1,4));
        OreDictionary.registerOre("blockGlassUV",new ItemStack(ItemRegistry.bw_glasses[0],1,5));

        try {
            activeContainer.set(modController,gregtech);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            FMLCommonHandler.instance().exitJava(-1,true);
        }
    }
}
