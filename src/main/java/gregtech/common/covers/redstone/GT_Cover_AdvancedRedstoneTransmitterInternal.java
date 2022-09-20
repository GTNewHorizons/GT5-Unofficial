package gregtech.common.covers.redstone;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;

public class GT_Cover_AdvancedRedstoneTransmitterInternal extends GT_Cover_AdvancedRedstoneTransmitterBase {

    public GT_Cover_AdvancedRedstoneTransmitterInternal(ITexture coverTexture) {
        super(coverTexture);
    }

    @Override
    public TransmitterData doCoverThingsImpl(byte aSide, byte aInputRedstone, int aCoverID,
                                             TransmitterData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        byte outputRedstone = aTileEntity.getOutputRedstoneSignal(aSide);
        if (aCoverVariable.isInvert()) {
            if (outputRedstone > 0) outputRedstone = 0;
            else outputRedstone = 15;
        }

        GregTech_API.setAdvancedRedstone(aCoverVariable.getUuid(), aCoverVariable.getFrequency(), outputRedstone);
        return aCoverVariable;
    }

    @Override
    public boolean letsRedstoneGoOutImpl(byte aSide, int aCoverID, TransmitterData aCoverVariable,
                                         ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean manipulatesSidedRedstoneOutputImpl(byte aSide, int aCoverID, TransmitterData aCoverVariable,
                                                         ICoverable aTileEntity) {
        return true;
    }
}
