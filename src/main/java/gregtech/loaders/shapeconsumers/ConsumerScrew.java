package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Shapes;
import gregtech.loaders.oreprocessing.ProcessingScrew;

/// Generates screw recipes through [ProcessingScrew].
public final class ConsumerScrew {

    private ConsumerScrew() {}

    static void register() {
        ShapeConsumerSupport.delegate(Shapes.screw, OrePrefixes.screw, () -> ProcessingScrew.INSTANCE);
    }
}
