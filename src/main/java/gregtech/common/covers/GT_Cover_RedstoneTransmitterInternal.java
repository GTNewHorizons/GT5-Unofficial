package gregtech.common.covers;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;

public class GT_Cover_RedstoneTransmitterInternal extends GT_Cover_RedstoneWirelessBase {

    /**
     * @deprecated use {@link #GT_Cover_RedstoneTransmitterInternal(ITexture coverTexture)} instead
     */
    @Deprecated
    public GT_Cover_RedstoneTransmitterInternal() {
        this(null);
    }

    public GT_Cover_RedstoneTransmitterInternal(ITexture coverTexture) {
        super(coverTexture);
    }

    @Override
    public boolean isRedstoneSensitive(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        long aTimer) {
        return false;
    }

    @Override
    public int doCoverThings(ForgeDirection aSide, byte aInputRedstone, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity, long aTimer) {
        GregTech_API.sWirelessRedstone.put(aCoverVariable, aTileEntity.getOutputRedstoneSignal(aSide));
        return aCoverVariable;
    }

    @Override
    public boolean letsRedstoneGoOut(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public int getTickRate(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 1;
    }

    @Override
    public boolean manipulatesSidedRedstoneOutput(ForgeDirection aSide, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }
}
