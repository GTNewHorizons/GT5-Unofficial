package tectech.thing.metaTileEntity.multi.base.parameter;

import java.util.function.Supplier;

import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.SyncHandler;

public class DoubleParameter extends NumericParameter<Double> {

    public DoubleParameter(Double value, String langKey, Supplier<Double> min, Supplier<Double> max) {
        super(value, langKey, min, max);
    }

    @Override
    public SyncHandler createSyncHandler() {
        return new DoubleSyncValue(this::getValue, this::setValue);
    }

    public double validateValue(double num) {
        return Math.max(min.get(), Math.min(num, max.get()));
    }
}
