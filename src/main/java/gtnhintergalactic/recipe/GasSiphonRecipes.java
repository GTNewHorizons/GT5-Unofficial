package gtnhintergalactic.recipe;

import java.util.HashMap;
import java.util.Map;

import gregtech.api.enums.materials2.FluidShapes;
import gregtech.api.enums.materials2.Materials;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.material.MaterialUtils;

/**
 * Available fluids for the Gas Siphon are defined here
 *
 * @author glowredman
 */
public class GasSiphonRecipes {

    public static final Map<String, GasSiphonRecipe> RECIPES = new HashMap<>();

    public static int calculateEUt(int depth, int planetTier) {
        return depth * (4 << (2 * planetTier + 2));
    }

    /**
     * Add all pumping recipes to the siphon
     */
    public static void addPumpingRecipes() {
        // T3 (-> 0.5 to 2A of EV)
        new GasSiphonRecipe("planet.jupiter", 3)
            .addFluid(
                1,
                MaterialLibAPI
                    .getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, (int) (15_000)))
            .addFluid(
                2,
                MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidGas, (int) (500)))
            .addFluid(
                3,
                MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidGas, (int) (300)))
            .addFluid(
                4,
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, (int) (200)))
            .build();

        // T5 (-> 0.5 to 2A of LuV)
        new GasSiphonRecipe("planet.saturn", 5)
            .addFluid(
                1,
                MaterialLibAPI
                    .getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, (int) (18_000)))
            .addFluid(
                2,
                MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidGas, (int) (800)))
            .addFluid(
                3,
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, (int) (500)))
            .addFluid(
                4,
                MaterialLibAPI
                    .getFluidStack(Materials.LiquidOxygen, FluidShapes.fluidGas, (int) (150)))
            .build();

        // T5 (-> 0.5 to 2A of LuV)
        new GasSiphonRecipe("planet.uranus", 5)
            .addFluid(
                1,
                MaterialLibAPI
                    .getFluidStack(Materials.Deuterium, FluidShapes.fluidGas, (int) (5_000)))
            .addFluid(
                2,
                MaterialLibAPI.getFluidStack(Materials.Neon, FluidShapes.fluidLiquid, (int) (450)))
            .addFluid(
                3,
                MaterialLibAPI.getFluidStack(Materials.Argon, FluidShapes.fluidGas, (int) (250)))
            .addFluid(
                4,
                MaterialLibAPI
                    .getFluidStack(Materials.Krypton, FluidShapes.fluidLiquid, (int) (100)))
            .build();

        // T6 (-> 0.5 to 2A of ZPM)
        new GasSiphonRecipe("planet.neptune", 6)
            .addFluid(
                1,
                MaterialLibAPI
                    .getFluidStack(Materials.Tritium, FluidShapes.fluidGas, (int) (3_000)))
            .addFluid(2, MaterialUtils.gas(Materials.Helium3, 500))
            .addFluid(
                3,
                MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, (int) (400)))
            .addFluid(
                4,
                MaterialLibAPI.getFluidStack(Materials.Xenon, FluidShapes.fluidLiquid, (int) (350)))
            .build();
    }

    /**
     * Builder-style container for defining Planetary Gas Siphon recipes.
     * <p>
     * Each planet has a fixed tier and a set of depth -> FluidStack mappings.
     * A "depth" represents how far the siphon drills into the planet’s atmosphere,
     * and each depth yields exactly one fluid type and amount.
     * <p>
     * Usage:
     * new GasSiphonRecipe("planet.jupiter", 3)
     * .addFluid(1, MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas,
     * (int) (15_000)))
     * .addFluid(2, MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidGas, (int)
     * (500)))
     * .addFluid(3, MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidGas,
     * (int) (300)))
     * .addFluid(4, MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, (int)
     * (200)))
     * .build();
     * <p>
     * Calling build() registers the recipe into GasSiphonRecipes.RECIPES.
     * <p>
     * This class does not perform validation; it simply stores the data in a
     * clean, readable format for NEI display and machine logic.
     */
    public static class GasSiphonRecipe {

        public final String planet;
        public final int tier;
        public final Map<Integer, FluidStack> depths = new HashMap<>();

        public GasSiphonRecipe(String planet, int tier) {
            this.planet = planet;
            this.tier = tier;
        }

        public GasSiphonRecipe addFluid(int depth, FluidStack result) {
            depths.put(depth, result);
            return this;
        }

        public void build() {
            RECIPES.put(planet, this);
        }
    }
}
