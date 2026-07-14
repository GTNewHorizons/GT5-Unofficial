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

import static gregtech.api.enums.OrePrefixes.cellMolten;
import static gregtech.api.enums.OrePrefixes.dust;
import static gregtech.api.enums.OrePrefixes.dustSmall;
import static gregtech.api.enums.OrePrefixes.dustTiny;
import static gregtech.api.enums.OrePrefixes.ingot;
import static gregtech.api.enums.OrePrefixes.nugget;
import static gregtech.api.enums.OrePrefixes.plate;
import static gregtech.api.enums.OrePrefixes.stick;
import static gregtech.api.enums.OrePrefixes.stickLong;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.NUGGETS;
import static gregtech.api.util.GTRecipeBuilder.QUARTER_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraftforge.fluids.FluidContainerRegistry;

import bartworks.system.material.Werkstoff;
import bartworks.system.material.werkstoff_loaders.IWerkstoffRunnable;
import bartworks.util.BWUtil;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.recipe.RecipeCategories;

/// Molten-fluid recipes for cellMolten-bearing werkstoffe: fluid-extractor melting of the solid shapes and
/// the molten-cell fluid-container registration. Mold solidification (ingot/nugget plus the worked plate/
/// stick/stickLong and screw/bolt/ring/gear/gearSmall/rotor shapes) is covered by the canonical autogen --
/// `ProcessingIngot`, `ProcessingNugget`, `ProcessingShaping`, `ProcessingPlate`, `ProcessingGear` and
/// `ProcessingRotor`, dispatched per MaterialLib shape by `gregtech.loaders.shapeconsumers`.
public class MoltenCellLoader implements IWerkstoffRunnable {

    @Override
    public void run(Werkstoff werkstoff) {
        if (!werkstoff.hasItemType(cellMolten)) {
            return;
        }

        if (!werkstoff.hasItemType(ingot)) {
            if (!werkstoff.hasItemType(dust)) {
                return;
            }

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(dust))
                .fluidOutputs(werkstoff.getMolten(1 * INGOTS))
                .duration(15 * SECONDS)
                .eut(BWUtil.calculateRecipeEU(werkstoff, 2))
                .recipeCategory(RecipeCategories.fluidExtractorRecycling)
                .addTo(fluidExtractionRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(dustSmall))
                .fluidOutputs(werkstoff.getMolten(1 * QUARTER_INGOTS))
                .duration(15 * SECONDS)
                .eut(BWUtil.calculateRecipeEU(werkstoff, 2))
                .recipeCategory(RecipeCategories.fluidExtractorRecycling)
                .addTo(fluidExtractionRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(dustTiny))
                .fluidOutputs(werkstoff.getMolten(1 * NUGGETS))
                .duration(15 * SECONDS)
                .eut(BWUtil.calculateRecipeEU(werkstoff, 2))
                .recipeCategory(RecipeCategories.fluidExtractorRecycling)
                .addTo(fluidExtractionRecipes);

        } else {

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(ingot))
                .fluidOutputs(werkstoff.getMolten(1 * INGOTS))
                .duration(15 * SECONDS)
                .eut(BWUtil.calculateRecipeEU(werkstoff, 2))
                .recipeCategory(RecipeCategories.fluidExtractorRecycling)
                .addTo(fluidExtractionRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(nugget))
                .fluidOutputs(werkstoff.getMolten(1 * NUGGETS))
                .duration(15 * SECONDS)
                .eut(BWUtil.calculateRecipeEU(werkstoff, 2))
                .recipeCategory(RecipeCategories.fluidExtractorRecycling)
                .addTo(fluidExtractionRecipes);

            if (!werkstoff.hasItemType(plate)) {
                return;
            }

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(stickLong))
                .fluidOutputs(werkstoff.getMolten(1 * INGOTS))
                .duration(15 * SECONDS)
                .eut(BWUtil.calculateRecipeEU(werkstoff, 2))
                .recipeCategory(RecipeCategories.fluidExtractorRecycling)
                .addTo(fluidExtractionRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(plate))
                .fluidOutputs(werkstoff.getMolten(1 * INGOTS))
                .duration(15 * SECONDS)
                .eut(BWUtil.calculateRecipeEU(werkstoff, 2))
                .recipeCategory(RecipeCategories.fluidExtractorRecycling)
                .addTo(fluidExtractionRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(stick))
                .fluidOutputs(werkstoff.getMolten(1 * HALF_INGOTS))
                .duration(15 * SECONDS)
                .eut(BWUtil.calculateRecipeEU(werkstoff, 2))
                .recipeCategory(RecipeCategories.fluidExtractorRecycling)
                .addTo(fluidExtractionRecipes);
        }

        // Tank "Recipe"
        FluidContainerRegistry.registerFluidContainer(
            werkstoff.getMolten(1 * INGOTS),
            werkstoff.get(cellMolten),
            Materials.Empty.getCells(1));
    }
}
