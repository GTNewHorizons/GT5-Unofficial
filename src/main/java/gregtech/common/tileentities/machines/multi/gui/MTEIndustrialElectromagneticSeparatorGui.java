package gregtech.common.tileentities.machines.multi.gui;

import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.metatileentity.implementations.gui.MTEMultiBlockBaseGui;
import gregtech.api.modularui2.GTGuiTextures;

public class MTEIndustrialElectromagneticSeparatorGui extends MTEMultiBlockBaseGui {

    public MTEIndustrialElectromagneticSeparatorGui(MTEMultiBlockBase base) {
        super(base);
    }

    @Override
    protected void setMachineModeIcons() {
        machineModeIcons.add(GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_SEPARATOR);
        machineModeIcons.add(GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_POLARIZER);
    }
}
