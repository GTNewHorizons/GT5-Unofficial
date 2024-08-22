package gregtech.api.util;

import static gregtech.api.util.GT_Utility.filterValidMTEs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.implementations.Hatch;

public class ExoticEnergyInputHelper {

    /**
     * The Valid Types of TecTech Hatch List.
     */
    private static final List<Class<? extends Hatch>> sExoticEnergyHatchType = new ArrayList<>();

    static {
        tryRegister("com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti");
        tryRegister("com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyTunnel");
    }

    public static void register(Class<? extends Hatch> clazz) {
        if (!Hatch.class.isAssignableFrom(clazz))
            throw new IllegalArgumentException(clazz.getName() + " is not a subclass of " + Hatch.class.getName());
        sExoticEnergyHatchType.add(clazz);
    }

    @SuppressWarnings("unchecked")
    public static void tryRegister(String className) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            return;
        }
        if (!Hatch.class.isAssignableFrom(clazz))
            throw new IllegalArgumentException(clazz.getName() + " is not a subclass of " + Hatch.class.getName());
        sExoticEnergyHatchType.add((Class<? extends Hatch>) clazz);
    }

    public static boolean drainEnergy(long aEU, Collection<? extends Hatch> hatches) {
        for (Hatch tHatch : hatches) {
            long tDrain = Math.min(
                tHatch.getBaseMetaTileEntity()
                    .getStoredEU(),
                aEU);
            tHatch.getBaseMetaTileEntity()
                .decreaseStoredEnergyUnits(tDrain, false);
            aEU -= tDrain;
        }
        return aEU <= 0;
    }

    public static boolean isExoticEnergyInput(IMetaTileEntity aHatch) {
        for (Class<?> clazz : sExoticEnergyHatchType) {
            if (clazz.isInstance(aHatch)) return true;
        }
        return false;
    }

    public static long getTotalEuMulti(Collection<? extends Hatch> hatches) {
        long rEU = 0L;
        for (Hatch tHatch : filterValidMTEs(hatches)) {
            rEU += tHatch.getBaseMetaTileEntity()
                .getInputVoltage() * tHatch.maxWorkingAmperesIn();
        }
        return rEU;
    }

    public static long getMaxInputVoltageMulti(Collection<? extends Hatch> hatches) {
        long rVoltage = 0;
        for (Hatch tHatch : filterValidMTEs(hatches)) {
            rVoltage += tHatch.getBaseMetaTileEntity()
                .getInputVoltage();
        }
        return rVoltage;
    }

    public static long getAverageInputVoltageMulti(Collection<? extends Hatch> hatches) {
        long rVoltage = 0;
        for (Hatch tHatch : filterValidMTEs(hatches)) {
            rVoltage += tHatch.getBaseMetaTileEntity()
                .getInputVoltage();
        }
        if (hatches.isEmpty()) {
            return 0;
        }
        return rVoltage / hatches.size();
    }

    public static long getMaxInputAmpsMulti(Collection<? extends Hatch> hatches) {
        long rAmp = 0;
        for (Hatch tHatch : filterValidMTEs(hatches)) {
            rAmp += tHatch.getBaseMetaTileEntity()
                .getInputAmperage();
        }
        return rAmp;
    }

    public static long getMaxWorkingInputAmpsMulti(Collection<? extends Hatch> hatches) {
        long rAmp = 0;
        for (Hatch tHatch : filterValidMTEs(hatches)) {
            rAmp += tHatch.maxWorkingAmperesIn();
        }
        return rAmp;
    }

    public static List<Class<? extends Hatch>> getAllClasses() {
        return Collections.unmodifiableList(sExoticEnergyHatchType);
    }
}
