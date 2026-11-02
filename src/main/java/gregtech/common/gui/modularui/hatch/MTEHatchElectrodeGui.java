package gregtech.common.gui.modularui.hatch;

import gregtech.api.metatileentity.implementations.MTEHatchInputBus;

public class MTEHatchElectrodeGui extends MTEHatchInputBusGui {

    public MTEHatchElectrodeGui(MTEHatchInputBus hatch) {
        super(hatch);
    }

    @Override
    protected int getDimension() {
        return 1;
    }

    @Override
    protected boolean supportsBottomLeftCornerFlow() {
        return false;
    }
}
