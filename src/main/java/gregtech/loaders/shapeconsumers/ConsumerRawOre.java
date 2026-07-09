package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.loaders.oreprocessing.ProcessingRawOre;

/// Dispatches [ProcessingRawOre]'s `rawOre`-prefix recipe generation for MaterialLib's cutover raw-ore shape.
public final class ConsumerRawOre {

    private ConsumerRawOre() {}

    static void register() {
        ShapeConsumerSupport.delegate(Materials2Shapes.shapeRawOre, OrePrefixes.rawOre, ProcessingRawOre.INSTANCE);
    }
}
