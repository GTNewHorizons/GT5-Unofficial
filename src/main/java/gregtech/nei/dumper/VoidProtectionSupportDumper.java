package gregtech.nei.dumper;

import gregtech.api.interfaces.modularui.ControllerWithButtons;

public class VoidProtectionSupportDumper extends MultiBlockFeatureSupportDumper {

    public VoidProtectionSupportDumper() {
        super("void_protection", ControllerWithButtons::supportsVoidProtection);
    }
}
