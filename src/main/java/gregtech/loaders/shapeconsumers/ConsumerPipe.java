package gregtech.loaders.shapeconsumers;

import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.TEBlockShapes;
import gregtech.loaders.oreprocessing.ProcessingPipe;

/// Dispatches [ProcessingPipe]'s recipe generation for the twelve pipe prefixes. The fluid and item shapes
/// sharing a `pipeTiny`..`pipeHuge` prefix string are both delegated under that prefix; their material sets
/// are disjoint, see [TEBlockShapes].
///
/// The High Pressure (Redstone) fluid pipes are skipped: their oredict identity is the tier-keyed
/// `pipeSmallUltimate`..`pipeLargeUltimate` names (registered by
/// `gregtech.loaders.preload.LoaderMaterialLibCutover`), and they have no generated recipes.
public final class ConsumerPipe {

    private ConsumerPipe() {}

    static void register() {
        fluid(TEBlockShapes.pipeTiny, OrePrefixes.pipeTiny);
        fluid(TEBlockShapes.pipeSmall, OrePrefixes.pipeSmall);
        fluid(TEBlockShapes.pipeMedium, OrePrefixes.pipeMedium);
        fluid(TEBlockShapes.pipeLarge, OrePrefixes.pipeLarge);
        fluid(TEBlockShapes.pipeHuge, OrePrefixes.pipeHuge);
        fluid(TEBlockShapes.pipeQuadruple, OrePrefixes.pipeQuadruple);
        fluid(TEBlockShapes.pipeNonuple, OrePrefixes.pipeNonuple);

        ShapeConsumerSupport.delegate(TEBlockShapes.itemPipeTiny, OrePrefixes.pipeTiny, () -> ProcessingPipe.INSTANCE);
        ShapeConsumerSupport
            .delegate(TEBlockShapes.itemPipeSmall, OrePrefixes.pipeSmall, () -> ProcessingPipe.INSTANCE);
        ShapeConsumerSupport
            .delegate(TEBlockShapes.itemPipeMedium, OrePrefixes.pipeMedium, () -> ProcessingPipe.INSTANCE);
        ShapeConsumerSupport
            .delegate(TEBlockShapes.itemPipeLarge, OrePrefixes.pipeLarge, () -> ProcessingPipe.INSTANCE);
        ShapeConsumerSupport.delegate(TEBlockShapes.itemPipeHuge, OrePrefixes.pipeHuge, () -> ProcessingPipe.INSTANCE);
        ShapeConsumerSupport.delegate(
            TEBlockShapes.itemPipeRestrictiveTiny,
            OrePrefixes.pipeRestrictiveTiny,
            () -> ProcessingPipe.INSTANCE);
        ShapeConsumerSupport.delegate(
            TEBlockShapes.itemPipeRestrictiveSmall,
            OrePrefixes.pipeRestrictiveSmall,
            () -> ProcessingPipe.INSTANCE);
        ShapeConsumerSupport.delegate(
            TEBlockShapes.itemPipeRestrictiveMedium,
            OrePrefixes.pipeRestrictiveMedium,
            () -> ProcessingPipe.INSTANCE);
        ShapeConsumerSupport.delegate(
            TEBlockShapes.itemPipeRestrictiveLarge,
            OrePrefixes.pipeRestrictiveLarge,
            () -> ProcessingPipe.INSTANCE);
        ShapeConsumerSupport.delegate(
            TEBlockShapes.itemPipeRestrictiveHuge,
            OrePrefixes.pipeRestrictiveHuge,
            () -> ProcessingPipe.INSTANCE);
    }

    private static void fluid(Shape shape, OrePrefixes prefix) {
        ShapeConsumerSupport
            .delegate(shape, prefix, material -> material != Materials.Redstone, () -> ProcessingPipe.INSTANCE);
    }
}
