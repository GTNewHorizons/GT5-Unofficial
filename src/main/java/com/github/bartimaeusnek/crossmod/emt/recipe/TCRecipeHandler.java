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

package com.github.bartimaeusnek.crossmod.emt.recipe;

import static com.github.bartimaeusnek.crossmod.thaumcraft.util.ThaumcraftHandler.AspectAdder;

import com.github.bartimaeusnek.bartworks.util.BWRecipes;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import com.github.bartimaeusnek.crossmod.thaumcraft.util.ThaumcraftHandler;
import com.google.common.collect.ArrayListMultimap;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

@SuppressWarnings("ALL")
public class TCRecipeHandler {
    public static final GT_Recipe.GT_Recipe_Map alchemicalConstructHandler = new TCRecipeHandler.TCRecipeMap(
            new HashSet<>(15000),
            "bwcm.recipe.alchemicalConstruct",
            "Industrical Alchemical Construct",
            null,
            "gregtech:textures/gui/basicmachines/Default",
            2,
            1,
            2,
            0,
            1,
            "",
            1,
            "",
            true,
            true);
    static Class aCrucibleRecipeClass;
    static Class aThaumcraftAPI;
    static Field craftingRecipes;
    static Field aCrucibleRecipeField;
    static Field aCrucibleRecipeCatalyst;
    static Field aspects;
    static Field key;

