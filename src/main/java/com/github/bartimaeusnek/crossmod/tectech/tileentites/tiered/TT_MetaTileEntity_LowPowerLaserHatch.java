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

package com.github.bartimaeusnek.crossmod.tectech.tileentites.tiered;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import org.apache.commons.lang3.ArrayUtils;

import com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyTunnel;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GT_Utility;

public class TT_MetaTileEntity_LowPowerLaserHatch extends GT_MetaTileEntity_Hatch_EnergyTunnel
    implements LowPowerLaser {

    public TT_MetaTileEntity_LowPowerLaserHatch(int aID, String aName, String aNameRegional, int aTier, int aAmp) {
        super(aID, aName, aNameRegional, aTier, aAmp);
    }

    public TT_MetaTileEntity_LowPowerLaserHatch(String aName, int aTier, int aAmp, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aAmp, aDescription, aTextures);
    }

    @Override
    public String[] getDescription() {
        return ArrayUtils.addAll(
            this.mDescriptionArray,
            StatCollector.translateToLocal("gt.blockmachines.hatch.energytunnel.desc.1") + ": "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(this.getTotalPower())
                + EnumChatFormatting.RESET
                + " EU/t",
            BW_Tooltip_Reference.ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS.get());
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TT_MetaTileEntity_LowPowerLaserHatch(
            this.mName,
            this.mTier,
            this.Amperes,
            this.mDescriptionArray,
            this.mTextures);
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
