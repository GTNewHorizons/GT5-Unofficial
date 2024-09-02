package gregtech.common.covers;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GTModHandler;

public class CoverSteamValve extends CoverPump {

    public CoverSteamValve(int aTransferRate, ITexture coverTexture) {
        super(aTransferRate, coverTexture);
    }

    @Override
    public boolean isRedstoneSensitive(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        long aTimer) {
        return false;
    }

    @Override
    protected boolean canTransferFluid(FluidStack fluid) {
        return GTModHandler.isAnySteam(fluid) || GTModHandler.isSuperHeatedSteam(fluid);
    }
}
