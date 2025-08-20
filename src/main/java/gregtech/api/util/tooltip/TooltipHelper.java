package gregtech.api.util.tooltip;

import java.text.DecimalFormat;

import net.minecraft.util.EnumChatFormatting;

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
}
