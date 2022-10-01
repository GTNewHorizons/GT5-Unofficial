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
import com.github.bartimaeusnek.bartworks.util.BWRecipes;
import com.github.bartimaeusnek.bartworks.util.Pair;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TextureSet;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class DustLoader implements IWerkstoffRunnable {

    @Override
    @SuppressWarnings("unchecked")
    public void run(Werkstoff werkstoff) {
        if (werkstoff.hasItemType(dust)) {
            List<FluidStack> flOutputs = new ArrayList<>();
            List<ItemStack> stOutputs = new ArrayList<>();
            HashMap<ISubTagContainer, Pair<Integer, Integer>> tracker = new HashMap<>();
            int cells = 0;

            if (werkstoff.getGenerationFeatures().hasMixerRecipes()
                    || werkstoff.getStats().isElektrolysis()
                    || werkstoff.getStats().isCentrifuge()
                    || werkstoff.getGenerationFeatures().hasChemicalRecipes()) {
                for (Pair<ISubTagContainer, Integer> container :
                        werkstoff.getContents().getValue().toArray(new Pair[0])) {
                    if (container.getKey() instanceof Materials) {
                        if (((Materials) container.getKey()).getGas(0) != null
                                || ((Materials) container.getKey()).getFluid(0) != null
                                || ((Materials) container.getKey()).mIconSet == TextureSet.SET_FLUID) {
                            FluidStack tmpFl = ((Materials) container.getKey()).getGas(1000 * container.getValue());
                            if (tmpFl == null || tmpFl.getFluid() == null) {
                                tmpFl = ((Materials) container.getKey()).getFluid(1000 * container.getValue());
                            }
                            flOutputs.add(tmpFl);
                            if (flOutputs.size() > 1) {
                                if (!tracker.containsKey(container.getKey())) {
                                    stOutputs.add(((Materials) container.getKey()).getCells(container.getValue()));
                                    tracker.put(
                                            container.getKey(), new Pair<>(container.getValue(), stOutputs.size() - 1));
                                } else {
                                    stOutputs.add(((Materials) container.getKey())
                                            .getCells(tracker.get(container.getKey())
                                                            .getKey()
                                                    + container.getValue()));
                                    stOutputs.remove(
                                            tracker.get(container.getKey()).getValue() + 1);
                                }
                                cells += container.getValue();
                            }
                        } else {
                            if (((Materials) container.getKey()).getDust(container.getValue()) == null) {
                                if (((Materials) container.getKey()).getCells(container.getValue()) != null
                                        && (((Materials) container.getKey()).getMolten(0) != null
                                                || ((Materials) container.getKey()).getSolid(0) != null)) {
                                    FluidStack tmpFl =
                                            ((Materials) container.getKey()).getMolten(1000 * container.getValue());
                                    if (tmpFl == null || tmpFl.getFluid() == null) {
                                        tmpFl = ((Materials) container.getKey()).getSolid(1000 * container.getValue());
                                    }
                                    flOutputs.add(tmpFl);
                                    if (flOutputs.size() > 1) {
                                        if (!tracker.containsKey(container.getKey())) {
                                            stOutputs.add(
                                                    ((Materials) container.getKey()).getCells(container.getValue()));
                                            tracker.put(
                                                    container.getKey(),
                                                    new Pair<>(container.getValue(), stOutputs.size() - 1));
                                        } else {
                                            stOutputs.add(((Materials) container.getKey())
                                                    .getCells(tracker.get(container.getKey())
                                                                    .getKey()
                                                            + container.getValue()));
                                            stOutputs.remove(tracker.get(container.getKey())
                                                            .getValue()
                                                    + 1);
                                        }
                                        cells += container.getValue();
                                    }
                                } else continue;
                            }
                            if (!tracker.containsKey(container.getKey())) {
                                stOutputs.add(((Materials) container.getKey()).getDust(container.getValue()));
                                tracker.put(container.getKey(), new Pair<>(container.getValue(), stOutputs.size() - 1));
                            } else {
                                stOutputs.add(((Materials) container.getKey())
                                        .getDust(tracker.get(container.getKey()).getKey() + container.getValue()));
                                stOutputs.remove(tracker.get(container.getKey()).getValue() + 1);
                            }
                        }
                    } else if (container.getKey() instanceof Werkstoff) {
                        if (((Werkstoff) container.getKey()).getStats().isGas()
                                || ((Werkstoff) container.getKey()).hasItemType(cell)) {
                            FluidStack tmpFl =
                                    ((Werkstoff) container.getKey()).getFluidOrGas(1000 * container.getValue());
                            if (tmpFl == null || tmpFl.getFluid() == null) {
                                tmpFl = ((Werkstoff) container.getKey()).getFluidOrGas(1000 * container.getValue());
                            }
                            flOutputs.add(tmpFl);
                            if (flOutputs.size() > 1) {
                                if (!tracker.containsKey(container.getKey())) {
                                    stOutputs.add(((Werkstoff) container.getKey()).get(cell, container.getValue()));
                                    tracker.put(
                                            container.getKey(), new Pair<>(container.getValue(), stOutputs.size() - 1));
                                } else {
                                    stOutputs.add(((Werkstoff) container.getKey())
                                            .get(
                                                    cell,
                                                    tracker.get(container.getKey())
                                                                    .getKey()
                                                            + container.getValue()));
                                    stOutputs.remove(
                                            tracker.get(container.getKey()).getValue() + 1);
                                }
                                cells += container.getValue();
                            }
                        } else {
                            if (!((Werkstoff) container.getKey()).hasItemType(dust)) continue;
                            if (!tracker.containsKey(container.getKey())) {
                                stOutputs.add(((Werkstoff) container.getKey()).get(dust, container.getValue()));
                                tracker.put(container.getKey(), new Pair<>(container.getValue(), stOutputs.size() - 1));
                            } else {
                                stOutputs.add(((Werkstoff) container.getKey())
                                        .get(
                                                dust,
                                                (tracker.get(container.getKey()).getKey() + container.getValue())));
                                stOutputs.remove(tracker.get(container.getKey()).getValue() + 1);
                            }
                        }
                    }
                }
                ItemStack input = werkstoff.get(dust);
                input.stackSize = werkstoff.getContents().getKey();
                if (werkstoff.getStats().isElektrolysis()) {
                    GT_Recipe tRecipe = new BWRecipes.DynamicGTRecipe(
                            true,
                            new ItemStack[] {input, cells > 0 ? Materials.Empty.getCells(cells) : null},
                            stOutputs.toArray(new ItemStack[0]),
                            null,
                            null,
                            new FluidStack[] {null},
                            new FluidStack[] {flOutputs.size() > 0 ? flOutputs.get(0) : null},
                            (int) Math.max(
                                    1L,
                                    Math.abs(werkstoff.getStats().getProtons()
                                            / werkstoff.getContents().getValue().size())),
                            Math.min(4, werkstoff.getContents().getValue().size()) * 30,
                            0);
                    GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes.add(tRecipe);
                    GT_Recipe.GT_Recipe_Map.sMultiblockElectrolyzerRecipes.add(tRecipe);
                }
                if (werkstoff.getStats().isCentrifuge()) {
                    GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.add(new BWRecipes.DynamicGTRecipe(
                            true,
                            new ItemStack[] {input, cells > 0 ? Materials.Empty.getCells(cells) : null},
                            stOutputs.toArray(new ItemStack[0]),
                            null,
                            null,
                            new FluidStack[] {null},
                            new FluidStack[] {flOutputs.size() > 0 ? flOutputs.get(0) : null},
                            (int) Math.max(
                                    1L,
                                    Math.abs(werkstoff.getStats().getMass()
                                            / werkstoff.getContents().getValue().size())),
                            Math.min(4, werkstoff.getContents().getValue().size()) * 5,
                            0));
                    GT_Recipe tRecipe = new GT_Recipe(
                            false,
                            stOutputs.toArray(new ItemStack[0]),
                            new ItemStack[] {input},
                            null,
                            null,
                            new FluidStack[] {flOutputs.size() > 0 ? flOutputs.get(0) : null},
                            null,
                            (int) Math.max(
                                    1L,
                                    Math.abs(werkstoff.getStats().getProtons()
                                            / werkstoff.getContents().getValue().size())),
                            Math.min(4, werkstoff.getContents().getValue().size()) * 30,
                            0);
                    GT_Recipe.GT_Recipe_Map.sMultiblockCentrifugeRecipes.add(tRecipe);
                }
                if (werkstoff.getGenerationFeatures().hasChemicalRecipes()) {
                    if (cells > 0) stOutputs.add(Materials.Empty.getCells(cells));
                    GT_Recipe.GT_Recipe_Map.sChemicalRecipes.add(new BWRecipes.DynamicGTRecipe(
                            true,
                            stOutputs.toArray(new ItemStack[0]),
                            new ItemStack[] {input},
                            null,
                            null,
                            new FluidStack[] {flOutputs.size() > 0 ? flOutputs.get(0) : null},
                            null,
                            (int) Math.max(
                                    1L,
                                    Math.abs(werkstoff.getStats().getProtons()
                                            / werkstoff.getContents().getValue().size())),
                            Math.min(4, werkstoff.getContents().getValue().size()) * 30,
                            0));
                    GT_Recipe.GT_Recipe_Map.sMultiblockChemicalRecipes.addRecipe(
                            true,
                            stOutputs.toArray(new ItemStack[0]),
                            new ItemStack[] {input},
                            null,
                            null,
                            new FluidStack[] {flOutputs.size() > 0 ? flOutputs.get(0) : null},
                            null,
                            (int) Math.max(
                                    1L,
                                    Math.abs(werkstoff.getStats().getProtons()
                                            / werkstoff.getContents().getValue().size())),
                            Math.min(4, werkstoff.getContents().getValue().size()) * 30,
                            0);
                }
                if (werkstoff.getGenerationFeatures().hasMixerRecipes()) {
                    if (cells > 0) stOutputs.add(Materials.Empty.getCells(cells));
                    short circuitID = werkstoff.getMixCircuit();
                    ItemStack circuit = circuitID == -1 ? null : GT_Utility.getIntegratedCircuit(circuitID);
                    if (circuit != null) stOutputs.add(circuit);
                    GT_Recipe.GT_Recipe_Map.sMixerRecipes.add(new BWRecipes.DynamicGTRecipe(
                            true,
                            stOutputs.toArray(new ItemStack[0]),
                            new ItemStack[] {input},
                            null,
                            null,
                            new FluidStack[] {flOutputs.size() > 0 ? flOutputs.get(0) : null},
                            null,
                            (int) Math.max(
                                    1L,
                                    Math.abs(werkstoff.getStats().getMass()
                                            / werkstoff.getContents().getValue().size())),
                            Math.min(4, werkstoff.getContents().getValue().size()) * 5,
                            0));
                    GT_Recipe tRecipe = new GT_Recipe(
                            false,
                            stOutputs.toArray(new ItemStack[0]),
                            new ItemStack[] {input},
                            null,
                            null,
                            new FluidStack[] {flOutputs.size() > 0 ? flOutputs.get(0) : null},
                            null,
                            (int) Math.max(
                                    1L,
                                    Math.abs(werkstoff.getStats().getProtons()
                                            / werkstoff.getContents().getValue().size())),
                            Math.min(4, werkstoff.getContents().getValue().size()) * 30,
                            0);
                    GT_Recipe.GT_Recipe_Map.sMultiblockMixerRecipes.add(tRecipe);
                }
            }

            GT_ModHandler.addCraftingRecipe(
                    werkstoff.get(dust), new Object[] {"TTT", "TTT", "TTT", 'T', werkstoff.get(dustTiny)});
            GT_ModHandler.addCraftingRecipe(
                    werkstoff.get(dust),
                    new Object[] {"TT ", "TT ", 'T', WerkstoffLoader.getCorrespondingItemStack(dustSmall, werkstoff)});
            GT_ModHandler.addCraftingRecipe(
                    WerkstoffLoader.getCorrespondingItemStack(dustSmall, werkstoff, 4),
                    new Object[] {" T ", 'T', werkstoff.get(dust)});
            GT_ModHandler.addCraftingRecipe(
                    WerkstoffLoader.getCorrespondingItemStack(dustTiny, werkstoff, 9),
                    new Object[] {"T  ", 'T', werkstoff.get(dust)});

            GT_Values.RA.addBoxingRecipe(
                    werkstoff.get(dustTiny, 9), ItemList.Schematic_Dust.get(0L), werkstoff.get(dust), 100, 4);
            GT_Values.RA.addBoxingRecipe(
                    werkstoff.get(dustSmall, 4), ItemList.Schematic_Dust.get(0L), werkstoff.get(dust), 100, 4);
            GT_Values.RA.addBoxingRecipe(
                    werkstoff.get(dustTiny, 9), ItemList.Schematic_3by3.get(0L), werkstoff.get(dust), 100, 4);
            GT_Values.RA.addBoxingRecipe(
                    werkstoff.get(dustSmall, 4), ItemList.Schematic_2by2.get(0L), werkstoff.get(dust), 100, 4);

            if (werkstoff.hasItemType(ingot) && !werkstoff.getStats().isBlastFurnace()) {
                GT_ModHandler.addSmeltingRecipe(werkstoff.get(dust), werkstoff.get(ingot));
                GT_ModHandler.addSmeltingRecipe(werkstoff.get(dustTiny), werkstoff.get(nugget));
            } else if (werkstoff.hasItemType(ingot)
                    && werkstoff.getStats().isBlastFurnace()
                    && werkstoff.getStats().getMeltingPoint() != 0) {
                if (werkstoff.contains(WerkstoffLoader.ANAEROBE_SMELTING)) {
                    GT_Values.RA.addBlastRecipe(
                            werkstoff.get(dust),
                            GT_Utility.getIntegratedCircuit(11),
                            Materials.Nitrogen.getGas(1000),
                            null,
                            werkstoff.getStats().getMeltingPoint() < 1750
                                    ? werkstoff.get(ingot)
                                    : werkstoff.get(ingotHot),
                            null,
                            (int) Math.max(werkstoff.getStats().getMass() / 40L, 1L)
                                    * werkstoff.getStats().getMeltingPoint(),
                            werkstoff.getStats().getMeltingVoltage(),
                            werkstoff.getStats().getMeltingPoint());
                } else if (werkstoff.contains(WerkstoffLoader.NOBLE_GAS_SMELTING)) {
                    GT_Values.RA.addBlastRecipe(
                            werkstoff.get(dust),
                            GT_Utility.getIntegratedCircuit(11),
                            Materials.Argon.getGas(1000),
                            null,
                            werkstoff.getStats().getMeltingPoint() < 1750
                                    ? werkstoff.get(ingot)
                                    : werkstoff.get(ingotHot),
                            null,
                            (int) Math.max(werkstoff.getStats().getMass() / 40L, 1L)
                                    * werkstoff.getStats().getMeltingPoint(),
                            werkstoff.getStats().getMeltingVoltage(),
                            werkstoff.getStats().getMeltingPoint());
                } else {
                    GT_Values.RA.addBlastRecipe(
                            werkstoff.get(dust),
                            GT_Utility.getIntegratedCircuit(1),
                            null,
                            null,
                            werkstoff.getStats().getMeltingPoint() < 1750
                                    ? werkstoff.get(ingot)
                                    : werkstoff.get(ingotHot),
                            null,
                            (int) Math.max(werkstoff.getStats().getMass() / 40L, 1L)
                                    * werkstoff.getStats().getMeltingPoint(),
                            werkstoff.getStats().getMeltingVoltage(),
                            werkstoff.getStats().getMeltingPoint());
                    if (werkstoff.getStats().getMeltingPoint() <= 1000) {
                        GT_Values.RA.addPrimitiveBlastRecipe(
                                werkstoff.get(dust),
                                null,
                                9,
                                werkstoff.get(ingot),
                                null,
                                (int) Math.max(werkstoff.getStats().getMass() / 40L, 1L)
                                        * werkstoff.getStats().getMeltingPoint());
                        // GT_ModHandler.addRCBlastFurnaceRecipe(werkstoff.get(ingot), werkstoff.get(dust),
                        // werkstoff.getStats().getMeltingPoint());
                    }
                }
            }

            if (werkstoff.getStats().isBlastFurnace() && werkstoff.getStats().getMeltingPoint() > 1750) {
                GT_Values.RA.addVacuumFreezerRecipe(werkstoff.get(ingotHot), werkstoff.get(ingot), (int)
                        Math.max(werkstoff.getStats().getMass() * 3L, 1L));
            }

            if (werkstoff.hasItemType(ingot)) {
                GT_ModHandler.addPulverisationRecipe(werkstoff.get(ingot), werkstoff.get(dust));
                GT_ModHandler.addPulverisationRecipe(werkstoff.get(nugget), werkstoff.get(dustTiny));
            }
            if (werkstoff.hasItemType(ingot) || werkstoff.hasItemType(gem)) {
                GT_ModHandler.addPulverisationRecipe(werkstoff.get(block), werkstoff.get(dust, 9));
            }
        }
    }
}
