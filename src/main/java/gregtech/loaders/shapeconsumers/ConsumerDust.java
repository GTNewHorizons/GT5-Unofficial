package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Shapes;
import gregtech.loaders.oreprocessing.ProcessingDust;

/// Generates dust-family recipes through [ProcessingDust].
public final class ConsumerDust {

    private ConsumerDust() {}

    static void register() {
        ShapeConsumerSupport.delegate(Shapes.dust, OrePrefixes.dust, () -> ProcessingDust.INSTANCE);
        ShapeConsumerSupport.delegate(Shapes.dustPure, OrePrefixes.dustPure, () -> ProcessingDust.INSTANCE);
        ShapeConsumerSupport.delegate(Shapes.dustImpure, OrePrefixes.dustImpure, () -> ProcessingDust.INSTANCE);
        ShapeConsumerSupport.delegate(Shapes.dustSmall, OrePrefixes.dustSmall, () -> ProcessingDust.INSTANCE);
        ShapeConsumerSupport.delegate(Shapes.dustTiny, OrePrefixes.dustTiny, () -> ProcessingDust.INSTANCE);
    }
}
