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

package bwcrossmod.thaumcraft.util;

import net.minecraft.item.ItemStack;

import bartworks.API.APIConfigValues;
import bartworks.util.Pair;
import bartworks.util.log.DebugLog;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;

public class ThaumcraftHandler {

    public static boolean isMagicalForestBiome(int biomeID) {
        return biomeID == ThaumcraftWorldGenerator.biomeMagicalForest.biomeID;
    }

    public static boolean isTaintBiome(int biomeID) {
        return biomeID == ThaumcraftWorldGenerator.biomeTaint.biomeID;
    }

    public static class AspectAdder {

        @SafeVarargs
        public static void addAspectViaBW(ItemStack stack, Pair<Object, Integer>... aspectPair) {
            if (stack == null || stack.getItem() == null || stack.getUnlocalizedName() == null) return;
            AspectList aspectList = new AspectList();
            for (Pair<Object, Integer> a : aspectPair) {
                Aspect aspect = (Aspect) a.getKey();
                int amount = a.getValue();
                if (APIConfigValues.debugLog) DebugLog.log(
                    "Stack:" + stack.getDisplayName()
                        + " Damage:"
                        + stack.getItemDamage()
                        + " aspectPair: "
                        + aspect.getName()
                        + " / "
                        + amount);
                aspectList.add(aspect, amount);
            }
            ThaumcraftApi.registerObjectTag(stack, aspectList);
        }
    }
}
