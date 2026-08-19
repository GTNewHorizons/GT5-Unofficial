package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.OreShapes;
import gregtech.loaders.oreprocessing.ProcessingOrePoor;

/// Dispatches [ProcessingOrePoor]'s `oreSmall`-prefix recipe generation for MaterialLib's cutover small-ore
/// shape, once per stone variant a material carries a small-ore block on (see
/// [ShapeConsumerSupport#delegateOreVariants]). Of the four prefixes [ProcessingOrePoor] shares one body
/// across (`orePoor`, `oreSmall`, `oreNormal`, `oreRich`, distinguished only by a yield multiplier), only
/// `oreSmall` (multiplier 1) is cut over; the other three have no MaterialLib shape, so every variant
/// dispatches under `oreSmall` rather than the stone's own prefix.
public final class ConsumerOrePoor {

    private ConsumerOrePoor() {}

    static void register() {
        ShapeConsumerSupport
            .delegateOreVariants(OreShapes.oreSmall, OrePrefixes.oreSmall, true, () -> ProcessingOrePoor.INSTANCE);
    }
}
