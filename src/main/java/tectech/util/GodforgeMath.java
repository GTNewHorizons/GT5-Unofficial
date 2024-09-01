package tectech.util;

import java.math.BigInteger;

import tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_ForgeOfGods;
import tectech.thing.metaTileEntity.multi.godforge_modules.GT_MetaTileEntity_EM_BaseModule;
import tectech.thing.metaTileEntity.multi.godforge_modules.GT_MetaTileEntity_EM_ExoticModule;
import tectech.thing.metaTileEntity.multi.godforge_modules.GT_MetaTileEntity_EM_MoltenModule;
import tectech.thing.metaTileEntity.multi.godforge_modules.GT_MetaTileEntity_EM_PlasmaModule;
import tectech.thing.metaTileEntity.multi.godforge_modules.GT_MetaTileEntity_EM_SmeltingModule;

public class GodforgeMath {

    public static int getRandomIntInRange(int min, int max) {
        return (int) (Math.random() * (max - min)) + min;
    }

    public static double calculateFuelConsumption(GT_MetaTileEntity_EM_ForgeOfGods godforge) {
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

    public static int calculateStartupFuelConsumption(GT_MetaTileEntity_EM_ForgeOfGods godforge) {
        return (int) Math.max(godforge.getFuelFactor() * 25 * Math.pow(1.2, godforge.getFuelFactor()), 1);
    }

    public static int calculateMaxFuelFactor(GT_MetaTileEntity_EM_ForgeOfGods godforge) {
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

    public static int calculateEffectiveFuelFactor(GT_MetaTileEntity_EM_ForgeOfGods godforge) {
        int fuelFactor = godforge.getFuelFactor();
        if (fuelFactor <= 43) {
            return fuelFactor;
        } else {
            return 43 + (int) Math.floor(Math.pow((fuelFactor - 43), 0.4));
        }
    }

    public static void calculateMaxHeatForModules(GT_MetaTileEntity_EM_BaseModule module,
        GT_MetaTileEntity_EM_ForgeOfGods godforge) {
        double logBase = 1.5;
        int baseHeat = 12601;
        if (godforge.isUpgradeActive(12)) {
            if (module instanceof GT_MetaTileEntity_EM_SmeltingModule) {
                logBase = 1.12;
            } else {
                logBase = 1.18;
            }
        }
        int recipeHeat = baseHeat + (int) (Math.log(godforge.getFuelFactor()) / Math.log(logBase) * 1000);
        module.setHeatForOC(calculateOverclockHeat(module, godforge, recipeHeat));
        module.setHeat(recipeHeat);
    }

    public static int calculateOverclockHeat(GT_MetaTileEntity_EM_BaseModule module,
        GT_MetaTileEntity_EM_ForgeOfGods godforge, Integer recipeHeat) {
        int actualHeat;
        double exponent;
        if (godforge.isUpgradeActive(20)) {
            if (module instanceof GT_MetaTileEntity_EM_SmeltingModule) {
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

    public static void calculateSpeedBonusForModules(GT_MetaTileEntity_EM_BaseModule module,
        GT_MetaTileEntity_EM_ForgeOfGods godforge) {
        double speedBonus = 1;

        if (godforge.isUpgradeActive(1)) {
            speedBonus = Math.pow(module.getHeat(), -0.01);
        }

        if (godforge.isUpgradeActive(22)) {
            if (module instanceof GT_MetaTileEntity_EM_PlasmaModule) {
                speedBonus /= Math.pow(module.getMaxParallel(), 0.02);
            } else {
                speedBonus /= Math.pow(module.getMaxParallel(), 0.012);
            }
        }

        if (module instanceof GT_MetaTileEntity_EM_ExoticModule) {
            if (godforge.isUpgradeActive(25)) {
                speedBonus = Math.sqrt(speedBonus);
            } else {
                speedBonus = 1;
            }
        }

        module.setSpeedBonus((float) speedBonus);
    }

    public static void calculateMaxParallelForModules(GT_MetaTileEntity_EM_BaseModule module,
        GT_MetaTileEntity_EM_ForgeOfGods godforge) {
        int baseParallel = 0;
        float fuelFactorMultiplier = 1;
        float heatMultiplier = 1;
        float upgradeAmountMultiplier = 1;
        int node53 = 1;
        boolean isMoltenOrSmeltingWithUpgrade = false;

        if (module instanceof GT_MetaTileEntity_EM_SmeltingModule) {
            baseParallel = 1024;
        }
        if (module instanceof GT_MetaTileEntity_EM_MoltenModule) {
            baseParallel = 512;
        }
        if (module instanceof GT_MetaTileEntity_EM_PlasmaModule) {
            baseParallel = 384;
        }
        if (module instanceof GT_MetaTileEntity_EM_ExoticModule) {
            baseParallel = 36;
        }

        if (module instanceof GT_MetaTileEntity_EM_MoltenModule
            || (module instanceof GT_MetaTileEntity_EM_SmeltingModule && godforge.isUpgradeActive(16))) {
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

        if (module instanceof GT_MetaTileEntity_EM_ExoticModule) {
            if (godforge.isUpgradeActive(25)) {
                maxParallel = (int) Math.max(9 * Math.floor(Math.sqrt(maxParallel) / 9), 36);
            } else {
                maxParallel = baseParallel;
            }
        }

        module.setMaxParallel(maxParallel);
    }

    public static void calculateEnergyDiscountForModules(GT_MetaTileEntity_EM_BaseModule module,
        GT_MetaTileEntity_EM_ForgeOfGods godforge) {
        double fillRatioDiscount = 1;
        double maxBatteryDiscount = 1;

        if (godforge.isUpgradeActive(8)) {
            maxBatteryDiscount = 1 - (1 - Math.pow(1.001, -0.01 * godforge.getMaxBatteryCharge())) / 20;
        }

        if (godforge.isUpgradeActive(19)) {
            double fillRatioMinusZeroPointFive = (double) godforge.getBatteryCharge() / godforge.getMaxBatteryCharge()
                - 0.5;
            if (module instanceof GT_MetaTileEntity_EM_PlasmaModule) {
                fillRatioDiscount = 1 - (Math.pow(fillRatioMinusZeroPointFive, 2) * (-0.6) + 0.15);
            } else {
                fillRatioDiscount = 1 - (Math.pow(fillRatioMinusZeroPointFive, 2) * (-0.6) + 0.15) * 2 / 3;
            }
        }

        if (module instanceof GT_MetaTileEntity_EM_ExoticModule) {
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

    public static void calculateProcessingVoltageForModules(GT_MetaTileEntity_EM_BaseModule module,
        GT_MetaTileEntity_EM_ForgeOfGods godforge) {
        long voltage = Integer.MAX_VALUE;

        if (godforge.isUpgradeActive(4)) {
            voltage += calculateEffectiveFuelFactor(godforge) * 100_000_000L;
        }

        if (godforge.isUpgradeActive(23)) {
            voltage *= Math.pow(4, godforge.getRingAmount());
        }

        module.setProcessingVoltage(voltage);
    }

    public static void setMiscModuleParameters(GT_MetaTileEntity_EM_BaseModule module,
        GT_MetaTileEntity_EM_ForgeOfGods godforge) {
        int plasmaTier = 0;
        double overclockTimeFactor = 2;

        if (godforge.isUpgradeActive(30)) {
            plasmaTier = 2;
        } else if (godforge.isUpgradeActive(24)) {
            plasmaTier = 1;
        }

        if (godforge.isUpgradeActive(14)) {
            if (module instanceof GT_MetaTileEntity_EM_PlasmaModule) {
                overclockTimeFactor = 2.3;
            } else {
                overclockTimeFactor = 2.15;
            }
            if (module instanceof GT_MetaTileEntity_EM_ExoticModule) {
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

    public static boolean allowModuleConnection(GT_MetaTileEntity_EM_BaseModule module,
        GT_MetaTileEntity_EM_ForgeOfGods godforge) {

        if (module instanceof GT_MetaTileEntity_EM_MoltenModule && godforge.isUpgradeActive(5)) {
            return true;
        }

        if (module instanceof GT_MetaTileEntity_EM_PlasmaModule && godforge.isUpgradeActive(7)) {
            return true;
        }

        if (module instanceof GT_MetaTileEntity_EM_ExoticModule && godforge.isUpgradeActive(11)) {
            return true;
        }

        return module instanceof GT_MetaTileEntity_EM_SmeltingModule;
    }

    public static void queryMilestoneStats(GT_MetaTileEntity_EM_BaseModule module,
        GT_MetaTileEntity_EM_ForgeOfGods godforge) {
        godforge.addTotalPowerConsumed(module.getPowerTally());
        module.setPowerTally(BigInteger.ZERO);
        godforge.addTotalRecipesProcessed(module.getRecipeTally());
        module.setRecipeTally(0);

    }
}
