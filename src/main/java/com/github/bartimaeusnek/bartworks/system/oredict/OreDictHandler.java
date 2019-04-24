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

import gregtech.api.enums.OrePrefixes;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;

public class OreDictHandler {

    private static final HashMap<String,ItemStack> cache = new HashMap<>();

    public static HashMap<String, ItemStack> getCache() {
        return OreDictHandler.cache;
    }

    public static ItemStack getItemStack(String elementName, OrePrefixes prefixes, int amount){
        if (cache.get(prefixes+elementName.replaceAll(" ","")) != null){
            ItemStack tmp = cache.get(prefixes+elementName.replaceAll(" ","")).copy();
            tmp.stackSize=amount;
            return tmp;
        } else if (!OreDictionary.getOres(prefixes+elementName.replaceAll(" ","")).isEmpty()){
            ItemStack tmp = OreDictionary.getOres(prefixes+elementName.replaceAll(" ","")).get(0).copy();
            tmp.stackSize=amount;
            cache.put(prefixes+elementName.replaceAll(" ",""),tmp);
            return tmp;
        }
        return null;
    }
}
