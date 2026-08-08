package gregtech.api.util;

import net.minecraft.util.StatCollector;

import gregtech.common.config.Client;
import mcp.mobius.waila.api.SpecialChars;

public abstract class GTWaila {

    public static String getMachineProgressString(boolean isActive, int maxProgresstime, int progresstime) {
        return getMachineProgressString(isActive, true, maxProgresstime, (long) progresstime);
    }

    public static String getMachineProgressString(boolean isActive, boolean isAllowedToWork, int maxProgresstime,
        int progresstime) {
        return getMachineProgressString(isActive, isAllowedToWork, maxProgresstime, (long) progresstime);
    }

    public static String getMachineProgressString(boolean isActive, long maxProgresstime, long progresstime) {
        return getMachineProgressString(isActive, true, maxProgresstime, progresstime);
    }

    public static String getMachineProgressString(boolean isActive, boolean isAllowedToWork, long maxProgresstime,
        long progresstime) {

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

        return SpecialChars
            .getRenderString("waila.gt.progress", progresstime + "", maxProgresstime + "", isAllowedToWork + "");
    }
}
