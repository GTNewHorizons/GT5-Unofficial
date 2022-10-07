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

import com.github.bartimaeusnek.bartworks.common.items.BW_ItemBlocks;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SubTag;
import ic2.core.Ic2Items;
import java.lang.reflect.Field;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.reflect.FieldUtils;

/**
 * This class gets injected into GT via ASM!
 * DO NOT CALL IT YOURSELF!
 */
public class BeforeGTPreload implements Runnable {

    private static boolean didrun;

    @Override
    public void run() {
        if (didrun) return;
        // fixing BorosilicateGlass... -_-'
        Materials.BorosilicateGlass.add(
                SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_RECYCLING, SubTag.SMELTING_TO_FLUID);

        Field activeContainer = FieldUtils.getDeclaredField(LoadController.class, "activeContainer", true);
        ModContainer bartworks = null;
        ModContainer gregtech = Loader.instance().activeModContainer();
        boolean switchback = false;
        LoadController modController = null;
        if (!Loader.instance().activeModContainer().getModId().equals("bartworks")) {
            Field fieldModController = FieldUtils.getDeclaredField(Loader.class, "modController", true);
            try {
                modController = (LoadController) fieldModController.get(Loader.instance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                FMLCommonHandler.instance().exitJava(-1, true);
            }

            assert modController != null;
            for (ModContainer mod : modController.getActiveModList()) {
                if (mod.getModId().equals("bartworks")) {
                    bartworks = mod;
                }
                if (bartworks != null) break;
            }
            if (bartworks == null || gregtech == null)
                FMLCommonHandler.instance().exitJava(-1, true);

            try {
                activeContainer.set(modController, bartworks);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                FMLCommonHandler.instance().exitJava(-1, true);
            }
            switchback = true;
        }

        Block[] bw_glasses;
        try {
            bw_glasses = (Block[]) Class.forName("com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry")
                    .getField("bw_glasses")
                    .get(null);
            GameRegistry.registerBlock(bw_glasses[0], BW_ItemBlocks.class, "BW_GlasBlocks");
            GameRegistry.registerBlock(bw_glasses[1], BW_ItemBlocks.class, "BW_GlasBlocks2");
            OreDictionary.registerOre("blockGlassHV", new ItemStack(Blocks.glass, 1, Short.MAX_VALUE));
            OreDictionary.registerOre("blockGlassHV", new ItemStack(bw_glasses[0], 1, 0));
            OreDictionary.registerOre("blockGlassEV", Ic2Items.reinforcedGlass);
            OreDictionary.registerOre("blockGlassEV", new ItemStack(bw_glasses[0], 1, 1));
            OreDictionary.registerOre("blockGlassIV", new ItemStack(bw_glasses[0], 1, 12));
            OreDictionary.registerOre("blockGlassIV", new ItemStack(bw_glasses[0], 1, 2));
            OreDictionary.registerOre("blockGlassLuV", new ItemStack(bw_glasses[0], 1, 3));
            OreDictionary.registerOre("blockGlassZPM", new ItemStack(bw_glasses[0], 1, 4));
            OreDictionary.registerOre("blockGlassUV", new ItemStack(bw_glasses[0], 1, 5));
            OreDictionary.registerOre("blockGlassUHV", new ItemStack(bw_glasses[0], 1, 13));
            OreDictionary.registerOre("blockGlassUEV", new ItemStack(bw_glasses[0], 1, 14));
            OreDictionary.registerOre("blockGlassUIV", new ItemStack(bw_glasses[0], 1, 15));
            OreDictionary.registerOre("blockGlassUMV", new ItemStack(bw_glasses[1], 1, 0));
        } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException e) {
            e.printStackTrace();
            FMLCommonHandler.instance().exitJava(-1, true);
        }
        if (switchback) {
            try {
                activeContainer.set(modController, gregtech);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                FMLCommonHandler.instance().exitJava(-1, true);
            }
        }
        BeforeGTPreload.didrun = true;
    }
}
