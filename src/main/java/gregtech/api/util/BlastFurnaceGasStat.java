package gregtech.api.util;

import java.util.Arrays;
import java.util.List;

import net.minecraftforge.fluids.FluidStack;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.Materials;

public enum BlastFurnaceGasStat {

    // No gas, recipe Time Multiplier = 1.25
    Nitrogen(Materials.Nitrogen.getGas(1L), 1.0, 1.0),
    Helium(Materials.Helium.getGas(1L), 0.9, 1.0),
    Argon(Materials.Argon.getGas(1L), 0.8, 0.85),
    Radon(Materials.Radon.getGas(1L), 0.7, 0.7),
    Neon(WerkstoffLoader.Neon.getFluidOrGas(1), 0.6, 0.55),
    Krypton(WerkstoffLoader.Krypton.getFluidOrGas(1), 0.5, 0.4),
    Xenon(WerkstoffLoader.Xenon.getFluidOrGas(1), 0.4, 0.25),
    Oganesson(WerkstoffLoader.Oganesson.getFluidOrGas(1), 0.3, 0.1);

    public static final List<BlastFurnaceGasStat> BlastFurnaceGasStats = Arrays
        .asList(Nitrogen, Helium, Argon, Radon, Neon, Krypton, Xenon, Oganesson);
    public final FluidStack gas;
    public final double recipeTimeMultiplier;
    public final double recipeConsumedAmountMultiplier;

    BlastFurnaceGasStat(FluidStack gas, double recipeTimeMultiplier, double recipeConsumedAmountMultiplier) {
        this.gas = gas;
        this.recipeTimeMultiplier = recipeTimeMultiplier;
        this.recipeConsumedAmountMultiplier = recipeConsumedAmountMultiplier;
    }
}
