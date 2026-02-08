package gregtech.common.tileentities.machines.multi.foundry;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

public class FoundryData {

    public FoundryModule[] modules = { FoundryModule.UNSET, FoundryModule.UNSET, FoundryModule.UNSET,
        FoundryModule.UNSET };
    public int tier = 0; // 1 - UEV , 2 - ~UIV, 3 - ~UXV

    public final float speedModifierBase = 1.5F;
    public final float euEffBase = 1.0F;
    public final int parallelScaleBase = 16;
    public final float ocFactorBase = 2.0F;

    public float ocFactorAdditive = 0.0F;
    public float speedAdditive = 0.0F;
    public float speedMultiplier = 1.0F;
    public float speedModifierAdj = speedModifierBase;

    public float euEffAdditive = 0.0F;
    public float euEffMultiplier = 1.0F;
    public float euEffAdj = euEffBase;

    public int parallelScaleAdditive = 0;
    public float parallelScaleMultiplier = 1.0F;
    public float parallelScaleAdj = parallelScaleBase;

    public int extraOverclocks = 0;

    public boolean UIVRecipesEnabled = false;
    public boolean hypercoolerPresent = false;
    public boolean tdsPresent = false;
    public boolean effOCPresent = false;
    public boolean allowEternity = false;

    public boolean isEfficiencyPairPresent = false;
    public boolean isHRPairPresent = false;
    public boolean isProductionPairPresent = false;
    public boolean isEndPairPresent = false;

    public void resetParameters() {
        ocFactorAdditive = 0.0F;

        speedAdditive = 0.0F;
        speedMultiplier = 1.0F;

        euEffAdditive = 0.0F;
        euEffMultiplier = 1.0F;

        parallelScaleAdditive = 0;
        parallelScaleMultiplier = 1.0F;

        hypercoolerPresent = false;
        UIVRecipesEnabled = false;
        tdsPresent = false;
        effOCPresent = false;
        allowEternity = false;
        extraOverclocks = 0;

        isEfficiencyPairPresent = false;
        isHRPairPresent = false;
        isProductionPairPresent = false;
        isEndPairPresent = false;
    }

    // base stats per module
    public void checkSolidifierModules() {
        resetParameters();
        // loop through each module. based on tier. 2 - 4 modules.

        int numValidModules = 2 + tier - 1;
        for (int i = 0; i < numValidModules; i++) {
            FoundryModule checkedModule = modules[i];
            checkedModule.statFunction.accept(this);
        }

        FoundryModule[] testModules = Arrays.copyOfRange(modules, 0, numValidModules);
        calculatePairings(testModules);
        calculateNewStats();
    }

    // pair stat buffs for having 2 specific modules together
    private void calculatePairings(FoundryModule[] modules) {
        if (ArrayUtils.contains(modules, FoundryModule.STREAMLINED_CASTERS)
            && ArrayUtils.contains(modules, FoundryModule.EXTRA_CASTING_BASINS)) {
            speedAdditive += 0.75F;
            parallelScaleAdditive += 6;
            isProductionPairPresent = true;
        }

        if (ArrayUtils.contains(modules, FoundryModule.POWER_EFFICIENT_SUBSYSTEMS)
            && ArrayUtils.contains(modules, FoundryModule.EFFICIENT_OC)) {
            ocFactorAdditive += 0.1F;
            euEffAdditive -= 0.5F;
            isEfficiencyPairPresent = true;
        }

        if (ArrayUtils.contains(modules, FoundryModule.HYPERCOOLER)
            && ArrayUtils.contains(modules, FoundryModule.UNIVERSAL_COLLAPSER)) {
            euEffMultiplier *= 2;
            speedMultiplier *= 2;
            allowEternity = true;
            isEndPairPresent = true;
        }

        int numHelio = (int) Arrays.stream(modules)
            .filter(m -> m == FoundryModule.HELIOCAST_REINFORCEMENT)
            .count();
        if (numHelio > 1) {
            isHRPairPresent = true;
            speedAdditive += (0.75F * numHelio);
            euEffAdditive -= (0.1F * numHelio);
            if (numHelio >= 3) {
                ocFactorAdditive += 0.1F;
                parallelScaleAdditive += (6 * numHelio);
            }
            if (numHelio == 4) {
                extraOverclocks += 2;
            }
        }
    }

    public FoundryData copy() {
        FoundryData newData = new FoundryData();
        newData.modules = Arrays.copyOf(this.modules, 4);
        newData.tier = this.tier;
        newData.checkSolidifierModules();
        return newData;
    }

    private void calculateNewStats() {
        parallelScaleAdj = (parallelScaleBase + parallelScaleAdditive) * parallelScaleMultiplier;
        speedModifierAdj = (speedModifierBase + speedAdditive) * speedMultiplier;
        euEffAdj = (euEffBase + euEffAdditive) * euEffMultiplier;
    }

    public boolean shouldUsePreview(FoundryData other) {
        if (this.modules[0] != other.modules[0]) return true;
        if (this.modules[1] != other.modules[1]) return true;
        if (this.modules[2] != other.modules[2]) return true;
        return this.modules[3] != other.modules[3];
    }

    public void setModule(int index, int ordinal) {
        // just in case, shouldn't be possible
        if (index >= modules.length || index < 0)
            throw new IndexOutOfBoundsException("Index of Module to add must be in bounds.");
        FoundryModule moduleToAdd = FoundryModule.values()[ordinal];

        if (moduleToAdd == FoundryModule.HYPERCOOLER) {
            checkSolidifierModules();
            if (hypercoolerPresent) return;
        }
        if (moduleToAdd == FoundryModule.UNIVERSAL_COLLAPSER) {
            checkSolidifierModules();
            if (tdsPresent) return;
        }
        if (moduleToAdd == FoundryModule.EFFICIENT_OC) {
            checkSolidifierModules();
            if (effOCPresent) return;
        }

        if (modules[index] == moduleToAdd) return;

        modules[index] = moduleToAdd;
        checkSolidifierModules();
    }

    public String getSpeedStr() {
        return (speedModifierAdj) * 100 + "%";
    }

    public String getParallelsString() {
        return (int) parallelScaleAdj + "";
    }

    public String getEuEFFString() {
        return ((int) (euEffAdj * 100)) + "%";
    }

    public String getOCFactorString() {
        return 2 + ocFactorAdditive + " : 4";
    }
}
