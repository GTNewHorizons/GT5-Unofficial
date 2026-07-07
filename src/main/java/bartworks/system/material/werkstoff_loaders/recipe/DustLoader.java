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
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.electrolyzerRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.packagerRecipes;
import static gregtech.api.recipe.RecipeMaps.primitiveBlastRecipes;
import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.ADDITIVE_AMOUNT;
import static gregtech.api.util.GTRecipeConstants.BlastFurnaceWithGas;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gregtech.api.enums.TierEU;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;

import bartworks.system.material.Werkstoff;
import bartworks.system.material.WerkstoffLoader;
import bartworks.system.material.werkstoff_loaders.IWerkstoffRunnable;
import bartworks.util.BWUtil;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TextureSet;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;

public class DustLoader implements IWerkstoffRunnable {

    @Override
    @SuppressWarnings("unchecked")
    public void run(Werkstoff werkstoff) {
        if (werkstoff.hasItemType(dust)) {
            List<FluidStack> fluidComponents = new ArrayList<>();
            List<ItemStack> itemComponents = new ArrayList<>();
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
                                fluidComponents.add(tmpFl);
                                if (fluidComponents.size() > 1) {
                                    if (!tracker.containsKey(key)) {
                                        itemComponents.add(materialKey.getCells(value));
                                        tracker.put(key, Pair.of(value, itemComponents.size() - 1));
                                    } else {
                                        itemComponents.add(
                                            materialKey.getCells(
                                                tracker.get(key)
                                                    .getKey() + value));
                                        itemComponents.remove(
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
                                    fluidComponents.add(tmpFl);
                                    if (fluidComponents.size() > 1) {
                                        if (!tracker.containsKey(key)) {
                                            itemComponents.add(materialKey.getCells(value));
                                            tracker.put(key, Pair.of(value, itemComponents.size() - 1));
                                        } else {
                                            itemComponents.add(
                                                materialKey.getCells(
                                                    tracker.get(key)
                                                        .getKey() + value));
                                            itemComponents.remove(
                                                tracker.get(key)
                                                    .getValue() + 1);
                                        }
                                        cells += value;
                                    }
                                }
                                if (!tracker.containsKey(key)) {
                                    itemComponents.add(materialKey.getDust(value));
                                    tracker.put(key, Pair.of(value, itemComponents.size() - 1));
                                } else {
                                    itemComponents.add(
                                        materialKey.getDust(
                                            tracker.get(key)
                                                .getKey() + value));
                                    itemComponents.remove(
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
                                fluidComponents.add(tmpFl);
                                if (fluidComponents.size() > 1) {
                                    if (!tracker.containsKey(key)) {
                                        itemComponents.add(werkstoffKey.get(cell, value));
                                        tracker.put(key, Pair.of(value, itemComponents.size() - 1));
                                    } else {
                                        itemComponents.add(
                                            werkstoffKey.get(
                                                cell,
                                                tracker.get(key)
                                                    .getKey() + value));
                                        itemComponents.remove(
                                            tracker.get(key)
                                                .getValue() + 1);
                                    }
                                    cells += value;
                                }
                            } else {
                                if (!werkstoffKey.hasItemType(dust)) continue;
                                if (!tracker.containsKey(key)) {
                                    itemComponents.add(werkstoffKey.get(dust, value));
                                    tracker.put(key, Pair.of(value, itemComponents.size() - 1));
                                } else {
                                    itemComponents.add(
                                        werkstoffKey.get(
                                            dust,
                                            tracker.get(key)
                                                .getKey() + value));
                                    itemComponents.remove(
                                        tracker.get(key)
                                            .getValue() + 1);
                                }
                            }
                        }
                    }
                    ItemStack werkstoffDust = werkstoff.get(dust);
                    werkstoffDust.stackSize = werkstoff.getContents()
                        .getKey();
                    if (werkstoffStats.isElektrolysis()) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(
                                cells > 0 ? new ItemStack[] { werkstoffDust, Materials.Empty.getCells(cells) }
                                    : new ItemStack[] { werkstoffDust })
                            .itemOutputs(itemComponents.toArray(new ItemStack[0]))
                            .fluidOutputs(
                                fluidComponents.isEmpty() ? new FluidStack[0]
                                    : new FluidStack[] { fluidComponents.get(0) })
                            .duration(
                                (int) Math.max(
                                    1L,
                                    Math.abs(
                                        werkstoffStats.getProtons() / werkstoff.getContents()
                                            .getValue()
                                            .size())))
                            .eut(
                                BWUtil.calculateRecipeEU(
                                    werkstoff,
                                    Math.min(
                                        4,
                                        werkstoff.getContents()
                                            .getValue()
                                            .size())
                                        * 30))
                            .addTo(electrolyzerRecipes);
                    }
                    if (werkstoffStats.isCentrifuge()) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(
                                cells > 0 ? new ItemStack[] { werkstoffDust, Materials.Empty.getCells(cells) }
                                    : new ItemStack[] { werkstoffDust })
                            .itemOutputs(itemComponents.toArray(new ItemStack[0]))
                            .fluidOutputs(
                                fluidComponents.isEmpty() ? new FluidStack[0]
                                    : new FluidStack[] { fluidComponents.get(0) })
                            .duration(
                                (int) Math.max(
                                    1L,
                                    Math.abs(
                                        werkstoffStats.getMass() / werkstoff.getContents()
                                            .getValue()
                                            .size())))
                            .eut(
                                BWUtil.calculateRecipeEU(
                                    werkstoff,
                                    Math.min(
                                        4,
                                        werkstoff.getContents()
                                            .getValue()
                                            .size())
                                        * 5))
                            .addTo(centrifugeRecipes);
                    }
                    if (werkstoff.getGenerationFeatures()
                        .hasChemicalRecipes()) {
                        if (cells > 0) itemComponents.add(Materials.Empty.getCells(cells));
                        GTValues.RA.stdBuilder()
                            .itemInputs(itemComponents.toArray(new ItemStack[0]))
                            .itemOutputs(werkstoffDust)
                            .fluidInputs(fluidComponents.toArray(new FluidStack[0]))
                            .duration(
                                (int) Math.max(
                                    1L,
                                    Math.abs(
                                        werkstoffStats.getProtons() / werkstoff.getContents()
                                            .getValue()
                                            .size())))
                            .eut(
                                BWUtil.calculateRecipeEU(
                                    werkstoff,
                                    Math.min(
                                        4,
                                        werkstoff.getContents()
                                            .getValue()
                                            .size())
                                        * 30))
                            .addTo(GTRecipeConstants.UniversalChemical);
                    }
                    if (werkstoff.getGenerationFeatures()
                        .hasMixerRecipes()) {
                        if (cells > 0) itemComponents.add(Materials.Empty.getCells(cells));
                        short circuitID = werkstoff.getMixCircuit();
                        ItemStack circuit = circuitID == -1 ? null : GTUtility.getIntegratedCircuit(circuitID);
                        if (circuit != null) itemComponents.add(circuit);
                        GTValues.RA.stdBuilder()
                            .itemInputs(itemComponents.toArray(new ItemStack[0]))
                            .itemOutputs(werkstoffDust)
                            .fluidInputs(
                                fluidComponents.isEmpty() ? new FluidStack[0]
                                    : new FluidStack[] { fluidComponents.get(0) })
                            .duration(
                                (int) Math.max(
                                    1L,
                                    Math.abs(
                                        werkstoffStats.getMass() / werkstoff.getContents()
                                            .getValue()
                                            .size())))
                            .eut(
                                BWUtil.calculateRecipeEU(
                                    werkstoff,
                                    Math.min(
                                        4,
                                        werkstoff.getContents()
                                            .getValue()
                                            .size())
                                        * 5))
                            .addTo(mixerRecipes);
                    }
                } else {
                    GTLog.err.println(
                        "Autogenerated recipe(s) using Werkstoff material '" + werkstoff.getDefaultName()
                            + "' (dust) removed due to no contents in material definition.");
                }
            }
            if (BWUtil.calculateRecipeEU(werkstoff, 16) > TierEU.RECIPE_IV) {
                GTModHandler.addCraftingRecipe(
                    werkstoff.get(dust),
                    GTModHandler.RecipeBits.BUFFERED,
                    new Object[]{"TTT", "TTT", "TTT", 'T', werkstoff.get(dustTiny)});
                GTModHandler.addCraftingRecipe(
                    werkstoff.get(dust),
                    GTModHandler.RecipeBits.BUFFERED,
                    new Object[]{"TT ", "TT ", 'T', WerkstoffLoader.getCorrespondingItemStack(dustSmall, werkstoff)});
                GTModHandler.addCraftingRecipe(
                    WerkstoffLoader.getCorrespondingItemStack(dustSmall, werkstoff, 4),
                    GTModHandler.RecipeBits.BUFFERED,
                    new Object[]{" T ", 'T', werkstoff.get(dust)});
                GTModHandler.addCraftingRecipe(
                    WerkstoffLoader.getCorrespondingItemStack(dustTiny, werkstoff, 9),
                    GTModHandler.RecipeBits.BUFFERED,
                    new Object[]{"T  ", 'T', werkstoff.get(dust)});
            }

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(dustTiny, 9), ItemList.Schematic_Dust.get(0L))
                .itemOutputs(werkstoff.get(dust))
                .duration(5 * SECONDS)
                .eut(BWUtil.calculateRecipeEU(werkstoff, 4))
                .addTo(packagerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(dustSmall, 4), ItemList.Schematic_Dust.get(0L))
                .itemOutputs(werkstoff.get(dust))
                .duration(5 * SECONDS)
                .eut(BWUtil.calculateRecipeEU(werkstoff, 4))
                .addTo(packagerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(dustTiny, 9), ItemList.Schematic_3by3.get(0L))
                .itemOutputs(werkstoff.get(dust))
                .duration(5 * SECONDS)
                .eut(BWUtil.calculateRecipeEU(werkstoff, 4))
                .addTo(packagerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(dustSmall, 4), ItemList.Schematic_2by2.get(0L))
                .itemOutputs(werkstoff.get(dust))
                .duration(5 * SECONDS)
                .eut(BWUtil.calculateRecipeEU(werkstoff, 4))
                .addTo(packagerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(dust, 1), ItemList.Schematic_Dust_Small.get(0L))
                .itemOutputs(werkstoff.get(dustSmall, 4))
                .duration(5 * SECONDS)
                .eut(BWUtil.calculateRecipeEU(werkstoff, 4))
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
                    .eut(BWUtil.calculateRecipeEU(werkstoff, 128))
                    .addTo(vacuumFreezerRecipes);
            }

            if (werkstoff.hasItemType(ingot)) {

                GTValues.RA.stdBuilder()
                    .itemInputs(werkstoff.get(ingot))
                    .itemOutputs(werkstoff.get(dust))
                    .duration(5 * SECONDS)
                    .eut(BWUtil.calculateRecipeEU(werkstoff, 2))
                    .addTo(maceratorRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(werkstoff.get(nugget))
                    .itemOutputs(werkstoff.get(dustTiny))
                    .duration(10 * TICKS)
                    .eut(BWUtil.calculateRecipeEU(werkstoff, 2))
                    .addTo(maceratorRecipes);

            }
        }
    }
}
