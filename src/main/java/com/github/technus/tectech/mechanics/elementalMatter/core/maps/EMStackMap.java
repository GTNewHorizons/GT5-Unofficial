package com.github.technus.tectech.mechanics.elementalMatter.core.maps;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.IEMDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.IEMStack;

/**
 * Created by Tec on 12.05.2017.
 */
abstract class EMStackMap<T extends IEMStack> implements IEMMapRead<T> {

    private final NavigableMap<IEMDefinition, T> backingMap;

    protected EMStackMap() {
        this(new TreeMap<>());
    }

    protected EMStackMap(NavigableMap<IEMDefinition, T> map) {
        this.backingMap = map;
    }

    @Override
    public NavigableMap<IEMDefinition, T> getBackingMap() {
        return backingMap;
    }

    @Override
    public abstract EMStackMap<T> clone();

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof IEMMapRead) {
            return compareTo((IEMMapRead<? extends IEMStack>) obj) == 0;
        }
        return false;
    }

    @Override
    public int hashCode() { // Hash only definitions to compare contents not amounts or data
        int hash = -(size() << 4);
        for (Map.Entry<IEMDefinition, T> entry : entrySet()) {
            hash += entry.getValue().getDefinition().hashCode();
        }
        return hash;
    }

    @Override
    public int compareTo(IEMMapRead<? extends IEMStack> o) {
        return IEMMapRead.super.compareTo(o);
    }
}
