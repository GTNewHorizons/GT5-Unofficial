package com.github.technus.tectech.mechanics.elementalMatter.core.maps;

import com.github.technus.tectech.mechanics.elementalMatter.core.decay.cElementalDecayResult;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.cElementalDefinitionStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.cElementalInstanceStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.tElementalException;
import com.github.technus.tectech.mechanics.elementalMatter.core.templates.iElementalDefinition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.bTransformationInfo.AVOGADRO_CONSTANT;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.cPrimitiveDefinition.nbtE__;
import static com.github.technus.tectech.util.DoubleCount.add;

/**
 * Created by danie_000 on 22.01.2017.
 */
public final class cElementalInstanceStackMap extends cElementalStackMap<cElementalInstanceStack> implements iElementalMapRW<cElementalInstanceStack> {
    //Constructors
    public cElementalInstanceStackMap() {}

    public cElementalInstanceStackMap(cElementalInstanceStack... inSafe) {
        this(true, inSafe);
    }

    public cElementalInstanceStackMap(boolean clone, cElementalInstanceStack... in) {
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

    private cElementalInstanceStackMap(NavigableMap<iElementalDefinition, cElementalInstanceStack> inSafe) {
        this(true, inSafe);
    }

    private cElementalInstanceStackMap(boolean clone, NavigableMap<iElementalDefinition, cElementalInstanceStack> in) {
        if (clone) {
            map = new TreeMap<>();
            for(cElementalInstanceStack stack:in.values()) {
                putUnify(stack.clone());
            }
        } else {
            map = in;
        }
    }

    @Override
    public Class<cElementalInstanceStack> getType() {
        return cElementalInstanceStack.class;
    }

    @Override
    public cElementalInstanceStackMap clone() {
        return new cElementalInstanceStackMap(map);
    }

    //Remove overflow
    public double removeOverflow(int stacksCount, double stackCapacity) {
        double massRemoved = 0;

        if (map.size() > stacksCount) {
            iElementalDefinition[] keys = keySetToArray();
            for (int i = stacksCount; i < keys.length; i++) {
                massRemoved += map.get(keys[i]).getDefinitionStack().getMass();
                map.remove(keys[i]);
            }
        }

        for (cElementalInstanceStack instance : valuesToArray()) {
            if (instance.amount > stackCapacity) {
                massRemoved += instance.definition.getMass() * (instance.amount - stackCapacity);
                instance.amount = stackCapacity;
            }
        }
        return massRemoved;
    }

    //Getters
    public String[] getElementalInfo() {
        String[] info = new String[map.size()];
        int i = 0;
        for (cElementalInstanceStack instance : map.values()) {
            info[i++] = EnumChatFormatting.BLUE + instance.definition.getName()+
                    " "+ EnumChatFormatting.AQUA + instance.definition.getSymbol()+ EnumChatFormatting.RESET+
                    " #: " + EnumChatFormatting.GREEN + String.format("%.3E",instance.amount/ AVOGADRO_CONSTANT) +" mol"+ EnumChatFormatting.RESET+
                    " E: " + EnumChatFormatting.GREEN + instance.getEnergy() + EnumChatFormatting.RESET+
                    " T: " + EnumChatFormatting.GREEN + (instance.getLifeTime()<0?"STABLE":String.format("%.3E",instance.getLifeTime()));
        }
        return info;
    }

    public ArrayList<String> getScanShortSymbols(int[] capabilities) {
        ArrayList<String> list=new ArrayList<>(16);
        for(Map.Entry<iElementalDefinition,cElementalInstanceStack> e:map.entrySet()){
            e.getValue().addScanShortSymbols(list,capabilities);
        }
        return list;
    }

    public ArrayList<String> getScanInfo(int[] capabilities) {
        ArrayList<String> list=new ArrayList<>(16);
        for(Map.Entry<iElementalDefinition,cElementalInstanceStack> e:map.entrySet()){
            e.getValue().addScanResults(list,capabilities);
        }
        return list;
    }

    //Tick Content
    public double tickContentByOneSecond(double lifeTimeMult, int postEnergize) {
        return tickContent(lifeTimeMult,postEnergize,1D);
    }

    public double tickContent(double lifeTimeMult, int postEnergize, double seconds){
        cleanUp();
        double diff=0;
        for (cElementalInstanceStack instance : valuesToArray()) {
            cElementalDecayResult newInstances = instance.decay(lifeTimeMult, instance.age += seconds, postEnergize);
            if (newInstances == null) {
                instance.nextColor();
            } else {
                diff=add(diff,newInstances.getMassDiff());
                removeAmount(false,instance);
                putUnifyAll(newInstances.getOutput());
            }
        }
        return diff;
    }

    //NBT
    public NBTTagCompound getScanShortSymbolsNBT(int[] capabilities) {
        NBTTagCompound nbt = new NBTTagCompound();
        ArrayList<String> info = getScanShortSymbols(capabilities);
        nbt.setInteger("i", info.size());
        for (int i = 0; i < info.size(); i++) {
            nbt.setString(Integer.toString(i), info.get(i));
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
        return inTree.valuesToArray();
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
    public String toString() {
        StringBuilder build=new StringBuilder("Instance Stack Map\n");
        for(cElementalInstanceStack stack:map.values()){
            build.append(stack.toString()).append('\n');
        }
        return build.toString();
    }

    public cElementalInstanceStackMap takeAll(){
        cElementalInstanceStackMap newStack=new cElementalInstanceStackMap(false,new TreeMap<>(this.map));//just in case to uncouple The map
        this.map.clear();
        return newStack;
    }

    public cElementalDefinitionStackMap toDefinitionMapForComparison() {
        cElementalDefinitionStack[] list = new cElementalDefinitionStack[size()];
        int                         i    = 0;
        for (Map.Entry<iElementalDefinition, cElementalInstanceStack> entry : entrySet()) {
            cElementalInstanceStack value = entry.getValue();
            list[i++] = new cElementalDefinitionStack(value.getDefinition(), value.getAmount());
        }
        return new cElementalDefinitionStackMap(list);
    }
}
