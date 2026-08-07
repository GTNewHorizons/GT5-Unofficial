package gregtech.api.items.armor;

import java.util.function.Supplier;

import net.minecraft.item.EnumRarity;
import net.minecraft.util.IIcon;

import gregtech.api.items.armor.MechArmorAugmentRegistries.ArmorType;

public class AugmentBuilder extends ArmorPartBuilder<AugmentBuilder> {

    public enum AugmentCategory {
        // Order sensitive - do not rearrange
        Protection,
        Movement,
        Utility,
        Prismatic
    }

    private AugmentCategory category = AugmentCategory.Protection;
    private int minimumCoreTier = 0;
    /// The maximum number of times this augment can be installed.
    private int maxStack = 1;
    private EnumRarity rarity;
    private Supplier<IIcon> textureSupplier = () -> null;

    @SuppressWarnings("unchecked")
    private final Supplier<IIcon>[] textureSuppliers = new Supplier[4];

    public AugmentBuilder setCategory(AugmentCategory category) {
        onMutated();
        this.category = category;
        return this;
    }

    public AugmentBuilder setMinimumCoreTier(int minimumCoreTier) {
        onMutated();
        this.minimumCoreTier = minimumCoreTier;
        rarity = EnumRarity.values()[Math.min(minimumCoreTier - 1, 3)];
        return this;
    }

    public AugmentBuilder setMaxStack(int maxStack) {
        onMutated();
        this.maxStack = maxStack;
        return this;
    }

    public AugmentBuilder setTexture(ArmorType type, Supplier<IIcon> specificSupplier) {
        onMutated();
        this.textureSuppliers[type.ordinal()] = specificSupplier;
        return this;
    }

    public IIcon getTexture(ArmorType armorType) {
        Supplier<IIcon> supplier = this.textureSuppliers[armorType.ordinal()];

        if (supplier != null) {
            return supplier.get();
        }

        return null;
    }

    public AugmentCategory getCategory() {
        return category;
    }

    public int getMinimumCoreTier() {
        return minimumCoreTier;
    }

    public int getMaxStack() {
        return maxStack;
    }

    public EnumRarity getRarity() {
        return rarity;
    }
}
