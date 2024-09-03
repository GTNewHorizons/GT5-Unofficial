package gregtech.nei.dumper;

import gregtech.api.interfaces.modularui.IControllerWithOptionalFeatures;

public class InputSeparationSupportDumper extends MultiBlockFeatureSupportDumper {

    public InputSeparationSupportDumper() {
        super("input_separation", IControllerWithOptionalFeatures::supportsInputSeparation);
    }
}
