package gregtech.api.util;

public abstract class GT_Waila {

    public static String getMachineProgressString(boolean isActive, int maxProgress, int progress) {
        return getMachineProgressString(isActive, (long) maxProgress, (long) progress);
    }

    public static String getMachineProgressString(boolean isActive, long maxProgress, long progress) {
        final String result;

        if (isActive) {
            if (maxProgress < 20) {
                result = String.format("Progress: %d ticks remaining", maxProgress);
            } else {
                result = String.format("Progress: %d s / %d s", progress / 20, maxProgress / 20);
            }
        } else {
            result = "Idle";
        }

        return result;
    }
}
