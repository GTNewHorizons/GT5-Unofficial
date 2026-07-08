package gregtech.api.enums;

import gregtech.api.enums.materials2.Materials2Families;
import gregtech.api.enums.materials2.Materials2Shapes;

/// Holds the MaterialLib-backed shapes, families, and materials for GregTech.
///
/// Populated from [#init()], which runs inside `GTMod`'s handler for
/// `com.ruling_0.materiallib.api.MaterialRegistrationEvent`.
public class Materials2 {

    // spotless:off

    // spotless:on

    public static void init() {
        Materials2Shapes.init();
        Materials2Families.init();
    }
}
