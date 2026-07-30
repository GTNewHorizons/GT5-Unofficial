package gtnhintergalactic.recipe;

import java.util.HashMap;
import java.util.Map;

import gregtech.api.enums.materials2.Materials;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.Mods;
import gregtech.api.enums.materials2.Materials2FluidShapes;
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
            MaterialLibAPI
                .getFluidStack(Materials.Chlorobenzene, Materials2FluidShapes.fluidLiquid, (int) (896_000)));

        // T3
        if (Mods.HardcoreEnderExpansion.isModLoaded()) {
            RECIPES.put(Pair.of(3, 1), FluidRegistry.getFluidStack("endergoo", 32_000));
        }
        RECIPES.put(
            Pair.of(3, 2),
            MaterialLibAPI.getFluidStack(
                Materials.OilExtraHeavy,
                Materials2FluidShapes.fluidLiquid,
                (int) (1_400_000)));
        RECIPES.put(Pair.of(3, 3), GTUtility.getLava(1_800_000));
        RECIPES.put(Pair.of(3, 4), MaterialUtils.gas(Materials.NatruralGas, 1_400_000));

        // T4
        RECIPES.put(
            Pair.of(4, 1),
            MaterialLibAPI
                .getFluidStack(Materials.SulfuricAcid, Materials2FluidShapes.fluidLiquid, (int) (784_000)));
        RECIPES.put(
            Pair.of(4, 2),
            MaterialLibAPI.getFluidStack(Materials.Iron, Materials2FluidShapes.fluidMolten, (int) (896_000)));
        RECIPES.put(
            Pair.of(4, 3),
            MaterialLibAPI
                .getFluidStack(Materials.Oil, Materials2FluidShapes.fluidLiquid, (int) (1_400_000)));
        RECIPES.put(
            Pair.of(4, 4),
            MaterialLibAPI
                .getFluidStack(Materials.OilHeavy, Materials2FluidShapes.fluidLiquid, (int) (1_792_000)));
        RECIPES.put(
            Pair.of(4, 5),
            MaterialLibAPI.getFluidStack(Materials.Lead, Materials2FluidShapes.fluidMolten, (int) (896_000)));
        RECIPES.put(
            Pair.of(4, 6),
            MaterialLibAPI
                .getFluidStack(Materials.OilMedium, Materials2FluidShapes.fluidLiquid, (int) (1_400_000)));
        RECIPES.put(
            Pair.of(4, 7),
            MaterialLibAPI
                .getFluidStack(Materials.OilLight, Materials2FluidShapes.fluidLiquid, (int) (780_000)));
        RECIPES.put(Pair.of(4, 8), FluidRegistry.getFluidStack("carbondioxide", 1_680_000));

        // T5
        RECIPES.put(
            Pair.of(5, 1),
            MaterialLibAPI
                .getFluidStack(Materials.CarbonMonoxide, Materials2FluidShapes.fluidGas, (int) (4_480_000)));
        RECIPES.put(Pair.of(5, 2), MaterialUtils.gas(Materials.Helium3, 2_800_000));
        RECIPES.put(
            Pair.of(5, 3),
            MaterialLibAPI
                .getFluidStack(Materials.SaltWater, Materials2FluidShapes.fluidLiquid, (int) (2_800_000)));
        RECIPES.put(
            Pair.of(5, 4),
            MaterialLibAPI
                .getFluidStack(Materials.Helium, Materials2FluidShapes.fluidGas, (int) (1_400_000)));
        RECIPES.put(
            Pair.of(5, 5),
            MaterialLibAPI
                .getFluidStack(Materials.LiquidOxygen, Materials2FluidShapes.fluidGas, (int) (896_000)));
        RECIPES.put(
            Pair.of(5, 6),
            MaterialLibAPI.getFluidStack(Materials.Neon, Materials2FluidShapes.fluidLiquid, (int) (32_000)));
        RECIPES.put(
            Pair.of(5, 7),
            MaterialLibAPI.getFluidStack(Materials.Argon, Materials2FluidShapes.fluidGas, (int) (32_000)));
        RECIPES.put(
            Pair.of(5, 8),
            MaterialLibAPI
                .getFluidStack(Materials.Krypton, Materials2FluidShapes.fluidLiquid, (int) (8_000)));
        RECIPES.put(
            Pair.of(5, 9),
            MaterialLibAPI
                .getFluidStack(Materials.Methane, Materials2FluidShapes.fluidGas, (int) (1_792_000)));
        RECIPES.put(
            Pair.of(5, 10),
            MaterialLibAPI
                .getFluidStack(Materials.HydricSulfide, Materials2FluidShapes.fluidGas, (int) (392_000)));
        RECIPES.put(
            Pair.of(5, 11),
            MaterialLibAPI
                .getFluidStack(Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (1_194_000)));

        // T6
        RECIPES.put(
            Pair.of(6, 1),
            MaterialLibAPI
                .getFluidStack(Materials.Deuterium, Materials2FluidShapes.fluidGas, (int) (1_568_000)));
        RECIPES.put(
            Pair.of(6, 2),
            MaterialLibAPI.getFluidStack(Materials.Tritium, Materials2FluidShapes.fluidGas, (int) (240_000)));
        RECIPES.put(
            Pair.of(6, 3),
            MaterialLibAPI.getFluidStack(Materials.Ammonia, Materials2FluidShapes.fluidGas, (int) (240_000)));
        RECIPES.put(
            Pair.of(6, 4),
            MaterialLibAPI.getFluidStack(Materials.Xenon, Materials2FluidShapes.fluidLiquid, (int) (16_000)));
        RECIPES.put(
            Pair.of(6, 5),
            MaterialLibAPI
                .getFluidStack(Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (1_792_000)));

        // T7
        RECIPES.put(Pair.of(7, 1), MaterialUtils.fluid(Materials.HydrofluoricAcidGT5U, 672_000));
        RECIPES.put(
            Pair.of(7, 2),
            MaterialLibAPI
                .getFluidStack(Materials.Fluorine, Materials2FluidShapes.fluidGas, (int) (1_792_000)));
        RECIPES.put(
            Pair.of(7, 3),
            MaterialLibAPI
                .getFluidStack(Materials.Nitrogen, Materials2FluidShapes.fluidGas, (int) (1_792_000)));
        RECIPES.put(
            Pair.of(7, 4),
            MaterialLibAPI
                .getFluidStack(Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (1_792_000)));

        // T8
        RECIPES.put(
            Pair.of(8, 1),
            MaterialLibAPI
                .getFluidStack(Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (1_568_000)));
        RECIPES.put(
            Pair.of(8, 2),
            MaterialLibAPI
                .getFluidStack(Materials.LiquidAir, Materials2FluidShapes.fluidLiquid, (int) (875_000)));
        RECIPES.put(
            Pair.of(8, 3),
            MaterialLibAPI
                .getFluidStack(Materials.Copper, Materials2FluidShapes.fluidMolten, (int) (672_000)));
        RECIPES.put(Pair.of(8, 5), GTModHandler.getDistilledWater(17_920_000));
        RECIPES.put(
            Pair.of(8, 6),
            MaterialLibAPI.getFluidStack(Materials.Radon, Materials2FluidShapes.fluidGas, (int) (64_000)));
        RECIPES.put(
            Pair.of(8, 7),
            MaterialLibAPI.getFluidStack(Materials.Tin, Materials2FluidShapes.fluidMolten, (int) (672_000)));

        if (Mods.GalaxySpace.isModLoaded()) {
            // T8
            RECIPES.put(Pair.of(8, 4), FluidRegistry.getFluidStack("unknowwater", 672_000));
        }
    }
}
