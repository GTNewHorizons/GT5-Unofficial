/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package bartworks.neiHandler;

import net.minecraft.item.ItemStack;

import bartworks.common.items.ItemLabParts;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.common.event.FMLInterModComms;
import gregtech.api.enums.GTValues;
import gregtech.api.recipe.RecipeCategory;
import gregtech.nei.GTNEIDefaultHandler;

public class BioLabNEIHandler extends GTNEIDefaultHandler {

    public BioLabNEIHandler(RecipeCategory recipeCategory) {
        super(recipeCategory);
        if (!BWNEIConfig.sIsAdded) {
            FMLInterModComms.sendRuntimeMessage(
                GTValues.GT,
                "NEIPlugins",
                "register-crafting-handler",
                "gregtech@" + this.getRecipeName() + "@" + this.getOverlayIdentifier());
            GuiCraftingRecipe.craftinghandlers.add(this);
            GuiUsageRecipe.usagehandlers.add(this);
        }
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        return new BioLabNEIHandler(this.recipeCategory);
    }

    @Override
    public void loadCraftingRecipes(ItemStack aResult) {
        if (aResult != null && aResult.getItem() instanceof ItemLabParts
            && aResult.getItemDamage() < 3
            && aResult.getTagCompound() != null) {
            for (CachedDefaultRecipe recipe : this.getCache())
                if (BWNEIConfig.checkRecipe(aResult, recipe.mOutputs)) this.arecipes.add(recipe);
        } else {
            super.loadCraftingRecipes(aResult);
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack aResult) {
        if (aResult != null && aResult.getItem() instanceof ItemLabParts
            && aResult.getItemDamage() < 3
            && aResult.getTagCompound() != null) {
            for (CachedDefaultRecipe recipe : this.getCache())
                if (BWNEIConfig.checkRecipe(aResult, recipe.mInputs)) this.arecipes.add(recipe);
        } else {
            super.loadUsageRecipes(aResult);
        }
    }
}
