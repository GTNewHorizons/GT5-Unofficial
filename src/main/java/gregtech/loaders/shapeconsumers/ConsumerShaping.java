package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Shapes;
import gregtech.loaders.oreprocessing.ProcessingShaping;

/// Dispatches [ProcessingShaping]'s `ingot`/`dust` recipe generation (extruder/mold recipe ladder) for
/// MaterialLib's cutover ingot and dust shapes. A second, independent registrator on those prefixes alongside
/// [ProcessingIngot]/[ProcessingDust] and [ProcessingOreSmelting].
public final class ConsumerShaping {

    private ConsumerShaping() {}

    static void register() {
        ShapeConsumerSupport.delegate(Shapes.ingot, OrePrefixes.ingot, () -> ProcessingShaping.INSTANCE);
        ShapeConsumerSupport.delegate(Shapes.dust, OrePrefixes.dust, () -> ProcessingShaping.INSTANCE);
    }
}
