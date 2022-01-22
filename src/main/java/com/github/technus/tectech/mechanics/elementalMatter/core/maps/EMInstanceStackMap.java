package com.github.technus.tectech.mechanics.elementalMatter.core.maps;

import com.github.technus.tectech.mechanics.elementalMatter.core.EMException;
import com.github.technus.tectech.mechanics.elementalMatter.core.decay.EMDecayResult;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.IEMDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMDefinitionsRegistry;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMDefinitionStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMInstanceStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.IEMStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationRegistry.AVOGADRO_CONSTANT;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMPrimitiveDefinition.nbtE__;
import static com.github.technus.tectech.util.DoubleCount.add;
import static net.minecraft.util.StatCollector.translateToLocal;

/**
 * Created by danie_000 on 22.01.2017.
 */
public final class EMInstanceStackMap extends EMStackMap<EMInstanceStack> implements IEMMapWrite<EMInstanceStack> {
    //Constructors
    public EMInstanceStackMap() {}

    public EMInstanceStackMap(EMInstanceStack... inSafe) {
        this(true, inSafe);
    }

    public EMInstanceStackMap(boolean clone, EMInstanceStack... in) {
        if (clone) {
            EMInstanceStack[] stacks =new EMInstanceStack[in.length];
            for(int i=0;i<stacks.length;i++) {
                stacks[i] = in[i].clone();
            }
            putUnifyAll(stacks);
        } else {
            putUnifyAll(in);
        }
    }

    private EMInstanceStackMap(NavigableMap<IEMDefinition, EMInstanceStack> inSafe) {
        this(true, inSafe);
    }

    private EMInstanceStackMap(boolean clone, NavigableMap<IEMDefinition, EMInstanceStack> in) {
        super(clone?new TreeMap<>():in);
        if (clone) {
            for(EMInstanceStack stack:in.values()) {
                putUnify(stack.clone());
            }
        }
    }

    @Override
    public Class<EMInstanceStack> getType() {
        return EMInstanceStack.class;
    }

    @Override
    public EMInstanceStackMap clone() {
        return new EMInstanceStackMap(getBackingMap());
    }

    //Remove overflow
    public double removeOverflow(int stacksCount, double stackCapacity) {
        double massRemoved = 0;

        if (getBackingMap().size() > stacksCount) {
            IEMDefinition[] keys = keySetToArray();
            for (int i = stacksCount; i < keys.length; i++) {
                massRemoved += getBackingMap().get(keys[i]).getDefinitionStack().getMass();
                getBackingMap().remove(keys[i]);
            }
        }

        for (EMInstanceStack instance : valuesToArray()) {
            if (instance.getAmount() > stackCapacity) {
                massRemoved += instance.getDefinition().getMass() * (instance.getAmount() - stackCapacity);
                instance.setAmount(stackCapacity);
            }
        }
        return massRemoved;
    }

    //Getters
    public String[] getElementalInfo() {
        String[] info = new String[getBackingMap().size()];
        int i = 0;
        for (EMInstanceStack instance : getBackingMap().values()) {
            info[i++] = EnumChatFormatting.BLUE + instance.getDefinition().getLocalizedName()+
                    " "+ EnumChatFormatting.AQUA + instance.getDefinition().getSymbol()+ EnumChatFormatting.RESET+
                    " #: " + EnumChatFormatting.GREEN + String.format("%.3E", instance.getAmount() /AVOGADRO_CONSTANT) +" "+translateToLocal("tt.keyword.mol")+ EnumChatFormatting.RESET+
                    " E: " + EnumChatFormatting.GREEN + instance.getEnergy() + EnumChatFormatting.RESET+
                    " T: " + EnumChatFormatting.GREEN + (instance.getLifeTime()<0?"STABLE":String.format("%.3E",instance.getLifeTime()));
        }
        return info;
    }

    public ArrayList<String> getScanShortSymbols(int[] capabilities) {
        ArrayList<String> list=new ArrayList<>(16);
        for(Map.Entry<IEMDefinition, EMInstanceStack> e: getBackingMap().entrySet()){
            e.getValue().addScanShortSymbols(list,capabilities);
        }
        return list;
    }

