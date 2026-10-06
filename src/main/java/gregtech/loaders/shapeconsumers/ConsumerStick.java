package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Shapes;
import gregtech.loaders.oreprocessing.ProcessingStick;

/// Generates stick recipes through [ProcessingStick].
public final class ConsumerStick {

    private ConsumerStick() {}

    static void register() {
        ShapeConsumerSupport.delegate(Shapes.stick, OrePrefixes.stick, () -> ProcessingStick.INSTANCE);
    }
}
