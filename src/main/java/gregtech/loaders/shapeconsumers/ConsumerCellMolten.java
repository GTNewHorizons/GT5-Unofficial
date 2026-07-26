package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2CellShapes;
import gregtech.loaders.oreprocessing.ProcessingCellMolten;

/// Dispatches [ProcessingCellMolten]'s molten-fluid extraction and container registration for MaterialLib's
/// `cellMolten` shape.
public final class ConsumerCellMolten {

    private ConsumerCellMolten() {}

    static void register() {
        ShapeConsumerSupport
            .delegate(Materials2CellShapes.cellMolten, OrePrefixes.cellMolten, () -> ProcessingCellMolten.INSTANCE);
    }
}
