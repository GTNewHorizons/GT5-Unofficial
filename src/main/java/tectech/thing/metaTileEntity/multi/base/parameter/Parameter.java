package tectech.thing.metaTileEntity.multi.base.parameter;

public class Parameter<T> {

    private T value;
    private final String langKey;

    public Parameter(T value, String langKey) {
        this.value = value;
        this.langKey = langKey;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getLangKey() {
        return langKey;
    }
}
