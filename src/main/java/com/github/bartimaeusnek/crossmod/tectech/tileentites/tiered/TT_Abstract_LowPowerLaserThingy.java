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

package com.github.bartimaeusnek.crossmod.tectech.tileentites.tiered;

import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import java.util.Optional;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TT_Abstract_LowPowerLaserThingy extends GT_MetaTileEntity_TieredMachineBlock
        implements LowPowerLaser {

    protected long AMPERES;

    public TT_Abstract_LowPowerLaserThingy(
            int aID,
            String aName,
            String aNameRegional,
            int aTier,
            long aAmperes,
            int aInvSlotCount,
            String aDescription,
            ITexture... aTextures) {
        super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription, aTextures);
        this.AMPERES = aAmperes;
    }

    public TT_Abstract_LowPowerLaserThingy(
            int aID,
            String aName,
            String aNameRegional,
            int aTier,
            long aAmperes,
            int aInvSlotCount,
            String[] aDescription,
            ITexture... aTextures) {
        super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription, aTextures);
        this.AMPERES = aAmperes;
    }

    public TT_Abstract_LowPowerLaserThingy(
            String aName, int aTier, long aAmperes, int aInvSlotCount, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
        this.AMPERES = aAmperes;
    }

    public TT_Abstract_LowPowerLaserThingy(
            String aName,
            int aTier,
            long aAmperes,
            int aInvSlotCount,
            String[] aDescription,
            ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
        this.AMPERES = aAmperes;
    }

    @Override
    public long getAMPERES() {
        return AMPERES;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity iGregTechTileEntity, int i, byte b, ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity iGregTechTileEntity, int i, byte b, ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean isInputFacing(byte aSide) {
        return true;
    }

    @Override
    public boolean isOutputFacing(byte aSide) {
        return true;
    }

    @Override
    public boolean shouldJoinIc2Enet() {
        return true;
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public void saveNBTData(NBTTagCompound nbtTagCompound) {
        Optional.ofNullable(nbtTagCompound).ifPresent(tag -> tag.setLong("AMPERES", AMPERES));
    }

    @Override
    public void loadNBTData(NBTTagCompound nbtTagCompound) {
        Optional.ofNullable(nbtTagCompound).ifPresent(tag -> AMPERES = tag.getLong("AMPERES"));
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return true;
    }

    @Override
    public boolean isEnetInput() {
        return false;
    }

    @Override
    public boolean isEnetOutput() {
        return false;
    }

    @Override
    public long maxEUInput() {
        return GT_Values.V[this.mTier];
    }

    @Override
    public long maxEUOutput() {
        return GT_Values.V[this.mTier];
    }

    @Override
    public boolean isTeleporterCompatible() {
        return false;
    }

    @Override
    public long getMinimumStoredEU() {
        return GT_Values.V[this.mTier + 1];
    }

    @Override
    public long maxEUStore() {
        return 512L + GT_Values.V[this.mTier + 1] * 24L * AMPERES;
    }
}