    static {
        try {
            aCrucibleRecipeClass = Class.forName("thaumcraft.api.crafting.CrucibleRecipe");
            aThaumcraftAPI = Class.forName("thaumcraft.api.ThaumcraftApi");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            aspects = aCrucibleRecipeClass.getField("aspects");

            key = aCrucibleRecipeClass.getField("key");

            aCrucibleRecipeField = aCrucibleRecipeClass.getDeclaredField("recipeOutput");
            aCrucibleRecipeField.setAccessible(true);

            aCrucibleRecipeCatalyst = aCrucibleRecipeClass.getField("catalyst");

            craftingRecipes = aThaumcraftAPI.getDeclaredField("craftingRecipes");
            craftingRecipes.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void init() throws IllegalAccessException, InvocationTargetException {
        ArrayList tcCraftingList = (ArrayList) craftingRecipes.get(null);
        HashSet crucibleRecipes = new HashSet();
        for (Object o : tcCraftingList) {
            if (TCRecipeHandler.aCrucibleRecipeClass.isInstance(o)) crucibleRecipes.add(o);
        }
        TCRecipeHandler.convertCrucibleRecipesToGTRecipes(crucibleRecipes);
    }

    public static void convertCrucibleRecipesToGTRecipes(HashSet crucibleRecipes)
            throws IllegalAccessException, InvocationTargetException {
        HashSet<GT_Recipe> ret = new HashSet<>();
        ArrayListMultimap<ItemStack, Integer> itemToCircuitConfigMap = ArrayListMultimap.create();
        ArrayListMultimap<ItemStack, Object> itemToAspectsMap = ArrayListMultimap.create();
        ArrayListMultimap<ItemStack, ItemStack> itemToOutputMap = ArrayListMultimap.create();
        ArrayListMultimap<ItemStack, String> itemToResearchMap = ArrayListMultimap.create();

        for (Object o : crucibleRecipes) {

            String key = (String) TCRecipeHandler.key.get(o);
            ItemStack out = (ItemStack) TCRecipeHandler.aCrucibleRecipeField.get(o);
            Object aspects = TCRecipeHandler.aspects.get(o);
            Object cat = TCRecipeHandler.aCrucibleRecipeCatalyst.get(o);

            if (cat instanceof ItemStack) {
                itemToAspectsMap.put((ItemStack) cat, aspects);
                itemToOutputMap.put((ItemStack) cat, out);
                itemToResearchMap.put((ItemStack) cat, key);
            } else if (cat instanceof String) {
                for (ItemStack stack : OreDictionary.getOres((String) cat)) {
                    itemToAspectsMap.put(stack, aspects);
                    itemToOutputMap.put(stack, out);
                    itemToResearchMap.put(stack, key);
                }
            } else if (cat instanceof ArrayList && ((ArrayList) cat).size() > 0) {
                for (ItemStack stack : ((ArrayList<ItemStack>) cat)) {
                    itemToAspectsMap.put(stack, aspects);
                    itemToOutputMap.put(stack, out);
                    itemToResearchMap.put(stack, key);
                }
            }
        }
        for (ItemStack o : itemToAspectsMap.keySet()) {
            if (o.getItemDamage() == Short.MAX_VALUE) itemToCircuitConfigMap.put(o, 24);
            else
                for (int j = 1; j <= itemToAspectsMap.get(o).size(); j++) {
                    itemToCircuitConfigMap.put(o, j % 24);
                }

            for (int j = 0; j < itemToAspectsMap.get(o).size(); j++) {
                ret.add(addRecipes(
                        itemToResearchMap.get(o).get(j),
                        itemToOutputMap.get(o).get(j),
                        itemToAspectsMap.get(o).get(j),
                        o,
                        itemToCircuitConfigMap.get(o).get(j)));
            }
        }

        for (GT_Recipe recipe : ret) {
            TCRecipeHandler.alchemicalConstructHandler.add(recipe);
        }
    }

    public static GT_Recipe addRecipes(String key, ItemStack out, Object aspects, ItemStack cat, int config)
            throws InvocationTargetException, IllegalAccessException {

        NBTTagCompound toWrite = new NBTTagCompound();
        ThaumcraftHandler.AspectAdder.writeAspectListToNBT.invoke(aspects, toWrite);
        ItemStack fake = new ItemStack(Items.feather);
        fake.setTagCompound(toWrite);
        fake.setStackDisplayName(key);
        GT_Recipe recipe = new BWRecipes.DynamicGTRecipe(
                false,
                new ItemStack[] {cat, GT_Utility.getIntegratedCircuit(config)},
                new ItemStack[] {out},
                fake,
                null,
                null,
                null,
                60,
                480,
                0);
        return recipe;
    }

    static class TCRecipeMap extends GT_Recipe.GT_Recipe_Map {
        public TCRecipeMap(
                Collection<GT_Recipe> aRecipeList,
                String aUnlocalizedName,
                String aLocalName,
                String aNEIName,
                String aNEIGUIPath,
                int aUsualInputCount,
                int aUsualOutputCount,
                int aMinimalInputItems,
                int aMinimalInputFluids,
                int aAmperage,
                String aNEISpecialValuePre,
                int aNEISpecialValueMultiplier,
                String aNEISpecialValuePost,
                boolean aShowVoltageAmperageInNEI,
                boolean aNEIAllowed) {
            super(
                    aRecipeList,
                    aUnlocalizedName,
                    aLocalName,
                    aNEIName,
                    aNEIGUIPath,
                    aUsualInputCount,
                    aUsualOutputCount,
                    aMinimalInputItems,
                    aMinimalInputFluids,
                    aAmperage,
                    aNEISpecialValuePre,
                    aNEISpecialValueMultiplier,
                    aNEISpecialValuePost,
                    aShowVoltageAmperageInNEI,
                    aNEIAllowed);
        }

        @Override
        public GT_Recipe findRecipe(
                IHasWorldObjectAndCoords aTileEntity,
                GT_Recipe aRecipe,
                boolean aNotUnificated,
                boolean aDontCheckStackSizes,
                long aVoltage,
                FluidStack[] aFluids,
                ItemStack aSpecialSlot,
                ItemStack... aInputs) {
            // No Recipes? Well, nothing to be found then.
            if (mRecipeList.isEmpty()) return null;

            // Some Recipe Classes require a certain amount of Inputs of certain kinds. Like "at least 1 Fluid + 1
            // Stack" or "at least 2 Stacks" before they start searching for Recipes.
            // This improves Performance massively, especially if people leave things like Circuits, Molds or Shapes in
            // their Machines to select Sub Recipes.
            if (GregTech_API.sPostloadFinished) {
                if (this.mMinimalInputItems > 0) {
                    if (aInputs == null) return null;
                    int tAmount = 0;
                    for (ItemStack aInput : aInputs) if (aInput != null) tAmount++;
                    if (tAmount < this.mMinimalInputItems) return null;
                }
            }

            // Unification happens here in case the Input isn't already unificated.
            if (aNotUnificated) aInputs = GT_OreDictUnificator.getStackArray(true, (Object[]) aInputs);

            //            // Check the Recipe which has been used last time in order to not have to search for it again,
            // if possible.
            //            if (aRecipe != null)
            //                if (!aRecipe.mFakeRecipe && aRecipe.mCanBeBuffered && aRecipe.isRecipeInputEqual(false,
            // aDontCheckStackSizes, aFluids, aInputs)) {
            //                    NBTTagCompound toCheckNBT = aSpecialSlot.getTagCompound();
            //                    NBTTagCompound givenNBT = ((ItemStack)aRecipe.mSpecialItems).getTagCompound();
            //                    Object aAspectListToCheck = null;
            //                    Object aGivenAspectList = null;
            //                    try {
            //                        aAspectListToCheck = AspectAdder.mAspectListClass.newInstance();
            //                        aGivenAspectList = AspectAdder.mAspectListClass.newInstance();
            //                        AspectAdder.readAspectListFromNBT.invoke(aAspectListToCheck,toCheckNBT);
            //                        AspectAdder.readAspectListFromNBT.invoke(aGivenAspectList,givenNBT);
            //                        if
            // (!TCRecipeHandler.TCRecipeMap.containsAspects(aAspectListToCheck,aGivenAspectList))
            //                            return null;
            //                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException
            // e) {
            //                        e.printStackTrace();
            //                        return null;
            //                    }
            //                    return aRecipe.mEnabled && aVoltage * mAmperage >= aRecipe.mEUt ? aRecipe : null;
            //                }

            // Now look for the Recipes inside the Item HashMaps, but only when the Recipes usually have Items.
            if (this.mUsualInputCount > 0 && aInputs != null)
                for (ItemStack tStack : aInputs)
                    if (tStack != null) {
                        Collection<GT_Recipe> tRecipes = mRecipeItemMap.get(new GT_ItemStack(tStack));
                        if (tRecipes != null)
                            for (GT_Recipe tRecipe : tRecipes)
                                if (!tRecipe.mFakeRecipe
                                        && tRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)) {
                                    NBTTagCompound toCheckNBT = aSpecialSlot.getTagCompound();
                                    NBTTagCompound givenNBT = ((ItemStack) aRecipe.mSpecialItems).getTagCompound();
                                    Object aAspectListToCheck = null;
                                    Object aGivenAspectList = null;
                                    try {
                                        aAspectListToCheck = AspectAdder.mAspectListClass.newInstance();
                                        aGivenAspectList = AspectAdder.mAspectListClass.newInstance();
                                        AspectAdder.readAspectListFromNBT.invoke(aAspectListToCheck, toCheckNBT);
                                        AspectAdder.readAspectListFromNBT.invoke(aGivenAspectList, givenNBT);
                                        if (!TCRecipeHandler.TCRecipeMap.containsAspects(
                                                aAspectListToCheck, aGivenAspectList)) continue;
                                    } catch (InstantiationException
                                            | IllegalAccessException
                                            | InvocationTargetException e) {
                                        e.printStackTrace();
                                        return null;
                                    }
                                    return tRecipe.mEnabled && aVoltage * mAmperage >= tRecipe.mEUt ? tRecipe : null;
                                }
                        tRecipes = mRecipeItemMap.get(new GT_ItemStack(GT_Utility.copyMetaData(GT_Values.W, tStack)));
                        if (tRecipes != null)
                            for (GT_Recipe tRecipe : tRecipes)
                                if (!tRecipe.mFakeRecipe
                                        && tRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)
                                        && BW_Util.areStacksEqualOrNull(
                                                (ItemStack) tRecipe.mSpecialItems, aSpecialSlot)) {
                                    NBTTagCompound toCheckNBT = aSpecialSlot.getTagCompound();
                                    NBTTagCompound givenNBT = ((ItemStack) aRecipe.mSpecialItems).getTagCompound();
                                    Object aAspectListToCheck = null;
                                    Object aGivenAspectList = null;
                                    try {
                                        aAspectListToCheck = AspectAdder.mAspectListClass.newInstance();
                                        aGivenAspectList = AspectAdder.mAspectListClass.newInstance();
                                        AspectAdder.readAspectListFromNBT.invoke(aAspectListToCheck, toCheckNBT);
                                        AspectAdder.readAspectListFromNBT.invoke(aGivenAspectList, givenNBT);
                                        if (!TCRecipeHandler.TCRecipeMap.containsAspects(
                                                aAspectListToCheck, aGivenAspectList)) continue;
                                    } catch (InstantiationException
                                            | IllegalAccessException
                                            | InvocationTargetException e) {
                                        e.printStackTrace();
                                        return null;
                                    }
                                    return tRecipe.mEnabled && aVoltage * mAmperage >= tRecipe.mEUt ? tRecipe : null;
                                }
                    }
            // And nothing has been found.
            return null;
        }

        private static boolean containsAspects(Object aAspectListToCheck, Object aGivenAspectList)
                throws InvocationTargetException, IllegalAccessException {
            Object[] aspects = (Object[]) ThaumcraftHandler.AspectAdder.getAspects.invoke(aGivenAspectList);
            for (int i = 0; i < aspects.length; i++) {
                if ((int) ThaumcraftHandler.AspectAdder.getAmount.invoke(aAspectListToCheck, aspects[i])
                        < (int) ThaumcraftHandler.AspectAdder.getAmount.invoke(aGivenAspectList, aspects[i]))
                    return false;
            }
            return true;
        }
    }
}
