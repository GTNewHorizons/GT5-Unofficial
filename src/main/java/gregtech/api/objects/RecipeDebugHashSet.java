package gregtech.api.objects;

import gregtech.api.util.GT_Recipe;

import java.util.Collection;
import java.util.Objects;

public class RecipeDebugHashSet extends SynchronisedHashSet<GT_Recipe> {


    private GT_Recipe.GT_Recipe_Map map;

    public GT_Recipe.GT_Recipe_Map getMap() {
        return map;
    }

    public void setMap(GT_Recipe.GT_Recipe_Map map) {
        this.map = map;
    }

    public RecipeDebugHashSet(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public RecipeDebugHashSet(int initialCapacity) {
        super(initialCapacity);
    }

    public RecipeDebugHashSet() {
        super();
    }

    public RecipeDebugHashSet(Collection<? extends GT_Recipe> c) {
        super(c);
    }

    @Override
    public synchronized boolean add(GT_Recipe v) {
        map.checkMinimals(v);
        return super.add(v);
    }

    @Override
    public synchronized boolean addAll(Collection<? extends GT_Recipe> c) {
        for (GT_Recipe v : c) {
            map.checkMinimals(v);
        }
        return super.addAll(c);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RecipeDebugHashSet that = (RecipeDebugHashSet) o;
        return Objects.equals(map, that.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), map);
    }
}
