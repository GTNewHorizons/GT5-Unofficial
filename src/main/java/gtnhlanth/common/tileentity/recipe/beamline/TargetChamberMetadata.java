package gtnhlanth.common.tileentity.recipe.beamline;

import net.minecraft.item.ItemStack;

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
