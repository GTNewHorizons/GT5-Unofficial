package gregtech.common.gui.modularui;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.cleanroommc.modularui.api.value.IDoubleValue;

public class NoSyncDouble implements IDoubleValue<Double> {

    private Supplier<Double> supplier;
    private Consumer<Double> consumer;

    public NoSyncDouble(Supplier<Double> supplier, Consumer<Double> consumer) {
        this.supplier = supplier;
        this.consumer = consumer;
    }

    @Override
    public double getDoubleValue() {
        return supplier.get();
    }

    @Override
    public void setDoubleValue(double val) {
        consumer.accept(val);
    }

    @Override
    public Double getValue() {
        return supplier.get();
    }

    @Override
    public void setValue(Double value) {
        consumer.accept(value);
    }
}
