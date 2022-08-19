package com.github.technus.tectech.thing.metaTileEntity.multi.base;

/**
 * Created by danie_000 on 23.12.2017.
 */
public class MultiblockControl<T> {
    private final boolean shouldExplode;
    private final T value;
    private final int eUt;
    private final int amperes;
    private final int requiredData;
    private final int effIncrease;
    private final int maxProgressTime;
    private final int pollutionToAdd;
    private final double excessMass;

    public MultiblockControl(
            T value,
            int eUt,
            int amperes,
            int requiredData,
            int effIncrease,
            int maxProgressTime,
            int pollutionToAdd,
            double excessMass,
            boolean shouldExplode) {
        this.value = value;
        this.eUt = eUt;
        this.amperes = amperes;
        this.requiredData = requiredData;
        this.effIncrease = effIncrease;
        this.maxProgressTime = maxProgressTime;
        this.pollutionToAdd = pollutionToAdd;
        this.excessMass = excessMass;
        this.shouldExplode = shouldExplode;
    }

    public MultiblockControl(
            T value,
            int eUt,
            int amperes,
            int requiredData,
            int effIncrease,
            int maxProgressTime,
            int pollutionToAdd,
            double excessMass) {
        this(value, eUt, amperes, requiredData, effIncrease, maxProgressTime, pollutionToAdd, excessMass, false);
    }

    public MultiblockControl(T value, int eUt, int amperes, int requiredData, int effIncrease, int maxProgressTime) {
        this(value, eUt, amperes, requiredData, effIncrease, maxProgressTime, 0, 0);
    }

    public MultiblockControl(double excessMass) {
        this(null, 0, 0, 0, 0, 0, 0, excessMass, true);
    }

    public T getValue() {
        return value;
    }

    public int getEUT() {
        return eUt;
    }

    public int getAmperage() {
        return amperes;
    }

    public int getRequiredData() {
        return requiredData;
    }

    public int getEffIncrease() {
        return effIncrease;
    }

    public int getMaxProgressTime() {
        return maxProgressTime;
    }

    public int getPollutionToAdd() {
        return pollutionToAdd;
    }

    public double getExcessMass() {
        return excessMass;
    }

    public boolean shouldExplode() {
        return shouldExplode;
    }
}
