package com.github.technus.tectech.mechanics.elementalMatter.core.definitions;

import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMDefinitionsRegistry;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMDefinitionStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.IEMStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by danie_000 on 23.01.2017.
 */
public abstract class EMComplexTemplate implements IEMDefinition {
    /**
     * Just empty array?
     */
    public static final IEMDefinition[] nothing = new EMPrimitiveTemplate[0];

    @Override
    public final EMComplexTemplate clone() {
        return this; // IMMUTABLE
    }

    @Override
    public int compareTo(IEMDefinition o) {
        int classCompare = compareClassID(o);
        if (classCompare != 0) {
            return classCompare;
        }
        // that allows neat check if the same thing and
        // top hierarchy amount can be used to store amount info
        return getSubParticles().compareWithAmountsInternal(o.getSubParticles());
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof IEMDefinition) {
            return compareTo((IEMDefinition) obj) == 0;
        }
        if (obj instanceof IEMStack) {
            return compareTo(((IEMStack) obj).getDefinition()) == 0;
        }
        return false;
    }

    @Override
    public int hashCode() { // Internal amounts should be also hashed
        int hash = -(getSubParticles().size() << 16);
        for (EMDefinitionStack stack : getSubParticles().valuesToArray()) {
            int amount = (int) stack.getAmount();
            hash += ((amount & 0x1) == 0 ? -amount : amount)
                    * (stack.getDefinition().hashCode() << 4);
        }
        return hash;
    }

    @Override
    public String toString() {
        return getLocalizedName() + " " + getSymbol();
    }

    public NBTTagCompound toNBT(EMDefinitionsRegistry registry) {
        return registry.indirectToNBT(getTagValue(), getSubParticles());
    }

    protected abstract String getTagValue();
}
