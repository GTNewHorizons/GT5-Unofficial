package com.github.technus.tectech.mechanics.elementalMatter.core.transformations;

import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.IEMStack;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;

/**
 * Created by Tec on 23.05.2017.
 */
public class EMOredictQuantizationInfo implements IEMExchangeInfo<String, IEMStack> {
    private final String   in;
    private final int      amount;
    private final IEMStack out;

    public EMOredictQuantizationInfo(String name, int qty, IEMStack emOut){
        in=name;
        amount=qty;
        out=emOut;
    }

    public EMOredictQuantizationInfo(OrePrefixes prefix, Materials material, int qty, IEMStack emOut){
        in=prefix.name() + material.mName;
        amount=qty;
        out=emOut;
    }

    public EMOredictQuantizationInfo(OrePrefixes prefix, String materialName, int qty, IEMStack emOut){
        in=prefix.name() + materialName;
        amount=qty;
        out=emOut;
    }

    @Override
    public String input() {
        return getIn();
    }

    @Override
    public IEMStack output() {
        return out.clone();
    }

    @Override
    public int hashCode() {
        return getIn().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EMOredictQuantizationInfo && hashCode() == obj.hashCode();
    }

    public String getIn() {
        return in;
    }

    public int getAmount() {
        return amount;
    }
}
