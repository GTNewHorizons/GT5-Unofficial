package gregtech.loaders.shapeconsumers;

import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.material.MaterialParts;
import gregtech.loaders.oreprocessing.ProcessingTransforming;

/// Dispatches [ProcessingTransforming]'s recipe generation for every prefix that carries material
/// (`getMaterialAmount() > 0`) and is neither a container nor enchantable, scoped to the prefixes that have a
/// MaterialLib shape via [MaterialParts#shapes]. Every candidate shape of a matching prefix is delegated, so the
/// fluid and item pipe shapes sharing a `pipeTiny`..`pipeHuge` prefix each dispatch their own materials.
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
