package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2OreShapes;
import gregtech.loaders.oreprocessing.ProcessingOrePoor;

/// Dispatches [ProcessingOrePoor]'s `oreSmall`-prefix recipe generation for MaterialLib's cutover small-ore
/// shape. Of the four prefixes [ProcessingOrePoor] shares one body across (`orePoor`, `oreSmall`, `oreNormal`,
/// `oreRich`, distinguished only by a yield multiplier), only `oreSmall` (multiplier 1) is cut over; the other
/// three have no MaterialLib shape and keep serving foreign mods exclusively through the legacy oredict path.
public final class ConsumerOrePoor {

    private ConsumerOrePoor() {}

    static void register() {
        ShapeConsumerSupport
            .delegate(Materials2OreShapes.shapeOreSmall, OrePrefixes.oreSmall, ProcessingOrePoor.INSTANCE);
    }
}
