package gregtech.common.covers.redstone;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;

public class GT_Cover_AdvancedRedstoneTransmitterExternal
    extends GT_Cover_AdvancedRedstoneTransmitterBase<GT_Cover_AdvancedRedstoneTransmitterBase.TransmitterData> {

    public GT_Cover_AdvancedRedstoneTransmitterExternal(ITexture coverTexture) {
        super(TransmitterData.class, coverTexture);
    }

    @Override
    public TransmitterData createDataObject() {
        return new TransmitterData();
    }

    @Override
    public TransmitterData createDataObject(int aLegacyData) {
        return createDataObject();
    }

    @Override
    public TransmitterData doCoverThingsImpl(ForgeDirection aSide, byte aInputRedstone, int aCoverID,
        TransmitterData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        byte outputRedstone = aInputRedstone;
        if (aCoverVariable.isInvert()) {
            if (outputRedstone > 0) outputRedstone = 0;
            else outputRedstone = 15;
        }

        long hash = hashCoverCoords(aTileEntity, aSide);
        setSignalAt(aCoverVariable.getUuid(), aCoverVariable.getFrequency(), hash, outputRedstone);

        return aCoverVariable;
    }

    @Override
    protected boolean isRedstoneSensitiveImpl(ForgeDirection aSide, int aCoverID, TransmitterData aCoverVariable,
        ICoverable aTileEntity, long aTimer) {
        return true;
    }

    @Override
    public boolean letsRedstoneGoInImpl(ForgeDirection aSide, int aCoverID, TransmitterData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }
}
