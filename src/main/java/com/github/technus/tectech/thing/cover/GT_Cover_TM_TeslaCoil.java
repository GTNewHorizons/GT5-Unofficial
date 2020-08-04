package com.github.technus.tectech.thing.cover;

import com.github.technus.tectech.mechanics.tesla.ITeslaConnectableSimple;
import com.github.technus.tectech.util.Vec3Impl;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;

import static com.github.technus.tectech.mechanics.tesla.ITeslaConnectable.TeslaUtil.*;
import static ic2.api.info.Info.DMG_ELECTRIC;

public class GT_Cover_TM_TeslaCoil extends GT_CoverBehavior implements ITeslaConnectableSimple {
    private IGregTechTileEntity IGT;

    public GT_Cover_TM_TeslaCoil() {
    }

    @Override
    public int doCoverThings(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
        //Only do stuff if we're on top and have power
        if (aSide == 1 || aTileEntity.getEUCapacity() > 0) {
            //Pull IGT onto the outside, should only execute first tick
            if (IGT == null) {
                IGT = aTileEntity.getIGregTechTileEntityOffset(0, 0, 0);
            }
            //Makes sure we're on the list
            teslaNodeSet.add(this);
        }

        return super.doCoverThings(aSide, aInputRedstone, aCoverID, aCoverVariable, aTileEntity, aTimer);
    }

    @Override
    public String getDescription(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return "Do not attempt to use screwdriver!";//TODO Translation support
    }

    @Override
    public boolean letsEnergyIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public int onCoverScrewdriverclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        //Shock a non-hazmat player if they dare stuff a screwdriver into one of these
        if (aTileEntity.getStoredEU() > 0 && !GT_Utility.isWearingFullElectroHazmat(aPlayer)) {
            aPlayer.attackEntityFrom(DMG_ELECTRIC, 20);
        }
        return aCoverVariable;
    }

    @Override
    public int getTickRate(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        //It updates once every 200 ticks, so once every 10 seconds
        return 200;
    }

    @Override
    public byte getTeslaReceptionCapability() {
        return 2;
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
        return new Vec3Impl(IGT);
    }

    @Override
    public Integer getTeslaDimension() {
        return IGT.getWorld().provider.dimensionId;
    }

    @Override
    public boolean teslaInjectEnergy(long teslaVoltageInjected) {
        //Same as in the microwave transmitters, this does not account for amp limits
        return IGT.injectEnergyUnits((byte) 1, teslaVoltageInjected, 1L) > 0L;
    }
}
