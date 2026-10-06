package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Shapes;
import gregtech.loaders.oreprocessing.ProcessingPure;

/// Generates purified crushed-ore recipes through [ProcessingPure].
public final class ConsumerPure {

    private ConsumerPure() {}

    static void register() {
        ShapeConsumerSupport
            .delegate(Shapes.crushedPurified, OrePrefixes.crushedPurified, () -> ProcessingPure.INSTANCE);
    }
}
