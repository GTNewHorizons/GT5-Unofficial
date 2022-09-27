package com.github.technus.tectech.recipe;

import gregtech.api.enums.Materials;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.math.IntMath.pow;

public abstract class EyeOfHarmonyRecipeStorage {

    static final long MILLION = pow(10, 6);
    static final long BILLION = pow(10, 9);
    static final long TRILLION = pow(10, 12);
    static final long QUADRILLION = pow(10, 15);
    static final long QUINTILLION = pow(10, 18);
    static final long SEXTILLION = pow(10, 21);

    // VEGA B (approx), 36000 seconds, 3x multiplier on output (excluding 2x already from ore output):
    // iron:490_898_412L
    // gold:91_781_220L
    // copper:156_028_074L
    // antimony:24_912_048L
    // lead:105_941_754L
    // silver:26_223_204L
    // naquadah:68_180_334L
    // enriched naquadah:3_933_480L
    // tungsten:22_289_724L
    // lithium:1_311_162L
    // uranium:94_403_538L
    // arsenic:62_935_692L
    // bismuth:71_327_118L
    // infused gold:3_933_480L
    // platinum:3_933_480L
    // chrome:7_866_960L
    // neutronium:18_880_710L
    // adamantium:10_489_284L
    // titanium:1_311_162L
    // niobium:62_935_692L
    // yttrium:62_935_692L
    // gallium:15_733_926L
    // thorium:62_935_692L
    // uranium 238:62_935_692L
    // plutonium 241:7_866_960L
    // uranium 235:7_866_960L
    // cadmium:41_957_130L
    // caesium:41_957_130L
    // lanthanum:5_244_642L
    // cerium:5_244_642L
    // awakened_draconium:2_097_858L
    // black_plutonium:6_293_568L
    // infinity_catalyst:6_293_568L

    static HashMap<Fluid, Long> plasma_energy_map = new HashMap<Fluid, Long>()  {{
        put(Materials.Helium.getPlasma(1).getFluid(), 81_920L);
        put(Materials.Boron.getPlasma(1).getFluid(), 112_640L);
        put(Materials.Nitrogen.getPlasma(1).getFluid(), 129_024L);
        put(Materials.Oxygen.getPlasma(1).getFluid(), 131_072L);
        put(Materials.Sulfur.getPlasma(1).getFluid(), 170_393L);
        put(Materials.Calcium.getPlasma(1).getFluid(), 188_416L);
        put(Materials.Titanium.getPlasma(1).getFluid(), 196_608L);
        put(Materials.Iron.getPlasma(1).getFluid(), 206_438L);
        put(Materials.Nickel.getPlasma(1).getFluid(), 213_811L);
        put(Materials.Zinc.getPlasma(1).getFluid(), 226_304L);
        put(Materials.Niobium.getPlasma(1).getFluid(), 269_516L);
        put(Materials.Silver.getPlasma(1).getFluid(), 282_685L);
        put(Materials.Tin.getPlasma(1).getFluid(), 304_496L);
        put(Materials.Americium.getPlasma(1).getFluid(), 501_760L);
        put(Materials.Radon.getPlasma(1).getFluid(), 450_560L);
        put(Materials.Bismuth.getPlasma(1).getFluid(), 425_984L);
    }};

    static long plasma_cost_calculator(FluidStack[] plasmas, double efficiency) {
        long total = 0;

        for (FluidStack plasma : plasmas) {
            total += (plasma_energy_map.get(plasma.getFluid()) * plasma.amount);
        }

        return (long) (total * efficiency);
    }

    static FluidStack[] valid_plasma_generator(final List<Pair<Materials, Long>> planet_list, final double multiplier) {

        List<FluidStack> plasma_list = new ArrayList<>();

        for (Pair<Materials, Long> pair : planet_list) {
            if (valid_plasmas.contains(pair.getLeft())) {
                plasma_list.add(pair.getLeft().getPlasma((int) (pair.getRight() * multiplier)));
            }
        }
        return plasma_list.toArray(new FluidStack[0]);
    }

