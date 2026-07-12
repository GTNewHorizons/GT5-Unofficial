package gregtech.common.covers;

import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.covers.CoverContext;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
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
                == MaterialLibAPI.getFluidStack(Materials2Materials.DenseSteam, Materials2FluidShapes.fluidGas, 1)
                    .getFluid()
            || fluid.getFluid() == MaterialLibAPI
                .getFluidStack(Materials2Materials.DenseSuperheatedSteam, Materials2FluidShapes.fluidGas, 1)
                .getFluid()
            || fluid.getFluid() == MaterialLibAPI
                .getFluidStack(Materials2Materials.DenseSupercriticalSteam, Materials2FluidShapes.fluidGas, 1)
                .getFluid();
    }
}
