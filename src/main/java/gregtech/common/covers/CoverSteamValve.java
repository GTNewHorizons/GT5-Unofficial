package gregtech.common.covers;

import gregtech.api.enums.materials2.FluidShapes;
import gregtech.api.enums.materials2.Materials;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.util.GTModHandler;

public class CoverSteamValve extends CoverPump {

    public CoverSteamValve(CoverContext context, int aTransferRate, ITexture coverTexture) {
        super(context, aTransferRate, coverTexture);
    }

    @Override
    protected boolean canTransferFluid(FluidStack fluid) {
        return isFluidCompatible(fluid);
    }

    public static boolean isFluidCompatible(FluidStack fluid) {
        if (fluid == null || fluid.getFluid() == null) return false;
        String fluidname = fluid.getFluid()
            .getName();
        return GTModHandler.isAnySteam(fluid) || GTModHandler.isSuperHeatedSteam(fluid)
            || fluidname.equals("supercriticalsteam")
            || fluid.getFluid()
                == MaterialLibAPI.getFluidStack(Materials.DenseSteam, FluidShapes.fluidGas, 1)
                    .getFluid()
            || fluid.getFluid() == MaterialLibAPI
                .getFluidStack(Materials.DenseSuperheatedSteam, FluidShapes.fluidGas, 1)
                .getFluid()
            || fluid.getFluid() == MaterialLibAPI
                .getFluidStack(Materials.DenseSupercriticalSteam, FluidShapes.fluidGas, 1)
                .getFluid();
    }
}
