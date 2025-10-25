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

    public static final Map<String, Map<Integer, FluidStack>> RECIPES = new HashMap<>();

    /**
     * Add all pumping recipes to the siphon
     */
    public static void addPumpingRecipes() {
        // T3 (-> 0.5 to 2A of EV)
        Map<Integer, FluidStack> jupiterRecipes = new HashMap<>();
        jupiterRecipes.put(1, Materials.Hydrogen.getGas(15_000));
        jupiterRecipes.put(2, Materials.Helium.getGas(500));
        jupiterRecipes.put(3, Materials.Nitrogen.getGas(300));
        jupiterRecipes.put(4, Materials.Oxygen.getGas(200));

        // T5 (-> 0.5 to 2A of LuV)
        Map<Integer, FluidStack> saturnRecipes = new HashMap<>();
        saturnRecipes.put(1, Materials.Hydrogen.getGas(18_000));
        saturnRecipes.put(2, Materials.Helium.getGas(800));
        saturnRecipes.put(3, Materials.Oxygen.getGas(500));
        saturnRecipes.put(4, Materials.LiquidOxygen.getGas(150));

        // T5 (-> 0.5 to 2A of LuV)
        Map<Integer, FluidStack> uranusRecipes = new HashMap<>();
        uranusRecipes.put(1, Materials.Deuterium.getGas(5_000));
        uranusRecipes.put(2, WerkstoffLoader.Neon.getFluidOrGas(450));
        uranusRecipes.put(3, Materials.Argon.getGas(250));
        uranusRecipes.put(4, WerkstoffLoader.Krypton.getFluidOrGas(100));

        // T6 (-> 0.5 to 2A of ZPM)
        Map<Integer, FluidStack> neptuneRecipes = new HashMap<>();
        neptuneRecipes.put(1, Materials.Tritium.getGas(3_000));
        neptuneRecipes.put(2, Materials.Helium3.getGas(500));
        neptuneRecipes.put(3, Materials.Ammonia.getGas(400));
        neptuneRecipes.put(4, WerkstoffLoader.Xenon.getFluidOrGas(350));

        RECIPES.put("planet.jupiter", jupiterRecipes);
        RECIPES.put("planet.saturn", saturnRecipes);
        RECIPES.put("planet.uranus", uranusRecipes);
        RECIPES.put("planet.neptune", neptuneRecipes);
    }
}
