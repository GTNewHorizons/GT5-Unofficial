package com.github.technus.tectech.elementalMatter.core.interfaces;

import com.github.technus.tectech.elementalMatter.core.cElementalDecay;
import com.github.technus.tectech.elementalMatter.core.cElementalDefinitionStackMap;
import com.github.technus.tectech.elementalMatter.core.containers.cElementalDefinitionStack;
import com.github.technus.tectech.elementalMatter.core.transformations.aFluidDequantizationInfo;
import com.github.technus.tectech.elementalMatter.core.transformations.aItemDequantizationInfo;
import com.github.technus.tectech.elementalMatter.core.transformations.aOredictDequantizationInfo;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;

/**
 * Created by danie_000 on 11.11.2016.
 */
public interface iElementalDefinition extends Comparable<iElementalDefinition>,Cloneable {//IMMUTABLE
    float STABLE_RAW_LIFE_TIME =1.5e36f;
    float NO_DECAY_RAW_LIFE_TIME=-1;
    long DEFAULT_ENERGY_LEVEL=0;
    float DEFAULT_ENERGY_REQUIREMENT=25000f;

    //Nomenclature
    String getName();

    String getSymbol();

    void addScanResults(ArrayList<String> lines, int capabilities, long energyLevel);

    byte getType();

    byte getClassType();//bigger number means bigger things usually, but it is just used to differentiate between classes of iED

    //Not dynamically changing stuff
    iElementalDefinition getAnti();//gives new anti particle def

    cElementalDecay[] getDecayArray();//possible decays

    cElementalDecay[] getNaturalDecayInstant();//natural decay if lifespan <1tick

    cElementalDecay[] getEnergyInducedDecay(long energyLevel);//energetic decay

    boolean usesSpecialEnergeticDecayHandling();

    float getEnergyDiffBetweenStates(long currentEnergy, long newEnergyLevel);//positive or negative

    float getMass();//mass... MeV/c^2

    int getCharge();//charge 1/3 electron charge

    //dynamically changing stuff
    byte getColor();//-1 nope cannot 0 it can but undefined

    float getRawTimeSpan(long currentEnergy);//defined in static fields or generated

    boolean isTimeSpanHalfLife();

    cElementalDefinitionStackMap getSubParticles();//contents... null if none

    aFluidDequantizationInfo someAmountIntoFluidStack();

    aItemDequantizationInfo someAmountIntoItemsStack();

    aOredictDequantizationInfo someAmountIntoOredictStack();

    NBTTagCompound toNBT();

    cElementalDefinitionStack getStackForm(int i);

    iElementalDefinition clone();
}
