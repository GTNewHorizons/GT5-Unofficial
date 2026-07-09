package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.loaders.oreprocessing.ProcessingToolHead;

/// Dispatches [ProcessingToolHead]'s recipe generation for MaterialLib's cutover tool-head-family and
/// turbine-blade shapes.
public final class ConsumerToolHead {

    private ConsumerToolHead() {}

    static void register() {
        ShapeConsumerSupport.delegate(
            Materials2Shapes.shapeToolHeadBuzzSaw,
            OrePrefixes.toolHeadBuzzSaw,
            () -> ProcessingToolHead.INSTANCE);
        ShapeConsumerSupport.delegate(
            Materials2Shapes.shapeToolHeadChainsaw,
            OrePrefixes.toolHeadChainsaw,
            () -> ProcessingToolHead.INSTANCE);
        ShapeConsumerSupport.delegate(
            Materials2Shapes.shapeToolHeadDrill,
            OrePrefixes.toolHeadDrill,
            () -> ProcessingToolHead.INSTANCE);
        ShapeConsumerSupport
            .delegate(Materials2Shapes.shapeToolHeadFile, OrePrefixes.toolHeadFile, () -> ProcessingToolHead.INSTANCE);
        ShapeConsumerSupport
            .delegate(Materials2Shapes.shapeToolHeadSaw, OrePrefixes.toolHeadSaw, () -> ProcessingToolHead.INSTANCE);
        ShapeConsumerSupport.delegate(
            Materials2Shapes.shapeToolHeadWrench,
            OrePrefixes.toolHeadWrench,
            () -> ProcessingToolHead.INSTANCE);
        ShapeConsumerSupport.delegate(
            Materials2Shapes.shapeToolHeadHammer,
            OrePrefixes.toolHeadHammer,
            () -> ProcessingToolHead.INSTANCE);
        ShapeConsumerSupport
            .delegate(Materials2Shapes.shapeTurbineBlade, OrePrefixes.turbineBlade, () -> ProcessingToolHead.INSTANCE);
    }
}
