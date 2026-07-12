package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.loaders.oreprocessing.ProcessingToolHead;

/// Dispatches [ProcessingToolHead]'s recipe generation for MaterialLib's cutover tool-head-family and
/// turbine-blade shapes.
public final class ConsumerToolHead {

    private ConsumerToolHead() {}

    static void register() {
        ShapeConsumerSupport
            .delegate(Materials2Shapes.toolHeadBuzzSaw, OrePrefixes.toolHeadBuzzSaw, () -> ProcessingToolHead.INSTANCE);
        ShapeConsumerSupport.delegate(
            Materials2Shapes.toolHeadChainsaw,
            OrePrefixes.toolHeadChainsaw,
            () -> ProcessingToolHead.INSTANCE);
        ShapeConsumerSupport
            .delegate(Materials2Shapes.toolHeadDrill, OrePrefixes.toolHeadDrill, () -> ProcessingToolHead.INSTANCE);
        ShapeConsumerSupport
            .delegate(Materials2Shapes.toolHeadFile, OrePrefixes.toolHeadFile, () -> ProcessingToolHead.INSTANCE);
        ShapeConsumerSupport
            .delegate(Materials2Shapes.toolHeadSaw, OrePrefixes.toolHeadSaw, () -> ProcessingToolHead.INSTANCE);
        ShapeConsumerSupport
            .delegate(Materials2Shapes.toolHeadWrench, OrePrefixes.toolHeadWrench, () -> ProcessingToolHead.INSTANCE);
        ShapeConsumerSupport
            .delegate(Materials2Shapes.toolHeadHammer, OrePrefixes.toolHeadHammer, () -> ProcessingToolHead.INSTANCE);
        ShapeConsumerSupport
            .delegate(Materials2Shapes.turbineBlade, OrePrefixes.turbineBlade, () -> ProcessingToolHead.INSTANCE);
    }
}
