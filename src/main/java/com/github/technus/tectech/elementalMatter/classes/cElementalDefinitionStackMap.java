package com.github.technus.tectech.elementalMatter.classes;

import com.github.technus.tectech.elementalMatter.interfaces.iElementalDefinition;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Map;
import java.util.TreeMap;

import static com.github.technus.tectech.elementalMatter.definitions.cPrimitiveDefinition.nbtE__;

/**
 * Created by Tec on 12.05.2017.
 */
public final class cElementalDefinitionStackMap/*IMMUTABLE*/ extends cElementalStackMap {
    //Constructors + Clone, all make a whole new OBJ.

    @Deprecated
    public cElementalDefinitionStackMap(iElementalDefinition... in) {
        map=new TreeMap<>();
        for (iElementalDefinition definition : in)
            map.put(definition, new cElementalDefinitionStack(definition, 1));
    }

    public cElementalDefinitionStackMap(cElementalDefinitionStack... in) {
        map=new TreeMap<>();
        for (cElementalDefinitionStack stack : in)
            map.put(stack.definition, stack);
    }

    public cElementalDefinitionStackMap(Map<iElementalDefinition, cElementalDefinitionStack> in) {
        map = new TreeMap<>();
        for (cElementalDefinitionStack stack : in.values())
            map.put(stack.definition, stack);
    }

    //IMMUTABLE DON'T NEED IT
    @Override
    public cElementalDefinitionStackMap Clone() {
        return this;
    }

    public cElementalMutableDefinitionStackMap toMutable() {
        return new cElementalMutableDefinitionStackMap(map);
    }

    @Override
    @Deprecated
    public Map<iElementalDefinition,cElementalDefinitionStack> getRawMap() {
        return toMutable().getRawMap();
    }


    //NBT
    public NBTTagCompound getInfoNBT() {
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

    public NBTTagCompound toNBT() {
        final NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("i", map.size());
        int i = 0;
        for (cElementalDefinitionStack defStack : map.values())
            nbt.setTag(Integer.toString(i++), defStack.toNBT());
        return nbt;
    }

    public static cElementalMutableDefinitionStackMap fromNBT(NBTTagCompound nbt) throws tElementalException {
        final cElementalDefinitionStack[] defStacks = new cElementalDefinitionStack[nbt.getInteger("i")];
        for (int i = 0; i < defStacks.length; i++) {
            defStacks[i] = cElementalDefinitionStack.fromNBT(nbt.getCompoundTag(Integer.toString(i)));
            if (defStacks[i].definition.equals(nbtE__))
                throw new tElementalException("Something went Wrong");
        }
        return new cElementalMutableDefinitionStackMap(defStacks);
    }
}
