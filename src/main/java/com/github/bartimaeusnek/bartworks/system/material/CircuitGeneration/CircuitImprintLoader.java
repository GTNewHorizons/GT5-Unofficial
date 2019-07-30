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
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import com.github.bartimaeusnek.bartworks.util.Pair;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashBiMap;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.items.GT_MetaGenerated_Item;
import gregtech.api.items.GT_MetaGenerated_Item_X32;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

public class CircuitImprintLoader implements Runnable {

    static final ArrayListMultimap<NBTTagCompound,GT_Recipe> recipeTagMap = ArrayListMultimap.create();
    static final HashBiMap<CircuitData,NBTTagCompound> bwCircuitTagMap = HashBiMap.create(20);
    static final HashSet<NBTTagCompound> refs = new HashSet<>();
    private static short reverseIDs = 32766;

    public static NBTTagCompound getTagFromStack(ItemStack stack){
        if (GT_Utility.isStackValid(stack))
            return stack.copy().splitStack(1).writeToNBT(new NBTTagCompound());
        return new NBTTagCompound();
    }

    public static ItemStack getStackFromTag(NBTTagCompound tagCompound){
        return ItemStack.loadItemStackFromNBT(tagCompound);
    }

    public static void makeCircuitParts() {
        ItemList[] itemLists = ItemList.values();
        for (ItemList single : itemLists) {
            if (!single.hasBeenSet())
                continue;
            ItemStack itemStack = single.get(1);
            if (!GT_Utility.isStackValid(itemStack))
                continue;
            int[] oreIDS = OreDictionary.getOreIDs(itemStack);
            if (oreIDS.length > 1)
                continue;
            String name = null;
            if (oreIDS.length == 1)
                name = OreDictionary.getOreName(oreIDS[0]);
            if ((name == null || name.isEmpty()) && (single.toString().contains("Circuit") || single.toString().contains("circuit")) && single.getBlock() == Blocks.air) {
                ArrayList<String> toolTip = new ArrayList<>();
                single.getItem().addInformation(single.get(1).copy(), null, toolTip, true);
                String localised = GT_LanguageManager.getTranslation(GT_LanguageManager.getTranslateableItemStackName(itemStack));
                BW_Meta_Items.getNEWCIRCUITS().addItem(CircuitImprintLoader.reverseIDs, "Wrap of " + localised, toolTip.size() > 0 ? toolTip.get(0) : "");
                GT_Values.RA.addAssemblerRecipe(new ItemStack[]{single.get(16).copy()},Materials.Plastic.getMolten(576),BW_Meta_Items.getNEWCIRCUITS().getStack(CircuitImprintLoader.reverseIDs),600,30);
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
//              if (circuitRecipe.mEUt > BW_Util.getTierVoltage(5))
                if (circuitRecipe.mFluidInputs[0].isFluidEqual(Materials.SolderingAlloy.getMolten(0))) {
                    torem.add(circuitRecipe);
                    BWRecipes.instance.getMappingsFor(BWRecipes.CIRCUITASSEMBLYLINE).addRecipe(circuitRecipe);
                }
            }
        }
        GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes.mRecipeList.removeAll(torem);
    }

}
