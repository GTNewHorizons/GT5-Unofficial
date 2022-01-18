package com.github.technus.tectech.mechanics.elementalMatter.core.stacks;

import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.IEMDefinition;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by danie_000 on 30.01.2017.
 */
public interface IEMStack extends Comparable<IEMStack>,Cloneable {
    IEMDefinition getDefinition();

    double getAmount();

    default double getCharge(){
        return getDefinition().getCharge()*getAmount();
    }

    default double getMass(){
        return getDefinition().getMass()*getAmount();
    }

    IEMStack clone();

    /**
     * Will return stack with mutated amount, it might be a new object!
     * @param newAmount new amount
     * @return new stack (or previous one if was mutable)
     */
    IEMStack mutateAmount(double newAmount);

    NBTTagCompound toNBT();

    @Override
    default int compareTo(IEMStack o){
        return getDefinition().compareTo(o.getDefinition());
    }
}
