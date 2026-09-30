package gregtech.api.enums;

import gregtech.api.enums.materials.BlockShapes;
import gregtech.api.enums.materials.CellShapes;
import gregtech.api.enums.materials.Families;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.OreShapes;
import gregtech.api.enums.materials.PipeMaterials;
import gregtech.api.enums.materials.ShapeData;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.enums.materials.TEBlockShapes;
import gregtech.api.enums.materials.Wildcard;
import gregtech.api.enums.materials.XModMaterials;

public class MaterialSystem {

    // Shapes and families come first: the material tables pass them as objects. PipeMaterials runs last: its
    // rows reference the material fields the passes above assign, as do XModMaterials' shape edits.
    public static void init() {
        FluidShapes.init();
        CellShapes.init();
        BlockShapes.init();
        OreShapes.init();
        Shapes.init();
        TEBlockShapes.init();
        Families.init();
        Materials.init();
        XModMaterials.init();
        Wildcard.registerFamilies();
        PipeMaterials.init();
        ShapeData.init();
    }
}
