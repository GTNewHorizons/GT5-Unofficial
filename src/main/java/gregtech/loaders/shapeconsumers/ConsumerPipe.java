package gregtech.loaders.shapeconsumers;

import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.PipeMaterials;
import gregtech.api.enums.materials.TEBlockShapes;
import gregtech.loaders.oreprocessing.ProcessingPipe;

/// Generates fluid and item pipe recipes through [ProcessingPipe].
///
/// Fluid and item pipes share the `pipeTiny`..`pipeHuge` prefixes. Their material sets are disjoint, see
/// [TEBlockShapes].
/// High Pressure (Redstone) fluid pipes are skipped; they have no generated recipes.
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
        ShapeConsumerSupport.delegate(
            shape,
            prefix,
            material -> !PipeMaterials.isHighPressurePipe(prefix, material),
            () -> ProcessingPipe.INSTANCE);
    }
}
