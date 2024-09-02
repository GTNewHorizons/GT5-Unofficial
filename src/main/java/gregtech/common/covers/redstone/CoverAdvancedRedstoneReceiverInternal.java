package gregtech.common.covers.redstone;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;

public class CoverAdvancedRedstoneReceiverInternal extends CoverAdvancedRedstoneReceiverBase {

    public CoverAdvancedRedstoneReceiverInternal(ITexture coverTexture) {
        super(coverTexture);
    }

    @Override
    public ReceiverData doCoverThingsImpl(ForgeDirection side, byte aInputRedstone, int aCoverID,
        ReceiverData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        if (aTileEntity instanceof IMachineProgress machine) {
            if (getRedstoneInput(side, aInputRedstone, aCoverID, aCoverVariable, aTileEntity) > 0) {
                machine.enableWorking();
            } else {
                machine.disableWorking();
            }

            machine.setWorkDataValue(aInputRedstone);
        }

        return aCoverVariable;
    }

    @Override
    protected byte getRedstoneInputImpl(ForgeDirection side, byte aInputRedstone, int aCoverID,
        ReceiverData aCoverVariable, ICoverable aTileEntity) {
        return getSignalAt(aCoverVariable.getUuid(), aCoverVariable.getFrequency(), aCoverVariable.getGateMode());
    }

    @Override
    protected boolean isRedstoneSensitiveImpl(ForgeDirection side, int aCoverID, ReceiverData aCoverVariable,
        ICoverable aTileEntity, long aTimer) {
        return true;
    }
}
