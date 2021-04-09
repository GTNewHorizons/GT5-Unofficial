package gregtech.api.objects;

import gregtech.api.util.GT_Recipe;
import lombok.*;

import java.util.Collection;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RecipeDebugHashSet extends SynchronisedHashSet<GT_Recipe> {

    @Getter @Setter
    private GT_Recipe.GT_Recipe_Map map;

    public RecipeDebugHashSet(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public RecipeDebugHashSet(int initialCapacity) {
        super(initialCapacity);
    }

    public RecipeDebugHashSet(Collection<? extends GT_Recipe> c) {
        super(c);
    }

    @Override
    @Synchronized
    public boolean add(GT_Recipe v) {
        map.checkMinimals(v);
        return super.add(v);
    }

    @Override
    @Synchronized
    public boolean addAll(Collection<? extends GT_Recipe> c) {
        for (GT_Recipe v : c) {
            map.checkMinimals(v);
        }
        return super.addAll(c);
    }
}
