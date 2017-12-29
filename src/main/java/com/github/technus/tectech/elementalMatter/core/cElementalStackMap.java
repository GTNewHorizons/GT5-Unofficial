package com.github.technus.tectech.elementalMatter.core;

import com.github.technus.tectech.elementalMatter.core.stacks.cElementalDefinitionStack;
import com.github.technus.tectech.elementalMatter.core.stacks.cElementalInstanceStack;
import com.github.technus.tectech.elementalMatter.core.templates.iElementalDefinition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by Tec on 12.05.2017.
 */
abstract class cElementalStackMap implements Comparable<cElementalStackMap> {
    protected TreeMap<iElementalDefinition, cElementalDefinitionStack> map;

    @Override
    public abstract cElementalStackMap clone();

    @Deprecated
    public abstract TreeMap<iElementalDefinition, cElementalDefinitionStack> getRawMap();

    //Getters
    public final cElementalDefinitionStack getFirst(){
        return map.firstEntry().getValue();
    }

    public final cElementalDefinitionStack getLast(){
        return map.lastEntry().getValue();
    }

    public final cElementalDefinitionStack getDefinitionStack(iElementalDefinition def) {
        return map.get(def);
    }

    public final String[] getElementalInfo() {
        String[] info = new String[map.size() * 3];
        int i = 0;
        for (cElementalDefinitionStack defStack : map.values()) {
            info[i] = EnumChatFormatting.BLUE + defStack.definition.getName();
            info[i + 1] = EnumChatFormatting.AQUA + defStack.definition.getSymbol();
            info[i + 2] = "Amount " + EnumChatFormatting.GREEN + defStack.amount;
            i += 3;
        }
        return info;
    }

    public final cElementalDefinitionStack[] values() {
        Collection<cElementalDefinitionStack> var = map.values();
        return var.toArray(new cElementalDefinitionStack[var.size()]);
    }

    public final iElementalDefinition[] keys() {
        Set<iElementalDefinition> var = map.keySet();
        return var.toArray(new iElementalDefinition[var.size()]);
    }

    public long getCountOfAllAmounts(){
        long sum=0;
        for(cElementalDefinitionStack stack:map.values()){
            sum+=stack.amount;
        }
        return sum;
    }

    //Tests
    public final boolean containsDefinition(iElementalDefinition def) {
        return map.containsKey(def);
    }

    public final boolean containsDefinitionStack(cElementalDefinitionStack inst) {
        return map.containsValue(inst);
    }

    public final int size() {
        return map.size();
    }

    public final boolean hasStacks() {
        return !map.isEmpty();
    }

    public final boolean isEmpty(){
        return map.isEmpty();
    }

    //NBT
    public final NBTTagCompound getInfoNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        String[] info = getElementalInfo();
        nbt.setInteger("i", info.length);
        for (int i = 0; i < info.length; i++) {
            nbt.setString(Integer.toString(i), info[i]);
        }
        return nbt;
    }

    public final NBTTagCompound toNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("i", map.size());
        int i = 0;
        for (cElementalDefinitionStack defStack : map.values()) {
            nbt.setTag(Integer.toString(i++), defStack.toNBT());
        }
        return nbt;
    }

    @Override
    public final int compareTo(cElementalStackMap o) {//this actually compares rest
        int sizeDiff = map.size() - o.map.size();
        if (sizeDiff != 0) {
            return sizeDiff;
        }
        cElementalDefinitionStack[] ofThis = values(), ofO = o.values();
        for (int i = 0; i < ofO.length; i++) {
            int result = ofThis[i].compareTo(ofO[i]);
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof cElementalStackMap) {
            return compareTo((cElementalStackMap) obj) == 0;
        }
        if (obj instanceof cElementalInstanceStackMap) {
            return compareTo(((cElementalInstanceStackMap) obj).toDefinitionMapForComparison()) == 0;
        }
        return false;
    }

    @Override
    public final int hashCode() {//Hash only definitions to compare contents not amounts or data
        int hash = -(map.size() << 4);
        for (cElementalDefinitionStack stack : map.values()) {
            hash += stack.definition.hashCode();
        }
        return hash;
    }

    public double getMass(){
        double mass=0;
        for(cElementalDefinitionStack stack:map.values()){
            mass+=stack.getMass();
        }
        return mass;
    }

    public long getCharge(){
        long charge=0;
        for(cElementalDefinitionStack stack:map.values()){
            charge+=stack.getCharge();
        }
        return charge;
    }
}
