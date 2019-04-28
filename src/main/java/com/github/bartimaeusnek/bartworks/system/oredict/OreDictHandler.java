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

package com.github.bartimaeusnek.bartworks.system.oredict;

import com.github.bartimaeusnek.bartworks.util.Pair;
import gregtech.api.enums.OrePrefixes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class OreDictHandler {

    private static final HashMap<String, Pair<Integer,Short>> cache = new HashMap<>();

    public static HashMap<String, Pair<Integer,Short>> getCache() {
        return OreDictHandler.cache;
    }

    public static void adaptCacheForWorld(){
        Set<String> used = new HashSet<>(cache.keySet());
        OreDictHandler.cache.clear();
        for (String s : used) {
            if (!OreDictionary.getOres(s).isEmpty()) {
                ItemStack tmp = OreDictionary.getOres(s).get(0).copy();
                cache.put(s, new Pair<>(Item.getIdFromItem(tmp.getItem()), (short) tmp.getItemDamage()));
            }
        }
    }

    public static ItemStack getItemStack(String elementName, OrePrefixes prefixes, int amount){
        if (cache.get(prefixes+elementName.replaceAll(" ","")) != null){
            Pair<Integer,Short> p = cache.get(prefixes+elementName.replaceAll(" ",""));
            return new ItemStack(Item.getItemById(p.getKey()),amount,p.getValue());
        } else if (!OreDictionary.getOres(prefixes+elementName.replaceAll(" ","")).isEmpty()){
            ItemStack tmp = OreDictionary.getOres(prefixes+elementName.replaceAll(" ","")).get(0).copy();
            cache.put(prefixes+elementName.replaceAll(" ",""),new Pair<>(Item.getIdFromItem(tmp.getItem()), (short) tmp.getItemDamage()));
            tmp.stackSize=amount;
            return tmp;
        }
        return null;
    }
}
