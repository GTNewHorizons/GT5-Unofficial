package gregtech.api.objects.iterators;

import javax.annotation.Nullable;

public interface Generator<T> {

    /**
     * Returns the next value in a sequence.
     * 
     * @return The value. Null is the 'stop' sentinel value.
     */
    @Nullable
    T next();
}
