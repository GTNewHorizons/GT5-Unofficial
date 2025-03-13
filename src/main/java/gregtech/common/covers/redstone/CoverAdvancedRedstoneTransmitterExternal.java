package gregtech.common.covers.redstone;

import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;

public class CoverAdvancedRedstoneTransmitterExternal
    extends CoverAdvancedRedstoneTransmitterBase<CoverAdvancedRedstoneTransmitterBase.TransmitterData> {

    public CoverAdvancedRedstoneTransmitterExternal(CoverContext context, ITexture coverTexture) {
        super(context, TransmitterData.class, coverTexture);
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
}
