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

package bwcrossmod.tectech.tileentites.tiered;

import net.minecraft.util.EnumChatFormatting;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyTunnel;

@Deprecated
public class MTELowPowerLaserHatch extends MTEHatchEnergyTunnel implements LowPowerLaser {

    public MTELowPowerLaserHatch(int aID, String aName, String aNameRegional, int aTier, int aAmp) {
        super(aID, aName, aNameRegional, aTier, aAmp);
    }

    public MTELowPowerLaserHatch(String aName, int aTier, int aAmp, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aAmp, aDescription, aTextures);
    }

    @Override
    public String[] getDescription() {
        String[] desc = new String[mDescriptionArray.length + 1];
        desc[0] = EnumChatFormatting.RED + "DEPRECATED! This machine will be removed in the next major update.";
        System.arraycopy(mDescriptionArray, 0, desc, 1, mDescriptionArray.length);
        return desc;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELowPowerLaserHatch(this.mName, this.mTier, this.Amperes, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public boolean isSender() {
        return false;
    }

    @Override
    public boolean isReceiver() {
        return true;
    }

    @Override
    public boolean isTunnel() {
        return false;
    }

    @Override
    public long getAMPERES() {
        return this.Amperes;
    }
}
