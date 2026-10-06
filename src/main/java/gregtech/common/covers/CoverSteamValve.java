package gregtech.common.covers;

import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.covers.CoverContext;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
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
            || fluid.getFluid() == MaterialLibAPI.getFluid(Materials.DenseSteam, FluidShapes.fluidGas)
            || fluid.getFluid() == MaterialLibAPI.getFluid(Materials.DenseSuperheatedSteam, FluidShapes.fluidGas)
            || fluid.getFluid() == MaterialLibAPI.getFluid(Materials.DenseSupercriticalSteam, FluidShapes.fluidGas);
    }
}
