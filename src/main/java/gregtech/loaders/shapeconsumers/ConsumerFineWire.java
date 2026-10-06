package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Shapes;
import gregtech.loaders.oreprocessing.ProcessingFineWire;

/// Generates fine-wire recipes through [ProcessingFineWire].
public final class ConsumerFineWire {

    private ConsumerFineWire() {}

    static void register() {
        ShapeConsumerSupport.delegate(Shapes.wireFine, OrePrefixes.wireFine, () -> ProcessingFineWire.INSTANCE);
    }
}
