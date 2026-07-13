package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.loaders.oreprocessing.ProcessingDirty;

/// Dispatches [ProcessingDirty]'s `crushed`-prefix recipe generation for MaterialLib's cutover crushed-ore
/// shape. Of the four prefixes [ProcessingDirty] shares one body across (`clump`, `shard`, `crushed`,
/// `dirtyGravel`), only `crushed` is cut over; the other three never held a real legacy item slot, so they
/// have no MaterialLib shape and keep serving foreign mods exclusively through the legacy oredict path.
public final class ConsumerDirty {

    private ConsumerDirty() {}

    static void register() {
        ShapeConsumerSupport.delegate(Materials2Shapes.crushed, OrePrefixes.crushed, () -> ProcessingDirty.INSTANCE);
    }
}
