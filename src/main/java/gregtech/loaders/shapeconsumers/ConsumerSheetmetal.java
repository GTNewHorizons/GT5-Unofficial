package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.BlockShapes;
import gregtech.loaders.oreprocessing.ProcessingSheetmetal;

/// Generates the sheetmetal bender recipe through [ProcessingSheetmetal].
public final class ConsumerSheetmetal {

    private ConsumerSheetmetal() {}

    static void register() {
        ShapeConsumerSupport
            .delegate(BlockShapes.sheetmetal, OrePrefixes.sheetmetal, () -> ProcessingSheetmetal.INSTANCE);
    }
}
