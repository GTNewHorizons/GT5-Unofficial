package kubatech.tileentity.gregtech.multiblock.eigmodes;

import static kubatech.api.utils.StringUtils.voltageTooltipFormatted;
import static kubatech.tileentity.gregtech.multiblock.MTEExtremeIndustrialGreenhouse.EIG_BALANCE_IC2_ACCELERATOR_TIER;

import gregtech.api.util.MultiblockTooltipBuilder;
import kubatech.api.eig.EIGMode;
import kubatech.tileentity.gregtech.multiblock.MTEExtremeIndustrialGreenhouse;

public class EIGIC2Mode extends EIGMode {

    public static final EIGIC2Mode instance = new EIGIC2Mode();

    @Override
    public int getUIIndex() {
        return 1;
    }

    @Override
    public String getName() {
        return "IC2";
    }

    @Override
    public int getMinVoltageTier() {
        return MTEExtremeIndustrialGreenhouse.EIG_BALANCE_IC2_MODE_MIN_TIER;
    }

    @Override
    public int getMinGlassTier() {
        return MTEExtremeIndustrialGreenhouse.EIG_BALANCE_IC2_MODE_MIN_TIER;
    }

    @Override
    public int getStartingSlotCount() {
        return 4;
    }

    @Override
    public int getSlotPerTierMultiplier() {
        return 4;
    }

    @Override
    public int getSeedCapacityPerSlot() {
        return 1;
    }

    @Override
    public int getWeedEXMultiplier() {
        return 5;
    }

    @Override
    public int getMaxFertilizerUsagePerSeed() {
        return 40;
    }

    @Override
    public double getFertilizerBoost() {
        return 0.1d;
    }

    @Override
    public MultiblockTooltipBuilder addTooltipInfo(MultiblockTooltipBuilder builder) {
        String minVoltageTier = voltageTooltipFormatted(this.getMinVoltageTier());
        String minGlassTier = voltageTooltipFormatted(this.getMinGlassTier());

        int acceleration = (1 << EIG_BALANCE_IC2_ACCELERATOR_TIER);

        double fertilizerBonusMultiplier = this.getFertilizerBoost() * 100;
        String fertilizerBonus = String.format("%.0f%%", fertilizerBonusMultiplier);

        return builder.addInfo("---------------------- IC2 CROPS ---------------------")
            .addInfo("Minimal voltage tier: " + minVoltageTier)
            .addInfo("Minimal glass tier: " + minGlassTier)
            .addInfo("Starting with " + this.getStartingSlotCount() + " slot")
            .addInfo(
                "Every tier past " + minVoltageTier + ", slots are multiplied by " + this.getSlotPerTierMultiplier())
            .addInfo("Every slot adds " + this.getSeedCapacityPerSlot() + " seed to the total seed capacity")
            .addInfo("Process time: 5 sec")
            .addInfo("All crops are accelerated by x" + acceleration + " times")
            .addInfo("Can consume up to " + this.getMaxFertilizerUsagePerSeed() + " fertilizer per seed per cycle")
            .addInfo("Boost per fertilizer: " + fertilizerBonus)
            .addInfo("Weed-EX 9000 consumption is multiplied by " + this.getWeedEXMultiplier());
    }

    @Override
    public int getSlotCount(int machineTier) {
        int tierAboveMin = machineTier - this.getMinVoltageTier();
        if (tierAboveMin < 0) return 0;
        return 4 << (2 * (tierAboveMin));
    }

}
