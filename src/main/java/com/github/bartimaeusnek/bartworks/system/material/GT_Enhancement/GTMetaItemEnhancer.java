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

package com.github.bartimaeusnek.bartworks.system.material.GT_Enhancement;

import com.github.bartimaeusnek.bartworks.API.LoaderReference;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class GTMetaItemEnhancer {
    static List<Materials> NoMetaValue;

    private GTMetaItemEnhancer() {}

    public static void init() {
        if (!WerkstoffLoader.gtnhGT) {
            Item moltenCell = new BWGTMetaItems(WerkstoffLoader.cellMolten, null);
            Materials[] values = Materials.values();
            for (int i = 0, valuesLength = values.length; i < valuesLength; i++) {
                Materials m = values[i];
                if (m == null || m.mStandardMoltenFluid == null) continue;
                final FluidContainerRegistry.FluidContainerData emptyData =
                        new FluidContainerRegistry.FluidContainerData(
                                m.getMolten(144),
                                new ItemStack(moltenCell, 1, i),
                                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1L));
                FluidContainerRegistry.registerFluidContainer(emptyData);
                GT_Utility.addFluidContainerData(emptyData);
                GT_Values.RA.addFluidCannerRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1L),
                        new ItemStack(moltenCell, 1, i),
                        m.getMolten(144),
                        GT_Values.NF);
                GT_Values.RA.addFluidCannerRecipe(
                        new ItemStack(moltenCell, 1, i),
                        GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1L),
                        GT_Values.NF,
                        m.getMolten(144));
            }
        }
        if (!LoaderReference.Forestry) {
            return;
        }
        NoMetaValue = Materials.getMaterialsMap().values().stream()
                .filter(m -> m.mMetaItemSubID == -1)
                .collect(Collectors.toList());
        Item moltenCapsuls = new BWGTMetaItems(WerkstoffLoader.capsuleMolten, null);
        Item capsuls = new BWGTMetaItems(OrePrefixes.capsule, NoMetaValue);
        // Item bottles = new BWGTMetaItems(OrePrefixes.bottle, NoMetaValue);

        Materials[] values = Materials.values();
        for (int i = 0, valuesLength = values.length; i < valuesLength; i++) {
            Materials m = values[i];
            if (m.mStandardMoltenFluid != null && GT_OreDictUnificator.get(WerkstoffLoader.cellMolten, m, 1) != null) {
                final FluidContainerRegistry.FluidContainerData emptyData =
                        new FluidContainerRegistry.FluidContainerData(
                                m.getMolten(144),
                                new ItemStack(moltenCapsuls, 1, i),
                                GT_ModHandler.getModItem("Forestry", "refractoryEmpty", 1));
                FluidContainerRegistry.registerFluidContainer(emptyData);
                GT_Utility.addFluidContainerData(emptyData);
                GT_Values.RA.addFluidCannerRecipe(
                        GT_ModHandler.getModItem("Forestry", "refractoryEmpty", 1),
                        new ItemStack(moltenCapsuls, 1, i),
                        m.getMolten(144),
                        GT_Values.NF);
                GT_Values.RA.addFluidCannerRecipe(
                        new ItemStack(moltenCapsuls, 1, i), GT_Values.NI, GT_Values.NF, m.getMolten(144));
            }
            if (m.getFluid(1) == null && m.getGas(1) == null) continue;
            if (OreDictionary.doesOreNameExist("capsule" + m.mName)) continue;
            addFluidData(m, GT_ModHandler.getModItem("Forestry", "waxCapsule", 1), capsuls, 1000, i, true);
            //   addFluidData(m, new ItemStack(Items.glass_bottle), bottles, 250, i, false);
        }
        for (int i = 0, valuesLength = NoMetaValue.size(); i < valuesLength; i++) {
            Materials m = NoMetaValue.get(i);
            if (m.getFluid(1) == null && m.getGas(1) == null) continue;
            if (OreDictionary.doesOreNameExist("capsule" + m.mName)) continue;
            addFluidData(m, GT_ModHandler.getModItem("Forestry", "waxCapsule", 1), capsuls, 1000, i + 1001, true);
            //   addFluidData(m, new ItemStack(Items.glass_bottle), bottles, 250, i + 1001, false);
        }
    }

    private static void addFluidData(Materials m, ItemStack container, Item filled, int amount, int it, boolean empty) {
        Fluid f = m.getFluid(1) != null ? m.getFluid(1).getFluid() : m.getGas(1).getFluid();
        final FluidContainerRegistry.FluidContainerData emptyData = new FluidContainerRegistry.FluidContainerData(
                new FluidStack(f, amount), new ItemStack(filled, 1, it), container);
        FluidContainerRegistry.registerFluidContainer(emptyData);
        GT_Utility.addFluidContainerData(emptyData);
        GT_Values.RA.addFluidCannerRecipe(
                container, new ItemStack(filled, 1, it), new FluidStack(f, amount), GT_Values.NF);
        GT_Values.RA.addFluidCannerRecipe(
                new ItemStack(filled, 1, it),
                empty ? GT_Values.NI : container,
                GT_Values.NF,
                new FluidStack(f, amount));
    }

    public static void addAdditionalOreDictToForestry() {
        if (!LoaderReference.Forestry) return;
        OreDictionary.registerOre("capsuleWater", getForestryItem("waxCapsuleWater"));
        OreDictionary.registerOre("capsuleIce", getForestryItem("waxCapsuleIce"));
        OreDictionary.registerOre("capsuleHoney", getForestryItem("waxCapsuleHoney"));
        OreDictionary.registerOre("capsuleJuice", getForestryItem("waxCapsuleJuice"));
        OreDictionary.registerOre("capsuleSeedOil", getForestryItem("waxCapsuleSeedOil"));
        OreDictionary.registerOre("capsuleEthanol", getForestryItem("waxCapsuleEthanol"));
        OreDictionary.registerOre("capsuleBiomass", getForestryItem("waxCapsuleBiomass"));
        OreDictionary.registerOre("capsuleShortMead", getForestryItem("waxCapsuleShortMead"));
        OreDictionary.registerOre("capsuleMead", getForestryItem("waxCapsuleMead"));
        OreDictionary.registerOre("capsuleFuel", getForestryItem("waxCapsuleFuel"));
        OreDictionary.registerOre("capsuleOil", getForestryItem("waxCapsuleOil"));
        OreDictionary.registerOre("capsuleLava", getForestryItem("refractoryLava"));
    }

    private static ItemStack getForestryItem(String itemName) {
        return GT_ModHandler.getModItem("Forestry", itemName, 1);
    }
}
