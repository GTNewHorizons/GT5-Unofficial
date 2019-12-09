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

package com.github.bartimaeusnek.bartworks.neiHandler;

import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.github.bartimaeusnek.bartworks.common.items.LabParts;
import cpw.mods.fml.common.event.FMLInterModComms;
import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Recipe;
import gregtech.nei.GT_NEI_DefaultHandler;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class BW_NEI_BioLabHandler extends GT_NEI_DefaultHandler {
    public BW_NEI_BioLabHandler(GT_Recipe.GT_Recipe_Map aRecipeMap) {
        super(aRecipeMap);
        this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(65, 13, 36, 18), this.getOverlayIdentifier()));
        if (!NEI_BW_Config.sIsAdded) {
            FMLInterModComms.sendRuntimeMessage(GT_Values.GT, "NEIPlugins", "register-crafting-handler", "gregtech@" + this.getRecipeName() + "@" + this.getOverlayIdentifier());
            GuiCraftingRecipe.craftinghandlers.add(this);
            GuiUsageRecipe.usagehandlers.add(this);
        }
    }

    public TemplateRecipeHandler newInstance() {
        return new BW_NEI_BioLabHandler(this.mRecipeMap);
    }

    public void loadCraftingRecipes(ItemStack aResult) {
        if (aResult != null && aResult.getItem() instanceof LabParts && aResult.getItemDamage() < 3) {
            for (GT_Recipe recipe : this.getSortedRecipes()) {
                if (aResult.getTagCompound() != null && recipe != null)
                    for (int i = 0; i < recipe.mOutputs.length; i++) {
                        if (recipe.mOutputs[i] != null)
                            if (aResult.getTagCompound().equals(recipe.mOutputs[i].getTagCompound())) {
                                this.arecipes.add(new CachedDefaultRecipe(recipe));
                                break;
                            }

                    }
            }
        } else
            super.loadCraftingRecipes(aResult);
    }

    @Override
    public void loadUsageRecipes(ItemStack aResult) {
        if (aResult != null && aResult.getItem() instanceof LabParts && aResult.getItemDamage() < 3) {
            for (GT_Recipe recipe : this.getSortedRecipes()) {
                if (aResult.getTagCompound() != null && recipe != null)
                    for (int i = 0; i < recipe.mInputs.length; i++) {
                        if (recipe.mInputs[i] != null)
                            if (aResult.getTagCompound().equals(recipe.mInputs[i].getTagCompound())) {
                                this.arecipes.add(new CachedDefaultRecipe(recipe));
                                break;
                            }

                    }
            }
        } else
            super.loadCraftingRecipes(aResult);
    }
}
