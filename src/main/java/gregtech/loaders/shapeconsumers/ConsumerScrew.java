package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.loaders.oreprocessing.ProcessingScrew;

/// Dispatches [ProcessingScrew]'s `screw`-prefix recipe generation for MaterialLib's cutover screw shape.
public final class ConsumerScrew {

    private ConsumerScrew() {}

    static void register() {
        ShapeConsumerSupport.delegate(Materials2Shapes.screw, OrePrefixes.screw, () -> ProcessingScrew.INSTANCE);
    }
}
