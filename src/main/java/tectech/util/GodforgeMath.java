package tectech.util;

import java.math.BigInteger;

import tectech.thing.metaTileEntity.multi.MTEForgeOfGods;
import tectech.thing.metaTileEntity.multi.godforge_modules.MTEBaseModule;
import tectech.thing.metaTileEntity.multi.godforge_modules.MTEExoticModule;
import tectech.thing.metaTileEntity.multi.godforge_modules.MTEMoltenModule;
import tectech.thing.metaTileEntity.multi.godforge_modules.MTEPlasmaModule;
import tectech.thing.metaTileEntity.multi.godforge_modules.MTESmeltingModule;

public class GodforgeMath {

    public static int getRandomIntInRange(int min, int max) {
        return (int) (Math.random() * (max - min)) + min;
    }

    public static double calculateFuelConsumption(MTEForgeOfGods godforge) {
        double upgradeFactor = 1;
        if (godforge.isUpgradeActive(2)) {
            upgradeFactor = 0.8;
        }
        if (godforge.getFuelType() == 0) {
            return Math
                .max(godforge.getFuelFactor() * 300 * Math.pow(1.15, godforge.getFuelFactor()) * upgradeFactor, 1);
        }
        if (godforge.getFuelType() == 1) {
            return Math.max(godforge.getFuelFactor() * 2 * Math.pow(1.08, godforge.getFuelFactor()) * upgradeFactor, 1);
        } else return Math.max(godforge.getFuelFactor() / 25 * upgradeFactor, 1);
    }

    public static int calculateStartupFuelConsumption(MTEForgeOfGods godforge) {
        return (int) Math.max(godforge.getFuelFactor() * 25 * Math.pow(1.2, godforge.getFuelFactor()), 1);
    }

    public static int calculateMaxFuelFactor(MTEForgeOfGods godforge) {
        int fuelCap = 5;
        if (godforge.isUpgradeActive(27)) {
            fuelCap = Integer.MAX_VALUE;
        } else {
            if (godforge.isUpgradeActive(9)) {
                fuelCap += godforge.getTotalActiveUpgrades();
            }
            if (godforge.isUpgradeActive(3)) {
                fuelCap *= 1.2;
            }
        }
        return Math.max(fuelCap, 1);
    }

    public static int calculateEffectiveFuelFactor(MTEForgeOfGods godforge) {
        int fuelFactor = godforge.getFuelFactor();
        if (fuelFactor <= 43) {
            return fuelFactor;
        } else {
            return 43 + (int) Math.floor(Math.pow((fuelFactor - 43), 0.4));
        }
    }

    public static void calculateMaxHeatForModules(MTEBaseModule module, MTEForgeOfGods godforge) {
        double logBase = 1.5;
        int baseHeat = 12601;
        if (godforge.isUpgradeActive(12)) {
            if (module instanceof MTESmeltingModule) {
                logBase = 1.12;
            } else {
                logBase = 1.18;
            }
        }
        int recipeHeat = baseHeat + (int) (Math.log(godforge.getFuelFactor()) / Math.log(logBase) * 1000);
        module.setHeatForOC(calculateOverclockHeat(module, godforge, recipeHeat));
        module.setHeat(recipeHeat);
    }

    public static int calculateOverclockHeat(MTEBaseModule module, MTEForgeOfGods godforge, Integer recipeHeat) {
        int actualHeat;
        double exponent;
        if (godforge.isUpgradeActive(20)) {
            if (module instanceof MTESmeltingModule) {
                exponent = 0.85;
            } else {
                exponent = 0.8;
            }
            if (recipeHeat > 30000) {
                actualHeat = (int) Math.floor(30000 + Math.pow(recipeHeat - 30000, exponent));
            } else {
                actualHeat = recipeHeat;
            }
        } else if (godforge.isUpgradeActive(17)) {
            actualHeat = Math.min(recipeHeat, 30000);
        } else {
            actualHeat = Math.min(recipeHeat, 15000);
        }
        return actualHeat;
    }

    public static void calculateSpeedBonusForModules(MTEBaseModule module, MTEForgeOfGods godforge) {
        double speedBonus = 1;

        if (godforge.isUpgradeActive(1)) {
            speedBonus = Math.pow(module.getHeat(), -0.01);
        }

        if (godforge.isUpgradeActive(22)) {
            if (module instanceof MTEPlasmaModule) {
                speedBonus /= Math.pow(module.getMaxParallel(), 0.02);
            } else {
                speedBonus /= Math.pow(module.getMaxParallel(), 0.012);
            }
        }

        if (module instanceof MTEExoticModule) {
            if (godforge.isUpgradeActive(25)) {
                speedBonus = Math.sqrt(speedBonus);
            } else {
                speedBonus = 1;
            }
        }

        module.setSpeedBonus((float) speedBonus);
    }

