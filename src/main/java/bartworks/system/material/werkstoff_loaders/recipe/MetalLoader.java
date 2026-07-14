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

import bartworks.system.material.Werkstoff;
import bartworks.system.material.werkstoff_loaders.IWerkstoffRunnable;
import bartworks.util.BWUtil;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.recipe.RecipeCategories;
import gregtech.api.recipe.RecipeMaps;

/// Alloy-smelter block molding for ingot-bearing werkstoffe, tier-independent. The canonical autogen
/// (`ProcessingShaping`, dispatched by `gregtech.loaders.shapeconsumers`) covers the block extruder recipe
/// unconditionally but gates its alloy-smelter block molding below IV, so this branch carries the recipe for
/// the higher-tier werkstoffe.
public class MetalLoader implements IWerkstoffRunnable {

    @Override
    public void run(Werkstoff werkstoff) {
        if (werkstoff.hasItemType(ingot)) {
            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(ingot, 9), ItemList.Shape_Mold_Block.get(0L))
                .itemOutputs(werkstoff.get(block))
                .duration(
                    (int) (werkstoff.getStats()
                        .getMass() * 9))
                .eut(
                    BWUtil.calculateRecipeEU(
                        werkstoff,
                        werkstoff.getStats()
                            .getMeltingPoint() >= 2800 ? 240 : 60))
                .recipeCategory(RecipeCategories.alloySmelterMolding)
                .addTo(RecipeMaps.alloySmelterRecipes);
        }
    }
}
