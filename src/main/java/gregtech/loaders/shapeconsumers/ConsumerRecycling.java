package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.CellShapes;
import gregtech.api.material.MaterialParts;
import gregtech.loaders.oreprocessing.ProcessingRecycling;

/// Generates canner recipes emptying a solid-filled container into dust through [ProcessingRecycling], for every
/// material-carrying container prefix with a MaterialLib shape.
///
/// [MaterialParts#shape] gives only a prefix's first candidate shape, so the alternate cell shapes
/// ([CellShapes#cellPlasmaLight], [CellShapes#cellGas], [CellShapes#cellSolid]) are registered explicitly.
public final class ConsumerRecycling {

    private ConsumerRecycling() {}

    static void register() {
        for (OrePrefixes prefix : OrePrefixes.VALUES) {
            if (!prefix.isMaterialBased() || prefix.getMaterialAmount() <= 0 || !prefix.isContainer()) continue;
            // Bolted casings are marked as containers but hold nothing to empty
            if (prefix == OrePrefixes.blockCasing || prefix == OrePrefixes.blockCasingAdvanced) continue;
            ShapeConsumerSupport.delegate(MaterialParts.shape(prefix), prefix, () -> ProcessingRecycling.INSTANCE);
        }
        ShapeConsumerSupport
            .delegate(CellShapes.cellPlasmaLight, OrePrefixes.cellPlasma, () -> ProcessingRecycling.INSTANCE);
        ShapeConsumerSupport.delegate(CellShapes.cellGas, OrePrefixes.cell, () -> ProcessingRecycling.INSTANCE);
        ShapeConsumerSupport.delegate(CellShapes.cellSolid, OrePrefixes.cell, () -> ProcessingRecycling.INSTANCE);
    }
}
