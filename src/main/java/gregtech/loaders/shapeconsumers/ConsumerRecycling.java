package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.CellShapes;
import gregtech.api.material.MaterialParts;
import gregtech.loaders.oreprocessing.ProcessingRecycling;

/// Dispatches [ProcessingRecycling]'s recipe generation (emptying a fluid-filled container to its dust) for
/// every material-based container prefix that carries material, scoped to prefixes with a MaterialLib shape via
/// [MaterialParts#shape]. `cellPlasma` additionally generates [CellShapes#cellPlasmaLight] for materials with no
/// molten fluid (see [MaterialParts]'s javadoc on its ordered-candidate-list exception) and `cell` additionally
/// generates [CellShapes#cellGas] for gas materials and [CellShapes#cellSolid] for materials with no fluid, so
/// each is registered as a further target explicitly; every other matching prefix has exactly one candidate
/// shape.
public final class ConsumerRecycling {

    private ConsumerRecycling() {}

    static void register() {
        for (OrePrefixes prefix : OrePrefixes.VALUES) {
            if (!prefix.isMaterialBased() || prefix.getMaterialAmount() <= 0 || !prefix.isContainer()) continue;
            // Bolted casings are marked as containers but hold no fluid to empty
            if (prefix == OrePrefixes.blockCasing || prefix == OrePrefixes.blockCasingAdvanced) continue;
            ShapeConsumerSupport.delegate(MaterialParts.shape(prefix), prefix, () -> ProcessingRecycling.INSTANCE);
        }
        ShapeConsumerSupport
            .delegate(CellShapes.cellPlasmaLight, OrePrefixes.cellPlasma, () -> ProcessingRecycling.INSTANCE);
        ShapeConsumerSupport.delegate(CellShapes.cellGas, OrePrefixes.cell, () -> ProcessingRecycling.INSTANCE);
        ShapeConsumerSupport.delegate(CellShapes.cellSolid, OrePrefixes.cell, () -> ProcessingRecycling.INSTANCE);
    }
}
