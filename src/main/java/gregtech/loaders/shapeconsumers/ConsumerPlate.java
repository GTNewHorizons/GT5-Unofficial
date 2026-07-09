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
        ShapeConsumerSupport.delegate(Materials2Shapes.shapePlate, OrePrefixes.plate, () -> ProcessingPlate.INSTANCE);
        ShapeConsumerSupport
            .delegate(Materials2Shapes.shapePlateDouble, OrePrefixes.plateDouble, () -> ProcessingPlate.INSTANCE);
        ShapeConsumerSupport
            .delegate(Materials2Shapes.shapePlateTriple, OrePrefixes.plateTriple, () -> ProcessingPlate.INSTANCE);
        ShapeConsumerSupport
            .delegate(Materials2Shapes.shapePlateQuadruple, OrePrefixes.plateQuadruple, () -> ProcessingPlate.INSTANCE);
        ShapeConsumerSupport
            .delegate(Materials2Shapes.shapePlateQuintuple, OrePrefixes.plateQuintuple, () -> ProcessingPlate.INSTANCE);
        ShapeConsumerSupport
            .delegate(Materials2Shapes.shapePlateDense, OrePrefixes.plateDense, () -> ProcessingPlate.INSTANCE);
        ShapeConsumerSupport.delegate(
            Materials2Shapes.shapePlateSuperdense,
            OrePrefixes.plateSuperdense,
            () -> ProcessingPlate.INSTANCE);
        ShapeConsumerSupport
            .delegate(Materials2Shapes.shapeItemCasing, OrePrefixes.itemCasing, () -> ProcessingPlate.INSTANCE);
    }
}
