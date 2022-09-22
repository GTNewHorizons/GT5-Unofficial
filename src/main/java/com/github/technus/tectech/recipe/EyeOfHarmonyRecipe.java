package com.github.technus.tectech.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class EyeOfHarmonyRecipe {

    private final List<Pair<ItemStack, Long>> output_items;
    private final FluidStack[] output_fluids;

    private long hydrogen_requirement = Long.MAX_VALUE;
    private long helium_requirement = Long.MAX_VALUE;

    private long eu_output = 0;
    private long eu_start_cost = Long.MAX_VALUE;

    private long recipe_processing_time_in_ticks = Long.MAX_VALUE;

    private double success_chance = 0;

    private long spacetime_casing_tier_required = Long.MAX_VALUE;

    public EyeOfHarmonyRecipe(List<Pair<ItemStack, Long>> _output_items,
                              FluidStack[] _output_fluids,
                              long _hydrogen_requirement,
                              long _helium_requirement,
                              long _eu_output,
                              long _eu_start_cost,
                              long _recipe_processing_time_in_ticks,
                              double _success_chance,
                              long _spacetime_casing_tier_required) {

        output_items = _output_items;
        output_fluids = _output_fluids;

        hydrogen_requirement = _hydrogen_requirement;
        helium_requirement = _helium_requirement;

        eu_output = _eu_output;
        eu_start_cost = _eu_start_cost;

        recipe_processing_time_in_ticks = _recipe_processing_time_in_ticks;

        success_chance = _success_chance;

        // 0 - 7;
        spacetime_casing_tier_required = _spacetime_casing_tier_required;
    }

    public List<Pair<ItemStack, Long>> getOutputItems() {
        return output_items;
    }

    public FluidStack[] getOutputFluids() {
        return output_fluids.clone();
    }

    public long getHydrogenRequirement() {
        return hydrogen_requirement;
    }

    public long getHeliumRequirement() {
        return helium_requirement;
    }

    public long getEUOutput() {
        return eu_output;
    }

    public long getEUStartCost() {
        return eu_start_cost;
    }

    public long getRecipeTime() {
        return recipe_processing_time_in_ticks;
    }

    public double getBaseRecipeSuccessChance() {
        return success_chance;
    }

    public long getSpacetimeCasingTierRequired() {
        return spacetime_casing_tier_required;
    }
}

