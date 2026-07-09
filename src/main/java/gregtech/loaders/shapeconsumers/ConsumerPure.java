package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.loaders.oreprocessing.ProcessingPure;

/// Dispatches [ProcessingPure]'s `crushedPurified`-prefix recipe generation for MaterialLib's cutover
/// crushed-purified-ore shape. Of the three prefixes [ProcessingPure] shares one body across
/// (`crushedPurified`, `cleanGravel`, `reduced`), only `crushedPurified` is cut over; the other two have no
/// MaterialLib shape and keep serving foreign mods exclusively through the legacy oredict path.
public final class ConsumerPure {

    private ConsumerPure() {}

    static void register() {
        ShapeConsumerSupport
            .delegate(Materials2Shapes.shapeCrushedPurified, OrePrefixes.crushedPurified, ProcessingPure.INSTANCE);
    }
}
