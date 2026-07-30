package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Shapes;
import gregtech.loaders.oreprocessing.ProcessingGear;

/// Dispatches [ProcessingGear]'s `gearGt`/`gearGtSmall` recipe generation for MaterialLib's cutover gear
/// shapes.
public final class ConsumerGear {

    private ConsumerGear() {}

    static void register() {
        ShapeConsumerSupport.delegate(Shapes.gearGt, OrePrefixes.gearGt, () -> ProcessingGear.INSTANCE);
        ShapeConsumerSupport.delegate(Shapes.gearGtSmall, OrePrefixes.gearGtSmall, () -> ProcessingGear.INSTANCE);
    }
}
