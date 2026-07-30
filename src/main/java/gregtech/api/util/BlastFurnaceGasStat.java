package gregtech.api.util;

import java.util.Arrays;
import java.util.List;

import gregtech.api.enums.materials2.Materials;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.materials2.Materials2FluidShapes;

public enum BlastFurnaceGasStat {

    // No gas, recipe Time Multiplier = 1.25
    Nitrogen(MaterialLibAPI.getFluidStack(Materials.Nitrogen, Materials2FluidShapes.fluidGas, (int) (1)), 1.0,
        1.0),
    Helium(MaterialLibAPI.getFluidStack(Materials.Helium, Materials2FluidShapes.fluidGas, (int) (1)), 0.9,
        1.0),
    Argon(MaterialLibAPI.getFluidStack(Materials.Argon, Materials2FluidShapes.fluidGas, (int) (1)), 0.8,
        0.85),
    Radon(MaterialLibAPI.getFluidStack(Materials.Radon, Materials2FluidShapes.fluidGas, (int) (1)), 0.7, 0.7),
    Neon(MaterialLibAPI.getFluidStack(Materials.Neon, Materials2FluidShapes.fluidLiquid, (int) (1)), 0.6,
        0.55),
    Krypton(MaterialLibAPI.getFluidStack(Materials.Krypton, Materials2FluidShapes.fluidLiquid, (int) (1)),
        0.5, 0.4),
    Xenon(MaterialLibAPI.getFluidStack(Materials.Xenon, Materials2FluidShapes.fluidLiquid, (int) (1)), 0.4,
        0.25),
    Oganesson(MaterialLibAPI.getFluidStack(Materials.Oganesson, Materials2FluidShapes.fluidLiquid, (int) (1)),
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
