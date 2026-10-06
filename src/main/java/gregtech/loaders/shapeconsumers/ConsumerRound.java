package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Shapes;
import gregtech.loaders.oreprocessing.ProcessingRound;

/// Generates round recipes through [ProcessingRound].
public final class ConsumerRound {

    private ConsumerRound() {}

    static void register() {
        ShapeConsumerSupport.delegate(Shapes.round, OrePrefixes.round, () -> ProcessingRound.INSTANCE);
    }
}
