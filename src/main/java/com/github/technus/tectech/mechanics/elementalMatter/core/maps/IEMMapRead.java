package com.github.technus.tectech.mechanics.elementalMatter.core.maps;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMDefinitionsRegistry;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.IEMStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.IEMDefinition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import java.lang.reflect.Array;
import java.util.*;

import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationRegistry.*;
import static com.github.technus.tectech.util.DoubleCount.ulpSigned;
import static net.minecraft.util.StatCollector.translateToLocal;

public interface IEMMapRead<T extends IEMStack> extends Comparable<IEMMapRead<?>>, Cloneable {
    NavigableMap<IEMDefinition,T> getBackingMap();

    IEMMapRead<T> clone();

    default Set<Map.Entry<IEMDefinition, T>> entrySet(){
        return getBackingMap().entrySet();
    }

    default Set<IEMDefinition> keySet(){
        return getBackingMap().keySet();
    }

    default IEMDefinition[] keySetToArray() {
        return keySetToArray(new IEMDefinition[size()]);
    }

    default IEMDefinition[] keySetToArray(IEMDefinition[] array) {
        return keySet().toArray(array);
    }

    default Collection<T> values(){
        return getBackingMap().values();
    }

    @SuppressWarnings("unchecked")
    default T[] valuesToArray(){
        return valuesToArray((T[]) Array.newInstance(getType(),size()));
    }

    default T[] valuesToArray(T[] array){
        return values().toArray(array);
    }

    Class<T> getType();

    //Getters
    default T getFirst(){
        return getBackingMap().firstEntry().getValue();
    }

    default T getLast(){
        return getBackingMap().lastEntry().getValue();
    }

    default T get(IEMStack stack) {
        return get(stack.getDefinition());
    }

    default T get(IEMDefinition def) {
        return getBackingMap().get(def);
    }

    default T getNaturallySorted(int pos) {
        if(pos<0 || pos>=size()){
            throw new IndexOutOfBoundsException("Index was: "+pos+" size was: "+size());
        }
        for (Map.Entry<IEMDefinition, T> entry : entrySet()) {
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
            info[i] = EnumChatFormatting.BLUE + defStack.getDefinition().getLocalizedName();
            info[i + 1] = EnumChatFormatting.AQUA + defStack.getDefinition().getSymbol();
            info[i + 2] = "Amount " + EnumChatFormatting.GREEN + defStack.getAmount()/AVOGADRO_CONSTANT+" "+translateToLocal("tt.keyword.mol");
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

    default NBTTagCompound toNBT(EMDefinitionsRegistry registry) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("i", size());
        int i = 0;
        for (Map.Entry<IEMDefinition, T> entry : entrySet()) {
            nbt.setTag(Integer.toString(i++), entry.getValue().toNBT(registry));
        }
        return nbt;
    }

    @Override
    default int compareTo(IEMMapRead<? extends IEMStack> o) {//this actually compares rest
        int sizeDiff = size() - o.size();
        if (sizeDiff != 0) {
            return sizeDiff;
        }

        Iterator<T>                  iterator  = values().iterator();
        Iterator<? extends IEMStack> iteratorO = o.values().iterator();

        while (iterator.hasNext()) {
            int result = iterator.next().compareTo(iteratorO.next());
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }

    /**
     * use only for nested operations!
     * @param o
     * @return
     */
    default int compareWithAmountsInternal(IEMMapRead<? extends IEMStack> o) {
        if (o == null) {
            return 1;
        }

        int lenDiff = size() - o.size();
        if (lenDiff != 0) {
            return lenDiff;
        }

        Iterator<Map.Entry<IEMDefinition, T>>                            iterator  = entrySet().iterator();
        Iterator<? extends Map.Entry<IEMDefinition, ? extends IEMStack>> iteratorO = o.entrySet().iterator();

        while (iterator.hasNext()) {
            T        first  = iterator.next().getValue();
            IEMStack second = iteratorO.next().getValue();
            int      result = first.compareTo(second);
            if (result != 0) {
                return result;
            }
            result=Double.compare(first.getAmount(),second.getAmount());
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }

    default double getMass(){
        double mass=0;
        for (Map.Entry<IEMDefinition, T> entry : entrySet()) {
            mass+=entry.getValue().getMass();
        }
        return mass;
    }

    default long getCharge(){
        long charge=0;
        for (Map.Entry<IEMDefinition, T> entry : entrySet()) {
            charge+=entry.getValue().getCharge();
        }
        return charge;
    }

    //Tests
    default boolean containsKey(IEMDefinition def) {
        return getBackingMap().containsKey(def);
    }

    default boolean containsKey(IEMStack def) {
        return containsKey(def.getDefinition());
    }

    default boolean containsAllKeys(IEMDefinition... definitions) {
        for (IEMDefinition def : definitions) {
            if (!containsKey(def)) {
                return false;
            }
        }
        return true;
    }

    default boolean containsAllKeys(IEMStack... hasElementalDefinition) {
        for (IEMStack has : hasElementalDefinition) {
            if (!containsKey(has)) {
                return false;
            }
        }
        return true;
    }

    default boolean containsAmountExact(IEMDefinition def, double amount) {
        T target = getBackingMap().get(def);
        return target != null && target.getAmount() >= amount;
    }

    default boolean containsAmountExact(IEMStack stack) {
        return containsAmountExact(stack.getDefinition(),stack.getAmount());
    }

    default boolean containsAllAmountsExact(IEMStack... stacks) {
        for (IEMStack stack : stacks) {
            if(!containsAmountExact(stack)){
                return false;
            }
        }
        return true;
    }

    default boolean containsAllAmountsExact(IEMMapRead<? extends IEMStack> container) {
        for (Map.Entry<IEMDefinition, ? extends IEMStack> entry : container.entrySet()) {
            if(!containsAmountExact(entry.getValue())){
                return false;
            }
        }
        return true;
    }

    default boolean containsAmount(IEMDefinition def, double amountToConsume) {
        double amountRequired=amountToConsume- EM_COUNT_EPSILON;
        if(amountRequired==amountToConsume){
            amountRequired-=ulpSigned(amountRequired);
        }
        return containsAmountExact(def,amountRequired);
    }

    default boolean containsAmount(IEMStack stack) {
        return containsAmount(stack.getDefinition(),stack.getAmount());
    }

    default boolean containsAllAmounts(IEMStack... stacks) {
        for (IEMStack stack : stacks) {
            if(!containsAmount(stack)){
                return false;
            }
        }
        return true;
    }

    default boolean containsAllAmounts(IEMMapRead<? extends IEMStack> container) {
        for (Map.Entry<IEMDefinition, ? extends IEMStack> entry : container.entrySet()) {
            if(!containsAmount(entry.getValue())){
                return false;
            }
        }
        return true;
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
