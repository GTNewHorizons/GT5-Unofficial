package kubatech.tileentity.gregtech.multiblock.modularui2;

import java.util.List;

import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

/*
 * Basicly another version of EasySyncValue, makes code looks better.
 */
public abstract class EasyListSyncValue<T extends GenericListSyncHandler<Y>, Y> {

    public final String name;
    public T handler;

    public EasyListSyncValue(String name) {
        this.name = name;
    }

    // You should register Handler here.
    protected abstract void registerHandler();

    public void registerSyncValue(PanelSyncManager syncManager) {
        registerHandler();
        syncManager.syncValue(name, handler);
    }

    public List<Y> getValue() {
        return handler.getValue();
    }
}
