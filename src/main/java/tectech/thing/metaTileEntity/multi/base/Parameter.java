package tectech.thing.metaTileEntity.multi.base;

import java.util.function.Supplier;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

public abstract class Parameter<T> {

    protected String key;
    Object[] args;
    protected T currentValue;
    private Supplier<T> minValue = null;
    private Supplier<T> maxValue = null;
    public boolean show = true;

    public Parameter(T value, String key) {
        this.currentValue = value;
        this.key = key;
    }

    public Parameter(T value, String key, Object... args) {
        this.currentValue = value;
        this.key = key;
        this.args = args;
    }

    public Parameter(T value, Supplier<T> minValue, Supplier<T> maxValue, String key) {
        this(value, key);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public Parameter(T value, Supplier<T> minValue, Supplier<T> maxValue, String key, Object... args) {
        this(value, key, args);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public void saveNBT(NBTTagCompound tag, int i) {}

    public void loadNBT(NBTTagCompound tag, int i) {}

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
        return StatCollector.translateToLocalFormatted(key, args);
    }

    public void setValue(T value) {
        this.currentValue = value;
    }

    public Parameter<T> dontShow() {
        this.show = false;
        return this;
    }

    public static class IntegerParameter extends Parameter<Integer> {

        @Override
        public void saveNBT(NBTTagCompound tag, int i) {
            tag.setInteger(String.valueOf(i), this.getValue());
        }

        @Override
        public void loadNBT(NBTTagCompound tag, int i) {
            this.setValue(tag.getInteger(String.valueOf(i)));
        }

        public IntegerParameter(Integer value, String key) {
            super(value, key);
        }

        public IntegerParameter(Integer value, Supplier<Integer> minValue, Supplier<Integer> maxValue, String key) {
            super(value, minValue, maxValue, key);
        }

        public IntegerParameter(Integer value, Supplier<Integer> minValue, Supplier<Integer> maxValue, String key,
            Object... args) {
            super(value, minValue, maxValue, key, args);
        }

        @Override
        public IntegerParameter dontShow() {
            super.dontShow();
            return this;
        }
    }

    public static class DoubleParameter extends Parameter<Double> {

        @Override
        public void saveNBT(NBTTagCompound tag, int i) {
            tag.setDouble(String.valueOf(i), this.getValue());
        }

        @Override
        public void loadNBT(NBTTagCompound tag, int i) {
            this.setValue(tag.getDouble(String.valueOf(i)));
        }

        public DoubleParameter(Double value, String key) {
            super(value, key);
        }

        public DoubleParameter(Double value, Supplier<Double> minValue, Supplier<Double> maxValue, String key) {
            super(value, minValue, maxValue, key);
        }

        public DoubleParameter(Double value, Supplier<Double> minValue, Supplier<Double> maxValue, String key,
            Object... args) {
            super(value, minValue, maxValue, key, args);
        }

        @Override
        public DoubleParameter dontShow() {
            super.dontShow();
            return this;
        }
    }

    public static class StringParameter extends Parameter<String> {

        @Override
        public void saveNBT(NBTTagCompound tag, int i) {
            tag.setString(String.valueOf(i), this.getValue());
        }

        @Override
        public void loadNBT(NBTTagCompound tag, int i) {
            this.setValue(tag.getString(String.valueOf(i)));
        }

        public StringParameter(String value, String key) {
            super(value, key);
        }

        @Override
        public StringParameter dontShow() {
            super.dontShow();
            return this;
        }
    }

    public static class BooleanParameter extends Parameter<Boolean> {

        @Override
        public void saveNBT(NBTTagCompound tag, int i) {
            tag.setBoolean(String.valueOf(i), this.getValue());
        }

        @Override
        public void loadNBT(NBTTagCompound tag, int i) {
            this.setValue(tag.getBoolean(String.valueOf(i)));
        }

        public BooleanParameter(Boolean value, String key) {
            super(value, key);
        }

        public void invert() {
            this.currentValue = !this.currentValue;
        }

        @Override
        public BooleanParameter dontShow() {
            super.dontShow();
            return this;
        }
    }

}
