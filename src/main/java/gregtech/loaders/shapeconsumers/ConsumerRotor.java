package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.loaders.oreprocessing.ProcessingRotor;

/// Dispatches [ProcessingRotor]'s `rotor`-prefix recipe generation for MaterialLib's cutover rotor shape.
public final class ConsumerRotor {

    private ConsumerRotor() {}

    static void register() {
        ShapeConsumerSupport.delegate(Materials2Shapes.rotor, OrePrefixes.rotor, () -> ProcessingRotor.INSTANCE);
    }
}
