package gregtech.loaders.shapeconsumers;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Shapes;
import gregtech.loaders.oreprocessing.ProcessingMixerGtpp;

/// Dispatches [ProcessingMixerGtpp]'s composite mixer recipe for every material
/// [ProcessingMixerGtpp#isEligible] admits. Membership is that eligibility set, not the `dust` shape: the
/// shape only anchors the dispatch to MaterialLib's postInit timing (see [ShapeConsumerSupport#delegate]).
public final class ConsumerMixerGtpp {

    private ConsumerMixerGtpp() {}

    static void register() {
        ShapeConsumerSupport.delegate(
            Shapes.dust,
            OrePrefixes.dust,
            ProcessingMixerGtpp::isEligible,
            () -> ProcessingMixerGtpp.INSTANCE);
    }
}
