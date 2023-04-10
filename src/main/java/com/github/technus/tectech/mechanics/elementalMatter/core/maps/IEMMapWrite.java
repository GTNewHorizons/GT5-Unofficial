package com.github.technus.tectech.mechanics.elementalMatter.core.maps;

import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationRegistry.EM_COUNT_EPSILON;
import static com.github.technus.tectech.util.DoubleCount.add;
import static com.github.technus.tectech.util.DoubleCount.sub;
import static com.github.technus.tectech.util.DoubleCount.ulpSigned;

import java.util.Map;

import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.IEMDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.IEMStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationRegistry;

public interface IEMMapWrite<T extends IEMStack> extends IEMMapWriteExact<T> {

    IEMMapWrite<T> clone();

    /**
     * Consumes amount from map
     * 
     * @param def             def to consume
     * @param amountToConsume should be comparable to {@link EMTransformationRegistry#EM_COUNT_PER_MATERIAL_AMOUNT}
     * @return consumed successfully
     */
    default boolean removeAmount(IEMDefinition def, double amountToConsume) {
        double amountRequired = amountToConsume - EM_COUNT_EPSILON;
        if (amountRequired == amountToConsume) {
            amountRequired -= ulpSigned(amountRequired);
        }
        return removeAmount(def, amountToConsume, amountRequired);
    }

    default boolean removeAmount(IEMDefinition def, double amountToConsume, double amountRequired) {
        T current = get(def);
        if (current != null) {
            if (current.getAmount() >= amountRequired) {
                double newAmount = sub(current.getAmount(), amountToConsume);
                if (IEMStack.isValidAmount(newAmount)) {
                    current = (T) current.mutateAmount(newAmount);
                    putReplace(current);
                } else {
                    removeKey(current.getDefinition());
                }
                return true;
            }
        }
        return false;
    }

    default boolean removeAmount(IEMStack stack) {
        return removeAmount(stack.getDefinition(), stack.getAmount());
    }

    default boolean removeAllAmounts(IEMStack... stacks) {
        boolean test = true;
        for (IEMStack stack : stacks) {
            test &= containsAmount(stack);
        }
        if (!test) {
            return test;
        }
        for (IEMStack stack : stacks) {
            removeAmount(stack);
        }
        return true;
    }

    default boolean removeAllAmounts(IEMMapRead<? extends IEMStack> map) {
        boolean test = true;
        for (Map.Entry<IEMDefinition, ? extends IEMStack> entry : map.entrySet()) {
            test &= containsAmount(entry.getValue());
        }
        if (!test) {
            return test;
        }
        for (Map.Entry<IEMDefinition, ? extends IEMStack> entry : map.entrySet()) {
            removeAmount(entry.getValue());
        }
        return true;
    }

    // Put unify
    /**
     *
     * @param stack thing to put
     * @return new mapping or null if merging actually removed stuff
     */
    default T putUnify(T stack) {
        T target = get(stack.getDefinition());
        if (target == null) {
            putReplace(stack);
            return stack;
        }
        double newAmount = add(target.getAmount(), stack.getAmount());
        if (IEMStack.isValidAmount(newAmount)) {
            stack = (T) target.mutateAmount(newAmount);
            putReplace(stack);
            return stack;
        } else {
            removeKey(stack.getDefinition());
            return null;
        }
    }

    default void putUnifyAll(T... defs) {
        for (T def : defs) {
            putUnify(def);
        }
    }

    default void putUnifyAll(IEMMapRead<T> inTreeUnsafe) {
        for (Map.Entry<IEMDefinition, T> in : inTreeUnsafe.entrySet()) {
            putUnify(in.getValue());
        }
    }
}
