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

import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.system.material.werkstoff_loaders.IWerkstoffRunnable;
import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_ModHandler;
import net.minecraft.item.ItemStack;

import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.blockCasing;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.blockCasingAdvanced;
import static gregtech.api.enums.OrePrefixes.*;

public class CasingLoader implements IWerkstoffRunnable {
    @Override
    public void run(Werkstoff werkstoff) {
        if (!(werkstoff.hasItemType(blockCasing) || werkstoff.hasItemType(plate) || werkstoff.hasItemType(screw) || werkstoff.hasItemType(gearGt) ))
            return;

        GT_ModHandler.addCraftingRecipe(werkstoff.get(blockCasing),new Object[]{
                "PSP",
                "PGP",
                "PSP",
                'P', werkstoff.get(plate),
                'S', werkstoff.get(screw),
                'G', werkstoff.get(gearGtSmall)
        });
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                werkstoff.get(plate,6),
                werkstoff.get(screw,2),
                werkstoff.get(gearGtSmall)
        }, GT_Values.NF,werkstoff.get(blockCasing), 200, 30);

        GT_ModHandler.addCraftingRecipe(werkstoff.get(blockCasingAdvanced),new Object[]{
                "PSP",
                "PGP",
                "PSP",
                'P', werkstoff.get(plateDouble),
                'S', werkstoff.get(screw),
                'G', werkstoff.get(gearGt)
        });
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                werkstoff.get(plateDouble,6),
                werkstoff.get(screw,2),
                werkstoff.get(gearGt)
        }, GT_Values.NF,werkstoff.get(blockCasingAdvanced), 200, 30);
    }
}