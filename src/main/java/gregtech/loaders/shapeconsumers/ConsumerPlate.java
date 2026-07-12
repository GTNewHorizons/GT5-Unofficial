package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.loaders.oreprocessing.ProcessingPlate;

/// Dispatches [ProcessingPlate]'s recipe generation for MaterialLib's cutover plate-family and casing shapes.
/// `plateAlloy` (the one prefix [ProcessingPlate] shares its body with whose logic is entirely foreign-item
/// compat -- hardcoded IC2/`ReinforcedGlass` recipes keyed by oredict-name string) has no MaterialLib shape
/// and keeps serving foreign mods exclusively through the legacy oredict path.
public final class ConsumerPlate {

    private ConsumerPlate() {}

    static void register() {
        ShapeConsumerSupport.delegate(Materials2Shapes.plate, OrePrefixes.plate, () -> ProcessingPlate.INSTANCE);
        ShapeConsumerSupport
            .delegate(Materials2Shapes.plateDouble, OrePrefixes.plateDouble, () -> ProcessingPlate.INSTANCE);
        ShapeConsumerSupport
            .delegate(Materials2Shapes.plateTriple, OrePrefixes.plateTriple, () -> ProcessingPlate.INSTANCE);
        ShapeConsumerSupport
            .delegate(Materials2Shapes.plateQuadruple, OrePrefixes.plateQuadruple, () -> ProcessingPlate.INSTANCE);
        ShapeConsumerSupport
            .delegate(Materials2Shapes.plateQuintuple, OrePrefixes.plateQuintuple, () -> ProcessingPlate.INSTANCE);
        ShapeConsumerSupport
            .delegate(Materials2Shapes.plateDense, OrePrefixes.plateDense, () -> ProcessingPlate.INSTANCE);
        ShapeConsumerSupport
            .delegate(Materials2Shapes.plateSuperdense, OrePrefixes.plateSuperdense, () -> ProcessingPlate.INSTANCE);
        ShapeConsumerSupport
            .delegate(Materials2Shapes.itemCasing, OrePrefixes.itemCasing, () -> ProcessingPlate.INSTANCE);
    }
}
