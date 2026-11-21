package kekztech.common.itemBlocks;

import static com.google.common.math.LongMath.pow;
import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.math.BigInteger;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import gregtech.api.enums.GTValues;

public class ItemBlockLapotronicEnergyUnit extends ItemBlock {

    public ItemBlockLapotronicEnergyUnit(Block block) {
        super(block);
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }

    @Override
    public boolean getHasSubtypes() {
        return true;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + stack.getItemDamage();
    }

    public static final int LSC_WIRELESS_EU_CAP_TIER_MULTIPLIER = 100;

    // 5 Minutes, 5 mins * 60s * 20 ticks.
    public static final long LSC_TIME_BETWEEN_WIRELESS_REBALANCE_IN_TICKS = 5L * 60L * 20L;

    // 60 Trillion EU.
    public static final BigInteger LSC_WIRELESS_EU_CAP = BigInteger.valueOf(60 * pow(10, 12));

    // 10 Billion EU/t
    private static final BigInteger UHV_CAP_EU_PER_TICK = LSC_WIRELESS_EU_CAP
        .divide(BigInteger.valueOf(LSC_TIME_BETWEEN_WIRELESS_REBALANCE_IN_TICKS));

    // 6 Quadrillion EU.
    public static final BigInteger UEV_WIRELESS_EU_CAP = LSC_WIRELESS_EU_CAP
        .multiply(BigInteger.valueOf(LSC_WIRELESS_EU_CAP_TIER_MULTIPLIER));

    // 1 Trillion EU/t
    private static final BigInteger UEV_CAP_EU_PER_TICK = UEV_WIRELESS_EU_CAP
        .divide(BigInteger.valueOf(LSC_TIME_BETWEEN_WIRELESS_REBALANCE_IN_TICKS));

    // 600 Quadrillion EU.
    public static final BigInteger UIV_WIRELESS_EU_CAP = UEV_WIRELESS_EU_CAP
        .multiply(BigInteger.valueOf(LSC_WIRELESS_EU_CAP_TIER_MULTIPLIER));

    // 100 Trillion EU/t
    private static final BigInteger UIV_CAP_EU_PER_TICK = UIV_WIRELESS_EU_CAP
        .divide(BigInteger.valueOf(LSC_TIME_BETWEEN_WIRELESS_REBALANCE_IN_TICKS));

    // 60 Quintillion EU.
    public static final BigInteger UMV_WIRELESS_EU_CAP = UIV_WIRELESS_EU_CAP
        .multiply(BigInteger.valueOf(LSC_WIRELESS_EU_CAP_TIER_MULTIPLIER));

    // 10 Quadrillion EU/t
    private static final BigInteger UMV_CAP_EU_PER_TICK = UMV_WIRELESS_EU_CAP
        .divide(BigInteger.valueOf(LSC_TIME_BETWEEN_WIRELESS_REBALANCE_IN_TICKS));

    public static final long EV_CAP_STORAGE = 60_000_000L;
    public static final long IV_CAP_STORAGE = 600_000_000L;
    public static final long LUV_CAP_STORAGE = 6_000_000_000L;
    public static final long ZPM_CAP_STORAGE = 60_000_000_000L;
    public static final long UV_CAP_STORAGE = 600_000_000_000L;
    public static final long UHV_CAP_STORAGE = Long.MAX_VALUE;
    public static final long UEV_CAP_STORAGE = Long.MAX_VALUE;
    public static final long UIV_CAP_STORAGE = Long.MAX_VALUE;
    public static final BigInteger UMV_CAP_STORAGE = BigInteger.valueOf(UIV_CAP_STORAGE)
        .pow(2);

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> lines, boolean advancedTooltips) {
        lines.add(StatCollector.translateToLocal("tile.kekztech_lapotronicenergyunit_block.desc"));
        switch (stack.getItemDamage()) {
            case 1:
                lines.add(
                    StatCollector.translateToLocalFormatted(
                        "tooltip.kekztech.lapotronic_energy_unit.capacity",
                        formatNumber(IV_CAP_STORAGE)));
                break;
            case 2:
                lines.add(
                    StatCollector.translateToLocalFormatted(
                        "tooltip.kekztech.lapotronic_energy_unit.capacity",
                        formatNumber(LUV_CAP_STORAGE)));
                break;
            case 3:
                lines.add(
                    StatCollector.translateToLocalFormatted(
                        "tooltip.kekztech.lapotronic_energy_unit.capacity",
                        formatNumber(ZPM_CAP_STORAGE)));
                break;
            case 4:
                lines.add(
                    StatCollector.translateToLocalFormatted(
                        "tooltip.kekztech.lapotronic_energy_unit.capacity",
                        formatNumber(UV_CAP_STORAGE)));
                break;
            case 5:
                lines.add(
                    StatCollector.translateToLocalFormatted(
                        "tooltip.kekztech.lapotronic_energy_unit.capacity",
                        formatNumber(UHV_CAP_STORAGE)));
                lines.add(
                    StatCollector.translateToLocalFormatted(
                        "tooltip.kekztech.lapotronic_energy_unit.wire_less_transfer",
                        formatNumber(UHV_CAP_EU_PER_TICK),
                        GTValues.TIER_COLORS[9] + GTValues.VN[9]));
                break;
            case 6:
                lines.add(StatCollector.translateToLocal("tooltip.kekztech.lapotronic_energy_unit.capacity.none"));
                break;
            case 7:
                lines.add(
                    StatCollector.translateToLocalFormatted(
                        "tooltip.kekztech.lapotronic_energy_unit.capacity",
                        formatNumber(EV_CAP_STORAGE)));
                break;
            case 8:
                lines.add(
                    StatCollector.translateToLocalFormatted(
                        "tooltip.kekztech.lapotronic_energy_unit.capacity",
                        formatNumber(UEV_CAP_STORAGE)));
                lines.add(
                    StatCollector.translateToLocalFormatted(
                        "tooltip.kekztech.lapotronic_energy_unit.wire_less_transfer",
                        formatNumber(UEV_CAP_EU_PER_TICK),
                        GTValues.TIER_COLORS[10] + GTValues.VN[10]));
                break;
            case 9:
                lines.add(
                    StatCollector.translateToLocalFormatted(
                        "tooltip.kekztech.lapotronic_energy_unit.capacity",
                        formatNumber(UIV_CAP_STORAGE)));
                lines.add(
                    StatCollector.translateToLocalFormatted(
                        "tooltip.kekztech.lapotronic_energy_unit.wire_less_transfer",
                        formatNumber(UIV_CAP_EU_PER_TICK),
                        GTValues.TIER_COLORS[11] + GTValues.VN[11]));
                break;
            case 10:
                lines.add(
                    StatCollector.translateToLocalFormatted(
                        "tooltip.kekztech.lapotronic_energy_unit.capacity",
                        formatNumber(UMV_CAP_STORAGE)));
                lines.add(
                    StatCollector.translateToLocalFormatted(
                        "tooltip.kekztech.lapotronic_energy_unit.wire_less_transfer",
                        formatNumber(UMV_CAP_EU_PER_TICK),
                        GTValues.TIER_COLORS[12] + GTValues.VN[12]));
                break;
        }
    }
}
