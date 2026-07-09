package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.loaders.oreprocessing.ProcessingToolOther;

/// Dispatches [ProcessingToolOther]'s `toolHeadHammer`-prefix recipe generation (a second, independent
/// registrator on that prefix alongside [ProcessingToolHead], mirroring the legacy multi-registrator list) for
/// MaterialLib's cutover hammer-head shape.
public final class ConsumerToolOther {

    private ConsumerToolOther() {}

    static void register() {
        ShapeConsumerSupport
            .delegate(Materials2Shapes.shapeToolHeadHammer, OrePrefixes.toolHeadHammer, ProcessingToolOther.INSTANCE);
    }
}
