package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.BlockShapes;
import gregtech.loaders.oreprocessing.ProcessingCasing;

/// Generates bolted and advanced casing recipes through [ProcessingCasing].
public final class ConsumerCasing {

    private ConsumerCasing() {}

    static void register() {
        ShapeConsumerSupport
            .delegate(BlockShapes.blockCasing, OrePrefixes.blockCasing, () -> ProcessingCasing.INSTANCE);
    }
}
