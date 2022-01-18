package com.github.technus.tectech.mechanics.elementalMatter.core.maps;

import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.IEMStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.IEMDefinition;

import java.util.Map;

public interface IEMMapWriteExact<T extends IEMStack> extends IEMMapRead<T> {
    default void cleanUp(){
        entrySet().removeIf(entry -> IEMMapRead.isInvalidAmount(entry.getValue().getAmount()));
    }

    default void clear() {
        getBackingMap().clear();
    }

    IEMMapWriteExact<T> clone();

    //Remove
    default T removeKey(IEMDefinition def) {
        return getBackingMap().remove(def);
    }

    default T removeKey(IEMStack has) {
        return removeKey(has.getDefinition());
    }

    default boolean removeKeys(IEMDefinition... definitions) {
        boolean hadAll=true;
        for (IEMDefinition def : definitions) {
            hadAll&=removeKey(def)!=null;
        }
        return hadAll;
    }

    default boolean removeKeys(IEMStack... hasElementalDefinition) {
        boolean hadAll=true;
        for (IEMStack has : hasElementalDefinition) {
            hadAll&=removeKey(has)!=null;
        }
        return hadAll;
    }

    default boolean removeAllKeys(IEMDefinition... definitions) {
        boolean hadAll=containsAllKeys(definitions);
        if(hadAll){
            for (IEMDefinition def : definitions) {
                removeKey(def);
            }
        }
        return hadAll;
    }

    default boolean removeAllKeys(IEMStack... hasElementalDefinition) {
        boolean hadAll=containsAllKeys(hasElementalDefinition);
        if(hadAll){
            for (IEMStack stack : hasElementalDefinition) {
                removeKey(stack);
            }
        }
        return hadAll;
    }

    default void putReplace(T defStackUnsafe) {
        getBackingMap().put(defStackUnsafe.getDefinition(), defStackUnsafe);
    }

    default void putReplaceAll(T... defStacksUnsafe) {
        for (T defStack : defStacksUnsafe) {
            putReplace(defStack);
        }
    }

    default void putReplaceAll(IEMMapRead<T> inContainerUnsafe) {
        getBackingMap().putAll(inContainerUnsafe.getBackingMap());
    }

    /**
     * Should only be used when modifying definitions to alter the integer count correctly
     * @param def
     * @return
     */
    default boolean removeAmountExact(IEMStack def){
        return removeAmountExact(def.getDefinition(),def.getAmount());
    }

    /**
     * Should only be used when modifying definitions to alter the integer count correctly
     * @param def
     * @param amountToConsume
     * @return
     */
    default boolean removeAmountExact(IEMDefinition def, double amountToConsume){
        T current=getBackingMap().get(def);
        if(current!=null){
            double newAmount=current.getAmount()-amountToConsume;
            if(newAmount>=0){
                if(IEMMapRead.isValidAmount(current.getAmount())){
                    current=(T)current.mutateAmount(newAmount);
                    getBackingMap().put(current.getDefinition(),current);
                } else {
                    getBackingMap().remove(current.getDefinition());
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
        boolean test=true;
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

    default T putUnifyExact(T stack) {
        T target=getBackingMap().get(stack.getDefinition());
        if(target==null) {
            putReplace(stack);
            return stack;
        }
        double newAmount = target.getAmount()+stack.getAmount();
        if (IEMMapRead.isValidAmount(newAmount)) {
            stack=(T) target.mutateAmount(newAmount);
            putReplace(stack);
            return stack;
        }else {
            removeKey(stack);
            return null;
        }
    }

    default void putUnifyAllExact(T... defs) {
        for (T def : defs) {
            putUnifyExact(def);
        }
    }

    default void putUnifyAllExact(IEMMapRead<T> inTreeUnsafe) {
        for (T in : inTreeUnsafe.values()) {
            putUnifyExact(in);
        }
    }
}
