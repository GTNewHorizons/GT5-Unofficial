package gregtech.loaders.shapeconsumers;

import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials;
import gregtech.api.enums.materials2.PipeShapes;
import gregtech.loaders.oreprocessing.ProcessingPipe;

/// Dispatches [ProcessingPipe]'s recipe generation for the twelve pipe prefixes. The fluid and item shapes
/// sharing a `pipeTiny`..`pipeHuge` prefix string are both delegated under that prefix (their material sets
/// are disjoint, see [PipeShapes]), matching the legacy oredict dispatch that fired for either
/// family's registrations.
///
/// The High Pressure (Redstone) fluid pipes are skipped: their oredict identity is the tier-keyed
/// `pipeSmallUltimate`..`pipeLargeUltimate` names (registered by
/// `gregtech.loaders.preload.LoaderMaterialLibCutover`), which the legacy dispatch never resolved a material
/// for, so they never had generated recipes.
public final class ConsumerPipe {

    private ConsumerPipe() {}

    static void register() {
        fluid(PipeShapes.pipeTiny, OrePrefixes.pipeTiny);
        fluid(PipeShapes.pipeSmall, OrePrefixes.pipeSmall);
        fluid(PipeShapes.pipeMedium, OrePrefixes.pipeMedium);
        fluid(PipeShapes.pipeLarge, OrePrefixes.pipeLarge);
        fluid(PipeShapes.pipeHuge, OrePrefixes.pipeHuge);
        fluid(PipeShapes.pipeQuadruple, OrePrefixes.pipeQuadruple);
        fluid(PipeShapes.pipeNonuple, OrePrefixes.pipeNonuple);

        ShapeConsumerSupport
            .delegate(PipeShapes.itemPipeTiny, OrePrefixes.pipeTiny, () -> ProcessingPipe.INSTANCE);
        ShapeConsumerSupport
            .delegate(PipeShapes.itemPipeSmall, OrePrefixes.pipeSmall, () -> ProcessingPipe.INSTANCE);
        ShapeConsumerSupport
            .delegate(PipeShapes.itemPipeMedium, OrePrefixes.pipeMedium, () -> ProcessingPipe.INSTANCE);
        ShapeConsumerSupport
            .delegate(PipeShapes.itemPipeLarge, OrePrefixes.pipeLarge, () -> ProcessingPipe.INSTANCE);
        ShapeConsumerSupport
            .delegate(PipeShapes.itemPipeHuge, OrePrefixes.pipeHuge, () -> ProcessingPipe.INSTANCE);
        ShapeConsumerSupport.delegate(
            PipeShapes.itemPipeRestrictiveTiny,
            OrePrefixes.pipeRestrictiveTiny,
            () -> ProcessingPipe.INSTANCE);
        ShapeConsumerSupport.delegate(
            PipeShapes.itemPipeRestrictiveSmall,
            OrePrefixes.pipeRestrictiveSmall,
            () -> ProcessingPipe.INSTANCE);
        ShapeConsumerSupport.delegate(
            PipeShapes.itemPipeRestrictiveMedium,
            OrePrefixes.pipeRestrictiveMedium,
            () -> ProcessingPipe.INSTANCE);
        ShapeConsumerSupport.delegate(
            PipeShapes.itemPipeRestrictiveLarge,
            OrePrefixes.pipeRestrictiveLarge,
            () -> ProcessingPipe.INSTANCE);
        ShapeConsumerSupport.delegate(
            PipeShapes.itemPipeRestrictiveHuge,
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
