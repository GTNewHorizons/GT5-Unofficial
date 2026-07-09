package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2CellShapes;
import gregtech.loaders.oreprocessing.ProcessingCell;

/// Dispatches [ProcessingCell]'s `cell`/`cellPlasma` recipe generation for MaterialLib's cutover cell shapes.
/// The one foreign-item-only fragment in [ProcessingCell]'s body (an `aModName.equalsIgnoreCase("AtomicScience")`
/// gate on an empty-cell extractor recipe) simply never matches here, since delegated calls always pass
/// `"materiallib"` -- correct, since that recipe was never meant for MaterialLib's own empty-cell stack either.
public final class ConsumerCell {

    private ConsumerCell() {}

    static void register() {
        ShapeConsumerSupport.delegate(Materials2CellShapes.shapeCell, OrePrefixes.cell, ProcessingCell.INSTANCE);
        ShapeConsumerSupport
            .delegate(Materials2CellShapes.shapeCellPlasma, OrePrefixes.cellPlasma, ProcessingCell.INSTANCE);
        ShapeConsumerSupport
            .delegate(Materials2CellShapes.shapeCellPlasmaLight, OrePrefixes.cellPlasma, ProcessingCell.INSTANCE);
    }
}
