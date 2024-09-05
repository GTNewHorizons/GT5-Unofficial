package kubatech.tileentity.gregtech.multiblock.eigmodes;

import static kubatech.api.utils.StringUtils.voltageTooltipFormatted;

import gregtech.api.util.MultiblockTooltipBuilder;
import kubatech.api.eig.EIGMode;
import kubatech.tileentity.gregtech.multiblock.MTEExtremeIndustrialGreenhouse;

public class EIGNormalMode extends EIGMode {

    public static final EIGNormalMode instance = new EIGNormalMode();

    @Override
    public int getUIIndex() {
        return 0;
    }

    @Override
    public String getName() {
        return "normal";
    }

    @Override
    public int getMinVoltageTier() {
        return MTEExtremeIndustrialGreenhouse.EIG_BALANCE_REGULAR_MODE_MIN_TIER;
    }

    @Override
    public int getMinGlassTier() {
        return 0;
    }

    @Override
    public int getStartingSlotCount() {
        return 1;
    }

    @Override
    public int getSlotPerTierMultiplier() {
        return 2;
    }

    @Override
    public int getSeedCapacityPerSlot() {
        return 64;
    }

    @Override
    public int getWeedEXMultiplier() {
        return 1;
    }

    @Override
    public int getMaxFertilizerUsagePerSeed() {
        return 2;
    }

    @Override
    public double getFertilizerBoost() {
        return 2.0d;
    }

    @Override
    public MultiblockTooltipBuilder addTooltipInfo(MultiblockTooltipBuilder builder) {
        String minVoltageTier = voltageTooltipFormatted(this.getMinVoltageTier());
        String minVoltageTierMinus1 = voltageTooltipFormatted(this.getMinVoltageTier() - 1);

        double fertilizerBonusMultiplier = this.getFertilizerBoost() * 100;
        String fertilizerBonus = String.format("%.0f%%", fertilizerBonusMultiplier);

        return builder.addInfo("-------------------- NORMAL CROPS --------------------")
            .addInfo("Minimal voltage tier: " + minVoltageTier)
            .addInfo("Starting with " + this.getStartingSlotCount() + " slot")
            .addInfo(
                "Every tier past " + minVoltageTier + ", slots are multiplied by " + this.getSlotPerTierMultiplier())
            .addInfo("Every slot adds " + this.getSeedCapacityPerSlot() + " seed to the total seed capacity")
            .addInfo("Base process time: 5 sec")
            .addInfo("Process time is divided by number of tiers past " + minVoltageTierMinus1 + " (Minimum 1 sec)")
            .addInfo("All crops are grown at the end of the operation")
            .addInfo("Does not drop seeds")
            .addInfo("Can consume up to " + this.getMaxFertilizerUsagePerSeed() + " fertilizer per seed per cycle")
            .addInfo("Boost per fertilizer: " + fertilizerBonus);
    }

    @Override
    public int getSlotCount(int machineTier) {
        int tierAboveMin = machineTier - this.getMinVoltageTier();
        if (tierAboveMin < 0) return 0;
        return (1 << tierAboveMin);
    }
}
