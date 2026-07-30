package gregtech.loaders.shapeconsumers;

import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.MaterialFacades;
import gregtech.api.enums.materials2.Materials2PipeShapes;
import gregtech.loaders.oreprocessing.ProcessingWire;

/// Dispatches [ProcessingWire]'s recipe generation for the six wire shapes.
///
/// The superconductor wire markers ([MaterialFacades]) generate the wire shapes, and their
/// `wireGt02`..`wireGt16` assembler recipes come from the `wireGt01` dispatch's wire-combining branch, which
/// fires for the markers like for any other wire material. The thicker wire shapes skip the markers, so the
/// down-crafting recipes that split a thick wire back into thinner ones are not emitted for them.
public final class ConsumerWire {

    private ConsumerWire() {}

    static void register() {
        ShapeConsumerSupport
            .delegate(Materials2PipeShapes.wireGt01, OrePrefixes.wireGt01, () -> ProcessingWire.INSTANCE);
        delegateThick(Materials2PipeShapes.wireGt02, OrePrefixes.wireGt02);
        delegateThick(Materials2PipeShapes.wireGt04, OrePrefixes.wireGt04);
        delegateThick(Materials2PipeShapes.wireGt08, OrePrefixes.wireGt08);
        delegateThick(Materials2PipeShapes.wireGt12, OrePrefixes.wireGt12);
        delegateThick(Materials2PipeShapes.wireGt16, OrePrefixes.wireGt16);
    }

    private static void delegateThick(Shape shape, OrePrefixes prefix) {
        ShapeConsumerSupport.delegate(
            shape,
            prefix,
            material -> !MaterialFacades.isSuperconductorMarker(material),
            () -> ProcessingWire.INSTANCE);
    }
}
