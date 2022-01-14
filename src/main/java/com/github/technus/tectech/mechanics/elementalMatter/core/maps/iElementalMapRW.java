package com.github.technus.tectech.mechanics.elementalMatter.core.maps;

import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.iElementalStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.templates.iElementalDefinition;

import java.util.Map;

import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.bTransformationInfo.AVOGADRO_CONSTANT_UNCERTAINTY;
import static com.github.technus.tectech.util.DoubleCount.sub;

public interface iElementalMapRW<T extends iElementalStack> extends iElementalMapR<T> {
    iElementalMapRW<T> clone();

    default void cleanUp(){
        getBackingMap().entrySet().removeIf(entry -> entry.getValue().getAmount() < AVOGADRO_CONSTANT_UNCERTAINTY);
    }

    default void clear() {
        getBackingMap().clear();
    }

    //Remove
    default T remove(iElementalDefinition def) {
        return getBackingMap().remove(def);
    }

    default T remove(iElementalStack has) {
        return remove(has.getDefinition());
    }

    default void removeAll(iElementalDefinition... definitions) {
        for (iElementalDefinition def : definitions) {
            getBackingMap().remove(def);
        }
    }

    default void removeAll(iElementalStack... hasElementalDefinition) {
        for (iElementalStack has : hasElementalDefinition) {
            getBackingMap().remove(has.getDefinition());
        }
    }

    default boolean removeAmount(boolean testOnly, iElementalStack stack) {
        return removeAmount(testOnly,stack.getDefinition(),stack.getAmount());
    }

    default boolean removeAmount(boolean testOnly, iElementalDefinition def,double amount) {
        T target = getBackingMap().get(def);
        if (target == null) {
            return false;
        }
        if (testOnly) {
            return target.getAmount() >= amount;
        } else {
            double newAmount = sub(target.getAmount(),amount);
            if (newAmount > 0) {
                getBackingMap().put(target.getDefinition(), (T)target.mutateAmount(newAmount));
                return true;
            } else if (newAmount == 0) {
                getBackingMap().remove(def);
                return true;
            }
        }
        return false;
    }

    default boolean removeAllAmounts(boolean testOnly, iElementalStack... stacks) {
        boolean test = true;
        for (iElementalStack stack : stacks) {
            test &= removeAmount(true, stack);
        }
        if (testOnly || !test) {
            return test;
        }
        for (iElementalStack stack : stacks) {
            removeAmount(false, stack);
        }
        return true;
    }

    default boolean removeAllAmounts(boolean testOnly, iElementalMapR<? extends iElementalStack> container) {
        boolean test=true;
        for (Map.Entry<iElementalDefinition, ? extends iElementalStack> entry : container.entrySet()) {
            test &= removeAmount(true, entry.getValue());
        }
        if (testOnly || !test) {
            return test;
        }
        for (Map.Entry<iElementalDefinition, ? extends iElementalStack> entry : container.entrySet()) {
            removeAmount(false, entry.getValue());
        }
        return true;
    }

    //Put replace
    default T putReplace(T defStackUnsafe) {
        return getBackingMap().put(defStackUnsafe.getDefinition(), defStackUnsafe);
    }

    default void putReplaceAll(T... defStacks) {
        for (T defStack : defStacks) {
            getBackingMap().put(defStack.getDefinition(), defStack);
        }
    }

    default void putReplaceAll(iElementalMapR<T> inContainerUnsafe) {
        getBackingMap().putAll(inContainerUnsafe.getBackingMap());
    }

    //Put unify
    default T putUnify(T def) {
        T stack=getBackingMap().get(def.getDefinition());
        if(stack==null) {
            return getBackingMap().put(def.getDefinition(), def);
        }
        return getBackingMap().put(def.getDefinition(), (T)stack.mutateAmount(def.getAmount()+stack.getAmount()));
    }

    default void putUnifyAll(T... defs) {
        for (T def : defs) {
            putUnify(def);
        }
    }

    default void putUnifyAll(iElementalMapR<T> inTreeUnsafe) {
        for (T in : inTreeUnsafe.values()) {
            putUnify(in);
        }
    }
}
