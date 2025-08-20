package gregtech.api.util.tooltip;

import net.minecraft.util.EnumChatFormatting;

/**
 * A list of helper methods that can be useful in creating tooltips.
 * Meant to simplify creating non-standard, repeated text in tooltips.
 * For use with {@link gregtech.api.util.MultiblockTooltipBuilder}
 */
public class TooltipHelper {

    public static final EnumChatFormatting PARALLEL_COLOR = EnumChatFormatting.GOLD;
    public static final EnumChatFormatting SPEED_COLOR = EnumChatFormatting.AQUA;
    public static final EnumChatFormatting EFF_COLOR = EnumChatFormatting.RED;
    public static final EnumChatFormatting TIER_COLOR = EnumChatFormatting.WHITE;

    /**
     * Wraps input string in parallel colors
     *
     * @param parallels Amount of parallels turned into  {@link TooltipHelper#PARALLEL_COLOR}
     * @return Colored String
     */
    public static String parallelText(String parallels)
    {
        return coloredText(parallels,PARALLEL_COLOR);
    }
    /**
     * Wraps input string in Speed colors
     *
     * @param speed Speed difference. {@link TooltipHelper#SPEED_COLOR}
     * @return Colored String
     */
    public static String speedText(String speed)
    {
        return coloredText(speed,SPEED_COLOR);
    }
    /**
     * Wraps input string in EFF colors
     *
     * @param eff Speed difference. {@link TooltipHelper#EFF_COLOR}
     * @return Colored String
     */
    public static String effText(String eff)
    {
        return coloredText(eff, EFF_COLOR);
    }

    /**
     * Wraps input string in Tier colors, for niche cases only.
     *
     * @see TooltipTier#getValue() for a more standardized way, this is for niche cases.
     *
     * @param tier Tier . {@link TooltipHelper#TIER_COLOR}
     * @return Colored String
     */
    public static String tierText(String tier)
    {
        return coloredText(tier,TIER_COLOR);
    }

    /**
     * Wraps input string in given color, if color is valid.
     *
     * @param text Text to be colored
     * @param coloring Color to apply to text
     * @return Colored String, resets to gray coloring after
     */
    public static String coloredText(String text, EnumChatFormatting coloring)
    {
        if (coloring.isColor()) return String.format("%s%s%s",coloring, text ,EnumChatFormatting.GRAY);
        return text;
    }
}
