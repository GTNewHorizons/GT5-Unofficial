package tectech.thing.metaTileEntity.multi.base.parameter;

import java.util.function.Supplier;

import com.cleanroommc.modularui.value.sync.SyncHandler;

public abstract class NumericParameter<T extends Number, S extends SyncHandler<?>> extends Parameter<T, S> {

    private final Supplier<T> min;
    private final Supplier<T> max;

    public NumericParameter(T value, String langKey, String nbtKey, Supplier<T> min, Supplier<T> max,
        Object... langArgs) {
        super(value, langKey, nbtKey, langArgs);
        this.min = min;
        this.max = max;
    }

    @Override
    public void setValue(T value) {
        super.setValue(validate(value));
    }

    public T getMin() {
        return min.get();
    }

    public T getMax() {
        return max.get();
    }

    public abstract T validate(T value);
}
