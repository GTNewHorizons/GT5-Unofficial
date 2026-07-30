package gregtech.loaders.shapeconsumers;

import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials;
import gregtech.api.enums.materials2.Materials2PipeShapes;
import gregtech.loaders.oreprocessing.ProcessingPipe;

/// Dispatches [ProcessingPipe]'s recipe generation for the twelve pipe prefixes. The fluid and item shapes
/// sharing a `pipeTiny`..`pipeHuge` prefix string are both delegated under that prefix (their material sets
/// are disjoint, see [Materials2PipeShapes]), matching the legacy oredict dispatch that fired for either
/// family's registrations.
///
/// The High Pressure (Redstone) fluid pipes are skipped: their oredict identity is the tier-keyed
/// `pipeSmallUltimate`..`pipeLargeUltimate` names (registered by
/// `gregtech.loaders.preload.LoaderMaterialLibCutover`), which the legacy dispatch never resolved a material
/// for, so they never had generated recipes.
public final class ConsumerPipe {

    private ConsumerPipe() {}

    static void register() {
        fluid(Materials2PipeShapes.pipeTiny, OrePrefixes.pipeTiny);
        fluid(Materials2PipeShapes.pipeSmall, OrePrefixes.pipeSmall);
        fluid(Materials2PipeShapes.pipeMedium, OrePrefixes.pipeMedium);
        fluid(Materials2PipeShapes.pipeLarge, OrePrefixes.pipeLarge);
        fluid(Materials2PipeShapes.pipeHuge, OrePrefixes.pipeHuge);
        fluid(Materials2PipeShapes.pipeQuadruple, OrePrefixes.pipeQuadruple);
        fluid(Materials2PipeShapes.pipeNonuple, OrePrefixes.pipeNonuple);

        ShapeConsumerSupport
            .delegate(Materials2PipeShapes.itemPipeTiny, OrePrefixes.pipeTiny, () -> ProcessingPipe.INSTANCE);
        ShapeConsumerSupport
            .delegate(Materials2PipeShapes.itemPipeSmall, OrePrefixes.pipeSmall, () -> ProcessingPipe.INSTANCE);
        ShapeConsumerSupport
            .delegate(Materials2PipeShapes.itemPipeMedium, OrePrefixes.pipeMedium, () -> ProcessingPipe.INSTANCE);
        ShapeConsumerSupport
            .delegate(Materials2PipeShapes.itemPipeLarge, OrePrefixes.pipeLarge, () -> ProcessingPipe.INSTANCE);
        ShapeConsumerSupport
            .delegate(Materials2PipeShapes.itemPipeHuge, OrePrefixes.pipeHuge, () -> ProcessingPipe.INSTANCE);
        ShapeConsumerSupport.delegate(
            Materials2PipeShapes.itemPipeRestrictiveTiny,
            OrePrefixes.pipeRestrictiveTiny,
            () -> ProcessingPipe.INSTANCE);
        ShapeConsumerSupport.delegate(
            Materials2PipeShapes.itemPipeRestrictiveSmall,
            OrePrefixes.pipeRestrictiveSmall,
            () -> ProcessingPipe.INSTANCE);
        ShapeConsumerSupport.delegate(
            Materials2PipeShapes.itemPipeRestrictiveMedium,
            OrePrefixes.pipeRestrictiveMedium,
            () -> ProcessingPipe.INSTANCE);
        ShapeConsumerSupport.delegate(
            Materials2PipeShapes.itemPipeRestrictiveLarge,
            OrePrefixes.pipeRestrictiveLarge,
            () -> ProcessingPipe.INSTANCE);
        ShapeConsumerSupport.delegate(
            Materials2PipeShapes.itemPipeRestrictiveHuge,
            OrePrefixes.pipeRestrictiveHuge,
            () -> ProcessingPipe.INSTANCE);
    }

    private static void fluid(Shape shape, OrePrefixes prefix) {
        ShapeConsumerSupport.delegate(
            shape,
            prefix,
            material -> material != Materials.Redstone,
            () -> ProcessingPipe.INSTANCE);
    }
}
