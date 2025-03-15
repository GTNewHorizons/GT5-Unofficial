package gregtech.common.covers.redstone;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.common.covers.Cover;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;

public class CoverAdvancedRedstoneTransmitterExternal
    extends CoverAdvancedRedstoneTransmitterBase<CoverAdvancedRedstoneTransmitterBase.TransmitterData> {

    public CoverAdvancedRedstoneTransmitterExternal(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    @Override
    protected TransmitterData initializeData() {
        return new CoverAdvancedRedstoneTransmitterBase.TransmitterData();
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return;
        }
        byte outputRedstone = aInputRedstone;
        if (coverData.isInvert()) {
            if (outputRedstone > 0) outputRedstone = 0;
            else outputRedstone = 15;
        }

        final long hash = hashCoverCoords(coverable, coverSide);
        setSignalAt(coverData.getUuid(), coverData.getFrequency(), hash, outputRedstone);
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

    protected class CoverAdvancedRedstoneTransmitterExternalUIFactory
        extends AdvancedRedstoneTransmitterBaseUIFactory<CoverAdvancedRedstoneTransmitterExternal> {

        public CoverAdvancedRedstoneTransmitterExternalUIFactory(CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @Override
        protected @NotNull CoverDataControllerWidget<CoverAdvancedRedstoneTransmitterExternal> getDataController() {
            return new CoverDataControllerWidget<>(this::adaptCover, getUIBuildContext());
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
