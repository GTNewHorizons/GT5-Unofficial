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

import static gregtech.api.enums.OrePrefixes.bolt;
import static gregtech.api.enums.OrePrefixes.gearGt;
import static gregtech.api.enums.OrePrefixes.gearGtSmall;
import static gregtech.api.enums.OrePrefixes.gem;
import static gregtech.api.enums.OrePrefixes.ingot;
import static gregtech.api.enums.OrePrefixes.plate;
import static gregtech.api.enums.OrePrefixes.ring;
import static gregtech.api.enums.OrePrefixes.rotor;
import static gregtech.api.enums.OrePrefixes.screw;
import static gregtech.api.enums.OrePrefixes.stick;
import static gregtech.api.enums.OrePrefixes.wireFine;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.recipe.RecipeMaps.extruderRecipes;
import static gregtech.api.recipe.RecipeMaps.latheRecipes;
import static gregtech.api.recipe.RecipeMaps.wiremillRecipes;
import static gregtech.api.util.GTRecipeBuilder.NUGGETS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import bartworks.system.material.Werkstoff;
import bartworks.system.material.werkstoff_loaders.IWerkstoffRunnable;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;

public class CraftingMaterialLoader implements IWerkstoffRunnable {

    @Override
    public void run(Werkstoff werkstoff) {
        if (werkstoff.hasItemType(screw)) {
            int tVoltageMultiplier = werkstoff.getStats()
                .getMeltingPoint() >= 2800 ? 60 : 15;

            // bolt

            GTValues.RA.stdBuilder()
                .itemInputs(
                    werkstoff.hasItemType(gem) ? werkstoff.get(gem) : werkstoff.get(ingot),
                    ItemList.Shape_Extruder_Bolt.get(0L))
                .itemOutputs(werkstoff.get(bolt, 8))
                .duration(
                    (int) Math.max(
                        werkstoff.getStats()
                            .getMass() * 2L,
                        1))
                .eut(8 * tVoltageMultiplier)
                .addTo(extruderRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(stick))
                .itemOutputs(werkstoff.get(bolt, 4))
                .duration(
                    (int) Math.max(
                        werkstoff.getStats()
                            .getMass() * 2L,
                        1L))
                .eut(4)
                .addTo(cutterRecipes);

            // screw

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(bolt))
                .itemOutputs(werkstoff.get(screw))
                .duration(
                    (int) Math.max(
                        werkstoff.getStats()
                            .getMass() / 8L,
                        1L))
                .eut(4)
                .addTo(latheRecipes);

            GTModHandler.addCraftingRecipe(
                werkstoff.get(screw),
                GTModHandler.RecipeBits.BITS_STD,
                new Object[] { "fX", "X ", 'X', werkstoff.get(bolt) });

            if (werkstoff.hasItemType(gem)) return;

            // ring

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(ingot), ItemList.Shape_Extruder_Ring.get(0L))
                .itemOutputs(werkstoff.get(ring, 4))
                .duration(
                    (int) Math.max(
                        werkstoff.getStats()
                            .getMass() * 2L,
                        1))
                .eut(6 * tVoltageMultiplier)
                .addTo(extruderRecipes);

            GTModHandler.addCraftingRecipe(
                werkstoff.get(ring),
                GTModHandler.RecipeBits.BITS_STD,
                new Object[] { "h ", "fX", 'X', werkstoff.get(stick) });

            // Gear
            GTModHandler.addCraftingRecipe(
                werkstoff.get(gearGt),
                GTModHandler.RecipeBits.BITS_STD,
                new Object[] { "SPS", "PwP", "SPS", 'P', werkstoff.get(plate), 'S', werkstoff.get(stick) });

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(ingot, 4), ItemList.Shape_Extruder_Gear.get(0L))
                .itemOutputs(werkstoff.get(gearGt))
                .duration(
                    (int) Math.max(
                        werkstoff.getStats()
                            .getMass() * 5L,
                        1))
                .eut(8 * tVoltageMultiplier)
                .addTo(extruderRecipes);
            // wireFine

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(ingot), ItemList.Shape_Extruder_Wire.get(0L))
                .itemOutputs(werkstoff.get(wireFine, 8))
                .duration(
                    (int) Math.max(
                        werkstoff.getStats()
                            .getMass() * 1.5F,
                        1F))
                .eut(8 * tVoltageMultiplier)
                .addTo(extruderRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(ingot))
                .circuit(3)
                .itemOutputs(werkstoff.get(wireFine, 8))
                .duration(
                    (int) Math.max(
                        werkstoff.getStats()
                            .getMass(),
                        1))
                .eut(8 * tVoltageMultiplier)
                .addTo(wiremillRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(stick))
                .circuit(3)
                .itemOutputs(werkstoff.get(wireFine, 4))
                .duration(
                    (int) Math.max(
                        werkstoff.getStats()
                            .getMass() * 0.5F,
                        1F))
                .eut(8 * tVoltageMultiplier)
                .addTo(wiremillRecipes);

            // smallGear
            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(ingot), ItemList.Shape_Extruder_Small_Gear.get(0L))
                .itemOutputs(werkstoff.get(gearGtSmall))
                .duration(
                    (int) werkstoff.getStats()
                        .getMass())
                .eut(8 * tVoltageMultiplier)
                .addTo(extruderRecipes);

            GTModHandler.addCraftingRecipe(
                werkstoff.get(gearGtSmall),
                GTModHandler.RecipeBits.BITS_STD,
                new Object[] { " S ", "hPx", " S ", 'S', werkstoff.get(stick), 'P', werkstoff.get(plate) });

            // Rotor
            GTModHandler.addCraftingRecipe(
                werkstoff.get(rotor),
                GTModHandler.RecipeBits.BITS_STD,
                new Object[] { "PhP", "SRf", "PdP", 'P', werkstoff.get(plate), 'R', werkstoff.get(ring), 'S',
                    werkstoff.get(screw) });

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(plate, 4), werkstoff.get(ring))
                .itemOutputs(werkstoff.get(rotor))
                .fluidInputs(Materials.Tin.getMolten(2 * NUGGETS))
                .duration(12 * SECONDS)
                .eut(24)
                .addTo(assemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(plate, 4), werkstoff.get(ring))
                .itemOutputs(werkstoff.get(rotor))
                .fluidInputs(Materials.Lead.getMolten(3 * NUGGETS))
                .duration(12 * SECONDS)
                .eut(24)
                .addTo(assemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(plate, 4), werkstoff.get(ring))
                .itemOutputs(werkstoff.get(rotor))
                .fluidInputs(Materials.SolderingAlloy.getMolten(1 * NUGGETS))
                .duration(12 * SECONDS)
                .eut(24)
                .addTo(assemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(ingot, 5), ItemList.Shape_Extruder_Rotor.get(0L))
                .itemOutputs(werkstoff.get(rotor))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV / 2)
                .addTo(extruderRecipes);
        }
    }
}
