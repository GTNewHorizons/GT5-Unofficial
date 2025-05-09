package gtnhintergalactic.recipe;

import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;

/**
 * Available fluids for space pumping are defined here
 *
 * @author minecraft7771
 */
public class SpacePumpingRecipes {

    /** Map containing all pumpable fluids */
    public static final Map<Pair<Integer, Integer>, FluidStack> RECIPES = new HashMap<>();

    /**
     * Add all pumping recipes to the module
     */
    public static void addPumpingRecipes() {
        // T2
        RECIPES.put(Pair.of(2, 1), Materials.Chlorobenzene.getFluid(896000));

        // T3
        RECIPES.put(Pair.of(3, 1), FluidRegistry.getFluidStack("endergoo", 32000));
        RECIPES.put(Pair.of(3, 2), Materials.OilExtraHeavy.getFluid(1400000));
        RECIPES.put(Pair.of(3, 3), Materials.Lava.getFluid(1800000));
        RECIPES.put(Pair.of(3, 4), Materials.NatruralGas.getGas(1400000));

        // T4
        RECIPES.put(Pair.of(4, 1), Materials.SulfuricAcid.getFluid(784000));
        RECIPES.put(Pair.of(4, 2), Materials.Iron.getMolten(896000));
        RECIPES.put(Pair.of(4, 3), Materials.Oil.getFluid(1400000));
        RECIPES.put(Pair.of(4, 4), Materials.OilHeavy.getFluid(1792000));
        RECIPES.put(Pair.of(4, 5), Materials.Lead.getMolten(896000));
        RECIPES.put(Pair.of(4, 6), Materials.OilMedium.getFluid(1400000));
        RECIPES.put(Pair.of(4, 7), Materials.OilLight.getFluid(780000));
        RECIPES.put(Pair.of(4, 8), FluidRegistry.getFluidStack("carbondioxide", 1680000));

        // T5
        RECIPES.put(Pair.of(5, 1), Materials.CarbonMonoxide.getGas(4480000));
        RECIPES.put(Pair.of(5, 2), Materials.Helium_3.getGas(2800000));
        RECIPES.put(Pair.of(5, 3), Materials.SaltWater.getFluid(2800000));
        RECIPES.put(Pair.of(5, 4), Materials.Helium.getGas(1400000));
        RECIPES.put(Pair.of(5, 5), Materials.LiquidOxygen.getGas(896000));
        RECIPES.put(Pair.of(5, 6), WerkstoffLoader.Neon.getFluidOrGas(32000));
        RECIPES.put(Pair.of(5, 7), Materials.Argon.getGas(32000));
        RECIPES.put(Pair.of(5, 8), WerkstoffLoader.Krypton.getFluidOrGas(8000));
        RECIPES.put(Pair.of(5, 9), Materials.Methane.getGas(1792000));
        RECIPES.put(Pair.of(5, 10), FluidRegistry.getFluidStack("liquid_hydricsulfur", 392000));
        RECIPES.put(Pair.of(5, 11), Materials.Ethane.getGas(1194000));

        // T6
        RECIPES.put(Pair.of(6, 1), Materials.Deuterium.getGas(1568000));
        RECIPES.put(Pair.of(6, 2), Materials.Tritium.getGas(240000));
        RECIPES.put(Pair.of(6, 3), Materials.Ammonia.getGas(240000));
        RECIPES.put(Pair.of(6, 4), WerkstoffLoader.Xenon.getFluidOrGas(16000));
        RECIPES.put(Pair.of(6, 5), Materials.Ethylene.getGas(1792000));

        // T7
        RECIPES.put(Pair.of(7, 1), Materials.HydrofluoricAcid.getFluid(672000));
        RECIPES.put(Pair.of(7, 2), Materials.Fluorine.getGas(1792000));
        RECIPES.put(Pair.of(7, 3), Materials.Nitrogen.getGas(1792000));
        RECIPES.put(Pair.of(7, 4), Materials.Oxygen.getGas(1792000));

        // T8
        RECIPES.put(Pair.of(8, 1), Materials.Hydrogen.getGas(1568000));
        RECIPES.put(Pair.of(8, 2), Materials.LiquidAir.getFluid(875000));
        RECIPES.put(Pair.of(8, 3), Materials.Copper.getMolten(672000));
        RECIPES.put(Pair.of(8, 5), FluidRegistry.getFluidStack("ic2distilledwater", 17920000));
        RECIPES.put(Pair.of(8, 6), Materials.Radon.getGas(64000));
        RECIPES.put(Pair.of(8, 7), Materials.Tin.getMolten(672000));

        if (Mods.GalaxySpace.isModLoaded()) {
            // T8
            RECIPES.put(Pair.of(8, 4), FluidRegistry.getFluidStack("unknowwater", 672000));
        }
    }
}
