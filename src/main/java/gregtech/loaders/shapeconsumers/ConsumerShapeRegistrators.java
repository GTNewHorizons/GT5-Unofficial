package gregtech.loaders.shapeconsumers;

import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.material.MaterialParts;

/// Dispatches the registrators attached through [OrePrefixes#addShapeRegistrator], which other code (the
/// prospector scanners, other mods) attaches after this package's consumers are registered: the consumer for
/// every prefix shape is registered up front and reads the prefix's list when MaterialLib dispatches it.
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
