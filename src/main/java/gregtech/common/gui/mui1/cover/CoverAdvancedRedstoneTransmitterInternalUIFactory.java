package gregtech.common.gui.mui1.cover;

import org.jetbrains.annotations.NotNull;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.common.covers.Cover;
import gregtech.common.covers.redstone.CoverAdvancedRedstoneTransmitterInternal;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;

public class CoverAdvancedRedstoneTransmitterInternalUIFactory
    extends AdvancedRedstoneTransmitterBaseUIFactory<CoverAdvancedRedstoneTransmitterInternal> {

    public CoverAdvancedRedstoneTransmitterInternalUIFactory(CoverUIBuildContext buildContext) {
        super(buildContext);
    }

    @Override
    protected @NotNull CoverDataControllerWidget<CoverAdvancedRedstoneTransmitterInternal> getDataController() {
        return new CoverDataControllerWidget<>(this::getCover, getUIBuildContext());
    }

    @Override
    protected CoverAdvancedRedstoneTransmitterInternal adaptCover(Cover cover) {
        if (cover instanceof CoverAdvancedRedstoneTransmitterInternal adapterCover) {
            return adapterCover;
        }
        return null;
    }
}
