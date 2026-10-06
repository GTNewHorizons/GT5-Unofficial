package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Shapes;
import gregtech.loaders.oreprocessing.ProcessingIngot;

/// Generates ingot and hot-ingot recipes through [ProcessingIngot].
public final class ConsumerIngot {

    private ConsumerIngot() {}

    static void register() {
        ShapeConsumerSupport.delegate(Shapes.ingot, OrePrefixes.ingot, () -> ProcessingIngot.INSTANCE);
        ShapeConsumerSupport.delegate(Shapes.ingotHot, OrePrefixes.ingotHot, () -> ProcessingIngot.INSTANCE);
    }
}
