package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.loaders.oreprocessing.ProcessingDirty;

/// Dispatches [ProcessingDirty]'s `crushed`-prefix recipe generation for MaterialLib's cutover crushed-ore
/// shape. Of the four prefixes [ProcessingDirty] shares one body across (`clump`, `shard`, `crushed`,
/// `dirtyGravel`), only `crushed` is cut over; the other three (all among the 15 shapes dropped in stage 05.1
/// for having no real legacy item slot) have no MaterialLib shape and keep serving foreign mods exclusively
/// through the legacy oredict path.
public final class ConsumerDirty {

    private ConsumerDirty() {}

    static void register() {
        ShapeConsumerSupport.delegate(Materials2Shapes.shapeCrushed, OrePrefixes.crushed, ProcessingDirty.INSTANCE);
    }
}
