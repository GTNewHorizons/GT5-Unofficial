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

import com.github.bartimaeusnek.bartworks.util.BWRecipes;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import static gregtech.api.enums.ItemList.*;

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

    public static void makeCircuitParts() {
        ItemList[] itemLists = values();
        for (ItemList single : itemLists) {
            if (!single.hasBeenSet())
                continue;
            if (
                    single.toString().contains("Wafer") ||
                    single.toString().contains("Circuit_Silicon_Ingot") ||
                    single.toString().contains("Raw") ||
                    single.toString().contains("raw") ||
                    single.toString().contains("Glass_Tube") ||
                    single == Circuit_Parts_GlassFiber ||
                    single == Circuit_Parts_Advanced ||
                    single == Circuit_Parts_Wiring_Advanced ||
                    single == Circuit_Parts_Wiring_Elite ||
                    single == Circuit_Parts_Wiring_Basic ||
                    single == Circuit_Integrated ||
                    single == Circuit_Parts_PetriDish ||
                    single == Circuit_Parts_Vacuum_Tube ||
                    single == Circuit_Integrated_Good ||
                    single == Circuit_Parts_Capacitor ||
                    single == Circuit_Parts_Diode ||
                    single == Circuit_Parts_Resistor ||
                    single == Circuit_Parts_Transistor

            ){
                continue;
            }
            ItemStack itemStack = single.get(1);
            if (!GT_Utility.isStackValid(itemStack))
                continue;
            int[] oreIDS = OreDictionary.getOreIDs(itemStack);
            if (oreIDS.length > 1)
                continue;
            String name = null;
            if (oreIDS.length == 1)
                name = OreDictionary.getOreName(oreIDS[0]);
            if ((name == null || name.isEmpty()) && (single.toString().contains("Circuit") || single.toString().contains("circuit") || single.toString().contains("board")) && single.getBlock() == Blocks.air) {
                ArrayList<String> toolTip = new ArrayList<>();
                single.getItem().addInformation(single.get(1).copy(), null, toolTip, true);
                String tt = (toolTip.size() > 0 ? toolTip.get(0) : "");
              //  tt += "Internal Name = "+single;
                String localised = GT_LanguageManager.getTranslation(GT_LanguageManager.getTranslateableItemStackName(itemStack));
                BW_Meta_Items.getNEWCIRCUITS().addItem(CircuitImprintLoader.reverseIDs, "Wrap of " + localised+"s", tt);
                GT_Values.RA.addAssemblerRecipe(new ItemStack[]{single.get(16).copy()},Materials.Plastic.getMolten(576),BW_Meta_Items.getNEWCIRCUITS().getStack(CircuitImprintLoader.reverseIDs),600,30);
                CircuitImprintLoader.circuitIIconRefs.put(CircuitImprintLoader.reverseIDs,single);
                CircuitImprintLoader.reverseIDs--;
            }
        }
    }

    @Override
    public void run() {
        Iterator<GT_Recipe> it = GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes.mRecipeList.iterator();
        GT_Recipe circuitRecipe;
        HashSet<GT_Recipe> torem = new HashSet<>();
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
                CircuitImprintLoader.recipeTagMap.put(CircuitImprintLoader.getTagFromStack(outputs[0]),circuitRecipe.copy());
                for (ItemStack s : circuitRecipe.mInputs){
                    if (circuitRecipe.mFluidInputs[0].isFluidEqual(Materials.SolderingAlloy.getMolten(0)))
                        CircuitImprintLoader.refs.add(CircuitImprintLoader.getTagFromStack(s.copy().splitStack(1)));
                }
                if (circuitRecipe.mFluidInputs[0].isFluidEqual(Materials.SolderingAlloy.getMolten(0)))
                    BWRecipes.instance.getMappingsFor(BWRecipes.CIRCUITASSEMBLYLINE).addRecipe(CircuitImprintLoader.reBuildRecipe(circuitRecipe));
//                if (circuitRecipe.mEUt > BW_Util.getTierVoltage(5)) {
//                    torem.add(circuitRecipe);
//                }
            }
        }
        GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes.mRecipeList.removeAll(torem);
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
               in[i] = original.mInputs[i].copy();
               in[i].stackSize *= 16;
//               if (in[i].stackSize > 64)
//                   return null;
           }
           } catch (ArrayIndexOutOfBoundsException e){
               break;
           } catch (NullPointerException e){
           }
       }
       return new BWRecipes.DynamicGTRecipe(false,in,new ItemStack[]{out},null,null, original.mFluidInputs,null,original.mDuration,original.mEUt,original.mSpecialValue);
    }

}
