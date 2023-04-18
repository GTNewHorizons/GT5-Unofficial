package com.github.technus.tectech.mechanics.elementalMatter.core.maps;

import java.util.Map;

import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.IEMDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.IEMStack;

public interface IEMMapWriteExact<T extends IEMStack> extends IEMMapRead<T> {

    default void cleanUp() {
        entrySet().removeIf(entry -> entry.getValue().isInvalidAmount());
    }

    default void clear() {
        getBackingMap().clear();
    }

    IEMMapWriteExact<T> clone();

    // Remove
    default T removeKey(IEMDefinition def) {
        return getBackingMap().remove(def);
    }

    default boolean removeKeys(IEMDefinition... definitions) {
        boolean hadAll = true;
        for (IEMDefinition def : definitions) {
            hadAll &= removeKey(def) != null;
        }
        return hadAll;
    }

    default boolean removeKeys(IEMStack... hasElementalDefinition) {
        boolean hadAll = true;
        for (IEMStack has : hasElementalDefinition) {
            hadAll &= removeKey(has.getDefinition()) != null;
        }
        return hadAll;
    }

    default boolean removeAllKeys(IEMDefinition... definitions) {
        boolean hadAll = containsAllKeys(definitions);
        if (hadAll) {
            for (IEMDefinition def : definitions) {
                removeKey(def);
            }
        }
        return hadAll;
    }

    default boolean removeAllKeys(IEMStack... hasElementalDefinition) {
        boolean hadAll = containsAllKeys(hasElementalDefinition);
        if (hadAll) {
            for (IEMStack stack : hasElementalDefinition) {
                removeKey(stack.getDefinition());
            }
        }
        return hadAll;
    }

    default void putReplace(T defStackUnsafe) {
        getBackingMap().put(defStackUnsafe.getDefinition(), defStackUnsafe);
    }

    default void putReplaceAll(@SuppressWarnings("unchecked") T... defStacksUnsafe) {
        for (T defStack : defStacksUnsafe) {
            putReplace(defStack);
        }
    }

    default void putReplaceAll(IEMMapRead<T> inContainerUnsafe) {
        getBackingMap().putAll(inContainerUnsafe.getBackingMap());
    }

    /**
     * Should only be used when modifying definitions to alter the integer count correctly
     * 
     * @param def
     * @return
     */
    default boolean removeAmountExact(IEMStack def) {
        return removeAmountExact(def.getDefinition(), def.getAmount());
    }

    /**
     * Should only be used when modifying definitions to alter the integer count correctly
     * 
     * @param def
     * @param amountToConsume
     * @return
     */
    @SuppressWarnings("unchecked")
    default boolean removeAmountExact(IEMDefinition def, double amountToConsume) {
        T current = get(def);
        if (current != null) {
            double newAmount = current.getAmount() - amountToConsume;
            if (newAmount >= 0) {
                if (current.isValidAmount()) {
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

    default boolean removeAllAmountsExact(IEMStack... stacks) {
        boolean test = true;
        for (IEMStack stack : stacks) {
            test &= containsAmountExact(stack);
        }
        if (!test) {
            return test;
        }
        for (IEMStack stack : stacks) {
            removeAmountExact(stack);
        }
        return true;
    }

    default boolean removeAllAmountsExact(IEMMapRead<? extends IEMStack> map) {
        boolean test = true;
        for (Map.Entry<IEMDefinition, ? extends IEMStack> entry : map.entrySet()) {
            test &= containsAmountExact(entry.getValue());
        }
        if (!test) {
            return test;
        }
        for (Map.Entry<IEMDefinition, ? extends IEMStack> entry : map.entrySet()) {
            removeAmountExact(entry.getValue());
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    default T putUnifyExact(T stack) {
        T target = get(stack.getDefinition());
        if (target == null) {
            putReplace(stack);
            return stack;
        }
        double newAmount = target.getAmount() + stack.getAmount();
        if (IEMStack.isValidAmount(newAmount)) {
            stack = (T) target.mutateAmount(newAmount);
            putReplace(stack);
            return stack;
        } else {
            removeKey(stack.getDefinition());
            return null;
        }
    }

    default void putUnifyAllExact(@SuppressWarnings("unchecked") T... defs) {
        for (T def : defs) {
            putUnifyExact(def);
        }
    }

    default void putUnifyAllExact(IEMMapRead<T> inTreeUnsafe) {
        for (Map.Entry<IEMDefinition, T> in : inTreeUnsafe.entrySet()) {
            putUnifyExact(in.getValue());
        }
    }
}
