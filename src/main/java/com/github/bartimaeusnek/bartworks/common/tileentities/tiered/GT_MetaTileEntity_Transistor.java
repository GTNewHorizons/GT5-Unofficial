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

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IBasicEnergyContainer;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class GT_MetaTileEntity_Transistor extends GT_MetaTileEntity_TieredMachineBlock {
    boolean powered;

    public GT_MetaTileEntity_Transistor(
            int aID, String aName, String aNameRegional, int aTier, String aDescription, ITexture... aTextures) {
        super(aID, aName, aNameRegional, aTier, 0, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Transistor(
            int aID, String aName, String aNameRegional, int aTier, String[] aDescription, ITexture... aTextures) {
        super(aID, aName, aNameRegional, aTier, 0, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Transistor(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Transistor(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[12][17][];

        for (byte i = -1; i < 16; ++i) {
            rTextures[0][i + 1] = new ITexture[] {
                Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT[this.mTier]
            };
            rTextures[1][i + 1] = new ITexture[] {
                Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT[this.mTier]
            };
            rTextures[2][i + 1] = new ITexture[] {
                Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT[this.mTier]
            };
            rTextures[3][i + 1] = new ITexture[] {
                Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI[this.mTier]
            };
            rTextures[4][i + 1] = new ITexture[] {
                Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI[this.mTier]
            };
            rTextures[5][i + 1] = new ITexture[] {
                Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI[this.mTier]
            };
            rTextures[6][i + 1] = new ITexture[] {
                Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_IN[this.mTier]
            };
            rTextures[7][i + 1] = new ITexture[] {
                Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_IN[this.mTier]
            };
            rTextures[8][i + 1] = new ITexture[] {
                Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_IN[this.mTier]
            };
            rTextures[9][i + 1] = new ITexture[] {
                Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier]
            };
            rTextures[10][i + 1] = new ITexture[] {
                Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier]
            };
            rTextures[11][i + 1] = new ITexture[] {
                Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier]
            };
        }

        return rTextures;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_MetaTileEntity_Transistor(this.mName, this.mTier, this.getDescription(), null);
    }

    @Override
    public void saveNBTData(NBTTagCompound nbtTagCompound) {}

    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            byte side = (byte) ForgeDirection.EAST.flag;
            if (aBaseMetaTileEntity.inputEnergyFrom(side)) {
                TileEntity tTileEntity = aBaseMetaTileEntity.getTileEntityAtSide(side);
                if (!(tTileEntity instanceof IBasicEnergyContainer)) {
                    this.powered = false;
                    return;
                }
                IBasicEnergyContainer tileAtSide = (IBasicEnergyContainer) tTileEntity;
                if (!tileAtSide.outputsEnergyTo((byte) ForgeDirection.WEST.flag)
                        || !tileAtSide.isUniversalEnergyStored(4L)) {
                    this.powered = false;
                    return;
                }
                if (!tileAtSide.decreaseStoredEnergyUnits(4, false)) {
                    this.powered = false;
                    return;
                }
                if (aBaseMetaTileEntity.injectEnergyUnits(side, 4L, 1L) == 4L) {
                    this.powered = true;
                }
            }

            if (aBaseMetaTileEntity.isAllowedToWork()) this.powered = !this.powered;
        }
    }

    public long maxEUInput() {
        return GT_Values.V[this.mTier];
    }

    @Override
    public long maxAmperesIn() {
        return 1L;
    }

    @Override
    public long maxAmperesOut() {
        return this.powered ? 1L : 0;
    }

    public long maxEUOutput() {
        return this.powered ? GT_Values.V[this.mTier] : 0;
    }

    @Override
    public void loadNBTData(NBTTagCompound nbtTagCompound) {}

    @Override
    public boolean allowPullStack(IGregTechTileEntity iGregTechTileEntity, int i, byte b, ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity iGregTechTileEntity, int i, byte b, ItemStack itemStack) {
        return false;
    }

    @Override
    public ITexture[] getTexture(
            IGregTechTileEntity iGregTechTileEntity, byte b, byte b1, byte b2, boolean b3, boolean b4) {
        return new ITexture[0];
    }
}
