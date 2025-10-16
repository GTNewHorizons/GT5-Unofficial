package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.gui;

import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.base.MTEMultiBlockBaseGui;

public class MTEIndustrialPlatePressGui extends MTEMultiBlockBaseGui {

    public MTEIndustrialPlatePressGui(MTEMultiBlockBase base) {
        super(base);
    }

    @Override
    protected void setMachineModeIcons() {
        machineModeIcons.add(GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_BENDING);
        machineModeIcons.add(GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_FORMING);
    }

}
