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
import gregtech.api.enums.Materials;
import gregtech.api.enums.SubTag;
import gregtech.api.util.GT_ModHandler;

public class OreLoader implements IWerkstoffRunnable {
    @Override
    public void run(Werkstoff werkstoff) {
        if (werkstoff.hasItemType(ore)
                && werkstoff.hasItemType(ingot)
                && !werkstoff.getStats().isBlastFurnace())
            GT_ModHandler.addSmeltingRecipe(
                    WerkstoffLoader.getCorrespondingItemStack(ore, werkstoff), werkstoff.get(ingot));

        if (werkstoff.hasItemType(ore)) {
            GT_Values.RA.addForgeHammerRecipe(
                    werkstoff.get(ore),
                    werkstoff.hasItemType(gem) ? werkstoff.get(gem) : werkstoff.get(crushed),
                    16,
                    10);
            GT_ModHandler.addPulverisationRecipe(
                    werkstoff.get(ore),
                    werkstoff.get(crushed, 2),
                    werkstoff.contains(SubTag.CRYSTAL) ? werkstoff.get(gem) : werkstoff.getOreByProduct(0, dust),
                    werkstoff.getNoOfByProducts() > 0 ? 10 : 0,
                    Materials.Stone.getDust(1),
                    50,
                    true);
        }
    }
}
