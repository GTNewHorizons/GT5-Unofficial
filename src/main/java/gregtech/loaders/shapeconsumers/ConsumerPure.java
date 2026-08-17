package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Shapes;
import gregtech.loaders.oreprocessing.ProcessingPure;

/// Dispatches [ProcessingPure]'s `crushedPurified`-prefix recipe generation for MaterialLib's cutover
/// crushed-purified-ore shape. Of the three prefixes [ProcessingPure] shares one body across
/// (`crushedPurified`, `cleanGravel`, `reduced`), only `crushedPurified` is cut over; the other two have no
/// MaterialLib shape.
public final class ConsumerPure {

    private ConsumerPure() {}

    static void register() {
        ShapeConsumerSupport
            .delegate(Shapes.crushedPurified, OrePrefixes.crushedPurified, () -> ProcessingPure.INSTANCE);
    }
}
