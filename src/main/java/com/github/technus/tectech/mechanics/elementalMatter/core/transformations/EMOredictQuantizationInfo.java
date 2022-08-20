package com.github.technus.tectech.mechanics.elementalMatter.core.transformations;

import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.IEMStack;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by Tec on 23.05.2017.
 */
public class EMOredictQuantizationInfo {
    private final int id;
    private final int amount;
    private final IEMStack out;

    public EMOredictQuantizationInfo(int id, int amount, IEMStack out) {
        this.id = id;
        this.amount = amount;
        this.out = out;
    }

    public EMOredictQuantizationInfo(String name, int qty, IEMStack emOut) {
        this(OreDictionary.getOreID(name), qty, emOut);
    }

    public EMOredictQuantizationInfo(OrePrefixes prefix, Materials material, int qty, IEMStack emOut) {
        this(prefix, material.mName, qty, emOut);
    }

    public EMOredictQuantizationInfo(OrePrefixes prefix, String materialName, int qty, IEMStack emOut) {
        this(OreDictionary.getOreID(prefix.name() + materialName), qty, emOut);
    }

    public IEMStack getOut() {
        return out;
    }

    @Override
    public int hashCode() {
        return getId();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EMOredictQuantizationInfo && hashCode() == obj.hashCode();
    }

    public int getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }
}
