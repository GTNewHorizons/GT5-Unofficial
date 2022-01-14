package com.github.technus.tectech.mechanics.elementalMatter.core.maps;

import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.iElementalStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.templates.iElementalDefinition;

import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Created by Tec on 12.05.2017.
 */
abstract class cElementalStackMap<T extends iElementalStack> implements iElementalMapR<T> {
    protected NavigableMap<iElementalDefinition, T> map;

    protected cElementalStackMap() {
        this(new TreeMap<>());
    }

    protected cElementalStackMap(NavigableMap<iElementalDefinition, T> map) {
        this.map = map;
    }

    @Override
    public NavigableMap<iElementalDefinition, T> getBackingMap() {
        return map;
    }

    @Override
    public abstract cElementalStackMap<T> clone();

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof cElementalInstanceStackMap) {
            return compareTo(((cElementalInstanceStackMap) obj).toDefinitionMapForComparison()) == 0;
        }
        if (obj instanceof cElementalStackMap) {
            return compareTo((cElementalStackMap) obj) == 0;
        }
        return false;
    }

    @Override
    public int hashCode() {//Hash only definitions to compare contents not amounts or data
        int hash = -(map.size() << 4);
        for (T stack : map.values()) {
            hash += stack.getDefinition().hashCode();
        }
        return hash;
    }
}
