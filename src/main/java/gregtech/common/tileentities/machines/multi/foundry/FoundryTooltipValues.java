package gregtech.common.tileentities.machines.multi.foundry;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import gregtech.api.util.GTUtility;

public class FoundryTooltipValues {

    public static String coolingStrOrder(String val1, String val2) {
        return EnumChatFormatting.BLUE + val1
            + EnumChatFormatting.GRAY
            + "/"
            + EnumChatFormatting.LIGHT_PURPLE
            + val2
            + EnumChatFormatting.GRAY;
    }

    public static String createModuleBaseText() {
        return EnumChatFormatting.GOLD
            + StatCollector.translateToLocal("gt.blockmachines.multimachine.foundry.basemodule");
    }

    public static String createModuleLimitText() {
        return StatCollector.translateToLocal("gt.blockmachines.multimachine.foundry.modulelimit");
    }

    public static String createFoundryFlavorText(String key) {
        return EnumChatFormatting.RED + StatCollector.translateToLocal(key);
    }

    public static String createTierLine(int tier) {
        return StatCollector.translateToLocalFormatted(
            "gt.blockmachines.multimachine.foundry.tier",
            GTUtility.getColoredTierNameFromTier((byte) tier));
    }
}
