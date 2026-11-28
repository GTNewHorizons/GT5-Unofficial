package gregtech.common.hydrodam;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatFluid;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

/// Formats the dam lines shown in WAILA, so that machines and dam water show them the same way.
public final class DamWailaLines {

    private DamWailaLines() {}

    @NotNull
    public static String reserve(double litres) {
        return StatCollector.translateToLocalFormatted("GT5U.waila.hydrodam.reserve", formatFluid((long) litres));
    }

    @NotNull
    public static String netChange(double litresPerTick) {
        return StatCollector
            .translateToLocalFormatted("GT5U.waila.hydrodam.net_change", formatNetChange(litresPerTick));
    }

    /// Formats a net change with one decimal and a sign, green when filling and red when draining.
    private static String formatNetChange(double litresPerTick) {
        double rounded = Math.round(litresPerTick * 10) / 10.0;

        if (rounded > 0) return EnumChatFormatting.GREEN + "+" + formatFluid(rounded) + EnumChatFormatting.RESET;
        if (rounded < 0) return EnumChatFormatting.RED + formatFluid(rounded) + EnumChatFormatting.RESET;

        return formatFluid(0);
    }
}
