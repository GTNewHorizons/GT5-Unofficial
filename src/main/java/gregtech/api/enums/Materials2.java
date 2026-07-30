package gregtech.api.enums;

import gregtech.api.enums.materials2.BlockShapes;
import gregtech.api.enums.materials2.Materials;
import gregtech.api.enums.materials2.MaterialFacades;
import gregtech.api.enums.materials2.CellShapes;
import gregtech.api.enums.materials2.Materials2Families;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.OreShapes;
import gregtech.api.enums.materials2.Materials2PipeMaterials;
import gregtech.api.enums.materials2.Materials2PipeShapes;
import gregtech.api.enums.materials2.Materials2ShapeData;
import gregtech.api.enums.materials2.Shapes;
import gregtech.loaders.materials.RecognitionMaterials;

/// Holds the MaterialLib-backed shapes, families, and materials for GregTech.
///
/// Populated from [#init()], which runs inside `GTMod`'s handler for
/// `com.ruling_0.materiallib.api.MaterialRegistrationEvent`.
public class Materials2 {

    // spotless:off

    // spotless:on

    // Shapes and families come first because the material tables pass them as objects.
    // RecognitionMaterials must follow Materials: one of its names (`Ammonium`) already belongs to a
    // real material, and it binds that rather than registering a duplicate. Materials2PipeMaterials runs last
    // because its rows reference the material fields the passes above assign.
    public static void init() {
        Materials2FluidShapes.init();
        CellShapes.init();
        BlockShapes.init();
        OreShapes.init();
        Shapes.init();
        Materials2PipeShapes.init();
        Materials2Families.init();
        Materials.init();
        MaterialFacades.registerBackingMaterials();
        RecognitionMaterials.registerBackingMaterials();
        Materials2PipeMaterials.init();
        Materials2ShapeData.init();
    }
}
