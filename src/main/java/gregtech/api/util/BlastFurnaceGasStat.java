package gregtech.api.util;

import java.util.Arrays;
import java.util.List;

import gregtech.api.enums.materials2.FluidShapes;
import gregtech.api.enums.materials2.Materials;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

public enum BlastFurnaceGasStat {

    // No gas, recipe Time Multiplier = 1.25
    Nitrogen(MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidGas, (int) (1)), 1.0,
        1.0),
    Helium(MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidGas, (int) (1)), 0.9,
        1.0),
    Argon(MaterialLibAPI.getFluidStack(Materials.Argon, FluidShapes.fluidGas, (int) (1)), 0.8,
        0.85),
    Radon(MaterialLibAPI.getFluidStack(Materials.Radon, FluidShapes.fluidGas, (int) (1)), 0.7, 0.7),
    Neon(MaterialLibAPI.getFluidStack(Materials.Neon, FluidShapes.fluidLiquid, (int) (1)), 0.6,
        0.55),
    Krypton(MaterialLibAPI.getFluidStack(Materials.Krypton, FluidShapes.fluidLiquid, (int) (1)),
        0.5, 0.4),
    Xenon(MaterialLibAPI.getFluidStack(Materials.Xenon, FluidShapes.fluidLiquid, (int) (1)), 0.4,
        0.25),
    Oganesson(MaterialLibAPI.getFluidStack(Materials.Oganesson, FluidShapes.fluidLiquid, (int) (1)),
        0.3, 0.1);

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
