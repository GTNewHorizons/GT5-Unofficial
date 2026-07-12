package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.loaders.oreprocessing.ProcessingLens;

/// Dispatches [ProcessingLens]'s `lens`-prefix recipe generation for MaterialLib's cutover lens shape.
public final class ConsumerLens {

    private ConsumerLens() {}

    static void register() {
        ShapeConsumerSupport.delegate(Materials2Shapes.lens, OrePrefixes.lens, () -> ProcessingLens.INSTANCE);
    }
}
