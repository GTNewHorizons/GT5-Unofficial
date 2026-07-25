package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2BlockShapes;
import gregtech.loaders.oreprocessing.ProcessingCasing;

/// Dispatches [ProcessingCasing]'s casing recipe generation for MaterialLib's `blockCasing` shape (the
/// registrator emits both the bolted and rebolted-advanced casing recipes).
public final class ConsumerCasing {

    private ConsumerCasing() {}

    static void register() {
        ShapeConsumerSupport
            .delegate(Materials2BlockShapes.blockCasing, OrePrefixes.blockCasing, () -> ProcessingCasing.INSTANCE);
    }
}
