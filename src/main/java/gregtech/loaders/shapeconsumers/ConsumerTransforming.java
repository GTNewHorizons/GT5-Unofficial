package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.material.MU;
import gregtech.loaders.oreprocessing.ProcessingTransforming;

/// Dispatches [ProcessingTransforming]'s recipe generation for every cut-over prefix its legacy dynamic
/// registration targets (`getMaterialAmount() > 0 && !isContainer() && !isEnchantable()`, mirrored here
/// instead of hand-listing the ~47 item-shape prefixes it matches), scoped to prefixes that actually have a
/// MaterialLib shape via [MU#shape]. No prefix this filter matches has more than one candidate shape (the one
/// exception, `cellPlasma`, is excluded by `isContainer()`), so [MU#shape]'s single-shape answer is safe here.
/// `plank` (which [ProcessingTransforming] additionally remaps to `plate` internally) has no MaterialLib shape
/// and is skipped like every other prefix [MU#shape] does not resolve.
public final class ConsumerTransforming {

    private ConsumerTransforming() {}

    static void register() {
        for (OrePrefixes prefix : OrePrefixes.VALUES) {
            if (prefix.getMaterialAmount() <= 0 || prefix.isContainer() || prefix.isEnchantable()) continue;
            ShapeConsumerSupport.delegate(MU.shape(prefix), prefix, ProcessingTransforming.INSTANCE);
        }
    }
}
