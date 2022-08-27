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

import com.github.bartimaeusnek.bartworks.util.Pair;
import gregtech.api.util.GT_OreDictUnificator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.item.ItemStack;

public class OreDictAdder {

    private static final ConcurrentHashMap<String, ItemStack> toAddMap = new ConcurrentHashMap<>();

    public static synchronized void addToMap(Pair<String, ItemStack> element) {
        OreDictAdder.toAddMap.put(element.getKey(), element.getValue());
    }

    @SafeVarargs
    public static synchronized void addToMap(Pair<String, ItemStack>... elements) {
        for (Pair<String, ItemStack> p : elements) OreDictAdder.toAddMap.put(p.getKey(), p.getValue());
    }

    public static void addToOreDict() {
        for (Map.Entry<String, ItemStack> entry : OreDictAdder.toAddMap.entrySet()) {
            GT_OreDictUnificator.registerOre(entry.getKey(), entry.getValue());
        }
    }
}
