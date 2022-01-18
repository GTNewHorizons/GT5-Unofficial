package com.github.technus.tectech.mechanics.elementalMatter.core.maps;

import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.IEMStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.IEMDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationInfo;

import java.util.Map;

import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationInfo.AVOGADRO_CONSTANT_EPSILON;
import static com.github.technus.tectech.util.DoubleCount.*;

public interface IEMMapWrite<T extends IEMStack> extends IEMMapWriteExact<T> {
    IEMMapWrite<T> clone();

    /**
     * Consumes amount from map
     * @param def def to consume
     * @param amountToConsume should be comparable to {@link EMTransformationInfo#AVOGADRO_CONSTANT}
     * @return consumed successfully
     */
    default boolean removeAmount(IEMDefinition def, double amountToConsume){
        double amountRequired=amountToConsume-AVOGADRO_CONSTANT_EPSILON;
        if(amountRequired==amountToConsume){
            amountRequired-=ulpSigned(amountRequired);
        }
        return removeAmount(def,amountToConsume,amountRequired);
    }

    default boolean removeAmount(IEMDefinition def, double amountToConsume, double amountRequired){
        T current=getBackingMap().get(def);
        if(current!=null){
            if(current.getAmount()>=amountRequired){
                double newAmount=sub(current.getAmount(),amountToConsume);
                if(IEMMapRead.isValidAmount(current.getAmount())){
                    current=(T)current.mutateAmount(newAmount);
                    getBackingMap().put(current.getDefinition(),current);
                }else {
                    getBackingMap().remove(current.getDefinition());
                }
                return true;
            }
        }
        return false;
    }

    default boolean removeAmount(IEMStack stack) {
        return removeAmount(stack.getDefinition(),stack.getAmount());
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
        boolean test=true;
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

    //Put unify
    /**
     *
     * @param stack thing to put
     * @return new mapping or null if merging actually removed stuff
     */
    default T putUnify(T stack) {
        T target=getBackingMap().get(stack.getDefinition());
        if(target==null) {
            putReplace(stack);
            return stack;
        }
        double newAmount = add(target.getAmount(), stack.getAmount());
        if (IEMMapRead.isValidAmount(newAmount)) {
            stack=(T) target.mutateAmount(newAmount);
            putReplace(stack);
            return stack;
        }else {
            removeKey(stack);
            return null;
        }
    }

    default void putUnifyAll(T... defs) {
        for (T def : defs) {
            putUnify(def);
        }
    }

    default void putUnifyAll(IEMMapRead<T> inTreeUnsafe) {
        for (T in : inTreeUnsafe.values()) {
            putUnify(in);
        }
    }
}
