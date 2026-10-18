package gregtech.api.util.tooltip;

import java.text.DecimalFormat;
import java.util.Arrays;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil;

import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.util.GTUtility;

/**
 * A list of helper methods that can be useful in creating tooltips.
 * Meant to simplify creating non-standard, repeated text in tooltips.
 * For use with {@link gregtech.api.util.MultiblockTooltipBuilder}
 */
public class TooltipHelper {

    public static final EnumChatFormatting DEFAULT = EnumChatFormatting.DARK_GRAY;
    /** For tier and tiered parts names. */
    public static final EnumChatFormatting TIER_COLOR = EnumChatFormatting.WHITE;
    /** Major components that require highlighting. */
    public static final EnumChatFormatting NAME_COLOR = EnumChatFormatting.WHITE;
    /** For warns and other important notes. */
    public static final EnumChatFormatting NOTE_COLOR = EnumChatFormatting.YELLOW;
    /** For miscellaneous component names or anything else that needs highlighting. */
    public static final EnumChatFormatting MISC_COLOR = EnumChatFormatting.BLUE;
    /** For speed modifiers. */
    public static final EnumChatFormatting SPEED_COLOR = EnumChatFormatting.GREEN;
    /** For range modifiers. */
    public static final EnumChatFormatting RANGE_COLOR = EnumChatFormatting.GREEN;
    /** For hatch usage notes. */
    public static final EnumChatFormatting HATCH_COLOR = EnumChatFormatting.GREEN;
    /** For parallels counts and modifiers. */
    public static final EnumChatFormatting PARALLEL_COLOR = EnumChatFormatting.GOLD;
    /** For machine mode names. */
    public static final EnumChatFormatting MODE_COLOR = EnumChatFormatting.GOLD;
    /** For things that are the key components of something. */
    public static final EnumChatFormatting KEY_COLOR = EnumChatFormatting.GOLD;
    /** For efficiency modifiers. */
    public static final EnumChatFormatting EFF_COLOR = EnumChatFormatting.AQUA;
    /** For power usage (eg: x EU/t). */
    public static final EnumChatFormatting EU_USAGE_COLOR = EnumChatFormatting.AQUA;
    /** For heat values. */
    public static final EnumChatFormatting HEAT_COLOR = EnumChatFormatting.RED;
    /** For depreciation notices. */
    public static final EnumChatFormatting DEPRECATED_COLOR = EnumChatFormatting.RED;
    /** For dangerous component descriptions. */
    public static final EnumChatFormatting DANGER_COLOR = EnumChatFormatting.DARK_RED;
    /** For mechanic descriptions. */
    public static final EnumChatFormatting MECHANIC_COLOR = EnumChatFormatting.LIGHT_PURPLE;
    /** For pollution values. */
    public static final EnumChatFormatting POLLUTION_COLOR = EnumChatFormatting.DARK_PURPLE;
    public static final EnumChatFormatting ITALIC = EnumChatFormatting.ITALIC;
    public static final EnumChatFormatting EU_VOLT_COLOR = EnumChatFormatting.YELLOW;
    /** For amperage measurements. */
    public static final EnumChatFormatting AMP_COLOR = EnumChatFormatting.AQUA;
    public static final EnumChatFormatting CAPACITY_COLOR = EnumChatFormatting.BLUE;
    public static final EnumChatFormatting EU_AMOUNT_COLOR = EnumChatFormatting.GRAY;
    public static final EnumChatFormatting CABLE_LOSS_COLOR = EnumChatFormatting.RED;
    public static final EnumChatFormatting L_COLOR = EnumChatFormatting.WHITE;
    public static final DecimalFormat percentageFormat = new DecimalFormat("0.##%");

    /**
     * Get the "Any casing" text for structure tooltips.
     */
    public static String anyCasingText() {
        // Translated on every call so it follows an in-game language change
        return StatCollector.translateToLocal("gt.mbtt.structure.any_casing");
    }

    /**
     * Wraps input string in parallel colors
     *
     * @param parallels Amount of parallels turned into {@link TooltipHelper#PARALLEL_COLOR}
     * @return Colored String
     */
    public static String parallelText(String parallels) {
        return coloredText(parallels, PARALLEL_COLOR);
    }

    /**
     * Wraps input string in parallel colors
     *
     * @param parallels Amount of parallels turned into {@link TooltipHelper#PARALLEL_COLOR}
     * @return Colored String
     */
    public static String parallelText(int parallels) {
        return coloredText(Integer.toString(parallels), parallels == 1 ? DEFAULT : PARALLEL_COLOR);
    }

    /**
     * Wraps input string in Speed colors
     *
     * @param speed Speed difference. {@link TooltipHelper#SPEED_COLOR}
     * @return Colored String
     */
    public static String speedText(String speed) {
        return coloredText(speed, SPEED_COLOR);
    }

    /**
     * Wraps input float in Speed colors, same as ProcessingLogic float
     *
     * @param speed Speed difference. {@link TooltipHelper#SPEED_COLOR}
     * @return Colored String
     */
    public static String speedText(float speed) {
        return coloredText(percentageFormat.format(speed), speed == 1f ? DEFAULT : SPEED_COLOR);
    }

    /**
     * Wraps input string in EFF colors
     *
     * @param eff Efficiency. {@link TooltipHelper#EFF_COLOR}
     * @return Colored String
     */
    public static String effText(String eff) {
        return coloredText(eff, EFF_COLOR);
    }

