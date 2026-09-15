package gregtech.common.gui.mui1.cover;

import org.jetbrains.annotations.NotNull;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.common.covers.Cover;
import gregtech.common.covers.redstone.CoverAdvancedRedstoneTransmitterExternal;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;

public class CoverAdvancedRedstoneTransmitterExternalUIFactory
    extends AdvancedRedstoneTransmitterBaseUIFactory<CoverAdvancedRedstoneTransmitterExternal> {

    public CoverAdvancedRedstoneTransmitterExternalUIFactory(CoverUIBuildContext buildContext) {
        super(buildContext);
    }

    @Override
    protected @NotNull CoverDataControllerWidget<CoverAdvancedRedstoneTransmitterExternal> getDataController() {
        return new CoverDataControllerWidget<>(this::getCover, getUIBuildContext());
    }

    @Override
    protected CoverAdvancedRedstoneTransmitterExternal adaptCover(Cover cover) {
        if (cover instanceof CoverAdvancedRedstoneTransmitterExternal adapterCover) {
            return adapterCover;
        }
        return null;
    }
}
