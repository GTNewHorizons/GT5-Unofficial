package gregtech.common.covers;

import gregtech.api.GregTechAPI;
import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;

public class CoverRedstoneReceiverExternal extends CoverRedstoneWirelessBase {

    public CoverRedstoneReceiverExternal(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    public boolean isRedstoneSensitive(long aTimer) {
        return false;
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return;
        }
        coverable.setOutputRedstoneSignal(
            coverSide,
            GregTechAPI.sWirelessRedstone.get(getMapFrequency()) == null ? 0
                : GregTechAPI.sWirelessRedstone.get(getMapFrequency()));
    }

    @Override
    public boolean manipulatesSidedRedstoneOutput() {
        return true;
    }

}
