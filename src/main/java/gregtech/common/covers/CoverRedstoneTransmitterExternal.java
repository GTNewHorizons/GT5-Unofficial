package gregtech.common.covers;

import gregtech.api.GregTechAPI;
import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.util.ISerializableObject;

public class CoverRedstoneTransmitterExternal extends CoverRedstoneWirelessBase {

    public CoverRedstoneTransmitterExternal(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    @Override
    public ISerializableObject.LegacyCoverData doCoverThings(byte aInputRedstone, long aTimer) {
        GregTechAPI.sWirelessRedstone.put(coverData.get(), aInputRedstone);
        return coverData;
    }

    @Override
    public boolean letsRedstoneGoIn() {
        return true;
    }
}
