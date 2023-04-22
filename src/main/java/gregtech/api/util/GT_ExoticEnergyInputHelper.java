package gregtech.api.util;

import static gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;

public class GT_ExoticEnergyInputHelper {

    /**
     * The Valid Types of TecTech Hatch List.
     */
    private static final List<Class<? extends GT_MetaTileEntity_Hatch>> sExoticEnergyHatchType = new ArrayList<>();

    static {
        tryRegister("com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti");
        tryRegister("com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyTunnel");
    }

    public static void register(Class<? extends GT_MetaTileEntity_Hatch> clazz) {
        if (!GT_MetaTileEntity_Hatch.class.isAssignableFrom(clazz)) throw new IllegalArgumentException(
            clazz.getName() + " is not a subclass of " + GT_MetaTileEntity_Hatch.class.getName());
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
        if (!GT_MetaTileEntity_Hatch.class.isAssignableFrom(clazz)) throw new IllegalArgumentException(
            clazz.getName() + " is not a subclass of " + GT_MetaTileEntity_Hatch.class.getName());
        sExoticEnergyHatchType.add((Class<? extends GT_MetaTileEntity_Hatch>) clazz);
    }

    public static boolean drainEnergy(long aEU, Collection<? extends GT_MetaTileEntity_Hatch> hatches) {
        for (GT_MetaTileEntity_Hatch tHatch : hatches) {
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

    public static long getTotalEuMulti(Collection<? extends GT_MetaTileEntity_Hatch> hatches) {
        long rEU = 0L;
        for (GT_MetaTileEntity_Hatch tHatch : hatches)
            if (isValidMetaTileEntity(tHatch)) rEU += tHatch.getBaseMetaTileEntity()
                .getInputVoltage() * tHatch.maxWorkingAmperesIn();
        return rEU;
    }

    public static long getMaxInputVoltageMulti(Collection<? extends GT_MetaTileEntity_Hatch> hatches) {
        long rVoltage = 0;
        for (GT_MetaTileEntity_Hatch tHatch : hatches)
            if (isValidMetaTileEntity(tHatch)) rVoltage += tHatch.getBaseMetaTileEntity()
                .getInputVoltage();
        return rVoltage;
    }

    public static long getAverageInputVoltageMulti(Collection<? extends GT_MetaTileEntity_Hatch> hatches) {
        long rVoltage = 0;
        if (hatches.size() <= 0) {
            return rVoltage;
        }
        for (GT_MetaTileEntity_Hatch tHatch : hatches)
            if (isValidMetaTileEntity(tHatch)) rVoltage += tHatch.getBaseMetaTileEntity()
                .getInputVoltage();
        return rVoltage / hatches.size();
    }

    public static long getMaxInputAmpsMulti(Collection<? extends GT_MetaTileEntity_Hatch> hatches) {
        long rAmp = 0;
        for (GT_MetaTileEntity_Hatch tHatch : hatches)
            if (isValidMetaTileEntity(tHatch)) rAmp += tHatch.getBaseMetaTileEntity()
                .getInputAmperage();
        return rAmp;
    }

    public static long getMaxWorkingInputAmpsMulti(Collection<? extends GT_MetaTileEntity_Hatch> hatches) {
        long rAmp = 0;
        for (GT_MetaTileEntity_Hatch tHatch : hatches) if (isValidMetaTileEntity(tHatch)) {
            rAmp += tHatch.maxWorkingAmperesIn();
        }
        return rAmp;
    }

    public static List<Class<? extends GT_MetaTileEntity_Hatch>> getAllClasses() {
        return Collections.unmodifiableList(sExoticEnergyHatchType);
    }
}
