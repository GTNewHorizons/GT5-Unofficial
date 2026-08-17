package gregtech.loaders.shapeconsumers;

import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.TEBlockShapes;
import gregtech.api.material.GTMaterialProperties;
import gregtech.loaders.oreprocessing.ProcessingWire;

/// Dispatches [ProcessingWire]'s recipe generation for the six wire shapes.
///
/// The thicker shapes skip the superconductor wire markers
/// ([GTMaterialProperties#IS_SUPERCONDUCTOR]): their `wireGt02`..`wireGt16` assembler recipes already come
/// from the `wireGt01` dispatch's wire-combining branch, and they carry no down-crafting recipes.
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
