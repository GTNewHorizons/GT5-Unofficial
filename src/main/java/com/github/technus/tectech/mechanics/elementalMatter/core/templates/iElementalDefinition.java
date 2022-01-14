package com.github.technus.tectech.mechanics.elementalMatter.core.templates;

import com.github.technus.tectech.mechanics.elementalMatter.core.decay.cElementalDecay;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.cElementalConstantStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.cElementalDefinitionStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.transformations.aFluidDequantizationInfo;
import com.github.technus.tectech.mechanics.elementalMatter.core.transformations.aItemDequantizationInfo;
import com.github.technus.tectech.mechanics.elementalMatter.core.transformations.aOredictDequantizationInfo;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;

/**
 * Created by danie_000 on 11.11.2016.
 */
public abstract class iElementalDefinition implements Comparable<iElementalDefinition>,Cloneable {//IMMUTABLE
    public static final double STABLE_RAW_LIFE_TIME =1.5e36D;
    public static final double NO_DECAY_RAW_LIFE_TIME=-1D;
    public static final long DEFAULT_ENERGY_LEVEL=0;
    public static final double DEFAULT_ENERGY_REQUIREMENT=25000D;//legit cuz normal atoms should only emit a gamma if they don't have defined energy levels

    //Nomenclature
    public abstract String getName();

    public abstract String getSymbol();

    public abstract String getShortSymbol();

    public abstract void addScanShortSymbols(ArrayList<String> lines, int capabilities, long energyLevel);

    public abstract void addScanResults(ArrayList<String> lines, int capabilities, long energyLevel);

    public abstract byte getType();

    public abstract byte getClassType();//bigger number means bigger things usually, but it is just used to differentiate between classes of iED

    //Not dynamically changing stuff
    public abstract iElementalDefinition getAnti();//gives new anti particle def

    public abstract cElementalDecay[] getDecayArray();//possible decays

    public abstract cElementalDecay[] getNaturalDecayInstant();//natural decay if lifespan <1tick

    public abstract cElementalDecay[] getEnergyInducedDecay(long energyLevel);//energetic decay

    public abstract boolean usesSpecialEnergeticDecayHandling();

    public abstract boolean usesMultipleDecayCalls(long energyLevel);

    public abstract boolean decayMakesEnergy(long energyLevel);

    public abstract boolean fusionMakesEnergy(long energyLevel);

    public abstract double getEnergyDiffBetweenStates(long currentEnergy, long newEnergyLevel);//positive or negative

    public abstract double getMass();//mass... MeV/c^2

    public abstract int getCharge();//charge 1/3 electron charge

    //dynamically changing stuff
    public abstract byte getColor();//-1 nope cannot 0 it can but undefined

    public abstract double getRawTimeSpan(long currentEnergy);//defined in static fields or generated

    public abstract boolean isTimeSpanHalfLife();

    public abstract cElementalConstantStackMap getSubParticles();//contents... null if none

    public abstract aFluidDequantizationInfo someAmountIntoFluidStack();

    public abstract aItemDequantizationInfo someAmountIntoItemsStack();

    public abstract aOredictDequantizationInfo someAmountIntoOredictStack();

    public abstract NBTTagCompound toNBT();

    public abstract cElementalDefinitionStack getStackForm(double amount);

    @Override
    public abstract iElementalDefinition clone();

    final int compareClassID(iElementalDefinition obj) {
        return (int) getClassType() - obj.getClassType();
    }
}
