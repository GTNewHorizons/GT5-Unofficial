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
import com.github.bartimaeusnek.bartworks.system.material.werkstoff_loaders.IWerkstoffRunnable;
import gregtech.GT_Mod;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SubTag;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;

public class CrushedLoader implements IWerkstoffRunnable {
    @Override
    public void run(Werkstoff werkstoff) {
        if (!werkstoff.hasItemType(ore) || !werkstoff.hasItemType(dust)) return;

        if (werkstoff.hasItemType(ingot) && !werkstoff.getStats().isBlastFurnace()) {
            if (werkstoff.getType().equals(Werkstoff.Types.ELEMENT)) {
                GT_ModHandler.addSmeltingRecipe(werkstoff.get(crushed), werkstoff.get(nugget, 10));
                GT_ModHandler.addSmeltingRecipe(werkstoff.get(crushedPurified), werkstoff.get(nugget, 10));
                GT_ModHandler.addSmeltingRecipe(werkstoff.get(crushedCentrifuged), werkstoff.get(nugget, 10));
            } else {
                GT_ModHandler.addSmeltingRecipe(werkstoff.get(crushed), werkstoff.get(ingot));
                GT_ModHandler.addSmeltingRecipe(werkstoff.get(crushedPurified), werkstoff.get(ingot));
                GT_ModHandler.addSmeltingRecipe(werkstoff.get(crushedCentrifuged), werkstoff.get(ingot));
            }
            GT_ModHandler.addSmeltingRecipe(werkstoff.get(dustImpure), werkstoff.get(ingot));
            GT_ModHandler.addSmeltingRecipe(werkstoff.get(dustPure), werkstoff.get(ingot));
            GT_ModHandler.addSmeltingRecipe(werkstoff.get(dust), werkstoff.get(ingot));
        }

        GT_ModHandler.addCraftingRecipe(
                werkstoff.get(dustImpure), new Object[] {"h  ", "W  ", 'W', werkstoff.get(crushed)});
        GT_ModHandler.addCraftingRecipe(
                werkstoff.get(dustPure), new Object[] {"h  ", "W  ", 'W', werkstoff.get(crushedPurified)});
        GT_ModHandler.addCraftingRecipe(
                werkstoff.get(dust), new Object[] {"h  ", "W  ", 'W', werkstoff.get(crushedCentrifuged)});

        GT_Values.RA.addForgeHammerRecipe(werkstoff.get(crushed), werkstoff.get(dustImpure), 10, 16);
        GT_ModHandler.addPulverisationRecipe(
                werkstoff.get(crushed), werkstoff.get(dustImpure), werkstoff.getOreByProduct(0, dust), 10, false);
        GT_ModHandler.addOreWasherRecipe(
                werkstoff.get(crushed),
                new int[] {10000, 1111, 10000},
                1000,
                werkstoff.get(crushedPurified),
                werkstoff.getOreByProduct(0, dust),
                GT_OreDictUnificator.get(dust, Materials.Stone, 1L));
        GT_ModHandler.addThermalCentrifugeRecipe(
                werkstoff.get(crushed),
                new int[] {10000, 1111, 10000},
                (int) Math.min(5000L, Math.abs(werkstoff.getStats().getProtons() * 20L)),
                werkstoff.get(crushedCentrifuged),
                werkstoff.getOreByProduct(1, dust),
                GT_OreDictUnificator.get(dust, Materials.Stone, 1L));

        GT_Values.RA.addForgeHammerRecipe(werkstoff.get(crushedPurified), werkstoff.get(dustPure), 10, 16);
        GT_ModHandler.addPulverisationRecipe(
                werkstoff.get(crushedPurified), werkstoff.get(dustPure), werkstoff.getOreByProduct(1, dust), 10, false);
        GT_ModHandler.addThermalCentrifugeRecipe(
                werkstoff.get(crushedPurified),
                new int[] {10000, 1111},
                (int) Math.min(5000L, Math.abs(werkstoff.getStats().getProtons() * 20L)),
                werkstoff.get(crushedCentrifuged),
                werkstoff.getOreByProduct(1, dust));

        GT_Values.RA.addForgeHammerRecipe(werkstoff.get(crushedCentrifuged), werkstoff.get(dust), 10, 16);
        GT_ModHandler.addPulverisationRecipe(
                werkstoff.get(crushedCentrifuged), werkstoff.get(dust), werkstoff.getOreByProduct(2, dust), 10, false);

        GT_Values.RA.addCentrifugeRecipe(
                werkstoff.get(dustImpure),
                null,
                null,
                null,
                werkstoff.get(dust),
                werkstoff.getOreByProduct(0, dust),
                null,
                null,
                null,
                null,
                new int[] {10000, 1111},
                (int) Math.max(1L, werkstoff.getStats().getMass() * 8L),
                5);
        GT_Values.RA.addCentrifugeRecipe(
                werkstoff.get(dustPure),
                null,
                null,
                null,
                werkstoff.get(dust),
                werkstoff.getOreByProduct(1, dust),
                null,
                null,
                null,
                null,
                new int[] {10000, 1111},
                (int) Math.max(1L, werkstoff.getStats().getMass() * 8L),
                5);

        if (werkstoff.contains(SubTag.CRYSTALLISABLE)) {
            GT_Values.RA.addAutoclaveRecipe(
                    werkstoff.get(dustPure), Materials.Water.getFluid(200L), werkstoff.get(gem), 9000, 2000, 24);
            GT_Values.RA.addAutoclaveRecipe(
                    werkstoff.get(dustImpure), Materials.Water.getFluid(200L), werkstoff.get(gem), 9000, 2000, 24);
            GT_Values.RA.addAutoclaveRecipe(
                    werkstoff.get(dustPure),
                    gregtech.api.util.GT_ModHandler.getDistilledWater(200L),
                    werkstoff.get(gem),
                    9500,
                    1500,
                    24);
            GT_Values.RA.addAutoclaveRecipe(
                    werkstoff.get(dustImpure),
                    gregtech.api.util.GT_ModHandler.getDistilledWater(200L),
                    werkstoff.get(gem),
                    9500,
                    1500,
                    24);
        }
        if (werkstoff.contains(SubTag.WASHING_MERCURY))
            GT_Values.RA.addChemicalBathRecipe(
                    werkstoff.get(crushed),
                    Materials.Mercury.getFluid(1000L),
                    werkstoff.get(crushedPurified),
                    werkstoff.getOreByProduct(1, dust),
                    GT_OreDictUnificator.get(dust, Materials.Stone, 1L),
                    new int[] {10000, 7000, 4000},
                    800,
                    8);
        if (werkstoff.contains(SubTag.WASHING_SODIUMPERSULFATE))
            GT_Values.RA.addChemicalBathRecipe(
                    werkstoff.get(crushed),
                    Materials.SodiumPersulfate.getFluid(GT_Mod.gregtechproxy.mDisableOldChemicalRecipes ? 1000L : 100L),
                    werkstoff.get(crushedPurified),
                    werkstoff.getOreByProduct(1, dust),
                    GT_OreDictUnificator.get(dust, Materials.Stone, 1L),
                    new int[] {10000, 7000, 4000},
                    800,
                    8);
        if (werkstoff.contains(SubTag.ELECTROMAGNETIC_SEPERATION_GOLD))
            GT_Values.RA.addElectromagneticSeparatorRecipe(
                    werkstoff.get(dustPure),
                    werkstoff.get(dust),
                    GT_OreDictUnificator.get(dustSmall, Materials.Gold, 1L),
                    GT_OreDictUnificator.get(nugget, Materials.Gold, 1L),
                    new int[] {10000, 4000, 2000},
                    400,
                    24);
        else if (werkstoff.contains(SubTag.ELECTROMAGNETIC_SEPERATION_IRON))
            GT_Values.RA.addElectromagneticSeparatorRecipe(
                    werkstoff.get(dustPure),
                    werkstoff.get(dust),
                    GT_OreDictUnificator.get(dustSmall, Materials.Iron, 1L),
                    GT_OreDictUnificator.get(nugget, Materials.Iron, 1L),
                    new int[] {10000, 4000, 2000},
                    400,
                    24);
        else if (werkstoff.contains(SubTag.ELECTROMAGNETIC_SEPERATION_NEODYMIUM))
            GT_Values.RA.addElectromagneticSeparatorRecipe(
                    werkstoff.get(dustPure),
                    werkstoff.get(dust),
                    GT_OreDictUnificator.get(dustSmall, Materials.Neodymium, 1L),
                    GT_OreDictUnificator.get(nugget, Materials.Neodymium, 1L),
                    new int[] {10000, 4000, 2000},
                    400,
                    24);
    }
}
