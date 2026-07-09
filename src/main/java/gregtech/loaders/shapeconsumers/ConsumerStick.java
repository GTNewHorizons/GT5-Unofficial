package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.loaders.oreprocessing.ProcessingStick;

/// Dispatches [ProcessingStick]'s `stick`-prefix recipe generation for MaterialLib's cutover stick shape.
public final class ConsumerStick {

    private ConsumerStick() {}

    static void register() {
        ShapeConsumerSupport.delegate(Materials2Shapes.shapeStick, OrePrefixes.stick, () -> ProcessingStick.INSTANCE);
    }
}
