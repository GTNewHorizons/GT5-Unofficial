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

package com.github.bartimaeusnek.bartworks.system.oredict;

import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.util.Pair;
import com.github.bartimaeusnek.crossmod.BartWorksCrossmod;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictHandler {

    private static final HashMap<String, Pair<Integer, Short>> cache = new HashMap<>();
    private static final HashSet<Pair<Integer, Short>> cacheNonBW = new HashSet<>();

    public static HashMap<String, Pair<Integer, Short>> getCache() {
        return OreDictHandler.cache;
    }

    public static HashSet<Pair<Integer, Short>> getNonBWCache() {
        return OreDictHandler.cacheNonBW;
    }

    public static void adaptCacheForWorld() {
        Set<String> used = new HashSet<>(OreDictHandler.cache.keySet());
        OreDictHandler.cache.clear();
        OreDictHandler.cacheNonBW.clear();
        for (String s : used) {
            if (!OreDictionary.getOres(s).isEmpty()) {
                ItemStack tmpstack = OreDictionary.getOres(s).get(0).copy();
                Pair<Integer, Short> p =
                        new Pair<>(Item.getIdFromItem(tmpstack.getItem()), (short) tmpstack.getItemDamage());
                OreDictHandler.cache.put(s, p);
                for (ItemStack tmp : OreDictionary.getOres(s)) {
                    Pair<Integer, Short> p2 =
                            new Pair<>(Item.getIdFromItem(tmp.getItem()), (short) tmp.getItemDamage());
                    GameRegistry.UniqueIdentifier UI = GameRegistry.findUniqueIdentifierFor(tmp.getItem());
                    if (UI == null) UI = GameRegistry.findUniqueIdentifierFor(Block.getBlockFromItem(tmp.getItem()));
                    if (!UI.modId.equals(MainMod.MOD_ID)
                            && !UI.modId.equals(BartWorksCrossmod.MOD_ID)
                            && !UI.modId.equals("BWCore")) {
                        OreDictHandler.cacheNonBW.add(p2);
                    }
                }
            }
        }
    }

    public static ItemStack getItemStack(String elementName, OrePrefixes prefixes, int amount) {
        if (OreDictHandler.cache.get(prefixes + elementName.replaceAll(" ", "")) != null) {
            Pair<Integer, Short> p = OreDictHandler.cache.get(prefixes + elementName.replaceAll(" ", ""));
            return new ItemStack(Item.getItemById(p.getKey()), amount, p.getValue());
        } else if (!OreDictionary.getOres(prefixes + elementName.replaceAll(" ", ""))
                .isEmpty()) {
            ItemStack tmp = GT_OreDictUnificator.get(OreDictionary.getOres(prefixes + elementName.replaceAll(" ", ""))
                            .get(0)
                            .copy())
                    .copy();
            OreDictHandler.cache.put(
                    prefixes + elementName.replaceAll(" ", ""),
                    new Pair<>(Item.getIdFromItem(tmp.getItem()), (short) tmp.getItemDamage()));
            tmp.stackSize = amount;
            return tmp;
        }
        return null;
    }
}
