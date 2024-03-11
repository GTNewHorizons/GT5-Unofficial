package com.github.technus.tectech.util;

import com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_ForgeOfGods;
import com.github.technus.tectech.thing.metaTileEntity.multi.godforge_modules.GT_MetaTileEntity_EM_BaseModule;
import com.github.technus.tectech.thing.metaTileEntity.multi.godforge_modules.GT_MetaTileEntity_EM_MoltenModule;
import com.github.technus.tectech.thing.metaTileEntity.multi.godforge_modules.GT_MetaTileEntity_EM_PlasmaModule;
import com.github.technus.tectech.thing.metaTileEntity.multi.godforge_modules.GT_MetaTileEntity_EM_SmeltingModule;

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

    public static int calculateMaxFuelFactor(GT_MetaTileEntity_EM_ForgeOfGods godforge) {

        if (godforge.isUpgradeActive(27)) {
            return Integer.MAX_VALUE;
        }

        int fuelCap = 5;
        if (godforge.isUpgradeActive(9)) {
            fuelCap += godforge.getTotalActiveUpgrades();
        }

        if (godforge.isUpgradeActive(3)) {
            fuelCap *= 1.2;
        }
        return fuelCap;
    }

    public static int calculateMaxHeatForModules(GT_MetaTileEntity_EM_BaseModule module,
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
        return baseHeat + (int) (Math.log(godforge.getFuelFactor()) / Math.log(logBase) * 1000);
    }

    public static float calucateSpeedBonusForModules(GT_MetaTileEntity_EM_BaseModule module,
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
        return (float) speedBonus;
    }

    public static int calculateEffectiveFuelFactor(GT_MetaTileEntity_EM_ForgeOfGods godforge) {
        int fuelFactor = godforge.getFuelFactor();
        if (fuelFactor <= 43) {
            return fuelFactor;
        } else {
            return 43 + (int) Math.floor(Math.pow((fuelFactor - 43), 0.4));
        }
    }

    public static int calucateMaxParallelForModules(GT_MetaTileEntity_EM_BaseModule module,
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
            baseParallel = 256;
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

        return (int) (baseParallel * node53 * fuelFactorMultiplier * heatMultiplier * upgradeAmountMultiplier);
    }
}
