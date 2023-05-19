package gregtech.api.multitileentity.interfaces;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.multitileentity.multiblock.base.MultiBlockPart;

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

    boolean drainEnergyUnits(MultiBlockPart aPart, ForgeDirection side, long aVoltage, long aAmperage);

    long injectEnergyUnits(MultiBlockPart aPart, ForgeDirection side, long aVoltage, long aAmperage);

    long getAverageElectricInput(MultiBlockPart aPart);

    long getAverageElectricOutput(MultiBlockPart aPart);

    long getStoredEU(MultiBlockPart aPart);

    long getEUCapacity(MultiBlockPart aPart);

    boolean inputEnergyFrom(MultiBlockPart aPart, ForgeDirection side);

    boolean outputsEnergyTo(MultiBlockPart aPart, ForgeDirection side);
}
