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
public final class cElementalInstanceStackTree implements Comparable<cElementalInstanceStackTree> {
    private TreeMap<iElementalDefinition, cElementalInstanceStack> tree = new TreeMap<>();

    //Constructors
    public cElementalInstanceStackTree() {
    }

    public cElementalInstanceStackTree(cElementalInstanceStack... inSafe) {
        this(true, inSafe);
    }

    public cElementalInstanceStackTree(boolean copy, cElementalInstanceStack... in) {
        if (in == null) return;
        if (copy) {
            for (cElementalInstanceStack instance : in) {
                tree.put(instance.definition, instance.getCopy());
            }
        } else {
            for (cElementalInstanceStack instance : in) {
                tree.put(instance.definition, instance);
            }
        }
    }

    private cElementalInstanceStackTree(TreeMap<iElementalDefinition, cElementalInstanceStack> inSafe) {
        this(true, inSafe);
    }

    private cElementalInstanceStackTree(boolean copy, TreeMap<iElementalDefinition, cElementalInstanceStack> in) {
        if (in == null) return;
        if (copy) {
            for (cElementalInstanceStack instance : in.values()) {
                tree.put(instance.definition, instance.getCopy());
            }
        } else {
            tree = in;
        }
    }

    public cElementalInstanceStackTree(cElementalInstanceStackTree inSafe) {
        this(true, inSafe.tree);
    }

    public cElementalInstanceStackTree(boolean copy, cElementalInstanceStackTree in) {
        this(copy, in.tree);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new cElementalInstanceStackTree(tree);
    }

