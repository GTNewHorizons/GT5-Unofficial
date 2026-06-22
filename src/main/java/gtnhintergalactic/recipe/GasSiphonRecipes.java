package gtnhintergalactic.recipe;

import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.fluids.FluidStack;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.Materials;

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
        new GasSiphonRecipe("planet.jupiter", 3).addFluid(1, Materials.Hydrogen.getGas(15_000))
            .addFluid(2, Materials.Helium.getGas(500))
            .addFluid(3, Materials.Nitrogen.getGas(300))
            .addFluid(4, Materials.Oxygen.getGas(200))
            .build();

        // T5 (-> 0.5 to 2A of LuV)
        new GasSiphonRecipe("planet.saturn", 5).addFluid(1, Materials.Hydrogen.getGas(18_000))
            .addFluid(2, Materials.Helium.getGas(800))
            .addFluid(3, Materials.Oxygen.getGas(500))
            .addFluid(4, Materials.LiquidOxygen.getGas(150))
            .build();

        // T5 (-> 0.5 to 2A of LuV)
        new GasSiphonRecipe("planet.uranus", 5).addFluid(1, Materials.Deuterium.getGas(5_000))
            .addFluid(2, WerkstoffLoader.Neon.getFluidOrGas(450))
            .addFluid(3, Materials.Argon.getGas(250))
            .addFluid(4, WerkstoffLoader.Krypton.getFluidOrGas(100))
            .build();

        // T6 (-> 0.5 to 2A of ZPM)
        new GasSiphonRecipe("planet.neptune", 6).addFluid(1, Materials.Tritium.getGas(3_000))
            .addFluid(2, Materials.Helium3.getGas(500))
            .addFluid(3, Materials.Ammonia.getGas(400))
            .addFluid(4, WerkstoffLoader.Xenon.getFluidOrGas(350))
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
     * .addFluid(1, Materials.Hydrogen.getGas(15_000))
     * .addFluid(2, Materials.Helium.getGas(500))
     * .addFluid(3, Materials.Nitrogen.getGas(300))
     * .addFluid(4, Materials.Oxygen.getGas(200))
     * .build();
     * <p>
     * Calling build() registers the recipe into GasSiphonRecipes.RECIPES.
     * <p>
     * This class does not perform validation; it simply stores the data in a
     * clean, readable format for NEI display and machine logic.
     *
     * @author jude123412
     */
    public static class GasSiphonRecipe {

        public final String planet;
        public final int tier;
        public final Map<Integer, FluidStack> DEPTHS = new HashMap<>();

        public GasSiphonRecipe(String planet, int tier) {
            this.planet = planet;
            this.tier = tier;
        }

        public GasSiphonRecipe addFluid(int depth, FluidStack result) {
            DEPTHS.put(depth, result);
            return this;
        }

        public void build() {
            RECIPES.put(planet, this);
        }
    }
}
