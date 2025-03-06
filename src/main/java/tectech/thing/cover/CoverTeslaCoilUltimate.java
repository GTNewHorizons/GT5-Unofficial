package tectech.thing.cover;

import net.minecraftforge.fluids.Fluid;

import gregtech.api.covers.CoverContext;

public class CoverTeslaCoilUltimate extends CoverTeslaCoil {

    public CoverTeslaCoilUltimate(CoverContext context) {
        super(context);
    }

    @Override
    public boolean letsEnergyOut() {
        return true;
    }

    @Override
    public boolean letsItemsIn(int aSlot) {
        return true;
    }

    @Override
    public boolean letsItemsOut(int aSlot) {
        return true;
    }

    @Override
    public boolean letsFluidIn(Fluid aFluid) {
        return true;
    }

    @Override
    public boolean letsFluidOut(Fluid aFluid) {
        return true;
    }

    public byte getTeslaReceptionCapability() {
        return 1;
    }
}
