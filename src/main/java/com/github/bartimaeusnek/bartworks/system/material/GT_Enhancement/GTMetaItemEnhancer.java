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

package com.github.bartimaeusnek.bartworks.system.material.GT_Enhancement;

import static gregtech.api.enums.Mods.BartWorks;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.recipe.RecipeMaps.fluidCannerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.api.util.GT_RecipeBuilder.WILDCARD;

import java.util.List;
import java.util.stream.Collectors;

import codechicken.nei.api.API;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class GTMetaItemEnhancer {

    static List<Materials> NoMetaValue;

    private GTMetaItemEnhancer() {}

    public static void init() {
        if (!Forestry.isModLoaded()) {
            return;
        }
        NoMetaValue = Materials.getMaterialsMap()
            .values()
            .stream()
            .filter(m -> m.mMetaItemSubID == -1)
            .collect(Collectors.toList());
        Item moltenCapsuls = new BWGTMetaItems(OrePrefixes.capsuleMolten, null);
        Item capsuls = new BWGTMetaItems(OrePrefixes.capsule, NoMetaValue);
        // Item bottles = new BWGTMetaItems(OrePrefixes.bottle, NoMetaValue);

        Materials[] values = Materials.values();
        for (int i = 0, valuesLength = values.length; i < valuesLength; i++) {
            Materials m = values[i];
            if (m.mStandardMoltenFluid != null && GT_OreDictUnificator.get(OrePrefixes.cellMolten, m, 1) != null) {
                final FluidContainerRegistry.FluidContainerData emptyData = new FluidContainerRegistry.FluidContainerData(
                    m.getMolten(144),
                    new ItemStack(moltenCapsuls, 1, i),
                    GT_ModHandler.getModItem(Forestry.ID, "refractoryEmpty", 1));
                FluidContainerRegistry.registerFluidContainer(emptyData);
                GT_Utility.addFluidContainerData(emptyData);

                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_ModHandler.getModItem(Forestry.ID, "refractoryEmpty", 1))
                    .itemOutputs(new ItemStack(moltenCapsuls, 1, i))
                    .fluidInputs(m.getMolten(144))
                    .duration(2 * TICKS)
                    .eut(2)
                    .addTo(fluidCannerRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(new ItemStack(moltenCapsuls, 1, i))
                    .fluidOutputs(m.getMolten(144))
                    .duration(2 * TICKS)
                    .eut(2)
                    .addTo(fluidCannerRecipes);

            }
            if (m.getFluid(1) == null && m.getGas(1) == null || OreDictionary.doesOreNameExist("capsule" + m.mName))
                continue;
            addFluidData(m, GT_ModHandler.getModItem(Forestry.ID, "waxCapsule", 1), capsuls, 1000, i, true);
            // addFluidData(m, new ItemStack(Items.glass_bottle), bottles, 250, i, false);
        }
        for (int i = 0, valuesLength = NoMetaValue.size(); i < valuesLength; i++) {
            Materials m = NoMetaValue.get(i);
            if (m.getFluid(1) == null && m.getGas(1) == null || OreDictionary.doesOreNameExist("capsule" + m.mName))
                continue;
            addFluidData(m, GT_ModHandler.getModItem(Forestry.ID, "waxCapsule", 1), capsuls, 1000, i + 1001, true);
            // addFluidData(m, new ItemStack(Items.glass_bottle), bottles, 250, i + 1001, false);
        }

        API.hideItem(new ItemStack(capsuls, 1, WILDCARD));
        API.hideItem(new ItemStack(moltenCapsuls, 1, WILDCARD));
    }

    private static void addFluidData(Materials m, ItemStack container, Item filled, int amount, int it, boolean empty) {
        Fluid f = m.getFluid(1) != null ? m.getFluid(1)
            .getFluid()
            : m.getGas(1)
                .getFluid();
        final FluidContainerRegistry.FluidContainerData emptyData = new FluidContainerRegistry.FluidContainerData(
            new FluidStack(f, amount),
            new ItemStack(filled, 1, it),
            container);
        FluidContainerRegistry.registerFluidContainer(emptyData);
        GT_Utility.addFluidContainerData(emptyData);

        GT_Values.RA.stdBuilder()
            .itemInputs(container)
            .itemOutputs(new ItemStack(filled, 1, it))
            .fluidInputs(new FluidStack(f, amount))
            .duration(amount / 62)
            .eut(2)
            .addTo(fluidCannerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(filled, 1, it))
            .fluidOutputs(new FluidStack(f, amount))
            .duration(amount / 62)
            .eut(2)
            .addTo(fluidCannerRecipes);

    }

    public static void addAdditionalOreDictToForestry() {
        if (!Forestry.isModLoaded()) return;
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
        return GT_ModHandler.getModItem(Forestry.ID, itemName, 1);
    }
}
