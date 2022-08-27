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

import com.github.bartimaeusnek.bartworks.API.LoaderReference;
import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.bartworks.system.material.werkstoff_loaders.IWerkstoffRunnable;
import com.github.bartimaeusnek.bartworks.util.BWRecipes;
import com.github.bartimaeusnek.bartworks.util.Pair;
import gregtech.api.enums.*;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.behaviors.Behaviour_DataOrb;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class CellLoader implements IWerkstoffRunnable {
    @Override
    @SuppressWarnings("unchecked")
    public void run(Werkstoff werkstoff) {
        if (!werkstoff.hasItemType(cell)) return;

        if ((werkstoff.getStats().isElektrolysis() || werkstoff.getStats().isCentrifuge())
                && !werkstoff.hasItemType(dust)) {
            List<FluidStack> flOutputs = new ArrayList<>();
            List<ItemStack> stOutputs = new ArrayList<>();
            HashMap<ISubTagContainer, Pair<Integer, Integer>> tracker = new HashMap<>();
            int cells = 0;
            for (Pair<ISubTagContainer, Integer> container :
                    werkstoff.getContents().getValue().toArray(new Pair[0])) {
                if (container.getKey() instanceof Materials) {
                    if (((Materials) container.getKey()).hasCorrespondingGas()
                            || ((Materials) container.getKey()).hasCorrespondingFluid()
                            || ((Materials) container.getKey()).mIconSet == TextureSet.SET_FLUID) {
                        FluidStack tmpFl = ((Materials) container.getKey()).getGas(1000 * container.getValue());
                        if (tmpFl == null || tmpFl.getFluid() == null) {
                            tmpFl = ((Materials) container.getKey()).getFluid(1000 * container.getValue());
                        }
                        flOutputs.add(tmpFl);
                        if (flOutputs.size() > 1) {
                            if (!tracker.containsKey(container.getKey())) {
                                stOutputs.add(((Materials) container.getKey()).getCells(container.getValue()));
                                tracker.put(container.getKey(), new Pair<>(container.getValue(), stOutputs.size() - 1));
                            } else {
                                stOutputs.add(((Materials) container.getKey())
                                        .getCells(
                                                tracker.get(container.getKey()).getKey() + container.getValue()));
                                stOutputs.remove(tracker.get(container.getKey()).getValue() + 1);
                            }
                            cells += container.getValue();
                        }
                    } else {
                        if (((Materials) container.getKey()).getDust(container.getValue()) == null) continue;
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
                        FluidStack tmpFl = ((Werkstoff) container.getKey()).getFluidOrGas(1000 * container.getValue());
                        if (tmpFl == null || tmpFl.getFluid() == null) {
                            tmpFl = ((Werkstoff) container.getKey()).getFluidOrGas(1000 * container.getValue());
                        }
                        flOutputs.add(tmpFl);
                        if (flOutputs.size() > 1) {
                            if (!tracker.containsKey(container.getKey())) {
                                stOutputs.add(((Werkstoff) container.getKey()).get(cell, container.getValue()));
                                tracker.put(container.getKey(), new Pair<>(container.getValue(), stOutputs.size() - 1));
                            } else {
                                stOutputs.add(((Werkstoff) container.getKey())
                                        .get(
                                                cell,
                                                tracker.get(container.getKey()).getKey() + container.getValue()));
                                stOutputs.remove(tracker.get(container.getKey()).getValue() + 1);
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
                                    .get(dust, (tracker.get(container.getKey()).getKey() + container.getValue())));
                            stOutputs.remove(tracker.get(container.getKey()).getValue() + 1);
                        }
                    }
                }
            }
            ItemStack input = werkstoff.get(cell);
            input.stackSize = 1;

            int cellEmpty = cells - 1;

            stOutputs.add(Materials.Empty.getCells(-cellEmpty));
            if (werkstoff.getStats().isElektrolysis())
                GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes.add(new BWRecipes.DynamicGTRecipe(
                        true,
                        new ItemStack[] {input, cellEmpty > 0 ? Materials.Empty.getCells(cellEmpty) : null},
                        stOutputs.toArray(new ItemStack[0]),
                        null,
                        null,
                        new FluidStack[] {null},
                        new FluidStack[] {flOutputs.size() > 0 ? flOutputs.get(0) : null},
                        (int) Math.max(
                                1L,
                                Math.abs(werkstoff.getStats().getProtons()
                                        * werkstoff.getContents().getValue().size())),
                        Math.min(4, werkstoff.getContents().getValue().size()) * 30,
                        0));
            if (werkstoff.getStats().isCentrifuge())
                GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.add(new BWRecipes.DynamicGTRecipe(
                        true,
                        new ItemStack[] {input, cellEmpty > 0 ? Materials.Empty.getCells(cellEmpty) : null},
                        stOutputs.toArray(new ItemStack[0]),
                        null,
                        null,
                        new FluidStack[] {null},
                        new FluidStack[] {flOutputs.size() > 0 ? flOutputs.get(0) : null},
                        (int) Math.max(
                                1L,
                                Math.abs(werkstoff.getStats().getMass()
                                        * werkstoff.getContents().getValue().size())),
                        Math.min(4, werkstoff.getContents().getValue().size()) * 5,
                        0));
        }

        // Tank "Recipe"
        GT_Utility.addFluidContainerData(new FluidContainerRegistry.FluidContainerData(
                new FluidStack(Objects.requireNonNull(WerkstoffLoader.fluids.get(werkstoff)), 1000),
                werkstoff.get(cell),
                Materials.Empty.getCells(1)));
        FluidContainerRegistry.registerFluidContainer(
                werkstoff.getFluidOrGas(1).getFluid(), werkstoff.get(cell), Materials.Empty.getCells(1));
        GT_Values.RA.addFluidCannerRecipe(
                Materials.Empty.getCells(1),
                werkstoff.get(cell),
                new FluidStack(Objects.requireNonNull(WerkstoffLoader.fluids.get(werkstoff)), 1000),
                GT_Values.NF);
        GT_Values.RA.addFluidCannerRecipe(
                werkstoff.get(cell),
                Materials.Empty.getCells(1),
                GT_Values.NF,
                new FluidStack(Objects.requireNonNull(WerkstoffLoader.fluids.get(werkstoff)), 1000));

        if (LoaderReference.Forestry) {
            FluidContainerRegistry.FluidContainerData emptyData = new FluidContainerRegistry.FluidContainerData(
                    new FluidStack(Objects.requireNonNull(WerkstoffLoader.fluids.get(werkstoff)), 1000),
                    werkstoff.get(capsule),
                    GT_ModHandler.getModItem("Forestry", "waxCapsule", 1),
                    true);
            GT_Utility.addFluidContainerData(emptyData);
            FluidContainerRegistry.registerFluidContainer(emptyData);
            GT_Values.RA.addFluidCannerRecipe(
                    werkstoff.get(capsule),
                    GT_Values.NI,
                    GT_Values.NF,
                    new FluidStack(Objects.requireNonNull(WerkstoffLoader.fluids.get(werkstoff)), 1000));
        }

        if (werkstoff.hasItemType(dust)) {
            GT_Values.RA.addFluidExtractionRecipe(
                    werkstoff.get(dust),
                    null,
                    werkstoff.getFluidOrGas(1000),
                    0,
                    (int) werkstoff.getStats().getMass(),
                    werkstoff.getStats().getMass() > 128 ? 64 : 30);
            GT_Values.RA.addFluidSolidifierRecipe(
                    GT_Utility.getIntegratedCircuit(1),
                    werkstoff.getFluidOrGas(1000),
                    werkstoff.get(dust),
                    (int) werkstoff.getStats().getMass(),
                    werkstoff.getStats().getMass() > 128 ? 64 : 30);
        }

        if (werkstoff.getType().equals(Werkstoff.Types.ELEMENT)) {
            Materials werkstoffBridgeMaterial = null;
            boolean ElementSet = false;
            for (Element e : Element.values()) {
                if (e.toString().equals(werkstoff.getToolTip())) {
                    werkstoffBridgeMaterial = werkstoff.getBridgeMaterial() != null
                            ? werkstoff.getBridgeMaterial()
                            : new Materials(
                                    -1,
                                    werkstoff.getTexSet(),
                                    0,
                                    0,
                                    0,
                                    false,
                                    werkstoff.getDefaultName(),
                                    werkstoff.getDefaultName());
                    werkstoffBridgeMaterial.mElement = e;
                    e.mLinkedMaterials.add(werkstoffBridgeMaterial);
                    ElementSet = true;
                    werkstoff.setBridgeMaterial(werkstoffBridgeMaterial);
                    break;
                }
            }
            if (!ElementSet) return;

            GT_OreDictUnificator.addAssociation(cell, werkstoffBridgeMaterial, werkstoff.get(cell), false);
            //            try {
            //                Field f = Materials.class.getDeclaredField("MATERIALS_MAP");
            //                f.setAccessible(true);
            //                Map<String, Materials> MATERIALS_MAP = (Map<String, Materials>) f.get(null);
            //                MATERIALS_MAP.remove(werkstoffBridgeMaterial.mName);
            //            } catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
            //                e.printStackTrace();
            //            }
            ItemStack scannerOutput = ItemList.Tool_DataOrb.get(1L);
            Behaviour_DataOrb.setDataTitle(scannerOutput, "Elemental-Scan");
            Behaviour_DataOrb.setDataName(scannerOutput, werkstoff.getToolTip());
            GT_Recipe.GT_Recipe_Map.sScannerFakeRecipes.addFakeRecipe(
                    false,
                    new BWRecipes.DynamicGTRecipe(
                            false,
                            new ItemStack[] {werkstoff.get(cell)},
                            new ItemStack[] {scannerOutput},
                            ItemList.Tool_DataOrb.get(1L),
                            null,
                            null,
                            null,
                            (int) (werkstoffBridgeMaterial.getMass() * 8192L),
                            30,
                            0));
            // GT_Recipe.GT_Recipe_Map.sReplicatorFakeRecipes.addFakeRecipe(false, new BWRecipes.DynamicGTRecipe(false,
            // new ItemStack[]{Materials.Empty.getCells(1)}, new ItemStack[]{werkstoff.get(cell)}, scannerOutput, null,
            // new FluidStack[]{Materials.UUMatter.getFluid(werkstoffBridgeMaterial.getMass())}, null, (int)
            // (werkstoffBridgeMaterial.getMass() * 512L), 30, 0));
        }
    }
}
