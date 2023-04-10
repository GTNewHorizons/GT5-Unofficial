package com.github.technus.tectech.mechanics.elementalMatter.core.maps;

import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationRegistry.AVOGADRO_CONSTANT;
import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationRegistry.EM_COUNT_EPSILON;
import static com.github.technus.tectech.util.DoubleCount.ulpSigned;
import static com.github.technus.tectech.util.TT_Utility.packNBT;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.IEMDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMDefinitionsRegistry;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.IEMStack;
import com.github.technus.tectech.util.TT_Utility;

public interface IEMMapRead<T extends IEMStack> extends Comparable<IEMMapRead<? extends IEMStack>>, Cloneable {

    NavigableMap<IEMDefinition, T> getBackingMap();

    IEMMapRead<T> clone();

    default Set<Map.Entry<IEMDefinition, T>> entrySet() {
        return getBackingMap().entrySet();
    }

    default Set<IEMDefinition> keySet() {
        return getBackingMap().keySet();
    }

    default IEMDefinition[] keySetToArray() {
        return keySetToArray(new IEMDefinition[size()]);
    }

    default IEMDefinition[] keySetToArray(IEMDefinition[] array) {
        return keySet().toArray(array);
    }

    default Collection<T> values() {
        return getBackingMap().values();
    }

    @SuppressWarnings("unchecked")
    default T[] valuesToArray() {
        return valuesToArray((T[]) Array.newInstance(getType(), size()));
    }

    default T[] valuesToArray(T[] array) {
        return values().toArray(array);
    }

    Class<T> getType();

    // Getters
    default T getFirst() {
        return getBackingMap().firstEntry().getValue();
    }

    default T getLast() {
        return getBackingMap().lastEntry().getValue();
    }

    default T get(IEMStack stack) {
        return get(stack.getDefinition());
    }

    default T get(IEMDefinition def) {
        return getBackingMap().get(def);
    }

    default T getNaturallySorted(int pos) {
        if (pos < 0 || pos >= size()) {
            throw new IndexOutOfBoundsException("Index was: " + pos + " size was: " + size());
        }
        for (Map.Entry<IEMDefinition, T> entry : entrySet()) {
            if (pos == 0) {
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
        for (Map.Entry<IEMDefinition, T> instance : entrySet()) {
            info[i++] = instance.getValue().getDefinition().getShortSymbol();
        }
        return info;
    }

    default String[] getElementalInfo() {
        String[] info = new String[size()];
        int i = 0;
        for (Map.Entry<IEMDefinition, T> entry : entrySet()) {
            T instance = entry.getValue();
            info[i++] = EnumChatFormatting.BLUE + instance.getDefinition().getLocalizedName()
                    + " "
                    + EnumChatFormatting.AQUA
                    + instance.getDefinition().getSymbol()
                    + EnumChatFormatting.RESET
                    + " "
                    + translateToLocal("tt.keyword.short.amount")
                    + ": "
                    + EnumChatFormatting.GREEN
                    + TT_Utility.formatNumberExp(instance.getAmount() / AVOGADRO_CONSTANT)
                    + " "
                    + translateToLocal("tt.keyword.unit.mol")
                    + EnumChatFormatting.RESET
                    + " "
                    + translateToLocal("tt.keyword.short.charge")
                    + ": "
                    + EnumChatFormatting.GREEN
                    + TT_Utility.formatNumberExp(instance.getCharge())
                    + " "
                    + translateToLocal("tt.keyword.unit.charge")
                    + EnumChatFormatting.RESET;
        }
        return info;
    }

    // NBT
    default NBTTagCompound toNBT(EMDefinitionsRegistry registry) {
        return packNBT(t -> t.toNBT(registry), valuesToArray());
    }

    @Override
    default int compareTo(IEMMapRead<? extends IEMStack> o) { // this actually compares rest
        int sizeDiff = size() - o.size();
        if (sizeDiff != 0) {
            return sizeDiff;
        }

        Iterator<Map.Entry<IEMDefinition, T>> iterator = entrySet().iterator();
        Iterator<? extends Map.Entry<IEMDefinition, ? extends IEMStack>> iteratorO = o.entrySet().iterator();

        while (iterator.hasNext()) {
            int result = iterator.next().getValue().compareTo(iteratorO.next().getValue());
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }

    /**
     * use only for nested operations!
     * 
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

        Iterator<Map.Entry<IEMDefinition, T>> iterator = entrySet().iterator();
        Iterator<? extends Map.Entry<IEMDefinition, ? extends IEMStack>> iteratorO = o.entrySet().iterator();

        while (iterator.hasNext()) {
            T first = iterator.next().getValue();
            IEMStack second = iteratorO.next().getValue();
            int result = first.compareTo(second);
            if (result != 0) {
                return result;
            }
            result = Double.compare(first.getAmount(), second.getAmount());
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }

    default double getMass() {
        double mass = 0;
        for (Map.Entry<IEMDefinition, T> entry : entrySet()) {
            mass += entry.getValue().getMass();
        }
        return mass;
    }

    default long getCharge() {
        long charge = 0;
        for (Map.Entry<IEMDefinition, T> entry : entrySet()) {
            charge += entry.getValue().getCharge();
        }
        return charge;
    }

    // Tests
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
        T target = get(def);
        return target != null && target.getAmount() >= amount;
    }

    default boolean containsAmountExact(IEMStack stack) {
        return containsAmountExact(stack.getDefinition(), stack.getAmount());
    }

    default boolean containsAllAmountsExact(IEMStack... stacks) {
        for (IEMStack stack : stacks) {
            if (!containsAmountExact(stack)) {
                return false;
            }
        }
        return true;
    }

    default boolean containsAllAmountsExact(IEMMapRead<? extends IEMStack> container) {
        for (Map.Entry<IEMDefinition, ? extends IEMStack> entry : container.entrySet()) {
            if (!containsAmountExact(entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    default boolean containsAmount(IEMDefinition def, double amountToConsume) {
        double amountRequired = amountToConsume - EM_COUNT_EPSILON;
        if (amountRequired == amountToConsume) {
            amountRequired -= ulpSigned(amountRequired);
        }
        return containsAmountExact(def, amountRequired);
    }

    default boolean containsAmount(IEMStack stack) {
        return containsAmount(stack.getDefinition(), stack.getAmount());
    }

    default boolean containsAllAmounts(IEMStack... stacks) {
        for (IEMStack stack : stacks) {
            if (!containsAmount(stack)) {
                return false;
            }
        }
        return true;
    }

    default boolean containsAllAmounts(IEMMapRead<? extends IEMStack> container) {
        for (Map.Entry<IEMDefinition, ? extends IEMStack> entry : container.entrySet()) {
            if (!containsAmount(entry.getValue())) {
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

    default boolean isEmpty() {
        return getBackingMap().isEmpty();
    }
}
