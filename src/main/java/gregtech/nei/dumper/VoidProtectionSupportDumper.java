package gregtech.nei.dumper;

import gregtech.api.interfaces.modularui.ControllerWithOptionalFeatures;

public class VoidProtectionSupportDumper extends MultiBlockFeatureSupportDumper {

    public VoidProtectionSupportDumper() {
        super("void_protection", ControllerWithOptionalFeatures::supportsVoidProtection);
    }
}
