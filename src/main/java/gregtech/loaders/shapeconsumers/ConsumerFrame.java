package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.TEBlockShapes;
import gregtech.loaders.oreprocessing.ProcessingFrame;

/// Generates frame-box recipes through [ProcessingFrame].
public final class ConsumerFrame {

    private ConsumerFrame() {}

    static void register() {
        ShapeConsumerSupport.delegate(TEBlockShapes.frameGt, OrePrefixes.frameGt, () -> ProcessingFrame.INSTANCE);
    }
}
