package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2BlockShapes;
import gregtech.loaders.oreprocessing.ProcessingBlock;

/// Dispatches [ProcessingBlock]'s `block`-prefix recipe generation for MaterialLib's cutover storage-block
/// shape.
public final class ConsumerBlock {

    private ConsumerBlock() {}

    static void register() {
        ShapeConsumerSupport.delegate(Materials2BlockShapes.shapeBlock, OrePrefixes.block, ProcessingBlock.INSTANCE);
    }
}
