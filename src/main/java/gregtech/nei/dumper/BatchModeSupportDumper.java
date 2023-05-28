package gregtech.nei.dumper;

import gregtech.api.interfaces.modularui.ControllerWithOptionalFeatures;

public class BatchModeSupportDumper extends MultiBlockFeatureSupportDumper {

    public BatchModeSupportDumper() {
        super("batch_mode", ControllerWithOptionalFeatures::supportsBatchMode);
    }
}
