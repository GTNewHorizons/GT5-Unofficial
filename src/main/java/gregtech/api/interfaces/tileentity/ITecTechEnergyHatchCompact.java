package gregtech.api.interfaces.tileentity;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;

import java.util.ArrayList;
import java.util.List;

import static gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity;

/**
   For TecTech Multi Amps Hatch and Laser Hatch.
*/
public interface ITecTechEnergyHatchCompact {

    /**
    The Valid Types of TecTech Hatch List.
     */
    List<Class<?>> TecTechHatchType = new ArrayList<>();

    /**
     * It should return a list that contains normal hatches and TecTech Hatches.
     * @return All energy hatches that installed in the multi.
     */
    List<GT_MetaTileEntity_Hatch> getAllEnergyHatchList();

    /**
     * Drain power from the hatches.
     * @param aEU The needed energy.
     * @return Is it success to drain?
     */
    default boolean drainEnergy(long aEU) {
        for (GT_MetaTileEntity_Hatch tHatch : getAllEnergyHatchList()) {
            long tDrain = Math.min(tHatch.getBaseMetaTileEntity().getStoredEU(), aEU);
            tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(tDrain, false);
            aEU -= tDrain;
        }
        return aEU <= 0;
    }

    /**
     * To check whether it is an valid TecTech Hatch.
     * @param aHatch The hatch that going to be checked.
     * @return Is it a valid TecTech hatch?
     */
    default boolean isTecTechHatch(IMetaTileEntity aHatch) {
        if (TecTechHatchType.isEmpty()) {
            try {
                Class<?> TecTechHatchMultiClazz = Class.forName("com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti");
                Class<?> TecTechHatchLaserClazz = Class.forName("com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyTunnel");
                TecTechHatchType.add(TecTechHatchMultiClazz);
                TecTechHatchType.add(TecTechHatchLaserClazz);
            } catch (ClassNotFoundException e) {
                return false;
            }
        }
        for (Class<?> clazz : TecTechHatchType) {
            if (clazz.isInstance(aHatch))
                return true;
        }
        return false;
    }

    default long getMaxInputVoltageMulti() {
        long rVoltage = 0;
        for (GT_MetaTileEntity_Hatch tHatch : getAllEnergyHatchList())
            if (isValidMetaTileEntity(tHatch)) rVoltage += tHatch.getBaseMetaTileEntity().getInputVoltage();
        return rVoltage;
    }

    default long getMaxInputAmpsMulti() {
        long rAmp = 0;
        for (GT_MetaTileEntity_Hatch tHatch : getAllEnergyHatchList())
            if (isValidMetaTileEntity(tHatch)) rAmp += tHatch.getBaseMetaTileEntity().getInputAmperage();
        return rAmp;
    }
}
