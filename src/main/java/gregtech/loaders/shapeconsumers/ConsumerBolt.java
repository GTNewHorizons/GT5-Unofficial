package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.loaders.oreprocessing.ProcessingBolt;

/// Dispatches [ProcessingBolt]'s `bolt`-prefix recipe generation for MaterialLib's cutover bolt shape.
public final class ConsumerBolt {

    private ConsumerBolt() {}

    static void register() {
        ShapeConsumerSupport.delegate(Materials2Shapes.bolt, OrePrefixes.bolt, () -> ProcessingBolt.INSTANCE);
    }
}
