package com.github.technus.tectech.mechanics.elementalMatter.core.transformations;

import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.IEMStack;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;

/**
 * Created by Tec on 23.05.2017.
 */
public class EMOredictDequantizationInfo implements IEMExchangeInfo<IEMStack,String> {
    private final IEMStack in;
    private final String   out;
    private final int      amount;

    public EMOredictDequantizationInfo(IEMStack emIn, String name, int qty) {
        in = emIn;
        out =name;
        amount = qty;
    }

    public EMOredictDequantizationInfo(IEMStack emIn, OrePrefixes prefix, Materials material, int qty) {
        in = emIn;
        out = prefix.name() + material.mName;
        amount = qty;
    }

    public EMOredictDequantizationInfo(IEMStack emIn, OrePrefixes prefix, String materialName, int qty) {
        in = emIn;
        out = prefix.name() + materialName;
        amount = qty;
    }

    @Override
    public IEMStack input() {
        return in.clone();//MEH!
    }

    @Override
    public String output() {
        return getOut();
    }

    @Override
    public int hashCode() {
        return in.getDefinition().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EMOredictDequantizationInfo && hashCode() == obj.hashCode();
    }

    public String getOut() {
        return out;
    }

    public int getAmount() {
        return amount;
    }
}