    public static void calculateMaxParallelForModules(MTEBaseModule module, MTEForgeOfGods godforge) {
        int baseParallel = 0;
        float fuelFactorMultiplier = 1;
        float heatMultiplier = 1;
        float upgradeAmountMultiplier = 1;
        int node53 = 1;
        boolean isMoltenOrSmeltingWithUpgrade = false;

        if (module instanceof MTESmeltingModule) {
            baseParallel = 1024;
        }
        if (module instanceof MTEMoltenModule) {
            baseParallel = 512;
        }
        if (module instanceof MTEPlasmaModule) {
            baseParallel = 384;
        }
        if (module instanceof MTEExoticModule) {
            baseParallel = 36;
        }

        if (module instanceof MTEMoltenModule
            || (module instanceof MTESmeltingModule && godforge.isUpgradeActive(16))) {
            isMoltenOrSmeltingWithUpgrade = true;
        }

        if (godforge.isUpgradeActive(10)) {
            node53 = 2;
        }

        if (godforge.isUpgradeActive(6)) {
            if (godforge.isUpgradeActive(13)) {
                if (isMoltenOrSmeltingWithUpgrade) {
                    fuelFactorMultiplier = 1 + calculateEffectiveFuelFactor(godforge) / 15f * 3;
                } else {
                    fuelFactorMultiplier = 1 + calculateEffectiveFuelFactor(godforge) / 15f * 2;
                }
            } else {
                fuelFactorMultiplier = 1 + calculateEffectiveFuelFactor(godforge) / 15f;
            }
        }

        if (godforge.isUpgradeActive(18)) {
            if (isMoltenOrSmeltingWithUpgrade) {
                heatMultiplier = 1 + module.getHeat() / 15000f;
            } else {
                heatMultiplier = 1 + module.getHeat() / 25000f;
            }
        }

        if (godforge.isUpgradeActive(21)) {
            if (isMoltenOrSmeltingWithUpgrade) {
                upgradeAmountMultiplier = 1 + godforge.getTotalActiveUpgrades() / 5f;
            } else {
                upgradeAmountMultiplier = 1 + godforge.getTotalActiveUpgrades() / 8f;
            }
        }

        int maxParallel = (int) (baseParallel * node53
            * fuelFactorMultiplier
            * heatMultiplier
            * upgradeAmountMultiplier);

        if (module instanceof MTEExoticModule) {
            if (godforge.isUpgradeActive(25)) {
                maxParallel = (int) Math.max(9 * Math.floor(Math.sqrt(maxParallel) / 9), 36);
            } else {
                maxParallel = baseParallel;
            }
        }

        module.setMaxParallel(maxParallel);
    }

    public static void calculateEnergyDiscountForModules(MTEBaseModule module, MTEForgeOfGods godforge) {
        double fillRatioDiscount = 1;
        double maxBatteryDiscount = 1;

        if (godforge.isUpgradeActive(8)) {
            maxBatteryDiscount = 1 - (1 - Math.pow(1.001, -0.01 * godforge.getMaxBatteryCharge())) / 20;
        }

        if (godforge.isUpgradeActive(19)) {
            double fillRatioMinusZeroPointFive = (double) godforge.getBatteryCharge() / godforge.getMaxBatteryCharge()
                - 0.5;
            if (module instanceof MTEPlasmaModule) {
                fillRatioDiscount = 1 - (Math.pow(fillRatioMinusZeroPointFive, 2) * (-0.6) + 0.15);
            } else {
                fillRatioDiscount = 1 - (Math.pow(fillRatioMinusZeroPointFive, 2) * (-0.6) + 0.15) * 2 / 3;
            }
        }

        if (module instanceof MTEExoticModule) {
            if (!godforge.isUpgradeActive(25)) {
                fillRatioDiscount = 1;
                maxBatteryDiscount = 1;
            } else {
                fillRatioDiscount = Math.sqrt(fillRatioDiscount);
                maxBatteryDiscount = Math.sqrt(maxBatteryDiscount);
            }
        }

        module.setEnergyDiscount((float) (fillRatioDiscount * maxBatteryDiscount));
    }

    public static void calculateProcessingVoltageForModules(MTEBaseModule module, MTEForgeOfGods godforge) {
        long voltage = Integer.MAX_VALUE;

        if (godforge.isUpgradeActive(4)) {
            voltage += calculateEffectiveFuelFactor(godforge) * 100_000_000L;
        }

        if (godforge.isUpgradeActive(23)) {
            voltage *= Math.pow(4, godforge.getRingAmount());
        }

        module.setProcessingVoltage(voltage);
    }

    public static void setMiscModuleParameters(MTEBaseModule module, MTEForgeOfGods godforge) {
        int plasmaTier = 0;
        double overclockTimeFactor = 2;

        if (godforge.isUpgradeActive(30)) {
            plasmaTier = 2;
        } else if (godforge.isUpgradeActive(24)) {
            plasmaTier = 1;
        }

        if (godforge.isUpgradeActive(14)) {
            if (module instanceof MTEPlasmaModule) {
                overclockTimeFactor = 2.3;
            } else {
                overclockTimeFactor = 2.15;
            }
            if (module instanceof MTEExoticModule) {
                if (godforge.isUpgradeActive(25)) {
                    overclockTimeFactor = 2 + Math.pow(overclockTimeFactor - 2, 2);
                } else {
                    overclockTimeFactor = 2;
                }
            }
        }

        module.setUpgrade83(godforge.isUpgradeActive(19));
        module.setMultiStepPlasma(godforge.isUpgradeActive(15));
        module.setPlasmaTier(plasmaTier);
        module.setMagmatterCapable(godforge.isUpgradeActive(30));
        module.setVoltageConfig(godforge.isUpgradeActive(28));
        module.setOverclockTimeFactor(overclockTimeFactor);
    }

    public static boolean allowModuleConnection(MTEBaseModule module, MTEForgeOfGods godforge) {

        if (module instanceof MTEMoltenModule && godforge.isUpgradeActive(5)) {
            return true;
        }

        if (module instanceof MTEPlasmaModule && godforge.isUpgradeActive(7)) {
            return true;
        }

        if (module instanceof MTEExoticModule && godforge.isUpgradeActive(11)) {
            return true;
        }

        return module instanceof MTESmeltingModule;
    }

    public static void queryMilestoneStats(MTEBaseModule module, MTEForgeOfGods godforge) {
        godforge.addTotalPowerConsumed(module.getPowerTally());
        module.setPowerTally(BigInteger.ZERO);
        godforge.addTotalRecipesProcessed(module.getRecipeTally());
        module.setRecipeTally(0);

    }
}
