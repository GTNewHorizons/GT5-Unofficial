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

package com.github.bartimaeusnek.bartworks.common.tileentities.multis;

import com.github.bartimaeusnek.bartworks.util.BW_Util;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.GT_Recipe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Collection;

public class GT_TileEntity_CircuitAssemblyLine extends GT_MetaTileEntity_MultiBlockBase {

    private NBTTagCompound type;
    private GT_Recipe bufferedRecipe = null;

    public GT_TileEntity_CircuitAssemblyLine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_CircuitAssemblyLine(String aName) {
        super(aName);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack itemStack) {

        return false;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        type = aNBT.getCompoundTag("Type");
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setTag("Type",type);
    }

    @Override
    public boolean checkRecipe(ItemStack itemStack) {
        if (type == null)
            return false;

        if (bufferedRecipe != null && bufferedRecipe.isRecipeInputEqual(false,true,BW_Util.getFluidsFromInputHatches(this),BW_Util.getItemsFromInputBusses(this)))
            return true;

        ItemStack stack = ItemStack.loadItemStackFromNBT(type);

        if (stack == null)
            return false;

        Collection<GT_Recipe> recipes = null;

        for (GT_ItemStack GTitemstack : GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes.mRecipeItemMap.keySet()){
            if (GTitemstack.mItem.equals(stack.getItem()) && GTitemstack.mMetaData == (short) stack.getItemDamage()){
                recipes = GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes.mRecipeItemMap.get(GTitemstack);
                break;
            }
        }

        if (recipes == null || recipes.isEmpty())
            return false;

        for (GT_Recipe recipe : recipes){
            if (recipe.isRecipeInputEqual(false,true,BW_Util.getFluidsFromInputHatches(this),BW_Util.getItemsFromInputBusses(this)))
                bufferedRecipe = recipe;
        }



        return true;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return false;
    }

    @Override
    public int getMaxEfficiency(ItemStack itemStack) {
        return 0;
    }

    @Override
    public int getPollutionPerTick(ItemStack itemStack) {
        return 0;
    }

    @Override
    public int getDamageToComponent(ItemStack itemStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack itemStack) {
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_TileEntity_CircuitAssemblyLine(this.mName);
    }

    @Override
    public String[] getDescription() {
        return new String[0];
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity iGregTechTileEntity, byte b, byte b1, byte b2, boolean b3, boolean b4) {
        return new ITexture[0];
    }
}
