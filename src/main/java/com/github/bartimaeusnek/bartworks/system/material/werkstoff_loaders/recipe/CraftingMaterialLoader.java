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

import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.bartworks.system.material.werkstoff_loaders.IWerkstoffRunnable;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Proxy;
import net.minecraft.item.ItemStack;

public class CraftingMaterialLoader implements IWerkstoffRunnable {
    @Override
    public void run(Werkstoff werkstoff) {
        if (werkstoff.hasItemType(screw)) {
            int tVoltageMultiplier = werkstoff.getStats().getMeltingPoint() >= 2800 ? 60 : 15;

            // bolt
            GT_Values.RA.addExtruderRecipe(
                    werkstoff.hasItemType(gem) ? werkstoff.get(gem) : werkstoff.get(ingot),
                    ItemList.Shape_Extruder_Bolt.get(0L),
                    werkstoff.get(bolt, 8),
                    (int) Math.max(werkstoff.getStats().getMass() * 2L, 1),
                    8 * tVoltageMultiplier);
            GT_Values.RA.addCutterRecipe(
                    werkstoff.get(stick),
                    werkstoff.get(bolt, 4),
                    null,
                    (int) Math.max(werkstoff.getStats().getMass() * 2L, 1L),
                    4);
            GT_Values.RA.addPulveriserRecipe(
                    werkstoff.get(bolt), new ItemStack[] {werkstoff.get(dustTiny, 1)}, null, 2, 8);

            // screw
            GT_Values.RA.addLatheRecipe(
                    werkstoff.get(bolt),
                    werkstoff.get(screw),
                    null,
                    (int) Math.max(werkstoff.getStats().getMass() / 8L, 1L),
                    4);
            GT_ModHandler.addCraftingRecipe(
                    werkstoff.get(screw), GT_Proxy.tBits, new Object[] {"fX", "X ", 'X', werkstoff.get(bolt)});
            GT_Values.RA.addPulveriserRecipe(
                    werkstoff.get(screw), new ItemStack[] {werkstoff.get(dustTiny, 1)}, null, 2, 8);

            if (werkstoff.hasItemType(gem)) return;

            // ring
            GT_Values.RA.addExtruderRecipe(
                    werkstoff.get(ingot),
                    ItemList.Shape_Extruder_Ring.get(0L),
                    werkstoff.get(ring, 4),
                    (int) Math.max(werkstoff.getStats().getMass() * 2L, 1),
                    6 * tVoltageMultiplier);
            GT_ModHandler.addCraftingRecipe(
                    werkstoff.get(ring), GT_Proxy.tBits, new Object[] {"h ", "fX", 'X', werkstoff.get(stick)});

            // Gear
            GT_ModHandler.addCraftingRecipe(werkstoff.get(gearGt), GT_Proxy.tBits, new Object[] {
                "SPS", "PwP", "SPS", 'P', werkstoff.get(plate), 'S', werkstoff.get(stick)
            });
            GT_Values.RA.addExtruderRecipe(
                    werkstoff.get(ingot, 4),
                    ItemList.Shape_Extruder_Gear.get(0L),
                    werkstoff.get(gearGt),
                    (int) Math.max(werkstoff.getStats().getMass() * 5L, 1),
                    8 * tVoltageMultiplier);

            // wireFine
            GT_Values.RA.addExtruderRecipe(
                    werkstoff.get(ingot),
                    ItemList.Shape_Extruder_Wire.get(0L),
                    werkstoff.get(wireFine, 8),
                    (int) Math.max(werkstoff.getStats().getMass() * 1.5F, 1F),
                    8 * tVoltageMultiplier);
            GT_Values.RA.addWiremillRecipe(
                    werkstoff.get(ingot),
                    GT_Utility.getIntegratedCircuit(3),
                    werkstoff.get(wireFine, 8),
                    (int) Math.max(werkstoff.getStats().getMass(), 1),
                    8 * tVoltageMultiplier);
            GT_Values.RA.addWiremillRecipe(
                    werkstoff.get(stick),
                    GT_Utility.getIntegratedCircuit(3),
                    werkstoff.get(wireFine, 4),
                    (int) Math.max(werkstoff.getStats().getMass() * 0.5F, 1F),
                    8 * tVoltageMultiplier);

            // smallGear
            if (WerkstoffLoader.smallGearShape != null)
                GT_Values.RA.addExtruderRecipe(
                        werkstoff.get(ingot),
                        WerkstoffLoader.smallGearShape.get(0L),
                        werkstoff.get(gearGtSmall),
                        (int) werkstoff.getStats().getMass(),
                        8 * tVoltageMultiplier);
            if (ConfigHandler.hardmode)
                GT_ModHandler.addCraftingRecipe(werkstoff.get(gearGtSmall), GT_Proxy.tBits, new Object[] {
                    " S ", "hPx", " S ", 'S', werkstoff.get(stick), 'P', werkstoff.get(plate)
                });
            else
                GT_ModHandler.addCraftingRecipe(werkstoff.get(gearGtSmall), GT_Proxy.tBits, new Object[] {
                    "P  ", " h ", 'P', werkstoff.get(plate)
                });

            // Rotor
            GT_ModHandler.addCraftingRecipe(werkstoff.get(rotor), GT_Proxy.tBits, new Object[] {
                "PhP", "SRf", "PdP", 'P', werkstoff.get(plate), 'R', werkstoff.get(ring), 'S', werkstoff.get(screw)
            });
            GT_Values.RA.addAssemblerRecipe(
                    werkstoff.get(plate, 4),
                    werkstoff.get(ring),
                    Materials.Tin.getMolten(32),
                    werkstoff.get(rotor),
                    240,
                    24);
            GT_Values.RA.addAssemblerRecipe(
                    werkstoff.get(plate, 4),
                    werkstoff.get(ring),
                    Materials.Lead.getMolten(48),
                    werkstoff.get(rotor),
                    240,
                    24);
            GT_Values.RA.addAssemblerRecipe(
                    werkstoff.get(plate, 4),
                    werkstoff.get(ring),
                    Materials.SolderingAlloy.getMolten(16),
                    werkstoff.get(rotor),
                    240,
                    24);

            if (WerkstoffLoader.rotorShape != null)
                GT_Values.RA.addExtruderRecipe(
                        werkstoff.get(ingot, 5), WerkstoffLoader.rotorShape.get(0L), werkstoff.get(rotor), 200, 60);

            // molten -> metal
            if (werkstoff.hasItemType(WerkstoffLoader.cellMolten)) {
                GT_Values.RA.addFluidSolidifierRecipe(
                        ItemList.Shape_Mold_Gear.get(0L), werkstoff.getMolten(576), werkstoff.get(gearGt), 128, 8);
                GT_Values.RA.addFluidSolidifierRecipe(
                        ItemList.Shape_Mold_Gear_Small.get(0L),
                        werkstoff.getMolten(144),
                        werkstoff.get(gearGtSmall),
                        16,
                        8);
                if (WerkstoffLoader.ringMold != null)
                    GT_Values.RA.addFluidSolidifierRecipe(
                            WerkstoffLoader.ringMold.get(0L),
                            werkstoff.getMolten(36),
                            werkstoff.get(ring),
                            100,
                            4 * tVoltageMultiplier);
                if (WerkstoffLoader.boltMold != null)
                    GT_Values.RA.addFluidSolidifierRecipe(
                            WerkstoffLoader.boltMold.get(0L),
                            werkstoff.getMolten(18),
                            werkstoff.get(bolt),
                            50,
                            2 * tVoltageMultiplier);

                if (WerkstoffLoader.rotorMold != null)
                    GT_Values.RA.addFluidSolidifierRecipe(
                            WerkstoffLoader.rotorMold.get(0L), werkstoff.getMolten(612), werkstoff.get(rotor), 100, 60);
            }

            GT_Values.RA.addPulveriserRecipe(
                    werkstoff.get(gearGt), new ItemStack[] {werkstoff.get(dust, 4)}, null, 2, 8);
            GT_Values.RA.addPulveriserRecipe(
                    werkstoff.get(gearGtSmall), new ItemStack[] {werkstoff.get(dust, 1)}, null, 2, 8);
            GT_Values.RA.addPulveriserRecipe(
                    werkstoff.get(rotor),
                    new ItemStack[] {werkstoff.get(dust, 4), werkstoff.get(dustSmall)},
                    null,
                    2,
                    8);
            GT_Values.RA.addPulveriserRecipe(
                    werkstoff.get(ring), new ItemStack[] {werkstoff.get(dustSmall, 1)}, null, 2, 8);
        }
    }
}
