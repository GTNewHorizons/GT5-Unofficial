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
        if (fluid == null) return false;
        String fluidname = fluid.getFluid()
            .getName();
        return GTModHandler.isAnySteam(fluid) || GTModHandler.isSuperHeatedSteam(fluid)
            || fluidname.equals("supercriticalsteam")
            || fluidname.equals("densesupercriticalsteam");
    }
}
