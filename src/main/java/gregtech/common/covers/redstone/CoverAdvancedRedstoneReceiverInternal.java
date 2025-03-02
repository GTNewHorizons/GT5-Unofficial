package gregtech.common.covers.redstone;

import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IMachineProgress;

public class CoverAdvancedRedstoneReceiverInternal extends CoverAdvancedRedstoneReceiverBase {

    public CoverAdvancedRedstoneReceiverInternal(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    @Override
    public ReceiverData doCoverThings(byte aInputRedstone, long aTimer) {
        if (coveredTile instanceof IMachineProgress machine) {
            if (getRedstoneInput(aInputRedstone) > 0) {
                machine.enableWorking();
            } else {
                machine.disableWorking();
            }
        }

        return coverData;
    }

    @Override
    public byte getRedstoneInput(byte aInputRedstone) {
        return getSignalAt(coverData.getUuid(), coverData.getFrequency(), coverData.getGateMode());
    }

    @Override
    public boolean isRedstoneSensitive(long aTimer) {
        return true;
    }
}
