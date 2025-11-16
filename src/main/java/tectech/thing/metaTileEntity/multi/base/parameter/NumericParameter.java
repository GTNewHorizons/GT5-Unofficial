package tectech.thing.metaTileEntity.multi.base.parameter;

import java.util.function.Supplier;

public abstract class NumericParameter<T extends Number> extends Parameter<T> {

    public NumericParameter(T value, String langKey, Supplier<T> min, Supplier<T> max) {
        super(value, langKey, min, max);
    }

    @Override
    public void setValue(T value) {
        if (value.doubleValue() < min.get()
            .doubleValue()) {
            super.setValue(min.get());
        } else if (value.doubleValue() > max.get()
            .doubleValue()) {
                super.setValue(max.get());
            } else {
                super.setValue(value);
            }
    }
}
