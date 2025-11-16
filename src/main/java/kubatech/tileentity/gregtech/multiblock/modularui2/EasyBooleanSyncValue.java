package kubatech.tileentity.gregtech.multiblock.modularui2;

import com.cleanroommc.modularui.utils.BooleanConsumer;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;

import java.util.function.BooleanSupplier;

public class EasyBooleanSyncValue extends EasySyncValue<BooleanSyncValue, Boolean> {
    BooleanSupplier getter;
    BooleanConsumer setter;
    public EasyBooleanSyncValue(String name, BooleanSupplier getter, BooleanConsumer setter) {
        super(name);
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    protected void registerHandler() {
        this.handler = new BooleanSyncValue(getter,setter);
    }
}
