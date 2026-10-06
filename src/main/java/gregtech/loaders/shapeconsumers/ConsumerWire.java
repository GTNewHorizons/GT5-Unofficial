package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.TEBlockShapes;
import gregtech.loaders.oreprocessing.ProcessingWire;

/// Generates wire recipes through [ProcessingWire].
public final class ConsumerWire {

    private ConsumerWire() {}

    static void register() {
        ShapeConsumerSupport.delegate(TEBlockShapes.wireGt01, OrePrefixes.wireGt01, () -> ProcessingWire.INSTANCE);
        ShapeConsumerSupport.delegate(TEBlockShapes.wireGt02, OrePrefixes.wireGt02, () -> ProcessingWire.INSTANCE);
        ShapeConsumerSupport.delegate(TEBlockShapes.wireGt04, OrePrefixes.wireGt04, () -> ProcessingWire.INSTANCE);
        ShapeConsumerSupport.delegate(TEBlockShapes.wireGt08, OrePrefixes.wireGt08, () -> ProcessingWire.INSTANCE);
        ShapeConsumerSupport.delegate(TEBlockShapes.wireGt12, OrePrefixes.wireGt12, () -> ProcessingWire.INSTANCE);
        ShapeConsumerSupport.delegate(TEBlockShapes.wireGt16, OrePrefixes.wireGt16, () -> ProcessingWire.INSTANCE);
    }
}
