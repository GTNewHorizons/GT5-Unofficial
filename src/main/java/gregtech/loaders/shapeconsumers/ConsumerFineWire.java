package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.loaders.oreprocessing.ProcessingFineWire;

/// Dispatches [ProcessingFineWire]'s `wireFine`-prefix recipe generation for MaterialLib's cutover fine-wire
/// shape.
public final class ConsumerFineWire {

    private ConsumerFineWire() {}

    static void register() {
        ShapeConsumerSupport
            .delegate(Materials2Shapes.shapeWireFine, OrePrefixes.wireFine, ProcessingFineWire.INSTANCE);
    }
}
