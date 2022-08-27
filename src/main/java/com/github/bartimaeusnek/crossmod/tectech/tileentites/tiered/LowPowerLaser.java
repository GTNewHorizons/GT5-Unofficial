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

import com.github.technus.tectech.mechanics.pipe.IConnectsToEnergyTunnel;
import com.github.technus.tectech.thing.metaTileEntity.pipe.GT_MetaTileEntity_Pipe_Energy;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;

public interface LowPowerLaser extends IMetaTileEntity, IConnectsToEnergyTunnel {

    boolean isSender();

    boolean isReceiver();

    boolean isTunnel();

    default boolean isConnectedCorrectly(byte side) {
        return false;
    }

    default void setEUVar(long aEnergy) {}

    default long getAMPERES() {
        return -1;
    }

    default long maxEUInput() {
        return -1;
    }

    default long maxEUOutput() {
        return -1;
    }

    default long maxEUStore() {
        return -1;
    }

    default long getTotalPower() {
        return this.getAMPERES() * Math.max(this.maxEUOutput(), this.maxEUInput()) - (this.getAMPERES() / 20);
    }

    default void moveAroundLowPower(IGregTechTileEntity aBaseMetaTileEntity) {
        byte color = this.getBaseMetaTileEntity().getColorization();
        if (color >= 0) {
            byte front = aBaseMetaTileEntity.getFrontFacing();
            byte opposite = GT_Utility.getOppositeSide(front);

            for (short dist = 1; dist < 250; ++dist) {
                IGregTechTileEntity tGTTileEntity =
                        aBaseMetaTileEntity.getIGregTechTileEntityAtSideAndDistance(front, dist);
                if (tGTTileEntity == null || tGTTileEntity.getColorization() != color) {
                    return;
                }

                IMetaTileEntity aMetaTileEntity = tGTTileEntity.getMetaTileEntity();
                if (aMetaTileEntity == null) {
                    return;
                }

                if (aMetaTileEntity instanceof LowPowerLaser
                        && ((LowPowerLaser) aMetaTileEntity).isReceiver()
                        && opposite == tGTTileEntity.getFrontFacing()) {
                    if (this.maxEUOutput() > ((LowPowerLaser) aMetaTileEntity).maxEUInput()
                            || this.getAMPERES() > ((LowPowerLaser) aMetaTileEntity).getAMPERES()) {
                        aMetaTileEntity.doExplosion(this.maxEUOutput());
                        this.setEUVar(aBaseMetaTileEntity.getStoredEU() - this.maxEUOutput());
                        return;
                    }

                    if (this.maxEUOutput() == ((LowPowerLaser) aMetaTileEntity).maxEUInput()) {
                        long diff = Math.min(
                                this.getAMPERES() * 20L * this.maxEUOutput(),
                                Math.min(
                                        ((LowPowerLaser) aMetaTileEntity).maxEUStore()
                                                - aMetaTileEntity
                                                        .getBaseMetaTileEntity()
                                                        .getStoredEU(),
                                        aBaseMetaTileEntity.getStoredEU()));
                        this.setEUVar(aBaseMetaTileEntity.getStoredEU() - diff);
                        ((LowPowerLaser) aMetaTileEntity)
                                .setEUVar(
                                        aMetaTileEntity.getBaseMetaTileEntity().getStoredEU() + diff);
                    }
                    return;
                }

                if ((!(aMetaTileEntity instanceof LowPowerLaser) || !((LowPowerLaser) aMetaTileEntity).isTunnel())
                        && !(aMetaTileEntity instanceof GT_MetaTileEntity_Pipe_Energy)) {
                    return;
                }

                if (aMetaTileEntity instanceof GT_MetaTileEntity_Pipe_Energy) {
                    if (((GT_MetaTileEntity_Pipe_Energy) aMetaTileEntity).connectionCount < 2) {
                        return;
                    }

                    ((GT_MetaTileEntity_Pipe_Energy) aMetaTileEntity).markUsed();
                } else if (aMetaTileEntity instanceof LowPowerLaser && ((LowPowerLaser) aMetaTileEntity).isTunnel()) {
                    if (!((LowPowerLaser) aMetaTileEntity).isConnectedCorrectly(front)) return;
                }
            }
        }
    }
}
