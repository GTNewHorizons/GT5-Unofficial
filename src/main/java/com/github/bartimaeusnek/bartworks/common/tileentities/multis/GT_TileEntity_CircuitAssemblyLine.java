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

import com.github.bartimaeusnek.bartworks.system.material.CircuitGeneration.BW_Meta_Items;
import com.github.bartimaeusnek.bartworks.system.material.CircuitGeneration.CircuitImprintLoader;
import com.github.bartimaeusnek.bartworks.util.BWRecipes;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import com.github.bartimaeusnek.bartworks.util.ChatColorHelper;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Collection;
import java.util.HashSet;

public class GT_TileEntity_CircuitAssemblyLine extends GT_MetaTileEntity_MultiBlockBase {

    public String getTypeForDisplay() {
        if (this.type.equals(new NBTTagCompound()))
            return "";
        return GT_LanguageManager.getTranslation(GT_LanguageManager.getTranslateableItemStackName(CircuitImprintLoader.getStackFromTag(this.type)));
    }

    private NBTTagCompound type = new NBTTagCompound();
    private GT_Recipe bufferedRecipe;

    public GT_TileEntity_CircuitAssemblyLine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_CircuitAssemblyLine(String aName) {
        super(aName);
    }

    private boolean imprintMachine(ItemStack itemStack){
        if (!this.type.equals(new NBTTagCompound()))
            return true;
        if (!GT_Utility.isStackValid(itemStack))
            return false;
        if (itemStack.getItem() instanceof BW_Meta_Items.BW_GT_MetaGenCircuits && itemStack.getItemDamage() == 0 && itemStack.getTagCompound() != null && this.type.equals(new NBTTagCompound())){
            this.type = itemStack.getTagCompound();
            this.mInventory[1] = null;
            this.getBaseMetaTileEntity().issueBlockUpdate();
            return true;
        }
        return false;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack itemStack) {
        return true;
    }

    @Override
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        if (aIndex == 20) {
            GT_Utility.doSoundAtClient(GregTech_API.sSoundList.get(212), 10, 1.0F, aX, aY, aZ);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        this.type = aNBT.getCompoundTag("Type");
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        if (!this.type.equals(new NBTTagCompound()))
            aNBT.setTag("Type", this.type);
        super.saveNBTData(aNBT);

    }

    private final Collection<GT_Recipe> GT_RECIPE_COLLECTION = new HashSet<>();

