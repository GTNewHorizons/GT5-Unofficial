package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Shapes;
import gregtech.loaders.oreprocessing.ProcessingDirty;

/// Dispatches [ProcessingDirty]'s `crushed`-prefix recipe generation for MaterialLib's cutover crushed-ore
/// shape. Of the four prefixes [ProcessingDirty] shares one body across (`clump`, `shard`, `crushed`,
/// `dirtyGravel`), only `crushed` is cut over; the other three never held a real legacy item slot and have no
/// MaterialLib shape.
public final class ConsumerDirty {

    private ConsumerDirty() {}

    static void register() {
        ShapeConsumerSupport.delegate(Shapes.crushed, OrePrefixes.crushed, () -> ProcessingDirty.INSTANCE);
    }
}
