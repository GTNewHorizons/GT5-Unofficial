package gregtech.common.covers;

import gregtech.api.GregTechAPI;
import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.ITexture;

public class CoverRedstoneTransmitterExternal extends CoverRedstoneWirelessBase {

    public CoverRedstoneTransmitterExternal(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    @Override
    public void onCoverRemoval() {
        GregTechAPI.sWirelessRedstone.remove(coverData);
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        GregTechAPI.sWirelessRedstone.put(coverData, aInputRedstone);
    }

    @Override
    public boolean letsRedstoneGoIn() {
        return true;
    }
}
