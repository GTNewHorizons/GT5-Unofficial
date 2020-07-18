package com.github.technus.tectech.mechanics.elementalMatter.core.stacks;

import com.github.technus.tectech.mechanics.elementalMatter.core.templates.cElementalDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.templates.iElementalDefinition;
import com.github.technus.tectech.util.DoubleCount;
import net.minecraft.nbt.NBTTagCompound;

import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.cPrimitiveDefinition.null__;

/**
 * Created by danie_000 on 20.11.2016.
 */
public final class cElementalDefinitionStack implements iHasElementalDefinition {
    public final iElementalDefinition definition;
    public final double amount;

    public cElementalDefinitionStack(iElementalDefinition def, double amount) {
        definition = def == null ? null__ : def;
        this.amount = amount;
    }

    @Override
    public cElementalDefinitionStack clone() {
        return this;//IMMUTABLE
    }

    @Override
    public iElementalDefinition getDefinition() {
        return definition;//IMMUTABLE
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public double getCharge() {
        return definition.getCharge() * amount;
    }

    @Override
    public double getMass() {
        return definition.getMass() * amount;
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("d", definition.toNBT());
        nbt.setDouble("Q", amount);
        return nbt;
    }

    public static cElementalDefinitionStack fromNBT(NBTTagCompound nbt) {
        return new cElementalDefinitionStack(
                cElementalDefinition.fromNBT(nbt.getCompoundTag("d")),
                nbt.getLong("q")+nbt.getDouble("Q"));
    }

    public cElementalDefinitionStack addAmountIntoNewInstance(double amount) {
        if(amount==0) {
            return this;
        }
        return new cElementalDefinitionStack(definition, DoubleCount.add(amount,this.amount));
    }

    public cElementalDefinitionStack addAmountIntoNewInstance(cElementalDefinitionStack... other) {
        if (other == null || other.length == 0) {
            return this;
        }
        double l = 0;
        for (cElementalDefinitionStack stack : other) {
            l=DoubleCount.add(l,stack.amount);
        }
        return addAmountIntoNewInstance(l);
    }

    @Override
    public int compareTo(iHasElementalDefinition o) {
        return definition.compareTo(o.getDefinition());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof iElementalDefinition) {
            return definition.compareTo((iElementalDefinition) obj) == 0;
        }
        if (obj instanceof iHasElementalDefinition) {
            return definition.compareTo(((iHasElementalDefinition) obj).getDefinition()) == 0;
        }
        return false;
    }

    //Amount shouldn't be hashed if this is just indicating amount and not structure
    @Override
    public int hashCode() {
        return definition.hashCode();
    }
}
