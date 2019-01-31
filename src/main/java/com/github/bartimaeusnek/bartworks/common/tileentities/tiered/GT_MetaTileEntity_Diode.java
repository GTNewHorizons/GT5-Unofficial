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

package com.github.bartimaeusnek.bartworks.common.tileentities.tiered;

import com.github.bartimaeusnek.bartworks.util.ChatColorHelper;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicHull;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class GT_MetaTileEntity_Diode extends GT_MetaTileEntity_BasicHull {

    private long maxAmps;
    private long aAmps;

    public GT_MetaTileEntity_Diode(int aID, String aName, String aNameRegional, int aTier, int maxAmps) {
        super(aID, aName, aNameRegional, aTier, "A Simple diode that will allow Energy Flow in only one direction.");
        this.maxAmps = maxAmps;
        aAmps = maxAmps;
    }

    public GT_MetaTileEntity_Diode(String aName, int aTier, long maxAmps, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
        this.maxAmps = maxAmps;
        aAmps = maxAmps;
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        super.onScrewdriverRightClick(aSide, aPlayer, aX, aY, aZ);
        --aAmps;
        if (aAmps < 0)
            aAmps = maxAmps;
        GT_Utility.sendChatToPlayer(aPlayer, "Max Amps: " + aAmps);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setLong("maxAmp", maxAmps);
        aNBT.setLong("Amps", aAmps);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        maxAmps = aNBT.getLong("maxAmp");
        aAmps = aNBT.getLong("Amps");
        super.loadNBTData(aNBT);
    }

    @Override
    public long maxAmperesOut() {
        return aAmps;
    }

    @Override
    public long maxAmperesIn() {
        return aAmps;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Diode(this.mName, this.mTier, this.maxAmps, this.mDescriptionArray, this.mTextures);
    }

    @SuppressWarnings("deprecation")
    public String[] getDescription() {
        return new String[]{mDescription, "Voltage: " + ChatColorHelper.YELLOW + GT_Values.V[this.mTier], "Amperage IN: " + ChatColorHelper.YELLOW + maxAmperesIn(), "Amperage OUT: " + ChatColorHelper.YELLOW + maxAmperesOut(), "Added by bartimaeusnek via " + ChatColorHelper.DARKGREEN + "BartWorks"};
    }
}
