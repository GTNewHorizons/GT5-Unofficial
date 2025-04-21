package gregtech.common.covers;

import gregtech.api.GregTechAPI;
import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.ISerializableObject.LegacyCoverData;

public class CoverRedstoneReceiverExternal extends CoverRedstoneWirelessBase {

    public CoverRedstoneReceiverExternal(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    public boolean isRedstoneSensitive(long aTimer) {
        return false;
    }

    @Override
    public LegacyCoverData doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return coverData;
        }
        int coverDataValue = coverData.get();
        coverable.setOutputRedstoneSignal(
            coverSide,
            GregTechAPI.sWirelessRedstone.get(coverDataValue) == null ? 0
                : GregTechAPI.sWirelessRedstone.get(coverDataValue));
        return coverData;
    }

    @Override
    public boolean manipulatesSidedRedstoneOutput() {
        return true;
    }

}
