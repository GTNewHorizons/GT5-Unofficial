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

package com.github.bartimaeusnek.bartworks.system.material.werkstoff_loaders.recipe;

import static gregtech.api.enums.OrePrefixes.*;

import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.bartworks.system.material.werkstoff_loaders.IWerkstoffRunnable;
import gregtech.api.enums.GT_Values;
import net.minecraft.item.ItemStack;

public class BlockLoader implements IWerkstoffRunnable {
    @Override
    public void run(Werkstoff werkstoff) {
        if (!werkstoff.hasItemType(block)) return;
        if (werkstoff.hasItemType(ingot)) {
            GT_Values.RA.addArcFurnaceRecipe(
                    werkstoff.get(block), new ItemStack[] {werkstoff.get(ingot, 9)}, null, 16, 90, false);
        }
        if (werkstoff.hasItemType(WerkstoffLoader.cellMolten)) {
            GT_Values.RA.addFluidExtractionRecipe(werkstoff.get(block), null, werkstoff.getMolten(1296), 0, 288, 8);
        }
        if (werkstoff.hasItemType(plate)) {
            GT_Values.RA.addCutterRecipe(
                    werkstoff.get(block),
                    werkstoff.get(plate, 9),
                    null,
                    (int) Math.max(werkstoff.getStats().getMass() * 10L, 1L),
                    30);
        }
    }
}