    @Override
    public int compareTo(cElementalInstanceStackTree o) {
        if (tree.size() != o.tree.size()) return tree.size() - o.tree.size();
        cElementalInstanceStack[] ofThis = values(), ofThat = o.values();
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

    public cElementalInstanceStack remove(iElementalDefinition def) {
        return tree.remove(def);
    }

    @Deprecated
    public cElementalInstanceStack remove(iHasElementalDefinition has) {
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
        final cElementalInstanceStack target = tree.get(instance.definition);
        if (target == null)
            return false;
        if (testOnly)
            return target.amount >= instance.amount;
        else {
            final int diff = target.amount - instance.amount;
            if (diff > 0) {
                target.amount = diff;
                return true;
            } else if (diff == 0) {
                tree.remove(instance.definition);
                return true;
            }
        }
        return false;
    }

    public boolean removeAmount(boolean testOnly, cElementalDefinitionStack stack) {
        final cElementalInstanceStack target = tree.get(stack.definition);
        if (target == null)
            return false;
        if (testOnly)
            return target.amount >= stack.amount;
        else {
            final int diff = target.amount - stack.amount;
            if (diff > 0) {
                target.amount = diff;
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

    public boolean removeAllAmounts(boolean testOnly, cElementalDefinitionStackTree container) {
        return removeAllAmounts(testOnly, container.values());
    }

    public boolean removeAllAmounts(boolean testOnly, cElementalInstanceStackTree container) {
        return removeAllAmounts(testOnly, container.values());
    }

    //Remove overflow
    public float removeOverflow(int stacksCount, int stackCapacity) {
        float massRemoved = 0;

        if (tree.size() > stacksCount) {
            iElementalDefinition[] keys = this.keys();
            for (int i = stacksCount; i < keys.length; i++) {
                massRemoved += tree.get(keys[i]).getDefinitionStack().getMass();
                tree.remove(keys[i]);
            }
        }

        for (cElementalInstanceStack instance : this.values())
            if (instance.amount > stackCapacity) {
                massRemoved += instance.definition.getMass() * (instance.amount - stackCapacity);
                instance.amount = stackCapacity;
            }
        return massRemoved;
    }

    //Put replace
    public cElementalInstanceStack putReplace(cElementalInstanceStack instanceUnsafe) {
        return tree.put(instanceUnsafe.definition, instanceUnsafe);
    }

    public void putReplaceAll(cElementalInstanceStack... instances) {
        for (cElementalInstanceStack instance : instances)
            this.tree.put(instance.definition, instance);
    }

    private void putReplaceAll(Map<iElementalDefinition, cElementalInstanceStack> inTreeUnsafe) {
        this.tree.putAll(inTreeUnsafe);
    }

    public void putReplaceAll(cElementalInstanceStackTree inContainerUnsafe) {
        putReplaceAll(inContainerUnsafe.tree);
    }

    //Put unify
    public cElementalInstanceStack putUnify(cElementalInstanceStack instance) {
        return tree.put(instance.definition, instance.unifyIntoThis(tree.get(instance.definition)));
    }

    public void putUnifyAll(cElementalInstanceStack... instances) {
        for (cElementalInstanceStack instance : instances)
            putUnify(instance);
    }

    private void putUnifyAll(Map<iElementalDefinition, cElementalInstanceStack> inTreeUnsafe) {
        for (cElementalInstanceStack in : inTreeUnsafe.values())
            putUnify(in);
    }

    public void putUnifyAll(cElementalInstanceStackTree containerUnsafe) {
        putUnifyAll(containerUnsafe.tree);
    }

    //Getters
    public cElementalInstanceStack getInstance(iElementalDefinition def) {
        return tree.get(def);
    }

    public String[] getElementalInfo() {
        final String[] info = new String[tree.size() * 3];
        int i = 0;
        for (cElementalInstanceStack instance : tree.values()) {
            info[i] = EnumChatFormatting.BLUE + instance.definition.getName();
            info[i + 1] = EnumChatFormatting.AQUA + instance.definition.getSymbol();
            info[i + 2] = "Amount " + EnumChatFormatting.GREEN + instance.amount;
            i += 3;
        }
        return info;
    }

    public cElementalInstanceStack[] values() {
        return tree.values().toArray(new cElementalInstanceStack[0]);
    }

    public iElementalDefinition[] keys() {
        return tree.keySet().toArray(new iElementalDefinition[0]);
    }

    public float getMass() {
        float mass = 0;
        for (cElementalInstanceStack stack : tree.values()) {
            mass += stack.getMass();
        }
        return mass;
    }

    //Tests
    public boolean containsDefinition(iElementalDefinition def) {
        return tree.containsKey(def);
    }

    public boolean containsInstance(cElementalInstanceStack inst) {
        return tree.containsValue(inst);
    }

    public int size() {
        return tree.size();
    }

    public boolean hasStacks() {
        return tree.size() > 0;
    }

    //Tick Content
    public void tickContent(float lifeTimeMult, int postEnergize) {
        for (cElementalInstanceStack instance : this.values()) {
            cElementalInstanceStackTree newThings = instance.decay(lifeTimeMult, instance.age += 20, postEnergize);
            if (newThings == null) {
                instance.nextColor();
            } else {
                tree.remove(instance.definition);
                for (cElementalInstanceStack newInstance : newThings.values())
                    putUnify(newInstance);
            }
        }

    }

    public void tickContent(int postEnergize) {
        for (cElementalInstanceStack instance : this.values()) {
            cElementalInstanceStackTree newThings = instance.decay(instance.age += 20, postEnergize);
            if (newThings == null) {
                instance.nextColor();
            } else {
                tree.remove(instance.definition);
                for (cElementalInstanceStack newInstance : newThings.values())
                    putUnify(newInstance);
            }
        }

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
        for (cElementalInstanceStack instance : tree.values())
            nbt.setTag(Integer.toString(i++), instance.toNBT());
        return nbt;
    }

    public static cElementalInstanceStackTree fromNBT(NBTTagCompound nbt) throws tElementalException {
        final cElementalInstanceStack[] instances = new cElementalInstanceStack[nbt.getInteger("i")];
        for (int i = 0; i < instances.length; i++) {
            instances[i] = cElementalInstanceStack.fromNBT(nbt.getCompoundTag(Integer.toString(i)));
            if (instances[i].definition.equals(debug__))
                throw new tElementalException("Something went Wrong");
        }
        return new cElementalInstanceStackTree(false, instances);
    }

    //stackUp
    public static cElementalInstanceStack[] stackUp(cElementalInstanceStack... in) {
        final cElementalInstanceStackTree inTree = new cElementalInstanceStackTree();
        inTree.putUnifyAll(in);
        return inTree.values();
    }
}
