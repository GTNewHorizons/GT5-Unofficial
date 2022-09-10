package gregtech.api.util;

import joptsimple.internal.Strings;

public abstract class GT_Waila {

    public static String getMachineProgressString(boolean isActive, int maxProgress, int progress) {
        StringBuilder stringBuilder = new StringBuilder(Strings.EMPTY);

        if (isActive) {
            if (maxProgress > 20) {
                stringBuilder.append(String.format("Progress: %d s / %d s", progress / 20, maxProgress / 20));
            } else {
                stringBuilder.append(String.format("Progress: %d ticks remaining", maxProgress));
            }
        } else {
            stringBuilder.append("Idle");
        }

        return stringBuilder.toString();
    }
}
