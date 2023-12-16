package gregtech.api.util;

public abstract class GT_Waila {

    public static String getMachineProgressString(boolean mIsActive, int mMaxProgresstime, int mProgresstime) {
        return getMachineProgressString(mIsActive, (long) mMaxProgresstime, (long) mProgresstime);
    }

    public static String getMachineProgressString(boolean mIsActive, long mMaxProgresstime, long mProgresstime) {

        if (!mIsActive) return "Idle";

        StringBuilder ret = new StringBuilder("In progress: ")
            .append(String.format("%,.2f", (double) mProgresstime / 20))
            .append("s / ")
            .append(String.format("%,.2f", (double) mMaxProgresstime / 20))
            .append("s (")
            .append(GT_Utility.formatNumbers((Math.round((double) mProgresstime / mMaxProgresstime * 1000) / 10.0)))
            .append("%)");

        return ret.toString();
    }
}
