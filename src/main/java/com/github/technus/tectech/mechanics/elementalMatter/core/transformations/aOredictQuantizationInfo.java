package com.github.technus.tectech.mechanics.elementalMatter.core.transformations;

import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.iElementalStack;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;

/**
 * Created by Tec on 23.05.2017.
 */
public class aOredictQuantizationInfo implements iExchangeInfo<String, iElementalStack> {
    public final  String          in;
    public final  int             amount;
    private final iElementalStack out;

    public aOredictQuantizationInfo(String name, int qty, iElementalStack emOut){
        in=name;
        amount=qty;
        out=emOut;
    }

    public aOredictQuantizationInfo(OrePrefixes prefix, Materials material, int qty, iElementalStack emOut){
        in=prefix.name() + material.mName;
        amount=qty;
        out=emOut;
    }

    public aOredictQuantizationInfo(OrePrefixes prefix, String materialName, int qty, iElementalStack emOut){
        in=prefix.name() + materialName;
        amount=qty;
        out=emOut;
    }

    @Override
    public String input() {
        return in;
    }

    @Override
    public iElementalStack output() {
        return out.clone();
    }

    @Override
    public int hashCode() {
        return in.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof aOredictQuantizationInfo && hashCode() == obj.hashCode();
    }
}
