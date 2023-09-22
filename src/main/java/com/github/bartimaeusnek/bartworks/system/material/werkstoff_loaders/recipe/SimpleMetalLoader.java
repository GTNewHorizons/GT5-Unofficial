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

import static gregtech.api.enums.OrePrefixes.block;
import static gregtech.api.enums.OrePrefixes.dust;
import static gregtech.api.enums.OrePrefixes.dustSmall;
import static gregtech.api.enums.OrePrefixes.foil;
import static gregtech.api.enums.OrePrefixes.gem;
import static gregtech.api.enums.OrePrefixes.ingot;
import static gregtech.api.enums.OrePrefixes.plate;
import static gregtech.api.enums.OrePrefixes.stick;
import static gregtech.api.enums.OrePrefixes.stickLong;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sBenderRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sExtruderRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sHammerRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sLatheRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sMaceratorRecipes;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;

import com.github.bartimaeusnek.bartworks.API.SideReference;
import com.github.bartimaeusnek.bartworks.client.textures.PrefixTextureLinker;
import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.system.material.werkstoff_loaders.IWerkstoffRunnable;
import com.github.bartimaeusnek.bartworks.util.BWRecipes;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TextureSet;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Proxy;

public class SimpleMetalLoader implements IWerkstoffRunnable {

    @Override
    public void run(Werkstoff werkstoff) {
        if (werkstoff.hasItemType(plate)) {
            if (werkstoff.hasItemType(gem)) {

                GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(gem))
                        .itemOutputs(werkstoff.get(stick), werkstoff.get(dustSmall, 2))
                        .duration((int) Math.max(werkstoff.getStats().getMass() * 5L, 1L)).eut(16).addTo(sLatheRecipes);

                GT_ModHandler.addCraftingRecipe(
                        werkstoff.get(stick, 2),
                        GT_Proxy.tBits,
                        new Object[] { "s", "X", 'X', werkstoff.get(stickLong) });
                GT_ModHandler.addCraftingRecipe(
                        werkstoff.get(stick),
                        GT_Proxy.tBits,
                        new Object[] { "f ", " X", 'X', werkstoff.get(gem) });

                GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(stick, 2)).itemOutputs(werkstoff.get(stickLong))
                        .duration((int) Math.max(werkstoff.getStats().getMass(), 1L)).eut(16).addTo(sHammerRecipes);

                TextureSet texSet = werkstoff.getTexSet();
                ITexture texture = SideReference.Side.Client
                        ? TextureFactory.of(
                                texSet.mTextures[PrefixTextureLinker.blockTexMap
                                        .getOrDefault(texSet, block.mTextureIndex)],
                                werkstoff.getRGBA(),
                                false)
                        : TextureFactory.of(texSet.mTextures[block.mTextureIndex], werkstoff.getRGBA(), false);
                GregTech_API.registerCover(werkstoff.get(plate), texture, null);

                GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(plate)).itemOutputs(werkstoff.get(dust))
                        .duration(2 * TICKS).eut(8).addTo(sMaceratorRecipes);

                return;
            }

            GT_ModHandler.addCraftingRecipe(
                    werkstoff.get(stick, 2),
                    GT_Proxy.tBits,
                    new Object[] { "s", "X", 'X', werkstoff.get(stickLong) });
            GT_ModHandler.addCraftingRecipe(
                    werkstoff.get(stick),
                    GT_Proxy.tBits,
                    new Object[] { "f ", " X", 'X', werkstoff.get(ingot) });
            GT_ModHandler.addCraftingRecipe(
                    werkstoff.get(plate),
                    GT_Proxy.tBits,
                    new Object[] { "h", "X", "X", 'X', werkstoff.get(ingot) });
            GT_ModHandler.addCraftingRecipe(
                    werkstoff.get(foil, 2),
                    GT_Proxy.tBits,
                    new Object[] { "hX", 'X', werkstoff.get(plate) });

            sBenderRecipes.add(
                    new BWRecipes.DynamicGTRecipe(
                            true,
                            new ItemStack[] { werkstoff.get(ingot), GT_Utility.getIntegratedCircuit(1) },
                            new ItemStack[] { werkstoff.get(plate) },
                            null,
                            null,
                            null,
                            null,
                            (int) Math.max(werkstoff.getStats().getMass(), 1L),
                            24,
                            0));

            GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(ingot, 3)).itemOutputs(werkstoff.get(plate, 2))
                    .duration((int) Math.max(werkstoff.getStats().getMass(), 1L)).eut(16).addTo(sHammerRecipes);

            GregTech_API.registerCover(
                    werkstoff.get(plate),
                    TextureFactory.of(werkstoff.getTexSet().mTextures[71], werkstoff.getRGBA(), false),
                    null);

            GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(ingot))
                    .itemOutputs(werkstoff.get(stick), werkstoff.get(dustSmall, 2))
                    .duration((int) Math.max(werkstoff.getStats().getMass() * 5L, 1L)).eut(16).addTo(sLatheRecipes);

            GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(plate), GT_Utility.getIntegratedCircuit(1))
                    .itemOutputs(werkstoff.get(foil, 4))
                    .duration((int) Math.max(werkstoff.getStats().getMass() * 1L, 1L)).eut(24).addTo(sBenderRecipes);

            GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(ingot), GT_Utility.getIntegratedCircuit(10))
                    .itemOutputs(werkstoff.get(foil, 4))
                    .duration((int) Math.max(werkstoff.getStats().getMass() * 2L, 1L)).eut(24).addTo(sBenderRecipes);

            GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(stick, 2)).itemOutputs(werkstoff.get(stickLong))
                    .duration((int) Math.max(werkstoff.getStats().getMass(), 1L)).eut(16).addTo(sHammerRecipes);

            GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(ingot), ItemList.Shape_Extruder_Plate.get(0))
                    .itemOutputs(werkstoff.get(plate)).duration((int) Math.max(werkstoff.getStats().getMass() * 2L, 1L))
                    .eut(45).addTo(sExtruderRecipes);

            GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(ingot), ItemList.Shape_Extruder_Rod.get(0))
                    .itemOutputs(werkstoff.get(stick, 2))
                    .duration((int) Math.max(werkstoff.getStats().getMass() * 2L, 1L)).eut(45).addTo(sExtruderRecipes);

            GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(ingot)).itemOutputs(werkstoff.get(dust))
                    .duration(2 * TICKS).eut(8).addTo(sMaceratorRecipes);

            GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(plate)).itemOutputs(werkstoff.get(dust))
                    .duration(2 * TICKS).eut(8).addTo(sMaceratorRecipes);

            GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(stickLong)).itemOutputs(werkstoff.get(dust))
                    .duration(2 * TICKS).eut(8).addTo(sMaceratorRecipes);

            GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(stick)).itemOutputs(werkstoff.get(dustSmall, 2))
                    .duration(2 * TICKS).eut(8).addTo(sMaceratorRecipes);

        }
    }
}
