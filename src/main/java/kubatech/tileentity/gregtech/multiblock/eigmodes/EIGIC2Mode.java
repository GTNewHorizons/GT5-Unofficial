package kubatech.tileentity.gregtech.multiblock.eigmodes;

import static gregtech.api.util.StringUtils.voltageTooltipFormatted;
import static kubatech.tileentity.gregtech.multiblock.MTEExtremeIndustrialGreenhouse.EIG_BALANCE_IC2_ACCELERATOR_TIER;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.ResourceLocation;

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
        Map<String, Object> ttVars = new HashMap<>();
        ttVars.put("minVoltageTier", voltageTooltipFormatted(this.getMinVoltageTier()));
        ttVars.put("minGlassTier", voltageTooltipFormatted(this.getMinGlassTier()));
        ttVars.put("startingSlotCount", this.getStartingSlotCount());
        ttVars.put("slotPerTierMultiplier", this.getSlotPerTierMultiplier());
        ttVars.put("seedCapacityPerSlot", this.getSeedCapacityPerSlot());
        ttVars.put("acceleration", 1 << EIG_BALANCE_IC2_ACCELERATOR_TIER);
        ttVars.put("maxFertilizerUsagePerSeed", this.getMaxFertilizerUsagePerSeed());
        ttVars.put("fertilizerBonus", String.format("%.0f%%", this.getFertilizerBoost() * 100));
        ttVars.put("weedExMultiplier", this.getWeedEXMultiplier());

        return builder
            .addMarkdown(new ResourceLocation("gregtech", "extreme-industrial-greenhouse-ic2-mode"), ttVars);
    }

    @Override
    public int getSlotCount(int machineTier) {
        int tierAboveMin = machineTier - this.getMinVoltageTier();
        if (tierAboveMin < 0) return 0;
        return 4 << (2 * (tierAboveMin));
    }

}
