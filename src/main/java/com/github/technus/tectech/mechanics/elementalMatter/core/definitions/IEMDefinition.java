package com.github.technus.tectech.mechanics.elementalMatter.core.definitions;

import com.github.technus.tectech.mechanics.elementalMatter.core.decay.EMDecay;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMDefinitionsRegistry;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMConstantStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMDefinitionStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;

/**
 * Created by danie_000 on 11.11.2016.
 */
public interface IEMDefinition extends Comparable<IEMDefinition>,Cloneable {//IMMUTABLE
    double            STABLE_RAW_LIFE_TIME       =1.5e36D;
    double            NO_DECAY_RAW_LIFE_TIME     =-1D;
    long              DEFAULT_ENERGY_LEVEL       =0;
    double            DEFAULT_ENERGY_REQUIREMENT =25000D;//legit cuz normal atoms should only emit a gamma if they don't have defined energy levels
    //add text based creators for recipe formula input?

    //Nomenclature
    String getLocalizedName();

    String getSymbol();

    String getShortSymbol();

    void addScanShortSymbols(ArrayList<String> lines, int capabilities, long energyLevel);

    void addScanResults(ArrayList<String> lines, int capabilities, long energyLevel);

    int getMatterMassType();//bigger number means bigger things usually, but it is just used to differentiate between classes of iED

    int getGeneration();

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
    int getMaxColors();

    default boolean hasColor(){
        return getMaxColors()>0;
    }

    double getRawTimeSpan(long currentEnergy);//defined in static fields or generated

    boolean isTimeSpanHalfLife();

    EMConstantStackMap getSubParticles();//contents... null if none

    NBTTagCompound toNBT(EMDefinitionsRegistry registry);

    default EMDefinitionStack getStackForm(double amount){
        return new EMDefinitionStack(this,amount);
    }

    default IEMDefinition clone(){
        return this;
    }

    default int compareClassID(IEMDefinition obj) {
        return getMatterMassType() - obj.getMatterMassType();
    }
}
