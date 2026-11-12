package goodgenerator.api.recipe;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;

import gregtech.api.util.GTRecipe;

public class ExtremeHeatExchangerRecipe extends GTRecipe {

    public ExtremeHeatExchangerRecipe(FluidStack[] input, FluidStack[] output, int special) {
        super(false, null, null, null, null, null, null, null, input, output, 0, 0, special);
    }

    public int getMaxHotFluidConsume() {
        if (this.mFluidInputs == null) return 0;
        return this.mFluidInputs[0].amount;
    }

    public @Nullable Fluid getNormalSteam() {
        if (this.mFluidOutputs == null) return null;
        return this.mFluidOutputs[0].getFluid();
    }

    public @Nullable Fluid getHeatedSteam() {
        if (this.mFluidOutputs == null) return null;
        return this.mFluidOutputs[1].getFluid();
    }

    public @Nullable Fluid getCooledFluid() {
        if (this.mFluidOutputs == null) return null;
        return this.mFluidOutputs[2].getFluid();
    }

    public int getEUt() {
        if (getNormalSteam() == null) return 0;

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
}
