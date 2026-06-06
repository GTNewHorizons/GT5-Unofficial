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

import static gregtech.api.enums.OrePrefixes.ingot;
import static gregtech.api.enums.OrePrefixes.plate;
import static gregtech.api.enums.OrePrefixes.plateDense;
import static gregtech.api.enums.OrePrefixes.plateDouble;
import static gregtech.api.enums.OrePrefixes.plateSuperdense;
import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.util.GTRecipeConstants.COMPRESSION_TIER;

import bartworks.system.material.Werkstoff;
import bartworks.system.material.werkstoff_loaders.IWerkstoffRunnable;
import bartworks.util.BWUtil;
import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.render.TextureFactory;

public class MultipleMetalLoader implements IWerkstoffRunnable {

    @Override
    public void run(Werkstoff werkstoff) {
        if (werkstoff.hasItemType(plateDense)) {
            int duration = (int) Math.max(
                werkstoff.getStats()
                    .getMass() * 2,
                1L);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(ingot, 2))
                .circuit(2)
                .itemOutputs(werkstoff.get(plateDouble))
                .duration(duration)
                .eut(BWUtil.calculateRecipeEU(werkstoff, 64))
                .addTo(benderRecipes);

            int compressionTier = (werkstoff.getStats()
                .getProcessingMaterialTierEU() >= TierEU.RECIPE_UEV || werkstoff.contains(SubTag.BLACK_HOLE)) ? 2 : 1;
            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(plate, 64))
                .itemOutputs(werkstoff.get(plateSuperdense))
                .metadata(COMPRESSION_TIER, compressionTier)
                .duration(
                    (int) Math.max(
                        werkstoff.getStats()
                            .getMass() * 4,
                        1L))
                .eut(BWUtil.calculateRecipeEU(werkstoff, 24))
                .addTo(compressorRecipes);

            CoverRegistry.registerDecorativeCover(
                werkstoff.get(plateDouble),
                TextureFactory.of(werkstoff.getTexSet().mTextures[72], werkstoff.getRGBA()));
        }
    }
}
