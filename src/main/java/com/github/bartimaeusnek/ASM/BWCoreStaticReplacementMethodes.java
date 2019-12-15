/*
 * Copyright (c) 2018-2019 bartimaeusnek
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

package com.github.bartimaeusnek.ASM;

import com.github.bartimaeusnek.bartworks.util.accessprioritylist.AccessPriorityList;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.Optional;

public class BWCoreStaticReplacementMethodes {

    public static final AccessPriorityList<IRecipe> RECENTLYUSEDRECIPES = new AccessPriorityList<>();

    @SuppressWarnings("ALL")
    public static ItemStack findCachedMatchingRecipe(InventoryCrafting inventoryCrafting, World world) {
        int i = 0;
        ItemStack itemstack = null;
        ItemStack itemstack1 = null;
        int j;

        for (j = 0; j < inventoryCrafting.getSizeInventory(); ++j)
        {
            ItemStack itemstack2 = inventoryCrafting.getStackInSlot(j);

            if (itemstack2 != null)
            {
                if (i == 0)
                {
                    itemstack = itemstack2;
                }

                if (i == 1)
                {
                    itemstack1 = itemstack2;
                }

                ++i;
            }
        }

        if (i == 2 && itemstack.getItem() == itemstack1.getItem() && itemstack.stackSize == 1 && itemstack1.stackSize == 1 && itemstack.getItem().isRepairable())
        {
            Item item = itemstack.getItem();
            int j1 = item.getMaxDamage() - itemstack.getItemDamageForDisplay();
            int k = item.getMaxDamage() - itemstack1.getItemDamageForDisplay();
            int l = j1 + k + item.getMaxDamage() * 5 / 100;
            int i1 = item.getMaxDamage() - l;

            if (i1 < 0)
            {
                i1 = 0;
            }

            return new ItemStack(itemstack.getItem(), 1, i1);
        } else {
            Optional<IRecipe> iPossibleRecipe = Optional.empty();
            int index = 0;
            for (Iterator<IRecipe> it = RECENTLYUSEDRECIPES.iterator(); it.hasNext();++index) {
                IRecipe RECENTLYUSEDRECIPE = it.next();
                if (RECENTLYUSEDRECIPE.matches(inventoryCrafting, world)) {
                    iPossibleRecipe = Optional.of(RECENTLYUSEDRECIPE);
                    break;
                }
            }

            if (iPossibleRecipe.isPresent()) {
                RECENTLYUSEDRECIPES.addPrioToNode(index);
                return iPossibleRecipe.get().getCraftingResult(inventoryCrafting);
            }

            iPossibleRecipe = CraftingManager.getInstance().getRecipeList().stream().filter(r -> ((IRecipe)r).matches(inventoryCrafting, world)).findFirst();
            ItemStack stack = iPossibleRecipe.map(iRecipe -> iRecipe.getCraftingResult(inventoryCrafting)).orElse(null);
            if (stack != null)
                RECENTLYUSEDRECIPES.addLast(iPossibleRecipe.get());
            return stack;
        }
    }

    private BWCoreStaticReplacementMethodes() {
    }

}
