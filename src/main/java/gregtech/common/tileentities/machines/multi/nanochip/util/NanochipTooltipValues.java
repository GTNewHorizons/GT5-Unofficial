package gregtech.common.tileentities.machines.multi.nanochip.util;

import net.minecraft.util.EnumChatFormatting;

public interface NanochipTooltipValues {

    default String tooltipFlavorText(String text) {
        return EnumChatFormatting.LIGHT_PURPLE + "" + EnumChatFormatting.ITALIC + text + EnumChatFormatting.GRAY;
    }

    default String machineInfoText(String machineType) {
        return machineType + ", " + NAC_MODULE;
    }

    public static final String NAC_MODULE = "NAC Module";
    public static final String TOOLTIP_CC = EnumChatFormatting.YELLOW + "CC" + EnumChatFormatting.GRAY;
    public static final String TOOLTIP_CCs = EnumChatFormatting.YELLOW + "CCs" + EnumChatFormatting.GRAY;

    public static final String TOOLTIP_VCI = EnumChatFormatting.YELLOW + "VCI" + EnumChatFormatting.GRAY;
    public static final String TOOLTIP_VCIs = EnumChatFormatting.YELLOW + "VCIs" + EnumChatFormatting.GRAY;

    public static final String TOOLTIP_VCO = EnumChatFormatting.YELLOW + "VCO" + EnumChatFormatting.GRAY;
    public static final String TOOLTIP_VCOs = EnumChatFormatting.YELLOW + "VCOs" + EnumChatFormatting.GRAY;

    public static final String TOOLTIP_COLOR = EnumChatFormatting.RED + "c"
        + EnumChatFormatting.YELLOW
        + "o"
        + EnumChatFormatting.GREEN
        + "l"
        + EnumChatFormatting.AQUA
        + "o"
        + EnumChatFormatting.LIGHT_PURPLE
        + "r"
        + EnumChatFormatting.GRAY;

    public static final String TOOLTIP_COLORS = EnumChatFormatting.RED + "c"
        + EnumChatFormatting.YELLOW
        + "o"
        + EnumChatFormatting.GREEN
        + "l"
        + EnumChatFormatting.AQUA
        + "o"
        + EnumChatFormatting.LIGHT_PURPLE
        + "r"
        + EnumChatFormatting.DARK_PURPLE
        + "s"
        + EnumChatFormatting.GRAY;

    public static final String TOOLTIP_COLORED = EnumChatFormatting.RED + "c"
        + EnumChatFormatting.YELLOW
        + "o"
        + EnumChatFormatting.GREEN
        + "l"
        + EnumChatFormatting.AQUA
        + "o"
        + EnumChatFormatting.DARK_AQUA
        + "r"
        + EnumChatFormatting.DARK_PURPLE
        + "e"
        + EnumChatFormatting.LIGHT_PURPLE
        + "d"
        + EnumChatFormatting.GRAY;

    public static final String TOOLTIP_COLOR_MATCH_VCS = "Outputs are placed in the " + TOOLTIP_VCO
        + " with the same "
        + TOOLTIP_COLOR
        + " as the input "
        + TOOLTIP_VCI;

    public static final String TOOLTIP_MODULE_DESCRIPTION = "Module of the " + EnumChatFormatting.GREEN
        + "Nanochip Assembly Complex"
        + EnumChatFormatting.GRAY;

    public static final String TOOLTIP_INFINITE_PARALLEL = "Has " + EnumChatFormatting.GOLD
        + "infinite"
        + EnumChatFormatting.GRAY
        + " parallels!";

    public static final String TOOLTIP_STRUCTURE_BASE_VCI = "Any base casing - Vacuum Conveyor Input";
    public static final String TOOLTIP_STRUCTURE_BASE_VCO = "Any base casing - Vacuum Conveyor Output";

}
