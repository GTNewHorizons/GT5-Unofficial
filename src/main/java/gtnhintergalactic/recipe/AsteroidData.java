package gtnhintergalactic.recipe;

import java.util.Objects;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.StoneType;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.util.GTUtility;
import gregtech.common.ores.GTOreAdapter;

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
    public final Material[] output;
    public final StoneType stoneType;
    public final int[] chances;
    public final int requiredModuleTier;
    public final int duration;
    public final int eut;

    public AsteroidData(String asteroidName, int minDistance, int maxDistance, int minSize, int maxSize,
        int computation, int recipeWeight, int minDroneTier, int maxDroneTier, Material[] output, StoneType stoneType,
        int[] chances, int requiredModuleTier, int duration, int eut) {
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
        this.stoneType = stoneType;
        this.chances = chances;
        this.requiredModuleTier = requiredModuleTier;
        this.outputItems = null;
        this.duration = duration;
        this.eut = eut;
    }

    public AsteroidData(String asteroidName, int minDistance, int maxDistance, int minSize, int maxSize,
        int computation, int recipeWeight, int minDroneTier, int maxDroneTier, ItemStack[] outputItems, int[] chances,
        int requiredModuleTier, int duration, int eut) {
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
        this.stoneType = null;
        this.output = null;
        this.duration = duration;
        this.eut = eut;
    }

    /// The stack a space mining recipe yields of `material`: its ore block in `stoneType`, or its dust when
    /// `stoneType` is null. Null when the material has no such stack.
    public static @Nullable ItemStack oreStack(Material material, @Nullable StoneType stoneType, int amount) {
        if (stoneType == null) return MaterialLibAPI.findStack(material, Shapes.dust, amount);
        ItemStack ore = GTOreAdapter.INSTANCE.getVariantStack(material, stoneType, false);
        return ore == null ? null : GTUtility.copyAmount(amount, ore);
    }

    /// The stack of this asteroid's `index`th output: the declared item, or else [#oreStack] of its material.
    public ItemStack getOutputStack(int index) {
        return outputItems != null ? outputItems[index] : oreStack(output[index], stoneType, 1);
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
