package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Shapes;
import gregtech.loaders.oreprocessing.ProcessingShaping;

/// Generates extruder, mold and alloy-smelter shaping recipes from ingots and dusts through [ProcessingShaping].
public final class ConsumerShaping {

    private ConsumerShaping() {}

    static void register() {
        ShapeConsumerSupport.delegate(Shapes.ingot, OrePrefixes.ingot, () -> ProcessingShaping.INSTANCE);
        ShapeConsumerSupport.delegate(Shapes.dust, OrePrefixes.dust, () -> ProcessingShaping.INSTANCE);
    }
}
