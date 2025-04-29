package tectech.thing.metaTileEntity.multi.base;

import java.util.function.Supplier;

import net.minecraft.util.StatCollector;

public abstract class Parameter<T> {

    private String localizedName;
    protected T currentValue;
    private Supplier<T> minValue = null;
    private Supplier<T> maxValue = null;

    public Parameter(T value, String key) {
        this.currentValue = value;
        this.localizedName = StatCollector.translateToLocal(key);
    }

    public Parameter(T value, Supplier<T> minValue, Supplier<T> maxValue, String key) {
        this(value, key);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public T getValue() {
        return currentValue;
    }

    public T getMinValue() {
        return minValue.get();
    }

    public T getMaxValue() {
        return maxValue.get();
    }

    public String getValueString() {
        return currentValue.toString();
    }

    public String getLocalizedName() {
        return localizedName;
    }

    public void setValue(T value) {
        this.currentValue = value;
    }

    public static class IntegerParameter extends Parameter<Integer> {

        public IntegerParameter(Integer value, String key) {
            super(value, key);
        }

        public IntegerParameter(Integer value, Supplier<Integer> minValue, Supplier<Integer> maxValue, String key) {
            super(value, minValue, maxValue, key);
        }
    }

    public static class DoubleParameter extends Parameter<Double> {

        public DoubleParameter(Double value, String key) {
            super(value, key);
        }

        public DoubleParameter(Double value, Supplier<Double> minValue, Supplier<Double> maxValue, String key) {
            super(value, minValue, maxValue, key);
        }
    }

    public static class StringParameter extends Parameter<String> {

        public StringParameter(String value, String key) {
            super(value, key);
        }
    }

    public static class BooleanParameter extends Parameter<Boolean> {

        public BooleanParameter(Boolean value, String key) {
            super(value, key);
        }

        public void invert() {
            this.currentValue = !this.currentValue;
        }
    }

}
