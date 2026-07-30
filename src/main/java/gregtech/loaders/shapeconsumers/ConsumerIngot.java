package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Shapes;
import gregtech.loaders.oreprocessing.ProcessingIngot;

/// Dispatches [ProcessingIngot]'s `ingot`/`ingotHot` recipe generation for MaterialLib's cutover ingot shapes.
public final class ConsumerIngot {

    private ConsumerIngot() {}

    static void register() {
        ShapeConsumerSupport.delegate(Shapes.ingot, OrePrefixes.ingot, () -> ProcessingIngot.INSTANCE);
        ShapeConsumerSupport.delegate(Shapes.ingotHot, OrePrefixes.ingotHot, () -> ProcessingIngot.INSTANCE);
    }
}
