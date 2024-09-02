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
import net.minecraftforge.common.util.ForgeDirection;

import bartworks.util.BWTooltipReference;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTUtility;

public class MTELowPowerLaserBox extends LowPowerLaserBase {

    public MTELowPowerLaserBox(int aID, String aName, String aNameRegional, int aTier, long aAmperes,
        ITexture... aTextures) {
        super(aID, aName, aNameRegional, aTier, aAmperes, 0, new String[0], aTextures);
    }

    public MTELowPowerLaserBox(String aName, int aTier, long aAmperes, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aAmperes, 0, aDescription, aTextures);
    }

    @Override
    public boolean isSender() {
        return this.getBaseMetaTileEntity()
            .isAllowedToWork();
    }

    @Override
    public boolean isReceiver() {
        return !this.getBaseMetaTileEntity()
            .isAllowedToWork();
    }

    @Override
    public boolean isTunnel() {
        return false;
    }

    @Override
    public long maxAmperesOut() {
        return !this.getBaseMetaTileEntity()
            .isAllowedToWork() ? this.AMPERES : 0;
    }

    @Override
    public long maxAmperesIn() {
        return this.getBaseMetaTileEntity()
            .isAllowedToWork() ? this.AMPERES + this.AMPERES / 4 : 0;
    }

    @Override
    public boolean hasAlternativeModeText() {
        return true;
    }

    @Override
    public String getAlternativeModeText() {
        return this.isReceiver() ? "Set to receiving mode" : "Set to sending mode";
    }

    @Override
    public boolean isEnetInput() {
        return this.getBaseMetaTileEntity()
            .isAllowedToWork();
    }

    @Override
    public boolean isEnetOutput() {
        return !this.getBaseMetaTileEntity()
            .isAllowedToWork();
    }

    @Override
    public boolean canConnect(ForgeDirection side) {
        return side == this.getBaseMetaTileEntity()
            .getFrontFacing();
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MTELowPowerLaserBox(this.mName, this.mTier, this.AMPERES, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            aBaseMetaTileEntity.setActive(aBaseMetaTileEntity.isAllowedToWork());

            byte Tick = (byte) (int) (aTick % 20L);
            if (16 == Tick) {
                if (aBaseMetaTileEntity.getStoredEU() > 0L) {
                    this.setEUVar(aBaseMetaTileEntity.getStoredEU() - this.AMPERES);
                    if (aBaseMetaTileEntity.getStoredEU() < 0L) {
                        this.setEUVar(0L);
                    }
                }
                if (this.getBaseMetaTileEntity()
                    .isAllowedToWork() && aBaseMetaTileEntity.getStoredEU() > this.getMinimumStoredEU()) {
                    this.moveAroundLowPower(aBaseMetaTileEntity);
                }
            }
        }
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[12][17][];

        for (byte i = -1; i < 16; ++i) {
            rTextures[0][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI[this.mTier] };
            rTextures[1][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI[this.mTier] };
            rTextures[2][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI[this.mTier] };
            rTextures[3][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_IN_LASER_TT[this.mTier] };
            rTextures[4][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_IN_LASER_TT[this.mTier] };
            rTextures[5][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_IN_LASER_TT[this.mTier] };
            rTextures[6][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier] };
            rTextures[7][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier] };
            rTextures[8][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier] };
            rTextures[9][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_OUT_LASER_TT[this.mTier] };
            rTextures[10][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_OUT_LASER_TT[this.mTier] };
            rTextures[11][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_OUT_LASER_TT[this.mTier] };
        }

        return rTextures;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean aActive, boolean aRedstone) {
        return this.mTextures[Math.min(2, side.ordinal()) + (side == facing ? 3 : 0) + (aActive ? 0 : 6)][aColorIndex
            + 1];
    }

    @Override
    public String[] getDescription() {
        return new String[] { "Like a transformer... but for LASERS!",
            "Transfer rate: " + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(this.getTotalPower())
                + EnumChatFormatting.WHITE
                + " EU/t",
            BWTooltipReference.ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS.get() };
    }
}
