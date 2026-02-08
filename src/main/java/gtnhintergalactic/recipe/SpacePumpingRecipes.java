package gtnhintergalactic.recipe;

import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.util.GTModHandler;

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
        RECIPES.put(Pair.of(2, 1), Materials.Chlorobenzene.getFluid(896_000));

        // T3
        if (Mods.HardcoreEnderExpansion.isModLoaded()) {
            RECIPES.put(Pair.of(3, 1), FluidRegistry.getFluidStack("endergoo", 32_000));
        }
        RECIPES.put(Pair.of(3, 2), Materials.OilExtraHeavy.getFluid(1_400_000));
        RECIPES.put(Pair.of(3, 3), Materials.Lava.getFluid(1_800_000));
        RECIPES.put(Pair.of(3, 4), Materials.NaturalGas.getGas(1_400_000));

        // T4
        RECIPES.put(Pair.of(4, 1), Materials.SulfuricAcid.getFluid(784_000));
        RECIPES.put(Pair.of(4, 2), Materials.Iron.getMolten(896_000));
        RECIPES.put(Pair.of(4, 3), Materials.Oil.getFluid(1_400_000));
        RECIPES.put(Pair.of(4, 4), Materials.OilHeavy.getFluid(1_792_000));
        RECIPES.put(Pair.of(4, 5), Materials.Lead.getMolten(896_000));
        RECIPES.put(Pair.of(4, 6), Materials.OilMedium.getFluid(1_400_000));
        RECIPES.put(Pair.of(4, 7), Materials.OilLight.getFluid(780_000));
        RECIPES.put(Pair.of(4, 8), FluidRegistry.getFluidStack("carbondioxide", 1_680_000));

        // T5
        RECIPES.put(Pair.of(5, 1), Materials.CarbonMonoxide.getGas(4_480_000));
        RECIPES.put(Pair.of(5, 2), Materials.Helium3.getGas(2_800_000));
        RECIPES.put(Pair.of(5, 3), Materials.SaltWater.getFluid(2_800_000));
        RECIPES.put(Pair.of(5, 4), Materials.Helium.getGas(1_400_000));
        RECIPES.put(Pair.of(5, 5), Materials.LiquidOxygen.getGas(896_000));
        RECIPES.put(Pair.of(5, 6), WerkstoffLoader.Neon.getFluidOrGas(32_000));
        RECIPES.put(Pair.of(5, 7), Materials.Argon.getGas(32_000));
        RECIPES.put(Pair.of(5, 8), WerkstoffLoader.Krypton.getFluidOrGas(8_000));
        RECIPES.put(Pair.of(5, 9), Materials.Methane.getGas(1_792_000));
        RECIPES.put(Pair.of(5, 10), Materials.HydricSulfide.getGas(392_000));
        RECIPES.put(Pair.of(5, 11), Materials.Ethane.getGas(1_194_000));

        // T6
        RECIPES.put(Pair.of(6, 1), Materials.Deuterium.getGas(1_568_000));
        RECIPES.put(Pair.of(6, 2), Materials.Tritium.getGas(240_000));
        RECIPES.put(Pair.of(6, 3), Materials.Ammonia.getGas(240_000));
        RECIPES.put(Pair.of(6, 4), WerkstoffLoader.Xenon.getFluidOrGas(16_000));
        RECIPES.put(Pair.of(6, 5), Materials.Ethylene.getGas(1_792_000));

        // T7
        RECIPES.put(Pair.of(7, 1), Materials.HydrofluoricAcid.getFluid(672_000));
        RECIPES.put(Pair.of(7, 2), Materials.Fluorine.getGas(1_792_000));
        RECIPES.put(Pair.of(7, 3), Materials.Nitrogen.getGas(1_792_000));
        RECIPES.put(Pair.of(7, 4), Materials.Oxygen.getGas(1_792_000));

        // T8
        RECIPES.put(Pair.of(8, 1), Materials.Hydrogen.getGas(1_568_000));
        RECIPES.put(Pair.of(8, 2), Materials.LiquidAir.getFluid(875_000));
        RECIPES.put(Pair.of(8, 3), Materials.Copper.getMolten(672_000));
        RECIPES.put(Pair.of(8, 5), GTModHandler.getDistilledWater(17_920_000));
        RECIPES.put(Pair.of(8, 6), Materials.Radon.getGas(64_000));
        RECIPES.put(Pair.of(8, 7), Materials.Tin.getMolten(672_000));

        if (Mods.GalaxySpace.isModLoaded()) {
            // T8
            RECIPES.put(Pair.of(8, 4), FluidRegistry.getFluidStack("unknowwater", 672_000));
        }
    }
}
