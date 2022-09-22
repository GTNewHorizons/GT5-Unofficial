package com.github.technus.tectech.recipe;

import gregtech.api.enums.Materials;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Pair;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class EyeOfHarmonyRecipeStorage {

    public static EyeOfHarmonyRecipe recipe_0 = new EyeOfHarmonyRecipe(
            Stream.of(
                    Pair.of(Materials.CosmicNeutronium.getDust(1), 100L),
                    Pair.of(Materials.Tin.getDust(1), 100L),
                    Pair.of(Materials.Iron.getDust(1), 100L)).collect(Collectors.toList()),
            new FluidStack[] {
                    Materials.Iron.getPlasma(423_414_412),
                    Materials.Boron.getPlasma(32_314_391),
            },
            10_000_000L,
            10_000_000L,
            1_000_000_000_000L,
            1_000_000_000L,
            500,
            0.40,
            1
    );
}
