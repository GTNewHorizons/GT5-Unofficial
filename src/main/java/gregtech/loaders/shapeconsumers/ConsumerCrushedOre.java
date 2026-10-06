package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Shapes;
import gregtech.loaders.oreprocessing.ProcessingCrushedOre;

/// Generates centrifuged and purified crushed-ore recipes through [ProcessingCrushedOre].
public final class ConsumerCrushedOre {

    private ConsumerCrushedOre() {}

    static void register() {
        ShapeConsumerSupport
            .delegate(Shapes.crushedCentrifuged, OrePrefixes.crushedCentrifuged, () -> ProcessingCrushedOre.INSTANCE);
        ShapeConsumerSupport
            .delegate(Shapes.crushedPurified, OrePrefixes.crushedPurified, () -> ProcessingCrushedOre.INSTANCE);
    }
}
