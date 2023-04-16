package gregtech.common.covers.redstone;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;

public class GT_Cover_AdvancedRedstoneReceiverExternal extends GT_Cover_AdvancedRedstoneReceiverBase {

    public GT_Cover_AdvancedRedstoneReceiverExternal(ITexture coverTexture) {
        super(coverTexture);
    }

    @Override
    public ReceiverData doCoverThingsImpl(ForgeDirection aSide, byte aInputRedstone, int aCoverID,
        ReceiverData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        aTileEntity.setOutputRedstoneSignal(
            aSide,
            getSignalAt(aCoverVariable.getUuid(), aCoverVariable.getFrequency(), aCoverVariable.getGateMode()));

        return aCoverVariable;
    }

    @Override
    protected boolean isRedstoneSensitiveImpl(ForgeDirection aSide, int aCoverID, ReceiverData aCoverVariable,
        ICoverable aTileEntity, long aTimer) {
        return false;
    }

    @Override
    protected boolean manipulatesSidedRedstoneOutputImpl(ForgeDirection aSide, int aCoverID,
        ReceiverData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }
}
