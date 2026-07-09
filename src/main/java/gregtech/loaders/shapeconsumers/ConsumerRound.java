package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.loaders.oreprocessing.ProcessingRound;

/// Dispatches [ProcessingRound]'s `round`-prefix recipe generation for MaterialLib's cutover round shape.
public final class ConsumerRound {

    private ConsumerRound() {}

    static void register() {
        ShapeConsumerSupport.delegate(Materials2Shapes.shapeRound, OrePrefixes.round, () -> ProcessingRound.INSTANCE);
    }
}
