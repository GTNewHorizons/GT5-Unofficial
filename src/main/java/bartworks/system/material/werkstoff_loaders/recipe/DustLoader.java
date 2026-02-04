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

import static gregtech.api.enums.OrePrefixes.cell;
import static gregtech.api.enums.OrePrefixes.dust;
import static gregtech.api.enums.OrePrefixes.dustSmall;
import static gregtech.api.enums.OrePrefixes.dustTiny;
import static gregtech.api.enums.OrePrefixes.ingot;
import static gregtech.api.enums.OrePrefixes.ingotHot;
import static gregtech.api.enums.OrePrefixes.nugget;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.recipe.RecipeMaps.packagerRecipes;
import static gregtech.api.recipe.RecipeMaps.primitiveBlastRecipes;
import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.ADDITIVE_AMOUNT;
import static gregtech.api.util.GTRecipeConstants.BlastFurnaceWithGas;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;

import bartworks.system.material.Werkstoff;
import bartworks.system.material.WerkstoffLoader;
import bartworks.system.material.werkstoff_loaders.IWerkstoffRunnable;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TextureSet;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;

public class DustLoader implements IWerkstoffRunnable {

    @Override
    @SuppressWarnings("unchecked")
    public void run(Werkstoff werkstoff) {
        if (werkstoff.hasItemType(dust)) {
            List<FluidStack> flOutputs = new ArrayList<>();
            List<ItemStack> stOutputs = new ArrayList<>();
            HashMap<ISubTagContainer, Pair<Integer, Integer>> tracker = new HashMap<>();

            Werkstoff.Stats werkstoffStats = werkstoff.getStats();

            int cells = 0;

            if (werkstoff.getGenerationFeatures()
                .hasMixerRecipes() || werkstoffStats.isElektrolysis()
                || werkstoffStats.isCentrifuge()
                || werkstoff.getGenerationFeatures()
                    .hasChemicalRecipes()) {

                if (werkstoff.getContents()
                    .getValue()
                    .size() > 0) {

                    for (Pair<ISubTagContainer, Integer> container : werkstoff.getContents()
                        .getValue()
                        .toArray(new Pair[0])) {
                        final ISubTagContainer key = container.getKey();
                        final int value = container.getValue();
                        if (key instanceof Materials materialKey) {
                            if ((materialKey.getGas(1) != null || materialKey.getFluid(1) != null
                                || materialKey.mIconSet == TextureSet.SET_FLUID) && materialKey.getDust(0) == null) {
                                FluidStack tmpFl = materialKey.getGas(1000L * value);
                                if (tmpFl == null || tmpFl.getFluid() == null) {
                                    tmpFl = materialKey.getFluid(1000L * value);
                                }
                                flOutputs.add(tmpFl);
                                if (flOutputs.size() > 1) {
                                    if (!tracker.containsKey(key)) {
                                        stOutputs.add(materialKey.getCells(value));
                                        tracker.put(key, Pair.of(value, stOutputs.size() - 1));
                                    } else {
                                        stOutputs.add(
                                            materialKey.getCells(
                                                tracker.get(key)
                                                    .getKey() + value));
                                        stOutputs.remove(
                                            tracker.get(key)
                                                .getValue() + 1);
                                    }
                                    cells += value;
                                }
                            } else {
                                if (materialKey.getDust(value) == null) {
                                    if (materialKey.getCells(value) == null
                                        || materialKey.getMolten(0) == null && materialKey.getSolid(0) == null)
                                        continue;
                                    FluidStack tmpFl = materialKey.getMolten(1000L * value);
                                    if (tmpFl == null || tmpFl.getFluid() == null) {
                                        tmpFl = materialKey.getSolid(1000L * value);
                                    }
                                    flOutputs.add(tmpFl);
                                    if (flOutputs.size() > 1) {
                                        if (!tracker.containsKey(key)) {
                                            stOutputs.add(materialKey.getCells(value));
                                            tracker.put(key, Pair.of(value, stOutputs.size() - 1));
                                        } else {
                                            stOutputs.add(
                                                materialKey.getCells(
                                                    tracker.get(key)
                                                        .getKey() + value));
                                            stOutputs.remove(
                                                tracker.get(key)
                                                    .getValue() + 1);
                                        }
                                        cells += value;
                                    }
                                }
                                if (!tracker.containsKey(key)) {
                                    stOutputs.add(materialKey.getDust(value));
                                    tracker.put(key, Pair.of(value, stOutputs.size() - 1));
                                } else {
                                    stOutputs.add(
                                        materialKey.getDust(
                                            tracker.get(key)
                                                .getKey() + value));
                                    stOutputs.remove(
                                        tracker.get(key)
                                            .getValue() + 1);
                                }
                            }
                        } else if (key instanceof Werkstoff werkstoffKey) {
                            if (werkstoffKey.getStats()
                                .isGas() || werkstoffKey.hasItemType(cell)) {
                                FluidStack tmpFl = werkstoffKey.getFluidOrGas(1000 * value);
                                if (tmpFl == null || tmpFl.getFluid() == null) {
                                    tmpFl = werkstoffKey.getFluidOrGas(1000 * value);
                                }
                                flOutputs.add(tmpFl);
                                if (flOutputs.size() > 1) {
                                    if (!tracker.containsKey(key)) {
                                        stOutputs.add(werkstoffKey.get(cell, value));
                                        tracker.put(key, Pair.of(value, stOutputs.size() - 1));
                                    } else {
                                        stOutputs.add(
                                            werkstoffKey.get(
                                                cell,
                                                tracker.get(key)
                                                    .getKey() + value));
                                        stOutputs.remove(
                                            tracker.get(key)
                                                .getValue() + 1);
                                    }
                                    cells += value;
                                }
                            } else {
                                if (!werkstoffKey.hasItemType(dust)) continue;
                                if (!tracker.containsKey(key)) {
                                    stOutputs.add(werkstoffKey.get(dust, value));
                                    tracker.put(key, Pair.of(value, stOutputs.size() - 1));
                                } else {
                                    stOutputs.add(
                                        werkstoffKey.get(
                                            dust,
                                            tracker.get(key)
                                                .getKey() + value));
                                    stOutputs.remove(
                                        tracker.get(key)
                                            .getValue() + 1);
                                }
                            }
                        }
                    }
                    ItemStack input = werkstoff.get(dust);
                    input.stackSize = werkstoff.getContents()
                        .getKey();
                    if (werkstoffStats.isElektrolysis()) {
                        GTRecipe tRecipe = new GTRecipe(
                            new ItemStack[] { input, cells > 0 ? Materials.Empty.getCells(cells) : null },
                            stOutputs.toArray(new ItemStack[0]),
                            null,
                            null,
                            new FluidStack[] { null },
                            new FluidStack[] { !flOutputs.isEmpty() ? flOutputs.get(0) : null },
                            (int) Math.max(
                                1L,
                                Math.abs(
                                    werkstoffStats.getProtons() / werkstoff.getContents()
                                        .getValue()
                                        .size())),
                            Math.min(
                                4,
                                werkstoff.getContents()
                                    .getValue()
                                    .size())
                                * 30,
                            0);
                        RecipeMaps.electrolyzerRecipes.add(tRecipe);
                    }
                    if (werkstoffStats.isCentrifuge()) {
                        RecipeMaps.centrifugeRecipes.add(
                            new GTRecipe(
                                new ItemStack[] { input, cells > 0 ? Materials.Empty.getCells(cells) : null },
                                stOutputs.toArray(new ItemStack[0]),
                                null,
                                null,
                                new FluidStack[] { null },
                                new FluidStack[] { !flOutputs.isEmpty() ? flOutputs.get(0) : null },
                                (int) Math.max(
                                    1L,
                                    Math.abs(
                                        werkstoffStats.getMass() / werkstoff.getContents()
                                            .getValue()
                                            .size())),
                                Math.min(
                                    4,
                                    werkstoff.getContents()
                                        .getValue()
                                        .size())
                                    * 5,
                                0));
                    }
                    if (werkstoff.getGenerationFeatures()
                        .hasChemicalRecipes()) {
                        if (cells > 0) stOutputs.add(Materials.Empty.getCells(cells));
                        GTValues.RA.stdBuilder()
                            .itemInputs(stOutputs.toArray(new ItemStack[0]))
                            .itemOutputs(input)
                            .fluidInputs(flOutputs.toArray(new FluidStack[0]))
                            .duration(
                                (int) Math.max(
                                    1L,
                                    Math.abs(
                                        werkstoffStats.getProtons() / werkstoff.getContents()
                                            .getValue()
                                            .size())))
                            .eut(
                                Math.min(
                                    4,
                                    werkstoff.getContents()
                                        .getValue()
                                        .size())
                                    * 30)
                            .addTo(GTRecipeConstants.UniversalChemical);
                    }
                    if (werkstoff.getGenerationFeatures()
                        .hasMixerRecipes()) {
                        if (cells > 0) stOutputs.add(Materials.Empty.getCells(cells));
                        short circuitID = werkstoff.getMixCircuit();
                        ItemStack circuit = circuitID == -1 ? null : GTUtility.getIntegratedCircuit(circuitID);
                        if (circuit != null) stOutputs.add(circuit);
                        RecipeMaps.mixerRecipes.add(
                            new GTRecipe(
                                stOutputs.toArray(new ItemStack[0]),
                                new ItemStack[] { input },
                                null,
                                null,
                                new FluidStack[] { !flOutputs.isEmpty() ? flOutputs.get(0) : null },
                                null,
                                (int) Math.max(
                                    1L,
                                    Math.abs(
                                        werkstoffStats.getMass() / werkstoff.getContents()
                                            .getValue()
                                            .size())),
                                Math.min(
                                    4,
                                    werkstoff.getContents()
                                        .getValue()
                                        .size())
                                    * 5,
                                0));
                    }
                } else {
                    GTLog.err.println(
                        "Autogenerated recipe(s) using Werkstoff material '" + werkstoff.getDefaultName()
                            + "' (dust) removed due to no contents in material definition.");
                }
            }

            GTModHandler.addCraftingRecipe(
                werkstoff.get(dust),
                new Object[] { "TTT", "TTT", "TTT", 'T', werkstoff.get(dustTiny) });
            GTModHandler.addCraftingRecipe(
                werkstoff.get(dust),
                new Object[] { "TT ", "TT ", 'T', WerkstoffLoader.getCorrespondingItemStack(dustSmall, werkstoff) });
            GTModHandler.addCraftingRecipe(
                WerkstoffLoader.getCorrespondingItemStack(dustSmall, werkstoff, 4),
                new Object[] { " T ", 'T', werkstoff.get(dust) });
            GTModHandler.addCraftingRecipe(
                WerkstoffLoader.getCorrespondingItemStack(dustTiny, werkstoff, 9),
                new Object[] { "T  ", 'T', werkstoff.get(dust) });

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(dustTiny, 9), ItemList.Schematic_Dust.get(0L))
                .itemOutputs(werkstoff.get(dust))
                .duration(5 * SECONDS)
                .eut(4)
                .addTo(packagerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(dustSmall, 4), ItemList.Schematic_Dust.get(0L))
                .itemOutputs(werkstoff.get(dust))
                .duration(5 * SECONDS)
                .eut(4)
                .addTo(packagerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(dustTiny, 9), ItemList.Schematic_3by3.get(0L))
                .itemOutputs(werkstoff.get(dust))
                .duration(5 * SECONDS)
                .eut(4)
                .addTo(packagerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(dustSmall, 4), ItemList.Schematic_2by2.get(0L))
                .itemOutputs(werkstoff.get(dust))
                .duration(5 * SECONDS)
                .eut(4)
                .addTo(packagerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(dust, 1), ItemList.Schematic_Dust_Small.get(0L))
                .itemOutputs(werkstoff.get(dustSmall, 4))
                .duration(5 * SECONDS)
                .eut(4)
                .addTo(packagerRecipes);

            if (werkstoff.hasItemType(ingot) && !werkstoffStats.isBlastFurnace()) {
                GTModHandler.addSmeltingRecipe(werkstoff.get(dust), werkstoff.get(ingot));
                GTModHandler.addSmeltingRecipe(werkstoff.get(dustTiny), werkstoff.get(nugget));
            } else if (werkstoff.hasItemType(ingot) && werkstoffStats.isBlastFurnace()
                && werkstoffStats.getMeltingPoint() != 0
                && werkstoffStats.autoGenerateBlastFurnaceRecipes()) {
                    // Just adds all types of gasses
                    if (werkstoff.contains(WerkstoffLoader.ANAEROBE_SMELTING)
                        || werkstoff.contains(WerkstoffLoader.NOBLE_GAS_SMELTING)) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(werkstoff.get(dust))
                            .circuit(11)
                            .itemOutputs(
                                werkstoffStats.getMeltingPoint() < 1750 ? werkstoff.get(ingot)
                                    : werkstoff.get(ingotHot))
                            .duration(Math.max(werkstoffStats.getMass() / 40L, 1L) * werkstoffStats.getMeltingPoint())
                            .eut(werkstoffStats.getMeltingVoltage())
                            .metadata(COIL_HEAT, werkstoffStats.getMeltingPoint())
                            .metadata(ADDITIVE_AMOUNT, 1000)
                            .addTo(BlastFurnaceWithGas);
                    } else {
                        GTValues.RA.stdBuilder()
                            .itemInputs(werkstoff.get(dust))
                            .circuit(1)
                            .itemOutputs(
                                werkstoffStats.getMeltingPoint() < 1750 ? werkstoff.get(ingot)
                                    : werkstoff.get(ingotHot))
                            .duration(Math.max(werkstoffStats.getMass() / 40L, 1L) * werkstoffStats.getMeltingPoint())
                            .eut(werkstoffStats.getMeltingVoltage())
                            .metadata(COIL_HEAT, werkstoffStats.getMeltingPoint())
                            .addTo(blastFurnaceRecipes);

                        if (werkstoffStats.getMeltingPoint() <= 1000) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(werkstoff.get(dust))
                                .itemOutputs(werkstoff.get(ingot))
                                .duration(
                                    (int) Math.max(werkstoffStats.getMass() / 40L, 1L)
                                        * werkstoffStats.getMeltingPoint())
                                .eut(0)
                                .metadata(ADDITIVE_AMOUNT, 9)
                                .addTo(primitiveBlastRecipes);
                        }
                    }
                }

            if (werkstoffStats.isBlastFurnace() && werkstoffStats.getMeltingPoint() > 1750
                && werkstoffStats.autoGenerateVacuumFreezerRecipes()) {
                GTValues.RA.stdBuilder()
                    .itemInputs(werkstoff.get(ingotHot))
                    .itemOutputs(werkstoff.get(ingot))
                    .duration((int) Math.max(werkstoffStats.getMass() * 3L, 1L))
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumFreezerRecipes);
            }

            if (werkstoff.hasItemType(ingot)) {

                GTValues.RA.stdBuilder()
                    .itemInputs(werkstoff.get(ingot))
                    .itemOutputs(werkstoff.get(dust))
                    .duration(20 * SECONDS)
                    .eut(2)
                    .addTo(maceratorRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(werkstoff.get(nugget))
                    .itemOutputs(werkstoff.get(dustTiny))
                    .duration(20 * SECONDS)
                    .eut(2)
                    .addTo(maceratorRecipes);

            }
        }
    }
}
