package com.github.technus.tectech.elementalMatter.core;

import com.github.technus.tectech.elementalMatter.core.stacks.iHasElementalDefinition;
import com.github.technus.tectech.elementalMatter.core.stacks.cElementalDefinitionStack;
import com.github.technus.tectech.elementalMatter.core.stacks.cElementalInstanceStack;
import com.github.technus.tectech.elementalMatter.core.templates.iElementalDefinition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static com.github.technus.tectech.elementalMatter.definitions.primitive.cPrimitiveDefinition.nbtE__;

/**
 * Created by danie_000 on 22.01.2017.
 */
public final class cElementalInstanceStackMap implements Comparable<cElementalInstanceStackMap> {
    TreeMap<iElementalDefinition, cElementalInstanceStack> map;

    //Constructors
    public cElementalInstanceStackMap() {
        map = new TreeMap<>();
    }

    public cElementalInstanceStackMap(cElementalInstanceStack... inSafe) {
        this(true, inSafe);
    }

    public cElementalInstanceStackMap(boolean clone, cElementalInstanceStack... in) {
        map = new TreeMap<>();
        if (clone) {
            cElementalInstanceStack[] stacks=new cElementalInstanceStack[in.length];
            for(int i=0;i<stacks.length;i++) {
                stacks[i] = in[i].clone();
            }
            putUnifyAll(stacks);
        } else {
            putUnifyAll(in);
        }
    }

    @Deprecated
    private cElementalInstanceStackMap(TreeMap<iElementalDefinition, cElementalInstanceStack> inSafe) {
        this(true, inSafe);
    }

    @Deprecated
    private cElementalInstanceStackMap(boolean clone, TreeMap<iElementalDefinition, cElementalInstanceStack> in) {
        if (clone) {
            map = new TreeMap<>();
            for(cElementalInstanceStack stack:in.values()) {
                putUnify(stack.clone());
            }
        } else {
            map = in;
        }
    }

    public cElementalInstanceStackMap(cElementalInstanceStackMap inSafe) {
        this(true, inSafe.map);
    }

    public cElementalInstanceStackMap(boolean copy, cElementalInstanceStackMap in) {
        this(copy, in.map);
    }

    @Override
    public cElementalInstanceStackMap clone() {
        return new cElementalInstanceStackMap(map);
    }

    public cElementalMutableDefinitionStackMap toDefinitionMapForComparison() {
        cElementalDefinitionStack[] list = new cElementalDefinitionStack[map.size()];
        int i = 0;
        for (cElementalInstanceStack stack : map.values()) {
            list[i++] = new cElementalDefinitionStack(stack.definition, stack.amount);
        }
        return new cElementalMutableDefinitionStackMap(list);
    }

    //@Deprecated
    //public cElementalStackMap toDefinitionMap(boolean mutable) {
    //    TreeMap<iElementalDefinition, cElementalDefinitionStack> newMap = new TreeMap<>();
    //    for (cElementalInstanceStack stack : map.values()) {
    //        newMap.put(stack.definition, new cElementalDefinitionStack(stack.definition, stack.amount));
    //    }
    //    if (mutable) {
    //        return new cElementalMutableDefinitionStackMap(newMap);
    //    }
    //    return new cElementalDefinitionStackMap(newMap);
    //}

    @Deprecated
    public Map<iElementalDefinition, cElementalInstanceStack> getRawMap() {
        return map;
    }

    //Removers
    public void clear() {
        map.clear();
    }

    public cElementalInstanceStack remove(iElementalDefinition def) {
        return map.remove(def);
    }

    @Deprecated
    public cElementalInstanceStack remove(iHasElementalDefinition has) {
        return map.remove(has.getDefinition());
    }

    public void removeAll(iElementalDefinition... definitions) {
        for (iElementalDefinition def : definitions) {
            map.remove(def);
        }
    }

