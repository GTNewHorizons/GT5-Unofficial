package goodgenerator.api.recipe;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.util.GTRecipe;

public class ExtremeHeatExchangerRecipe extends GTRecipe {

    public ExtremeHeatExchangerRecipe(FluidStack[] input, FluidStack[] output, int special) {
        super(false, null, null, null, null, input, output, 0, 0, special);
    }

    public int getMaxHotFluidConsume() {
        if (this.mFluidInputs != null) {
            return this.mFluidInputs[0].amount;
        }
        return 0;
    }

    public Fluid getNormalSteam() {
        if (this.mFluidOutputs != null) {
            return this.mFluidOutputs[0].getFluid();
        }
        return null;
    }

    public Fluid getHeatedSteam() {
        if (this.mFluidOutputs != null) {
            return this.mFluidOutputs[1].getFluid();
        }
        return null;
    }

    public Fluid getCooledFluid() {
        if (this.mFluidOutputs != null) {
            return this.mFluidOutputs[2].getFluid();
        }
        return null;
    }

    public int getEUt() {
        if (getNormalSteam() != null) {
            switch (getNormalSteam().getName()) {
                case "steam": {
                    int tVal = this.mFluidInputs[1].amount * 4;
                    if (tVal < 0) tVal = -tVal;
                    return tVal;
                }
                case "ic2superheatedsteam": {
                    int tVal = this.mFluidInputs[1].amount * 8;
                    if (tVal < 0) tVal = -tVal;
                    return tVal;
                }
                case "supercriticalsteam": {
                    int tVal = this.mFluidInputs[1].amount * 800;
                    if (tVal < 0) tVal = -tVal;
                    return tVal;
                }
                default:
                    return 0;
            }
        }
        return 0;
    }
}
