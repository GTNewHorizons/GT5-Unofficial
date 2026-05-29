package tectech.thing.metaTileEntity.multi.base.parameter;

import java.util.function.Supplier;

public abstract class NumericParameter<T extends Number> extends Parameter<T> {

    public NumericParameter(T value, String langKey, String nbtKey, Supplier<T> min, Supplier<T> max,
        Object... langArgs) {
        super(value, langKey, nbtKey, min, max, langArgs);
    }

    @Override
    public void setValue(T value) {
        if (value.doubleValue() < this.getMin()
            .doubleValue()) {
            super.setValue(this.getMin());
        } else if (value.doubleValue() > this.getMax()
            .doubleValue()) {
                super.setValue(this.getMax());
            } else {
                super.setValue(value);
            }
    }
}
