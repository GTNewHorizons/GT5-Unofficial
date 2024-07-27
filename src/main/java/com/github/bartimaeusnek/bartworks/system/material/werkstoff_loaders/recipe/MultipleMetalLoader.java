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

package com.github.bartimaeusnek.bartworks.system.material.werkstoff_loaders.recipe;

import static gregtech.api.enums.OrePrefixes.dust;
import static gregtech.api.enums.OrePrefixes.ingot;
import static gregtech.api.enums.OrePrefixes.plateDense;
import static gregtech.api.enums.OrePrefixes.plateDouble;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;

import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.system.material.werkstoff_loaders.IWerkstoffRunnable;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

public class MultipleMetalLoader implements IWerkstoffRunnable {

    @Override
    public void run(Werkstoff werkstoff) {
        if (werkstoff.hasItemType(plateDense)) {
            RecipeMaps.benderRecipes.add(
                new GT_Recipe(
                    true,
                    new ItemStack[] { werkstoff.get(ingot, 2), GT_Utility.getIntegratedCircuit(2) },
                    new ItemStack[] { werkstoff.get(plateDouble) },
                    null,
                    null,
                    null,
                    null,
                    (int) Math.max(
                        werkstoff.getStats()
                            .getMass() * 2,
                        1L),
                    60,
                    0));
            GregTech_API.registerCover(
                werkstoff.get(plateDouble),
                TextureFactory.of(werkstoff.getTexSet().mTextures[72], werkstoff.getRGBA(), false),
                null);

            GT_Values.RA.stdBuilder()
                .itemInputs(werkstoff.get(plateDouble))
                .itemOutputs(werkstoff.get(dust, 2))
                .duration(2 * TICKS)
                .eut(8)
                .addTo(maceratorRecipes);

        }
    }
}
