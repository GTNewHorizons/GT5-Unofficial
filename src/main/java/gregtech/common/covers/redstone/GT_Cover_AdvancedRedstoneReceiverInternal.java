package gregtech.common.covers.redstone;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;

public class GT_Cover_AdvancedRedstoneReceiverInternal extends GT_Cover_AdvancedRedstoneReceiverBase {

    public GT_Cover_AdvancedRedstoneReceiverInternal(ITexture coverTexture) {
        super(coverTexture);
    }

    @Override
    public ReceiverData doCoverThingsImpl(ForgeDirection aSide, byte aInputRedstone, int aCoverID,
        ReceiverData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        if (aTileEntity instanceof IMachineProgress machine) {
            if (getRedstoneInput(aSide, aInputRedstone, aCoverID, aCoverVariable, aTileEntity) > 0) {
                machine.enableWorking();
            } else {
                machine.disableWorking();
            }

            machine.setWorkDataValue(aInputRedstone);
        }

        return aCoverVariable;
    }

    @Override
    protected byte getRedstoneInputImpl(ForgeDirection aSide, byte aInputRedstone, int aCoverID,
        ReceiverData aCoverVariable, ICoverable aTileEntity) {
        return getSignalAt(aCoverVariable.getUuid(), aCoverVariable.getFrequency(), aCoverVariable.getGateMode());
    }

    @Override
    protected boolean isRedstoneSensitiveImpl(ForgeDirection aSide, int aCoverID, ReceiverData aCoverVariable,
        ICoverable aTileEntity, long aTimer) {
        return true;
    }
}
