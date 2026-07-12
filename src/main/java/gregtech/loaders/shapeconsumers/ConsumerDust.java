package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.loaders.oreprocessing.ProcessingDust;

/// Dispatches [ProcessingDust]'s recipe generation for MaterialLib's cutover dust-family shapes. `dustRefined`
/// (which shares [ProcessingDust]'s `"dustPure", "dustImpure", "dustRefined"` switch case) has no MaterialLib
/// shape and keeps serving foreign mods exclusively through the legacy oredict path.
public final class ConsumerDust {

    private ConsumerDust() {}

    static void register() {
        ShapeConsumerSupport.delegate(Materials2Shapes.dust, OrePrefixes.dust, () -> ProcessingDust.INSTANCE);
        ShapeConsumerSupport.delegate(Materials2Shapes.dustPure, OrePrefixes.dustPure, () -> ProcessingDust.INSTANCE);
        ShapeConsumerSupport
            .delegate(Materials2Shapes.dustImpure, OrePrefixes.dustImpure, () -> ProcessingDust.INSTANCE);
        ShapeConsumerSupport.delegate(Materials2Shapes.dustSmall, OrePrefixes.dustSmall, () -> ProcessingDust.INSTANCE);
        ShapeConsumerSupport.delegate(Materials2Shapes.dustTiny, OrePrefixes.dustTiny, () -> ProcessingDust.INSTANCE);
    }
}
