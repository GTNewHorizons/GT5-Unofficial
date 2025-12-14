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
import static gregtech.api.enums.OrePrefixes.dustSmall;
import static gregtech.api.enums.OrePrefixes.foil;
import static gregtech.api.enums.OrePrefixes.gem;
import static gregtech.api.enums.OrePrefixes.ingot;
import static gregtech.api.enums.OrePrefixes.plate;
import static gregtech.api.enums.OrePrefixes.stick;
import static gregtech.api.enums.OrePrefixes.stickLong;
import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.recipe.RecipeMaps.extruderRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.latheRecipes;

import bartworks.API.SideReference;
import bartworks.client.textures.PrefixTextureLinker;
import bartworks.system.material.Werkstoff;
import bartworks.system.material.WerkstoffLoader;
import bartworks.system.material.werkstoff_loaders.IWerkstoffRunnable;
import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TextureSet;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;

public class SimpleMetalLoader implements IWerkstoffRunnable {

    @Override
    public void run(Werkstoff werkstoff) {
        if (werkstoff.hasItemType(plate)) {
            if (werkstoff.hasItemType(gem)) {

                GTValues.RA.stdBuilder()
                    .itemInputs(werkstoff.get(gem))
                    .itemOutputs(werkstoff.get(stick), werkstoff.get(dustSmall, 2))
                    .duration(
                        (int) Math.max(
                            werkstoff.getStats()
                                .getMass() * 5L,
                            1L))
                    .eut(16)
                    .addTo(latheRecipes);

                GTModHandler.addCraftingRecipe(
                    werkstoff.get(stick, 2),
                    GTModHandler.RecipeBits.BITS_STD,
                    new Object[] { "s", "X", 'X', werkstoff.get(stickLong) });
                GTModHandler.addCraftingRecipe(
                    werkstoff.get(stick),
                    GTModHandler.RecipeBits.BITS_STD,
                    new Object[] { "f ", " X", 'X', werkstoff.get(gem) });

                GTValues.RA.stdBuilder()
                    .itemInputs(werkstoff.get(stick, 2))
                    .itemOutputs(werkstoff.get(stickLong))
                    .duration(
                        (int) Math.max(
                            werkstoff.getStats()
                                .getMass(),
                            1L))
                    .eut(16)
                    .addTo(hammerRecipes);

                TextureSet texSet = werkstoff.getTexSet();
                ITexture texture = SideReference.Side.Client
                    ? TextureFactory.of(
                        texSet.mTextures[PrefixTextureLinker.blockTexMap
                            .getOrDefault(texSet, (short) block.getTextureIndex())],
                        werkstoff.getRGBA())
                    : TextureFactory.of(texSet.mTextures[block.getTextureIndex()], werkstoff.getRGBA());
                CoverRegistry.registerDecorativeCover(werkstoff.get(plate), texture);

                return;
            }

            GTModHandler.addCraftingRecipe(
                werkstoff.get(stick, 2),
                GTModHandler.RecipeBits.BITS_STD,
                new Object[] { "s", "X", 'X', werkstoff.get(stickLong) });
            GTModHandler.addCraftingRecipe(
                werkstoff.get(stick),
                GTModHandler.RecipeBits.BITS_STD,
                new Object[] { "f ", " X", 'X', werkstoff.get(ingot) });
            GTModHandler.addCraftingRecipe(
                werkstoff.get(plate),
                GTModHandler.RecipeBits.BITS_STD,
                new Object[] { "h", "X", "X", 'X', werkstoff.get(ingot) });
            GTModHandler.addCraftingRecipe(
                werkstoff.get(foil, 2),
                GTModHandler.RecipeBits.BITS_STD,
                new Object[] { "hX", 'X', werkstoff.get(plate) });

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(ingot))
                .circuit(1)
                .itemOutputs(werkstoff.get(plate))
                .duration(
                    (int) Math.max(
                        werkstoff.getStats()
                            .getMass(),
                        1L))
                .eut(24)
                .addTo(benderRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(ingot, 3))
                .itemOutputs(werkstoff.get(plate, 2))
                .duration(
                    (int) Math.max(
                        werkstoff.getStats()
                            .getMass(),
                        1L))
                .eut(16)
                .addTo(hammerRecipes);

            CoverRegistry.registerDecorativeCover(
                werkstoff.get(plate),
                TextureFactory.of(werkstoff.getTexSet().mTextures[71], werkstoff.getRGBA()));

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(ingot))
                .itemOutputs(werkstoff.get(stick), werkstoff.get(dustSmall, 2))
                .duration(
                    (int) Math.max(
                        werkstoff.getStats()
                            .getMass() * 5L,
                        1L))
                .eut(16)
                .addTo(latheRecipes);

            if (werkstoff != WerkstoffLoader.Fluorophlogopite) {
                GTValues.RA.stdBuilder()
                    .itemInputs(werkstoff.get(plate))
                    .circuit(1)
                    .itemOutputs(werkstoff.get(foil, 4))
                    .duration(
                        (int) Math.max(
                            werkstoff.getStats()
                                .getMass(),
                            1L))
                    .eut(24)
                    .addTo(benderRecipes);
            }

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(ingot))
                .circuit(10)
                .itemOutputs(werkstoff.get(foil, 4))
                .duration(
                    (int) Math.max(
                        werkstoff.getStats()
                            .getMass() * 2L,
                        1L))
                .eut(24)
                .addTo(benderRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(stick, 2))
                .itemOutputs(werkstoff.get(stickLong))
                .duration(
                    (int) Math.max(
                        werkstoff.getStats()
                            .getMass(),
                        1L))
                .eut(16)
                .addTo(hammerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(ingot), ItemList.Shape_Extruder_Plate.get(0))
                .itemOutputs(werkstoff.get(plate))
                .duration(
                    (int) Math.max(
                        werkstoff.getStats()
                            .getMass() * 2L,
                        1L))
                .eut(45)
                .addTo(extruderRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(ingot), ItemList.Shape_Extruder_Rod.get(0))
                .itemOutputs(werkstoff.get(stick, 2))
                .duration(
                    (int) Math.max(
                        werkstoff.getStats()
                            .getMass() * 2L,
                        1L))
                .eut(45)
                .addTo(extruderRecipes);
        }
    }
}
