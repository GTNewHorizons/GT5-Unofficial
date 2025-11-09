package gregtech.common.gui.modularui.multiblock.godforge.data;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.SyncHandler;

import tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsData;

/**
 * Sync handler helper primarily written to solve the issue of multiple panels needing
 * sync handlers for the same data, resulting in issues with sync value ID collisions.
 * Also just a lot cleaner to use.
 */
public class Syncers<T extends SyncHandler> {

    // spotless:off

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

    // ---------- //
    // Milestones //
    // ---------- //

    public static final Syncers<IntSyncValue> MILESTONE_CLICKED = new Syncers<>(
        "fog.sync.milestone_clicked",
        IntSyncValue.class,
        data -> {
            AtomicInteger i = new AtomicInteger();
            return new IntSyncValue(i::intValue, i::set);
        });

    public static final Syncers<DoubleSyncValue> MILESTONE_CHARGE_PROGRESS = new Syncers<>(
        "fog.sync.milestone_charge_progress",
        DoubleSyncValue.class,
        data -> new DoubleSyncValue(
            data::getPowerMilestonePercentage, val ->
            data.setPowerMilestonePercentage((float) val)));
    public static final Syncers<DoubleSyncValue> MILESTONE_CHARGE_PROGRESS_INVERTED = new Syncers<>(
        "fog.sync.milestone_charge_progress_inverted",
        DoubleSyncValue.class,
        data -> new DoubleSyncValue(
            data::getInvertedPowerMilestonePercentage,
            val -> data.setInvertedPowerMilestonePercentage((float) val)));

    public static final Syncers<DoubleSyncValue> MILESTONE_CONVERSION_PROGRESS = new Syncers<>(
        "fog.sync.milestone_conversion_progress",
        DoubleSyncValue.class,
        data -> new DoubleSyncValue(
            data::getRecipeMilestonePercentage, val ->
            data.setRecipeMilestonePercentage((float) val)));
    public static final Syncers<DoubleSyncValue> MILESTONE_CONVERSION_PROGRESS_INVERTED = new Syncers<>(
        "fog.sync.milestone_conversion_progress_inverted",
        DoubleSyncValue.class,
        data -> new DoubleSyncValue(
            data::getInvertedRecipeMilestonePercentage,
            val -> data.setInvertedRecipeMilestonePercentage((float) val)));

    public static final Syncers<DoubleSyncValue> MILESTONE_CATALYST_PROGRESS = new Syncers<>(
        "fog.sync.milestone_catalyst_progress",
        DoubleSyncValue.class,
        data -> new DoubleSyncValue(
            data::getFuelMilestonePercentage, val ->
            data.setFuelMilestonePercentage((float) val)));
    public static final Syncers<DoubleSyncValue> MILESTONE_CATALYST_PROGRESS_INVERTED = new Syncers<>(
        "fog.sync.milestone_catalyst_progress_inverted",
        DoubleSyncValue.class,
        data -> new DoubleSyncValue(
            data::getInvertedFuelMilestonePercentage,
            val -> data.setInvertedFuelMilestonePercentage((float) val)));

    public static final Syncers<DoubleSyncValue> MILESTONE_COMPOSITION_PROGRESS = new Syncers<>(
        "fog.sync.milestone_composition_progress",
        DoubleSyncValue.class,
        data -> new DoubleSyncValue(
            data::getStructureMilestonePercentage, val ->
            data.setStructureMilestonePercentage((float) val)));
    public static final Syncers<DoubleSyncValue> MILESTONE_COMPOSITION_PROGRESS_INVERTED = new Syncers<>(
        "fog.sync.milestone_composition_progress_inverted",
        DoubleSyncValue.class,
        data -> new DoubleSyncValue(
            data::getInvertedStructureMilestonePercentage,
            val -> data.setInvertedStructureMilestonePercentage((float) val)));

    // spotless:on

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

    public T lookup(PanelSyncManager syncManager, Panels fromPanel) {
        return syncManager.findSyncHandler(getSyncId(fromPanel), clazz);
    }

    public String getSyncId(Panels fromPanel) {
        if (fromPanel == Panels.MAIN) {
            return syncId;
        }
        return fromPanel.getPanelId() + "/" + syncId;
    }
}
