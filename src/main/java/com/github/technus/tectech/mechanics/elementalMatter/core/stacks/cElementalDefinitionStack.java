package com.github.technus.tectech.mechanics.elementalMatter.core.stacks;

import com.github.technus.tectech.mechanics.elementalMatter.core.templates.cElementalDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.templates.iElementalDefinition;
import net.minecraft.nbt.NBTTagCompound;

import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.cPrimitiveDefinition.null__;

/**
 * Created by danie_000 on 20.11.2016.
 */
public final class cElementalDefinitionStack implements iElementalStack {
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
    public cElementalDefinitionStack mutateAmount(double amount) {
        if(this.amount==amount){
            return this;
        }
        return new cElementalDefinitionStack(definition,amount);//IMMUTABLE
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

    @Override
    public int compareTo(iElementalStack o) {
        return definition.compareTo(o.getDefinition());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof iElementalDefinition) {
            return definition.compareTo((iElementalDefinition) obj) == 0;
        }
        if (obj instanceof iElementalStack) {
            return definition.compareTo(((iElementalStack) obj).getDefinition()) == 0;
        }
        return false;
    }

    //Amount shouldn't be hashed if this is just indicating amount and not structure
    @Override
    public int hashCode() {
        return definition.hashCode();
    }
}
