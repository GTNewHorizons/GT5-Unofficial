package gregtech.common.covers.redstone;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.common.covers.Cover;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;

public class CoverAdvancedRedstoneTransmitterExternal extends CoverAdvancedRedstoneTransmitterBase {

    public CoverAdvancedRedstoneTransmitterExternal(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return;
        }
        byte outputRedstone = aInputRedstone;
        if (isInverted()) {
            if (outputRedstone > 0) outputRedstone = 0;
            else outputRedstone = 15;
        }

        final long hash = hashCoverCoords(coverable, coverSide);
        setSignalAt(getUuid(), getFrequency(), hash, outputRedstone);
    }

    @Override
    public boolean isRedstoneSensitive(long aTimer) {
        return true;
    }

    @Override
    public boolean letsRedstoneGoIn() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new CoverAdvancedRedstoneTransmitterExternalUIFactory(buildContext).createWindow();
    }

    protected static class CoverAdvancedRedstoneTransmitterExternalUIFactory
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

}
