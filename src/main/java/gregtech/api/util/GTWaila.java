package gregtech.api.util;

public abstract class GTWaila {

    public static String getMachineProgressString(boolean isActive, int maxProgresstime, int progresstime) {
        return getMachineProgressString(isActive, maxProgresstime, (long) progresstime);
    }

    public static String getMachineProgressString(boolean isActive, long maxProgresstime, long progresstime) {

        if (!isActive) return "Idle";

        String ret = "In progress: " +
            String.format("%,.2f", (double) progresstime / 20) +
            "s / " +
            String.format("%,.2f", (double) maxProgresstime / 20) +
            "s (" +
            GTUtility.formatNumbers((Math.round((double) progresstime / maxProgresstime * 1000) / 10.0)) +
            "%)";

        return ret;
    }
}
