package tectech.thing.metaTileEntity.multi.godforge.util;

import static tectech.thing.metaTileEntity.multi.godforge.upgrade.ForgeOfGodsUpgrade.*;

import java.math.BigInteger;

import gregtech.api.util.GTUtility;
import tectech.thing.metaTileEntity.multi.godforge.MTEBaseModule;
import tectech.thing.metaTileEntity.multi.godforge.MTEExoticModule;
import tectech.thing.metaTileEntity.multi.godforge.MTEForgeOfGods;
import tectech.thing.metaTileEntity.multi.godforge.MTEMoltenModule;
import tectech.thing.metaTileEntity.multi.godforge.MTEPlasmaModule;
import tectech.thing.metaTileEntity.multi.godforge.MTESmeltingModule;

public class GodforgeMath {

    public static int getRandomIntInRange(int min, int max) {
        return (int) (Math.random() * (max - min)) + min;
    }

    public static double calculateFuelConsumption(MTEForgeOfGods godforge) {
        double upgradeFactor = 1;
        if (godforge.isUpgradeActive(STEM)) {
            upgradeFactor = 0.8;
        }
        if (godforge.getFuelType() == 0) {
            return godforge.getFuelFactor() * 300 * GTUtility.powInt(1.15, godforge.getFuelFactor()) * upgradeFactor;
        }
        if (godforge.getFuelType() == 1) {
            return godforge.getFuelFactor() * 2 * GTUtility.powInt(1.08, godforge.getFuelFactor()) * upgradeFactor;
        } else return godforge.getFuelFactor() / 25f * upgradeFactor;
    }

    public static int calculateStartupFuelConsumption(MTEForgeOfGods godforge) {
        return (int) Math.max(godforge.getFuelFactor() * 25 * GTUtility.powInt(1.2, godforge.getFuelFactor()), 1);
    }

    public static int calculateMaxFuelFactor(MTEForgeOfGods godforge) {
        int fuelCap = 5;
        if (godforge.isUpgradeActive(TSE)) {
            fuelCap = Integer.MAX_VALUE;
        } else {
            if (godforge.isUpgradeActive(GEM)) {
                fuelCap += godforge.getTotalActiveUpgrades();
            }
            if (godforge.isUpgradeActive(CFCE)) {
                fuelCap *= 1.2;
            }
        }
        return Math.max(fuelCap, 1);
    }

    public static int calculateEffectiveFuelFactor(int fuelFactor) {
        if (fuelFactor <= 43) {
            return fuelFactor;
        } else {
            return 43 + (int) Math.floor(Math.pow((fuelFactor - 43), 0.4));
        }
    }

    public static void calculateMaxHeatForModules(MTEBaseModule module, MTEForgeOfGods godforge) {
        calculateMaxHeatForModules(module, godforge, godforge.getFuelFactor());
    }

    public static void calculateMaxHeatForModules(MTEBaseModule module, MTEForgeOfGods godforge, int fuelFactor) {
        double logBase = 1.5;
        int baseHeat = 12601;
        if (godforge.isUpgradeActive(SEFCP)) {
            if (module instanceof MTESmeltingModule) {
                logBase = 1.12;
            } else {
                logBase = 1.18;
            }
        }
        int recipeHeat = baseHeat + (int) (Math.log(fuelFactor) / Math.log(logBase) * 1000);
        module.setHeatForOC(calculateOverclockHeat(module, godforge, recipeHeat));
        module.setHeat(recipeHeat);
    }

    public static int calculateOverclockHeat(MTEBaseModule module, MTEForgeOfGods godforge, Integer recipeHeat) {
        int actualHeat;
        double exponent;
        if (godforge.isUpgradeActive(NDPE)) {
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
        } else if (godforge.isUpgradeActive(CNTI)) {
            actualHeat = Math.min(recipeHeat, 30000);
        } else {
            actualHeat = Math.min(recipeHeat, 15000);
        }
        return actualHeat;
    }

