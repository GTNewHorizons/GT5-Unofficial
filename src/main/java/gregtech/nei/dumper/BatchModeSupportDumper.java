package gregtech.nei.dumper;

import gregtech.api.ec.component.multiblock.IBatchConfigComponent;
import gregtech.api.interfaces.modularui.IControllerWithOptionalFeatures;

public class BatchModeSupportDumper extends MultiBlockFeatureSupportDumper {

    public BatchModeSupportDumper() {
        super("batch_mode", IControllerWithOptionalFeatures::supportsBatchMode, IBatchConfigComponent.KEY);
    }
}
