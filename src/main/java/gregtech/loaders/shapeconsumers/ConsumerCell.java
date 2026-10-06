package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.CellShapes;
import gregtech.loaders.oreprocessing.ProcessingCell;

/// Generates cell and plasma-cell recipes through [ProcessingCell].
public final class ConsumerCell {

    private ConsumerCell() {}

    static void register() {
        ShapeConsumerSupport.delegate(CellShapes.cell, OrePrefixes.cell, () -> ProcessingCell.INSTANCE);
        ShapeConsumerSupport.delegate(CellShapes.cellGas, OrePrefixes.cell, () -> ProcessingCell.INSTANCE);
        ShapeConsumerSupport.delegate(CellShapes.cellSolid, OrePrefixes.cell, () -> ProcessingCell.INSTANCE);
        ShapeConsumerSupport.delegate(CellShapes.cellPlasma, OrePrefixes.cellPlasma, () -> ProcessingCell.INSTANCE);
        ShapeConsumerSupport
            .delegate(CellShapes.cellPlasmaLight, OrePrefixes.cellPlasma, () -> ProcessingCell.INSTANCE);
    }
}