    /**
     * Wraps input string in EFF colors
     * Will have Dark Gray if value is unchanged standard
     *
     * @param eff Efficiency. {@link TooltipHelper#EFF_COLOR}
     * @return Colored String
     */
    public static String effText(float eff) {
        return coloredText(percentageFormat.format(eff), eff == 1f ? DEFAULT : EFF_COLOR);
    }

    /**
     * Wraps input string in Tier colors, for niche cases only.
     *
     * @see TooltipTier#getValue() for a more standardized way,
     *
     * @param tier Tier . {@link TooltipHelper#TIER_COLOR}
     * @return Colored String
     */
    public static String tierText(String tier) {
        return coloredText(tier, TIER_COLOR);
    }

    public static String tierText(TooltipTier tier) {
        return coloredText(tier.getValue(), TIER_COLOR);
    }

    /**
     * Wraps input string in given color, if color is valid.
     *
     * @param text     Text to be colored
     * @param coloring Color to apply to text
     * @return Colored String, resets to gray coloring after
     */
    public static String coloredText(String text, EnumChatFormatting coloring) {
        if (coloring.isColor() || coloring == EnumChatFormatting.GRAY)
            return String.format("%s%s%s", coloring, text, EnumChatFormatting.GRAY);
        return text;
    }

    /**
     * Wraps input string in italic formatting
     *
     * @param text Text to be italicized
     * @return Italic String
     */
    public static String italicText(String text) {
        return String.format("%s%s%s", ITALIC, text, EnumChatFormatting.RESET);
    }

    /**
     * Returns a voltage tier string with appropriate color formatting.
     * <br>
     * If the voltage index is out of range, it returns "Invalid Voltage Tier".
     *
     * @param voltageIndex The voltage index, as defined in {@link VoltageIndex}.
     * @return A formatted string representing the voltage tier.
     */
    public static String voltageTierText(int voltageIndex) {
        return voltageTierText(voltageIndex, true);
    }

    /**
     * Returns a voltage tier string with appropriate color formatting.
     * <br>
     * If the voltage index is out of range, it returns "Invalid Voltage Tier".
     *
     * @param voltageIndex   The voltage index, as defined in {@link VoltageIndex}.
     * @param withTierSuffix If true, returns the string like "UHV-tier". If false, returns like "UHV"
     * @return A formatted string representing the voltage tier.
     */
    public static String voltageTierText(int voltageIndex, boolean withTierSuffix) {
        if (voltageIndex < VoltageIndex.ULV || voltageIndex > VoltageIndex.MAX) return "Invalid Voltage Tier";
        return GTValues.TIER_COLORS[voltageIndex] + GTValues.VN[voltageIndex]
            + EnumChatFormatting.GRAY
            + (withTierSuffix ? StatCollector.translateToLocal("gt.voltage.tier") : "");
    }

    /**
     * @return The given EU, formatted.
     */
    public static String euText(long eu) {
        return EU_VOLT_COLOR + NumberFormatUtil.formatNumber(eu) + EnumChatFormatting.GRAY;
    }

    /**
     * @return The given EU/t, formatted in the form "[Number] EU/t".
     */
    public static String euRateText(long eu) {
        return EU_VOLT_COLOR + NumberFormatUtil.formatNumber(eu) + TIER_COLOR + " EU/t" + EnumChatFormatting.GRAY;
    }

    /**
     * @return The given number of EU, formatted.
     */
    public static String euCapacityText(long capacity) {
        return CAPACITY_COLOR + NumberFormatUtil.formatNumber(capacity) + EU_AMOUNT_COLOR + " EU";
    }

    /**
     * @return A string of the form "[voltage] ([voltage tier])"
     */
    public static String voltageText(long voltage) {
        final byte tier = GTUtility.getTier(voltage);
        return EU_VOLT_COLOR + NumberFormatUtil
            .formatNumber(voltage) + EnumChatFormatting.GRAY + " (" + voltageTierText(tier, false) + ")";
    }

    /**
     * @return The given number of amps, formatted.
     */
    public static String ampText(long amps) {
        return AMP_COLOR + NumberFormatUtil.formatNumber(amps) + EnumChatFormatting.GRAY;
    }

    /**
     * @return A string of the form "[lossPerMeter] EU-Volt"
     */
    public static String cableLossText(long lossPerMeter) {
        return StatCollector.translateToLocalFormatted(
            "GT5U.item.cable.loss.eu_volt",
            CABLE_LOSS_COLOR + NumberFormatUtil.formatNumber(lossPerMeter) + EnumChatFormatting.GRAY);
    }

    /**
     * @return The given number of L, formatted.
     */
    public static String fluidText(long liters) {
        String text = NumberFormatUtil.formatNumber(liters) + StatCollector.translateToLocal("gt.unit.liter");
        return coloredText(text, L_COLOR);
    }

    /**
     * @return The given number of L per sec, formatted.
     */
    public static String fluidRateText(long litersPerSecond) {
        String text = NumberFormatUtil.formatNumber(litersPerSecond)
            + StatCollector.translateToLocal("gt.unit.liter_per_second");
        return coloredText(text, L_COLOR);
    }

    /**
     * @return The tooltip as it was, but with a warning placed first if pollution is toggled off in config
     */
    public static String[] pollutionDisabledTooltip(String[] description) {
        if (GTMod.proxy.mPollution) {
            return description;
        }
        String[] descriptionWithWarning = Arrays.copyOf(description, description.length + 1);
        System.arraycopy(descriptionWithWarning, 0, descriptionWithWarning, 1, description.length);
        descriptionWithWarning[0] = EnumChatFormatting.RED + "Pollution is OFF - This Block is Redundant";
        return descriptionWithWarning;
    }
}
