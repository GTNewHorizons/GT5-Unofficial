package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Shapes;
import gregtech.loaders.oreprocessing.ProcessingCrushedOre;

/// Dispatches [ProcessingCrushedOre]'s `crushedCentrifuged`/`crushedPurified` recipe generation for
/// MaterialLib's cutover crushed-ore shapes.
public final class ConsumerCrushedOre {

    private ConsumerCrushedOre() {}

    static void register() {
        ShapeConsumerSupport.delegate(
            Shapes.crushedCentrifuged,
            OrePrefixes.crushedCentrifuged,
            () -> ProcessingCrushedOre.INSTANCE);
        ShapeConsumerSupport.delegate(
            Shapes.crushedPurified,
            OrePrefixes.crushedPurified,
            () -> ProcessingCrushedOre.INSTANCE);
    }
}
