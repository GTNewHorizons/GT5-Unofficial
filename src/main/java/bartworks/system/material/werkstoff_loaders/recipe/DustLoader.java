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

import static gregtech.api.enums.OrePrefixes.block;
import static gregtech.api.enums.OrePrefixes.cell;
import static gregtech.api.enums.OrePrefixes.dust;
import static gregtech.api.enums.OrePrefixes.dustSmall;
import static gregtech.api.enums.OrePrefixes.dustTiny;
import static gregtech.api.enums.OrePrefixes.gem;
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
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import bartworks.system.material.Werkstoff;
import bartworks.system.material.WerkstoffLoader;
import bartworks.system.material.werkstoff_loaders.IWerkstoffRunnable;
import bartworks.util.Pair;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TextureSet;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.recipe.RecipeMaps;
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
            int cells = 0;

            if (werkstoff.getGenerationFeatures()
                .hasMixerRecipes()
                || werkstoff.getStats()
                    .isElektrolysis()
                || werkstoff.getStats()
                    .isCentrifuge()
                || werkstoff.getGenerationFeatures()
                    .hasChemicalRecipes()) {
                for (Pair<ISubTagContainer, Integer> container : werkstoff.getContents()
                    .getValue()
                    .toArray(new Pair[0])) {
                    final ISubTagContainer key = container.getKey();
                    final int value = container.getValue();
                    if (key instanceof Materials materialKey) {
                        if ((materialKey.getGas(0) != null || materialKey.getFluid(0) != null
                            || materialKey.mIconSet == TextureSet.SET_FLUID) && materialKey.getDust(0) == null) {
                            FluidStack tmpFl = materialKey.getGas(1000L * value);
                            if (tmpFl == null || tmpFl.getFluid() == null) {
                                tmpFl = materialKey.getFluid(1000L * value);
                            }
                            flOutputs.add(tmpFl);
                            if (flOutputs.size() > 1) {
                                if (!tracker.containsKey(key)) {
                                    stOutputs.add(materialKey.getCells(value));
                                    tracker.put(key, new Pair<>(value, stOutputs.size() - 1));
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
                                    || materialKey.getMolten(0) == null && materialKey.getSolid(0) == null) continue;
                                FluidStack tmpFl = materialKey.getMolten(1000L * value);
                                if (tmpFl == null || tmpFl.getFluid() == null) {
                                    tmpFl = materialKey.getSolid(1000L * value);
                                }
                                flOutputs.add(tmpFl);
                                if (flOutputs.size() > 1) {
                                    if (!tracker.containsKey(key)) {
                                        stOutputs.add(materialKey.getCells(value));
                                        tracker.put(key, new Pair<>(value, stOutputs.size() - 1));
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
                                tracker.put(key, new Pair<>(value, stOutputs.size() - 1));
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
                                    tracker.put(key, new Pair<>(value, stOutputs.size() - 1));
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
                                tracker.put(key, new Pair<>(value, stOutputs.size() - 1));
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
                if (werkstoff.getStats()
                    .isElektrolysis()) {
                    GTRecipe tRecipe = new GTRecipe(
                        true,
                        new ItemStack[] { input, cells > 0 ? Materials.Empty.getCells(cells) : null },
                        stOutputs.toArray(new ItemStack[0]),
                        null,
                        null,
                        new FluidStack[] { null },
                        new FluidStack[] { flOutputs.size() > 0 ? flOutputs.get(0) : null },
                        (int) Math.max(
                            1L,
                            Math.abs(
                                werkstoff.getStats()
                                    .getProtons()
                                    / werkstoff.getContents()
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
                    RecipeMaps.electrolyzerNonCellRecipes.add(tRecipe);
                }
                if (werkstoff.getStats()
                    .isCentrifuge()) {
                    RecipeMaps.centrifugeRecipes.add(
                        new GTRecipe(
                            true,
                            new ItemStack[] { input, cells > 0 ? Materials.Empty.getCells(cells) : null },
                            stOutputs.toArray(new ItemStack[0]),
                            null,
                            null,
                            new FluidStack[] { null },
                            new FluidStack[] { flOutputs.size() > 0 ? flOutputs.get(0) : null },
                            (int) Math.max(
                                1L,
                                Math.abs(
                                    werkstoff.getStats()
                                        .getMass()
                                        / werkstoff.getContents()
                                            .getValue()
                                            .size())),
                            Math.min(
                                4,
                                werkstoff.getContents()
                                    .getValue()
                                    .size())
                                * 5,
                            0));
                    GTRecipe tRecipe = new GTRecipe(
                        false,
                        stOutputs.toArray(new ItemStack[0]),
                        new ItemStack[] { input },
                        null,
                        null,
                        new FluidStack[] { flOutputs.size() > 0 ? flOutputs.get(0) : null },
                        null,
                        (int) Math.max(
                            1L,
                            Math.abs(
                                werkstoff.getStats()
                                    .getProtons()
                                    / werkstoff.getContents()
                                        .getValue()
                                        .size())),
                        Math.min(
                            4,
                            werkstoff.getContents()
                                .getValue()
                                .size())
                            * 30,
                        0);
                    RecipeMaps.centrifugeNonCellRecipes.add(tRecipe);
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
                                    werkstoff.getStats()
                                        .getProtons()
                                        / werkstoff.getContents()
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
                            true,
                            stOutputs.toArray(new ItemStack[0]),
                            new ItemStack[] { input },
                            null,
                            null,
                            new FluidStack[] { flOutputs.size() > 0 ? flOutputs.get(0) : null },
                            null,
                            (int) Math.max(
                                1L,
                                Math.abs(
                                    werkstoff.getStats()
                                        .getMass()
                                        / werkstoff.getContents()
                                            .getValue()
                                            .size())),
                            Math.min(
                                4,
                                werkstoff.getContents()
                                    .getValue()
                                    .size())
                                * 5,
                            0));
                    GTRecipe tRecipe = new GTRecipe(
                        false,
                        stOutputs.toArray(new ItemStack[0]),
                        new ItemStack[] { input },
                        null,
                        null,
                        new FluidStack[] { flOutputs.size() > 0 ? flOutputs.get(0) : null },
                        null,
                        (int) Math.max(
                            1L,
                            Math.abs(
                                werkstoff.getStats()
                                    .getProtons()
                                    / werkstoff.getContents()
                                        .getValue()
                                        .size())),
                        Math.min(
                            4,
                            werkstoff.getContents()
                                .getValue()
                                .size())
                            * 30,
                        0);
                    RecipeMaps.mixerNonCellRecipes.add(tRecipe);
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

            if (werkstoff.hasItemType(ingot) && !werkstoff.getStats()
                .isBlastFurnace()) {
                GTModHandler.addSmeltingRecipe(werkstoff.get(dust), werkstoff.get(ingot));
                GTModHandler.addSmeltingRecipe(werkstoff.get(dustTiny), werkstoff.get(nugget));
            } else if (werkstoff.hasItemType(ingot) && werkstoff.getStats()
                .isBlastFurnace()
                && werkstoff.getStats()
                    .getMeltingPoint() != 0) {
                        if (werkstoff.contains(WerkstoffLoader.ANAEROBE_SMELTING)) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(werkstoff.get(dust), GTUtility.getIntegratedCircuit(11))
                                .itemOutputs(
                                    werkstoff.getStats()
                                        .getMeltingPoint() < 1750 ? werkstoff.get(ingot) : werkstoff.get(ingotHot))
                                .fluidInputs(Materials.Nitrogen.getGas(1000))
                                .duration(
                                    Math.max(
                                        werkstoff.getStats()
                                            .getMass() / 40L,
                                        1L)
                                        * werkstoff.getStats()
                                            .getMeltingPoint())
                                .eut(
                                    werkstoff.getStats()
                                        .getMeltingVoltage())
                                .metadata(
                                    COIL_HEAT,
                                    werkstoff.getStats()
                                        .getMeltingPoint())
                                .addTo(blastFurnaceRecipes);

                        } else if (werkstoff.contains(WerkstoffLoader.NOBLE_GAS_SMELTING)) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(werkstoff.get(dust), GTUtility.getIntegratedCircuit(11))
                                .itemOutputs(
                                    werkstoff.getStats()
                                        .getMeltingPoint() < 1750 ? werkstoff.get(ingot) : werkstoff.get(ingotHot))
                                .fluidInputs(Materials.Argon.getGas(1000))
                                .duration(
                                    Math.max(
                                        werkstoff.getStats()
                                            .getMass() / 40L,
                                        1L)
                                        * werkstoff.getStats()
                                            .getMeltingPoint())
                                .eut(
                                    werkstoff.getStats()
                                        .getMeltingVoltage())
                                .metadata(
                                    COIL_HEAT,
                                    werkstoff.getStats()
                                        .getMeltingPoint())
                                .addTo(blastFurnaceRecipes);

                        } else {
                            GTValues.RA.stdBuilder()
                                .itemInputs(werkstoff.get(dust), GTUtility.getIntegratedCircuit(1))
                                .itemOutputs(
                                    werkstoff.getStats()
                                        .getMeltingPoint() < 1750 ? werkstoff.get(ingot) : werkstoff.get(ingotHot))
                                .duration(
                                    Math.max(
                                        werkstoff.getStats()
                                            .getMass() / 40L,
                                        1L)
                                        * werkstoff.getStats()
                                            .getMeltingPoint())
                                .eut(
                                    werkstoff.getStats()
                                        .getMeltingVoltage())
                                .metadata(
                                    COIL_HEAT,
                                    werkstoff.getStats()
                                        .getMeltingPoint())
                                .addTo(blastFurnaceRecipes);

                            if (werkstoff.getStats()
                                .getMeltingPoint() <= 1000) {
                                GTValues.RA.stdBuilder()
                                    .itemInputs(werkstoff.get(dust))
                                    .itemOutputs(werkstoff.get(ingot))
                                    .duration(
                                        (int) Math.max(
                                            werkstoff.getStats()
                                                .getMass() / 40L,
                                            1L) * werkstoff.getStats()
                                                .getMeltingPoint())
                                    .eut(0)
                                    .metadata(ADDITIVE_AMOUNT, 9)
                                    .addTo(primitiveBlastRecipes);
                            }
                        }
                    }

            if (werkstoff.getStats()
                .isBlastFurnace()
                && werkstoff.getStats()
                    .getMeltingPoint() > 1750) {
                GTValues.RA.stdBuilder()
                    .itemInputs(werkstoff.get(ingotHot))
                    .itemOutputs(werkstoff.get(ingot))
                    .duration(
                        (int) Math.max(
                            werkstoff.getStats()
                                .getMass() * 3L,
                            1L))
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
            if (werkstoff.hasItemType(ingot) || werkstoff.hasItemType(gem)) {

                GTValues.RA.stdBuilder()
                    .itemInputs(werkstoff.get(block))
                    .itemOutputs(werkstoff.get(dust, 9))
                    .duration(20 * SECONDS)
                    .eut(2)
                    .addTo(maceratorRecipes);

            }
        }
    }
}
