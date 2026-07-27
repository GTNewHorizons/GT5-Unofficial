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

import bartworks.API.recipe.BartWorksRecipeMaps;
import bartworks.MainMod;
import bartworks.common.loaders.FluidLoader;
import bartworks.common.loaders.ItemRegistry;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEIBWConfig implements IConfigureNEI {

    static boolean sIsAdded = true;

    static boolean checkRecipe(ItemStack labPart, Iterable<? extends PositionedStack> stacks) {
        for (PositionedStack stack : stacks) {
            for (ItemStack item : stack.items) {
                if (labPart.getTagCompound()
                    .equals(item.getTagCompound())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void loadConfig() {
        API.hideItem(new ItemStack(ItemRegistry.TAB));
        API.hideItem(new ItemStack(FluidLoader.bioFluidBlock));

        NEIBWConfig.sIsAdded = false;
        new BioVatNEIHandler(BartWorksRecipeMaps.bacterialVatRecipes.getDefaultRecipeCategory());
        new BioLabNEIHandler(BartWorksRecipeMaps.bioLabRecipes.getDefaultRecipeCategory());
        NEIBWConfig.sIsAdded = true;
    }

    @Override
    public String getName() {
        return "BartWorks NEI Plugin";
    }

    @Override
    public String getVersion() {
        return MainMod.APIVERSION;
    }
}
