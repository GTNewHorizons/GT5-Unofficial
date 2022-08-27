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

package com.github.bartimaeusnek.crossmod.emt.tileentities.multi;

import static gregtech.api.enums.GT_Values.V;

import com.github.bartimaeusnek.crossmod.emt.recipe.TCRecipeHandler;
import com.github.bartimaeusnek.crossmod.emt.util.EMTHandler;
import com.github.bartimaeusnek.crossmod.thaumcraft.util.ThaumcraftHandler;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

@SuppressWarnings("ALL")
public class GT_Industrial_Alchemic_Construct extends GT_MetaTileEntity_MultiBlockBase {

    private List<Object> mEssentiaHatches = new ArrayList<>();

    public boolean addInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity.getClass().isInstance(EMTHandler.aEssentiaInputHatch))
            return this.addEssetiaHatchToList(aTileEntity, aBaseCasingIndex);
        return super.addInputToMachineList(aTileEntity, aBaseCasingIndex);
    }

    private boolean addEssetiaHatchToList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity == null) {
                return false;
            } else if (aTileEntity.getClass().isInstance(EMTHandler.aEssentiaInputHatch)) {
                ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.mEssentiaHatches.add(aMetaTileEntity);
            } else {
                return false;
            }
        }
    }

    public GT_Industrial_Alchemic_Construct(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    private GT_Industrial_Alchemic_Construct(String aName) {
        super(aName);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean checkRecipe(ItemStack itemStack) {
        ItemStack stack = new ItemStack(Items.feather);
        String owner = this.getBaseMetaTileEntity().getOwnerName();
        Object allAspects = null;
        try {
            allAspects = ThaumcraftHandler.AspectAdder.mAspectListClass.newInstance();
            for (Object o : this.mEssentiaHatches) {
                Object aspectList = EMTHandler.aAspectField.get(o);
                ThaumcraftHandler.AspectAdder.add.invoke(allAspects, aspectList);
            }
            NBTTagCompound toWrite = (NBTTagCompound)
                    ThaumcraftHandler.AspectAdder.writeAspectListToNBT.invoke(allAspects, new NBTTagCompound());
            stack.setTagCompound(toWrite);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
        ItemStack[] tInputs = this.getStoredInputs().toArray(new ItemStack[0]);
        ItemStack outputItems = null;

        long tVoltage = this.getMaxInputVoltage();
        byte tTier = (byte) Math.max(1, Math.min(GT_Utility.getTier(tVoltage), V.length - 1));
        GT_Recipe tRecipe = TCRecipeHandler.alchemicalConstructHandler.findRecipe(
                this.getBaseMetaTileEntity(), null, false, false, V[tTier], null, stack, tInputs);
        ItemStack helper = (ItemStack) tRecipe.mSpecialItems;
        NBTTagCompound tagCompound = helper.getTagCompound();
        String research = tagCompound.getCompoundTag("display").getString("Name");
        Object aspectList = null;
        try {
            aspectList = ThaumcraftHandler.AspectAdder.mAspectListClass.newInstance();
            ThaumcraftHandler.AspectAdder.readAspectListFromNBT.invoke(aspectList, tagCompound);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
            return false;
        }
        boolean complete = false;
        try {
            complete = (boolean) ThaumcraftHandler.AspectAdder.isResearchComplete.invoke(null, owner, research);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        if (!complete) return false;
        if (!tRecipe.isRecipeInputEqual(true, new FluidStack[0], tInputs)) return false;
        LinkedHashMap<Object, Integer> list = null;
        LinkedHashMap<Object, Integer> needed = null;
        try {
            list = (LinkedHashMap) ThaumcraftHandler.AspectAdder.linkedAspektList.get(allAspects);
            needed = (LinkedHashMap) ThaumcraftHandler.AspectAdder.linkedAspektList.get(aspectList);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
        assert list != null;
        assert needed != null;
        for (Map.Entry<Object, Integer> toTake : needed.entrySet()) {
            list.replace(toTake.getKey(), list.get(toTake.getKey()) - toTake.getValue());
        }
        this.addOutput(tRecipe.mOutputs[0]);
        this.updateSlots();
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
        return new GT_Industrial_Alchemic_Construct(mName);
    }

    @Override
    public String[] getDescription() {
        return new String[0];
    }

    @Override
    public ITexture[] getTexture(
            IGregTechTileEntity iGregTechTileEntity, byte b, byte b1, byte b2, boolean b3, boolean b4) {
        return new ITexture[0];
    }
}
