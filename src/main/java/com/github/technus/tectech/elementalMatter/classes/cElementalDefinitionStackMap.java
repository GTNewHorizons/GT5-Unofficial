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
public final class cElementalDefinitionStackMap implements Comparable<cElementalDefinitionStackMap> {
    private Map<iElementalDefinition, cElementalDefinitionStack> tree = new TreeMap<>();

    //Constructors
    public cElementalDefinitionStackMap() {
    }

    @Deprecated
    public cElementalDefinitionStackMap(iElementalDefinition... in) {
        if (in == null) return;
        for (iElementalDefinition definition : in)
            tree.put(definition, new cElementalDefinitionStack(definition, 1));
    }

    public cElementalDefinitionStackMap(cElementalDefinitionStack... in) {
        if (in == null) return;
        for (cElementalDefinitionStack stack : in)
            tree.put(stack.definition, stack);
    }

    private cElementalDefinitionStackMap(Map<iElementalDefinition, cElementalDefinitionStack> in) {
        if (in == null) return;
        tree = in;
    }

    public cElementalDefinitionStackMap(cElementalDefinitionStackMap in) {
        this(in.tree);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new cElementalDefinitionStackMap(tree);
    }

    @Override
    public int compareTo(cElementalDefinitionStackMap o) {
        if (tree.size() != o.tree.size()) return tree.size() - o.tree.size();
        cElementalDefinitionStack[] ofThis = values(), ofThat = o.values();
        for (int i = 0; i < ofThat.length; i++) {
            int result = ofThis[i].compareTo(ofThat[i]);
            if (result != 0) return result;
        }
        return 0;
    }

    //Removers
    public void clear() {
        tree.clear();
    }

    public cElementalDefinitionStack remove(iElementalDefinition def) {
        return tree.remove(def);
    }

    @Deprecated
    public cElementalDefinitionStack remove(iHasElementalDefinition has) {
        return tree.remove(has.getDefinition());
    }

    public void removeAll(iElementalDefinition... definitions) {
        for (iElementalDefinition def : definitions)
            tree.remove(def);
    }

    @Deprecated
    private void removeAll(iHasElementalDefinition... hasElementals) {
        for (iHasElementalDefinition has : hasElementals)
            tree.remove(has.getDefinition());
    }

    //Remove amounts
    public boolean removeAmount(boolean testOnly, cElementalInstanceStack instance) {
        final cElementalDefinitionStack target = tree.get(instance.definition);
        if (target == null)
            return false;
        if (testOnly)
            return target.amount >= instance.amount;
        else {
            final int diff = target.amount - instance.amount;
            if (diff > 0) {
                tree.put(target.definition, new cElementalDefinitionStack(target.definition, diff));
                return true;
            } else if (diff == 0) {
                tree.remove(instance.definition);
                return true;
            }
        }
        return false;
    }

    public boolean removeAmount(boolean testOnly, cElementalDefinitionStack stack) {
        final cElementalDefinitionStack target = tree.get(stack.definition);
        if (target == null)
            return false;
        if (testOnly)
            return target.amount >= stack.amount;
        else {
            final int diff = target.amount - stack.amount;
            if (diff > 0) {
                tree.put(target.definition, new cElementalDefinitionStack(target.definition, diff));
                return true;
            } else if (diff == 0) {
                tree.remove(stack.definition);
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

    public boolean removeAllAmounts(boolean testOnly, cElementalDefinitionStackMap container) {
        return removeAllAmounts(testOnly, container.values());
    }

    public boolean removeAllAmounts(boolean testOnly, cElementalInstanceStackMap container) {
        return removeAllAmounts(testOnly, container.values());
    }

    //Put replace
    public cElementalDefinitionStack putReplace(cElementalDefinitionStack defStackUnsafe) {
        return tree.put(defStackUnsafe.definition, defStackUnsafe);
    }

    public void putReplaceAll(cElementalDefinitionStack... defStacks) {
        for (cElementalDefinitionStack defStack : defStacks)
            this.tree.put(defStack.definition, defStack);
    }

    private void putReplaceAll(Map<iElementalDefinition, cElementalDefinitionStack> inTreeUnsafe) {
        this.tree.putAll(inTreeUnsafe);
    }

    public void putReplaceAll(cElementalDefinitionStackMap inContainerUnsafe) {
        putReplaceAll(inContainerUnsafe.tree);
    }

    //Put unify
    public cElementalDefinitionStack putUnify(cElementalDefinitionStack def) {
        return tree.put(def.definition, def.unifyIntoNew(tree.get(def.definition)));
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

    public void putUnifyAll(cElementalDefinitionStackMap containerUnsafe) {
        putUnifyAll(containerUnsafe.tree);
    }

    //Getters
    public cElementalDefinitionStack getDefinitionStack(iElementalDefinition def) {
        return tree.get(def);
    }

    public String[] getElementalInfo() {
        final String[] info = new String[tree.size() * 3];
        int i = 0;
        for (cElementalDefinitionStack defStack : tree.values()) {
            info[i] = EnumChatFormatting.BLUE + defStack.definition.getName();
            info[i + 1] = EnumChatFormatting.AQUA + defStack.definition.getSymbol();
            info[i + 2] = "Amount " + EnumChatFormatting.GREEN + defStack.amount;
            i += 3;
        }
        return info;
    }

    public cElementalDefinitionStack[] values() {
        return tree.values().toArray(new cElementalDefinitionStack[0]);
    }

    public iElementalDefinition[] keys() {
        return tree.keySet().toArray(new iElementalDefinition[0]);
    }

    //Tests
    public boolean containsDefinition(iElementalDefinition def) {
        return tree.containsKey(def);
    }

    public boolean containsDefinitionStack(cElementalDefinitionStack inst) {
        return tree.containsValue(inst);
    }

    public int size() {
        return tree.size();
    }

    public boolean hasStacks() {
        return tree.size() > 0;
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
        nbt.setInteger("i", tree.size());
        int i = 0;
        for (cElementalDefinitionStack defStack : tree.values())
            nbt.setTag(Integer.toString(i++), defStack.toNBT());
        return nbt;
    }

    public static cElementalDefinitionStackMap fromNBT(NBTTagCompound nbt) throws tElementalException {
        final cElementalDefinitionStack[] defStacks = new cElementalDefinitionStack[nbt.getInteger("i")];
        for (int i = 0; i < defStacks.length; i++) {
            defStacks[i] = cElementalDefinitionStack.fromNBT(nbt.getCompoundTag(Integer.toString(i)));
            if (defStacks[i].definition.equals(debug__))
                throw new tElementalException("Something went Wrong");
        }
        return new cElementalDefinitionStackMap(defStacks);
    }

    //stackUp
    @Deprecated
    public static cElementalDefinitionStackMap stackUpTree(iElementalDefinition... in) {
        final cElementalDefinitionStackMap inTree = new cElementalDefinitionStackMap();
        for (iElementalDefinition def : in) {
            inTree.putUnify(new cElementalDefinitionStack(def, 1));
        }
        return inTree;
    }

    public static cElementalDefinitionStackMap stackUpTree(cElementalDefinitionStack... in) {
        final cElementalDefinitionStackMap inTree = new cElementalDefinitionStackMap();
        inTree.putUnifyAll(in);
        return inTree;
    }
}
