package gregtech.loaders.shapeconsumers;

import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.TEBlockShapes;
import gregtech.api.material.GTMaterialProperties;
import gregtech.loaders.oreprocessing.ProcessingWire;

/// Dispatches [ProcessingWire]'s recipe generation for the six wire shapes.
///
/// The superconductor wire markers ([GTMaterialProperties#IS_SUPERCONDUCTOR]) generate the wire shapes, and
/// their `wireGt02`..`wireGt16` assembler recipes come from the `wireGt01` dispatch's wire-combining branch,
/// which fires for the markers like for any other wire material. The thicker wire shapes skip the markers, so
/// the down-crafting recipes that split a thick wire back into thinner ones are not emitted for them.
public final class ConsumerWire {

    private ConsumerWire() {}

    static void register() {
        ShapeConsumerSupport.delegate(TEBlockShapes.wireGt01, OrePrefixes.wireGt01, () -> ProcessingWire.INSTANCE);
        delegateThick(TEBlockShapes.wireGt02, OrePrefixes.wireGt02);
        delegateThick(TEBlockShapes.wireGt04, OrePrefixes.wireGt04);
        delegateThick(TEBlockShapes.wireGt08, OrePrefixes.wireGt08);
        delegateThick(TEBlockShapes.wireGt12, OrePrefixes.wireGt12);
        delegateThick(TEBlockShapes.wireGt16, OrePrefixes.wireGt16);
    }

    private static void delegateThick(Shape shape, OrePrefixes prefix) {
        ShapeConsumerSupport.delegate(
            shape,
            prefix,
            material -> !material.getProperty(GTMaterialProperties.IS_SUPERCONDUCTOR),
            () -> ProcessingWire.INSTANCE);
    }
}
