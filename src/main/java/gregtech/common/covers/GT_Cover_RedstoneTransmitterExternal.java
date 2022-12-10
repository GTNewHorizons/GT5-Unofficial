package gregtech.common.covers;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.ISerializableObject;

public class GT_Cover_RedstoneTransmitterExternal extends GT_Cover_RedstoneWirelessBase {

    /**
     * @deprecated use {@link #GT_Cover_RedstoneTransmitterExternal(ITexture coverTexture)} instead
     */
    @Deprecated
    public GT_Cover_RedstoneTransmitterExternal() {
        this(null);
    }

    public GT_Cover_RedstoneTransmitterExternal(ITexture coverTexture) {
        super(coverTexture);
    }

    @Override
    public int doCoverThings(
            byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
        GregTech_API.sWirelessRedstone.put(aCoverVariable, aInputRedstone);
        return aCoverVariable;
    }

    @Override
    protected boolean isRedstoneSensitiveImpl(
            byte aSide,
            int aCoverID,
            ISerializableObject.LegacyCoverData aCoverVariable,
            ICoverable aTileEntity,
            long aTimer) {
        return true;
    }

    @Override
    public boolean letsRedstoneGoIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public int getTickRate(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 1;
    }
}
