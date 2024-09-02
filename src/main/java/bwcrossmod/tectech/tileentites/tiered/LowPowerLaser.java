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

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import tectech.mechanics.pipe.IConnectsToEnergyTunnel;
import tectech.thing.metaTileEntity.pipe.MTEPipeEnergy;

public interface LowPowerLaser extends IMetaTileEntity, IConnectsToEnergyTunnel {

    boolean isSender();

    boolean isReceiver();

    boolean isTunnel();

    default boolean isConnectedCorrectly(ForgeDirection side) {
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
        return this.getAMPERES() * Math.max(this.maxEUOutput(), this.maxEUInput()) - this.getAMPERES() / 20;
    }

    default void moveAroundLowPower(IGregTechTileEntity aBaseMetaTileEntity) {
        byte color = this.getBaseMetaTileEntity()
            .getColorization();
        if (color >= 0) {
            ForgeDirection front = aBaseMetaTileEntity.getFrontFacing();
            ForgeDirection opposite = front.getOpposite();

            for (short dist = 1; dist < 250; ++dist) {
                IGregTechTileEntity tGTTileEntity = aBaseMetaTileEntity
                    .getIGregTechTileEntityAtSideAndDistance(front, dist);
                if (tGTTileEntity == null || tGTTileEntity.getColorization() != color) {
                    return;
                }

                IMetaTileEntity aMetaTileEntity = tGTTileEntity.getMetaTileEntity();
                if (aMetaTileEntity == null) {
                    return;
                }

                if (aMetaTileEntity instanceof LowPowerLaser lowPowerLaser && lowPowerLaser.isReceiver()
                    && opposite == tGTTileEntity.getFrontFacing()) {
                    if (this.maxEUOutput() > lowPowerLaser.maxEUInput()
                        || this.getAMPERES() > lowPowerLaser.getAMPERES()) {
                        aMetaTileEntity.doExplosion(this.maxEUOutput());
                        this.setEUVar(aBaseMetaTileEntity.getStoredEU() - this.maxEUOutput());
                        return;
                    }

                    if (this.maxEUOutput() == lowPowerLaser.maxEUInput()) {
                        long diff = Math.min(
                            this.getAMPERES() * 20L * this.maxEUOutput(),
                            Math.min(
                                lowPowerLaser.maxEUStore() - aMetaTileEntity.getBaseMetaTileEntity()
                                    .getStoredEU(),
                                aBaseMetaTileEntity.getStoredEU()));
                        this.setEUVar(aBaseMetaTileEntity.getStoredEU() - diff);
                        lowPowerLaser.setEUVar(
                            aMetaTileEntity.getBaseMetaTileEntity()
                                .getStoredEU() + diff);
                    }
                    return;
                }

                if ((!(aMetaTileEntity instanceof LowPowerLaser lowPowerLaser) || !lowPowerLaser.isTunnel())
                    && !(aMetaTileEntity instanceof MTEPipeEnergy)) {
                    return;
                }

                if (aMetaTileEntity instanceof MTEPipeEnergy tePipeEnergy) {
                    if (tePipeEnergy.connectionCount < 2) {
                        return;
                    }
                    tePipeEnergy.markUsed();
                    return;
                }

                if (aMetaTileEntity instanceof LowPowerLaser lowPowerLaser && lowPowerLaser.isTunnel()
                    && !lowPowerLaser.isConnectedCorrectly(front)) {
                    return;
                }
            }
        }
    }
}
