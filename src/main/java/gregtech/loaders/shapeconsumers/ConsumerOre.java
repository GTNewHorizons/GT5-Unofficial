package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2OreShapes;
import gregtech.loaders.oreprocessing.ProcessingOre;

/// Dispatches [ProcessingOre]'s `ore`-prefix recipe generation for MaterialLib's cutover ore shape. Of the
/// ~45 legacy dimension-variant `ore*` prefixes [ProcessingOre] also registers on, only `ore` itself is
/// material-based and cut over; the rest have no MaterialLib shape and keep serving foreign mods exclusively
/// through the legacy oredict path.
public final class ConsumerOre {

    private ConsumerOre() {}

    static void register() {
        ShapeConsumerSupport.delegate(Materials2OreShapes.shapeOre, OrePrefixes.ore, ProcessingOre.INSTANCE);
    }
}
