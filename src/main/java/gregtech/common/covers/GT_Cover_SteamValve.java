package gregtech.common.covers;

import net.minecraftforge.fluids.FluidStack;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GT_ModHandler;

public class GT_Cover_SteamValve extends GT_Cover_Pump {

    /**
     * @deprecated use {@link #GT_Cover_SteamValve(int aTransferRate, ITexture coverTexture)} instead
     */
    @Deprecated
    public GT_Cover_SteamValve(int aTransferRate) {
        this(aTransferRate, null);
    }

    public GT_Cover_SteamValve(int aTransferRate, ITexture coverTexture) {
        super(aTransferRate, coverTexture);
    }

    @Override
    public boolean isRedstoneSensitive(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        long aTimer) {
        return false;
    }

    @Override
    protected boolean canTransferFluid(FluidStack fluid) {
        return GT_ModHandler.isAnySteam(fluid) || GT_ModHandler.isSuperHeatedSteam(fluid);
    }
}
