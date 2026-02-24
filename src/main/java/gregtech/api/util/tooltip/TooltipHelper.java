package gregtech.api.util.tooltip;

import java.text.DecimalFormat;

import net.minecraft.util.EnumChatFormatting;

import com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil;

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
    public static final EnumChatFormatting PARALLEL_COLOR = EnumChatFormatting.GOLD;
    public static final EnumChatFormatting SPEED_COLOR = EnumChatFormatting.GREEN;
    public static final EnumChatFormatting EFF_COLOR = EnumChatFormatting.AQUA;
    public static final EnumChatFormatting TIER_COLOR = EnumChatFormatting.WHITE;
    public static final EnumChatFormatting ITALIC = EnumChatFormatting.ITALIC;
    public static final EnumChatFormatting EU_VOLT_COLOR = EnumChatFormatting.YELLOW;
    public static final EnumChatFormatting AMP_COLOR = EnumChatFormatting.AQUA;
    public static final EnumChatFormatting CABLE_LOSS_COLOR = EnumChatFormatting.RED;
    public static final EnumChatFormatting L_COLOR = EnumChatFormatting.WHITE;
    public static final DecimalFormat percentageFormat = new DecimalFormat("0.##%");

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
        return CABLE_LOSS_COLOR + NumberFormatUtil.formatNumber(lossPerMeter)
            + " "
            + EnumChatFormatting.GRAY
            + GTUtility.translate("GT5U.item.cable.eu_volt");
    }

    /**
     * @return The given number of L, formatted.
     */
    public static String fluidText(long liters) {
        return L_COLOR + NumberFormatUtil.formatNumber(liters)
            + GTUtility.translate("gt.mbtt.info.l")
            + EnumChatFormatting.GRAY;
    }

    /**
     * @return The given number of L per sec, formatted.
     */
    public static String fluidRateText(long litersPerSecond) {
        return L_COLOR + NumberFormatUtil.formatNumber(litersPerSecond)
            + GTUtility.translate("gt.mbtt.info.l_s")
            + EnumChatFormatting.GRAY;
    }
}
