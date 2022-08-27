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

import com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference;
import com.github.bartimaeusnek.bartworks.util.ChatColorHelper;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;

public class TT_MetaTileEntity_LowPowerLaserBox extends TT_Abstract_LowPowerLaserThingy {

    public TT_MetaTileEntity_LowPowerLaserBox(
            int aID, String aName, String aNameRegional, int aTier, long aAmperes, ITexture... aTextures) {
        super(aID, aName, aNameRegional, aTier, aAmperes, 0, new String[0], aTextures);
    }

    public TT_MetaTileEntity_LowPowerLaserBox(
            String aName, int aTier, long aAmperes, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aAmperes, 0, aDescription, aTextures);
    }

    @Override
    public boolean isSender() {
        return this.getBaseMetaTileEntity().isAllowedToWork();
    }

    @Override
    public boolean isReceiver() {
        return !this.getBaseMetaTileEntity().isAllowedToWork();
    }

    @Override
    public boolean isTunnel() {
        return false;
    }

    @Override
    public long maxAmperesOut() {
        return !this.getBaseMetaTileEntity().isAllowedToWork() ? AMPERES : 0;
    }

    @Override
    public long maxAmperesIn() {
        return this.getBaseMetaTileEntity().isAllowedToWork() ? AMPERES + (AMPERES / 4) : 0;
    }

    @Override
    public boolean hasAlternativeModeText() {
        return true;
    }

    @Override
    public String getAlternativeModeText() {
        return isReceiver() ? "Set to receiving mode" : "Set to sending mode";
    }

    @Override
    public boolean isEnetInput() {
        return this.getBaseMetaTileEntity().isAllowedToWork();
    }

    @Override
    public boolean isEnetOutput() {
        return !this.getBaseMetaTileEntity().isAllowedToWork();
    }

    @Override
    public boolean canConnect(byte aSide) {
        return aSide == this.getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new TT_MetaTileEntity_LowPowerLaserBox(
                this.mName, this.mTier, this.AMPERES, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            aBaseMetaTileEntity.setActive(aBaseMetaTileEntity.isAllowedToWork());

            byte Tick = (byte) ((int) (aTick % 20L));
            if (16 == Tick) {
                if (aBaseMetaTileEntity.getStoredEU() > 0L) {
                    this.setEUVar(aBaseMetaTileEntity.getStoredEU() - this.AMPERES);
                    if (aBaseMetaTileEntity.getStoredEU() < 0L) {
                        this.setEUVar(0L);
                    }
                }
                if (this.getBaseMetaTileEntity().isAllowedToWork())
                    if (aBaseMetaTileEntity.getStoredEU() > this.getMinimumStoredEU()) {
                        this.moveAroundLowPower(aBaseMetaTileEntity);
                    }
            }
        }
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[12][17][];

        for (byte i = -1; i < 16; ++i) {
            rTextures[0][i + 1] = new ITexture[] {
                Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI[this.mTier]
            };
            rTextures[1][i + 1] = new ITexture[] {
                Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI[this.mTier]
            };
            rTextures[2][i + 1] = new ITexture[] {
                Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI[this.mTier]
            };
            rTextures[3][i + 1] = new ITexture[] {
                Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                com.github.technus.tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_IN_LASER_TT[this.mTier]
            };
            rTextures[4][i + 1] = new ITexture[] {
                Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                com.github.technus.tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_IN_LASER_TT[this.mTier]
            };
            rTextures[5][i + 1] = new ITexture[] {
                Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                com.github.technus.tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_IN_LASER_TT[this.mTier]
            };
            rTextures[6][i + 1] = new ITexture[] {
                Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier]
            };
            rTextures[7][i + 1] = new ITexture[] {
                Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier]
            };
            rTextures[8][i + 1] = new ITexture[] {
                Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier]
            };
            rTextures[9][i + 1] = new ITexture[] {
                Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                com.github.technus.tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_OUT_LASER_TT[this.mTier]
            };
            rTextures[10][i + 1] = new ITexture[] {
                Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                com.github.technus.tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_OUT_LASER_TT[this.mTier]
            };
            rTextures[11][i + 1] = new ITexture[] {
                Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                com.github.technus.tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_OUT_LASER_TT[this.mTier]
            };
        }

        return rTextures;
    }

    @Override
    public ITexture[] getTexture(
            IGregTechTileEntity aBaseMetaTileEntity,
            byte aSide,
            byte aFacing,
            byte aColorIndex,
            boolean aActive,
            boolean aRedstone) {
        return this.mTextures[Math.min(2, aSide) + (aSide == aFacing ? 3 : 0) + (aActive ? 0 : 6)][aColorIndex + 1];
    }

    @Override
    public String[] getDescription() {
        return new String[] {
            "Like a Tranformer... but for LAZORZ",
            "Transfer rate: " + ChatColorHelper.YELLOW + GT_Utility.formatNumbers(this.getTotalPower())
                    + ChatColorHelper.WHITE + " EU/t",
            BW_Tooltip_Reference.ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS.get()
        };
    }
}
