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

package com.github.bartimaeusnek.bartworks.system.material.GT_Enhancement;

import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.system.material.BW_MetaGenerated_Items;
import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import com.github.bartimaeusnek.crossmod.BartWorksCrossmod;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.items.GT_Generic_Block;
import gregtech.api.items.GT_Generic_Item;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.MaterialStack;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Ores_Abstract;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.*;

import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.*;
import static gregtech.api.enums.OrePrefixes.*;

public class PlatinumSludgeOverHaul {
    private static final Materials[] BLACKLIST = {
            Materials.HSSS,
            Materials.EnderiumBase,
            Materials.Osmiridium,
            Materials.get("Uraniumtriplatinid"),
            Materials.get("Tetranaquadahdiindiumhexaplatiumosminid"),
            Materials.get("Longasssuperconductornameforuvwire"),
    };
    private static final OrePrefixes[] OPBLACKLIST = {
           crushedCentrifuged,crushed,crushedPurified,dustPure,dustImpure,dustRefined,dust,dustTiny,dustSmall
    };

    private PlatinumSludgeOverHaul() {
    }

    private static void runHelperrecipes() {
        //DilutedSulfuricAcid
        GT_Values.RA.addMixerRecipe(Materials.SulfuricAcid.getCells(3), Materials.Water.getCells(1), GT_Utility.getIntegratedCircuit(1), null, null, null, Materials.DilutedSulfuricAcid.getCells(4), 30, 30);
        GT_Values.RA.addMixerRecipe(Materials.Water.getCells(1), GT_Utility.getIntegratedCircuit(1), null, null, Materials.SulfuricAcid.getFluid(3000), Materials.DilutedSulfuricAcid.getFluid(4000), Materials.Empty.getCells(1), 30, 30);
        GT_Values.RA.addMixerRecipe(Materials.SulfuricAcid.getCells(3), GT_Utility.getIntegratedCircuit(1), null, null, Materials.Water.getFluid(1000), Materials.DilutedSulfuricAcid.getFluid(4000), Materials.Empty.getCells(3), 30, 30);
        //FormicAcid
        GT_Values.RA.addChemicalRecipe(Materials.CarbonMonoxide.getCells(1), Materials.SodiumHydroxide.getDust(1), null, null, Sodiumformate.get(cell), null, 15);
        GT_Values.RA.addChemicalRecipe(Sodiumformate.get(cell, 2), GT_Utility.getIntegratedCircuit(1), Materials.SulfuricAcid.getFluid(1000), null, FormicAcid.get(cell, 2), Sodiumsulfate.get(dust, 7), 15);
        GT_Values.RA.addChemicalRecipe(Materials.SulfuricAcid.getCells(1), GT_Utility.getIntegratedCircuit(1), Sodiumformate.getFluidOrGas(2000), FormicAcid.getFluidOrGas(2000), Materials.Empty.getCells(1), Sodiumsulfate.get(dust, 7), 15);
        //AquaRegia
        GT_Values.RA.addMixerRecipe(Materials.DilutedSulfuricAcid.getCells(1), Materials.NitricAcid.getCells(1), GT_Utility.getIntegratedCircuit(1), null, null, null, AquaRegia.get(cell, 2), 30, 30);
        GT_Values.RA.addMixerRecipe(Materials.DilutedSulfuricAcid.getCells(1), Materials.NitricAcid.getCells(1), GT_Utility.getIntegratedCircuit(2), null, null, AquaRegia.getFluidOrGas(2000), Materials.Empty.getCells(2), 30, 30);
        GT_Values.RA.addMixerRecipe(Materials.NitricAcid.getCells(1), GT_Utility.getIntegratedCircuit(3), null, null, Materials.DilutedSulfuricAcid.getFluid(1000), AquaRegia.getFluidOrGas(2000), Materials.Empty.getCells(1), 30, 30);
        GT_Values.RA.addMixerRecipe(Materials.DilutedSulfuricAcid.getCells(1), GT_Utility.getIntegratedCircuit(4), null, null, Materials.NitricAcid.getFluid(1000), AquaRegia.getFluidOrGas(2000), Materials.Empty.getCells(1), 30, 30);

        //AmmoniumCloride
        GT_Values.RA.addChemicalRecipe(Materials.Ammonia.getCells(1), GT_Utility.getIntegratedCircuit(1), Materials.HydrochloricAcid.getFluid(1000), null, AmmoniumChloride.get(cell, 1), null, 15);
        GT_Values.RA.addChemicalRecipe(Materials.HydrochloricAcid.getCells(1), GT_Utility.getIntegratedCircuit(1), Materials.Ammonia.getGas(1000), AmmoniumChloride.getFluidOrGas(1000), Materials.Empty.getCells(1), null, 15);

        //base sollution
        for (Werkstoff w : Werkstoff.werkstoffHashSet)
            if (w.containsStuff(Materials.Sulfur) && (w.containsStuff(Materials.Copper) || w.containsStuff(Materials.Nickel))) {
                GT_Values.RA.addChemicalRecipe(w.get(crushedPurified), GT_Utility.getIntegratedCircuit(1), AquaRegia.getFluidOrGas(150), PTConcentrate.getFluidOrGas(150), null, 250);
                GT_Values.RA.addChemicalRecipe(w.get(crushedPurified), PTMetallicPowder.get(dust), AquaRegia.getFluidOrGas(1150), PTConcentrate.getFluidOrGas(1150), PTResidue.get(dustTiny), 250);
            }
        for (Materials m : Materials.values())
            if (PlatinumSludgeOverHaul.materialsContains(m, Materials.Sulfur) && (PlatinumSludgeOverHaul.materialsContains(m, Materials.Copper) || PlatinumSludgeOverHaul.materialsContains(m, Materials.Nickel))) {
                GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(crushedPurified, m, 1), GT_Utility.getIntegratedCircuit(1), AquaRegia.getFluidOrGas(150), PTConcentrate.getFluidOrGas(150), null, 250);
                GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(crushedPurified, m, 1), PTMetallicPowder.get(dust), AquaRegia.getFluidOrGas(1150), PTConcentrate.getFluidOrGas(1150), PTResidue.get(dustTiny), 250);
            }
        //Pt
        GT_Values.RA.addBlastRecipe(PTMetallicPowder.get(dust, 3), GT_Utility.getIntegratedCircuit(1), null, null, Materials.Platinum.getNuggets(2), null, 600, 120, Materials.Platinum.mMeltingPoint);

        GT_Values.RA.addChemicalRecipe(PTMetallicPowder.get(dust), GT_Utility.getIntegratedCircuit(1), AquaRegia.getFluidOrGas(1000), PTConcentrate.getFluidOrGas(1000), PTResidue.get(dustTiny), 250);
        GT_Values.RA.addCentrifugeRecipe(PTConcentrate.get(cell,2),null, AmmoniumChloride.getFluidOrGas(200), PDAmmonia.getFluidOrGas(200), PTSaltCrude.get(dustTiny, 18), PTRawPowder.get(dustTiny,2), Materials.NitrogenDioxide.getCells(1), Materials.DilutedSulfuricAcid.getCells(1), null, null, null, 1200, 30);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{}, new FluidStack[]{PTConcentrate.getFluidOrGas(2000), AmmoniumChloride.getFluidOrGas(200)}, new FluidStack[]{PDAmmonia.getFluidOrGas(200), Materials.NitrogenDioxide.getGas(1000),Materials.DilutedSulfuricAcid.getFluid(1000)}, new ItemStack[]{PTSaltCrude.get(dustTiny, 16), PTRawPowder.get(dustTiny,2)}, 1200, 30);
        GT_Values.RA.addSifterRecipe(PTSaltCrude.get(dust), new ItemStack[]{
                PTSaltRefined.get(dust),
                PTSaltRefined.get(dust),
                PTSaltRefined.get(dust),
                PTSaltRefined.get(dust),
                PTSaltRefined.get(dust),
                PTSaltRefined.get(dust),
                PTSaltRefined.get(dust),
                PTSaltRefined.get(dust),
                PTSaltRefined.get(dust),
        }, new int[] {
                1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1500,
        }, 600, 30);
        GT_Values.RA.addBlastRecipe(PTSaltRefined.get(dust), GT_Utility.getIntegratedCircuit(1), null, Materials.Chlorine.getGas(87), PTMetallicPowder.get(dust), null, 200, 120, 900);
        GT_Values.RA.addChemicalRecipe(PTRawPowder.get(dust, 2), Materials.Calcium.getDust(1), null, null, Materials.Platinum.getDust(2), CalciumChloride.get(dust), 30);
        //Pd
        GT_Values.RA.addChemicalRecipe(PDMetallicPowder.get(dust), GT_Utility.getIntegratedCircuit(1), Materials.Ammonia.getGas(1000), PDAmmonia.getFluidOrGas(1000), null, 250);
        GT_Values.RA.addChemicalRecipe(PDMetallicPowder.get(dust), GT_Utility.getIntegratedCircuit(1), PDAmmonia.getFluidOrGas(1000), null, PDSalt.get(dustTiny, 16), PDRawPowder.get(dustTiny, 2), 250);
        GT_Values.RA.addChemicalRecipe(GT_Utility.getIntegratedCircuit(2), null, PDAmmonia.getFluidOrGas(1000), null, PDSalt.get(dust), 250);
        GT_Values.RA.addSifterRecipe(PDSalt.get(dust), new ItemStack[]{
                PDMetallicPowder.get(dust),
                PDMetallicPowder.get(dust),
                PDMetallicPowder.get(dust),
                PDMetallicPowder.get(dust),
                PDMetallicPowder.get(dust),
                PDMetallicPowder.get(dust),
                PDMetallicPowder.get(dust),
                PDMetallicPowder.get(dust),
                PDMetallicPowder.get(dust),
        }, new int[] {
                1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1500,
        }, 600, 30);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(PDRawPowder.get(dust, 2), Materials.Empty.getCells(1), FormicAcid.getFluidOrGas(4000), Materials.Ammonia.getGas(2000), Materials.Palladium.getDust(2), Materials.Ethylene.getCells(1), 250, 30);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{PDRawPowder.get(dust, 2)}, new FluidStack[]{FormicAcid.getFluidOrGas(4000)}, new FluidStack[]{Materials.Ammonia.getGas(2000), Materials.Ethylene.getGas(1000), Materials.Water.getFluid(1000)}, new ItemStack[]{Materials.Palladium.getDust(2)}, 250, 30);
        GT_Values.RA.addChemicalRecipe(Sodiumsulfate.get(dust, 2), Materials.Hydrogen.getCells(1), null, Materials.SulfuricAcid.getFluid(2000), Materials.Sodium.getDust(1), Materials.Empty.getCells(3), 30);
        //K2S2O7
        // GT_Values.RA.addChemicalRecipe(Sodiumsulfate.get(dust), Materials.Potassium.getDust(2), Materials.Oxygen.getGas(3000), null, PotassiumDisulfate.get(dust,6), null, 30);
        //Rh/Os/Ir/Ru
        GT_Values.RA.addBlastRecipe(PTResidue.get(dust), GT_Utility.getIntegratedCircuit(11), PotassiumDisulfate.getMolten(360), RHSulfate.getFluidOrGas(360), LeachResidue.get(dust), null, 200, 120, 775);

        //Ru
        GT_Values.RA.addBlastRecipe(LeachResidue.get(dust, 10), Materials.Saltpeter.getDust(10), Materials.SaltWater.getFluid(1000), GT_ModHandler.getSteam(1000), SodiumRuthenate.get(dust, 3), IrOsLeachResidue.get(dust, 6), 200, 120, 775);
        GT_Values.RA.addChemicalRecipe(SodiumRuthenate.get(dust, 6), Materials.Chlorine.getCells(3), null, RutheniumTetroxideSollution.getFluidOrGas(9000), Materials.Empty.getCells(3), 300);
        GT_Values.RA.addFluidHeaterRecipe(GT_Utility.getIntegratedCircuit(1), RutheniumTetroxideSollution.getFluidOrGas(800), HotRutheniumTetroxideSollution.getFluidOrGas(800), 300, 480);
        GT_Values.RA.addCrackingRecipe(17, RutheniumTetroxideSollution.getFluidOrGas(1000), GT_ModHandler.getSteam(1000), HotRutheniumTetroxideSollution.getFluidOrGas(2000), 150, 480);
        GT_Values.RA.addDistillationTowerRecipe(HotRutheniumTetroxideSollution.getFluidOrGas(9000), new FluidStack[]{
                Materials.Water.getFluid(1800),
                RutheniumTetroxide.getFluidOrGas(7200)
        }, Materials.Salt.getDust(6), 1500, 480);
        GT_Values.RA.addChemicalRecipe(RutheniumTetroxide.get(dust, 1), Materials.HydrochloricAcid.getCells(6), null, Materials.Water.getFluid(2000), Ruthenium.get(dust), Materials.Chlorine.getCells(6), 300);

        //Os
        GT_Values.RA.addBlastRecipe(IrOsLeachResidue.get(dust, 4), GT_Utility.getIntegratedCircuit(11), Materials.HydrochloricAcid.getFluid(1000), AcidicOsmiumSolution.getFluidOrGas(2000), IrLeachResidue.get(dust, 2), null, 200, 120, 775);
        GT_Values.RA.addDistillationTowerRecipe(AcidicOsmiumSolution.getFluidOrGas(1000), new FluidStack[]{OsmiumSolution.getFluidOrGas(100), Materials.Water.getFluid(900)}, null, 150, BW_Util.getMachineVoltageFromTier(5));
        GT_Values.RA.addChemicalRecipe(OsmiumSolution.get(cell), Materials.HydrochloricAcid.getCells(6), null, Materials.Water.getFluid(2000), Materials.Osmium.getDust(1), Materials.Chlorine.getCells(7), 300);

        //Ir
        GT_Values.RA.addBlastRecipe(IrLeachResidue.get(dust), GT_Utility.getIntegratedCircuit(1), null, null, PGSDResidue.get(dust), IridiumDioxide.get(dust), 200, 120, 775);
        GT_Values.RA.addChemicalRecipe(IridiumDioxide.get(dust), Materials.HydrochloricAcid.getCells(1), null, AcidicIridiumSolution.getFluidOrGas(1000), Materials.Empty.getCells(1), null, 300);
        GT_Values.RA.addChemicalRecipe(AcidicIridiumSolution.get(cell), AmmoniumChloride.get(cell, 3), null, Materials.Ammonia.getGas(3000), Materials.Empty.getCells(4), IridiumChloride.get(dust), 300);
        GT_Values.RA.addChemicalRecipe(IridiumChloride.get(dust), Materials.Calcium.getDust(3), null, CalciumChloride.getFluidOrGas(3000), PGSDResidue2.get(dust), Materials.Iridium.getDust(1), 300, 1920);

        //Rh
        GT_Values.RA.addChemicalRecipe(RHSulfate.get(cell, 11), GT_Utility.getIntegratedCircuit(1), Materials.Water.getFluid(10000), Materials.Potassium.getMolten(2000), RHSulfateSolution.get(cell, 11), LeachResidue.get(dustTiny, 10), 300, 30);

        GT_Values.RA.addChemicalRecipe(Materials.Zinc.getDust(1), null, RHSulfateSolution.getFluidOrGas(1000), null, ZincSulfate.get(dust,6), CrudeRhMetall.get(dust), 300);
        GT_Values.RA.addBlastRecipe(CrudeRhMetall.get(dust), Materials.Salt.getDust(1), Materials.Chlorine.getGas(1000), null, RHSalt.get(dust, 3), null, 300, 120, 600);
        GT_Values.RA.addMixerRecipe(RHSalt.get(dust, 10), null, null, null, Materials.Water.getFluid(2000), RHSaltSolution.getFluidOrGas(2000), null, 300, 30);
        GT_Values.RA.addChemicalRecipe(SodiumNitrate.get(dust), GT_Utility.getIntegratedCircuit(1), RHSaltSolution.getFluidOrGas(1000), null, RHNitrate.get(dust), Materials.Salt.getDust(1), 300);
        GT_Values.RA.addSifterRecipe(RHNitrate.get(dust), new ItemStack[]{
                RhFilterCake.get(dust),
                RhFilterCake.get(dust),
                RhFilterCake.get(dust),
                RhFilterCake.get(dust),
                RhFilterCake.get(dust),
                RhFilterCake.get(dust),
                RhFilterCake.get(dust),
                RhFilterCake.get(dust),
                RhFilterCake.get(dust),
        }, new int[] {
                1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1500,
        }, 600, 30);
        GT_Values.RA.addMixerRecipe(RhFilterCake.get(dust), null, null, null, Materials.Water.getFluid(1000), RHFilterCakeSolution.getFluidOrGas(1000), null, 300, 30);
        GT_Values.RA.addChemicalRecipe(GT_Utility.getIntegratedCircuit(2), null, RHFilterCakeSolution.getFluidOrGas(1000), null, ReRh.get(dust), null, 300);
        GT_Values.RA.addChemicalRecipe(ReRh.get(dust), Materials.Empty.getCells(1), Materials.HydrochloricAcid.getFluid(1000), Materials.Chlorine.getGas(1000), Rhodium.get(dust), Materials.Ammonia.getCells(1), 300);
    }

    private static boolean materialsContains(Materials one, ISubTagContainer other) {
        if (one == null || one.mMaterialList == null || one.mMaterialList.isEmpty())
            return false;
        for (MaterialStack stack : one.mMaterialList)
            if (stack.mMaterial.equals(other))
                return true;
        return false;
    }

    public static void replacePureElements() {
        //furnace
        for (Object entry : FurnaceRecipes.smelting().getSmeltingList().entrySet()) {
            Map.Entry realEntry = (Map.Entry) entry;
            if (GT_Utility.isStackValid(realEntry.getKey()) && BW_Util.checkStackAndPrefix((ItemStack) realEntry.getKey()))
                if ((!GT_OreDictUnificator.getAssociation((ItemStack) realEntry.getKey()).mPrefix.equals(dust) && !GT_OreDictUnificator.getAssociation((ItemStack) realEntry.getKey()).mPrefix.equals(dustTiny)) || !GT_OreDictUnificator.getAssociation((ItemStack) realEntry.getKey()).mMaterial.mMaterial.equals(Materials.Platinum))
                    if (GT_Utility.isStackValid(realEntry.getValue()) && BW_Util.checkStackAndPrefix((ItemStack) realEntry.getValue())){
                        ItemData ass = GT_OreDictUnificator.getAssociation((ItemStack) realEntry.getValue());
                        if (ass.mMaterial.mMaterial.equals(Materials.Platinum))
                            if (!PlatinumSludgeOverHaul.isInBlackList((ItemStack) realEntry.getKey()))
                                realEntry.setValue(PTMetallicPowder.get(ass.mPrefix == nugget ? dustTiny : dust, ((ItemStack) realEntry.getValue()).stackSize * 2));
                        else if (ass.mMaterial.mMaterial.equals(Materials.Palladium))
                            if (!PlatinumSludgeOverHaul.isInBlackList((ItemStack) realEntry.getKey()))
                                realEntry.setValue(PDMetallicPowder.get(ass.mPrefix == nugget ? dustTiny : dust, ((ItemStack) realEntry.getValue()).stackSize * 2));
                    }
        }
        //vanilla crafting
        CraftingManager.getInstance().getRecipeList().forEach(PlatinumSludgeOverHaul::setnewMaterialInRecipe);
        //gt crafting
        try {
            ((List<IRecipe>) FieldUtils.getDeclaredField(GT_ModHandler.class, "sBufferRecipeList", true).get(null)).forEach(PlatinumSludgeOverHaul::setnewMaterialInRecipe);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        //gt machines
        maploop:
        for (GT_Recipe.GT_Recipe_Map map : GT_Recipe.GT_Recipe_Map.sMappings) {
            if (map == GT_Recipe.GT_Recipe_Map.sUnboxinatorRecipes || map == GT_Recipe.GT_Recipe_Map.sBoxinatorRecipes)
                continue;
            HashSet<GT_Recipe> toDel = new HashSet<GT_Recipe>();
            recipeloop:
            for (GT_Recipe recipe : map.mRecipeList) {
                if (recipe.mFakeRecipe)
                    continue maploop;

                for (int i = 0; i < recipe.mFluidOutputs.length; i++) {
                    if (map.equals(GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes))
                        continue maploop;
                    else if (map.equals(GT_Recipe.GT_Recipe_Map.sMultiblockChemicalRecipes) || map.equals(GT_Recipe.GT_Recipe_Map.sChemicalRecipes)) {
                        if (GT_Utility.areFluidsEqual(Ruthenium.getMolten(1), recipe.mFluidOutputs[i]))
                            toDel.add(recipe);
                        else if (GT_Utility.areFluidsEqual(Rhodium.getMolten(1), recipe.mFluidOutputs[i]))
                            toDel.add(recipe);
                        else if (GT_Utility.areFluidsEqual(Materials.Iridium.getMolten(1), recipe.mFluidOutputs[i]))
                            recipe.mFluidOutputs[i] = Materials.Water.getFluid(1000);
                        else if (GT_Utility.areFluidsEqual(Materials.Platinum.getMolten(1), recipe.mFluidOutputs[i]))
                            recipe.mFluidOutputs[i] = Materials.Water.getFluid(1000);
                        else if (GT_Utility.areFluidsEqual(Materials.Osmium.getMolten(1), recipe.mFluidOutputs[i]))
                            recipe.mFluidOutputs[i] = Materials.Water.getFluid(1000);
                    } else {
                        if (GT_Utility.areFluidsEqual(Ruthenium.getMolten(1), recipe.mFluidOutputs[i]))
                            toDel.add(recipe);
                        else if (GT_Utility.areFluidsEqual(Rhodium.getMolten(1), recipe.mFluidOutputs[i]))
                            toDel.add(recipe);
                        else if (GT_Utility.areFluidsEqual(Materials.Iridium.getMolten(1), recipe.mFluidOutputs[i]))
                            toDel.add(recipe);
                        else if (GT_Utility.areFluidsEqual(Materials.Platinum.getMolten(1), recipe.mFluidOutputs[i]))
                            toDel.add(recipe);
                        else if (GT_Utility.areFluidsEqual(Materials.Osmium.getMolten(1), recipe.mFluidOutputs[i]))
                            toDel.add(recipe);
                    }
                }
                for (int i = 0; i < recipe.mOutputs.length; i++) {
                    if (!GT_Utility.isStackValid(recipe.mOutputs[i]))
                        continue;
                    if (BW_Util.areStacksEqualOrNull(Ruthenium.get(dust), recipe.mOutputs[i]) || BW_Util.areStacksEqualOrNull(Ruthenium.get(dustImpure), recipe.mOutputs[i]) || BW_Util.areStacksEqualOrNull(Ruthenium.get(dustPure), recipe.mOutputs[i])) {
                        if (!BW_Util.areStacksEqualOrNull(Ruthenium.get(ingot), recipe.mInputs[0])) {
                            for (int j = 0; j < recipe.mInputs.length; j++)
                                if (PlatinumSludgeOverHaul.isInBlackList(recipe.mInputs[j]))
                                    continue recipeloop;
                            int amount = recipe.mOutputs[i].stackSize * 2;
                            recipe.mOutputs[i] = LeachResidue.get(dust, amount);
                        }
                    }
                    if (BW_Util.areStacksEqualOrNull(Rhodium.get(dust), recipe.mOutputs[i]) || BW_Util.areStacksEqualOrNull(Rhodium.get(dustImpure), recipe.mOutputs[i]) || BW_Util.areStacksEqualOrNull(Rhodium.get(dustPure), recipe.mOutputs[i])) {
                        if (!BW_Util.areStacksEqualOrNull(Rhodium.get(ingot), recipe.mInputs[0])) {
                            for (int j = 0; j < recipe.mInputs.length; j++)
                                if (PlatinumSludgeOverHaul.isInBlackList(recipe.mInputs[j]))
                                    continue recipeloop;
                            int amount = recipe.mOutputs[i].stackSize * 2;
                            recipe.mOutputs[i] = CrudeRhMetall.get(dust, amount);
                        }
                    }
                    if (!BW_Util.checkStackAndPrefix(recipe.mOutputs[i]))
                        continue;
                    //Pt
                    if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mMaterial.mMaterial.equals(Materials.Platinum)) {
                        for (int j = 0; j < recipe.mInputs.length; j++) {
                            if (PlatinumSludgeOverHaul.isInBlackList(recipe.mInputs[j]))
                                continue recipeloop;
                        }
                        if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dust) || GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dustImpure) || GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dustPure)) {
                            int amount = recipe.mOutputs[i].stackSize;
                            recipe.mOutputs[i] = PTMetallicPowder.get(dust).splitStack(amount * 2);
                        } else if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dustSmall)) {
                            int amount = recipe.mOutputs[i].stackSize;
                            recipe.mOutputs[i] = PTMetallicPowder.get(dustSmall).splitStack(amount * 2);
                        } else if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dustTiny)) {
                            int amount = recipe.mOutputs[i].stackSize;
                            recipe.mOutputs[i] = PTMetallicPowder.get(dustTiny).splitStack(amount * 2);
                        }
                    } else if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mMaterial.mMaterial.equals(Materials.Palladium)) {
                        for (int j = 0; j < recipe.mInputs.length; j++) {
                            if (PlatinumSludgeOverHaul.isInBlackList(recipe.mInputs[j]))
                                continue recipeloop;
                        }
                        if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dust) || GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dustImpure) || GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dustPure)) {
                            int amount = recipe.mOutputs[i].stackSize;
                            recipe.mOutputs[i] = PDMetallicPowder.get(dust).splitStack(amount * 2);
                        } else if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dustSmall)) {
                            int amount = recipe.mOutputs[i].stackSize;
                            recipe.mOutputs[i] = PDMetallicPowder.get(dustSmall).splitStack(amount * 2);
                        } else if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dustTiny)) {
                            int amount = recipe.mOutputs[i].stackSize;
                            recipe.mOutputs[i] = PDMetallicPowder.get(dustTiny).splitStack(amount * 2);
                        }
                    } else if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mMaterial.mMaterial.equals(Materials.Osmium)) {
                        for (int j = 0; j < recipe.mInputs.length; j++) {
                            if (PlatinumSludgeOverHaul.isInBlackList(recipe.mInputs[j]))
                                continue recipeloop;
                        }
                        if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dust) || GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dustImpure) || GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dustPure)) {
                            int amount = recipe.mOutputs[i].stackSize;
                            recipe.mOutputs[i] = IrOsLeachResidue.get(dust).splitStack(amount);
                        } else if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dustSmall)) {
                            int amount = recipe.mOutputs[i].stackSize;
                            recipe.mOutputs[i] = IrOsLeachResidue.get(dustSmall).splitStack(amount);
                        } else if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dustTiny)) {
                            int amount = recipe.mOutputs[i].stackSize;
                            recipe.mOutputs[i] = IrOsLeachResidue.get(dustTiny).splitStack(amount);
                        }
                    } else if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mMaterial.mMaterial.equals(Materials.Iridium)) {
                        for (int j = 0; j < recipe.mInputs.length; j++) {
                            if (PlatinumSludgeOverHaul.isInBlackList(recipe.mInputs[j]))
                                continue recipeloop;
                        }
                        if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dust) || GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dustImpure) || GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dustPure)) {
                            int amount = recipe.mOutputs[i].stackSize;
                            recipe.mOutputs[i] = IrLeachResidue.get(dust).splitStack(amount);
                        } else if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dustSmall)) {
                            int amount = recipe.mOutputs[i].stackSize;
                            recipe.mOutputs[i] = IrLeachResidue.get(dustSmall).splitStack(amount);
                        } else if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix.equals(dustTiny)) {
                            int amount = recipe.mOutputs[i].stackSize;
                            recipe.mOutputs[i] = IrLeachResidue.get(dustTiny).splitStack(amount);
                        }
                    }
                }
            }
            map.mRecipeList.removeAll(toDel);
        }
        //TODO: remove EnderIO recipes

        //fix HV tier
        PlatinumSludgeOverHaul.replaceHVCircuitMaterials();
        //add new recipes
        PlatinumSludgeOverHaul.runHelperrecipes();
    }

    private static void replaceHVCircuitMaterials(){
        GT_Values.RA.addMixerRecipe(Materials.Redstone.getDust(1),Materials.Electrum.getDust(1),GT_Utility.getIntegratedCircuit(1),null,null,null,Materials.Nikolite.getDust(8),1800,120);
        for (GT_Recipe recipe : GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes.mRecipeList){
            if (recipe.mEUt > 512)
                continue;
            if (BW_Util.checkStackAndPrefix(recipe.mOutputs[0])) {
                for (int i = 0; i < recipe.mInputs.length; i++) {
                    ItemStack stack = recipe.mInputs[i];
                    ItemData ass = GT_OreDictUnificator.getAssociation(stack);
                    if (BW_Util.checkStackAndPrefix(stack) && ass.mMaterial.mMaterial.equals(Materials.Platinum)) {
                        recipe.mInputs[i] = GT_OreDictUnificator.get(ass.mPrefix,Materials.BlueAlloy,stack.stackSize);
                    }
                }
            }
        }
    }

    private static void setnewMaterialInRecipe(Object obj) {
        String inputName = "output";
        String inputItemName = "input";
        if (!(obj instanceof ShapedOreRecipe || obj instanceof ShapelessOreRecipe)){
                if (obj instanceof ShapedRecipes || (obj instanceof ShapelessRecipes)) {
                    inputName = "recipeOutput";
                    inputItemName = "recipeItems";
                }
                else if (Loader.isModLoaded("miscutils")) {
                    try {
                        if (Class.forName("gtPlusPlus.api.objects.minecraft.ShapedRecipe").isAssignableFrom(obj.getClass()))
                            obj = FieldUtils.getField(obj.getClass(),"mRecipe",true).get(obj);
                    } catch (ClassNotFoundException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
            }
        }

        IRecipe recipe = (IRecipe) obj;
        ItemStack otpt = recipe.getRecipeOutput();

        Field out = FieldUtils.getDeclaredField(recipe.getClass(), inputName, true);
        if (out == null)
            out = FieldUtils.getField(recipe.getClass(), inputName, true);


        Field in = FieldUtils.getDeclaredField(recipe.getClass(), inputItemName, true);
        if (in == null)
            in = FieldUtils.getField(recipe.getClass(), inputItemName, true);
        if (in == null)
            return;
        FieldUtils.removeFinalModifier(in,true);
        Object input = null;
        try {
            input = in.get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return;
        }
        assert input != null;

        if (out != null && GT_Utility.areStacksEqual(otpt, Materials.Platinum.getDust(1), true)) {
            if (PlatinumSludgeOverHaul.checkRecipe(input,Materials.Platinum))
                return;
            try {
                out.set(recipe, PTMetallicPowder.get(dust, otpt.stackSize * 2));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else if (out != null && GT_Utility.areStacksEqual(otpt, Materials.Palladium.getDust(1), true)) {
            if (PlatinumSludgeOverHaul.checkRecipe(input,Materials.Palladium))
                return;
            try {
                out.set(recipe, PDMetallicPowder.get(dust, otpt.stackSize * 2));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else if (out != null && GT_Utility.areStacksEqual(otpt, Materials.Iridium.getDust(1), true)) {
            if (PlatinumSludgeOverHaul.checkRecipe(input,Materials.Iridium))
                return;
            try {
                out.set(recipe, IrLeachResidue.get(dust, otpt.stackSize));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else if (out != null && GT_Utility.areStacksEqual(otpt, Materials.Osmium.getDust(1), true)) {
            if (PlatinumSludgeOverHaul.checkRecipe(input,Materials.Osmium))
                return;
            try {
                out.set(recipe, IrOsLeachResidue.get(dust, otpt.stackSize));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean checkRecipe(Object input,Materials mat){
        if (input instanceof List || input instanceof Object[]) {
            Set lists = new HashSet(), stacks= new HashSet();
            List ip = input instanceof List ? (List)input : new ArrayList();
            Object[] ip2 =  input instanceof Object[] ? (Object[])input : new Object[0];

            for (Object o : ip){
                if (o instanceof List)
                    lists.add(o);
                else if (o instanceof ItemStack)
                    stacks.add(o);
            }
            for (Object o : ip2){
                if (o instanceof List)
                    lists.add(o);
                else if (o instanceof ItemStack)
                    stacks.add(o);
            }

            for (Object o : lists) {
                if (!((List) o).isEmpty())
                    stacks.add(((List) o).get(0));
            }

            for (Object stack : stacks) {
                if (stack instanceof ItemStack) {
                    if (GT_Utility.areStacksEqual(GT_OreDictUnificator.get(crateGtDust, mat, 1), (ItemStack) stack))
                        return true;
                }
            }

            boolean allSame = false;
            for (Object stack : stacks) {
                if (stack instanceof ItemStack) {
                    allSame = BW_Util.checkStackAndPrefix((ItemStack) stack) && GT_OreDictUnificator.getAssociation((ItemStack) stack).mMaterial.mMaterial.equals(mat);
                } else {
                    allSame = false;
                    break;
                }
                if (!allSame)
                    break;
            }
            return allSame;
        }
        return false;
    }

    private static boolean isInBlackList(ItemStack stack) {
        if (stack == null)
            return true;

        if (stack.getItem() instanceof BW_MetaGenerated_Items)
            return true;

        if (GameRegistry.findUniqueIdentifierFor(stack.getItem()).modId.equals(MainMod.MOD_ID))
            return true;

        if (GameRegistry.findUniqueIdentifierFor(stack.getItem()).modId.equals(BartWorksCrossmod.MOD_ID))
            return true;

        if (Block.getBlockFromItem(stack.getItem()) instanceof GT_Generic_Block && !(Block.getBlockFromItem(stack.getItem()) instanceof GT_Block_Ores_Abstract))
            return true;

        if (GT_Utility.areStacksEqual(ItemList.Depleted_Naquadah_1.get(1),stack,true) || GT_Utility.areStacksEqual(ItemList.Depleted_Naquadah_2.get(1),stack,true) || GT_Utility.areStacksEqual(ItemList.Depleted_Naquadah_4.get(1),stack,true))
            return true;


        if (GT_Utility.areStacksEqual(ItemList.Tool_Lighter_Platinum_Empty.get(1),stack,true) || GT_Utility.areStacksEqual(ItemList.Tool_Lighter_Platinum_Used.get(1),stack,true) || GT_Utility.areStacksEqual(ItemList.Tool_Lighter_Platinum_Full.get(1),stack,true))
            return true;

        if (GT_Utility.areStacksEqual(ItemList.Emitter_EV.get(1),stack,true))
            return true;

        try {
            ItemList gtnhItemListItems = ItemList.valueOf("Large_Fluid_Cell_TungstenSteel");
            if (GT_Utility.areStacksEqual(gtnhItemListItems.get(1), stack, true))
                return true;
            gtnhItemListItems = ItemList.valueOf("Depleted_MNq_1");
            if (GT_Utility.areStacksEqual(gtnhItemListItems.get(1), stack, true))
                return true;
            gtnhItemListItems = ItemList.valueOf("Depleted_MNq_2");
            if (GT_Utility.areStacksEqual(gtnhItemListItems.get(1), stack, true))
                return true;
            gtnhItemListItems = ItemList.valueOf("Depleted_MNq_4");
            if (GT_Utility.areStacksEqual(gtnhItemListItems.get(1), stack, true))
                return true;
        } catch (IllegalArgumentException ignored) {}

        if (stack.getItem() instanceof GT_Generic_Item) {
            if (!BW_Util.checkStackAndPrefix(stack))
                return false;
            return (!Arrays.asList(PlatinumSludgeOverHaul.OPBLACKLIST).contains(GT_OreDictUnificator.getAssociation(stack).mPrefix)) || Arrays.asList(PlatinumSludgeOverHaul.BLACKLIST).contains(GT_OreDictUnificator.getAssociation(stack).mMaterial.mMaterial);
        }

        if (Loader.isModLoaded("miscutils")) {
            try {
                if (Class.forName("gtPlusPlus.core.item.base.BaseItemComponent").isAssignableFrom(stack.getItem().getClass()) && !(stack.getUnlocalizedName().contains("dust") || stack.getUnlocalizedName().contains("Dust")))
                    return true;
                if (Class.forName("gtPlusPlus.core.block.base.BlockBaseModular").isAssignableFrom(Block.getBlockFromItem(stack.getItem()).getClass()))
                    return true;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (!BW_Util.checkStackAndPrefix(stack))
            return false;

        return Arrays.asList(PlatinumSludgeOverHaul.BLACKLIST).contains(GT_OreDictUnificator.getAssociation(stack).mMaterial.mMaterial);
    }
}