package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Shapes;
import gregtech.loaders.oreprocessing.ProcessingNugget;

/// Generates nugget recipes through [ProcessingNugget].
public final class ConsumerNugget {

    private ConsumerNugget() {}

    static void register() {
        ShapeConsumerSupport.delegate(Shapes.nugget, OrePrefixes.nugget, () -> ProcessingNugget.INSTANCE);
    }
}
