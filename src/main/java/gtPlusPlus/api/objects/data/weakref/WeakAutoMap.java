package gtPlusPlus.api.objects.data.weakref;

import gtPlusPlus.api.objects.data.AutoMap;
import java.util.WeakHashMap;

public class WeakAutoMap<T> extends AutoMap<T> {
    private static final long serialVersionUID = 8328345351801363386L;

    public WeakAutoMap() {
        super(new WeakHashMap<Integer, T>());
    }
}
