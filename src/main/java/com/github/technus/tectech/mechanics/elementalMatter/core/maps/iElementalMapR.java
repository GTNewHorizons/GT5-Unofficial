package com.github.technus.tectech.mechanics.elementalMatter.core.maps;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.iElementalStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.templates.iElementalDefinition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import java.lang.reflect.Array;
import java.util.*;

import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.bTransformationInfo.AVOGADRO_CONSTANT;

public interface iElementalMapR<T extends iElementalStack> extends Comparable<iElementalMapR<? extends iElementalStack>>, Cloneable {
    NavigableMap<iElementalDefinition,T> getBackingMap();

    iElementalMapR<T> clone();

    default Set<Map.Entry<iElementalDefinition, T>> entrySet(){
        return getBackingMap().entrySet();
    }

    default Set<iElementalDefinition> keySet(){
        return getBackingMap().keySet();
    }

    default iElementalDefinition[] keySetToArray() {
        return keySetToArray(new iElementalDefinition[size()]);
    }

    default iElementalDefinition[] keySetToArray(iElementalDefinition[] array) {
        return getBackingMap().keySet().toArray(array);
    }

    default Collection<T> values(){
        return getBackingMap().values();
    }

    @SuppressWarnings("unchecked")
    default T[] valuesToArray(){
        return valuesToArray((T[]) Array.newInstance(getType(),size()));
    }

    default T[] valuesToArray(T[] array){
        return getBackingMap().values().toArray(array);
    }

    Class<T> getType();

    //Getters
    default T getFirst(){
        return getBackingMap().firstEntry().getValue();
    }

    default T getLast(){
        return getBackingMap().lastEntry().getValue();
    }

    default T get(iElementalDefinition def) {
        return getBackingMap().get(def);
    }

    default T getNaturallySorted(int pos) {
        if(pos<0 || pos>=size()){
            throw new IndexOutOfBoundsException("Index was: "+pos+" size was: "+size());
        }
        for (Map.Entry<iElementalDefinition, T> entry : entrySet()) {
            if(pos==0){
                return entry.getValue();
            }
            pos--;
        }
        return null;
    }

    default T getRandom() {
        return getNaturallySorted(TecTech.RANDOM.nextInt(size()));
    }

    default String[] getShortSymbolsInfo() {
        String[] info = new String[size()];
        int i = 0;
        for (T instance : values()) {
            info[i++] = instance.getDefinition().getShortSymbol();
        }
        return info;
    }

    default String[] getElementalInfo() {
        String[] info = new String[size() * 3];
        int i = 0;
        for (T defStack : values()) {
            info[i] = EnumChatFormatting.BLUE + defStack.getDefinition().getName();
            info[i + 1] = EnumChatFormatting.AQUA + defStack.getDefinition().getSymbol();
            info[i + 2] = "Amount " + EnumChatFormatting.GREEN + defStack.getAmount()/AVOGADRO_CONSTANT;
            i += 3;
        }
        return info;
    }

    //NBT
    default NBTTagCompound getShortSymbolsNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        String[] info = getShortSymbolsInfo();
        nbt.setInteger("i", info.length);
        for (int i = 0; i < info.length; i++) {
            nbt.setString(Integer.toString(i), info[i]);
        }
        return nbt;
    }

    default NBTTagCompound getInfoNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        String[] info = getElementalInfo();
        nbt.setInteger("i", info.length);
        for (int i = 0; i < info.length; i++) {
            nbt.setString(Integer.toString(i), info[i]);
        }
        return nbt;
    }

    default NBTTagCompound toNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("i", size());
        int i = 0;
        for (T stack : values()) {
            nbt.setTag(Integer.toString(i++), stack.toNBT());
        }
        return nbt;
    }

    @Override
    default int compareTo(iElementalMapR<? extends iElementalStack> o) {//this actually compares rest
        int sizeDiff = size() - o.size();
        if (sizeDiff != 0) {
            return sizeDiff;
        }

        Iterator<T> iterator = values().iterator();
        Iterator<? extends iElementalStack> iteratorO = o.values().iterator();

        while (iterator.hasNext()) {
            int result = iterator.next().compareTo(iteratorO.next());
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }

    default double getMass(){
        double mass=0;
        for (Map.Entry<iElementalDefinition, T> entry : getBackingMap().entrySet()) {
            mass+=entry.getValue().getMass();
        }
        return mass;
    }

    default long getCharge(){
        long charge=0;
        for (Map.Entry<iElementalDefinition, T> entry : getBackingMap().entrySet()) {
            charge+=entry.getValue().getCharge();
        }
        return charge;
    }

    //Tests
    default boolean containsKey(iElementalDefinition def) {
        return getBackingMap().containsKey(def);
    }

    default boolean containsKey(iElementalStack def) {
        return containsKey(def.getDefinition());
    }

    default int size() {
        return getBackingMap().size();
    }

    default boolean hasStacks() {
        return !isEmpty();
    }

    default boolean isEmpty(){
        return getBackingMap().isEmpty();
    }
}
