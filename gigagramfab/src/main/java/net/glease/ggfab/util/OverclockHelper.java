package net.glease.ggfab.util;

import gregtech.api.util.GT_Utility;

public class OverclockHelper {

    public static OverclockOutput normalOverclock(long recipeEUt, int duration, long inputVoltage, boolean perfectOC) {
        if (recipeEUt > inputVoltage) return null;
        int recipeTier = Math.max(1, GT_Utility.getTier(recipeEUt)); // ULV no overclock
        int machineTier = GT_Utility.getTier(inputVoltage);
        int shift = perfectOC ? 2 : 1;
        while (recipeTier < machineTier && duration > 1) {
            duration >>= shift;
            recipeEUt <<= 2;
            recipeTier++;
        }
        return new OverclockOutput(recipeEUt, duration);
    }

    public static OverclockOutput laserOverclock(long recipeEUt, int duration, long inputEUt,
            float penaltyIncreaseFactor) {
        if (recipeEUt > inputEUt) return null;
        float currentPenalty = 4 + penaltyIncreaseFactor;
        // 2/(n+k) overclock until energy hatch is crying
        // must ensure it doesn't go to negative after overclock
        while (recipeEUt * currentPenalty > 0 && recipeEUt * currentPenalty < inputEUt && duration > 1) {
            duration >>= 1;
            recipeEUt *= currentPenalty;
            currentPenalty += penaltyIncreaseFactor;
        }
        return new OverclockOutput(recipeEUt, duration);
    }

    public static final class OverclockOutput {

        private final long mEUt;
        private final int mDuration;

        public OverclockOutput(long aEUt, int aDuration) {
            this.mEUt = aEUt;
            this.mDuration = aDuration;
        }

        public long getEUt() {
            return mEUt;
        }

        public int getDuration() {
            return mDuration;
        }

        @Override
        public String toString() {
            return mEUt + "@" + mDuration + "ticks";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof OverclockOutput)) return false;

            OverclockOutput that = (OverclockOutput) o;

            if (mEUt != that.mEUt) return false;
            return mDuration == that.mDuration;
        }

        @Override
        public int hashCode() {
            int result = (int) (mEUt ^ (mEUt >>> 32));
            result = 31 * result + mDuration;
            return result;
        }
    }
}
