package gregtech.api.interfaces.metatileentity;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.util.shutdown.ShutDownReason;

/**
 * Compatibility wrapper for {@link MTEMultiBlockBase}.
 *
 * @deprecated Scheduled for removal. Please use the new EC APIs instead.
 */
@Deprecated
public interface IMultiblockController {

    IGregTechTileEntity getBaseMetaTileEntity();

    void enableWorking();

    void disableWorking();

    boolean isAllowedToWork();

    void stopMachine(@Nonnull ShutDownReason reason);

    ItemStack getControllerSlot();

    ItemStack getStackForm(long aAmount);

    int getRepairStatus();

    int getIdealStatus();

    boolean isValid();

    String getMetaName();

    String getLocalName();

    int maxProgresstime();
}
