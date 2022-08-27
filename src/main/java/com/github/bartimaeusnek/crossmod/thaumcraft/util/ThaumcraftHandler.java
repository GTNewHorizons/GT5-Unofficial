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

package com.github.bartimaeusnek.crossmod.thaumcraft.util;

import com.github.bartimaeusnek.bartworks.API.API_ConfigValues;
import com.github.bartimaeusnek.bartworks.util.Pair;
import com.github.bartimaeusnek.bartworks.util.log.DebugLog;
import gregtech.api.enums.TC_Aspects;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.biome.BiomeGenBase;

@SuppressWarnings({"rawtypes", "unchecked", "unused"})
public class ThaumcraftHandler {
    private ThaumcraftHandler() {}

    private static Integer taintBiomeID;
    private static Integer magicalForestBiomeID;
    private static Class mWandInterface;

    static {
        try {
            ThaumcraftHandler.mWandInterface = Class.forName("thaumcraft.common.items.wands.ItemWandCasting");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean isWand(ItemStack aStack) {
        try {
            return aStack != null
                    && ThaumcraftHandler.mWandInterface.isAssignableFrom(
                            aStack.getItem().getClass());
        } catch (Throwable var3) {
            return false;
        }
    }

    public static boolean isMagicalForestBiome(int biomeID) {
        if (ThaumcraftHandler.magicalForestBiomeID == null) {
            try {
                BiomeGenBase biome =
                        (BiomeGenBase) Class.forName("thaumcraft.common.lib.world.ThaumcraftWorldGenerator")
                                .getField("biomeMagicalForest")
                                .get(null);
                return biomeID == (ThaumcraftHandler.magicalForestBiomeID = biome.biomeID);
            } catch (ClassCastException | ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                return false;
            }
        }
        return biomeID == ThaumcraftHandler.magicalForestBiomeID;
    }

    public static boolean isTaintBiome(int biomeID) {
        if (ThaumcraftHandler.taintBiomeID == null) {
            try {
                BiomeGenBase TaintBiome =
                        (BiomeGenBase) Class.forName("thaumcraft.common.lib.world.ThaumcraftWorldGenerator")
                                .getField("biomeTaint")
                                .get(null);
                return biomeID == (ThaumcraftHandler.taintBiomeID = TaintBiome.biomeID);
            } catch (ClassCastException | ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                return false;
            }
        }
        return biomeID == ThaumcraftHandler.taintBiomeID;
    }

    public static class AspectAdder {
        public static Class mAspectListClass;
        public static Class mAspectClass;
        public static Method registerObjectTag;
        public static Method addToList;
        public static Method getName;
        public static Method writeAspectListToNBT;
        public static Method readAspectListFromNBT;
        public static Method add;
        public static Method getAmount;
        public static Method getAspects;
        public static Method isResearchComplete;
        public static Field linkedAspektList;

        static {
            try {
                ThaumcraftHandler.AspectAdder.mAspectListClass = Class.forName("thaumcraft.api.aspects.AspectList");
                ThaumcraftHandler.AspectAdder.mAspectClass = Class.forName("thaumcraft.api.aspects.Aspect");
                ThaumcraftHandler.AspectAdder.addToList = ThaumcraftHandler.AspectAdder.mAspectListClass.getMethod(
                        "add", ThaumcraftHandler.AspectAdder.mAspectClass, int.class);
                ThaumcraftHandler.AspectAdder.registerObjectTag = Class.forName("thaumcraft.api.ThaumcraftApi")
                        .getMethod(
                                "registerObjectTag", ItemStack.class, ThaumcraftHandler.AspectAdder.mAspectListClass);
                ThaumcraftHandler.AspectAdder.getName = ThaumcraftHandler.AspectAdder.mAspectClass.getMethod("getName");
                ThaumcraftHandler.AspectAdder.writeAspectListToNBT =
                        ThaumcraftHandler.AspectAdder.mAspectListClass.getMethod("writeToNBT", NBTTagCompound.class);
                ThaumcraftHandler.AspectAdder.add = ThaumcraftHandler.AspectAdder.mAspectListClass.getMethod(
                        "add", ThaumcraftHandler.AspectAdder.mAspectListClass);
                ThaumcraftHandler.AspectAdder.getAmount = ThaumcraftHandler.AspectAdder.mAspectListClass.getMethod(
                        "getAmount", ThaumcraftHandler.AspectAdder.mAspectClass);
                ThaumcraftHandler.AspectAdder.getAspects =
                        ThaumcraftHandler.AspectAdder.mAspectListClass.getMethod("getAspects");
                ThaumcraftHandler.AspectAdder.readAspectListFromNBT =
                        ThaumcraftHandler.AspectAdder.mAspectListClass.getMethod("readFromNBT", NBTTagCompound.class);
                ThaumcraftHandler.AspectAdder.isResearchComplete = Class.forName(
                                "thaumcraft.common.lib.research.ResearchManager")
                        .getMethod("isResearchComplete", String.class, String.class);
                ThaumcraftHandler.AspectAdder.linkedAspektList =
                        ThaumcraftHandler.AspectAdder.mAspectListClass.getField("aspects");
            } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        public static void addAspectViaBW(ItemStack stack, Pair<Object, Integer>... aspectPair) {
            if (stack == null || stack.getItem() == null || stack.getUnlocalizedName() == null) return;
            try {
                Object aspectList = ThaumcraftHandler.AspectAdder.mAspectListClass.newInstance();
                for (Pair a : aspectPair) {
                    if (API_ConfigValues.debugLog)
                        DebugLog.log("Stack:" + stack.getDisplayName() + " Damage:" + stack.getItemDamage()
                                + " aspectPair: " + ThaumcraftHandler.AspectAdder.getName.invoke(a.getKey()) + " / "
                                + a.getValue());
                    ThaumcraftHandler.AspectAdder.addToList.invoke(aspectList, a.getKey(), a.getValue());
                }
                ThaumcraftHandler.AspectAdder.registerObjectTag.invoke(null, stack, aspectList);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        public static void addAspectViaGT(ItemStack stack, TC_Aspects.TC_AspectStack... tc_aspectStacks) {
            try {
                Object aspectList = ThaumcraftHandler.AspectAdder.mAspectListClass.newInstance();
                for (TC_Aspects.TC_AspectStack tc_aspects : tc_aspectStacks)
                    ThaumcraftHandler.AspectAdder.addToList.invoke(
                            aspectList, tc_aspects.mAspect.mAspect, (int) tc_aspects.mAmount);
                ThaumcraftHandler.AspectAdder.registerObjectTag.invoke(null, stack, aspectList);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
