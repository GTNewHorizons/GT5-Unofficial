package gregtech.nei.dumper;

import gregtech.api.interfaces.modularui.ControllerWithButtons;

public class InputSeparationSupportDumper extends MultiBlockFeatureSupportDumper {

    public InputSeparationSupportDumper() {
        super("input_separation", ControllerWithButtons::supportsInputSeparation);
    }
}
