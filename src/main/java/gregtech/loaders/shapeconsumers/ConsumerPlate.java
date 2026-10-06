package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Shapes;
import gregtech.loaders.oreprocessing.ProcessingPlate;

/// Generates plate-family and item-casing recipes through [ProcessingPlate].
public final class ConsumerPlate {

    private ConsumerPlate() {}

    static void register() {
        ShapeConsumerSupport.delegate(Shapes.plate, OrePrefixes.plate, () -> ProcessingPlate.INSTANCE);
        ShapeConsumerSupport.delegate(Shapes.plateDouble, OrePrefixes.plateDouble, () -> ProcessingPlate.INSTANCE);
        ShapeConsumerSupport.delegate(Shapes.plateTriple, OrePrefixes.plateTriple, () -> ProcessingPlate.INSTANCE);
        ShapeConsumerSupport
            .delegate(Shapes.plateQuadruple, OrePrefixes.plateQuadruple, () -> ProcessingPlate.INSTANCE);
        ShapeConsumerSupport
            .delegate(Shapes.plateQuintuple, OrePrefixes.plateQuintuple, () -> ProcessingPlate.INSTANCE);
        ShapeConsumerSupport.delegate(Shapes.plateDense, OrePrefixes.plateDense, () -> ProcessingPlate.INSTANCE);
        ShapeConsumerSupport
            .delegate(Shapes.plateSuperdense, OrePrefixes.plateSuperdense, () -> ProcessingPlate.INSTANCE);
        ShapeConsumerSupport.delegate(Shapes.itemCasing, OrePrefixes.itemCasing, () -> ProcessingPlate.INSTANCE);
    }
}