    public static void calculateSpeedBonusForModules(MTEBaseModule module, MTEForgeOfGods godforge) {
        double speedBonus = 1;

        if (godforge.isUpgradeActive(IGCC)) {
            speedBonus = Math.pow(module.getHeat(), -0.01);
        }

        if (godforge.isUpgradeActive(DOR)) {
            if (module instanceof MTEPlasmaModule) {
                speedBonus /= Math.pow(module.getMaxParallel(), 0.02);
            } else {
                speedBonus /= Math.pow(module.getMaxParallel(), 0.012);
            }
        }

        if (module instanceof MTEExoticModule) {
            if (godforge.isUpgradeActive(PA)) {
                speedBonus = Math.sqrt(speedBonus);
            } else {
                speedBonus = 1;
            }
        }

        module.setSpeedBonus(speedBonus);
    }

    public static void calculateMaxParallelForModules(MTEBaseModule module, MTEForgeOfGods godforge) {
        calculateMaxParallelForModules(module, godforge, godforge.getFuelFactor());
    }

    public static void calculateMaxParallelForModules(MTEBaseModule module, MTEForgeOfGods godforge, int fuelFactor) {
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
            baseParallel = 64;
        }

        if (module instanceof MTEMoltenModule
            || (module instanceof MTESmeltingModule && godforge.isUpgradeActive(DOP))) {
            isMoltenOrSmeltingWithUpgrade = true;
        }

        if (godforge.isUpgradeActive(CTCDD)) {
            node53 = 2;
        }

        if (godforge.isUpgradeActive(SA)) {
            fuelFactorMultiplier = 1 + calculateEffectiveFuelFactor(fuelFactor) / 15f;
            if (godforge.isUpgradeActive(TCT)) {
                if (isMoltenOrSmeltingWithUpgrade) {
                    fuelFactorMultiplier *= 3;
                } else {
                    fuelFactorMultiplier *= 2;
                }
            }
        }

        if (godforge.isUpgradeActive(EPEC)) {
            if (isMoltenOrSmeltingWithUpgrade) {
                heatMultiplier = 1 + module.getHeat() / 15000f;
            } else {
                heatMultiplier = 1 + module.getHeat() / 25000f;
            }
        }

        if (godforge.isUpgradeActive(POS)) {
            if (isMoltenOrSmeltingWithUpgrade) {
                upgradeAmountMultiplier = 1 + godforge.getTotalActiveUpgrades() / 5f;
            } else {
                upgradeAmountMultiplier = 1 + godforge.getTotalActiveUpgrades() / 8f;
            }
        }

        float totalBonuses = node53 * fuelFactorMultiplier * heatMultiplier * upgradeAmountMultiplier;

        if (module instanceof MTEExoticModule) {
            if (godforge.isUpgradeActive(PA)) {
                totalBonuses = (float) Math.sqrt(totalBonuses);
            } else {
                totalBonuses = 1;
            }
        }

        int maxParallel = (int) (baseParallel * totalBonuses);

