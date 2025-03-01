package gregtech.common.covers;

import gregtech.api.covers.CoverPlacerBase;

public class PrimitiveCoverPlacer extends CoverPlacerBase {

    @Override
    public boolean allowOnPrimitiveBlock() {
        return true;
    }
}
