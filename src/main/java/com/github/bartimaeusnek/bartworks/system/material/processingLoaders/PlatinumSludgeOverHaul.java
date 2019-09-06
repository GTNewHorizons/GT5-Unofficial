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
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.objects.MaterialStack;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashSet;
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

        //base sollution
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
        //Pt
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
        //Pd
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
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(PDRawPowder.get(dust,2), Materials.Empty.getCells(1), FormicAcid.getFluidOrGas(4000), Materials.Ammonia.getGas(2000),Materials.Palladium.getDust(2),Materials.Ethylene.getCells(1), 250,30);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{PDRawPowder.get(dust,2)},new FluidStack[]{FormicAcid.getFluidOrGas(4000)},new FluidStack[]{Materials.Ammonia.getGas(2000),Materials.Ethylene.getGas(1000),Materials.Water.getFluid(1000)},new ItemStack[]{Materials.Palladium.getDust(2)},250,30);
        GT_Values.RA.addChemicalRecipe(Sodiumsulfate.get(cell,2),Materials.Hydrogen.getCells(1),null,Materials.SulfuricAcid.getFluid(2000),Materials.Sodium.getDust(1),Materials.Empty.getCells(3),30);
        //K2S2O7
        GT_Values.RA.addChemicalRecipe(Sodiumsulfate.get(cell,1),Materials.Potassium.getDust(2),Materials.Oxygen.getGas(3000),null,PotassiumDisulfate.get(dust),Materials.Empty.getCells(1),30);
        //Rh/Os/Ir/Ru
        GT_Values.RA.addBlastRecipe(PTResidue.get(dust),GT_Utility.getIntegratedCircuit(11),PotassiumDisulfate.getMolten(1440),RHSulfate.getFluidOrGas(1440),LeachResidue.get(dust),null,200,120,775);

        //Ru
        GT_Values.RA.addBlastRecipe(LeachResidue.get(dust),Materials.Saltpeter.getDust(8),Materials.SaltWater.getFluid(1000), GT_ModHandler.getSteam(1000),SodiumRuthenate.get(dust,3),IrOsLeachResidue.get(dust,6),200,120,775);
        GT_Values.RA.addChemicalRecipe(SodiumRuthenate.get(dust,6),Materials.Chlorine.getCells(3),null,RutheniumTetroxideSollution.getFluidOrGas(9000),Materials.Empty.getCells(3),300);
        GT_Values.RA.addFluidHeaterRecipe(null,RutheniumTetroxideSollution.getFluidOrGas(800),HotRutheniumTetroxideSollution.getFluidOrGas(800),600,480);
        //int circuitConfig, FluidStack aInput, FluidStack aInput2, FluidStack aOutput, int aDuration, int aEUt
        GT_Values.RA.addCrackingRecipe(17,RutheniumTetroxideSollution.getFluidOrGas(1000),null,HotRutheniumTetroxideSollution.getFluidOrGas(1000),150,480);
        GT_Values.RA.addDistillationTowerRecipe(HotRutheniumTetroxideSollution.getFluidOrGas(9000),new FluidStack[]{
                Materials.Water.getFluid(1800),
                RutheniumTetroxide.getFluidOrGas(7200)
        },Materials.Salt.getDust(6),1500,480);
        GT_Values.RA.addChemicalRecipe(RutheniumTetroxide.get(dust,1),Materials.HydrochloricAcid.getCells(8),null,Materials.Water.getFluid(2000),Ruthenium.get(dust),Materials.Chlorine.getCells(6),300);

        //Os
        GT_Values.RA.addBlastRecipe(IrOsLeachResidue.get(dust),GT_Utility.getIntegratedCircuit(11),Materials.HydrochloricAcid.getFluid(1000), AcidicOsmiumSolution.getFluidOrGas(1000), IrLeachResidue.get(dust),null,200,120,775);
        GT_Values.RA.addDistillationTowerRecipe(AcidicOsmiumSolution.getFluidOrGas(1000),new FluidStack[]{OsmiumSolution.getFluidOrGas(100),Materials.Water.getFluid(900)},null,600,BW_Util.getMachineVoltageFromTier(5));
        GT_Values.RA.addChemicalRecipe(OsmiumSolution.get(cell),Materials.HydrochloricAcid.getCells(8),null,Materials.Water.getFluid(2000),Materials.Osmium.getDust(1),Materials.Chlorine.getCells(6),300);

        //Ir
        GT_Values.RA.addBlastRecipe(IrLeachResidue.get(dust),GT_Utility.getIntegratedCircuit(1),null, null,PGSDResidue.get(dust),IridiumDioxide.get(dust),200,120,775);
        GT_Values.RA.addChemicalRecipe(IridiumDioxide.get(dust),Materials.HydrochloricAcid.getCells(1),null,AcidicIridiumSolution.getFluidOrGas(1000),Materials.Empty.getCells(1),null,300);
        GT_Values.RA.addChemicalRecipe(AcidicIridiumSolution.get(cell),AmmoniumCloride.get(cell,3),null,Materials.Ammonia.getGas(3000),Materials.Empty.getCells(4),IridiumChloride.get(dust),300);
        GT_Values.RA.addChemicalRecipe(IridiumChloride.get(dust),Materials.Calcium.getDust(3),null,CalciumChloride.getFluidOrGas(3000),PGSDResidue2.get(dust),Materials.Iridium.getDust(1),300,1920);
        //Rh
        GT_Values.RA.addMixerRecipe(RHSulfate.get(cell),null,null,null,Materials.Water.getFluid(1000),RHSulfateSolution.getFluidOrGas(1000),LeachResidue.get(dustSmall),300,30);
        GT_Values.RA.addChemicalRecipe(Materials.Zinc.getDust(1),null,RHSulfateSolution.getFluidOrGas(1000),null,ZincSulfate.get(dust),CrudeRhMetall.get(dust),300);
        GT_Values.RA.addBlastRecipe(CrudeRhMetall.get(dust),Materials.Salt.getDust(1),Materials.Chlorine.getGas(1000),null,RHSalt.get(dust,3),null,300,120,600);
        GT_Values.RA.addMixerRecipe(RHSalt.get(dust),null,null,null,Materials.Water.getFluid(1000),RHSaltSolution.getFluidOrGas(1000),null,300,30);
        GT_Values.RA.addChemicalRecipe(SodiumNitrate.get(dust),GT_Utility.getIntegratedCircuit(11),RHSaltSolution.getFluidOrGas(1000),null,RHNitrate.get(dust),Materials.Salt.getDust(1),300);
        GT_Values.RA.addSifterRecipe(RHNitrate.get(dust),new ItemStack[]{
                RhFilterCake.get(dustTiny),
                RhFilterCake.get(dustTiny),
                RhFilterCake.get(dustTiny),
                RhFilterCake.get(dustTiny),
                RhFilterCake.get(dustTiny),
                RhFilterCake.get(dustTiny),
                RhFilterCake.get(dustTiny),
                RhFilterCake.get(dustTiny),
                RhFilterCake.get(dustTiny),
        },new int[]{
                1000,1000,1000,1000,1000,1000,1000,1000,500,
        },600,30);
        GT_Values.RA.addMixerRecipe(RhFilterCake.get(dust),null,null,null,Materials.Water.getFluid(1000),RHFilterCakeSolution.getFluidOrGas(1000),null,300,30);
        GT_Values.RA.addChemicalRecipe(GT_Utility.getIntegratedCircuit(10),null,RHFilterCakeSolution.getFluidOrGas(1000),null,ReRh.get(dust),null,300);
        GT_Values.RA.addChemicalRecipe(ReRh.get(dust),Materials.Empty.getCells(1),Materials.HydrochloricAcid.getFluid(1000),Materials.Chlorine.getGas(1000),Rhodium.get(dust),Materials.Ammonia.getCells(1),300);
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
            HashSet toDel = new HashSet();
            recipeloop: for (GT_Recipe recipe : map.mRecipeList) {
                if (recipe.mFakeRecipe)
                    continue maploop;
                for (int i = 0; i < recipe.mFluidOutputs.length; i++) {
                    if (map.equals(GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes))
                        continue maploop;
                    if (GT_Utility.areFluidsEqual(Ruthenium.getMolten(1), recipe.mFluidOutputs[i]))
                        toDel.add(recipe);
                    else if (GT_Utility.areFluidsEqual(Rhodium.getMolten(1), recipe.mFluidOutputs[i]))
                        toDel.add(recipe);
                }
                for (int i = 0; i < recipe.mOutputs.length; i++) {
                    if (!GT_Utility.isStackValid(recipe.mOutputs[i]))
                        continue;
                    if (BW_Util.areStacksEqualOrNull(Ruthenium.get(dust), recipe.mOutputs[i]) || BW_Util.areStacksEqualOrNull(Ruthenium.get(dustImpure), recipe.mOutputs[i]) || BW_Util.areStacksEqualOrNull(Ruthenium.get(dustPure), recipe.mOutputs[i])) {
                        if (!BW_Util.areStacksEqualOrNull(Ruthenium.get(ingot), recipe.mInputs[0])) {
                            int amount = recipe.mOutputs[i].stackSize;
                            recipe.mOutputs[i] = LeachResidue.get(dust, amount);
                        }
                    }
                    if (BW_Util.areStacksEqualOrNull(Rhodium.get(dust), recipe.mOutputs[i]) || BW_Util.areStacksEqualOrNull(Rhodium.get(dustImpure), recipe.mOutputs[i]) || BW_Util.areStacksEqualOrNull(Rhodium.get(dustPure), recipe.mOutputs[i])) {
                        if (!BW_Util.areStacksEqualOrNull(Rhodium.get(ingot), recipe.mInputs[0])) {
                            int amount = recipe.mOutputs[i].stackSize;
                            recipe.mOutputs[i] = CrudeRhMetall.get(dust, amount);
                        }
                    }
                    if (!BW_Util.checkStackAndPrefix(recipe.mOutputs[i]))
                        continue;
                    //Pt
                    if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mMaterial.mMaterial.equals(Materials.Platinum)) {
                        for (int j = 0; j < recipe.mInputs.length; j++) {
                            if (BW_Util.checkStackAndPrefix(recipe.mInputs[j]))
                                if (PlatinumSludgeOverHaul.isInBlackList(recipe.mInputs[j]))
                                    continue recipeloop;
                        }
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
                        for (int j = 0; j < recipe.mInputs.length; j++) {
                            if (BW_Util.checkStackAndPrefix(recipe.mInputs[j]))
                                if (PlatinumSludgeOverHaul.isInBlackList(recipe.mInputs[j]))
                                    continue recipeloop;
                        }
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
                    } else if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mMaterial.mMaterial.equals(Materials.Osmium)) {
                        for (int j = 0; j < recipe.mInputs.length; j++) {
                            if (BW_Util.checkStackAndPrefix(recipe.mInputs[j]))
                                if (PlatinumSludgeOverHaul.isInBlackList(recipe.mInputs[j]))
                                    continue recipeloop;
                        }
                        if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(crushed) || GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(crushedCentrifuged) || GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(crushedPurified)) {
                            int amount = recipe.mOutputs[i].stackSize;
                            recipe.mOutputs[i] = IrOsLeachResidue.get(dustTiny).splitStack(amount * 22);
                        } else if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dust) || GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dustImpure) || GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dustPure)) {
                            int amount = recipe.mOutputs[i].stackSize;
                            recipe.mOutputs[i] = IrOsLeachResidue.get(dust).splitStack(amount * 2);
                        } else if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dustSmall)) {
                            int amount = recipe.mOutputs[i].stackSize;
                            recipe.mOutputs[i] = IrOsLeachResidue.get(dustSmall).splitStack(amount * 2);
                        } else if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dustTiny)) {
                            int amount = recipe.mOutputs[i].stackSize;
                            recipe.mOutputs[i] = IrOsLeachResidue.get(dustTiny).splitStack(amount * 2);
                        }
                    } else if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mMaterial.mMaterial.equals(Materials.Iridium)) {
                        for (int j = 0; j < recipe.mInputs.length; j++) {
                            if (BW_Util.checkStackAndPrefix(recipe.mInputs[j]))
                                if (PlatinumSludgeOverHaul.isInBlackList(recipe.mInputs[j]))
                                    continue recipeloop;
                        }
                        if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(crushed) || GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(crushedCentrifuged) || GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(crushedPurified)) {
                            int amount = recipe.mOutputs[i].stackSize;
                            recipe.mOutputs[i] = IrOsLeachResidue.get(dustTiny).splitStack(amount * 11);
                        } else if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dust) || GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dustImpure) || GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dustPure)) {
                            int amount = recipe.mOutputs[i].stackSize;
                            recipe.mOutputs[i] = IrOsLeachResidue.get(dust).splitStack(amount);
                        } else if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dustSmall)) {
                            int amount = recipe.mOutputs[i].stackSize;
                            recipe.mOutputs[i] = IrOsLeachResidue.get(dustSmall).splitStack(amount);
                        } else if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dustTiny)) {
                            int amount = recipe.mOutputs[i].stackSize;
                            recipe.mOutputs[i] = IrOsLeachResidue.get(dustTiny).splitStack(amount);
                        }
                    }
                }
            }
            map.mRecipeList.removeAll(toDel);
        }
        PlatinumSludgeOverHaul.runHelperrecipes();
    }

    private static final OrePrefixes[] OPWHITELIST = {
            dust,
            dustTiny,
            dustSmall,
            dustPure,
            dustImpure,
            dustRefined,
            crateGtDust,
            ore,
            oreBasalt,
            oreBlackgranite,
            oreDense,
            oreEnd,
            oreEndstone,
            oreMarble,
            oreNether,
            oreNetherrack,
            oreNormal,
            orePoor,
            oreRedgranite,
            oreRich,
            oreSmall,
            oreGem,
            denseore,
            crushed,
            crushedPurified,
            crushedCentrifuged,
            gem,
            gemFlawed,
            gemExquisite,
            gemFlawless,
            gemChipped,
            crateGtGem,
    };

    private static final Materials[] BLACKLIST = {
            Materials.HSSS,
            Materials.EnderiumBase,
            Materials.Osmiridium,
            Materials.get("Uraniumtriplatinid"),
            Materials.get("Tetranaquadahdiindiumhexaplatiumosminid"),
            Materials.get("Longasssuperconductornameforuvwire"),
    };

    private static boolean isInBlackList(ItemStack stack){
        if (!BW_Util.checkStackAndPrefix(stack))
            return false;
        for (Materials m : PlatinumSludgeOverHaul.BLACKLIST){
            if (GT_OreDictUnificator.getAssociation(stack).mMaterial.mMaterial.equals(m))
                return true;
        }
        return false;
    }
}