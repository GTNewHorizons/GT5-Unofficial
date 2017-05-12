package com.github.technus.tectech.elementalMatter.classes;

import com.github.technus.tectech.elementalMatter.interfaces.iElementalDefinition;
import com.github.technus.tectech.elementalMatter.interfaces.iHasElementalDefinition;
import net.minecraft.nbt.NBTTagCompound;

import static com.github.technus.tectech.elementalMatter.definitions.cPrimitiveDefinition.null__;

/**
 * Created by danie_000 on 20.11.2016.
 */
public final class cElementalDefinitionStack implements iHasElementalDefinition {//TODO unify checks for amount?
    public final iElementalDefinition definition;
    public final int amount;

    public cElementalDefinitionStack(iElementalDefinition def, int amount) {
        this.definition = def == null ? null__ : def;
        this.amount = amount;
    }

    @Override
    protected final Object clone() {
        return this;//IMMUTABLE
    }

    @Override
    public iElementalDefinition getDefinition() {
        return definition;//IMMUTABLE
    }

    @Override
    public int getAmount() {
        return amount;
    }

    public int getCharge() {
        return definition.getCharge() * amount;
    }

    public float getMass() {
        return definition.getMass() * amount;
    }

    @Override
    public int compareTo(iHasElementalDefinition o) {
        return definition.compareTo(o.getDefinition());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof iElementalDefinition)
            return definition.compareTo((iElementalDefinition) obj) == 0;
        if (obj instanceof iHasElementalDefinition)
            return definition.compareTo(((iHasElementalDefinition) obj).getDefinition()) == 0;
        return false;
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("d", definition.toNBT());
        nbt.setInteger("q", amount);
        return nbt;
    }

    public static cElementalDefinitionStack fromNBT(NBTTagCompound nbt) {
        return new cElementalDefinitionStack(
                cElementalDefinition.fromNBT(nbt.getCompoundTag("d")),
                nbt.getInteger("q"));
    }

    public cElementalDefinitionStack unifyIntoNew(cElementalDefinitionStack... other) {
        if (other == null) return this;
        int i = amount;
        for (cElementalDefinitionStack stack : other)
            if (stack != null)
                i += stack.amount;
        return new cElementalDefinitionStack(definition, i);
    }

    @Override
    public int hashCode() {
        return definition.hashCode();
    }
}
