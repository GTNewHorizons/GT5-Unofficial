package gregtech.common.gui.modularui.multiblock.godforge.data;

import java.util.function.Function;

import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.SyncHandler;

import tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsData;

/**
 * Sync handler helper primarily written to solve the issue of multiple panels needing
 * sync handlers for the same data, resulting in issues with sync value ID collisions.
 * Also just a lot cleaner to use.
 */
public class Syncers<T extends SyncHandler> {

    public static final Syncers<BooleanSyncValue> BATTERY_CHARGING = new Syncers<>(
        "fog.sync.battery_charging",
        BooleanSyncValue.class,
        data -> new BooleanSyncValue(data::isBatteryCharging, data::setBatteryCharging));

    public static final Syncers<BooleanSyncValue> SHARD_EJECTION = new Syncers<>(
        "fog.sync.shard_ejection",
        BooleanSyncValue.class,
        data -> new BooleanSyncValue(data::isGravitonShardEjection, data::setGravitonShardEjection));

    public static final Syncers<BooleanSyncValue> INVERSION = new Syncers<>(
        "fog.sync.inversion",
        BooleanSyncValue.class,
        data -> new BooleanSyncValue(data::isInversion, data::setInversion));

    private final String syncId;
    private final Class<T> clazz;
    private final Function<ForgeOfGodsData, T> syncValueSupplier;

    private Syncers(String syncId, Class<T> clazz, Function<ForgeOfGodsData, T> syncValueSupplier) {
        this.syncValueSupplier = syncValueSupplier;
        this.clazz = clazz;
        this.syncId = syncId;
    }

    public T register(PanelSyncManager syncManager, ForgeOfGodsData data, Panels fromPanel) {
        T syncValue = create(data);
        syncManager.syncValue(getSyncId(fromPanel), syncValue);
        return syncValue;
    }

    public T create(ForgeOfGodsData data) {
        return syncValueSupplier.apply(data);
    }

    public T get(PanelSyncManager syncManager, Panels fromPanel) {
        return syncManager.findSyncHandler(getSyncId(fromPanel), clazz);
    }

    public String getSyncId(Panels fromPanel) {
        if (fromPanel == Panels.MAIN) {
            return syncId;
        }
        return fromPanel.getPanelId() + "/" + syncId;
    }
}
