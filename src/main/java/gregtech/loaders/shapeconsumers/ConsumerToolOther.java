package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Shapes;
import gregtech.loaders.oreprocessing.ProcessingToolOther;

/// Dispatches [ProcessingToolOther]'s `toolHeadHammer`-prefix recipe generation for MaterialLib's cutover
/// hammer-head shape. A second, independent registrator on that prefix alongside [ProcessingToolHead].
public final class ConsumerToolOther {

    private ConsumerToolOther() {}

    static void register() {
        ShapeConsumerSupport
            .delegate(Shapes.toolHeadHammer, OrePrefixes.toolHeadHammer, () -> ProcessingToolOther.INSTANCE);
    }
}
