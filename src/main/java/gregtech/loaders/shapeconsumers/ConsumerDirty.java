package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Shapes;
import gregtech.loaders.oreprocessing.ProcessingDirty;

/// Generates crushed-ore recipes through [ProcessingDirty].
public final class ConsumerDirty {

    private ConsumerDirty() {}

    static void register() {
        ShapeConsumerSupport.delegate(Shapes.crushed, OrePrefixes.crushed, () -> ProcessingDirty.INSTANCE);
    }
}
