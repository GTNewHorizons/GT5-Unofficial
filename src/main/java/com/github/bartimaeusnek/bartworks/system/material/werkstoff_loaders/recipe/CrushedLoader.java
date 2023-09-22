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

import static gregtech.api.enums.OrePrefixes.crushed;
import static gregtech.api.enums.OrePrefixes.crushedCentrifuged;
import static gregtech.api.enums.OrePrefixes.crushedPurified;
import static gregtech.api.enums.OrePrefixes.dust;
import static gregtech.api.enums.OrePrefixes.dustImpure;
import static gregtech.api.enums.OrePrefixes.dustPure;
import static gregtech.api.enums.OrePrefixes.dustSmall;
import static gregtech.api.enums.OrePrefixes.gem;
import static gregtech.api.enums.OrePrefixes.ingot;
import static gregtech.api.enums.OrePrefixes.nugget;
import static gregtech.api.enums.OrePrefixes.ore;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sElectroMagneticSeparatorRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sHammerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

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
                werkstoff.get(dustImpure),
                new Object[] { "h  ", "W  ", 'W', werkstoff.get(crushed) });
        GT_ModHandler.addCraftingRecipe(
                werkstoff.get(dustPure),
                new Object[] { "h  ", "W  ", 'W', werkstoff.get(crushedPurified) });
        GT_ModHandler.addCraftingRecipe(
                werkstoff.get(dust),
                new Object[] { "h  ", "W  ", 'W', werkstoff.get(crushedCentrifuged) });

        GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(crushed)).itemOutputs(werkstoff.get(dustImpure))
                .duration(10 * TICKS).eut(16).addTo(sHammerRecipes);

        GT_ModHandler.addPulverisationRecipe(
                werkstoff.get(crushed),
                werkstoff.get(dustImpure),
                werkstoff.getOreByProduct(0, dust),
                10,
                false);
        GT_ModHandler.addOreWasherRecipe(
                werkstoff.get(crushed),
                new int[] { 10000, 1111, 10000 },
                1000,
                werkstoff.get(crushedPurified),
                werkstoff.getOreByProduct(0, dust),
                GT_OreDictUnificator.get(dust, Materials.Stone, 1L));
        GT_ModHandler.addThermalCentrifugeRecipe(
                werkstoff.get(crushed),
                new int[] { 10000, 1111, 10000 },
                (int) Math.min(5000L, Math.abs(werkstoff.getStats().getProtons() * 20L)),
                werkstoff.get(crushedCentrifuged),
                werkstoff.getOreByProduct(1, dust),
                GT_OreDictUnificator.get(dust, Materials.Stone, 1L));

        GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(crushedPurified)).itemOutputs(werkstoff.get(dustPure))
                .duration(10 * TICKS).eut(16).addTo(sHammerRecipes);

        GT_ModHandler.addPulverisationRecipe(
                werkstoff.get(crushedPurified),
                werkstoff.get(dustPure),
                werkstoff.getOreByProduct(1, dust),
                10,
                false);
        GT_ModHandler.addThermalCentrifugeRecipe(
                werkstoff.get(crushedPurified),
                new int[] { 10000, 1111 },
                (int) Math.min(5000L, Math.abs(werkstoff.getStats().getProtons() * 20L)),
                werkstoff.get(crushedCentrifuged),
                werkstoff.getOreByProduct(1, dust));

        GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(crushedCentrifuged)).itemOutputs(werkstoff.get(dust))
                .duration(10 * TICKS).eut(16).addTo(sHammerRecipes);

        GT_ModHandler.addPulverisationRecipe(
                werkstoff.get(crushedCentrifuged),
                werkstoff.get(dust),
                werkstoff.getOreByProduct(2, dust),
                10,
                false);

        GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(dustImpure))
                .itemOutputs(werkstoff.get(dust), werkstoff.getOreByProduct(0, dust)).outputChances(100_00, 11_11)
                .duration(Math.max(1L, werkstoff.getStats().getMass() * 8L)).eut(5).addTo(sCentrifugeRecipes);

        GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(dustPure))
                .itemOutputs(werkstoff.get(dust), werkstoff.getOreByProduct(1, dust)).outputChances(100_00, 11_11)
                .duration(Math.max(1L, werkstoff.getStats().getMass() * 8L)).eut(5).addTo(sCentrifugeRecipes);

        if (werkstoff.contains(SubTag.CRYSTALLISABLE)) {

            GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(dustPure)).itemOutputs(werkstoff.get(gem))
                    .outputChances(9000).fluidInputs(Materials.Water.getFluid(200L))
                    .duration(1 * MINUTES + 40 * SECONDS).eut(24).addTo(sAutoclaveRecipes);

            GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(dustImpure)).itemOutputs(werkstoff.get(gem))
                    .outputChances(9000).fluidInputs(Materials.Water.getFluid(200L))
                    .duration(1 * MINUTES + 40 * SECONDS).eut(24).addTo(sAutoclaveRecipes);

            GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(dustPure)).itemOutputs(werkstoff.get(gem))
                    .outputChances(9500).fluidInputs(gregtech.api.util.GT_ModHandler.getDistilledWater(200L))
                    .duration(1 * MINUTES + 15 * SECONDS).eut(24).addTo(sAutoclaveRecipes);

            GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(dustImpure)).itemOutputs(werkstoff.get(gem))
                    .outputChances(9500).fluidInputs(gregtech.api.util.GT_ModHandler.getDistilledWater(200L))
                    .duration(1 * MINUTES + 15 * SECONDS).eut(24).addTo(sAutoclaveRecipes);

        }
        if (werkstoff.contains(SubTag.WASHING_MERCURY)) {

            GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(crushed))
                    .itemOutputs(
                            werkstoff.get(crushedPurified),
                            werkstoff.getOreByProduct(1, dust),
                            GT_OreDictUnificator.get(dust, Materials.Stone, 1L))
                    .outputChances(10000, 7000, 4000).fluidInputs(Materials.Mercury.getFluid(1000L))
                    .duration(40 * SECONDS).eut(8).addTo(sChemicalBathRecipes);

        }
        if (werkstoff.contains(SubTag.WASHING_SODIUMPERSULFATE)) {

            GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(crushed))
                    .itemOutputs(
                            werkstoff.get(crushedPurified),
                            werkstoff.getOreByProduct(1, dust),
                            GT_OreDictUnificator.get(dust, Materials.Stone, 1L))
                    .outputChances(10000, 7000, 4000)
                    .fluidInputs(
                            Materials.SodiumPersulfate
                                    .getFluid(GT_Mod.gregtechproxy.mDisableOldChemicalRecipes ? 1000L : 100L))
                    .duration(40 * SECONDS).eut(8).addTo(sChemicalBathRecipes);

        }
        if (werkstoff.contains(SubTag.ELECTROMAGNETIC_SEPERATION_GOLD)) {

            GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(dustPure))
                    .itemOutputs(
                            werkstoff.get(dust),
                            GT_OreDictUnificator.get(dustSmall, Materials.Gold, 1L),
                            GT_OreDictUnificator.get(nugget, Materials.Gold, 1L))
                    .outputChances(10000, 4000, 2000).duration(20 * SECONDS).eut(24)
                    .addTo(sElectroMagneticSeparatorRecipes);

        } else if (werkstoff.contains(SubTag.ELECTROMAGNETIC_SEPERATION_IRON)) {

            GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(dustPure))
                    .itemOutputs(
                            werkstoff.get(dust),
                            GT_OreDictUnificator.get(dustSmall, Materials.Iron, 1L),
                            GT_OreDictUnificator.get(nugget, Materials.Iron, 1L))
                    .outputChances(10000, 4000, 2000).duration(20 * SECONDS).eut(24)
                    .addTo(sElectroMagneticSeparatorRecipes);

        } else if (werkstoff.contains(SubTag.ELECTROMAGNETIC_SEPERATION_NEODYMIUM)) {

            GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(dustPure))
                    .itemOutputs(
                            werkstoff.get(dust),
                            GT_OreDictUnificator.get(dustSmall, Materials.Neodymium, 1L),
                            GT_OreDictUnificator.get(nugget, Materials.Neodymium, 1L))
                    .outputChances(10000, 4000, 2000).duration(20 * SECONDS).eut(24)
                    .addTo(sElectroMagneticSeparatorRecipes);

        }
    }
}
