package gregtech.api.util;

import net.minecraft.util.StatCollector;

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

        if (!isAllowedToWork) return StatCollector.translateToLocal("GT5U.waila.machine.working_disabled");
        if (!isActive) return StatCollector.translateToLocal("GT5U.waila.machine.idle");

        return StatCollector.translateToLocalFormatted(
            "GT5U.waila.machine.in_progress",
            (double) progresstime / 20,
            (double) maxProgresstime / 20,
            (Math.round((double) progresstime / maxProgresstime * 1000) / 10.0));
    }
}
