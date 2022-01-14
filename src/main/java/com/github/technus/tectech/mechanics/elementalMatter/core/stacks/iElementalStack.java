package com.github.technus.tectech.mechanics.elementalMatter.core.stacks;

import com.github.technus.tectech.mechanics.elementalMatter.core.templates.iElementalDefinition;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by danie_000 on 30.01.2017.
 */
public interface iElementalStack extends Comparable<iElementalStack>,Cloneable {
    iElementalDefinition getDefinition();

    double getAmount();

    double getCharge();

    double getMass();

    iElementalStack clone();

    iElementalStack mutateAmount(double amount);

    NBTTagCompound toNBT();
}
