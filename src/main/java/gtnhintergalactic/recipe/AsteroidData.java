package gtnhintergalactic.recipe;

import java.util.Objects;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import org.apache.commons.lang3.builder.ToStringBuilder;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;

public class AsteroidData {

    public final String asteroidName;
    public final int minDistance;
    public final int maxDistance;
    public final int minSize;
    public final int maxSize;
    public final int computation;
    public final int recipeWeight;
    public final int minDroneTier;
    public final int maxDroneTier;
    public final ItemStack[] outputItems;
    public final Materials[] output;
    public final OrePrefixes orePrefixes;
    public final int[] chances;
    public final int requiredModuleTier;
    public final int duration;

    public AsteroidData(String asteroidName, int minDistance, int maxDistance, int minSize, int maxSize,
        int computation, int recipeWeight, int minDroneTier, int maxDroneTier, Materials[] output,
        OrePrefixes orePrefixes, int[] chances, int requiredModuleTier, int duration) {
        this.asteroidName = asteroidName;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.computation = computation;
        this.recipeWeight = recipeWeight;
        this.minDroneTier = minDroneTier;
        this.maxDroneTier = maxDroneTier;
        this.output = output;
        this.orePrefixes = orePrefixes;
        this.chances = chances;
        this.requiredModuleTier = requiredModuleTier;
        this.outputItems = null;
        this.duration = duration;
    }

    public AsteroidData(String asteroidName, int minDistance, int maxDistance, int minSize, int maxSize,
        int computation, int recipeWeight, int minDroneTier, int maxDroneTier, ItemStack[] outputItems, int[] chances,
        int requiredModuleTier, int duration) {
        this.asteroidName = asteroidName;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.computation = computation;
        this.recipeWeight = recipeWeight;
        this.minDroneTier = minDroneTier;
        this.maxDroneTier = maxDroneTier;
        this.outputItems = outputItems;
        this.chances = chances;
        this.requiredModuleTier = requiredModuleTier;
        this.orePrefixes = null;
        this.output = null;
        this.duration = duration;
    }

    public String getAsteroidNameLocalized() {
        return StatCollector.translateToLocal("ig.asteroid." + asteroidName) + " "
            + StatCollector.translateToLocal("ig.asteroid");
    }

    @Override
    public int hashCode() {
        return Objects.hash(asteroidName, minDistance, maxDistance, minSize, maxSize, computation, recipeWeight);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AsteroidData that = (AsteroidData) o;

        if (minDistance != that.minDistance) return false;
        if (maxDistance != that.maxDistance) return false;
        if (minSize != that.minSize) return false;
        if (maxSize != that.maxSize) return false;
        if (computation != that.computation) return false;
        if (recipeWeight != that.recipeWeight) return false;
        return asteroidName.equals(that.asteroidName);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("asteroidName", asteroidName)
            .append("minDistance", minDistance)
            .append("maxDistance", maxDistance)
            .append("minSize", minSize)
            .append("maxSize", maxSize)
            .append("computation", computation)
            .append("recipeWeight", recipeWeight)
            .toString();
    }
}
