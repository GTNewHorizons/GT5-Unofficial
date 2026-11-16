package gregtech.api.util;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import appeng.util.ReadableNumberConverter;
import gregtech.common.config.Client;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderBar;

public abstract class GTWaila {

    /**
     * One line for a stack listed in a WAILA tooltip, e.g. {@code Tin Ingot: §61.5k§r}. Tiles that list their stored or
     * cached contents all render through this, so their tooltips read the same.
     */
    public static String getStackListLine(String name, long amount) {
        return name + ": "
            + EnumChatFormatting.GOLD
            + ReadableNumberConverter.INSTANCE.toWideReadableForm(amount)
            + EnumChatFormatting.RESET;
    }

    public static String getMachineProgressString(int maxProgressTime, int progressTime) {
        return getMachineProgressString(true, true, maxProgressTime, progressTime);
    }

    public static String getMachineProgressString(boolean isActive, int maxProgressTime, int progressTime) {
        return getMachineProgressString(isActive, true, maxProgressTime, (long) progressTime);
    }

    public static String getMachineProgressString(boolean isActive, boolean isAllowedToWork, int maxProgressTime,
        int progressTime) {
        return getMachineProgressString(isActive, isAllowedToWork, maxProgressTime, (long) progressTime);
    }

    public static String getMachineProgressString(boolean isActive, long maxProgressTime, long progressTime) {
        return getMachineProgressString(isActive, true, maxProgressTime, progressTime);
    }

    public static String getMachineProgressString(boolean isActive, boolean isAllowedToWork, long maxProgressTime,
        long progressTime) {

        if (Client.waila.showWorkingDisable) {
            if (!isAllowedToWork && !isActive) {
                return StatCollector.translateToLocal("GT5U.waila.machine.working_disabled");
            }
        }

        if (Client.waila.showIdle) {
            if (!isActive) {
                return StatCollector.translateToLocal("GT5U.waila.machine.idle");
            }
        }

        String progressText;

        if (maxProgressTime < 20) {
            progressText = StatCollector
                .translateToLocalFormatted("GT5U.waila.machine.progress_tick", progressTime, maxProgressTime);
        } else {
            String currentSeconds = String.format("%.2f", progressTime / 20.0);
            String maxSeconds = String.format("%.2f", maxProgressTime / 20.0);
            progressText = StatCollector
                .translateToLocalFormatted("GT5U.waila.machine.progress_second", currentSeconds, maxSeconds);
        }

        return TTRenderBar.create(
            progressText,
            ColorUtils.progressBarTop.getColor(),
            ColorUtils.progressBarBottom.getColor(),
            (double) progressTime / maxProgressTime);
    }
}
