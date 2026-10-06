package gregtech.loaders.shapeconsumers;

import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.material.MaterialParts;
import gregtech.loaders.oreprocessing.ProcessingTransforming;

/// Generates chemical-bath and polarizer transforming recipes through [ProcessingTransforming], for every
/// material-carrying prefix that is neither a container nor enchantable. Every candidate shape of a prefix is
/// delegated, so fluid and item pipes each dispatch their own materials.
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
