package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.OreShapes;
import gregtech.loaders.oreprocessing.ProcessingOre;

/// Dispatches [ProcessingOre]'s `ore`-prefix recipe generation for MaterialLib's cutover ore shape. Of the
/// legacy dimension-variant `ore*` prefixes [ProcessingOre] also registers on, only `ore` itself is
/// material-based and cut over; the rest have no MaterialLib shape.
public final class ConsumerOre {

    private ConsumerOre() {}

    static void register() {
        ShapeConsumerSupport.delegate(OreShapes.ore, OrePrefixes.ore, () -> ProcessingOre.INSTANCE);
    }
}
