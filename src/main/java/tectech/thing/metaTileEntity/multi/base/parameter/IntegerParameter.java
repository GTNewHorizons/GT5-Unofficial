package tectech.thing.metaTileEntity.multi.base.parameter;

import java.util.function.Supplier;

import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.SyncHandler;

public class IntegerParameter extends Parameter<Integer> {

    public IntegerParameter(Integer value, String langKey, Supplier<Integer> min, Supplier<Integer> max) {
        super(value, langKey, min, max);
    }

    @Override
    public SyncHandler createSyncHandler() {
        return new IntSyncValue(this::getValue, this::setValue);
    }
}
