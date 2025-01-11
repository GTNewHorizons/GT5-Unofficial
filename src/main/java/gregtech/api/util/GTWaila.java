package gregtech.api.util;

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

        if (!isAllowedToWork) return "Working Disabled";
        if (!isActive) return "Idle";

        return "In progress: " + String.format("%,.1f", (double) progresstime / 20)
            + "s / "
            + String.format("%,.1f", (double) maxProgresstime / 20)
            + "s ("
            + String.format("%,.1f", (Math.round((double) progresstime / maxProgresstime * 1000) / 10.0))
            + "%)";
    }
}
