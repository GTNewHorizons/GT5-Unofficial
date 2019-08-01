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

package com.github.bartimaeusnek.bartworks.system.material.CircuitGeneration;

import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.bartworks.util.BWRecipes;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashSet;
import java.util.Iterator;

public class CircuitImprintLoader implements Runnable {

    static final ArrayListMultimap<NBTTagCompound,GT_Recipe> recipeTagMap = ArrayListMultimap.create();
    static final HashBiMap<CircuitData,NBTTagCompound> bwCircuitTagMap = HashBiMap.create(20);
    static final HashSet<NBTTagCompound> refs = new HashSet<>();
    public static short reverseIDs = Short.MAX_VALUE-1;
    public static HashBiMap<Short, ItemList> circuitIIconRefs = HashBiMap.create(20);

    public static NBTTagCompound getTagFromStack(ItemStack stack){
        if (GT_Utility.isStackValid(stack))
            return stack.copy().splitStack(1).writeToNBT(new NBTTagCompound());
        return new NBTTagCompound();
    }

    public static ItemStack getStackFromTag(NBTTagCompound tagCompound){
        return ItemStack.loadItemStackFromNBT(tagCompound);
    }

    @Override
    public void run() {
        Iterator<GT_Recipe> it = GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes.mRecipeList.iterator();
        GT_Recipe circuitRecipe;
        HashSet<GT_Recipe> toRem = new HashSet<>();
        HashSet<GT_Recipe> toAdd = new HashSet<>();
        while (it.hasNext()) {
            circuitRecipe = it.next();
            ItemStack[] outputs = circuitRecipe.mOutputs;
            if (outputs.length < 1)
                continue;
            int[] oreIDS = OreDictionary.getOreIDs(outputs[0]);
            if (oreIDS.length < 1)
                continue;
            String name = OreDictionary.getOreName(oreIDS[0]);
            if (name.contains("Circuit") || name.contains("circuit")) {
                CircuitImprintLoader.recipeTagMap.put(CircuitImprintLoader.getTagFromStack(outputs[0]), circuitRecipe.copy());
                for (ItemStack s : circuitRecipe.mInputs) {
                    if (circuitRecipe.mFluidInputs[0].isFluidEqual(Materials.SolderingAlloy.getMolten(0)))
                        CircuitImprintLoader.refs.add(CircuitImprintLoader.getTagFromStack(s.copy().splitStack(1)));
                }
                if (circuitRecipe.mFluidInputs[0].isFluidEqual(Materials.SolderingAlloy.getMolten(0))) {

                    GT_Recipe newRecipe = CircuitImprintLoader.reBuildRecipe(circuitRecipe);
                    if (newRecipe != null)
                        BWRecipes.instance.getMappingsFor(BWRecipes.CIRCUITASSEMBLYLINE).addRecipe(newRecipe);
                    if (circuitRecipe.mEUt > BW_Util.getTierVoltage(5)) {
                        toRem.add(circuitRecipe);
                        toAdd.add(CircuitImprintLoader.makeMoreExpensive(circuitRecipe));
                    }
                } else {
                    if (circuitRecipe.mEUt > BW_Util.getTierVoltage(5)) {
                        toRem.add(circuitRecipe);
                    }
                }

            }
        }
        GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes.mRecipeList.addAll(toAdd);
        GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes.mRecipeList.removeAll(toRem);
        this.makeCircuitImprints();
    }

    public static GT_Recipe makeMoreExpensive(GT_Recipe original){
        GT_Recipe newRecipe = original.copy();
        for (ItemStack is : newRecipe.mInputs){
            int[] oreIDs = OreDictionary.getOreIDs(is);
            if(oreIDs == null || oreIDs.length < 1 || !OreDictionary.getOreName(oreIDs[0]).contains("circuit"))
                is.stackSize = Math.max( 64, is.stackSize *= 4);
        }
        newRecipe.mFluidInputs[0].amount *= 4;
        newRecipe.mDuration *= 4;
        return newRecipe;
    }

