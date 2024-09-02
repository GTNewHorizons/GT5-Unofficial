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
import static gregtech.api.enums.OrePrefixes.cellMolten;
import static gregtech.api.enums.OrePrefixes.dust;
import static gregtech.api.enums.OrePrefixes.dustSmall;
import static gregtech.api.enums.OrePrefixes.dustTiny;
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
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.recipe.RecipeMaps.wiremillRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import bartworks.system.material.Werkstoff;
import bartworks.system.material.WerkstoffLoader;
import bartworks.system.material.werkstoff_loaders.IWerkstoffRunnable;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.common.GTProxy;

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

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(bolt))
                .itemOutputs(werkstoff.get(dustTiny, 1))
                .duration(2 * TICKS)
                .eut(8)
                .addTo(maceratorRecipes);

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
                GTProxy.tBits,
                new Object[] { "fX", "X ", 'X', werkstoff.get(bolt) });

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(screw))
                .itemOutputs(werkstoff.get(dustTiny, 1))
                .duration(2 * TICKS)
                .eut(8)
                .addTo(maceratorRecipes);

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
                GTProxy.tBits,
                new Object[] { "h ", "fX", 'X', werkstoff.get(stick) });

            // Gear
            GTModHandler.addCraftingRecipe(
                werkstoff.get(gearGt),
                GTProxy.tBits,
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
                .itemInputs(werkstoff.get(ingot), GTUtility.getIntegratedCircuit(3))
                .itemOutputs(werkstoff.get(wireFine, 8))
                .duration(
                    (int) Math.max(
                        werkstoff.getStats()
                            .getMass(),
                        1))
                .eut(8 * tVoltageMultiplier)
                .addTo(wiremillRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(stick), GTUtility.getIntegratedCircuit(3))
                .itemOutputs(werkstoff.get(wireFine, 4))
                .duration(
                    (int) Math.max(
                        werkstoff.getStats()
                            .getMass() * 0.5F,
                        1F))
                .eut(8 * tVoltageMultiplier)
                .addTo(wiremillRecipes);

            // smallGear
            if (WerkstoffLoader.smallGearShape != null) {

                GTValues.RA.stdBuilder()
                    .itemInputs(werkstoff.get(ingot), WerkstoffLoader.smallGearShape.get(0L))
                    .itemOutputs(werkstoff.get(gearGtSmall))
                    .duration(
                        (int) werkstoff.getStats()
                            .getMass())
                    .eut(8 * tVoltageMultiplier)
                    .addTo(extruderRecipes);

            }

            GTModHandler.addCraftingRecipe(
                werkstoff.get(gearGtSmall),
                GTProxy.tBits,
                new Object[] { " S ", "hPx", " S ", 'S', werkstoff.get(stick), 'P', werkstoff.get(plate) });

            // Rotor
            GTModHandler.addCraftingRecipe(
                werkstoff.get(rotor),
                GTProxy.tBits,
                new Object[] { "PhP", "SRf", "PdP", 'P', werkstoff.get(plate), 'R', werkstoff.get(ring), 'S',
                    werkstoff.get(screw) });

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(plate, 4), werkstoff.get(ring))
                .itemOutputs(werkstoff.get(rotor))
                .fluidInputs(Materials.Tin.getMolten(32))
                .duration(12 * SECONDS)
                .eut(24)
                .addTo(assemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(plate, 4), werkstoff.get(ring))
                .itemOutputs(werkstoff.get(rotor))
                .fluidInputs(Materials.Lead.getMolten(48))
                .duration(12 * SECONDS)
                .eut(24)
                .addTo(assemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(plate, 4), werkstoff.get(ring))
                .itemOutputs(werkstoff.get(rotor))
                .fluidInputs(Materials.SolderingAlloy.getMolten(16))
                .duration(12 * SECONDS)
                .eut(24)
                .addTo(assemblerRecipes);

            if (WerkstoffLoader.rotorShape != null) {

                GTValues.RA.stdBuilder()
                    .itemInputs(werkstoff.get(ingot, 5), WerkstoffLoader.rotorShape.get(0L))
                    .itemOutputs(werkstoff.get(rotor))
                    .duration(10 * SECONDS)
                    .eut(60)
                    .addTo(extruderRecipes);

            }

            // molten -> metal
            if (werkstoff.hasItemType(cellMolten)) {

                /*
                 * !! No more hardcoded gear, etc. recipe gen, now must go through GenerationFeatures() !!
                 * GT_Values.RA.addFluidSolidifierRecipe( ItemList.Shape_Mold_Gear.get(0L), werkstoff.getMolten(576),
                 * werkstoff.get(gearGt), 128, 8); GT_Values.RA.addFluidSolidifierRecipe(
                 * ItemList.Shape_Mold_Gear_Small.get(0L), werkstoff.getMolten(144), werkstoff.get(gearGtSmall), 16, 8);
                 * if (WerkstoffLoader.ringMold != null) GT_Values.RA.addFluidSolidifierRecipe(
                 * WerkstoffLoader.ringMold.get(0L), werkstoff.getMolten(36), werkstoff.get(ring), 100, 4 *
                 * tVoltageMultiplier); if (WerkstoffLoader.boltMold != null) GT_Values.RA.addFluidSolidifierRecipe(
                 * WerkstoffLoader.boltMold.get(0L), werkstoff.getMolten(18), werkstoff.get(bolt), 50, 2 *
                 * tVoltageMultiplier); if (WerkstoffLoader.rotorMold != null) GT_Values.RA.addFluidSolidifierRecipe(
                 * WerkstoffLoader.rotorMold.get(0L), werkstoff.getMolten(612), werkstoff.get(rotor), 100, 60);
                 */
            }

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(gearGt))
                .itemOutputs(werkstoff.get(dust, 4))
                .duration(2 * TICKS)
                .eut(8)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(gearGtSmall))
                .itemOutputs(werkstoff.get(dust, 1))
                .duration(2 * TICKS)
                .eut(8)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(rotor))
                .itemOutputs(werkstoff.get(dust, 4), werkstoff.get(dustSmall))
                .duration(2 * TICKS)
                .eut(8)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(ring))
                .itemOutputs(werkstoff.get(dustSmall, 1))
                .duration(2 * TICKS)
                .eut(8)
                .addTo(maceratorRecipes);

        }
    }
}
