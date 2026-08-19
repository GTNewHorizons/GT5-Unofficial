package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.OreShapes;
import gregtech.api.enums.materials.Shapes;
import gregtech.loaders.oreprocessing.ProcessingIceOre;

/// Dispatches [ProcessingIceOre]'s `ore`/`rawOre` recipe generation. A second, independent registrator on
/// those prefixes alongside [ProcessingOre]/[ProcessingRawOre]. The ore line fans out per stone variant (see
/// [ShapeConsumerSupport#delegateOreVariants]), which is what reaches the ice stones ice-ore materials
/// actually generate on; `rawOre` is an item shape with no variants.
public final class ConsumerIceOre {

    private ConsumerIceOre() {}

    static void register() {
        ShapeConsumerSupport
            .delegateOreVariants(OreShapes.ore, OrePrefixes.ore, false, () -> ProcessingIceOre.INSTANCE);
        ShapeConsumerSupport.delegate(Shapes.rawOre, OrePrefixes.rawOre, () -> ProcessingIceOre.INSTANCE);
    }
}
