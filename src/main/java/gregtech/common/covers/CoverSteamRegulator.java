package gregtech.common.covers;

import net.minecraftforge.fluids.FluidStack;

import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.ITexture;

public class CoverSteamRegulator extends CoverFluidRegulator {

    public CoverSteamRegulator(CoverContext context, int aTransferRate, ITexture coverTexture) {
        super(context, aTransferRate, coverTexture);
    }

    @Override
    protected boolean canTransferFluid(FluidStack fluid) {
        return CoverSteamValve.isFluidCompatible(fluid);
    }
}
