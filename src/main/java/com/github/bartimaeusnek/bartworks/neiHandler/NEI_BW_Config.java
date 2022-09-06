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

package com.github.bartimaeusnek.bartworks.neiHandler;

import codechicken.nei.PositionedStack;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.common.loaders.FluidLoader;
import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_HTGR;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.bartworks.util.BWRecipes;
import gregtech.api.enums.OrePrefixes;
import net.minecraft.item.ItemStack;

public class NEI_BW_Config implements IConfigureNEI {

    static boolean sIsAdded = true;

    static boolean checkRecipe(ItemStack labPart, Iterable<? extends PositionedStack> stacks) {
        for (PositionedStack stack : stacks) {
            for (ItemStack item : stack.items) {
                if (labPart.getTagCompound().equals(item.getTagCompound())) {
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
        API.hideItem(new ItemStack(ItemRegistry.bw_fake_glasses));
        ItemStack[] prefixesToHide = {
            WerkstoffLoader.getCorrespondingItemStack(OrePrefixes.dustTiny, WerkstoffLoader.Bismutite)
                    .copy(),
            WerkstoffLoader.getCorrespondingItemStack(OrePrefixes.dustSmall, WerkstoffLoader.Bismutite)
                    .copy(),
            WerkstoffLoader.getCorrespondingItemStack(OrePrefixes.crushed, WerkstoffLoader.Bismutite)
                    .copy(),
            WerkstoffLoader.getCorrespondingItemStack(OrePrefixes.crushedPurified, WerkstoffLoader.Bismutite)
                    .copy(),
            WerkstoffLoader.getCorrespondingItemStack(OrePrefixes.crushedCentrifuged, WerkstoffLoader.Bismutite)
                    .copy(),
            WerkstoffLoader.getCorrespondingItemStack(OrePrefixes.nugget, WerkstoffLoader.Bismutite)
                    .copy(),
            WerkstoffLoader.getCorrespondingItemStack(OrePrefixes.gemChipped, WerkstoffLoader.Bismutite)
                    .copy(),
            WerkstoffLoader.getCorrespondingItemStack(OrePrefixes.gemFlawed, WerkstoffLoader.Bismutite)
                    .copy(),
            WerkstoffLoader.getCorrespondingItemStack(OrePrefixes.gemFlawless, WerkstoffLoader.Bismutite)
                    .copy(),
            WerkstoffLoader.getCorrespondingItemStack(OrePrefixes.gemExquisite, WerkstoffLoader.Bismutite)
                    .copy(),
            WerkstoffLoader.getCorrespondingItemStack(OrePrefixes.dustImpure, WerkstoffLoader.Bismutite)
                    .copy(),
            WerkstoffLoader.getCorrespondingItemStack(OrePrefixes.dustPure, WerkstoffLoader.Bismutite)
                    .copy(),
        };
        for (ItemStack stack : prefixesToHide) {
            stack.setItemDamage(Short.MAX_VALUE);
            API.hideItem(stack);
        }
        NEI_BW_Config.sIsAdded = false;
        new BW_NEI_OreHandler();
        new BW_NEI_BioVatHandler(BWRecipes.instance.getMappingsFor(BWRecipes.BACTERIALVATBYTE));
        new BW_NEI_BioLabHandler(BWRecipes.instance.getMappingsFor(BWRecipes.BIOLABBYTE));
        new BW_NEI_RadHatchHandler(BWRecipes.instance.getMappingsFor(BWRecipes.RADHATCH));
        new BW_NEI_HTGRHandler(GT_TileEntity_HTGR.fakeRecipeMap);
        NEI_BW_Config.sIsAdded = true;
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
