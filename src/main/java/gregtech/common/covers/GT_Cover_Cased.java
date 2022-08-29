package gregtech.common.covers;

import gregtech.api.interfaces.ITexture;
import gregtech.api.util.GT_CoverBehavior;

public class GT_Cover_Cased extends GT_CoverBehavior {
    private final ITexture coverTexture;

    protected GT_Cover_Cased(final ITexture coverTexture) {
        this.coverTexture = coverTexture;
    }

    public ITexture getCoverTexture() {
        return this.coverTexture;
    }
}
