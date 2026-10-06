package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Shapes;
import gregtech.loaders.oreprocessing.ProcessingStickLong;

/// Generates long-stick recipes through [ProcessingStickLong].
public final class ConsumerStickLong {

    private ConsumerStickLong() {}

    static void register() {
        ShapeConsumerSupport.delegate(Shapes.stickLong, OrePrefixes.stickLong, () -> ProcessingStickLong.INSTANCE);
    }
}
