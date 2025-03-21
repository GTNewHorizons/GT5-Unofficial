package gregtech.common.covers.redstone;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.common.covers.Cover;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;

public class CoverAdvancedRedstoneTransmitterInternal extends CoverAdvancedRedstoneTransmitterBase {

    public CoverAdvancedRedstoneTransmitterInternal(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return;
        }
        byte outputRedstone = coverable.getOutputRedstoneSignal(coverSide);
        if (isInverted()) {
            if (outputRedstone > 0) outputRedstone = 0;
            else outputRedstone = 15;
        }

        final long hash = hashCoverCoords(coverable, coverSide);
        setSignalAt(getUuid(), getFrequency(), hash, outputRedstone);
    }

    @Override
    public boolean letsRedstoneGoOut() {
        return true;
    }

    @Override
    public boolean manipulatesSidedRedstoneOutput() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new CoverAdvancedRedstoneTransmitterInternalUIFactory(buildContext).createWindow();
    }

    protected static class CoverAdvancedRedstoneTransmitterInternalUIFactory
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
}
