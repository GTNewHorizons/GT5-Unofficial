package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.OreShapes;
import gregtech.loaders.oreprocessing.ProcessingOre;

/// Dispatches [ProcessingOre]'s ore recipe generation for MaterialLib's cutover ore shape, once per stone
/// variant a material carries a block on, under that stone's own `ore*` prefix (see
/// [ShapeConsumerSupport#delegateOreVariants]).
public final class ConsumerOre {

    private ConsumerOre() {}

    static void register() {
        ShapeConsumerSupport.delegateOreVariants(OreShapes.ore, OrePrefixes.ore, false, () -> ProcessingOre.INSTANCE);
    }
}
