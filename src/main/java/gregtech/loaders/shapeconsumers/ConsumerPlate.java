package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Shapes;
import gregtech.loaders.oreprocessing.ProcessingPlate;

/// Dispatches [ProcessingPlate]'s recipe generation for MaterialLib's cutover plate-family and casing shapes.
/// `plateAlloy` (the one prefix [ProcessingPlate] shares its body with whose logic is entirely foreign-item
/// compat -- hardcoded IC2/`ReinforcedGlass` recipes keyed by oredict-name string) has no MaterialLib shape
/// and keeps serving foreign mods exclusively through the legacy oredict path.
public final class ConsumerPlate {

    private ConsumerPlate() {}

    static void register() {
        ShapeConsumerSupport.delegate(Shapes.plate, OrePrefixes.plate, () -> ProcessingPlate.INSTANCE);
        ShapeConsumerSupport
            .delegate(Shapes.plateDouble, OrePrefixes.plateDouble, () -> ProcessingPlate.INSTANCE);
        ShapeConsumerSupport
            .delegate(Shapes.plateTriple, OrePrefixes.plateTriple, () -> ProcessingPlate.INSTANCE);
        ShapeConsumerSupport
            .delegate(Shapes.plateQuadruple, OrePrefixes.plateQuadruple, () -> ProcessingPlate.INSTANCE);
        ShapeConsumerSupport
            .delegate(Shapes.plateQuintuple, OrePrefixes.plateQuintuple, () -> ProcessingPlate.INSTANCE);
        ShapeConsumerSupport
            .delegate(Shapes.plateDense, OrePrefixes.plateDense, () -> ProcessingPlate.INSTANCE);
        ShapeConsumerSupport
            .delegate(Shapes.plateSuperdense, OrePrefixes.plateSuperdense, () -> ProcessingPlate.INSTANCE);
        ShapeConsumerSupport
            .delegate(Shapes.itemCasing, OrePrefixes.itemCasing, () -> ProcessingPlate.INSTANCE);
    }
}
