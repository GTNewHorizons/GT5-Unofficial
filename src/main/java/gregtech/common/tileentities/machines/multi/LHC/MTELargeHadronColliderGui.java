package gregtech.common.tileentities.machines.multi.LHC;

import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.metatileentity.implementations.gui.MTEMultiBlockBaseGui;
import gregtech.api.modularui2.GTGuiTextures;

public class MTELargeHadronColliderGui extends MTEMultiBlockBaseGui {

    public MTELargeHadronColliderGui(MTEMultiBlockBase base) {
        super(base);
    }

    @Override
    protected void setMachineModeIcons() {
        machineModeIcons.add(GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_CUTTING); //todo: make new
        machineModeIcons.add(GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_SLICING); //todo: make new
    }
}
