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

/// Declares GregTech's shapes, families and materials with MaterialLib, in dependency order.
public class MaterialSystem {

    // Shapes and families come first since the material tables reference them. XModMaterials and PipeMaterials
    // edit materials, so they run after Materials.
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
