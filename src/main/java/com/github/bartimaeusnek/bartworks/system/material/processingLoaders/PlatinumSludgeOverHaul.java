/*
 * Copyright (c) 2019 bartimaeusnek
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

package com.github.bartimaeusnek.bartworks.system.material.processingLoaders;

import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;

import com.github.bartimaeusnek.bartworks.util.BW_Util;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.objects.MaterialStack;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fluids.FluidStack;

import java.util.Map;

import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.*;
import static gregtech.api.enums.OrePrefixes.*;

public class PlatinumSludgeOverHaul {
    private PlatinumSludgeOverHaul(){}

    public static void runHelperrecipes(){
        //FormicAcid
        GT_Values.RA.addChemicalRecipe(Sodiumformate.get(cell,2), GT_Utility.getIntegratedCircuit(21), Materials.SulfuricAcid.getFluid(1000),null,FormicAcid.get(cell,2),Sodiumsulfate.get(dust),15);
        GT_Values.RA.addChemicalRecipe(Materials.SulfuricAcid.getCells(2), GT_Utility.getIntegratedCircuit(21), Sodiumformate.getFluidOrGas(1000),FormicAcid.getFluidOrGas(2000),Materials.Empty.getCells(2),Sodiumsulfate.get(dust),15);
        //AquaRegia
        GT_Values.RA.addMixerRecipe(Materials.DilutedSulfuricAcid.getCells(1),Materials.NitricAcid.getCells(1),GT_Utility.getIntegratedCircuit(1),null,null,null, AquaRegia.get(cell,2),30,30);
        //AmmoniumCloride
        GT_Values.RA.addChemicalRecipe(Materials.Ammonia.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.HydrochloricAcid.getFluid(1000),null,AmmoniumCloride.get(cell,1),null,15);
        GT_Values.RA.addChemicalRecipe(Materials.HydrochloricAcid.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.Ammonia.getGas(1000),AmmoniumCloride.getFluidOrGas(1000),Materials.Empty.getCells(1),null,15);

        for (Werkstoff w :Werkstoff.werkstoffHashMap.values())
            if (w.containsStuff(Materials.Sulfur)&&(w.containsStuff(Materials.Copper)||w.containsStuff(Materials.Nickel))) {
                GT_Values.RA.addChemicalRecipe(w.get(crushedPurified), GT_Utility.getIntegratedCircuit(11), AquaRegia.getFluidOrGas(150), PTConcentrate.getFluidOrGas(150), null, 250);
                GT_Values.RA.addChemicalRecipe(w.get(crushedPurified), PTMetallicPowder.get(dust), AquaRegia.getFluidOrGas(1150), PTConcentrate.getFluidOrGas(1150), PTResidue.get(dust), 250);
            }
        for (Materials m : Materials.values())
            if (materialsContains(m,Materials.Sulfur)&&(materialsContains(m,Materials.Copper)||materialsContains(m,Materials.Nickel))){
                GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(crushedPurified,m,1), GT_Utility.getIntegratedCircuit(11), AquaRegia.getFluidOrGas(150), PTConcentrate.getFluidOrGas(150), null, 250);
                GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(crushedPurified,m,1), PTMetallicPowder.get(dust), AquaRegia.getFluidOrGas(1150), PTConcentrate.getFluidOrGas(1150), PTResidue.get(dust), 250);
            }
        GT_Values.RA.addChemicalRecipe(PTMetallicPowder.get(dust), GT_Utility.getIntegratedCircuit(11), AquaRegia.getFluidOrGas(1000), PTConcentrate.getFluidOrGas(1000), PTResidue.get(dust), 250);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{},new FluidStack[]{PTConcentrate.getFluidOrGas(1000),AmmoniumCloride.getFluidOrGas(1000)},new FluidStack[]{PDAmmonia.getFluidOrGas(1000)},new ItemStack[]{PTSaltCrude.get(dustTiny,8),PTRawPowder.get(dustTiny)},600,30);
        GT_Values.RA.addSifterRecipe(PTSaltCrude.get(dust),new ItemStack[]{
                PTSaltRefined.get(dustTiny),
                PTSaltRefined.get(dustTiny),
                PTSaltRefined.get(dustTiny),
                PTSaltRefined.get(dustTiny),
                PTSaltRefined.get(dustTiny),
                PTSaltRefined.get(dustTiny),
                PTSaltRefined.get(dustTiny),
                PTSaltRefined.get(dustTiny),
                PTSaltRefined.get(dustTiny),
        },new int[]{
                1000,1000,1000,1000,1000,1000,1000,1000,500,
        },600,30);
        GT_Values.RA.addBlastRecipe(PTSaltRefined.get(dust),null,null,null,PTMetallicPowder.get(dust),null,200,120,900);
        GT_Values.RA.addChemicalRecipe(PTRawPowder.get(dust,2),Materials.Calcium.getDust(1),null,null,Materials.Platinum.getDust(2),CalciumChloride.get(dust),30);
        GT_Values.RA.addChemicalRecipe(PDMetallicPowder.get(dust), GT_Utility.getIntegratedCircuit(11), PDAmmonia.getFluidOrGas(1000), null,PDSalt.get(dustTiny,16),PDRawPowder.get(dustTiny,2), 250);
        GT_Values.RA.addChemicalRecipe(GT_Utility.getIntegratedCircuit(10),null,PDAmmonia.getFluidOrGas(1000), null, PDSalt.get(dustTiny,9), 250);
        GT_Values.RA.addSifterRecipe(PDSalt.get(dust),new ItemStack[]{
                PDMetallicPowder.get(dustTiny),
                PDMetallicPowder.get(dustTiny),
                PDMetallicPowder.get(dustTiny),
                PDMetallicPowder.get(dustTiny),
                PDMetallicPowder.get(dustTiny),
                PDMetallicPowder.get(dustTiny),
                PDMetallicPowder.get(dustTiny),
                PDMetallicPowder.get(dustTiny),
                PDMetallicPowder.get(dustTiny),
        },new int[]{
                1000,1000,1000,1000,1000,1000,1000,1000,500,
        },600,30);
        GT_Values.RA.addChemicalRecipe(PDRawPowder.get(dust,2), GT_Utility.getIntegratedCircuit(11), FormicAcid.getFluidOrGas(4000), Materials.Ammonium.getGas(2000),Materials.Palladium.getDust(2),Materials.Ethylene.getDust(1), 250);


    }


    private static boolean materialsContains(Materials one, ISubTagContainer other){
        if (one == null || one.mMaterialList == null || one.mMaterialList.isEmpty())
            return false;
        for (MaterialStack stack : one.mMaterialList)
            if (stack.mMaterial.equals(other))
                return true;
        return false;
    }


    public static void replacePureElements(){
        for (Object entry : FurnaceRecipes.smelting().getSmeltingList().entrySet()){
            Map.Entry realEntry = (Map.Entry) entry;
            if (GT_Utility.isStackValid(realEntry.getKey()) && BW_Util.checkStackAndPrefix((ItemStack) realEntry.getKey()))
                if (!GT_OreDictUnificator.getAssociation((ItemStack) realEntry.getKey()).mPrefix.equals(dust) && !GT_OreDictUnificator.getAssociation((ItemStack) realEntry.getKey()).mPrefix.equals(dustTiny))
                    if (GT_Utility.isStackValid(realEntry.getValue()) && BW_Util.checkStackAndPrefix((ItemStack) realEntry.getValue()))
                        if (GT_OreDictUnificator.getAssociation((ItemStack) realEntry.getValue()).mMaterial.mMaterial.equals(Materials.Platinum))
                            realEntry.setValue(PTMetallicPowder.get(dust));
                        else if (GT_OreDictUnificator.getAssociation((ItemStack) realEntry.getValue()).mMaterial.mMaterial.equals(Materials.Palladium))
                            realEntry.setValue(PDMetallicPowder.get(dust));
        }

        maploop: for (GT_Recipe.GT_Recipe_Map map : GT_Recipe.GT_Recipe_Map.sMappings) {
            for (GT_Recipe recipe : map.mRecipeList) {
                if (recipe.mFakeRecipe)
                    continue maploop;
                for (int i = 0; i < recipe.mOutputs.length; i++) {
                    if (!BW_Util.checkStackAndPrefix(recipe.mOutputs[i]))
                        continue;
                    if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mMaterial.mMaterial.equals(Materials.Platinum)) {
                        if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(crushed) || GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(crushedCentrifuged) || GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(crushedPurified)) {
                            int amount = recipe.mOutputs[i].stackSize;
                            recipe.mOutputs[i] = PTMetallicPowder.get(dustTiny).splitStack(amount * 11);
                        } else if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dust) || GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dustImpure) || GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dustPure)) {
                            int amount = recipe.mOutputs[i].stackSize;
                            recipe.mOutputs[i] = PTMetallicPowder.get(dust).splitStack(amount);
                        } else if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dustSmall)) {
                            int amount = recipe.mOutputs[i].stackSize;
                            recipe.mOutputs[i] = PTMetallicPowder.get(dustSmall).splitStack(amount);
                        } else if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dustTiny)) {
                            int amount = recipe.mOutputs[i].stackSize;
                            recipe.mOutputs[i] = PTMetallicPowder.get(dustTiny).splitStack(amount);
                        }
                    } else if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mMaterial.mMaterial.equals(Materials.Palladium)) {
                        if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(crushed) || GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(crushedCentrifuged) || GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(crushedPurified)) {
                            int amount = recipe.mOutputs[i].stackSize;
                            recipe.mOutputs[i] = PDMetallicPowder.get(dustTiny).splitStack(amount * 11);
                        } else if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dust) || GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dustImpure) || GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dustPure)) {
                            int amount = recipe.mOutputs[i].stackSize;
                            recipe.mOutputs[i] = PDMetallicPowder.get(dust).splitStack(amount);
                        } else if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dustSmall)) {
                            int amount = recipe.mOutputs[i].stackSize;
                            recipe.mOutputs[i] = PDMetallicPowder.get(dustSmall).splitStack(amount);
                        } else if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dustTiny)) {
                            int amount = recipe.mOutputs[i].stackSize;
                            recipe.mOutputs[i] = PDMetallicPowder.get(dustTiny).splitStack(amount);
                        }
                    }
                }
            }
        }
        PlatinumSludgeOverHaul.runHelperrecipes();
    }
    
}