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

package com.github.bartimaeusnek.bartworks.common.tileentities.tiered;

import com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference;
import com.github.bartimaeusnek.bartworks.util.ChatColorHelper;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Transformer;
import gregtech.api.util.GT_Utility;
import net.minecraft.util.StatCollector;

public class GT_MetaTileEntity_EnergyDistributor extends GT_MetaTileEntity_Transformer {

    public GT_MetaTileEntity_EnergyDistributor(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, null);
    }

    public GT_MetaTileEntity_EnergyDistributor(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    public GT_MetaTileEntity_EnergyDistributor(
            String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EnergyDistributor(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    public long maxEUInput() {
        return GT_Values.V[this.mTier];
    }

    public long maxEUOutput() {
        return GT_Values.V[this.mTier];
    }

    public long maxAmperesOut() {
        return 320;
    }

    public long maxAmperesIn() {
        return 320;
    }

    @Override
    public long maxEUStore() {
        return 512L + (GT_Values.V[this.mTier] * 320L);
    }

    public String[] getDescription() {
        return new String[] {
            StatCollector.translateToLocal("tooltip.tile.energydistributor.0.name"),
            StatCollector.translateToLocal("tooltip.tile.tiereddsc.0.name") + " " + ChatColorHelper.YELLOW
                    + GT_Utility.formatNumbers(GT_Values.V[this.mTier]),
            StatCollector.translateToLocal("tooltip.tile.tiereddsc.1.name") + " " + ChatColorHelper.YELLOW
                    + GT_Utility.formatNumbers(this.maxAmperesIn()),
            StatCollector.translateToLocal("tooltip.tile.tiereddsc.2.name") + " " + ChatColorHelper.YELLOW
                    + GT_Utility.formatNumbers(this.maxAmperesOut()),
            BW_Tooltip_Reference.ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS.get()
        };
    }
}
