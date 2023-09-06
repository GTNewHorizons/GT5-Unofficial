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
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sAssemblerRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sCutterRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sExtruderRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sLatheRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sMaceratorRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sWiremillRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.bartworks.system.material.werkstoff_loaders.IWerkstoffRunnable;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Proxy;

public class CraftingMaterialLoader implements IWerkstoffRunnable {

    @Override
    public void run(Werkstoff werkstoff) {
        if (werkstoff.hasItemType(screw)) {
            int tVoltageMultiplier = werkstoff.getStats().getMeltingPoint() >= 2800 ? 60 : 15;

            // bolt

            GT_Values.RA.stdBuilder()
                    .itemInputs(
                            werkstoff.hasItemType(gem) ? werkstoff.get(gem) : werkstoff.get(ingot),
                            ItemList.Shape_Extruder_Bolt.get(0L))
                    .itemOutputs(werkstoff.get(bolt, 8)).noFluidInputs().noFluidOutputs()
                    .duration((int) Math.max(werkstoff.getStats().getMass() * 2L, 1)).eut(8 * tVoltageMultiplier)
                    .addTo(sExtruderRecipes);

            GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(stick)).itemOutputs(werkstoff.get(bolt, 4))
                    .noFluidInputs().noFluidOutputs().duration((int) Math.max(werkstoff.getStats().getMass() * 2L, 1L))
                    .eut(4).addTo(sCutterRecipes);

            GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(bolt)).itemOutputs(werkstoff.get(dustTiny, 1))
                    .noFluidInputs().noFluidOutputs().duration(2 * TICKS).eut(8).addTo(sMaceratorRecipes);

            // screw

            GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(bolt)).itemOutputs(werkstoff.get(screw)).noFluidInputs()
                    .noFluidOutputs().duration((int) Math.max(werkstoff.getStats().getMass() / 8L, 1L)).eut(4)
                    .addTo(sLatheRecipes);

            GT_ModHandler.addCraftingRecipe(
                    werkstoff.get(screw),
                    GT_Proxy.tBits,
                    new Object[] { "fX", "X ", 'X', werkstoff.get(bolt) });

            GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(screw)).itemOutputs(werkstoff.get(dustTiny, 1))
                    .noFluidInputs().noFluidOutputs().duration(2 * TICKS).eut(8).addTo(sMaceratorRecipes);

            if (werkstoff.hasItemType(gem)) return;

            // ring

            GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(ingot), ItemList.Shape_Extruder_Ring.get(0L))
                    .itemOutputs(werkstoff.get(ring, 4)).noFluidInputs().noFluidOutputs()
                    .duration((int) Math.max(werkstoff.getStats().getMass() * 2L, 1)).eut(6 * tVoltageMultiplier)
                    .addTo(sExtruderRecipes);

            GT_ModHandler.addCraftingRecipe(
                    werkstoff.get(ring),
                    GT_Proxy.tBits,
                    new Object[] { "h ", "fX", 'X', werkstoff.get(stick) });

            // Gear
            GT_ModHandler.addCraftingRecipe(
                    werkstoff.get(gearGt),
                    GT_Proxy.tBits,
                    new Object[] { "SPS", "PwP", "SPS", 'P', werkstoff.get(plate), 'S', werkstoff.get(stick) });

            GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(ingot, 4), ItemList.Shape_Extruder_Gear.get(0L))
                    .itemOutputs(werkstoff.get(gearGt)).noFluidInputs().noFluidOutputs()
                    .duration((int) Math.max(werkstoff.getStats().getMass() * 5L, 1)).eut(8 * tVoltageMultiplier)
                    .addTo(sExtruderRecipes);
            // wireFine

            GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(ingot), ItemList.Shape_Extruder_Wire.get(0L))
                    .itemOutputs(werkstoff.get(wireFine, 8)).noFluidInputs().noFluidOutputs()
                    .duration((int) Math.max(werkstoff.getStats().getMass() * 1.5F, 1F)).eut(8 * tVoltageMultiplier)
                    .addTo(sExtruderRecipes);

            GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(ingot), GT_Utility.getIntegratedCircuit(3))
                    .itemOutputs(werkstoff.get(wireFine, 8)).noFluidInputs().noFluidOutputs()
                    .duration((int) Math.max(werkstoff.getStats().getMass(), 1)).eut(8 * tVoltageMultiplier)
                    .addTo(sWiremillRecipes);

            GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(stick), GT_Utility.getIntegratedCircuit(3))
                    .itemOutputs(werkstoff.get(wireFine, 4)).noFluidInputs().noFluidOutputs()
                    .duration((int) Math.max(werkstoff.getStats().getMass() * 0.5F, 1F)).eut(8 * tVoltageMultiplier)
                    .addTo(sWiremillRecipes);

            // smallGear
            if (WerkstoffLoader.smallGearShape != null) {

                GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(ingot), WerkstoffLoader.smallGearShape.get(0L))
                        .itemOutputs(werkstoff.get(gearGtSmall)).noFluidInputs().noFluidOutputs()
                        .duration((int) werkstoff.getStats().getMass()).eut(8 * tVoltageMultiplier)
                        .addTo(sExtruderRecipes);

            }

            GT_ModHandler.addCraftingRecipe(
                    werkstoff.get(gearGtSmall),
                    GT_Proxy.tBits,
                    new Object[] { " S ", "hPx", " S ", 'S', werkstoff.get(stick), 'P', werkstoff.get(plate) });

            // Rotor
            GT_ModHandler.addCraftingRecipe(
                    werkstoff.get(rotor),
                    GT_Proxy.tBits,
                    new Object[] { "PhP", "SRf", "PdP", 'P', werkstoff.get(plate), 'R', werkstoff.get(ring), 'S',
                            werkstoff.get(screw) });

            GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(plate, 4), werkstoff.get(ring))
                    .itemOutputs(werkstoff.get(rotor)).fluidInputs(Materials.Tin.getMolten(32)).noFluidOutputs()
                    .duration(12 * SECONDS).eut(24).addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(plate, 4), werkstoff.get(ring))
                    .itemOutputs(werkstoff.get(rotor)).fluidInputs(Materials.Lead.getMolten(48)).noFluidOutputs()
                    .duration(12 * SECONDS).eut(24).addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(plate, 4), werkstoff.get(ring))
                    .itemOutputs(werkstoff.get(rotor)).fluidInputs(Materials.SolderingAlloy.getMolten(16))
                    .noFluidOutputs().duration(12 * SECONDS).eut(24).addTo(sAssemblerRecipes);

            if (WerkstoffLoader.rotorShape != null) {

                GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(ingot, 5), WerkstoffLoader.rotorShape.get(0L))
                        .itemOutputs(werkstoff.get(rotor)).noFluidInputs().noFluidOutputs().duration(10 * SECONDS)
                        .eut(60).addTo(sExtruderRecipes);

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

            GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(gearGt)).itemOutputs(werkstoff.get(dust, 4))
                    .noFluidInputs().noFluidOutputs().duration(2 * TICKS).eut(8).addTo(sMaceratorRecipes);

            GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(gearGtSmall)).itemOutputs(werkstoff.get(dust, 1))
                    .noFluidInputs().noFluidOutputs().duration(2 * TICKS).eut(8).addTo(sMaceratorRecipes);

            GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(rotor))
                    .itemOutputs(werkstoff.get(dust, 4), werkstoff.get(dustSmall)).noFluidInputs().noFluidOutputs()
                    .duration(2 * TICKS).eut(8).addTo(sMaceratorRecipes);

            GT_Values.RA.stdBuilder().itemInputs(werkstoff.get(ring)).itemOutputs(werkstoff.get(dustSmall, 1))
                    .noFluidInputs().noFluidOutputs().duration(2 * TICKS).eut(8).addTo(sMaceratorRecipes);

        }
    }
}
