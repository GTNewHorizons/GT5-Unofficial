package gregtech.api.multitileentity.interfaces;

import gregtech.api.multitileentity.multiblock.base.MultiBlockPart;
import net.minecraftforge.common.util.ForgeDirection;

interface IMultiBlockEnergy {

    boolean isUniversalEnergyStored(MultiBlockPart aPart, long aEnergyAmount);

    long getUniversalEnergyStored(MultiBlockPart aPart);

    long getUniversalEnergyCapacity(MultiBlockPart aPart);

    long getOutputAmperage(MultiBlockPart aPart);

    long getOutputVoltage(MultiBlockPart aPart);

    long getInputAmperage(MultiBlockPart aPart);

    long getInputVoltage(MultiBlockPart aPart);

    boolean decreaseStoredEnergyUnits(MultiBlockPart aPart, long aEnergy, boolean aIgnoreTooLittleEnergy);

    boolean increaseStoredEnergyUnits(MultiBlockPart aPart, long aEnergy, boolean aIgnoreTooMuchEnergy);

    boolean drainEnergyUnits(MultiBlockPart aPart, ForgeDirection aSide, long aVoltage, long aAmperage);

    long injectEnergyUnits(MultiBlockPart aPart, ForgeDirection aSide, long aVoltage, long aAmperage);

    long getAverageElectricInput(MultiBlockPart aPart);

    long getAverageElectricOutput(MultiBlockPart aPart);

    long getStoredEU(MultiBlockPart aPart);

    long getEUCapacity(MultiBlockPart aPart);

    boolean inputEnergyFrom(MultiBlockPart aPart, ForgeDirection aSide);

    boolean outputsEnergyTo(MultiBlockPart aPart, ForgeDirection aSide);
}