    public ArrayList<String> getScanInfo(int[] capabilities) {
        ArrayList<String> list=new ArrayList<>(16);
        for(Map.Entry<IEMDefinition, EMInstanceStack> e: getBackingMap().entrySet()){
            e.getValue().addScanResults(list,capabilities);
        }
        return list;
    }

    //Tick Content
    public double tickContentByOneSecond(double lifeTimeMult, int postEnergize) {
        return tickContent(lifeTimeMult,postEnergize,1D);
    }

    public double tickContent(double lifeTimeMult, int postEnergize, double seconds){
        //cleanUp();
        double diff=0;
        for (EMInstanceStack instance : takeAllToArray()) {
            instance.setAge(instance.getAge() + seconds);
            EMDecayResult newInstances = instance.decay(lifeTimeMult, instance.getAge(), postEnergize);
            if (newInstances == null) {
                putUnify(instance);
            } else {
                diff=add(diff,newInstances.getMassDiff());
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

    public static EMInstanceStackMap fromNBT(EMDefinitionsRegistry registry, NBTTagCompound nbt) throws EMException {
        EMInstanceStack[] instances = new EMInstanceStack[nbt.getInteger("i")];
        for (int i = 0; i < instances.length; i++) {
            instances[i] = EMInstanceStack.fromNBT(registry,nbt.getCompoundTag(Integer.toString(i)));
            if (instances[i].getDefinition().equals(nbtE__)) {
                throw new EMException("Something went Wrong");
            }
        }
        return new EMInstanceStackMap(false, instances);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EMInstanceStackMap) {
            return compareTo((EMInstanceStackMap) obj) == 0;
        }
        if (obj instanceof EMStackMap) {
            return toDefinitionMapForComparison().compareTo((EMStackMap<?>) obj) == 0;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder build=new StringBuilder("Instance Stack Map\n");
        for(EMInstanceStack stack: getBackingMap().values()){
            build.append(stack.toString()).append('\n');
        }
        return build.toString();
    }

    public EMInstanceStack[] takeAllToArray(){
        EMInstanceStack[] newStack = valuesToArray();//just in case to uncouple The map
        this.getBackingMap().clear();
        return newStack;
    }

    public EMInstanceStackMap takeAll(){
        EMInstanceStackMap newStack =new EMInstanceStackMap(false,new TreeMap<>(this.getBackingMap()));//just in case to uncouple The map
        this.getBackingMap().clear();
        return newStack;
    }

    public EMDefinitionStackMap toDefinitionMapForComparison() {
        EMDefinitionStack[] list = new EMDefinitionStack[size()];
        int                 i    = 0;
        for (Map.Entry<IEMDefinition, EMInstanceStack> entry : entrySet()) {
            EMInstanceStack value = entry.getValue();
            list[i++] = new EMDefinitionStack(value.getDefinition(), value.getAmount());
        }
        return new EMDefinitionStackMap(list);
    }

    @Override
    public EMInstanceStack putUnify(EMInstanceStack stack) {
        EMInstanceStack target =get(stack.getDefinition());
        if(target==null) {
            putReplace(stack);
            return stack;
        }
        double newAmount = add(target.getAmount(), stack.getAmount());
        if (IEMStack.isValidAmount(newAmount)) {
            stack=target.unifyIntoThis(stack);
            putReplace(stack);
            return stack;
        }else {
            removeKey(stack);
            return null;
        }
    }

    @Override
    public EMInstanceStack putUnifyExact(EMInstanceStack stack) {
        EMInstanceStack target =get(stack.getDefinition());
        if(target==null) {
            putReplace(stack);
            return stack;
        }
        double newAmount = target.getAmount()+stack.getAmount();
        if (IEMStack.isValidAmount(newAmount)) {
            stack=target.unifyIntoThis(stack);
            putReplace(stack);
            return stack;
        }else {
            removeKey(stack);
            return null;
        }
    }
}
