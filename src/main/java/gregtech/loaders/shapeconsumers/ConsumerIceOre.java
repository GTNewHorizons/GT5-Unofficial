package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.OreShapes;
import gregtech.api.enums.materials.Shapes;
import gregtech.loaders.oreprocessing.ProcessingIceOre;

/// Generates ice-ore recipes through [ProcessingIceOre]. Ore blocks dispatch once per stone variant, which is what
/// reaches the ice stones (see [ShapeConsumerSupport#delegateOreVariants]).
public final class ConsumerIceOre {

    private ConsumerIceOre() {}

    static void register() {
        ShapeConsumerSupport.delegateOreVariants(OreShapes.ore, OrePrefixes.ore, () -> ProcessingIceOre.INSTANCE);
        ShapeConsumerSupport.delegate(Shapes.rawOre, OrePrefixes.rawOre, () -> ProcessingIceOre.INSTANCE);
    }
}
