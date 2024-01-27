package gregtech.common.covers;

import net.minecraftforge.fluids.FluidStack;

import gregtech.api.interfaces.ITexture;
import gregtech.api.util.GT_ModHandler;

public class GT_Cover_SteamRegulator extends GT_Cover_FluidRegulator {

    public GT_Cover_SteamRegulator(int aTransferRate, ITexture coverTexture) {
        super(aTransferRate, coverTexture);
    }

    @Override
    protected boolean canTransferFluid(FluidStack fluid) {
        return GT_ModHandler.isAnySteam(fluid) || GT_ModHandler.isSuperHeatedSteam(fluid);
    }
}
