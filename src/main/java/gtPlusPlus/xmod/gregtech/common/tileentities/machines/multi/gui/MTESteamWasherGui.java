package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.gui;

import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.base.MTEMultiBlockBaseGui;

public class MTESteamWasherGui extends MTEMultiBlockBaseGui {

    public MTESteamWasherGui(MTEMultiBlockBase base) {
        super(base);
    }

    @Override
    protected void setMachineModeIcons() {
        machineModeIcons.add(GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_WASHPLANT);
        machineModeIcons.add(GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_SIMPLEWASHER);
    }
}
