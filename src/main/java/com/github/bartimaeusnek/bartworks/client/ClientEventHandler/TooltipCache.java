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

package com.github.bartimaeusnek.bartworks.client.ClientEventHandler;

import com.github.bartimaeusnek.bartworks.util.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

class TooltipCache {
    private static final HashMap<Pair<Integer, Short>, char[]> cache = new HashMap<>();

    static boolean put(ItemStack itemStack, List<String> tooltip) {
        Pair<Integer, Short> p = new Pair<>(Item.getIdFromItem(itemStack.getItem()), (short) itemStack.getItemDamage());
        if (TooltipCache.cache.containsKey(p)) return false;

        if (!tooltip.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String s : tooltip) {
                sb.append(s);
                sb.append(System.lineSeparator());
            }
            char[] rettype = sb.toString().toCharArray();
            return TooltipCache.cache.put(p, rettype) == rettype;
        } else {
            return false;
        }
    }

    static List<String> getTooltip(ItemStack itemStack) {
        Pair<Integer, Short> p = new Pair<>(Item.getIdFromItem(itemStack.getItem()), (short) itemStack.getItemDamage());
        char[] toTest = TooltipCache.cache.get(p);
        if (toTest == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(new String(toTest).split(System.lineSeparator()));
    }

    private static void checkSize() {
        if (TooltipCache.cache.size() > Short.MAX_VALUE * 2) {
            TooltipCache.cache.clear();
        }
    }
}
