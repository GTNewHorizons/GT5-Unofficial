package tectech.thing.metaTileEntity.multi.base.parameter;

import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.SyncHandler;

public class BooleanParameter extends Parameter<Boolean> {

    public BooleanParameter(Boolean value, String langKey) {
        super(value, langKey);
    }

    @Override
    public SyncHandler createSyncHandler() {
        return new BooleanSyncValue(this::getValue, this::setValue);
    }
}
