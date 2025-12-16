package gregtech.common.covers.redstone;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.common.covers.CoverPosition;
import gregtech.common.gui.mui1.cover.CoverAdvancedRedstoneTransmitterExternalUIFactory;

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

        final CoverPosition key = getCoverKey(coverable, coverSide);
        setSignalAt(getUuid(), getFrequency(), key, outputRedstone);
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

}
