package gtnhlanth.common.tileentity.recipe.beamline;

import java.util.Objects;

import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.builder.ToStringBuilder;

import gregtech.api.util.GTUtility;

public class TargetChamberMetadata {

    public final int particleID;
    public final int amount;

    public final float minEnergy;
    public final float maxEnergy;

    public final float minFocus;
    public final float energyRatio;

    public final ItemStack focusItem;

    private TargetChamberMetadata(ItemStack focusItem, int particleID, int amount, float minEnergy, float maxEnergy,
        float minFocus, float energyRatio) {
        this.particleID = particleID;
        this.amount = amount;
        this.minEnergy = minEnergy;
        this.maxEnergy = maxEnergy;
        this.minFocus = minFocus;
        this.energyRatio = energyRatio;
        this.focusItem = focusItem;
    }

    @Override
    public int hashCode() {
        return Objects.hash(particleID, amount, minEnergy, maxEnergy, minEnergy, energyRatio, focusItem);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TargetChamberMetadata that = (TargetChamberMetadata) o;

        if (particleID != that.particleID) return false;
        if (amount != that.amount) return false;
        if (minEnergy != that.minEnergy) return false;
        if (maxEnergy != that.maxEnergy) return false;
        if (minFocus != that.minFocus) return false;
        if (energyRatio != that.energyRatio) return false;
        return GTUtility.areStacksEqual(focusItem, that.focusItem);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("particleID", particleID)
            .append("amount", amount)
            .append("minEnergy", minEnergy)
            .append("maxEnergy", maxEnergy)
            .append("minFocus", minFocus)
            .append("energyRatio", energyRatio)
            .append("focusItem", focusItem)
            .toString();
    }

    public static Builder builder(ItemStack focusItem) {
        return new Builder(focusItem);
    }

    public static class Builder {

        private final ItemStack focusItem;

        private int particleID;
        private int amount;

        private float minEnergy;
        private float maxEnergy;

        private float minFocus;
        private float energyRatio;

        private Builder(ItemStack focusItem) {
            this.focusItem = focusItem;
        }

        public Builder particleID(int particleID) {
            this.particleID = particleID;
            return this;
        }

        public Builder amount(int amount) {
            this.amount = amount;
            return this;
        }

        public Builder energy(float minEnergy, float maxEnergy, float energyRatio) {
            this.minEnergy = minEnergy;
            this.maxEnergy = maxEnergy;
            this.energyRatio = energyRatio;
            return this;
        }

        public Builder minFocus(float minFocus) {
            this.minFocus = minFocus;
            return this;
        }

        public TargetChamberMetadata build() {
            return new TargetChamberMetadata(
                focusItem,
                particleID,
                amount,
                minEnergy,
                maxEnergy,
                minFocus,
                energyRatio);
        }
    }
}
