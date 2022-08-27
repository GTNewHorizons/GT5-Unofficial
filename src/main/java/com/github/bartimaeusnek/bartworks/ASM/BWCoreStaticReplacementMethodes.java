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

package com.github.bartimaeusnek.bartworks.ASM;

import com.github.bartimaeusnek.bartworks.util.NonNullWrappedHashSet;
import com.github.bartimaeusnek.bartworks.util.accessprioritylist.AccessPriorityList;
import com.github.bartimaeusnek.bartworks.util.accessprioritylist.AccessPriorityListNode;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class BWCoreStaticReplacementMethodes {
    private static ThreadLocal<AccessPriorityList<IRecipe>> RECENTLYUSEDRECIPES =
            ThreadLocal.withInitial(AccessPriorityList::new);

    public static void clearRecentlyUsedRecipes() {
        // the easiest way to ensure the cache is flushed without causing synchronization overhead
        // is to just replace the whole ThreadLocal instance.
        RECENTLYUSEDRECIPES = ThreadLocal.withInitial(AccessPriorityList::new);
    }

    @SuppressWarnings("ALL")
    public static ItemStack findCachedMatchingRecipe(InventoryCrafting inventoryCrafting, World world) {
        int i = 0;
        ItemStack itemstack = null;
        ItemStack itemstack1 = null;
        int j;

        for (j = 0; j < inventoryCrafting.getSizeInventory(); ++j) {
            ItemStack itemstack2 = inventoryCrafting.getStackInSlot(j);

            if (itemstack2 != null) {
                if (i == 0) itemstack = itemstack2;

                if (i == 1) itemstack1 = itemstack2;

                ++i;
            }
        }

        if (i == 2
                && itemstack.getItem() == itemstack1.getItem()
                && itemstack.stackSize == 1
                && itemstack1.stackSize == 1
                && itemstack.getItem().isRepairable()) {
            Item item = itemstack.getItem();
            int j1 = item.getMaxDamage() - itemstack.getItemDamageForDisplay();
            int k = item.getMaxDamage() - itemstack1.getItemDamageForDisplay();
            int l = j1 + k + item.getMaxDamage() * 5 / 100;
            int i1 = item.getMaxDamage() - l;

            if (i1 < 0) i1 = 0;

            return new ItemStack(itemstack.getItem(), 1, i1);

        } else {

            IRecipe iPossibleRecipe = null;
            AccessPriorityList<IRecipe> cache = RECENTLYUSEDRECIPES.get();
            Iterator<AccessPriorityListNode<IRecipe>> it = cache.nodeIterator();

            while (it.hasNext()) {
                AccessPriorityListNode<IRecipe> recipeNode = it.next();
                iPossibleRecipe = recipeNode.getELEMENT();

                if (!iPossibleRecipe.matches(inventoryCrafting, world)) continue;

                cache.addPrioToNode(recipeNode);
                return iPossibleRecipe.getCraftingResult(inventoryCrafting);
            }

            ItemStack stack = null;

            HashSet<IRecipe> recipeSet = new NonNullWrappedHashSet<>();
            List recipeList = CraftingManager.getInstance().getRecipeList();

            for (int k = 0; k < recipeList.size(); k++) {
                IRecipe r = (IRecipe) recipeList.get(k);
                if (r.matches(inventoryCrafting, world)) recipeSet.add(r);
            }

            Object[] arr = recipeSet.toArray();

            if (arr.length == 0) return null;

            IRecipe recipe = (IRecipe) arr[0];
            stack = recipe.getCraftingResult(inventoryCrafting);

            if (arr.length != 1) return stack;

            if (stack != null) cache.addLast(recipe);

            return stack;
        }
    }

    private BWCoreStaticReplacementMethodes() {}
}
