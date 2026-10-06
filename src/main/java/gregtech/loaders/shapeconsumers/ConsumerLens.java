package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Shapes;
import gregtech.loaders.oreprocessing.ProcessingLens;

/// Generates lens recipes through [ProcessingLens].
public final class ConsumerLens {

    private ConsumerLens() {}

    static void register() {
        ShapeConsumerSupport.delegate(Shapes.lens, OrePrefixes.lens, () -> ProcessingLens.INSTANCE);
    }
}
