package kubatech.tileentity.gregtech.multiblock.modularui2;

import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.ValueSyncHandler;

/*
 * Basicly a wrapper of ValueSyncHandler, makes code looks better.
 */
public abstract class EasySyncValue<T extends ValueSyncHandler<Y>, Y> {

    public final String name;
    public T handler;

    public EasySyncValue(String name) {
        this.name = name;
    }

    // You should register Handler here.
    protected abstract void registerHandler();

    public void registerSyncValue(PanelSyncManager syncManager) {
        registerHandler();
        syncManager.syncValue(name, handler);
    }

    public Y getValue() {
        return handler.getValue();
    }
}
