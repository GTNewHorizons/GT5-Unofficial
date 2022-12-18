package com.github.technus.tectech.mechanics.elementalMatter.core.stacks;

import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationRegistry.EM_COUNT_MINIMUM;

import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.IEMDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMDefinitionsRegistry;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by danie_000 on 30.01.2017.
 */
public interface IEMStack extends Comparable<IEMStack>, Cloneable {
    static boolean isValidAmount(double amount) {
        return amount >= EM_COUNT_MINIMUM;
    }

    static boolean isInvalidAmount(double amount) {
        return amount < EM_COUNT_MINIMUM;
    }

    default boolean isValidAmount() {
        return isValidAmount(getAmount());
    }

    default boolean isInvalidAmount() {
        return isInvalidAmount(getAmount());
    }

    IEMDefinition getDefinition();

    double getAmount();

    default double getCharge() {
        return getDefinition().getCharge() * getAmount();
    }

    default double getMass() {
        return getDefinition().getMass() * getAmount();
    }

    IEMStack clone();

    /**
     * Will return stack with mutated amount, it might be a new object!
     *
     * @param newAmount new amount
     * @return new stack (or previous one if was mutable)
     */
    IEMStack mutateAmount(double newAmount);

    NBTTagCompound toNBT(EMDefinitionsRegistry registry);

    @Override
    default int compareTo(IEMStack o) {
        return getDefinition().compareTo(o.getDefinition());
    }
}
