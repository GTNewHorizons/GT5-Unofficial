package gregtech.api.enums;

import gregtech.api.enums.materials.BlockShapes;
import gregtech.api.enums.materials.CellShapes;
import gregtech.api.enums.materials.Families;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.MaterialFacades;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.OreShapes;
import gregtech.api.enums.materials.PipeMaterials;
import gregtech.api.enums.materials.PipeShapes;
import gregtech.api.enums.materials.ShapeData;
import gregtech.api.enums.materials.Shapes;
import gregtech.loaders.materials.RecognitionMaterials;

public class MaterialSystem {

    // Shapes and families come first because the material tables pass them as objects.
    // RecognitionMaterials must follow Materials: one of its names (`Ammonium`) already belongs to a
    // real material, and it binds that rather than registering a duplicate. PipeMaterials runs last
    // because its rows reference the material fields the passes above assign.
    public static void init() {
        FluidShapes.init();
        CellShapes.init();
        BlockShapes.init();
        OreShapes.init();
        Shapes.init();
        PipeShapes.init();
        Families.init();
        Materials.init();
        MaterialFacades.registerBackingMaterials();
        RecognitionMaterials.registerBackingMaterials();
        PipeMaterials.init();
        ShapeData.init();
    }
}
