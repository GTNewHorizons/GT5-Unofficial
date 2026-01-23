package kekztech.common.itemBlocks;

import static com.google.common.math.LongMath.pow;

import java.math.BigInteger;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import gregtech.api.enums.GTValues;
import gregtech.api.util.GTUtility;

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

    // 5 Minutes, 5 mins * 60s * 20 ticks.
    public static long LSC_time_between_wireless_rebalance_in_ticks = 5L * 60L * 20L;

    // 60 Trillion EU.
    public static BigInteger LSC_wireless_eu_cap = BigInteger.valueOf(60 * pow(10, 12));

    // 10 Billion EU/t
    private static final BigInteger UHV_cap_eu_per_tick = LSC_wireless_eu_cap
        .divide(BigInteger.valueOf(LSC_time_between_wireless_rebalance_in_ticks));

    // 6 Quadrillion EU.
    public static BigInteger UEV_wireless_eu_cap = BigInteger.valueOf(100 * 60 * pow(10, 12));

    // 1 Trillion EU/t
    private static final BigInteger UEV_cap_eu_per_tick = UEV_wireless_eu_cap
        .divide(BigInteger.valueOf(LSC_time_between_wireless_rebalance_in_ticks));

    // 600 Quadrillion EU.
    public static BigInteger UIV_wireless_eu_cap = BigInteger.valueOf(60 * pow(10, 16));

    // 100 Trillion EU/t
    private static final BigInteger UIV_cap_eu_per_tick = UIV_wireless_eu_cap
        .divide(BigInteger.valueOf(LSC_time_between_wireless_rebalance_in_ticks));

    // 60 Quintillion EU.
    public static BigInteger UMV_wireless_eu_cap = UIV_wireless_eu_cap.multiply(BigInteger.valueOf(100));

    // 10 Quadrillion EU/t
    private static final BigInteger UMV_cap_eu_per_tick = UMV_wireless_eu_cap
        .divide(BigInteger.valueOf(LSC_time_between_wireless_rebalance_in_ticks));

    public static long EV_cap_storage = 60_000_000L;
    public static long IV_cap_storage = 600_000_000L;
    public static long LuV_cap_storage = 6_000_000_000L;
    public static long ZPM_cap_storage = 60_000_000_000L;
    public static long UV_cap_storage = 600_000_000_000L;
    public static long UHV_cap_storage = Long.MAX_VALUE;
    public static long UEV_cap_storage = Long.MAX_VALUE;
    public static long UIV_cap_storage = Long.MAX_VALUE;
    public static BigInteger UMV_cap_storage = BigInteger.valueOf(UIV_cap_storage)
        .pow(2);

    @SuppressWarnings("unchecked")
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean advancedTooltips) {
        lines.add(StatCollector.translateToLocal("tile.kekztech_lapotronicenergyunit_block.desc"));
        switch (stack.getItemDamage()) {
            case 1:
                lines.add(
                    StatCollector.translateToLocalFormatted(
                        "tooltip.kekztech.lapotronic_energy_unit.capacity",
                        formatNumber(IV_cap_storage)));
                break;
            case 2:
                lines.add(
                    StatCollector.translateToLocalFormatted(
                        "tooltip.kekztech.lapotronic_energy_unit.capacity",
                        formatNumber(LuV_cap_storage)));
                break;
            case 3:
                lines.add(
                    StatCollector.translateToLocalFormatted(
                        "tooltip.kekztech.lapotronic_energy_unit.capacity",
                        formatNumber(ZPM_cap_storage)));
                break;
            case 4:
                lines.add(
                    StatCollector.translateToLocalFormatted(
                        "tooltip.kekztech.lapotronic_energy_unit.capacity",
                        formatNumber(UV_cap_storage)));
                break;
            case 5:
                lines.add(
                    StatCollector.translateToLocalFormatted(
                        "tooltip.kekztech.lapotronic_energy_unit.capacity",
                        formatNumber(UHV_cap_storage)));
                lines.add(
                    StatCollector.translateToLocalFormatted(
                        "tooltip.kekztech.lapotronic_energy_unit.wire_less_transfer",
                        formatNumber(UHV_cap_eu_per_tick),
                        GTValues.TIER_COLORS[9] + GTValues.VN[9]));
                break;
            case 6:
                lines.add(StatCollector.translateToLocal("tooltip.kekztech.lapotronic_energy_unit.capacity.none"));
                break;
            case 7:
                lines.add(
                    StatCollector.translateToLocalFormatted(
                        "tooltip.kekztech.lapotronic_energy_unit.capacity",
                        formatNumber(EV_cap_storage)));
                break;
            case 8:
                lines.add(
                    StatCollector.translateToLocalFormatted(
                        "tooltip.kekztech.lapotronic_energy_unit.capacity",
                        formatNumber(UEV_cap_storage)));
                lines.add(
                    StatCollector.translateToLocalFormatted(
                        "tooltip.kekztech.lapotronic_energy_unit.wire_less_transfer",
                        formatNumber(UEV_cap_eu_per_tick),
                        GTValues.TIER_COLORS[10] + GTValues.VN[10]));
                break;
            case 9:
                lines.add(
                    StatCollector.translateToLocalFormatted(
                        "tooltip.kekztech.lapotronic_energy_unit.capacity",
                        formatNumber(UIV_cap_storage)));
                lines.add(
                    StatCollector.translateToLocalFormatted(
                        "tooltip.kekztech.lapotronic_energy_unit.wire_less_transfer",
                        formatNumber(UIV_cap_eu_per_tick),
                        GTValues.TIER_COLORS[11] + GTValues.VN[11]));
                break;
            case 10:
                lines.add(
                    StatCollector.translateToLocalFormatted(
                        "tooltip.kekztech.lapotronic_energy_unit.capacity",
                        formatNumber(UMV_cap_storage)));
                lines.add(
                    StatCollector.translateToLocalFormatted(
                        "tooltip.kekztech.lapotronic_energy_unit.wire_less_transfer",
                        formatNumber(UMV_cap_eu_per_tick),
                        GTValues.TIER_COLORS[12] + GTValues.VN[12]));
                break;
        }
    }
}
