package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.OreShapes;
import gregtech.api.enums.materials.Shapes;
import gregtech.loaders.oreprocessing.ProcessingIceOre;

/// Dispatches [ProcessingIceOre]'s `ore`/`rawOre` recipe generation. A second, independent registrator on
/// those prefixes alongside [ProcessingOre]/[ProcessingRawOre].
public final class ConsumerIceOre {

    private ConsumerIceOre() {}

    static void register() {
        ShapeConsumerSupport.delegate(OreShapes.ore, OrePrefixes.ore, () -> ProcessingIceOre.INSTANCE);
        ShapeConsumerSupport.delegate(Shapes.rawOre, OrePrefixes.rawOre, () -> ProcessingIceOre.INSTANCE);
    }
}
