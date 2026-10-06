package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Shapes;
import gregtech.loaders.oreprocessing.ProcessingBolt;

/// Generates bolt recipes through [ProcessingBolt].
public final class ConsumerBolt {

    private ConsumerBolt() {}

    static void register() {
        ShapeConsumerSupport.delegate(Shapes.bolt, OrePrefixes.bolt, () -> ProcessingBolt.INSTANCE);
    }
}
