package gregtech.api.objects;

import gregtech.api.util.GT_Recipe;

import java.util.Collection;
import java.util.HashSet;

public class RecipeDebugHashSet extends HashSet<GT_Recipe> {

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
    public boolean add(GT_Recipe v) {
        map.checkMinimals(v);
        return super.add(v);
    }

    @Override
    public boolean addAll(Collection<? extends GT_Recipe> c) {
        for (GT_Recipe v : c) {
            map.checkMinimals(v);
        }
        return super.addAll(c);
    }
}
