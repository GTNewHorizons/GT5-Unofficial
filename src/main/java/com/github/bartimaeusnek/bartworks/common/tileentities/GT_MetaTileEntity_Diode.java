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

package com.github.bartimaeusnek.bartworks.common.tileentities;

import com.github.bartimaeusnek.bartworks.util.ChatColorHelper;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicHull;

public class GT_MetaTileEntity_Diode extends GT_MetaTileEntity_BasicHull {

    private long amps;

    public GT_MetaTileEntity_Diode(int aID, String aName, String aNameRegional, int aTier, int amps) {
        super(aID, aName, aNameRegional, aTier, "A Simple diode that will allow Energy Flow in only one direction.");
        this.amps = amps;
    }

    public GT_MetaTileEntity_Diode(String aName, int aTier, int aInvSlotCount, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public long maxAmperesOut() {
        return amps;
    }

    @Override
    public long maxAmperesIn() {
        return amps;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Diode(this.mName, this.mTier, this.mInventory.length, this.mDescriptionArray, this.mTextures);
    }

    public String[] getDescription() {
        return new String[]{mDescription, "Voltage: " + ChatColorHelper.YELLOW + GT_Values.V[this.mTier], "Amperage IN: " + ChatColorHelper.YELLOW + maxAmperesIn(), "Amperage OUT: " + ChatColorHelper.YELLOW + maxAmperesOut(), "Added by bartimaeusnek via " + ChatColorHelper.DARKGREEN + "BartWorks"};
    }
}
