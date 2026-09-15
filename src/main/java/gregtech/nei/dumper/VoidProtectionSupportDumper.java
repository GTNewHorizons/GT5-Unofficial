package gregtech.nei.dumper;

import gregtech.api.interfaces.modularui.IControllerWithOptionalFeatures;

public class VoidProtectionSupportDumper extends MultiBlockFeatureSupportDumper {

    public VoidProtectionSupportDumper() {
        super("void_protection", IControllerWithOptionalFeatures::supportsVoidProtection);
    }
}
