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

package com.github.bartimaeusnek.bartworks.system.material;

import static gregtech.api.enums.GT_Values.RA;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_ModHandler.RecipeBits;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class BW_MetaGeneratedFrames extends MetaPipeEntity {

    public final Werkstoff mMaterial;

    public BW_MetaGeneratedFrames(int aID, String aName, String aNameRegional, Werkstoff aMaterial) {
        super(aID, aName, aNameRegional, 0);
        mMaterial = aMaterial;

        GT_OreDictUnificator.registerOre(OrePrefixes.frameGt, aMaterial, getStackForm(1));
        GT_ModHandler.addCraftingRecipe(
                getStackForm(2),
                RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] {"SSS", "SwS", "SSS", 'S', mMaterial.get(OrePrefixes.stick)});
        RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial.getVarName(), 4),
                ItemList.Circuit_Integrated.getWithDamage(0, 4),
                getStackForm(1),
                64,
                8);
    }

    private BW_MetaGeneratedFrames(String aName, Werkstoff aMaterial) {
        super(aName, 0);
        mMaterial = aMaterial;
    }

    @Override
    public byte getTileEntityBaseType() {
        return 4;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new BW_MetaGeneratedFrames(this.mName, this.mMaterial);
    }

    @Override
    public ITexture[] getTexture(
            IGregTechTileEntity aBaseMetaTileEntity,
            byte aSide,
            byte aConnections,
            byte aColorIndex,
            boolean aConnected,
            boolean aRedstone) {
        return new ITexture[] {
            TextureFactory.of(
                    this.mMaterial.getTexSet().mTextures[OrePrefixes.frameGt.mTextureIndex],
                    Dyes.getModulation(aColorIndex, this.mMaterial.getRGBA()))
        };
    }

    @Override
    public String[] getDescription() {
        return new String[] {"Just something you can put a Cover or CFoam on."};
    }

    @Override
    public final boolean isSimpleMachine() {
        return true;
    }

    @Override
    public final boolean isFacingValid(byte aFacing) {
        return false;
    }

    @Override
    public final boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public final boolean renderInside(byte aSide) {
        return true;
    }

    @Override
    public final float getThickNess() {
        return 1.0F;
    }

    @Override
    public final void saveNBTData(NBTTagCompound aNBT) {
        /*Do nothing*/
    }

    @Override
    public final void loadNBTData(NBTTagCompound aNBT) {
        /*Do nothing*/
    }

    @Override
    public final boolean allowPutStack(
            IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public final boolean allowPullStack(
            IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    public int connect(byte aSide) {
        return 0;
    }

    public void disconnect(byte aSide) {
        /* Do nothing*/
    }
}
