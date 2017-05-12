package com.github.technus.tectech.elementalMatter.classes;

import com.github.technus.tectech.elementalMatter.interfaces.iElementalDefinition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import java.util.Map;

import static com.github.technus.tectech.elementalMatter.definitions.cPrimitiveDefinition.nbtE__;

/**
 * Created by Tec on 12.05.2017.
 */
abstract class cElementalStackMap implements Comparable<cElementalStackMap> {
    protected Map<iElementalDefinition, cElementalDefinitionStack> map;

    public int compareTo(cElementalStackMap o) {
        if (map.size() != o.map.size()) return map.size() - o.map.size();
        cElementalDefinitionStack[] ofThis = values(), ofThat = o.values();
        for (int i = 0; i < ofThat.length; i++) {
            int result = ofThis[i].compareTo(ofThat[i]);
            if (result != 0) return result;
        }
        return 0;
    }

    @Override
    public abstract cElementalStackMap clone();

    @Deprecated
    public abstract Map<iElementalDefinition,cElementalDefinitionStack> getRawMap();

    //Getters
    public final cElementalDefinitionStack getDefinitionStack(iElementalDefinition def) {
        return map.get(def);
    }

    public final String[] getElementalInfo() {
        final String[] info = new String[map.size() * 3];
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
        return map.values().toArray(new cElementalDefinitionStack[0]);
    }

    public final iElementalDefinition[] keys() {
        return map.keySet().toArray(new iElementalDefinition[0]);
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
        return map.size() > 0;
    }

    //stackUp
    @Deprecated
    public static cElementalMutableDefinitionStackMap stackUpTree(iElementalDefinition... in) {
        final cElementalMutableDefinitionStackMap inTree = new cElementalMutableDefinitionStackMap();
        for (iElementalDefinition def : in) {
            inTree.putUnify(new cElementalDefinitionStack(def, 1));
        }
        return inTree;
    }

    public static cElementalMutableDefinitionStackMap stackUpTree(cElementalDefinitionStack... in) {
        final cElementalMutableDefinitionStackMap inTree = new cElementalMutableDefinitionStackMap();
        inTree.putUnifyAll(in);
        return inTree;
    }

    //NBT
    public final NBTTagCompound getInfoNBT() {
        final NBTTagCompound nbt = new NBTTagCompound();
        final String[] info = getElementalInfo();
        nbt.setInteger("i", info.length);
        for (int i = 0; i < info.length; i++)
            nbt.setString(Integer.toString(i), info[i]);
        return nbt;
    }

    public static String[] infoFromNBT(NBTTagCompound nbt) {
        final String[] strings = new String[nbt.getInteger("i")];
        for (int i = 0; i < strings.length; i++)
            strings[i] = nbt.getString(Integer.toString(i));
        return strings;
    }

    public final NBTTagCompound toNBT() {
        final NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("i", map.size());
        int i = 0;
        for (cElementalDefinitionStack defStack : map.values())
            nbt.setTag(Integer.toString(i++), defStack.toNBT());
        return nbt;
    }
}