        module.setMaxParallel(maxParallel);
    }

    public static void calculateEnergyDiscountForModules(MTEBaseModule module, MTEForgeOfGods godforge) {
        double fillRatioDiscount = 1;
        double maxBatteryDiscount = 1;

        if (godforge.isUpgradeActive(REC)) {
            maxBatteryDiscount = 1 - (1 - Math.pow(1.05, -0.05 * godforge.getMaxBatteryCharge())) / 20;
        }

        if (godforge.isUpgradeActive(IMKG)) {
            double fillRatioMinusZeroPointFive = (double) godforge.getBatteryCharge() / godforge.getMaxBatteryCharge()
                - 0.5;
            if (module instanceof MTEPlasmaModule) {
                fillRatioDiscount = 1 - (fillRatioMinusZeroPointFive * fillRatioMinusZeroPointFive * (-0.6) + 0.15);
            } else {
                fillRatioDiscount = 1
                    - (fillRatioMinusZeroPointFive * fillRatioMinusZeroPointFive * (-0.6) + 0.15) * 2 / 3;
            }
        }

        if (module instanceof MTEExoticModule) {
            if (godforge.isUpgradeActive(PA)) {
                fillRatioDiscount = Math.sqrt(fillRatioDiscount);
                maxBatteryDiscount = Math.sqrt(maxBatteryDiscount);
            } else {
                fillRatioDiscount = 1;
                maxBatteryDiscount = 1;
            }
        }

        module.setEnergyDiscount((float) (fillRatioDiscount * maxBatteryDiscount));
    }

    public static void calculateProcessingVoltageForModules(MTEBaseModule module, MTEForgeOfGods godforge) {
        calculateProcessingVoltageForModules(module, godforge, godforge.getFuelFactor());
    }

    public static void calculateProcessingVoltageForModules(MTEBaseModule module, MTEForgeOfGods godforge,
        int fuelFactor) {
        long voltage = 2_000_000_000;

        if (godforge.isUpgradeActive(GISS)) {
            voltage += calculateEffectiveFuelFactor(fuelFactor) * 100_000_000L;
        }

        if (godforge.isUpgradeActive(NGMS)) {
            voltage *= GTUtility.powInt(4, godforge.getRingAmount());
        }

        module.setProcessingVoltage(voltage);
    }

    public static void setMiscModuleParameters(MTEBaseModule module, MTEForgeOfGods godforge) {
        int plasmaTier = 0;
        double overclockTimeFactor = 2;

        if (godforge.isUpgradeActive(END)) {
            plasmaTier = 2;
        } else if (godforge.isUpgradeActive(SEDS)) {
            plasmaTier = 1;
        }

        if (godforge.isUpgradeActive(GGEBE)) {
            if (module instanceof MTEPlasmaModule) {
                overclockTimeFactor = 2.3;
            } else {
                overclockTimeFactor = 2.15;
            }
            if (module instanceof MTEExoticModule) {
                if (godforge.isUpgradeActive(PA)) {
                    overclockTimeFactor = 2 + (overclockTimeFactor - 2) * (overclockTimeFactor - 2);
                } else {
                    overclockTimeFactor = 2;
                }
            }
        }

        module.setUpgrade83(godforge.isUpgradeActive(IMKG));
        module.setMultiStepPlasma(godforge.isUpgradeActive(TPTP));
        module.setPlasmaTier(plasmaTier);
        module.setMagmatterCapable(godforge.isUpgradeActive(END));
        module.setVoltageConfig(godforge.isUpgradeActive(TBF));
        module.setOverclockTimeFactor(overclockTimeFactor);
    }

    public static boolean allowModuleConnection(MTEBaseModule module, MTEForgeOfGods godforge) {

        if (module instanceof MTEMoltenModule && godforge.isUpgradeActive(FDIM)) {
            return true;
        }

        if (module instanceof MTEPlasmaModule && godforge.isUpgradeActive(GPCI)) {
            return true;
        }

        if (module instanceof MTEExoticModule exoticizer) {
            if (godforge.isUpgradeActive(QGPIU) && !exoticizer.isMagmatterModeOn()) {
                return true;
            }
            if (godforge.isUpgradeActive(END) && exoticizer.isMagmatterModeOn()) {
                return true;
            }
        }

        return module instanceof MTESmeltingModule;
    }

    public static boolean factorChangeDuringRecipeAntiCheese(MTEBaseModule module) {
        if (module instanceof MTESmeltingModule || module instanceof MTEMoltenModule) {
            return module.getCurrentRecipeHeat() > module.getHeat();
        } else {
            return false;
        }
    }

    public static void queryMilestoneStats(MTEBaseModule module, MTEForgeOfGods godforge) {
        godforge.addTotalPowerConsumed(module.getPowerTally());
        module.setPowerTally(BigInteger.ZERO);
        godforge.addTotalRecipesProcessed(module.getRecipeTally());
        module.setRecipeTally(0);
        module.setInversionConfig(godforge.isInversionAvailable());
    }
}
