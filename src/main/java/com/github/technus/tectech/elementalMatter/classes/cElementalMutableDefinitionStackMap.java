package com.github.technus.tectech.elementalMatter.classes;

import com.github.technus.tectech.elementalMatter.interfaces.iElementalDefinition;
import com.github.technus.tectech.elementalMatter.interfaces.iHasElementalDefinition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import java.util.Map;
import java.util.TreeMap;

import static com.github.technus.tectech.elementalMatter.definitions.cPrimitiveDefinition.debug__;

/**
 * Created by danie_000 on 22.01.2017.
 */
public class cElementalMutableDefinitionStackMap implements Comparable<cElementalMutableDefinitionStackMap> {//TODO MAKE MUTABLE AF
    protected Map<iElementalDefinition, cElementalDefinitionStack> map;

    //Constructors + Clone, all make a whole new OBJ.
    public cElementalMutableDefinitionStackMap() {map=new TreeMap<>();}

    @Deprecated
    public cElementalMutableDefinitionStackMap(iElementalDefinition... in) {
        map=new TreeMap<>();
        for (iElementalDefinition definition : in)
            map.put(definition, new cElementalDefinitionStack(definition, 1));
    }

    public cElementalMutableDefinitionStackMap(cElementalDefinitionStack... in) {
        map=new TreeMap<>();
        for (cElementalDefinitionStack stack : in)
            map.put(stack.definition, stack);
    }

    public cElementalMutableDefinitionStackMap(Map<iElementalDefinition, cElementalDefinitionStack> in) {
        this(true,in);
    }

    public cElementalMutableDefinitionStackMap(boolean clone, Map<iElementalDefinition, cElementalDefinitionStack> in) {
        if (clone) {
            map = new TreeMap<>();
            for (cElementalDefinitionStack stack : in.values())
                map.put(stack.definition, stack);
        } else {
            map = in;
        }
    }

    @Override
    protected Object clone() {//Equal to making new obj...
        return Clone();
    }

    public cElementalMutableDefinitionStackMap Clone(){
        return new cElementalMutableDefinitionStackMap(map);
    }

    public cElementalDefinitionStackMap immutable() {
        return new cElementalDefinitionStackMap(this);
    }

    public cElementalMutableDefinitionStackMap mutable() {return this;}

    @Deprecated
    public Map<iElementalDefinition,cElementalDefinitionStack> getRawMap() {
        return map;
    }

    @Override
    public int compareTo(cElementalMutableDefinitionStackMap o) {
        if (map.size() != o.map.size()) return map.size() - o.map.size();
        cElementalDefinitionStack[] ofThis = values(), ofThat = o.values();
        for (int i = 0; i < ofThat.length; i++) {
            int result = ofThis[i].compareTo(ofThat[i]);
            if (result != 0) return result;
        }
        return 0;
    }

    //Removers
    public void clear() {
        map.clear();
    }

    public cElementalDefinitionStack remove(iElementalDefinition def) {
        return map.remove(def);
    }

    @Deprecated
    public cElementalDefinitionStack remove(iHasElementalDefinition has) {
        return map.remove(has.getDefinition());
    }

    public void removeAll(iElementalDefinition... definitions) {
        for (iElementalDefinition def : definitions)
            map.remove(def);
    }

    @Deprecated
    private void removeAll(iHasElementalDefinition... hasElementals) {
        for (iHasElementalDefinition has : hasElementals)
            map.remove(has.getDefinition());
    }

    //Remove amounts
    public boolean removeAmount(boolean testOnly, cElementalInstanceStack instance) {
        final cElementalDefinitionStack target = map.get(instance.definition);
        if (target == null)
            return false;
        if (testOnly)
            return target.amount >= instance.amount;
        else {
            final int diff = target.amount - instance.amount;
            if (diff > 0) {
                map.put(target.definition, new cElementalDefinitionStack(target.definition, diff));
                return true;
            } else if (diff == 0) {
                map.remove(instance.definition);
                return true;
            }
        }
        return false;
    }

    public boolean removeAmount(boolean testOnly, cElementalDefinitionStack stack) {
        final cElementalDefinitionStack target = map.get(stack.definition);
        if (target == null)
            return false;
        if (testOnly)
            return target.amount >= stack.amount;
        else {
            final int diff = target.amount - stack.amount;
            if (diff > 0) {
                map.put(target.definition, new cElementalDefinitionStack(target.definition, diff));
                return true;
            } else if (diff == 0) {
                map.remove(stack.definition);
                return true;
            }
        }
        return false;
    }

