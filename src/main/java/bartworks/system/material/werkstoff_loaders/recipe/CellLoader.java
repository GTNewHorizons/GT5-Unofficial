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

import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.OrePrefixes.capsule;
import static gregtech.api.enums.OrePrefixes.cell;
import static gregtech.api.enums.OrePrefixes.dust;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.recipe.RecipeMaps.scannerFakeRecipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;

import bartworks.system.material.Werkstoff;
import bartworks.system.material.WerkstoffLoader;
import bartworks.system.material.werkstoff_loaders.IWerkstoffRunnable;
import gregtech.api.enums.Element;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.MaterialBuilder;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TextureSet;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.recipe.RecipeCategories;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.common.items.behaviors.BehaviourDataOrb;

public class CellLoader implements IWerkstoffRunnable {

    @Override
    @SuppressWarnings("unchecked")
    public void run(Werkstoff werkstoff) {
        if (!werkstoff.hasItemType(cell)) return;

        if ((werkstoff.getStats()
            .isElektrolysis()
            || werkstoff.getStats()
                .isCentrifuge())
            && !werkstoff.hasItemType(dust)) {

            if (werkstoff.getContents()
                .getValue()
                .size() > 0) {

                List<FluidStack> flOutputs = new ArrayList<>();
                List<ItemStack> stOutputs = new ArrayList<>();
                HashMap<ISubTagContainer, Pair<Integer, Integer>> tracker = new HashMap<>();
                int cells = 0;
                for (Pair<ISubTagContainer, Integer> container : werkstoff.getContents()
                    .getValue()
                    .toArray(new Pair[0])) {
                    if (container.getKey() instanceof Materials) {
                        if ((((Materials) container.getKey()).getGas(1) != null
                            || ((Materials) container.getKey()).getFluid(1) != null
                            || ((Materials) container.getKey()).mIconSet == TextureSet.SET_FLUID)
                            && ((Materials) container.getKey()).getDust(0) == null) {
                            FluidStack tmpFl = ((Materials) container.getKey()).getGas(1000L * container.getValue());
                            if (tmpFl == null || tmpFl.getFluid() == null) {
                                tmpFl = ((Materials) container.getKey()).getFluid(1000L * container.getValue());
                            }
                            flOutputs.add(tmpFl);
                            if (flOutputs.size() > 1) {
                                if (!tracker.containsKey(container.getKey())) {
                                    stOutputs.add(((Materials) container.getKey()).getCells(container.getValue()));
                                    tracker
                                        .put(container.getKey(), Pair.of(container.getValue(), stOutputs.size() - 1));
                                } else {
                                    stOutputs.add(
                                        ((Materials) container.getKey()).getCells(
                                            tracker.get(container.getKey())
                                                .getKey() + container.getValue()));
                                    stOutputs.remove(
                                        tracker.get(container.getKey())
                                            .getValue() + 1);
                                }
                                cells += container.getValue();
                            }
                        } else {
                            if (((Materials) container.getKey()).getDust(container.getValue()) == null) continue;
                            if (!tracker.containsKey(container.getKey())) {
                                stOutputs.add(((Materials) container.getKey()).getDust(container.getValue()));
                                tracker.put(container.getKey(), Pair.of(container.getValue(), stOutputs.size() - 1));
                            } else {
                                stOutputs.add(
                                    ((Materials) container.getKey()).getDust(
                                        tracker.get(container.getKey())
                                            .getKey() + container.getValue()));
                                stOutputs.remove(
                                    tracker.get(container.getKey())
                                        .getValue() + 1);
                            }
                        }
                    } else if (container.getKey() instanceof Werkstoff) {
                        if (((Werkstoff) container.getKey()).getStats()
                            .isGas() || ((Werkstoff) container.getKey()).hasItemType(cell)) {
                            FluidStack tmpFl = ((Werkstoff) container.getKey())
                                .getFluidOrGas(1000 * container.getValue());
                            if (tmpFl == null || tmpFl.getFluid() == null) {
                                tmpFl = ((Werkstoff) container.getKey()).getFluidOrGas(1000 * container.getValue());
                            }
                            flOutputs.add(tmpFl);
                            if (flOutputs.size() > 1) {
                                if (!tracker.containsKey(container.getKey())) {
                                    stOutputs.add(((Werkstoff) container.getKey()).get(cell, container.getValue()));
                                    tracker
                                        .put(container.getKey(), Pair.of(container.getValue(), stOutputs.size() - 1));
                                } else {
                                    stOutputs.add(
                                        ((Werkstoff) container.getKey()).get(
                                            cell,
                                            tracker.get(container.getKey())
                                                .getKey() + container.getValue()));
                                    stOutputs.remove(
                                        tracker.get(container.getKey())
                                            .getValue() + 1);
                                }
                                cells += container.getValue();
                            }
                        } else {
                            if (!((Werkstoff) container.getKey()).hasItemType(dust)) continue;
                            if (!tracker.containsKey(container.getKey())) {
                                stOutputs.add(((Werkstoff) container.getKey()).get(dust, container.getValue()));
                                tracker.put(container.getKey(), Pair.of(container.getValue(), stOutputs.size() - 1));
                            } else {
                                stOutputs.add(
                                    ((Werkstoff) container.getKey()).get(
                                        dust,
                                        tracker.get(container.getKey())
                                            .getKey() + container.getValue()));
                                stOutputs.remove(
                                    tracker.get(container.getKey())
                                        .getValue() + 1);
                            }
                        }
                    }
                }
                ItemStack input = werkstoff.get(cell);
                input.stackSize = 1;

                int cellEmpty = cells - 1;

                stOutputs.add(Materials.Empty.getCells(-cellEmpty));
                if (werkstoff.getStats()
                    .isElektrolysis())
                    RecipeMaps.electrolyzerRecipes.add(
                        new GTRecipe(
                            new ItemStack[] { input, cellEmpty > 0 ? Materials.Empty.getCells(cellEmpty) : null },
                            stOutputs.toArray(new ItemStack[0]),
                            null,
                            null,
                            new FluidStack[] { null },
                            new FluidStack[] { !flOutputs.isEmpty() ? flOutputs.get(0) : null },
                            (int) Math.max(
                                1L,
                                Math.abs(
                                    werkstoff.getStats()
                                        .getProtons()
                                        * werkstoff.getContents()
                                            .getValue()
                                            .size())),
                            Math.min(
                                4,
                                werkstoff.getContents()
                                    .getValue()
                                    .size())
                                * 30,
                            0));
                if (werkstoff.getStats()
                    .isCentrifuge())
                    RecipeMaps.centrifugeRecipes.add(
                        new GTRecipe(
                            new ItemStack[] { input, cellEmpty > 0 ? Materials.Empty.getCells(cellEmpty) : null },
                            stOutputs.toArray(new ItemStack[0]),
                            null,
                            null,
                            new FluidStack[] { null },
                            new FluidStack[] { !flOutputs.isEmpty() ? flOutputs.get(0) : null },
                            (int) Math.max(
                                1L,
                                Math.abs(
                                    werkstoff.getStats()
                                        .getMass()
                                        * werkstoff.getContents()
                                            .getValue()
                                            .size())),
                            Math.min(
                                4,
                                werkstoff.getContents()
                                    .getValue()
                                    .size())
                                * 5,
                            0));
            } else {
                GTLog.err.println(
                    "Autogenerated recipe(s) using Werkstoff material '" + werkstoff.getDefaultName()
                        + "' (fluid) removed due to no contents in material definition.");
            }
        }

        // Tank "Recipe"
        GTUtility.addFluidContainerData(
            new FluidContainerRegistry.FluidContainerData(
                new FluidStack(Objects.requireNonNull(WerkstoffLoader.fluids.get(werkstoff)), 1000),
                werkstoff.get(cell),
                Materials.Empty.getCells(1)));
        FluidContainerRegistry.registerFluidContainer(
            werkstoff.getFluidOrGas(1)
                .getFluid(),
            werkstoff.get(cell),
            Materials.Empty.getCells(1));

        if (Forestry.isModLoaded()) {
            FluidContainerRegistry.FluidContainerData emptyData = new FluidContainerRegistry.FluidContainerData(
                new FluidStack(Objects.requireNonNull(WerkstoffLoader.fluids.get(werkstoff)), 1000),
                werkstoff.get(capsule),
                GTModHandler.getModItem(Forestry.ID, "waxCapsule", 1),
                true);
            GTUtility.addFluidContainerData(emptyData);
            FluidContainerRegistry.registerFluidContainer(emptyData);

        }

        if (werkstoff.hasItemType(dust)) {

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(dust))
                .fluidOutputs(werkstoff.getFluidOrGas(1_000))
                .duration(
                    werkstoff.getStats()
                        .getMass())
                .eut(
                    werkstoff.getStats()
                        .getMass() > 128 ? 64 : 30)
                .recipeCategory(RecipeCategories.fluidExtractorRecycling)
                .addTo(fluidExtractionRecipes);

            GTValues.RA.stdBuilder()
                .circuit(1)
                .itemOutputs(werkstoff.get(dust))
                .fluidInputs(werkstoff.getFluidOrGas(1_000))
                .duration(
                    (int) werkstoff.getStats()
                        .getMass())
                .eut(
                    werkstoff.getStats()
                        .getMass() > 128 ? 64 : 30)
                .addTo(fluidSolidifierRecipes);

        }

