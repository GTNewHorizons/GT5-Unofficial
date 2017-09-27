package com.github.technus.tectech.elementalMatter.core.interfaces;

import com.github.technus.tectech.elementalMatter.core.cElementalDecay;
import com.github.technus.tectech.elementalMatter.core.cElementalDefinitionStackMap;
import com.github.technus.tectech.elementalMatter.core.containers.cElementalDefinitionStack;
import com.github.technus.tectech.elementalMatter.core.transformations.aFluidDequantizationInfo;
import com.github.technus.tectech.elementalMatter.core.transformations.aItemDequantizationInfo;
import com.github.technus.tectech.elementalMatter.core.transformations.aOredictDequantizationInfo;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by danie_000 on 11.11.2016.
 */
public interface iElementalDefinition extends Comparable<iElementalDefinition>,Cloneable {//IMMUTABLE
    float stableRawLifeTime=1.5e25f;

    //Nomenclature
    String getName();

    String getSymbol();

    byte getType();

    byte getClassType();//bigger number means bigger things usually, but it is just used to differentiate between classes of iED

    //Not dynamically changing stuff
    iElementalDefinition getAnti();//gives new anti particle def

    cElementalDecay[] getDecayArray();//possible decays

    cElementalDecay[] getNaturalDecayInstant();//natural decay if lifespan <1tick

    cElementalDecay[] getEnergeticDecayInstant();//energetic decay if lifespan <1tick

    float getMass();//mass... MeV/c^2

    int getCharge();//charge 1/3 electron charge

    //dynamically changing stuff
    byte getColor();//-1 nope cannot 0 it can but undefined

    float getRawLifeTime();//defined in static fields or generated

    cElementalDefinitionStackMap getSubParticles();//contents... null if none

    aFluidDequantizationInfo someAmountIntoFluidStack();

    aItemDequantizationInfo someAmountIntoItemsStack();

    aOredictDequantizationInfo someAmountIntoOredictStack();

    NBTTagCompound toNBT();

    cElementalDefinitionStack getStackForm(int i);

    iElementalDefinition clone();
}
