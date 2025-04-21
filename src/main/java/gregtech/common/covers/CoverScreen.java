package gregtech.common.covers;

import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.ITexture;

public class CoverScreen extends CoverBehavior {

    public CoverScreen(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    public boolean isRedstoneSensitive(long aTimer) {
        return false;
    }
}