    @Deprecated
    public boolean removeAmount(boolean testOnly, iElementalDefinition def) {
        return removeAmount(testOnly, new cElementalDefinitionStack(def, 1));
    }

    public boolean removeAllAmounts(boolean testOnly, cElementalInstanceStack... instances) {
        boolean test = true;
        for (cElementalInstanceStack stack : instances)
            test &= removeAmount(true, stack);
        if (testOnly || !test) return test;
        for (cElementalInstanceStack stack : instances)
            removeAmount(false, stack);
        return true;
    }

    public boolean removeAllAmounts(boolean testOnly, cElementalDefinitionStack... stacks) {
        boolean test = true;
        for (cElementalDefinitionStack stack : stacks)
            test &= removeAmount(true, stack);
        if (testOnly || !test) return test;
        for (cElementalDefinitionStack stack : stacks)
            removeAmount(false, stack);
        return true;
    }

    @Deprecated
    public boolean removeAllAmounts(boolean testOnly, iElementalDefinition... definitions) {
        final cElementalDefinitionStack[] stacks = new cElementalDefinitionStack[definitions.length];
        for (int i = 0; i < stacks.length; i++)
            stacks[i] = new cElementalDefinitionStack(definitions[i], 1);
        return removeAllAmounts(testOnly, stacks);
    }

    public boolean removeAllAmounts(boolean testOnly, cElementalMutableDefinitionStackMap container) {
        return removeAllAmounts(testOnly, container.values());
    }

    public boolean removeAllAmounts(boolean testOnly, cElementalInstanceStackMap container) {
        return removeAllAmounts(testOnly, container.values());
    }

    //Put replace
    public cElementalDefinitionStack putReplace(cElementalDefinitionStack defStackUnsafe) {
        return map.put(defStackUnsafe.definition, defStackUnsafe);
    }

    public void putReplaceAll(cElementalDefinitionStack... defStacks) {
        for (cElementalDefinitionStack defStack : defStacks)
            this.map.put(defStack.definition, defStack);
    }

    private void putReplaceAll(Map<iElementalDefinition, cElementalDefinitionStack> inTreeUnsafe) {
        this.map.putAll(inTreeUnsafe);
    }

    public void putReplaceAll(cElementalMutableDefinitionStackMap inContainerUnsafe) {
        putReplaceAll(inContainerUnsafe.map);
    }

    //Put unify
    public cElementalDefinitionStack putUnify(cElementalDefinitionStack def) {
        return map.put(def.definition, def.unifyIntoNew(map.get(def.definition)));
    }

    @Deprecated
    public cElementalDefinitionStack putUnify(iElementalDefinition def) {
        return putUnify(new cElementalDefinitionStack(def, 1));
    }

    public void putUnifyAll(cElementalDefinitionStack... defs) {
        for (cElementalDefinitionStack def : defs)
            putUnify(def);
    }

    @Deprecated
    public void putUnifyAll(iElementalDefinition... defs) {
        for (iElementalDefinition def : defs)
            putUnify(def);
    }

    private void putUnifyAll(Map<iElementalDefinition, cElementalDefinitionStack> inTreeUnsafe) {
        for (cElementalDefinitionStack in : inTreeUnsafe.values())
            putUnify(in);
    }

    public void putUnifyAll(cElementalMutableDefinitionStackMap containerUnsafe) {
        putUnifyAll(containerUnsafe.map);
    }

    //Getters
    public cElementalDefinitionStack getDefinitionStack(iElementalDefinition def) {
        return map.get(def);
    }

    public String[] getElementalInfo() {
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

    public cElementalDefinitionStack[] values() {
        return map.values().toArray(new cElementalDefinitionStack[0]);
    }

    public iElementalDefinition[] keys() {
        return map.keySet().toArray(new iElementalDefinition[0]);
    }

    //Tests
    public boolean containsDefinition(iElementalDefinition def) {
        return map.containsKey(def);
    }

    public boolean containsDefinitionStack(cElementalDefinitionStack inst) {
        return map.containsValue(inst);
    }

    public int size() {
        return map.size();
    }

    public boolean hasStacks() {
        return map.size() > 0;
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
            if (defStacks[i].definition.equals(debug__))
                throw new tElementalException("Something went Wrong");
        }
        return new cElementalMutableDefinitionStackMap(defStacks);
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
}
