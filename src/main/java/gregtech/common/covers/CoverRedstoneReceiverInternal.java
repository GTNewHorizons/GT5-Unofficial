package gregtech.common.covers;

import gregtech.api.GregTechAPI;
import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.util.ISerializableObject;

public class CoverRedstoneReceiverInternal extends CoverRedstoneWirelessBase {

    public CoverRedstoneReceiverInternal(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    @Override
    public ISerializableObject.LegacyCoverData doCoverThings(byte aInputRedstone, long aTimer) {
        if (coveredTile.get() instanceof IMachineProgress machine) {
            if (getRedstoneInput(aInputRedstone) > 0) machine.enableWorking();
            else machine.disableWorking();
        }
        return coverData;
    }

    @Override
    public byte getRedstoneInput(byte aInputRedstone) {
        return GregTechAPI.sWirelessRedstone.get(coverData.get()) == null ? 0
            : GregTechAPI.sWirelessRedstone.get(coverData.get());
    }

}
