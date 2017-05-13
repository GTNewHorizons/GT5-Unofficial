package com.github.technus.tectech.elementalMatter.interfaces;

import com.github.technus.tectech.elementalMatter.classes.cElementalDecay;
import com.github.technus.tectech.elementalMatter.classes.cElementalDefinitionStackMap;
import com.github.technus.tectech.elementalMatter.classes.cElementalMutableDefinitionStackMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by danie_000 on 11.11.2016.
 */
public interface iElementalDefinition extends Comparable<iElementalDefinition> {//IMMUTABLE

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

    ItemStack materializesIntoItem();

    FluidStack materializesIntoFluid();

    NBTTagCompound toNBT();
}
