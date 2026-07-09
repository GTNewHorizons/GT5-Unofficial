package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2CellShapes;
import gregtech.api.material.MU;
import gregtech.loaders.oreprocessing.ProcessingRecycling;

/// Dispatches [ProcessingRecycling]'s recipe generation (emptying a fluid-filled container to its dust) for
/// every cut-over prefix its legacy dynamic registration targets (`isMaterialBased() && getMaterialAmount() >
/// 0 && isContainer()`, mirrored here instead of hand-listing the nine cell-family prefixes it matches),
/// scoped to prefixes with a MaterialLib shape via [MU#shape]. `cellPlasma` additionally generates
/// [Materials2CellShapes#shapeCellPlasmaLight] for materials with no molten fluid (see [MU]'s javadoc on its
/// ordered-candidate-list exception), so it is registered as a second target explicitly; every other matching
/// prefix has exactly one candidate shape. `bucket`/`bucketClay`/`bottle`/`capsule`/`capsuleMolten`/
/// `blockCasing`/`blockCasingAdvanced` also match the legacy filter but have no MaterialLib shape and keep
/// serving foreign mods exclusively through the legacy oredict path.
public final class ConsumerRecycling {

    private ConsumerRecycling() {}

    static void register() {
        for (OrePrefixes prefix : OrePrefixes.VALUES) {
            if (!prefix.isMaterialBased() || prefix.getMaterialAmount() <= 0 || !prefix.isContainer()) continue;
            ShapeConsumerSupport.delegate(MU.shape(prefix), prefix, ProcessingRecycling.INSTANCE);
        }
        ShapeConsumerSupport
            .delegate(Materials2CellShapes.shapeCellPlasmaLight, OrePrefixes.cellPlasma, ProcessingRecycling.INSTANCE);
    }
}