    @Deprecated
    private void removeAll(iHasElementalDefinition... hasElementalDefinition) {
        for (iHasElementalDefinition has : hasElementalDefinition) {
            map.remove(has.getDefinition());
        }
    }

    //Remove amounts
    public boolean removeAmount(boolean testOnly, cElementalInstanceStack instance) {
        cElementalInstanceStack target = map.get(instance.definition);
        if (target == null) {
            return false;
        }
        if (testOnly) {
            return target.amount >= instance.amount;
        } else {
            long diff = target.amount - instance.amount;
            if (diff > 0) {
                target.amount = diff;
                return true;
            } else if (diff == 0) {
                map.remove(instance.definition);
                return true;
            }
        }
        return false;
    }

    public boolean removeAmount(boolean testOnly, iHasElementalDefinition stack) {
        cElementalInstanceStack target = map.get(stack.getDefinition());
        if (target == null) {
            return false;
        }
        if (testOnly) {
            return target.amount >= stack.getAmount();
        } else {
            long diff = target.amount - stack.getAmount();
            if (diff > 0) {
                target.amount = diff;
                return true;
            } else if (diff == 0) {
                map.remove(stack.getDefinition());
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
        for (cElementalInstanceStack stack : instances) {
            test &= removeAmount(true, stack);
        }
        if (testOnly || !test) {
            return test;
        }
        for (cElementalInstanceStack stack : instances) {
            removeAmount(false, stack);
        }
        return true;
    }

    public boolean removeAllAmounts(boolean testOnly, iHasElementalDefinition... stacks) {
        boolean test = true;
        for (iHasElementalDefinition stack : stacks) {
            test &= removeAmount(true, stack);
        }
        if (testOnly || !test) {
            return test;
        }
        for (iHasElementalDefinition stack : stacks) {
            removeAmount(false, stack);
        }
        return true;
    }

    @Deprecated
    public boolean removeAllAmounts(boolean testOnly, iElementalDefinition... definitions) {
        cElementalDefinitionStack[] stacks = new cElementalDefinitionStack[definitions.length];
        for (int i = 0; i < stacks.length; i++) {
            stacks[i] = new cElementalDefinitionStack(definitions[i], 1);
        }
        return removeAllAmounts(testOnly, stacks);
    }

    public boolean removeAllAmounts(boolean testOnly, cElementalStackMap container) {
        boolean test=true;
        for (Map.Entry<iElementalDefinition, cElementalDefinitionStack> entry : container.map.entrySet()) {
            test &= removeAmount(true, entry.getValue());
        }
        if (testOnly || !test) {
            return test;
        }
        for (Map.Entry<iElementalDefinition, cElementalDefinitionStack> entry : container.map.entrySet()) {
            removeAmount(false, entry.getValue());
        }
        return true;
    }

    public boolean removeAllAmounts(boolean testOnly, cElementalInstanceStackMap container) {
        boolean test=true;
        for (Map.Entry<iElementalDefinition, cElementalInstanceStack> entry : container.map.entrySet()) {
            test &= removeAmount(true, entry.getValue());
        }
        if (testOnly || !test) {
            return test;
        }
        for (Map.Entry<iElementalDefinition, cElementalInstanceStack> entry : container.map.entrySet()) {
            test &= removeAmount(false, entry.getValue());
        }
        return true;
    }

    //Remove overflow
    public float removeOverflow(int stacksCount, long stackCapacity) {
        float massRemoved = 0;

        if (map.size() > stacksCount) {
            iElementalDefinition[] keys = keys();
            for (int i = stacksCount; i < keys.length; i++) {
                massRemoved += map.get(keys[i]).getDefinitionStack().getMass();
                map.remove(keys[i]);
            }
        }

        for (cElementalInstanceStack instance : values()) {
            if (instance.amount > stackCapacity) {
                massRemoved += instance.definition.getMass() * (instance.amount - stackCapacity);
                instance.amount = stackCapacity;
            }
        }
        return massRemoved;
    }

    //Put replace
    public cElementalInstanceStack putReplace(cElementalInstanceStack instanceUnsafe) {
        return map.put(instanceUnsafe.definition, instanceUnsafe);
    }

    public void putReplaceAll(cElementalInstanceStack... instances) {
        for (cElementalInstanceStack instance : instances) {
            map.put(instance.definition, instance);
        }
    }

    private void putReplaceAll(Map<iElementalDefinition, cElementalInstanceStack> inTreeUnsafe) {
        map.putAll(inTreeUnsafe);
    }

    public void putReplaceAll(cElementalInstanceStackMap inContainerUnsafe) {
        putReplaceAll(inContainerUnsafe.map);
    }

    //Put unify
    public cElementalInstanceStack putUnify(cElementalInstanceStack instance) {
        cElementalInstanceStack stack=map.get(instance.definition);
        if(stack==null) {
            return map.put(instance.definition, instance);
        }
        return map.put(instance.definition, stack.unifyIntoThis(instance));
    }

    public void putUnifyAll(cElementalInstanceStack... instances) {
        for (cElementalInstanceStack instance : instances) {
            putUnify(instance);
        }
    }

    private void putUnifyAll(Map<iElementalDefinition, cElementalInstanceStack> inTreeUnsafe) {
        for (cElementalInstanceStack in : inTreeUnsafe.values()) {
            putUnify(in);
        }
    }

    public void putUnifyAll(cElementalInstanceStackMap containerUnsafe) {
        putUnifyAll(containerUnsafe.map);
    }

    //Getters
    public cElementalInstanceStack getFirst(){
        return map.firstEntry().getValue();
    }

    public cElementalInstanceStack getLast(){
        return map.lastEntry().getValue();
    }

    public cElementalInstanceStack getInstance(iElementalDefinition def) {
        return map.get(def);
    }

    public cElementalInstanceStack get(int i){
        Collection<cElementalInstanceStack> var = map.values();
        return var.toArray(new cElementalInstanceStack[var.size()])[i];
    }

    public String[] getElementalInfo() {
        String[] info = new String[map.size() * 4];
        int i = 0;
        for (cElementalInstanceStack instance : map.values()) {
            info[i] = EnumChatFormatting.BLUE + instance.definition.getName();
            info[i + 1] = EnumChatFormatting.AQUA + instance.definition.getSymbol();
            info[i + 2] = "Amount " + EnumChatFormatting.GREEN + instance.amount;
            info[i + 3] = "LifeTime " + EnumChatFormatting.GREEN + instance.getLifeTime();
            i += 4;
        }
        return info;
    }

    public ArrayList<String> getScanInfo(int[] capabilities) {
        ArrayList<String> list=new ArrayList<>(16);
        for(Map.Entry<iElementalDefinition,cElementalInstanceStack> e:map.entrySet()){
            e.getValue().addScanResults(list,capabilities);
        }
        return list;
    }

    public cElementalInstanceStack[] values() {
        Collection<cElementalInstanceStack> var = map.values();
        return var.toArray(new cElementalInstanceStack[var.size()]);
    }

    public iElementalDefinition[] keys() {
        Set<iElementalDefinition> var = map.keySet();
        return var.toArray(new iElementalDefinition[var.size()]);
    }

    public float getMass() {
        float mass = 0;
        for (cElementalInstanceStack stack : map.values()) {
            mass += stack.getMass();
        }
        return mass;
    }

    //Tests
    public boolean containsDefinition(iElementalDefinition def) {
        return map.containsKey(def);
    }

    public boolean containsInstance(cElementalInstanceStack inst) {
        return map.containsValue(inst);
    }

    public int size() {
        return map.size();
    }

    public boolean hasStacks() {
        return !map.isEmpty();
    }

    //Tick Content
    public void tickContentByOneSecond(float lifeTimeMult, int postEnergize) {
        tickContent(lifeTimeMult,postEnergize,1);
    }

    public void tickContent(float lifeTimeMult, int postEnergize, int seconds){
        for (cElementalInstanceStack instance : values()) {
            cElementalInstanceStackMap newInstances = instance.decay(lifeTimeMult, instance.age += seconds, postEnergize);
            if (newInstances == null) {
                instance.nextColor();
            } else {
                map.remove(instance.definition);
                for (cElementalInstanceStack newInstance : newInstances.values()) {
                    putUnify(newInstance);
                    newInstance.nextColor();
                }
            }
        }
    }

    //NBT
    public NBTTagCompound getInfoNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        String[] info = getElementalInfo();
        nbt.setInteger("i", info.length);
        for (int i = 0; i < info.length; i++) {
            nbt.setString(Integer.toString(i), info[i]);
        }
        return nbt;
    }

    public NBTTagCompound getScanInfoNBT(int[] capabilities) {
        NBTTagCompound nbt = new NBTTagCompound();
        ArrayList<String> info = getScanInfo(capabilities);
        nbt.setInteger("i", info.size());
        for (int i = 0; i < info.size(); i++) {
            nbt.setString(Integer.toString(i), info.get(i));
        }
        return nbt;
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("i", map.size());
        int i = 0;
        for (cElementalInstanceStack instance : map.values()) {
            nbt.setTag(Integer.toString(i++), instance.toNBT());
        }
        return nbt;
    }

    public static cElementalInstanceStackMap fromNBT(NBTTagCompound nbt) throws tElementalException {
        cElementalInstanceStack[] instances = new cElementalInstanceStack[nbt.getInteger("i")];
        for (int i = 0; i < instances.length; i++) {
            instances[i] = cElementalInstanceStack.fromNBT(nbt.getCompoundTag(Integer.toString(i)));
            if (instances[i].definition.equals(nbtE__)) {
                throw new tElementalException("Something went Wrong");
            }
        }
        return new cElementalInstanceStackMap(false, instances);
    }

    //stackUp
    public static cElementalInstanceStack[] stackUp(cElementalInstanceStack... in) {
        cElementalInstanceStackMap inTree = new cElementalInstanceStackMap();
        inTree.putUnifyAll(in);
        return inTree.values();
    }

    @Override
    public int compareTo(cElementalInstanceStackMap o) {
        int sizeDiff = map.size() - o.map.size();
        if (sizeDiff != 0) {
            return sizeDiff;
        }
        cElementalInstanceStack[] ofThis = values(), ofThat = o.values();
        for (int i = 0; i < ofThat.length; i++) {
            int result = ofThis[i].compareTo(ofThat[i]);
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof cElementalInstanceStackMap) {
            return compareTo((cElementalInstanceStackMap) obj) == 0;
        }
        if (obj instanceof cElementalStackMap) {
            return toDefinitionMapForComparison().compareTo((cElementalStackMap) obj) == 0;
        }
        return false;
    }

    @Override
    public int hashCode() {//Hash only definitions to compare contents not amounts or data
        int hash = -(map.size() << 4);
        for (cElementalInstanceStack stack : map.values()) {
            hash += stack.definition.hashCode();
        }
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder build=new StringBuilder("Instance Stack Map\n");
        for(cElementalInstanceStack stack:map.values()){
            build.append(stack.toString()).append('\n');
        }
        return build.toString();
    }

    public cElementalInstanceStackMap takeAll(){
        TreeMap<iElementalDefinition, cElementalInstanceStack> map=this.map;
        this.map=new TreeMap<>();
        return new cElementalInstanceStackMap(map);
    }

    public void cleanUp(){
        for(Map.Entry<iElementalDefinition, cElementalInstanceStack> entry:map.entrySet()){
            if(entry.getValue().amount<=0) {
                map.remove(entry.getKey());
            }
        }
    }
}
