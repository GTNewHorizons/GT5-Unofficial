package gregtech.common.tileentities.machines.multi.foundry;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

public class FoundryData {

    public FoundryModules[] modules = { FoundryModules.UNSET, FoundryModules.UNSET, FoundryModules.UNSET,
        FoundryModules.UNSET };
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
    }

    // base stats per module
    public void checkSolidifierModules() {
        resetParameters();
        // loop through each module. based on tier. 2 - 4 modules.
        FoundryModules[] testModules = Arrays.copyOfRange(modules, 0, 2 + (tier - 1));
        for (FoundryModules checkedModule : testModules) {
            switch (checkedModule) {
                case UNSET:
                    break;
                case HYPERCOOLER:
                    hypercoolerPresent = true;
                    break;
                case HELIOCAST_REINFORCEMENT:
                    UIVRecipesEnabled = true;
                    break;
                case EFFICIENT_OC:
                    effOCPresent = true;
                    ocFactorAdditive += 0.35F;
                    break;
                case UNIVERSAL_COLLAPSER:
                    if (tdsPresent) break;
                    tdsPresent = true;
                    euEffMultiplier *= 4;
                    speedMultiplier *= 2;
                    break;
                case STREAMLINED_CASTERS:
                    speedAdditive += 1.5F;
                    break;
                case EXTRA_CASTING_BASINS:
                    parallelScaleAdditive += 12;
                    break;
                case POWER_EFFICIENT_SUBSYSTEMS:
                    euEffAdditive -= 0.1f;
                    euEffMultiplier *= 0.8f;
                    break;
            }
        }

        calculatePairings(testModules);
        calculateNewStats();
    }

    // pair stat buffs for having 2 specific modules together
    private void calculatePairings(FoundryModules[] modules) {
        if (ArrayUtils.contains(modules, FoundryModules.STREAMLINED_CASTERS)
            && ArrayUtils.contains(modules, FoundryModules.EXTRA_CASTING_BASINS)) {
            speedAdditive += 0.75F;
            parallelScaleAdditive += 6;
        }

        if (ArrayUtils.contains(modules, FoundryModules.POWER_EFFICIENT_SUBSYSTEMS)
            && ArrayUtils.contains(modules, FoundryModules.EFFICIENT_OC)) {
            ocFactorAdditive += 0.1F;
            euEffAdditive -= 0.5F;
        }

        if (ArrayUtils.contains(modules, FoundryModules.HYPERCOOLER)
            && ArrayUtils.contains(modules, FoundryModules.UNIVERSAL_COLLAPSER)) {
            euEffMultiplier *= 2;
            speedMultiplier *= 2;
            allowEternity = true;
        }

        int numHelio = (int) Arrays.stream(modules)
            .filter(m -> m == FoundryModules.HELIOCAST_REINFORCEMENT)
            .count();
        if (numHelio > 1) {
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

    public String getSpeedStr() {
        checkSolidifierModules();
        return (speedModifierAdj) * 100 + "%";
    }

    public String getParallelsString() {
        checkSolidifierModules();
        return (int) parallelScaleAdj + "";
    }

    public String getEuEFFString() {
        checkSolidifierModules();
        return ((int) (euEffAdj * 100)) + "%";
    }

    public String getOCFactorString() {
        checkSolidifierModules();
        return 2 + ocFactorAdditive + " : 4";
    }
}
