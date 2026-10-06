package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Shapes;
import gregtech.loaders.oreprocessing.ProcessingOreSmelting;

/// Generates blast-furnace smelting recipes for crushed ores and dusts through [ProcessingOreSmelting].
public final class ConsumerOreSmelting {

    private ConsumerOreSmelting() {}

    static void register() {
        ShapeConsumerSupport.delegate(Shapes.crushed, OrePrefixes.crushed, () -> ProcessingOreSmelting.INSTANCE);
        ShapeConsumerSupport
            .delegate(Shapes.crushedPurified, OrePrefixes.crushedPurified, () -> ProcessingOreSmelting.INSTANCE);
        ShapeConsumerSupport
            .delegate(Shapes.crushedCentrifuged, OrePrefixes.crushedCentrifuged, () -> ProcessingOreSmelting.INSTANCE);
        ShapeConsumerSupport.delegate(Shapes.dust, OrePrefixes.dust, () -> ProcessingOreSmelting.INSTANCE);
        ShapeConsumerSupport.delegate(Shapes.dustImpure, OrePrefixes.dustImpure, () -> ProcessingOreSmelting.INSTANCE);
        ShapeConsumerSupport.delegate(Shapes.dustPure, OrePrefixes.dustPure, () -> ProcessingOreSmelting.INSTANCE);
    }
}
