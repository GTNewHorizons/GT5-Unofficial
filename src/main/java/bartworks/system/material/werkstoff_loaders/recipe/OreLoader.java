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

package bartworks.system.material.werkstoff_loaders.recipe;

import static gregtech.api.enums.GTValues.RA;
import static gregtech.api.enums.OrePrefixes.crushed;
import static gregtech.api.enums.OrePrefixes.dust;
import static gregtech.api.enums.OrePrefixes.gem;
import static gregtech.api.enums.OrePrefixes.ingot;
import static gregtech.api.enums.OrePrefixes.ore;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import bartworks.system.material.Werkstoff;
import bartworks.system.material.WerkstoffLoader;
import bartworks.system.material.werkstoff_loaders.IWerkstoffRunnable;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SubTag;
import gregtech.api.util.GTModHandler;

public class OreLoader implements IWerkstoffRunnable {

    @Override
    public void run(Werkstoff werkstoff) {
        if (werkstoff.hasItemType(ore) && werkstoff.hasItemType(ingot)
            && !werkstoff.getStats()
                .isBlastFurnace())
            GTModHandler
                .addSmeltingRecipe(WerkstoffLoader.getCorrespondingItemStack(ore, werkstoff), werkstoff.get(ingot));

        if (werkstoff.hasItemType(ore)) {

            RA.stdBuilder()
                .itemInputs(werkstoff.get(ore))
                .itemOutputs(werkstoff.hasItemType(gem) ? werkstoff.get(gem) : werkstoff.get(crushed))
                .duration(16 * TICKS)
                .eut(10)
                .addTo(hammerRecipes);

            RA.stdBuilder()
                .itemInputs(werkstoff.get(ore))
                .itemOutputs(
                    werkstoff.get(crushed, 2),
                    werkstoff.contains(SubTag.CRYSTAL) ? werkstoff.get(gem) : werkstoff.getOreByProduct(0, dust),
                    Materials.Stone.getDust(1))
                .outputChances(100_00, 10_00, 50_00)
                .eut(2)
                .duration(20 * SECONDS)
                .addTo(maceratorRecipes);
        }
    }
}
