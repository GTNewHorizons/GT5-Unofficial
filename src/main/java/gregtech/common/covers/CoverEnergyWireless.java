package gregtech.common.covers;

import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;
import static gregtech.common.misc.WirelessNetworkManager.ticks_between_energy_addition;
import static java.lang.Long.min;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.util.CoverBehavior;
import gregtech.api.util.ISerializableObject;

public class CoverEnergyWireless extends CoverBehavior {

    private final long transferred_energy_per_operation;

    public CoverEnergyWireless(int voltage) {
        this.transferred_energy_per_operation = 2 * voltage * ticks_between_energy_addition;
    }

    @Override
    public boolean isRedstoneSensitive(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        long aTimer) {
        return false;
    }

    @Override
    public boolean allowsCopyPasteTool() {
        return false;
    }

    @Override
    public boolean allowsTickRateAddition() {
        return false;
    }

    @Override
    public boolean hasCoverGUI() {
        return false;
    }

    @Override
    public int doCoverThings(ForgeDirection side, byte aInputRedstone, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity, long aTimer) {
        if (aCoverVariable == 0 || aTimer % ticks_between_energy_addition == 0) {
            tryFetchingEnergy(aTileEntity);
        }
        return 1;
    }

    private static UUID getOwner(Object te) {
        if (te instanceof BaseMetaTileEntity igte) {
            return igte.getOwnerUuid();
        } else {
            return null;
        }
    }

    private void tryFetchingEnergy(ICoverable tileEntity) {
        if (tileEntity instanceof BaseMetaTileEntity bmte) {
            long currentEU = bmte.getStoredEUuncapped();
            long euToTransfer = min(transferred_energy_per_operation - currentEU, transferred_energy_per_operation);
            if (euToTransfer <= 0) return; // nothing to transfer
            if (!addEUToGlobalEnergyMap(getOwner(tileEntity), -euToTransfer)) return;
            bmte.increaseStoredEnergyUnits(euToTransfer, true);
        }
    }

    @Override
    protected boolean onCoverRightClickImpl(ForgeDirection side, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX,
        float aY, float aZ) {
        return false;
    }

    @Override
    protected boolean isGUIClickableImpl(ForgeDirection side, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return false;
    }

    @Override
    public boolean alwaysLookConnected(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public int getTickRate(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 20;
    }
}
