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
import com.github.bartimaeusnek.bartworks.util.BW_ColorUtil;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class GemLoader implements IWerkstoffRunnable {

    @Override
    public void run(Werkstoff werkstoff) {
        if (werkstoff.hasItemType(gem)) {
            if (werkstoff.getGenerationFeatures().hasSifterRecipes()
                    || (werkstoff.hasItemType(ore) && werkstoff.hasItemType(dust))) {

                GT_ModHandler.addCompressionRecipe(werkstoff.get(gem, 9), werkstoff.get(block));
                GT_Values.RA.addForgeHammerRecipe(werkstoff.get(block), werkstoff.get(gem, 9), 100, 24);
                GT_Values.RA.addSifterRecipe(
                        werkstoff.get(crushedPurified),
                        new ItemStack[] {
                            werkstoff.get(gemExquisite),
                            werkstoff.get(gemFlawless),
                            werkstoff.get(gem),
                            werkstoff.get(gemFlawed),
                            werkstoff.get(gemChipped),
                            werkstoff.get(dust)
                        },
                        new int[] {200, 1000, 2500, 2000, 4000, 5000},
                        800,
                        16);
            }

            GT_ModHandler.addPulverisationRecipe(werkstoff.get(gemExquisite), werkstoff.get(dust, 4));
            GT_ModHandler.addPulverisationRecipe(werkstoff.get(gemFlawless), werkstoff.get(dust, 2));
            GT_ModHandler.addPulverisationRecipe(werkstoff.get(gem), werkstoff.get(dust));
            GT_ModHandler.addPulverisationRecipe(werkstoff.get(gemFlawed), werkstoff.get(dustSmall, 2));
            GT_ModHandler.addPulverisationRecipe(werkstoff.get(gemChipped), werkstoff.get(dustSmall));

            GT_ModHandler.addCraftingRecipe(
                    werkstoff.get(gemFlawless, 2), 0, new Object[] {"h  ", "W  ", 'W', werkstoff.get(gemExquisite)});
            GT_ModHandler.addCraftingRecipe(
                    werkstoff.get(gem, 2), 0, new Object[] {"h  ", "W  ", 'W', werkstoff.get(gemFlawless)});
            GT_ModHandler.addCraftingRecipe(
                    werkstoff.get(gemFlawed, 2), 0, new Object[] {"h  ", "W  ", 'W', werkstoff.get(gem)});
            GT_ModHandler.addCraftingRecipe(
                    werkstoff.get(gemChipped, 2), 0, new Object[] {"h  ", "W  ", 'W', werkstoff.get(gemFlawed)});

            GT_Values.RA.addForgeHammerRecipe(werkstoff.get(gemExquisite), werkstoff.get(gemFlawless, 2), 64, 16);
            GT_Values.RA.addForgeHammerRecipe(werkstoff.get(gemFlawless), werkstoff.get(gem, 2), 64, 16);
            GT_Values.RA.addForgeHammerRecipe(werkstoff.get(gem), werkstoff.get(gemFlawed, 2), 64, 16);
            GT_Values.RA.addForgeHammerRecipe(werkstoff.get(gemFlawed), werkstoff.get(gemChipped, 2), 64, 16);
            GT_Values.RA.addForgeHammerRecipe(werkstoff.get(gemChipped), werkstoff.get(dustTiny), 64, 16);

            if (!werkstoff.contains(WerkstoffLoader.NO_BLAST)) {
                GT_Values.RA.addImplosionRecipe(
                        werkstoff.get(gemFlawless, 3),
                        8,
                        werkstoff.get(gemExquisite),
                        GT_OreDictUnificator.get(dustTiny, Materials.DarkAsh, 2));
                GT_Values.RA.addImplosionRecipe(
                        werkstoff.get(gem, 3),
                        8,
                        werkstoff.get(gemFlawless),
                        GT_OreDictUnificator.get(dustTiny, Materials.DarkAsh, 2));
                GT_Values.RA.addImplosionRecipe(
                        werkstoff.get(gemFlawed, 3),
                        8,
                        werkstoff.get(gem),
                        GT_OreDictUnificator.get(dustTiny, Materials.DarkAsh, 2));
                GT_Values.RA.addImplosionRecipe(
                        werkstoff.get(gemChipped, 3),
                        8,
                        werkstoff.get(gemFlawed),
                        GT_OreDictUnificator.get(dustTiny, Materials.DarkAsh, 2));

                GT_Values.RA.addImplosionRecipe(
                        werkstoff.get(dust, 4),
                        24,
                        werkstoff.get(gem, 3),
                        GT_OreDictUnificator.get(dustTiny, Materials.DarkAsh, 8));
            }

            if (werkstoff.hasItemType(plate)) {
                GT_Values.RA.addLatheRecipe(
                        werkstoff.get(plate), werkstoff.get(lens), werkstoff.get(dustSmall), 1200, 120);
            }

            GT_Values.RA.addLatheRecipe(
                    werkstoff.get(gemExquisite), werkstoff.get(lens), werkstoff.get(dust, 2), 2400, 30);
            GregTech_API.registerCover(
                    werkstoff.get(lens),
                    TextureFactory.of(
                            Textures.BlockIcons.MACHINE_CASINGS[2][0],
                            TextureFactory.of(Textures.BlockIcons.OVERLAY_LENS, werkstoff.getRGBA(), false)),
                    new gregtech.common.covers.GT_Cover_Lens(BW_ColorUtil.getDyeFromColor(werkstoff.getRGBA()).mIndex));
            GT_ModHandler.addPulverisationRecipe(werkstoff.get(lens), werkstoff.get(dustSmall, 3));

            for (ItemStack is : OreDictionary.getOres("craftingLens"
                    + BW_ColorUtil.getDyeFromColor(werkstoff.getRGBA()).mName.replace(" ", ""))) {
                is.stackSize = 0;
                GT_Values.RA.addLaserEngraverRecipe(
                        werkstoff.get(gemChipped, 3), is, werkstoff.get(gemFlawed, 1), 600, 30);
                GT_Values.RA.addLaserEngraverRecipe(werkstoff.get(gemFlawed, 3), is, werkstoff.get(gem, 1), 600, 120);
                GT_Values.RA.addLaserEngraverRecipe(
                        werkstoff.get(gem, 3), is, werkstoff.get(gemFlawless, 1), 1200, 480);
                GT_Values.RA.addLaserEngraverRecipe(
                        werkstoff.get(gemFlawless, 3), is, werkstoff.get(gemExquisite, 1), 2400, 2000);
            }
        }
    }
}
