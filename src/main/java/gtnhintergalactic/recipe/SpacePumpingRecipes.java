package gtnhintergalactic.recipe;

import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.Mods;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

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
        RECIPES.put(
            Pair.of(2, 1),
            MaterialLibAPI.getFluidStack(Materials.Chlorobenzene, FluidShapes.fluidLiquid, 896_000));

        // T3
        if (Mods.HardcoreEnderExpansion.isModLoaded()) {
            RECIPES.put(Pair.of(3, 1), FluidRegistry.getFluidStack("endergoo", 32_000));
        }
        RECIPES.put(
            Pair.of(3, 2),
            MaterialLibAPI.getFluidStack(Materials.OilExtraHeavy, FluidShapes.fluidLiquid, 1_400_000));
        RECIPES.put(Pair.of(3, 3), GTUtility.getLava(1_800_000));
        RECIPES.put(Pair.of(3, 4), MaterialUtils.gas(Materials.NatruralGas, 1_400_000));

        // T4
        RECIPES
            .put(Pair.of(4, 1), MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 784_000));
        RECIPES.put(Pair.of(4, 2), MaterialLibAPI.getFluidStack(Materials.Iron, FluidShapes.fluidMolten, 896_000));
        RECIPES.put(Pair.of(4, 3), MaterialLibAPI.getFluidStack(Materials.Oil, FluidShapes.fluidLiquid, 1_400_000));
        RECIPES
            .put(Pair.of(4, 4), MaterialLibAPI.getFluidStack(Materials.OilHeavy, FluidShapes.fluidLiquid, 1_792_000));
        RECIPES.put(Pair.of(4, 5), MaterialLibAPI.getFluidStack(Materials.Lead, FluidShapes.fluidMolten, 896_000));
        RECIPES
            .put(Pair.of(4, 6), MaterialLibAPI.getFluidStack(Materials.OilMedium, FluidShapes.fluidLiquid, 1_400_000));
        RECIPES.put(Pair.of(4, 7), MaterialLibAPI.getFluidStack(Materials.OilLight, FluidShapes.fluidLiquid, 780_000));
        RECIPES.put(Pair.of(4, 8), FluidRegistry.getFluidStack("carbondioxide", 1_680_000));

        // T5
        RECIPES.put(
            Pair.of(5, 1),
            MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, 4_480_000));
        RECIPES.put(Pair.of(5, 2), MaterialUtils.gas(Materials.Helium3, 2_800_000));
        RECIPES
            .put(Pair.of(5, 3), MaterialLibAPI.getFluidStack(Materials.SaltWater, FluidShapes.fluidLiquid, 2_800_000));
        RECIPES.put(Pair.of(5, 4), MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidGas, 1_400_000));
        RECIPES.put(Pair.of(5, 5), MaterialLibAPI.getFluidStack(Materials.LiquidOxygen, FluidShapes.fluidGas, 896_000));
        RECIPES.put(Pair.of(5, 6), MaterialLibAPI.getFluidStack(Materials.Neon, FluidShapes.fluidLiquid, 32_000));
        RECIPES.put(Pair.of(5, 7), MaterialLibAPI.getFluidStack(Materials.Argon, FluidShapes.fluidGas, 32_000));
        RECIPES.put(Pair.of(5, 8), MaterialLibAPI.getFluidStack(Materials.Krypton, FluidShapes.fluidLiquid, 8_000));
        RECIPES.put(Pair.of(5, 9), MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 1_792_000));
        RECIPES
            .put(Pair.of(5, 10), MaterialLibAPI.getFluidStack(Materials.HydricSulfide, FluidShapes.fluidGas, 392_000));
        RECIPES.put(Pair.of(5, 11), MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, 1_194_000));

        // T6
        RECIPES.put(Pair.of(6, 1), MaterialLibAPI.getFluidStack(Materials.Deuterium, FluidShapes.fluidGas, 1_568_000));
        RECIPES.put(Pair.of(6, 2), MaterialLibAPI.getFluidStack(Materials.Tritium, FluidShapes.fluidGas, 240_000));
        RECIPES.put(Pair.of(6, 3), MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 240_000));
        RECIPES.put(Pair.of(6, 4), MaterialLibAPI.getFluidStack(Materials.Xenon, FluidShapes.fluidLiquid, 16_000));
        RECIPES.put(Pair.of(6, 5), MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 1_792_000));

        // T7
        RECIPES.put(Pair.of(7, 1), MaterialUtils.fluid(Materials.HydrofluoricAcidGT5U, 672_000));
        RECIPES.put(Pair.of(7, 2), MaterialLibAPI.getFluidStack(Materials.Fluorine, FluidShapes.fluidGas, 1_792_000));
        RECIPES.put(Pair.of(7, 3), MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidGas, 1_792_000));
        RECIPES.put(Pair.of(7, 4), MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 1_792_000));

        // T8
        RECIPES.put(Pair.of(8, 1), MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 1_568_000));
        RECIPES.put(Pair.of(8, 2), MaterialLibAPI.getFluidStack(Materials.LiquidAir, FluidShapes.fluidLiquid, 875_000));
        RECIPES.put(Pair.of(8, 3), MaterialLibAPI.getFluidStack(Materials.Copper, FluidShapes.fluidMolten, 672_000));
        RECIPES.put(Pair.of(8, 5), GTModHandler.getDistilledWater(17_920_000));
        RECIPES.put(Pair.of(8, 6), MaterialLibAPI.getFluidStack(Materials.Radon, FluidShapes.fluidGas, 64_000));
        RECIPES.put(Pair.of(8, 7), MaterialLibAPI.getFluidStack(Materials.Tin, FluidShapes.fluidMolten, 672_000));

        if (Mods.GalaxySpace.isModLoaded()) {
            // T8
            RECIPES.put(Pair.of(8, 4), FluidRegistry.getFluidStack("unknowwater", 672_000));
        }
    }
}
