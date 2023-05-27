package gregtech.nei.dumper;

import gregtech.api.interfaces.modularui.ControllerWithButtons;

public class BatchModeSupportDumper extends MultiBlockFeatureSupportDumper {

    public BatchModeSupportDumper() {
        super("batch_mode", ControllerWithButtons::supportsBatchMode);
    }
}
