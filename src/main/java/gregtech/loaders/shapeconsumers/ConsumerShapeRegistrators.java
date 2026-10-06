package gregtech.loaders.shapeconsumers;

import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.material.MaterialParts;

/// Runs the registrators attached through [OrePrefixes#addShapeRegistrator] for every prefix shape. Registrators may
/// attach after this consumer is registered, so each dispatch reads the prefix's current list.
public final class ConsumerShapeRegistrators {

    private ConsumerShapeRegistrators() {}

    static void register() {
        for (OrePrefixes prefix : OrePrefixes.VALUES) {
            for (Shape shape : MaterialParts.shapes(prefix)) {
                ShapeConsumerSupport.delegate(
                    shape,
                    prefix,
                    material -> !prefix.getShapeRegistrators()
                        .isEmpty(),
                    () -> (p, material, oreDictName, modName, stack) -> {
                        for (var registrator : prefix.getShapeRegistrators()) {
                            registrator.registerOre(p, material, oreDictName, modName, stack);
                        }
                    });
            }
        }
    }
}
