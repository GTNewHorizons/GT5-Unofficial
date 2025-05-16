package tectech.thing.metaTileEntity.multi.base;

import java.util.function.Supplier;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

public abstract class Parameter<T> {

    public String key;
    Object[] args;
    protected T currentValue;
    private Supplier<T> minValue = null;
    private Supplier<T> maxValue = null;
    final String nbtKey;
    public boolean show = true;

    public Parameter(T value, String nbtKey, String key) {
        this.currentValue = value;
        this.nbtKey = nbtKey;
        this.key = key;
    }

    public Parameter(T value, String nbtKey, String key, Object... args) {
        this.currentValue = value;
        this.nbtKey = nbtKey;
        this.key = key;
        this.args = args;
    }

    public Parameter(T value, Supplier<T> minValue, Supplier<T> maxValue, String nbtKey, String key) {
        this(value, nbtKey, key);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public Parameter(T value, Supplier<T> minValue, Supplier<T> maxValue, String nbtKey, String key, Object... args) {
        this(value, nbtKey, key, args);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public void saveNBT(NBTTagCompound tag) {}

    public void loadNBT(NBTTagCompound tag) {}

    public void saveToParameterCard(NBTTagCompound tag) {}

    public void loadFromParameterCard(NBTTagCompound tag) {}

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
        public void saveNBT(NBTTagCompound tag) {
            tag.setInteger(this.nbtKey, this.getValue());
        }

        @Override
        public void loadNBT(NBTTagCompound tag) {
            if (!tag.hasKey(this.nbtKey)) return;
            this.setValue(tag.getInteger(this.nbtKey));
        }

        @Override
        public void saveToParameterCard(NBTTagCompound tag) {
            tag.setString("key", this.key);
            tag.setString("type", "integer");
            tag.setInteger("value", this.getValue());
        }

        @Override
        public void loadFromParameterCard(NBTTagCompound tag) {
            this.setValue(tag.getInteger("value"));
        }

        public IntegerParameter(Integer value, String nbtKey, String key) {
            super(value, nbtKey, key);
        }

        public IntegerParameter(Integer value, Supplier<Integer> minValue, Supplier<Integer> maxValue, String nbtKey,
            String key) {
            super(value, minValue, maxValue, nbtKey, key);
        }

        public IntegerParameter(Integer value, Supplier<Integer> minValue, Supplier<Integer> maxValue, String nbtKey,
            String key, Object... args) {
            super(value, minValue, maxValue, nbtKey, key, args);
        }

        @Override
        public IntegerParameter dontShow() {
            super.dontShow();
            return this;
        }
    }

    public static class DoubleParameter extends Parameter<Double> {

        @Override
        public void saveNBT(NBTTagCompound tag) {
            tag.setDouble(this.nbtKey, this.getValue());
        }

        @Override
        public void loadNBT(NBTTagCompound tag) {
            if (!tag.hasKey(this.nbtKey)) return;
            this.setValue(tag.getDouble(this.nbtKey));
        }

        @Override
        public void saveToParameterCard(NBTTagCompound tag) {
            tag.setString("key", this.key);
            tag.setString("type", "double");
            tag.setDouble("value", this.getValue());
        }

        @Override
        public void loadFromParameterCard(NBTTagCompound tag) {
            this.setValue(tag.getDouble("value"));
        }

        public DoubleParameter(Double value, String nbtKey, String key) {
            super(value, nbtKey, key);
        }

        public DoubleParameter(Double value, Supplier<Double> minValue, Supplier<Double> maxValue, String nbtKey,
            String key) {
            super(value, minValue, maxValue, nbtKey, key);
        }

        public DoubleParameter(Double value, Supplier<Double> minValue, Supplier<Double> maxValue, String nbtKey,
            String key, Object... args) {
            super(value, minValue, maxValue, nbtKey, key, args);
        }

        @Override
        public DoubleParameter dontShow() {
            super.dontShow();
            return this;
        }
    }

    public static class StringParameter extends Parameter<String> {

        @Override
        public void saveNBT(NBTTagCompound tag) {
            tag.setString(this.nbtKey, this.getValue());
        }

        @Override
        public void loadNBT(NBTTagCompound tag) {
            if (!tag.hasKey(this.nbtKey)) return;
            this.setValue(tag.getString(this.nbtKey));
        }

        @Override
        public void saveToParameterCard(NBTTagCompound tag) {
            tag.setString("key", this.key);
            tag.setString("type", "string");
            tag.setString("value", this.getValue());
        }

        @Override
        public void loadFromParameterCard(NBTTagCompound tag) {
            this.setValue(tag.getString("value"));
        }

        public StringParameter(String value, String nbtKey, String key) {
            super(value, nbtKey, key);
        }

        @Override
        public StringParameter dontShow() {
            super.dontShow();
            return this;
        }
    }

    public static class BooleanParameter extends Parameter<Boolean> {

        @Override
        public void saveNBT(NBTTagCompound tag) {
            tag.setBoolean(this.nbtKey, this.getValue());
        }

        @Override
        public void loadNBT(NBTTagCompound tag) {
            if (!tag.hasKey(this.nbtKey)) return;
            this.setValue(tag.getBoolean(this.nbtKey));
        }

        @Override
        public void saveToParameterCard(NBTTagCompound tag) {
            tag.setString("key", this.key);
            tag.setString("type", "boolean");
            tag.setBoolean("value", this.getValue());
        }

        @Override
        public void loadFromParameterCard(NBTTagCompound tag) {
            this.setValue(tag.getBoolean("value"));
        }

        public BooleanParameter(Boolean value, String nbtKey, String key) {
            super(value, nbtKey, key);
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
