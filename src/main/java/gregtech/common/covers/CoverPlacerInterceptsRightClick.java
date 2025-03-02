package gregtech.common.covers;

import gregtech.api.covers.CoverPlacerBase;

public class CoverPlacerInterceptsRightClick extends CoverPlacerBase {

    @Override
    public boolean isGuiClickable() {
        return false;
    }

}
