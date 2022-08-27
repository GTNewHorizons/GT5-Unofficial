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
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.github.bartimaeusnek.bartworks.common.items.LabParts;
import com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_BioVat;
import cpw.mods.fml.common.event.FMLInterModComms;
import gregtech.GT_Mod;
import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.nei.GT_NEI_DefaultHandler;
import java.awt.*;
import java.util.Collections;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class BW_NEI_BioVatHandler extends GT_NEI_DefaultHandler {

    public BW_NEI_BioVatHandler(GT_Recipe.GT_Recipe_Map aRecipeMap) {
        super(aRecipeMap);
        this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(
                new Rectangle(65, 13, 36, 18), this.getOverlayIdentifier()));
        if (!NEI_BW_Config.sIsAdded) {
            FMLInterModComms.sendRuntimeMessage(
                    GT_Values.GT,
                    "NEIPlugins",
                    "register-crafting-handler",
                    "gregtech@" + this.getRecipeName() + "@" + this.getOverlayIdentifier());
            GuiCraftingRecipe.craftinghandlers.add(this);
            GuiUsageRecipe.usagehandlers.add(this);
        }
    }

    public TemplateRecipeHandler newInstance() {
        return new BW_NEI_BioVatHandler(this.mRecipeMap);
    }

    public void drawExtras(int aRecipeIndex) {
        int base = 70;
        int[] lines = {base, base + 8, base + 16, base + 24, base + 32, base + 40, base + 48, base + 56, base + 64};
        int tEUt = ((GT_NEI_DefaultHandler.CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mEUt;
        int tDuration = ((GT_NEI_DefaultHandler.CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mDuration;
        String[] recipeDesc =
                ((GT_NEI_DefaultHandler.CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.getNeiDesc();
        int tSpecial;
        if (recipeDesc == null) {
            if (tEUt != 0) {
                drawText(
                        10,
                        lines[0],
                        this.trans("152", "Total: ") + GT_Utility.formatNumbers((long) tDuration * (long) tEUt) + " EU",
                        -16777216);
                drawText(
                        10,
                        lines[1],
                        this.trans("153", "Usage: ") + GT_Utility.formatNumbers(tEUt) + " EU/t",
                        -16777216);
                if (this.mRecipeMap.mShowVoltageAmperageInNEI) {
                    drawText(
                            10,
                            lines[2],
                            this.trans("154", "Voltage: ") + GT_Utility.formatNumbers(tEUt / this.mRecipeMap.mAmperage)
                                    + " EU",
                            -16777216);
                    drawText(
                            10,
                            lines[3],
                            this.trans("155", "Amperage: ") + GT_Utility.formatNumbers(this.mRecipeMap.mAmperage),
                            -16777216);
                } else {
                    drawText(10, lines[2], this.trans("156", "Voltage: unspecified"), -16777216);
                    drawText(10, lines[3], this.trans("157", "Amperage: unspecified"), -16777216);
                }
            }

            if (tDuration > 0) {
                drawText(
                        10,
                        lines[4],
                        this.trans("158", "Time: ")
                                + GT_Utility.formatNumbers(0.05d * tDuration)
                                + this.trans("161", " secs"),
                        -16777216);
            }

            tSpecial =
                    ((GT_NEI_DefaultHandler.CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mSpecialValue;

            int[] tSpecialA = GT_TileEntity_BioVat.specialValueUnpack(tSpecial);

            drawText(10, lines[5], StatCollector.translateToLocal("nei.biovat.0.name") + " " + tSpecialA[0], -16777216);

            if (tSpecialA[1] == -100 && GT_Mod.gregtechproxy.mLowGravProcessing) {
                drawText(10, lines[7], this.trans("159", "Needs Low Gravity"), -16777216);
            } else if (tSpecialA[1] == -200 && GT_Mod.gregtechproxy.mEnableCleanroom) {
                drawText(10, lines[7], this.trans("160", "Needs Cleanroom"), -16777216);
            } else if (tSpecialA[1] == -300 && GT_Mod.gregtechproxy.mEnableCleanroom) {
                drawText(10, lines[7], this.trans("160", "Needs Cleanroom & LowGrav"), -16777216);
            } else if (tSpecialA[1] == -400) {
                drawText(10, lines[7], this.trans("216", "Deprecated Recipe"), -16777216);
            } else if (GT_Utility.isStringValid(this.mRecipeMap.mNEISpecialValuePre)
                    || GT_Utility.isStringValid(this.mRecipeMap.mNEISpecialValuePost)) {
                drawText(
                        10,
                        lines[6],
                        (tSpecialA[2] == 1
                                        ? StatCollector.translateToLocal("nei.biovat.1.name")
                                        : StatCollector.translateToLocal("nei.biovat.2.name"))
                                + this.mRecipeMap.mNEISpecialValuePre
                                + GT_Utility.formatNumbers(tSpecialA[3] * this.mRecipeMap.mNEISpecialValueMultiplier)
                                + this.mRecipeMap.mNEISpecialValuePost,
                        -16777216);
            }
        } else {
            tSpecial = 0;

            for (String descLine : recipeDesc) {
                drawText(10, 73 + 10 * tSpecial, descLine, -16777216);
                ++tSpecial;
            }
        }
    }

    private void loadLabPartRecipes(ItemStack aResult) {
        for (CachedDefaultRecipe recipe : getCache()) {
            // dirty way of finding the special slot item
            // see constructor of CachedDefaultRecipe on why relx==120 and rely==52 means special slot
            for (PositionedStack stack : recipe.mInputs) {
                if (stack.relx == 120 && stack.rely == 52) {
                    if (NEI_BW_Config.checkRecipe(aResult, Collections.singletonList(stack))) arecipes.add(recipe);
                }
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack aResult) {
        if (aResult != null
                && aResult.getItem() instanceof LabParts
                && aResult.getItemDamage() < 3
                && aResult.getTagCompound() != null) {
            loadLabPartRecipes(aResult);
        } else {
            super.loadUsageRecipes(aResult);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack aResult) {
        if (aResult != null
                && aResult.getItem() instanceof LabParts
                && aResult.getItemDamage() < 3
                && aResult.getTagCompound() != null) {
            loadLabPartRecipes(aResult);
        } else {
            super.loadCraftingRecipes(aResult);
        }
    }
}
