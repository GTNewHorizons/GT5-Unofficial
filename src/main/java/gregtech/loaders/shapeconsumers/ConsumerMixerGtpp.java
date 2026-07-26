package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2GtppComposites;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.loaders.oreprocessing.ProcessingMixerGtpp;

/// Dispatches [ProcessingMixerGtpp]'s composite mixer recipe for every material [Materials2GtppComposites]
/// declares. Gated on the `dust` shape rather than driven by it -- unlike every other `Consumer*` in this
/// package, membership here is the declared table itself (every entry's ratio-mix output needs a dust
/// regardless), so the `dust` shape only anchors the dispatch to MaterialLib's postInit timing (see
/// [ShapeConsumerSupport#delegate]) the same way [ConsumerWire]'s superconductor-marker filter does.
public final class ConsumerMixerGtpp {

    private ConsumerMixerGtpp() {}

    static void register() {
        ShapeConsumerSupport.delegate(
            Materials2Shapes.dust,
            OrePrefixes.dust,
            Materials2GtppComposites::has,
            () -> ProcessingMixerGtpp.INSTANCE);
    }
}
