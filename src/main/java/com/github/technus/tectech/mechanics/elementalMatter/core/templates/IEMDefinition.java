package com.github.technus.tectech.mechanics.elementalMatter.core.templates;

import com.github.technus.tectech.mechanics.elementalMatter.core.decay.EMDecay;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMConstantStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMDefinitionStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMFluidDequantizationInfo;
import com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMItemDequantizationInfo;
import com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMOredictDequantizationInfo;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;

/**
 * Created by danie_000 on 11.11.2016.
 */
public interface IEMDefinition extends Comparable<IEMDefinition>,Cloneable {//IMMUTABLE
    double STABLE_RAW_LIFE_TIME =1.5e36D;
    double NO_DECAY_RAW_LIFE_TIME=-1D;
    long DEFAULT_ENERGY_LEVEL=0;
    double DEFAULT_ENERGY_REQUIREMENT=25000D;//legit cuz normal atoms should only emit a gamma if they don't have defined energy levels

    //Nomenclature
    String getLocalizedName();

    String getSymbol();

    String getShortSymbol();

    void addScanShortSymbols(ArrayList<String> lines, int capabilities, long energyLevel);

    void addScanResults(ArrayList<String> lines, int capabilities, long energyLevel);

    byte getType();

    byte getClassType();//bigger number means bigger things usually, but it is just used to differentiate between classes of iED

    //Not dynamically changing stuff
    IEMDefinition getAnti();//gives new anti particle def

    EMDecay[] getDecayArray();//possible decays

    EMDecay[] getNaturalDecayInstant();//natural decay if lifespan <1tick

    EMDecay[] getEnergyInducedDecay(long energyLevel);//energetic decay

    boolean usesSpecialEnergeticDecayHandling();

    boolean usesMultipleDecayCalls(long energyLevel);

    boolean decayMakesEnergy(long energyLevel);

    boolean fusionMakesEnergy(long energyLevel);

    double getEnergyDiffBetweenStates(long currentEnergy, long newEnergyLevel);//positive or negative

    double getMass();//mass... MeV/c^2

    int getCharge();//charge 1/3 electron charge

    //dynamically changing stuff
    byte getColor();//-1 nope cannot 0 it can but undefined

    double getRawTimeSpan(long currentEnergy);//defined in static fields or generated

    boolean isTimeSpanHalfLife();

    EMConstantStackMap getSubParticles();//contents... null if none

    EMFluidDequantizationInfo someAmountIntoFluidStack();

    EMItemDequantizationInfo someAmountIntoItemsStack();

    EMOredictDequantizationInfo someAmountIntoOredictStack();

    NBTTagCompound toNBT();

    EMDefinitionStack getStackForm(double amount);

    IEMDefinition clone();

    default int compareClassID(IEMDefinition obj) {
        return (int) getClassType() - obj.getClassType();
    }
}
