package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2BlockShapes;
import gregtech.loaders.oreprocessing.ProcessingSheetmetal;

/// Dispatches [ProcessingSheetmetal]'s bender recipe for the `sheetmetal` shape, to every material that
/// generates the `sheetmetal` prefix.
public final class ConsumerSheetmetal {

    private ConsumerSheetmetal() {}

    static void register() {
        ShapeConsumerSupport
            .delegate(Materials2BlockShapes.sheetmetal, OrePrefixes.sheetmetal, () -> ProcessingSheetmetal.INSTANCE);
    }
}
