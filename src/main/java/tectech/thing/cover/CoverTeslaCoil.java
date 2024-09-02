package tectech.thing.cover;

import static ic2.api.info.Info.DMG_ELECTRIC;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.CoverBehavior;
import gregtech.api.util.GTUtility;
import tectech.mechanics.tesla.ITeslaConnectable;
import tectech.mechanics.tesla.TeslaCoverConnection;

public class CoverTeslaCoil extends CoverBehavior {

    public CoverTeslaCoil() {}

    @Override
    public int doCoverThings(ForgeDirection side, byte aInputRedstone, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity, long aTimer) {
        // Only do stuff if we're on top and have power
        if (side == ForgeDirection.UP || aTileEntity.getEUCapacity() > 0) {
            // Makes sure we're on the list
            ITeslaConnectable.TeslaUtil.teslaSimpleNodeSetAdd(
                new TeslaCoverConnection(
                    aTileEntity.getIGregTechTileEntityOffset(0, 0, 0),
                    getTeslaReceptionCapability()));
        }
        return super.doCoverThings(side, aInputRedstone, aCoverID, aCoverVariable, aTileEntity, aTimer);
    }

    @Override
    public void onCoverUnload(ICoverable aTileEntity) {
        if (!aTileEntity.isClientSide()) {
            ITeslaConnectable.TeslaUtil.teslaSimpleNodeSetRemove(
                new TeslaCoverConnection(
                    aTileEntity.getIGregTechTileEntityOffset(0, 0, 0),
                    getTeslaReceptionCapability()));
        }
    }

    @Override
    public boolean onCoverRemoval(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        boolean aForced) {
        ITeslaConnectable.TeslaUtil.teslaSimpleNodeSetRemove(
            new TeslaCoverConnection(aTileEntity.getIGregTechTileEntityOffset(0, 0, 0), getTeslaReceptionCapability()));
        return super.onCoverRemoval(side, aCoverID, aCoverVariable, aTileEntity, aForced);
    }

    @Override
    public void onBaseTEDestroyed(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        ITeslaConnectable.TeslaUtil.teslaSimpleNodeSetRemove(
            new TeslaCoverConnection(aTileEntity.getIGregTechTileEntityOffset(0, 0, 0), getTeslaReceptionCapability()));
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
        if (aTileEntity.getStoredEU() > 0 && !GTUtility.isWearingFullElectroHazmat(aPlayer)) {
            aPlayer.attackEntityFrom(DMG_ELECTRIC, 20);
        }
        return aCoverVariable;
    }

    @Override
    public int getTickRate(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        // It updates once every 10 ticks, so once every 0.5 second
        return 10;
    }

    public byte getTeslaReceptionCapability() {
        return 2;
    }
}