    static List<Pair<ItemStack, Long>> valid_dust_generator(final List<Pair<Materials, Long>> planet_list, final double multiplier) {

        List<Pair<ItemStack, Long>> dust_list = new ArrayList<>();

        for (Pair<Materials, Long> pair : planet_list) {
            if (valid_plasmas.contains(pair.getLeft())) {
                dust_list.add(Pair.of(pair.getLeft().getDust(1), (long) (pair.getRight() * multiplier)));
            }
        }
        return dust_list;
    }

    static final  List<Materials> valid_plasmas = Stream.of(
            Materials.Copper,
            Materials.Silver,
            Materials.Helium,
            Materials.Boron,
            Materials.Nitrogen,
            Materials.Oxygen,
            Materials.Sulfur,
            Materials.Calcium,
            Materials.Titanium,
            Materials.Iron,
            Materials.Nickel,
            Materials.Zinc,
            Materials.Niobium,
            Materials.Silver,
            Materials.Tin,
            Materials.Bismuth,
            Materials.Americium,
            Materials.Niobium
    ).collect(Collectors.toList());

    // Vega B weights (approximately). 36000 seconds, Og, UV VM, 2x for ore processing and a final 3x output bonus.
    static final List<Pair<Materials, Long>> vega_b = Stream.of(
            Pair.of(Materials.Iron, 490_898_412L),
            Pair.of(Materials.Gold, 91_781_220L),
            Pair.of(Materials.Copper, 156_028_074L),
            Pair.of(Materials.Antimony, 24_912_048L),
            Pair.of(Materials.Lead, 105_941_754L),
            Pair.of(Materials.Silver, 26_223_204L),
            Pair.of(Materials.Naquadah, 68_180_334L),
            Pair.of(Materials.NaquadahEnriched, 3_933_480L),
            Pair.of(Materials.Tungsten, 22_289_724L),
            Pair.of(Materials.Lithium, 1_311_162L),
            Pair.of(Materials.Uranium235, 94_403_538L + 156_028_074L),
            Pair.of(Materials.Arsenic, 62_935_692L),
            Pair.of(Materials.Bismuth, 71_327_118L),
            Pair.of(Materials.InfusedGold, 3_933_480L),
            Pair.of(Materials.Platinum, 3_933_480L),
            Pair.of(Materials.Chrome, 7_866_960L),
            Pair.of(Materials.Neutronium, 18_880_710L),
            Pair.of(Materials.Adamantium, 10_489_284L),
            Pair.of(Materials.Titanium, 1_311_162L),
            Pair.of(Materials.Niobium, 62_935_692L),
            Pair.of(Materials.Yttrium, 62_935_692L),
            Pair.of(Materials.Gallium, 15_733_926L),
            Pair.of(Materials.Thorium, 62_935_692L),
            Pair.of(Materials.Uranium, 62_935_692L),
            Pair.of(Materials.Plutonium241, 7_866_960L),
            Pair.of(Materials.Cadmium, 41_957_130L),
            Pair.of(Materials.Caesium, 41_957_130L),
            Pair.of(Materials.Lanthanum, 5_244_642L),
            Pair.of(Materials.Cerium, 5_244_642L),
            Pair.of(Materials.DraconiumAwakened, 2_097_858L),
            Pair.of(Materials.BlackPlutonium, 6_293_568L),
            Pair.of(Materials.InfinityCatalyst, 6_293_568L)
    ).collect(Collectors.toList());

    static final FluidStack[] vega_b_plasmas = valid_plasma_generator(vega_b, 0.1);

    static final long vega_b_seconds = 36_000L;

    public static final EyeOfHarmonyRecipe recipe_0 = new EyeOfHarmonyRecipe(
            valid_dust_generator(vega_b, 1),
            vega_b_plasmas,
            100,
            100,
            1800 * TRILLION,
            524_288L * vega_b_seconds * 20 + 600 * TRILLION + plasma_cost_calculator(vega_b_plasmas, 1),
            vega_b_seconds * 20,
            0.40,
            1
    );

    static final EyeOfHarmonyRecipe recipe_1 = new EyeOfHarmonyRecipe(
            valid_dust_generator(vega_b, 1),
            vega_b_plasmas,
            100 * BILLION,
            100 * BILLION,
            1800 * TRILLION,
            524_288L * vega_b_seconds * 20 + 600 * TRILLION + plasma_cost_calculator(vega_b_plasmas, 1),
            vega_b_seconds * 20,
            0.40,
            1
    );

}
