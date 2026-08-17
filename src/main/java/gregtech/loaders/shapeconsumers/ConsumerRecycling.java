package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.CellShapes;
import gregtech.api.material.MaterialParts;
import gregtech.loaders.oreprocessing.ProcessingRecycling;

/// Dispatches [ProcessingRecycling]'s recipe generation (emptying a fluid-filled container to its dust) for
/// every cut-over prefix its legacy dynamic registration targets (`isMaterialBased() && getMaterialAmount() >
/// 0 && isContainer()`, mirrored here instead of hand-listing the nine cell-family prefixes it matches),
/// scoped to prefixes with a MaterialLib shape via [MaterialParts#shape]. `cellPlasma` additionally generates
/// [CellShapes#cellPlasmaLight] for materials with no molten fluid (see [MaterialParts]'s javadoc on its
/// ordered-candidate-list exception) and `cell` additionally generates [CellShapes#cellGas] for gas materials,
/// so each is registered as a second target explicitly; every other matching prefix has exactly one candidate
/// shape. `bucket`/`bucketClay`/`bottle`/`capsule`/`capsuleMolten`/`blockCasing`/`blockCasingAdvanced` also
/// match the legacy filter but have no MaterialLib shape.
public final class ConsumerRecycling {

    private ConsumerRecycling() {}

    static void register() {
        for (OrePrefixes prefix : OrePrefixes.VALUES) {
            if (!prefix.isMaterialBased() || prefix.getMaterialAmount() <= 0 || !prefix.isContainer()) continue;
            ShapeConsumerSupport.delegate(MaterialParts.shape(prefix), prefix, () -> ProcessingRecycling.INSTANCE);
        }
        ShapeConsumerSupport
            .delegate(CellShapes.cellPlasmaLight, OrePrefixes.cellPlasma, () -> ProcessingRecycling.INSTANCE);
        ShapeConsumerSupport.delegate(CellShapes.cellGas, OrePrefixes.cell, () -> ProcessingRecycling.INSTANCE);
    }
}
