package gregtech.common.gui.modularui.hatch;

import gregtech.api.metatileentity.implementations.MTEHatchOutput;

public class MTEHatchVoidGui extends MTEHatchOutputGui {

    public MTEHatchVoidGui(MTEHatchOutput hatch) {
        super(hatch);
    }

    @Override
    protected boolean supportsScreen() {
        return false;
    }

    @Override
    protected boolean supportsScreenIOColumn() {
        return false;
    }
}
