package kubatech.tileentity.gregtech.multiblock.eigmodes;

import static gregtech.api.util.StringUtils.voltageTooltipFormatted;
import static kubatech.tileentity.gregtech.multiblock.MTEExtremeIndustrialGreenhouse.EIG_BALANCE_IC2_ACCELERATOR_TIER;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

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

        return builder.addSeparator()
            .addInfo(
                EnumChatFormatting.RED + StatCollector.translateToLocal("kubatech.multiblock.EIGIC2Mode.deprecated")
                    + EnumChatFormatting.RESET)
            .addInfo(EnumChatFormatting.GOLD + StatCollector.translateToLocal("kubatech.multiblock.EIGIC2Mode.header"))
            .addInfo(StatCollector.translateToLocalFormatted("kubatech.multiblock.EIGIC2Mode.desc1", minVoltageTier))
            .addInfo(StatCollector.translateToLocalFormatted("kubatech.multiblock.EIGIC2Mode.desc2", minGlassTier))
            .addInfo(
                StatCollector
                    .translateToLocalFormatted("kubatech.multiblock.EIGIC2Mode.desc3", this.getStartingSlotCount()))
            .addInfo(
                StatCollector.translateToLocalFormatted(
                    "kubatech.multiblock.EIGIC2Mode.desc4",
                    minVoltageTier,
                    this.getSlotPerTierMultiplier()))
            .addInfo(
                StatCollector
                    .translateToLocalFormatted("kubatech.multiblock.EIGIC2Mode.desc5", this.getSeedCapacityPerSlot()))
            .addInfo(StatCollector.translateToLocal("kubatech.multiblock.EIGIC2Mode.desc6"))
            .addInfo(StatCollector.translateToLocalFormatted("kubatech.multiblock.EIGIC2Mode.desc7", acceleration))
            .addInfo(
                StatCollector.translateToLocalFormatted(
                    "kubatech.multiblock.EIGIC2Mode.desc8",
                    this.getMaxFertilizerUsagePerSeed()))
            .addInfo(StatCollector.translateToLocalFormatted("kubatech.multiblock.EIGIC2Mode.desc9", fertilizerBonus))
            .addInfo(
                StatCollector
                    .translateToLocalFormatted("kubatech.multiblock.EIGIC2Mode.desc10", this.getWeedEXMultiplier()));
    }

    @Override
    public int getSlotCount(int machineTier) {
        int tierAboveMin = machineTier - this.getMinVoltageTier();
        if (tierAboveMin < 0) return 0;
        return 4 << (2 * (tierAboveMin));
    }

}
