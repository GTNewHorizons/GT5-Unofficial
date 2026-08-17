package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.TEBlockShapes;
import gregtech.loaders.oreprocessing.ProcessingFrame;

/// Dispatches [ProcessingFrame]'s frame-box recipe generation for the `frameGt` shape, whose membership is
/// every generated material with metal items, plus Wood -- see `PipeMaterials#frameMaterials`.
public final class ConsumerFrame {

    private ConsumerFrame() {}

    static void register() {
        ShapeConsumerSupport.delegate(TEBlockShapes.frameGt, OrePrefixes.frameGt, () -> ProcessingFrame.INSTANCE);
    }
}
