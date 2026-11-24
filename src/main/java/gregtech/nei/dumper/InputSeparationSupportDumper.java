package gregtech.nei.dumper;

import gregtech.api.ec.component.multiblock.IInputSeparationComponent;
import gregtech.api.interfaces.modularui.IControllerWithOptionalFeatures;

public class InputSeparationSupportDumper extends MultiBlockFeatureSupportDumper {

    public InputSeparationSupportDumper() {
        super(
            "input_separation",
            IControllerWithOptionalFeatures::supportsInputSeparation,
            IInputSeparationComponent.KEY);
    }
}
