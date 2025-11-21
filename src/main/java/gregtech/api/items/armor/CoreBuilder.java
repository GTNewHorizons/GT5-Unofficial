package gregtech.api.items.armor;

public class CoreBuilder extends ArmorPartBuilder<CoreBuilder> {

    private int tier, chargeTier, chargeMax;

    public int getTier() {
        return tier;
    }

    public int getChargeTier() {
        return chargeTier;
    }

    public int getChargeMax() {
        return chargeMax;
    }

    public CoreBuilder setTier(int tier) {
        this.tier = tier;
        return this;
    }

    public CoreBuilder setChargeTier(int chargeTier) {
        this.chargeTier = chargeTier;
        return this;
    }

    public CoreBuilder setChargeMax(int chargeMax) {
        this.chargeMax = chargeMax;
        return this;
    }
}
