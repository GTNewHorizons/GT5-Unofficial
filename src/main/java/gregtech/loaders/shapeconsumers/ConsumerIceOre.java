package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2OreShapes;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.loaders.oreprocessing.ProcessingIceOre;

/// Dispatches [ProcessingIceOre]'s `ore`/`rawOre` recipe generation (a second, independent registrator on
/// those prefixes alongside [ProcessingOre]/[ProcessingRawOre], mirroring the legacy multi-registrator list).
public final class ConsumerIceOre {

    private ConsumerIceOre() {}

    static void register() {
        ShapeConsumerSupport.delegate(Materials2OreShapes.shapeOre, OrePrefixes.ore, () -> ProcessingIceOre.INSTANCE);
        ShapeConsumerSupport
            .delegate(Materials2Shapes.shapeRawOre, OrePrefixes.rawOre, () -> ProcessingIceOre.INSTANCE);
    }
}
