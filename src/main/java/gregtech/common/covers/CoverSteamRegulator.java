package gregtech.common.covers;

import net.minecraftforge.fluids.FluidStack;

import gregtech.api.interfaces.ITexture;
import gregtech.api.util.GTModHandler;

public class CoverSteamRegulator extends CoverFluidRegulator {

    public CoverSteamRegulator(int aTransferRate, ITexture coverTexture) {
        super(aTransferRate, coverTexture);
    }

    @Override
    protected boolean canTransferFluid(FluidStack fluid) {
        return GTModHandler.isAnySteam(fluid) || GTModHandler.isSuperHeatedSteam(fluid);
    }
}
