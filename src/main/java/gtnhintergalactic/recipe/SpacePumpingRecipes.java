package gtnhintergalactic.recipe;

import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
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
        RECIPES.put(
            Pair.of(2, 1),
            MaterialLibAPI
                .getFluidStack(Materials2Materials.Chlorobenzene, Materials2FluidShapes.fluidLiquid, (int) (896_000)));

        // T3
        if (Mods.HardcoreEnderExpansion.isModLoaded()) {
            RECIPES.put(Pair.of(3, 1), FluidRegistry.getFluidStack("endergoo", 32_000));
        }
        RECIPES.put(
            Pair.of(3, 2),
            MaterialLibAPI.getFluidStack(
                Materials2Materials.OilExtraHeavy,
                Materials2FluidShapes.fluidLiquid,
                (int) (1_400_000)));
        RECIPES.put(Pair.of(3, 3), Materials.Lava.getFluid(1_800_000));
        RECIPES.put(Pair.of(3, 4), Materials.NaturalGas.getGas(1_400_000));

        // T4
        RECIPES.put(
            Pair.of(4, 1),
            MaterialLibAPI
                .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.fluidLiquid, (int) (784_000)));
        RECIPES.put(
            Pair.of(4, 2),
            MaterialLibAPI.getFluidStack(Materials2Materials.Iron, Materials2FluidShapes.fluidMolten, (int) (896_000)));
        RECIPES.put(
            Pair.of(4, 3),
            MaterialLibAPI
                .getFluidStack(Materials2Materials.Oil, Materials2FluidShapes.fluidLiquid, (int) (1_400_000)));
        RECIPES.put(
            Pair.of(4, 4),
            MaterialLibAPI
                .getFluidStack(Materials2Materials.OilHeavy, Materials2FluidShapes.fluidLiquid, (int) (1_792_000)));
        RECIPES.put(
            Pair.of(4, 5),
            MaterialLibAPI.getFluidStack(Materials2Materials.Lead, Materials2FluidShapes.fluidMolten, (int) (896_000)));
        RECIPES.put(
            Pair.of(4, 6),
            MaterialLibAPI
                .getFluidStack(Materials2Materials.OilMedium, Materials2FluidShapes.fluidLiquid, (int) (1_400_000)));
        RECIPES.put(
            Pair.of(4, 7),
            MaterialLibAPI
                .getFluidStack(Materials2Materials.OilLight, Materials2FluidShapes.fluidLiquid, (int) (780_000)));
        RECIPES.put(Pair.of(4, 8), FluidRegistry.getFluidStack("carbondioxide", 1_680_000));

        // T5
        RECIPES.put(
            Pair.of(5, 1),
            MaterialLibAPI
                .getFluidStack(Materials2Materials.CarbonMonoxide, Materials2FluidShapes.fluidGas, (int) (4_480_000)));
        RECIPES.put(Pair.of(5, 2), Materials.Helium3.getGas(2_800_000));
        RECIPES.put(
            Pair.of(5, 3),
            MaterialLibAPI
                .getFluidStack(Materials2Materials.SaltWater, Materials2FluidShapes.fluidLiquid, (int) (2_800_000)));
        RECIPES.put(
            Pair.of(5, 4),
            MaterialLibAPI
                .getFluidStack(Materials2Materials.Helium, Materials2FluidShapes.fluidGas, (int) (1_400_000)));
        RECIPES.put(
            Pair.of(5, 5),
            MaterialLibAPI
                .getFluidStack(Materials2Materials.LiquidOxygen, Materials2FluidShapes.fluidGas, (int) (896_000)));
        RECIPES.put(Pair.of(5, 6), WerkstoffLoader.Neon.getFluidOrGas(32_000));
        RECIPES.put(
            Pair.of(5, 7),
            MaterialLibAPI.getFluidStack(Materials2Materials.Argon, Materials2FluidShapes.fluidGas, (int) (32_000)));
        RECIPES.put(Pair.of(5, 8), WerkstoffLoader.Krypton.getFluidOrGas(8_000));
        RECIPES.put(
            Pair.of(5, 9),
            MaterialLibAPI
                .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (1_792_000)));
        RECIPES.put(
            Pair.of(5, 10),
            MaterialLibAPI
                .getFluidStack(Materials2Materials.HydricSulfide, Materials2FluidShapes.fluidGas, (int) (392_000)));
        RECIPES.put(
            Pair.of(5, 11),
            MaterialLibAPI
                .getFluidStack(Materials2Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (1_194_000)));

        // T6
        RECIPES.put(
            Pair.of(6, 1),
            MaterialLibAPI
                .getFluidStack(Materials2Materials.Deuterium, Materials2FluidShapes.fluidGas, (int) (1_568_000)));
        RECIPES.put(
            Pair.of(6, 2),
            MaterialLibAPI.getFluidStack(Materials2Materials.Tritium, Materials2FluidShapes.fluidGas, (int) (240_000)));
        RECIPES.put(
            Pair.of(6, 3),
            MaterialLibAPI.getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.fluidGas, (int) (240_000)));
        RECIPES.put(Pair.of(6, 4), WerkstoffLoader.Xenon.getFluidOrGas(16_000));
        RECIPES.put(
            Pair.of(6, 5),
            MaterialLibAPI
                .getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (1_792_000)));

        // T7
        RECIPES.put(Pair.of(7, 1), Materials.HydrofluoricAcid.getFluid(672_000));
        RECIPES.put(
            Pair.of(7, 2),
            MaterialLibAPI
                .getFluidStack(Materials2Materials.Fluorine, Materials2FluidShapes.fluidGas, (int) (1_792_000)));
        RECIPES.put(
            Pair.of(7, 3),
            MaterialLibAPI
                .getFluidStack(Materials2Materials.Nitrogen, Materials2FluidShapes.fluidGas, (int) (1_792_000)));
        RECIPES.put(
            Pair.of(7, 4),
            MaterialLibAPI
                .getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (1_792_000)));

        // T8
        RECIPES.put(
            Pair.of(8, 1),
            MaterialLibAPI
                .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (1_568_000)));
        RECIPES.put(
            Pair.of(8, 2),
            MaterialLibAPI
                .getFluidStack(Materials2Materials.LiquidAir, Materials2FluidShapes.fluidLiquid, (int) (875_000)));
        RECIPES.put(
            Pair.of(8, 3),
            MaterialLibAPI
                .getFluidStack(Materials2Materials.Copper, Materials2FluidShapes.fluidMolten, (int) (672_000)));
        RECIPES.put(Pair.of(8, 5), GTModHandler.getDistilledWater(17_920_000));
        RECIPES.put(
            Pair.of(8, 6),
            MaterialLibAPI.getFluidStack(Materials2Materials.Radon, Materials2FluidShapes.fluidGas, (int) (64_000)));
        RECIPES.put(
            Pair.of(8, 7),
            MaterialLibAPI.getFluidStack(Materials2Materials.Tin, Materials2FluidShapes.fluidMolten, (int) (672_000)));

        if (Mods.GalaxySpace.isModLoaded()) {
            // T8
            RECIPES.put(Pair.of(8, 4), FluidRegistry.getFluidStack("unknowwater", 672_000));
        }
    }
}
