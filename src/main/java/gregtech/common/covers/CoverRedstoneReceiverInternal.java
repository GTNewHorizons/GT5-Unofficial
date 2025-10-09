package gregtech.common.covers;

import gregtech.api.GregTechAPI;
import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IMachineProgress;

public class CoverRedstoneReceiverInternal extends CoverRedstoneWirelessBase {

    public CoverRedstoneReceiverInternal(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        if (coveredTile.get() instanceof IMachineProgress machine) {
            if (getRedstoneInput(aInputRedstone) > 0) machine.enableWorking();
            else machine.disableWorking();
        }
    }

    @Override
    public byte getRedstoneInput(byte aInputRedstone) {
        return GregTechAPI.sWirelessRedstone.get(getMapFrequency()) == null ? 0
            : GregTechAPI.sWirelessRedstone.get(getMapFrequency());
    }

}
