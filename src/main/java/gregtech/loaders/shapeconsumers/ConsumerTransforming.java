package gregtech.loaders.shapeconsumers;

import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.material.MaterialParts;
import gregtech.loaders.oreprocessing.ProcessingTransforming;

/// Dispatches [ProcessingTransforming]'s recipe generation for every cut-over prefix its legacy dynamic
/// registration targets (`getMaterialAmount() > 0 && !isContainer() && !isEnchantable()`, mirrored here
/// instead of hand-listing the prefixes it matches), scoped to prefixes that actually have a MaterialLib
/// shape via [MaterialParts#shapes]. Every candidate shape of a matching prefix is delegated, so the fluid and item
/// pipe
/// shapes sharing a `pipeTiny`..`pipeHuge` prefix each dispatch their own materials (the legacy oredict path
/// fired for either family's registrations). `plank` (which [ProcessingTransforming] additionally remaps to
/// `plate` internally) has no MaterialLib shape and is skipped like every other prefix [MaterialParts#shapes] does not
/// resolve.
public final class ConsumerTransforming {

    private ConsumerTransforming() {}

    static void register() {
        for (OrePrefixes prefix : OrePrefixes.VALUES) {
            if (prefix.getMaterialAmount() <= 0 || prefix.isContainer() || prefix.isEnchantable()) continue;
            for (Shape shape : MaterialParts.shapes(prefix)) {
                ShapeConsumerSupport.delegate(shape, prefix, () -> ProcessingTransforming.INSTANCE);
            }
        }
    }
}
