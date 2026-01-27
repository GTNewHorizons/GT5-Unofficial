package gregtech.common.tileentities.machines.multi.nanochip.util;

import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import net.minecraft.util.EnumChatFormatting;

public interface NanochipTooltipValues {

    default String tooltipFlavorText(String text) {
        return EnumChatFormatting.LIGHT_PURPLE + "" + EnumChatFormatting.ITALIC + text + EnumChatFormatting.GRAY;
    }

    default String machineInfoText(String machineType) {
        return translateToLocalFormatted("GT5U.tooltip.nac.interface.machine_info", machineType, NAC_MODULE);
    }

    String NAC_MODULE = translateToLocal("GT5U.tooltip.nac.interface.nac_module");
    String TOOLTIP_CC = translateToLocal("GT5U.tooltip.nac.interface.cc");
    String TOOLTIP_CCs = translateToLocal("GT5U.tooltip.nac.interface.ccs");

    String TOOLTIP_VCI = translateToLocal("GT5U.tooltip.nac.interface.vci");
    String TOOLTIP_VCIs = translateToLocal("GT5U.tooltip.nac.interface.vcis");

    String TOOLTIP_VCO = translateToLocal("GT5U.tooltip.nac.interface.vco");
    String TOOLTIP_VCOs = translateToLocal("GT5U.tooltip.nac.interface.vcos");

    String TOOLTIP_COLOR = translateToLocal("GT5U.tooltip.nac.interface.color");

    String TOOLTIP_COLORS = translateToLocal("GT5U.tooltip.nac.interface.colors");

    String TOOLTIP_COLORED = translateToLocal("GT5U.tooltip.nac.interface.colored");

    String TOOLTIP_COLOR_MATCH_VCS = translateToLocalFormatted(
        "GT5U.tooltip.nac.interface.color_match_vcs",
        TOOLTIP_VCO,
        TOOLTIP_COLOR,
        TOOLTIP_VCI);

    String TOOLTIP_MODULE_DESCRIPTION = translateToLocal("GT5U.tooltip.nac.interface.module_description");

    String TOOLTIP_INFINITE_PARALLEL = translateToLocal("GT5U.tooltip.nac.interface.infinite_parallel");

    String TOOLTIP_STRUCTURE_BASE_VCI = translateToLocal("GT5U.tooltip.nac.interface.structure_base_vci");
    String TOOLTIP_STRUCTURE_BASE_VCO = translateToLocal("GT5U.tooltip.nac.interface.structure_base_vco");

}
