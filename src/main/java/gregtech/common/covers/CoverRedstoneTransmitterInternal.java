package gregtech.common.covers;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;

public class CoverRedstoneTransmitterInternal extends CoverRedstoneWirelessBase {

    public CoverRedstoneTransmitterInternal(ITexture coverTexture) {
        super(coverTexture);
    }

    @Override
    public boolean isRedstoneSensitive(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        long aTimer) {
        return false;
    }

    @Override
    public int doCoverThings(ForgeDirection side, byte aInputRedstone, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity, long aTimer) {
        GregTechAPI.sWirelessRedstone.put(aCoverVariable, aTileEntity.getOutputRedstoneSignal(side));
        return aCoverVariable;
    }

    @Override
    public boolean letsRedstoneGoOut(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public int getTickRate(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 1;
    }

    @Override
    public boolean manipulatesSidedRedstoneOutput(ForgeDirection side, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }
}
