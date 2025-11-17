package kubatech.tileentity.gregtech.multiblock.modularui2;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

import com.cleanroommc.modularui.value.sync.IntSyncValue;

public class EasyIntSyncValue extends EasySyncValue<IntSyncValue, Integer> {

    IntSupplier getter;
    IntConsumer setter;

    public EasyIntSyncValue(String name, IntSupplier getter, IntConsumer setter) {
        super(name);
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    protected void registerHandler() {
        this.handler = new IntSyncValue(getter, setter);
    }
}