    @Override
    public boolean checkRecipe(ItemStack itemStack) {
        if (this.type.equals(new NBTTagCompound()))
            if (!this.imprintMachine(itemStack))
                return false;

        if (this.bufferedRecipe != null && this.bufferedRecipe.isRecipeInputEqual(true,false, BW_Util.getFluidsFromInputHatches(this), BW_Util.getItemsFromInputBusses(this))) {
            BW_Util.calculateOverclockedNessMulti(this.bufferedRecipe.mEUt,this.bufferedRecipe.mDuration,1,this.getMaxInputVoltage(),this);
            this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
            this.mOutputItems = this.bufferedRecipe.mOutputs;
            this.mOutputFluids = this.bufferedRecipe.mFluidOutputs;
            this.updateSlots();
            return true;
        }

        ItemStack stack = ItemStack.loadItemStackFromNBT(this.type);

        if (stack == null)
            return false;

        if (this.GT_RECIPE_COLLECTION.isEmpty()) {
            for (GT_Recipe recipe : BWRecipes.instance.getMappingsFor((byte) 3).mRecipeList) {
                if (GT_Utility.areStacksEqual(recipe.mOutputs[0], stack, true)) {
                    this.GT_RECIPE_COLLECTION.add(recipe);
                }
            }
        }

        for (GT_Recipe recipe : this.GT_RECIPE_COLLECTION) {
            if (recipe.isRecipeInputEqual(true,false, BW_Util.getFluidsFromInputHatches(this), BW_Util.getItemsFromInputBusses(this)))
                this.bufferedRecipe = recipe;
            else
                continue;

            BW_Util.calculateOverclockedNessMulti(this.bufferedRecipe.mEUt,this.bufferedRecipe.mDuration,1,this.getMaxInputVoltage(),this);
            this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
            this.mOutputItems = this.bufferedRecipe.mOutputs;
            this.mOutputFluids = this.bufferedRecipe.mFluidOutputs;
            this.updateSlots();
            return true;
        }
        return false;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
        int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
        int r;
        int i;
        IGregTechTileEntity tTileEntity;
        if (xDir != 0) {
            for(r = 0; r <= 7; ++r) {
                i = r * xDir;

                tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir, 0, i);
                if (!this.addEnergyInputToMachineList(tTileEntity, 16))
                    if ((aBaseMetaTileEntity.getBlockOffset(0, 0, i) != GregTech_API.sBlockCasings3 || aBaseMetaTileEntity.getMetaIDOffset(0, 0, i) != 10) && r == 1)
                        return false;
                if (!aBaseMetaTileEntity.getBlockOffset(0, -1, i).getUnlocalizedName().equals("blockAlloyGlass"))
                    return false;

                tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(0, -2, i);
                if (!this.addMaintenanceToMachineList(tTileEntity, 16) && !this.addInputToMachineList(tTileEntity, 16))
                    if (aBaseMetaTileEntity.getBlockOffset(0, -2, i) != GregTech_API.sBlockCasings2 || aBaseMetaTileEntity.getMetaIDOffset(0, -2, i) != 0)
                        return false;

                if (i != 0 && (aBaseMetaTileEntity.getBlockOffset(xDir, -1, i) != GregTech_API.sBlockCasings2 || aBaseMetaTileEntity.getMetaIDOffset(xDir, -1, i) != 5))
                    return false;

                if (!aBaseMetaTileEntity.getBlockOffset(xDir * 2, -1, i).getUnlocalizedName().equals("blockAlloyGlass"))
                    return false;

                tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir * 2, -2, i);
                if (!this.addMaintenanceToMachineList(tTileEntity, 16) && !this.addInputToMachineList(tTileEntity, 16))
                    if (aBaseMetaTileEntity.getBlockOffset(xDir * 2, -2, i) != GregTech_API.sBlockCasings2 || aBaseMetaTileEntity.getMetaIDOffset(xDir * 2, -2, i) != 0)
                        return false;

                tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir, -2, i);
                if (!this.addInputToMachineList(tTileEntity, 16) && this.addOutputToMachineList(tTileEntity, 16))
                    return r > 0 && this.mEnergyHatches.size() > 0;

            }
        } else {

            for(r = 0; r <= 7; ++r) {
                i = r * -zDir;

                //top with grate and energy hatch
                tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(i, 0, zDir);
                if (!this.addEnergyInputToMachineList(tTileEntity, 16))
                    if ((aBaseMetaTileEntity.getBlockOffset(i, 0, 0) != GregTech_API.sBlockCasings3 || aBaseMetaTileEntity.getMetaIDOffset(i, 0, 0) != 10) && r == 1)
                        return false;

                if (!aBaseMetaTileEntity.getBlockOffset(i, -1, 0).getUnlocalizedName().equals("blockAlloyGlass"))
                    return false;

                tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(i, -2, 0);
                if (!this.addMaintenanceToMachineList(tTileEntity, 16) && !this.addInputToMachineList(tTileEntity, 16))
                    if (aBaseMetaTileEntity.getBlockOffset(i, -2, 0) != GregTech_API.sBlockCasings2 || aBaseMetaTileEntity.getMetaIDOffset(i, -2, 0) != 0)
                        return false;

                if (i != 0 && (aBaseMetaTileEntity.getBlockOffset(i, -1, zDir) != GregTech_API.sBlockCasings2 || aBaseMetaTileEntity.getMetaIDOffset(i, -1, zDir) != 5))
                    return false;

                if (!aBaseMetaTileEntity.getBlockOffset(i, -1, zDir * 2).getUnlocalizedName().equals("blockAlloyGlass"))
                    return false;


                tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(i, -2, zDir * 2);
                if (!this.addMaintenanceToMachineList(tTileEntity, 16) && !this.addInputToMachineList(tTileEntity, 16))
                    if (aBaseMetaTileEntity.getBlockOffset(i, -2, zDir * 2) != GregTech_API.sBlockCasings2 || aBaseMetaTileEntity.getMetaIDOffset(i, -2, zDir * 2) != 0)
                        return false;

                tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(i, -2, zDir);
                if (!this.addInputToMachineList(tTileEntity, 16) && this.addOutputToMachineList(tTileEntity, 16))
                    return r > 0 && this.mEnergyHatches.size() == 1;
            }
        }

        return false;
    }

    @Override
    public boolean addInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
                ((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
                ((GT_MetaTileEntity_Hatch_Input)aMetaTileEntity).mRecipeMap = this.getRecipeMap();
                return this.mInputHatches.add((GT_MetaTileEntity_Hatch_Input)aMetaTileEntity);
            } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus && ((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity).mTier == 0) {
                ((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
                ((GT_MetaTileEntity_Hatch_InputBus)aMetaTileEntity).mRecipeMap = this.getRecipeMap();
                return this.mInputBusses.add((GT_MetaTileEntity_Hatch_InputBus)aMetaTileEntity);
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean addOutputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus && ((GT_MetaTileEntity_Hatch_OutputBus) aMetaTileEntity).mTier == 0) {
                ((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.mOutputBusses.add((GT_MetaTileEntity_Hatch_OutputBus)aMetaTileEntity);
            } else {
                return false;
            }
        }
    }

    @Override
    public int getMaxEfficiency(ItemStack itemStack) {
        return 10000;
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
        return new String[]{
                "Circuit Assembly Line", "Size(WxHxD): (2-7)x3x3, variable length",
                "Bottom: Steel Machine Casing(or 1x Maintenance or Input Hatch),",
                "ULV Input Bus (Last ULV Output Bus), Steel Machine Casing",
                "Middle: Reinforced Glass, Assembling Line Casing, Reinforced Glass",
                "Top: Grate Machine Casing (or Controller or 1x Energy Hatch)",
                "Up to 7 repeating slices, last is Output Bus",
                "Imprint this machine with a Circuit Imprint,",
                "by putting the imprint in the controller.",
                "Every Circuit Assembly Line can only be imprinted ONCE.",
                StatCollector.translateToLocal("tooltip.bw.1.name") + ChatColorHelper.DARKGREEN + " BartWorks"
        };
    }

    @Override
    public String[] getInfoData() {
        String[] ret = new String[super.getInfoData().length+1];
        System.arraycopy(super.getInfoData(),0,ret,0,super.getInfoData().length);
        ret[super.getInfoData().length] = "Imprinted with: "+ GT_LanguageManager.getTranslation(GT_LanguageManager.getTranslateableItemStackName(CircuitImprintLoader.getStackFromTag(this.type)));
        return ret;
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        return aSide == aFacing ? new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[16], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE)} : new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[16]};
    }
}