        if (Werkstoff.Types.ELEMENT.equals(werkstoff.getType())) {
            Materials werkstoffBridgeMaterial = null;
            boolean ElementSet = false;
            for (Element e : Element.values()) {
                if (e.toString()
                    .equals(werkstoff.getToolTip())) {
                    werkstoffBridgeMaterial = werkstoff.getBridgeMaterial() != null ? werkstoff.getBridgeMaterial()
                        : new MaterialBuilder().setName(werkstoff.getDefaultName())
                            .setDefaultLocalName(werkstoff.getDefaultName())
                            .setUnifiable(false)
                            .setIconSet(werkstoff.getTexSet())
                            .constructMaterial();
                    werkstoffBridgeMaterial.mElement = e;
                    e.mLinkedMaterials.add(werkstoffBridgeMaterial);
                    ElementSet = true;
                    werkstoff.setBridgeMaterial(werkstoffBridgeMaterial);
                    break;
                }
            }
            if (!ElementSet) return;

            GTOreDictUnificator.addAssociation(cell, werkstoffBridgeMaterial, werkstoff.get(cell), false);

            ItemStack scannerOutput = ItemList.Tool_DataOrb.get(1L);
            BehaviourDataOrb.setDataTitle(scannerOutput, "Elemental-Scan");
            BehaviourDataOrb.setDataName(scannerOutput, werkstoff.getToolTip());
            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(cell))
                .itemOutputs(scannerOutput)
                .special(ItemList.Tool_DataOrb.get(1L))
                .duration(werkstoffBridgeMaterial.getMass() * 8192)
                .eut(TierEU.RECIPE_LV)
                .ignoreCollision()
                .fake()
                .addTo(scannerFakeRecipes);
        }
    }
}
