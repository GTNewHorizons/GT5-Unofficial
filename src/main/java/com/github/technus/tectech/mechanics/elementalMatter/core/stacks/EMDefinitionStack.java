package com.github.technus.tectech.mechanics.elementalMatter.core.stacks;

import com.github.technus.tectech.mechanics.elementalMatter.core.templates.EMComplex;
import com.github.technus.tectech.mechanics.elementalMatter.core.templates.IEMDefinition;
import net.minecraft.nbt.NBTTagCompound;

import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMPrimitiveDefinition.null__;

/**
 * Created by danie_000 on 20.11.2016.
 */
public final class EMDefinitionStack implements IEMStack {
    private final IEMDefinition definition;
    private final double        amount;

    public EMDefinitionStack(IEMDefinition def, double amount) {
        definition = def == null ? null__ : def;
        this.amount = amount;
    }

    @Override
    public EMDefinitionStack clone() {
        return this;//IMMUTABLE
    }

    @Override
    public EMDefinitionStack mutateAmount(double newAmount) {
        if(getAmount() == newAmount){
            return this;
        }
        return new EMDefinitionStack(getDefinition(), newAmount);//IMMUTABLE
    }

    @Override
    public IEMDefinition getDefinition() {
        return definition;//IMMUTABLE
    }

    @Override
    public double getAmount() {
        return amount;
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("d", getDefinition().toNBT());
        nbt.setDouble("Q", getAmount());
        return nbt;
    }

    public static EMDefinitionStack fromNBT(NBTTagCompound nbt) {
        return new EMDefinitionStack(
                EMComplex.fromNBT(nbt.getCompoundTag("d")),
                nbt.getLong("q")+nbt.getDouble("Q"));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IEMDefinition) {
            return getDefinition().compareTo((IEMDefinition) obj) == 0;
        }
        if (obj instanceof IEMStack) {
            return getDefinition().compareTo(((IEMStack) obj).getDefinition()) == 0;
        }
        return false;
    }

    //Amount shouldn't be hashed if this is just indicating amount and not structure
    @Override
    public int hashCode() {
        return getDefinition().hashCode();
    }
}
