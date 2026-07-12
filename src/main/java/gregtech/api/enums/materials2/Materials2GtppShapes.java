package gregtech.api.enums.materials2;

import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

/// Hand-maintained gregtech-owned [Shape] declarations for gtPlusPlus part prefixes with no stage-02
/// equivalent in [Materials2Shapes] -- see `scripts/mu/gen_materials.py`'s gtpp-fold region (stage 11).
///
/// `milled` is the only one: `scripts/mu/gen_shapes.py`'s `is_included` bugfix list excludes it because no
/// legacy GT `MetaGeneratedItemX32` ever held a constructor slot for it, but `gtPlusPlus.core.item.base.ore.
/// BaseItemMilledOre` did construct a real per-material item for it, so unlike GT's own unused `milled`
/// capability bit, gtPlusPlus materials with a dumped `milled` part need a real shape to cut over onto.
public class Materials2GtppShapes {

    public static Shape milled;

    public static void init() {
        milled = MaterialLibAPI.newItemShape("gregtech", "milled")
            .displayName("Milled %s")
            .build();
    }

    private Materials2GtppShapes() {}
}
