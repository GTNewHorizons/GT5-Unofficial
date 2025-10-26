package kubatech.tileentity.gregtech.multiblock.eigmodes;

import static gregtech.api.util.StringUtils.voltageTooltipFormatted;

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

        return builder.addSeparator()
            .addInfo(
                "gt.ei_greenhouse.tips.normal",
                minVoltageTier,
                this.getStartingSlotCount(),
                minVoltageTier,
                this.getSlotPerTierMultiplier(),
                this.getSeedCapacityPerSlot(),
                minVoltageTierMinus1,
                this.getMaxFertilizerUsagePerSeed(),
                fertilizerBonus);
    }

    @Override
    public int getSlotCount(int machineTier) {
        int tierAboveMin = machineTier - this.getMinVoltageTier();
        if (tierAboveMin < 0) return 0;
        return (1 << tierAboveMin);
    }
}
