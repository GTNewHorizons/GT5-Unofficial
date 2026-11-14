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

    String TOOLTIP_VCI_LONG = translateToLocal("GT5U.tooltip.nac.interface.vci_long");
    String TOOLTIP_VCI = translateToLocal("GT5U.tooltip.nac.interface.vci");
    String TOOLTIP_VCIs = translateToLocal("GT5U.tooltip.nac.interface.vcis");

    String TOOLTIP_VCO_LONG = translateToLocal("GT5U.tooltip.nac.interface.vco_long");
    String TOOLTIP_VCO = translateToLocal("GT5U.tooltip.nac.interface.vco");
    String TOOLTIP_VCOs = translateToLocal("GT5U.tooltip.nac.interface.vcos");

    String TOOLTIP_COLOR = translateToLocal("GT5U.tooltip.nac.interface.color");

    String TOOLTIP_COLORS = translateToLocal("GT5U.tooltip.nac.interface.colors");

    String TOOLTIP_COLORED = translateToLocal("GT5U.tooltip.nac.interface.colored");

    String TOOLTIP_MODULE_DESCRIPTION = translateToLocal("GT5U.tooltip.nac.interface.module_description");

    String TOOLTIP_STRUCTURE_CONTROL_ROOM_BASE_CASING = translateToLocal(
        "GT5U.tooltip.nac.interface.structure_control_room_base_casing");
    String TOOLTIP_STRUCTURE_BASE_CASING = translateToLocal("GT5U.tooltip.nac.interface.structure_base_casing");
    String TOOLTIP_STRUCTURE_BASE_VCI = translateToLocalFormatted(
        "GT5U.tooltip.nac.interface.structure_base_vci",
        TOOLTIP_STRUCTURE_BASE_CASING);
    String TOOLTIP_STRUCTURE_BASE_VCO = translateToLocalFormatted(
        "GT5U.tooltip.nac.interface.structure_base_vco",
        TOOLTIP_STRUCTURE_BASE_CASING);
    String VC_HATCHES = translateToLocal("GT5U.tooltip.nac.interface.vc_hatches");

}
