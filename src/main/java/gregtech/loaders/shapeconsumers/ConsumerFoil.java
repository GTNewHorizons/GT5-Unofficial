package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Shapes;
import gregtech.loaders.oreprocessing.ProcessingFoil;

/// Generates foil recipes through [ProcessingFoil].
public final class ConsumerFoil {

    private ConsumerFoil() {}

    static void register() {
        ShapeConsumerSupport.delegate(Shapes.foil, OrePrefixes.foil, () -> ProcessingFoil.INSTANCE);
    }
}
