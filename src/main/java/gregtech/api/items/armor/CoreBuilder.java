package gregtech.api.items.armor;

import net.minecraft.item.EnumRarity;

public class CoreBuilder extends ArmorPartBuilder<CoreBuilder> {

    private int tier, chargeTier, chargeMax;
    private EnumRarity rarity = EnumRarity.common;

    public int getTier() {
        return tier;
    }

    public int getChargeTier() {
        return chargeTier;
    }

    public int getChargeMax() {
        return chargeMax;
    }

    public EnumRarity getRarity() {
        return rarity;
    }

    public CoreBuilder setTier(int tier) {
        onMutated();
        this.tier = tier;
        return this;
    }

    public CoreBuilder setChargeTier(int chargeTier) {
        onMutated();
        this.chargeTier = chargeTier;
        return this;
    }

    public CoreBuilder setChargeMax(int chargeMax) {
        onMutated();
        this.chargeMax = chargeMax;
        return this;
    }

    public CoreBuilder setRarity(EnumRarity rarity) {
        onMutated();
        this.rarity = rarity;
        return this;
    }
}
