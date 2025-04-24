package tectech.thing.metaTileEntity.multi.base;

import net.minecraft.util.StatCollector;

public abstract class Parameter<T> {
    private String localizedName;
    protected T currentValue;
    private T minValue = null;
    private T maxValue = null;

    public Parameter(T value, String key){
        this.currentValue = value;
        this.localizedName = StatCollector.translateToLocal(key);
    }

    public Parameter(T value, T minValue, T maxValue, String key) {
        this(value, key);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public T getValue(){
        return currentValue;
    }

    public T getMinValue() {
        return minValue;
    }

    public T getMaxValue() {
        return maxValue;
    }

    public String getValueString(){
        return currentValue.toString();
    }

    public String getLocalizedName(){
        return localizedName;
    }
    public void setValue(T value){
        this.currentValue = value;
    }
    public static class IntegerParameter extends Parameter<Integer>{
        public IntegerParameter(Integer value, String key) {
            super(value, key);
        }

        public IntegerParameter(Integer value, Integer minValue, Integer maxValue, String key) {
            super(value, minValue, maxValue, key);
        }
    }
    public static class DoubleParameter extends Parameter<Double>{
        public DoubleParameter(Double value, String key) {
            super(value, key);
        }

        public DoubleParameter(Double value, Double minValue, Double maxValue, String key) {
            super(value, minValue, maxValue, key);
        }
    }
    public static class StringParameter extends Parameter<String>{
        public StringParameter(String value, String key) {
            super(value, key);
        }
    }
    public static class BooleanParameter extends Parameter<Boolean>{
        public BooleanParameter(Boolean value, String key) {
            super(value, key);
        }
        public void invert(){
            this.currentValue = !this.currentValue;
        }
    }

}
