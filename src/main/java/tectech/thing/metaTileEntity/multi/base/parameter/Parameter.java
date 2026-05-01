package tectech.thing.metaTileEntity.multi.base.parameter;

import java.util.function.Supplier;

import com.cleanroommc.modularui.value.sync.SyncHandler;

public abstract class Parameter<T> {

    protected T value;
    // Suppliers relevant to number parameters only
    protected Supplier<T> min;
    protected Supplier<T> max;

    private final String langKey;
    private boolean showInGui = true;

    public Parameter(T value, String langKey) {
        this.value = value;
        this.langKey = langKey;
    }

    public Parameter(T value, String langKey, Supplier<T> min, Supplier<T> max) {
        this.value = value;
        this.langKey = langKey;
        this.min = min;
        this.max = max;
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

    public T getMin() {
        return min.get();
    }

    public T getMax() {
        return max.get();
    }

    public Parameter<T> disableGui() {
        this.showInGui = false;
        return this;
    }

    public boolean shouldShowInGui() {
        return showInGui;
    }

    public abstract SyncHandler createSyncHandler();
}
