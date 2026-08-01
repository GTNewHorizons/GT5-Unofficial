package kubatech.tileentity.gregtech.multiblock.eigmodes;

import static gregtech.api.util.StringUtils.voltageTooltipFormatted;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.ResourceLocation;

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
        Map<String, Object> ttVars = new HashMap<>();
        ttVars.put("minVoltageTier", voltageTooltipFormatted(this.getMinVoltageTier()));
        ttVars.put("minVoltageTierMinus1", voltageTooltipFormatted(this.getMinVoltageTier() - 1));
        ttVars.put("startingSlotCount", this.getStartingSlotCount());
        ttVars.put("slotPerTierMultiplier", this.getSlotPerTierMultiplier());
        ttVars.put("seedCapacityPerSlot", this.getSeedCapacityPerSlot());
        ttVars.put("maxFertilizerUsagePerSeed", this.getMaxFertilizerUsagePerSeed());
        ttVars.put("fertilizerBonus", String.format("%.0f%%", this.getFertilizerBoost() * 100));

        return builder.addMarkdown(
            new ResourceLocation("gregtech", "extreme-industrial-greenhouse-normal-mode"),
            ttVars);
    }

    @Override
    public int getSlotCount(int machineTier) {
        int tierAboveMin = machineTier - this.getMinVoltageTier();
        if (tierAboveMin < 0) return 0;
        return (1 << tierAboveMin);
    }
}
