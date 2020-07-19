package com.github.technus.tectech.mechanics.elementalMatter.core;

public class cElementalDecayResult {
    private final cElementalInstanceStackMap output;
    private double massAffected;
    private double massDiff;

    public cElementalDecayResult(cElementalInstanceStackMap output, double massAffected, double massDiff) {
        this.output = output;
        this.massAffected = massAffected;
        this.massDiff = massDiff;
    }

    public cElementalInstanceStackMap getOutput() {
        return output;
    }

    /**
     * How much was lost in the process (decayed or removed)
     * @return
     */
    public double getMassAffected() {
        return massAffected;
    }

    /**
     * Difference of mass of actually decayed elements
     * @return
     */
    public double getMassDiff() {
        return massDiff;
    }

    public void setMassAffected(double massAffected) {
        this.massAffected = massAffected;
    }

    public void setMassDiff(double massDiff) {
        this.massDiff = massDiff;
    }
}
