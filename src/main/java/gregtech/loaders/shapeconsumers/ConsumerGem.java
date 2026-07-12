package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.loaders.oreprocessing.ProcessingGem;

/// Dispatches [ProcessingGem]'s recipe generation for MaterialLib's cutover gem-family shapes.
public final class ConsumerGem {

    private ConsumerGem() {}

    static void register() {
        ShapeConsumerSupport.delegate(Materials2Shapes.gem, OrePrefixes.gem, () -> ProcessingGem.INSTANCE);
        ShapeConsumerSupport
            .delegate(Materials2Shapes.gemChipped, OrePrefixes.gemChipped, () -> ProcessingGem.INSTANCE);
        ShapeConsumerSupport
            .delegate(Materials2Shapes.gemExquisite, OrePrefixes.gemExquisite, () -> ProcessingGem.INSTANCE);
        ShapeConsumerSupport.delegate(Materials2Shapes.gemFlawed, OrePrefixes.gemFlawed, () -> ProcessingGem.INSTANCE);
        ShapeConsumerSupport
            .delegate(Materials2Shapes.gemFlawless, OrePrefixes.gemFlawless, () -> ProcessingGem.INSTANCE);
    }
}
