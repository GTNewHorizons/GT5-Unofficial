package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Shapes;
import gregtech.loaders.oreprocessing.ProcessingToolHead;

/// Generates tool-head and turbine-blade recipes through [ProcessingToolHead].
public final class ConsumerToolHead {

    private ConsumerToolHead() {}

    static void register() {
        ShapeConsumerSupport
            .delegate(Shapes.toolHeadBuzzSaw, OrePrefixes.toolHeadBuzzSaw, () -> ProcessingToolHead.INSTANCE);
        ShapeConsumerSupport
            .delegate(Shapes.toolHeadChainsaw, OrePrefixes.toolHeadChainsaw, () -> ProcessingToolHead.INSTANCE);
        ShapeConsumerSupport
            .delegate(Shapes.toolHeadDrill, OrePrefixes.toolHeadDrill, () -> ProcessingToolHead.INSTANCE);
        ShapeConsumerSupport.delegate(Shapes.toolHeadFile, OrePrefixes.toolHeadFile, () -> ProcessingToolHead.INSTANCE);
        ShapeConsumerSupport.delegate(Shapes.toolHeadSaw, OrePrefixes.toolHeadSaw, () -> ProcessingToolHead.INSTANCE);
        ShapeConsumerSupport
            .delegate(Shapes.toolHeadWrench, OrePrefixes.toolHeadWrench, () -> ProcessingToolHead.INSTANCE);
        ShapeConsumerSupport
            .delegate(Shapes.toolHeadHammer, OrePrefixes.toolHeadHammer, () -> ProcessingToolHead.INSTANCE);
        ShapeConsumerSupport.delegate(Shapes.turbineBlade, OrePrefixes.turbineBlade, () -> ProcessingToolHead.INSTANCE);
    }
}
