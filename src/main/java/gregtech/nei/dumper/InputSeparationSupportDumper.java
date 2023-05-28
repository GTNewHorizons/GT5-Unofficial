package gregtech.nei.dumper;

import gregtech.api.interfaces.modularui.ControllerWithOptionalFeatures;

public class InputSeparationSupportDumper extends MultiBlockFeatureSupportDumper {

    public InputSeparationSupportDumper() {
        super("input_separation", ControllerWithOptionalFeatures::supportsInputSeparation);
    }
}
