package gregtech.common.gui.mui1.cover;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.common.covers.Cover;
import gregtech.common.covers.CoverLegacyData;

public abstract class CoverLegacyDataUIFactory extends CoverUIFactory<CoverLegacyData> {

    protected CoverLegacyDataUIFactory(CoverUIBuildContext buildContext) {
        super(buildContext);
    }

    @Override
    public CoverLegacyData adaptCover(Cover cover) {
        if (cover instanceof CoverLegacyData adapterCover) {
            return adapterCover;
        }
        return null;
    }
}
