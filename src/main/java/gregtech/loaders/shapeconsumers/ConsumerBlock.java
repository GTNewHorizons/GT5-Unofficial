package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.BlockShapes;
import gregtech.loaders.oreprocessing.ProcessingBlock;

/// Generates storage-block recipes through [ProcessingBlock].
public final class ConsumerBlock {

    private ConsumerBlock() {}

    static void register() {
        ShapeConsumerSupport.delegate(BlockShapes.block, OrePrefixes.block, () -> ProcessingBlock.INSTANCE);
    }
}
