package gregtech.api.items.armor;

public class AugmentBuilder extends ArmorPartBuilder<AugmentBuilder> {

    public enum AugmentCategory {
        // Order sensitive - do not rearrange
        Protection,
        Movement,
        Utility,
        Prismatic
    }

    private AugmentCategory category = AugmentCategory.Protection;
    private int minimumCore = 1;
    /// The maximum number of times this augment can be installed.
    private int maxStack = 1;

    public AugmentBuilder category(AugmentCategory category) {
        onMutated();
        this.category = category;
        return this;
    }

    public AugmentBuilder minimumCore(int minimumCore) {
        onMutated();
        this.minimumCore = minimumCore;
        return this;
    }

    public AugmentBuilder maxStack(int maxStack) {
        onMutated();
        this.maxStack = maxStack;
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
}
