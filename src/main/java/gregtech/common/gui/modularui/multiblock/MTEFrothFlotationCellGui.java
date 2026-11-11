package gregtech.common.gui.modularui.multiblock;

import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;

import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.MTEFrothFlotationCell;

public class MTEFrothFlotationCellGui extends MTEMultiBlockBaseGui<MTEFrothFlotationCell> {

    public MTEFrothFlotationCellGui(MTEFrothFlotationCell multiblock) {
        super(multiblock);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        syncManager.syncValue(
            "lockedMaterial",
            new StringSyncValue(multiblock::getLockedMaterialName, multiblock::setLockedMaterialName));
    }
}
