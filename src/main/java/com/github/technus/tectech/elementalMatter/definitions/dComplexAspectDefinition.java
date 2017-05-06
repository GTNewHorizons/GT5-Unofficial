package com.github.technus.tectech.elementalMatter.definitions;

import com.github.technus.tectech.auxiliary.TecTechConfig;
import com.github.technus.tectech.elementalMatter.classes.*;
import com.github.technus.tectech.elementalMatter.interfaces.iElementalDefinition;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import static com.github.technus.tectech.elementalMatter.classes.cElementalDecay.noDecay;

/**
 * Created by Tec on 06.05.2017.
 */
public class dComplexAspectDefinition extends cElementalDefinition {
    //TODO aspect binding

    private static final byte nbtType = (byte) 'c';

    private final cElementalDefinitionStackMap aspectStacks;

    public dComplexAspectDefinition(cElementalDefinitionStack[] tree) throws tElementalException{//todo constructors
        aspectStacks=new cElementalDefinitionStackMap();
    }

    @Override
    public String getName() {//todo name
        return "Aspect: ";
    }

    @Override
    public String getSymbol() {
        String symbol = "";
        for (cElementalDefinitionStack quark : aspectStacks.values())
            for (int i = 0; i < quark.amount; i++)
                symbol += quark.definition.getSymbol();
        return symbol;
    }

    @Override
    public NBTTagCompound toNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setByte("t", nbtType);
        cElementalDefinitionStack[] quarkStacksValues = aspectStacks.values();
        nbt.setInteger("i", quarkStacksValues.length);
        for (int i = 0; i < quarkStacksValues.length; i++)
            nbt.setTag(Integer.toString(i), quarkStacksValues[i].toNBT());
        return nbt;
    }

    public static iElementalDefinition fromNBT(NBTTagCompound nbt) {
        cElementalDefinitionStack[] stacks = new cElementalDefinitionStack[nbt.getInteger("i")];
        for (int i = 0; i < stacks.length; i++)
            stacks[i] = cElementalDefinitionStack.fromNBT(nbt.getCompoundTag(Integer.toString(i)));
        try {
            return new dComplexAspectDefinition(stacks);
        } catch (tElementalException e) {
            if (TecTechConfig.DEBUG_MODE) e.printStackTrace();
            return null;
        }
    }

    @Override
    public float getRawLifeTime() {
        return -1;
    }

    @Override
    public int getCharge() {
        return 0;
    }

    @Override
    public byte getType() {
        return 0;
    }

    @Override
    public byte getColor() {
        return -1;
    }

    @Override
    public cElementalDefinitionStackMap getSubParticles() {
        return aspectStacks;
    }

    @Override
    public cElementalDecay[] getEnergeticDecayInstant() {
        return new cElementalDecay[]{new cElementalDecay(0.75F, aspectStacks), eBosonDefinition.deadEnd};
    }

    @Override
    public cElementalDecay[] getNaturalDecayInstant() {
        return noDecay;
    }

    @Override
    public cElementalDecay[] getDecayArray() {
        return noDecay;
    }

    @Override
    public float getMass() {
        return 0;
    }

    @Override
    public FluidStack materializesIntoFluid() {
        return null;
    }

    @Override
    public ItemStack materializesIntoItem() {
        return null;
    }

    @Override
    public iElementalDefinition getAnti() {
        return null;
    }

    public static void run() {
        try {
            cElementalDefinition.addCreatorFromNBT(nbtType, dComplexAspectDefinition.class.getMethod("fromNBT", NBTTagCompound.class));
        } catch (Exception e) {
            if (TecTechConfig.DEBUG_MODE) e.printStackTrace();
        }
    }

    @Override
    public byte getClassType() {
        return -96;
    }
}
