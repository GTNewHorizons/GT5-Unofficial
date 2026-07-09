package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.loaders.oreprocessing.ProcessingIngot;

/// Dispatches [ProcessingIngot]'s `ingot`/`ingotHot` recipe generation for MaterialLib's cutover ingot shapes.
public final class ConsumerIngot {

    private ConsumerIngot() {}

    static void register() {
        ShapeConsumerSupport.delegate(Materials2Shapes.shapeIngot, OrePrefixes.ingot, ProcessingIngot.INSTANCE);
        ShapeConsumerSupport.delegate(Materials2Shapes.shapeIngotHot, OrePrefixes.ingotHot, ProcessingIngot.INSTANCE);
    }
}
