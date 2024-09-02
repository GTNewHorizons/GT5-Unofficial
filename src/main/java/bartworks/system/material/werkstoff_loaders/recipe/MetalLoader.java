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

import static gregtech.api.enums.OrePrefixes.block;
import static gregtech.api.enums.OrePrefixes.ingot;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.extruderRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import bartworks.system.material.Werkstoff;
import bartworks.system.material.werkstoff_loaders.IWerkstoffRunnable;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.recipe.RecipeCategories;
import gregtech.api.recipe.RecipeMaps;

public class MetalLoader implements IWerkstoffRunnable {

    @Override
    public void run(Werkstoff werkstoff) {
        if (werkstoff.hasItemType(ingot)) {
            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(ingot, 9))
                .itemOutputs(werkstoff.get(block))
                .duration(15 * SECONDS)
                .eut(2)
                .addTo(compressorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(ingot, 9), ItemList.Shape_Extruder_Block.get(0))
                .itemOutputs(werkstoff.get(block))
                .duration(
                    (int) werkstoff.getStats()
                        .getMass())
                .eut(
                    8 * werkstoff.getStats()
                        .getMeltingPoint() >= 2800 ? 60 : 15)
                .addTo(extruderRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(ingot, 9), ItemList.Shape_Mold_Block.get(0L))
                .itemOutputs(werkstoff.get(block))
                .duration(
                    (int) (werkstoff.getStats()
                        .getMass() / 2))
                .eut(
                    4 * werkstoff.getStats()
                        .getMeltingPoint() >= 2800 ? 60 : 15)
                .recipeCategory(RecipeCategories.alloySmelterMolding)
                .addTo(RecipeMaps.alloySmelterRecipes);
        }
    }
}
