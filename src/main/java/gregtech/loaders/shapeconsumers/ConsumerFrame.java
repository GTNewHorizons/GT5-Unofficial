package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.PipeShapes;
import gregtech.loaders.oreprocessing.ProcessingFrame;

/// Dispatches [ProcessingFrame]'s frame-box recipe generation for the `frameGt` shape. The shape's membership
/// is the legacy frame set (every generated material with metal items, plus Wood -- see
/// `PipeMaterials#frameMaterials`), so the per-material dispatch covers exactly the materials the
/// legacy frame registrations emitted recipes for.
public final class ConsumerFrame {

    private ConsumerFrame() {}

    static void register() {
        ShapeConsumerSupport.delegate(PipeShapes.frameGt, OrePrefixes.frameGt, () -> ProcessingFrame.INSTANCE);
    }
}
