package gregtech.nei.dumper;

import gregtech.api.ec.component.multiblock.IVoidingConfigComponent;
import gregtech.api.interfaces.modularui.IControllerWithOptionalFeatures;

public class VoidProtectionSupportDumper extends MultiBlockFeatureSupportDumper {

    public VoidProtectionSupportDumper() {
        super("void_protection", IControllerWithOptionalFeatures::supportsVoidProtection, IVoidingConfigComponent.KEY);
    }
}
