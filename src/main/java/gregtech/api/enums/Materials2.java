package gregtech.api.enums;

import gregtech.api.enums.materials2.Materials2BlockShapes;
import gregtech.api.enums.materials2.Materials2CellShapes;
import gregtech.api.enums.materials2.Materials2Families;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2GtppShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2OreShapes;
import gregtech.api.enums.materials2.Materials2Shapes;

/// Holds the MaterialLib-backed shapes, families, and materials for GregTech.
///
/// Populated from [#init()], which runs inside `GTMod`'s handler for
/// `com.ruling_0.materiallib.api.MaterialRegistrationEvent`.
public class Materials2 {

    // spotless:off

    // spotless:on

    public static void init() {
        Materials2FluidShapes.init();
        Materials2CellShapes.init();
        Materials2BlockShapes.init();
        Materials2OreShapes.init();
        Materials2Shapes.init();
        Materials2GtppShapes.init();
        Materials2Families.init();
        Materials2Materials.init();
    }
}
