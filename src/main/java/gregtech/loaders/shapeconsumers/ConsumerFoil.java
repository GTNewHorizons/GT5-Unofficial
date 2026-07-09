package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.loaders.oreprocessing.ProcessingFoil;

/// Dispatches [ProcessingFoil]'s `foil`-prefix recipe generation for MaterialLib's cutover foil shape.
public final class ConsumerFoil {

    private ConsumerFoil() {}

    static void register() {
        ShapeConsumerSupport.delegate(Materials2Shapes.shapeFoil, OrePrefixes.foil, () -> ProcessingFoil.INSTANCE);
    }
}
