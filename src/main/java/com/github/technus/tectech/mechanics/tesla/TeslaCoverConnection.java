package com.github.technus.tectech.mechanics.tesla;

import static com.github.technus.tectech.mechanics.tesla.ITeslaConnectable.TeslaUtil.teslaSimpleNodeSetRemoveScheduled;

import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.base.Objects;
import com.gtnewhorizon.structurelib.util.Vec3Impl;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class TeslaCoverConnection implements ITeslaConnectableSimple {

    private final IGregTechTileEntity IGT;
    private final Vec3Impl pos;
    private final byte teslaReceptionCapability;

    public TeslaCoverConnection(IGregTechTileEntity IGT, byte teslaReceptionCapability) {
        this.IGT = IGT;
        this.pos = new Vec3Impl(IGT.getXCoord(), IGT.getYCoord(), IGT.getZCoord());

        this.teslaReceptionCapability = teslaReceptionCapability;
    }

    @Override
    public byte getTeslaReceptionCapability() {
        return teslaReceptionCapability;
    }

    @Override
    public float getTeslaReceptionCoefficient() {
        return 1;
    }

    @Override
    public boolean isTeslaReadyToReceive() {
        return true;
    }

    @Override
    public long getTeslaStoredEnergy() {
        return IGT.getStoredEU();
    }

    @Override
    public Vec3Impl getTeslaPosition() {
        return pos;
    }

    @Override
    public Integer getTeslaDimension() {
        return IGT.getWorld().provider.dimensionId;
    }

    @Override
    public boolean teslaInjectEnergy(long teslaVoltageInjected) {
        // Same as in the microwave transmitters, this does not account for amp limits
        boolean output = false;

        if (!IGT.isDead()) {
            output = IGT.injectEnergyUnits(ForgeDirection.UP, teslaVoltageInjected, 1L) > 0L;
        } else {
            teslaSimpleNodeSetRemoveScheduled(this);
        }

        return output;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeslaCoverConnection that = (TeslaCoverConnection) o;
        return Objects.equal(IGT, that.IGT);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(IGT);
    }
}
