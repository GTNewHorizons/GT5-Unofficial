package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Shapes;
import gregtech.loaders.oreprocessing.ProcessingRotor;

/// Generates rotor recipes through [ProcessingRotor].
public final class ConsumerRotor {

    private ConsumerRotor() {}

    static void register() {
        ShapeConsumerSupport.delegate(Shapes.rotor, OrePrefixes.rotor, () -> ProcessingRotor.INSTANCE);
    }
}
