package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Shapes;
import gregtech.loaders.oreprocessing.ProcessingToolOther;

/// Generates hammer-head recipes through [ProcessingToolOther].
public final class ConsumerToolOther {

    private ConsumerToolOther() {}

    static void register() {
        ShapeConsumerSupport
            .delegate(Shapes.toolHeadHammer, OrePrefixes.toolHeadHammer, () -> ProcessingToolOther.INSTANCE);
    }
}
