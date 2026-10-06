package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.OreShapes;
import gregtech.loaders.oreprocessing.ProcessingOre;

/// Generates ore-block recipes through [ProcessingOre], once per stone variant under that stone's own `ore*` prefix
/// (see [ShapeConsumerSupport#delegateOreVariants]).
public final class ConsumerOre {

    private ConsumerOre() {}

    static void register() {
        ShapeConsumerSupport.delegateOreVariants(OreShapes.ore, OrePrefixes.ore, () -> ProcessingOre.INSTANCE);
    }
}
