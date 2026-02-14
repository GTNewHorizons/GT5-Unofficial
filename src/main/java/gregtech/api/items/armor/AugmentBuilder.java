package gregtech.api.items.armor;

import net.minecraft.item.EnumRarity;

public class AugmentBuilder extends ArmorPartBuilder<AugmentBuilder> {

    public enum AugmentCategory {
        // Order sensitive - do not rearrange
        Protection,
        Movement,
        Utility,
        Prismatic
    }

    private AugmentCategory category = AugmentCategory.Protection;
    private int minimumCore = 0;
    /// The maximum number of times this augment can be installed.
    private int maxStack = 1;
    private EnumRarity rarity;

    public AugmentBuilder setCategory(AugmentCategory category) {
        onMutated();
        this.category = category;
        return this;
    }

    public AugmentBuilder setMinimumCore(int minimumCore) {
        onMutated();
        this.minimumCore = minimumCore;
        return this;
    }

    public AugmentBuilder setMaxStack(int maxStack) {
        onMutated();
        this.maxStack = maxStack;
        return this;
    }

    public AugmentBuilder setRarity(EnumRarity rarity) {
        onMutated();
        this.rarity = rarity;
        return this;
    }

    public AugmentCategory getCategory() {
        return category;
    }

    public int getMinimumCore() {
        return minimumCore;
    }

    public int getMaxStack() {
        return maxStack;
    }

    public EnumRarity getRarity() {
        return rarity;
    }
}
