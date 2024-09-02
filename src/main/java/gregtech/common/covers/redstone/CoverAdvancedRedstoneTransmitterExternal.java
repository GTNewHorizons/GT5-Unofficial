package gregtech.common.covers.redstone;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;

public class CoverAdvancedRedstoneTransmitterExternal
    extends CoverAdvancedRedstoneTransmitterBase<CoverAdvancedRedstoneTransmitterBase.TransmitterData> {

    public CoverAdvancedRedstoneTransmitterExternal(ITexture coverTexture) {
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
    public TransmitterData doCoverThingsImpl(ForgeDirection side, byte aInputRedstone, int aCoverID,
        TransmitterData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        byte outputRedstone = aInputRedstone;
        if (aCoverVariable.isInvert()) {
            if (outputRedstone > 0) outputRedstone = 0;
            else outputRedstone = 15;
        }

        final long hash = hashCoverCoords(aTileEntity, side);
        setSignalAt(aCoverVariable.getUuid(), aCoverVariable.getFrequency(), hash, outputRedstone);

        return aCoverVariable;
    }

    @Override
    protected boolean isRedstoneSensitiveImpl(ForgeDirection side, int aCoverID, TransmitterData aCoverVariable,
        ICoverable aTileEntity, long aTimer) {
        return true;
    }

    @Override
    public boolean letsRedstoneGoInImpl(ForgeDirection side, int aCoverID, TransmitterData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }
}
