package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.loaders.oreprocessing.ProcessingGear;

/// Dispatches [ProcessingGear]'s `gearGt`/`gearGtSmall` recipe generation for MaterialLib's cutover gear
/// shapes.
public final class ConsumerGear {

    private ConsumerGear() {}

    static void register() {
        ShapeConsumerSupport.delegate(Materials2Shapes.shapeGearGt, OrePrefixes.gearGt, () -> ProcessingGear.INSTANCE);
        ShapeConsumerSupport
            .delegate(Materials2Shapes.shapeGearGtSmall, OrePrefixes.gearGtSmall, () -> ProcessingGear.INSTANCE);
    }
}