    public static GT_Recipe reBuildRecipe(GT_Recipe original){
       ItemStack out = original.copy().getOutput(0);
       out.stackSize = 16;
       ItemStack[] in = new  ItemStack[6];
       BiMap<ItemList, Short> inversed = CircuitImprintLoader.circuitIIconRefs.inverse();
       for (int i = 0; i < 6; i++) {
           try {
           for (ItemList il : inversed.keySet()){
                   if (GT_Utility.areStacksEqual(il.get(1), original.mInputs[i])) {
                       in[i] = BW_Meta_Items.getNEWCIRCUITS().getStack(inversed.get(il), original.mInputs[i].stackSize);
                   }

           }
           if (original.mInputs[i] != null && in[i] == null){
               if (BW_Util.checkStackAndPrefix(original.mInputs[i]) && GT_OreDictUnificator.getAssociation(original.mInputs[i]).mPrefix == OrePrefixes.wireGt01){
                   in[i] = GT_OreDictUnificator.get(OrePrefixes.wireGt16,GT_OreDictUnificator.getAssociation(original.mInputs[i]).mMaterial.mMaterial,original.mInputs[i].stackSize);
               }
               else {
                   in[i] = original.mInputs[i].copy();
                   in[i].stackSize *= 16;
               }
//               if (in[i].stackSize > 64)
//                   return null;
           }
           } catch (ArrayIndexOutOfBoundsException e){
               break;
           } catch (NullPointerException e){
               e.printStackTrace();
           }
       }
       if (CircuitImprintLoader.checkForBlacklistedComponents(in)){
           return null;
       }

       return new BWRecipes.DynamicGTRecipe(false,in,new ItemStack[]{out},BW_Meta_Items.getNEWCIRCUITS().getStackWithNBT(CircuitImprintLoader.getTagFromStack(original.mOutputs[0]),0,0),null, original.mFluidInputs,null,original.mDuration,original.mEUt,original.mSpecialValue);
    }


    public static final HashSet<ItemStack> blacklistSet = new HashSet<>();

    private static boolean checkForBlacklistedComponents(ItemStack[] itemStacks){
        for (ItemStack is: itemStacks){
            for (ItemStack is2 : CircuitImprintLoader.blacklistSet){
                if (GT_Utility.areStacksEqual(is,is2))
                    return true;
            }
        }
        return false;
    }

    private void makeCircuitImprints(){
        for (NBTTagCompound tag : CircuitImprintLoader.recipeTagMap.keySet()){
            ItemStack stack = CircuitImprintLoader.getStackFromTag(tag);
            int eut = Integer.MAX_VALUE;
            for (GT_Recipe recipe : CircuitImprintLoader.recipeTagMap.get(tag)) {
                eut = Math.min(eut, recipe.mEUt);
            }
            eut = Math.min(eut,BW_Util.getMachineVoltageFromTier(BW_Util.getCircuitTierFromOreDictName(OreDictionary.getOreName(OreDictionary.getOreIDs(stack)[0]))));
            GT_Recipe slicingRecipe = new BWRecipes.DynamicGTRecipe(true,new ItemStack[]{stack,ItemList.Shape_Slicer_Flat.get(0)},new ItemStack[]{BW_Meta_Items.getNEWCIRCUITS().getStackWithNBT(tag,1,1)},null,null,null,null,300,eut,BW_Util.CLEANROOM);
            GT_Recipe.GT_Recipe_Map.sSlicerRecipes.add(slicingRecipe);
            GT_ModHandler.addCraftingRecipe(BW_Meta_Items.getNEWCIRCUITS().getStackWithNBT(tag,0,1),GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS ,new Object[]{
                    " X ",
                    "GPG",
                    " X ",
                    'P', BW_Meta_Items.getNEWCIRCUITS().getStackWithNBT(tag,1,1),
                    'G', WerkstoffLoader.Prasiolite.get(OrePrefixes.gemExquisite,1),
                    'X', BW_Meta_Items.getNEWCIRCUITS().getStack(3)
            });
        }
//      for (NBTTagCompound tag : CircuitImprintLoader.bwCircuitTagMap.values()){
//          CircuitData data = CircuitImprintLoader.bwCircuitTagMap.inverse().get(tag);
//          ItemStack stack = CircuitImprintLoader.getStackFromTag(tag);
//          GT_Recipe slicingRecipe = new BWRecipes.DynamicGTRecipe(true,new ItemStack[]{stack,ItemList.Shape_Slicer_Flat.get(0)},new ItemStack[]{BW_Meta_Items.getNEWCIRCUITS().getStackWithNBT(tag,1,1)},null,null,null,null,300, Math.toIntExact(data.getaVoltage()),data.getaSpecial());
//          GT_Recipe.GT_Recipe_Map.sSlicerRecipes.add(slicingRecipe);
//          GT_ModHandler.addCraftingRecipe(BW_Meta_Items.getNEWCIRCUITS().getStackWithNBT(tag,0,1),GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS ,new Object[]{
//                  "DXD",
//                  "GPG",
//                  "DXD",
//                  'D', WerkstoffLoader.ArInGaPhoBiBoTe.get(OrePrefixes.dust,1),
//                  'P', BW_Meta_Items.getNEWCIRCUITS().getStackWithNBT(tag,1,1),
//                  'G', WerkstoffLoader.Prasiolite.get(OrePrefixes.gemExquisite,1),
//                  'X', WerkstoffLoader.MagnetoResonaticDust.get(OrePrefixes.dust,1)
//          });
//      }
    }
}