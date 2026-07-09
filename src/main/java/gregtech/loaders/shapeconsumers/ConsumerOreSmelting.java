package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.loaders.oreprocessing.ProcessingOreSmelting;

/// Dispatches [ProcessingOreSmelting]'s recipe generation for MaterialLib's cutover crushed-ore and dust
/// shapes. `dustRefined` (which shares [ProcessingOreSmelting]'s dust-family switch case) has no MaterialLib
/// shape and keeps serving foreign mods exclusively through the legacy oredict path.
public final class ConsumerOreSmelting {

    private ConsumerOreSmelting() {}

    static void register() {
        ShapeConsumerSupport
            .delegate(Materials2Shapes.shapeCrushed, OrePrefixes.crushed, ProcessingOreSmelting.INSTANCE);
        ShapeConsumerSupport.delegate(
            Materials2Shapes.shapeCrushedPurified,
            OrePrefixes.crushedPurified,
            ProcessingOreSmelting.INSTANCE);
        ShapeConsumerSupport.delegate(
            Materials2Shapes.shapeCrushedCentrifuged,
            OrePrefixes.crushedCentrifuged,
            ProcessingOreSmelting.INSTANCE);
        ShapeConsumerSupport.delegate(Materials2Shapes.shapeDust, OrePrefixes.dust, ProcessingOreSmelting.INSTANCE);
        ShapeConsumerSupport
            .delegate(Materials2Shapes.shapeDustImpure, OrePrefixes.dustImpure, ProcessingOreSmelting.INSTANCE);
        ShapeConsumerSupport
            .delegate(Materials2Shapes.shapeDustPure, OrePrefixes.dustPure, ProcessingOreSmelting.INSTANCE);
    }
}
