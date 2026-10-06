package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Shapes;
import gregtech.loaders.oreprocessing.ProcessingGem;

/// Generates gem-family recipes through [ProcessingGem].
public final class ConsumerGem {

    private ConsumerGem() {}

    static void register() {
        ShapeConsumerSupport.delegate(Shapes.gem, OrePrefixes.gem, () -> ProcessingGem.INSTANCE);
        ShapeConsumerSupport.delegate(Shapes.gemChipped, OrePrefixes.gemChipped, () -> ProcessingGem.INSTANCE);
        ShapeConsumerSupport.delegate(Shapes.gemExquisite, OrePrefixes.gemExquisite, () -> ProcessingGem.INSTANCE);
        ShapeConsumerSupport.delegate(Shapes.gemFlawed, OrePrefixes.gemFlawed, () -> ProcessingGem.INSTANCE);
        ShapeConsumerSupport.delegate(Shapes.gemFlawless, OrePrefixes.gemFlawless, () -> ProcessingGem.INSTANCE);
    }
}
