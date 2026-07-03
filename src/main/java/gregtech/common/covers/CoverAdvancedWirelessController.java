package gregtech.common.covers;

import org.jetbrains.annotations.NotNull;

import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.ITexture;
import gregtech.common.gui.modularui.cover.CoverAdvancedWirelessControllerGui;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;

public class CoverAdvancedWirelessController extends CoverWirelessController {

    public CoverAdvancedWirelessController(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
        this.setMode(GateMode.AND);
    }

    // GUI stuff

    @Override
    protected @NotNull CoverBaseGui<?> getCoverGui() {
        return new CoverAdvancedWirelessControllerGui(this);
    }
}
