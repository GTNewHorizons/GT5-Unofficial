package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.loaders.oreprocessing.ProcessingShaping;

/// Dispatches [ProcessingShaping]'s `ingot`/`dust` recipe generation (extruder/mold recipe ladder, a second,
/// independent registrator on those prefixes alongside [ProcessingIngot]/[ProcessingDust] and
/// [ProcessingOreSmelting], mirroring the legacy multi-registrator list) for MaterialLib's cutover ingot and
/// dust shapes.
public final class ConsumerShaping {

    private ConsumerShaping() {}

    static void register() {
        ShapeConsumerSupport.delegate(Materials2Shapes.shapeIngot, OrePrefixes.ingot, ProcessingShaping.INSTANCE);
        ShapeConsumerSupport.delegate(Materials2Shapes.shapeDust, OrePrefixes.dust, ProcessingShaping.INSTANCE);
    }
}
