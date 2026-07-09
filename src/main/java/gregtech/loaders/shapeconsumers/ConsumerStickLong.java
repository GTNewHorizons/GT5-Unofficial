package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.loaders.oreprocessing.ProcessingStickLong;

/// Dispatches [ProcessingStickLong]'s `stickLong`-prefix recipe generation for MaterialLib's cutover long-stick
/// shape.
public final class ConsumerStickLong {

    private ConsumerStickLong() {}

    static void register() {
        ShapeConsumerSupport
            .delegate(Materials2Shapes.shapeStickLong, OrePrefixes.stickLong, () -> ProcessingStickLong.INSTANCE);
    }
}
