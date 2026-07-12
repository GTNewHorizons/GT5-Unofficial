package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.loaders.oreprocessing.ProcessingNugget;

/// Dispatches [ProcessingNugget]'s `nugget`-prefix recipe generation for MaterialLib's cutover nugget shape.
public final class ConsumerNugget {

    private ConsumerNugget() {}

    static void register() {
        ShapeConsumerSupport.delegate(Materials2Shapes.nugget, OrePrefixes.nugget, () -> ProcessingNugget.INSTANCE);
    }
}
