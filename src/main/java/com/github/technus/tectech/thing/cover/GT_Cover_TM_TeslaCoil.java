package com.github.technus.tectech.thing.cover;

import static com.github.technus.tectech.mechanics.tesla.ITeslaConnectable.TeslaUtil.teslaSimpleNodeSetAdd;
import static ic2.api.info.Info.DMG_ELECTRIC;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;

import com.github.technus.tectech.mechanics.tesla.TeslaCoverConnection;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;

public class GT_Cover_TM_TeslaCoil extends GT_CoverBehavior {

    public GT_Cover_TM_TeslaCoil() {}

    @Override
    public int doCoverThings(ForgeDirection side, byte aInputRedstone, int aCoverID, int aCoverVariable,
            ICoverable aTileEntity, long aTimer) {
        // Only do stuff if we're on top and have power
        if (side == ForgeDirection.UP || aTileEntity.getEUCapacity() > 0) {
            // Makes sure we're on the list
            teslaSimpleNodeSetAdd(
                    new TeslaCoverConnection(
                            aTileEntity.getIGregTechTileEntityOffset(0, 0, 0),
                            getTeslaReceptionCapability()));
        }
        return super.doCoverThings(side, aInputRedstone, aCoverID, aCoverVariable, aTileEntity, aTimer);
    }

    @Override
    public String getDescription(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return "Do not attempt to use screwdriver!"; // TODO Translation support
    }

    @Override
    public boolean letsEnergyIn(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public int onCoverScrewdriverclick(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
            EntityPlayer aPlayer, float aX, float aY, float aZ) {
        // Shock a non-hazmat player if they dare stuff a screwdriver into one of these
        if (aTileEntity.getStoredEU() > 0 && !GT_Utility.isWearingFullElectroHazmat(aPlayer)) {
            aPlayer.attackEntityFrom(DMG_ELECTRIC, 20);
        }
        return aCoverVariable;
    }

    @Override
    public int getTickRate(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        // It updates once every 200 ticks, so once every 10 seconds
        return 200;
    }

    public byte getTeslaReceptionCapability() {
        return 2;
    }
}
