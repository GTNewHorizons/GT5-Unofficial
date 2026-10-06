package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Shapes;
import gregtech.loaders.oreprocessing.ProcessingRawOre;

/// Generates raw-ore recipes through [ProcessingRawOre].
public final class ConsumerRawOre {

    private ConsumerRawOre() {}

    static void register() {
        ShapeConsumerSupport.delegate(Shapes.rawOre, OrePrefixes.rawOre, () -> ProcessingRawOre.INSTANCE);
    }
}
