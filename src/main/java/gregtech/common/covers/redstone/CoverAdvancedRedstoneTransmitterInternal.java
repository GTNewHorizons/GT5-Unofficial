package gregtech.common.covers.redstone;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.covers.CoverFactory;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;

public class CoverAdvancedRedstoneTransmitterInternal
    extends CoverAdvancedRedstoneTransmitterBase<CoverAdvancedRedstoneTransmitterBase.TransmitterData> {

    public CoverAdvancedRedstoneTransmitterInternal(ITexture coverTexture,
        CoverFactory<TransmitterData> transmitterDataCoverFactory) {
        super(TransmitterData.class, coverTexture, transmitterDataCoverFactory);
    }

    @Override
    public TransmitterData doCoverThingsImpl(ForgeDirection side, byte aInputRedstone, int aCoverID,
        TransmitterData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        byte outputRedstone = aTileEntity.getOutputRedstoneSignal(side);
        if (aCoverVariable.isInvert()) {
            if (outputRedstone > 0) outputRedstone = 0;
            else outputRedstone = 15;
        }

        final long hash = hashCoverCoords(aTileEntity, side);
        setSignalAt(aCoverVariable.getUuid(), aCoverVariable.getFrequency(), hash, outputRedstone);
        return aCoverVariable;
    }

    @Override
    public boolean letsRedstoneGoOutImpl(ForgeDirection side, int aCoverID, TransmitterData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean manipulatesSidedRedstoneOutputImpl(ForgeDirection side, int aCoverID,
        TransmitterData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }
}
