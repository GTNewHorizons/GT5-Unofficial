package gregtech.common.gui.modularui.multiblock.godforge.data;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import com.cleanroommc.modularui.value.sync.BigIntSyncValue;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.SyncHandler;

import gregtech.common.gui.modularui.multiblock.godforge.util.SyncHypervisor;
import tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsData;

/**
 * Sync handler helper primarily written to solve the issue of multiple panels needing
 * sync handlers for the same data, resulting in issues with sync value ID collisions.
 * Also just a lot cleaner to use.
 */
public class Syncers<T extends SyncHandler> {

    // spotless:off

    public static final Syncers<EnumSyncValue<Formatters>> FORMATTER = new Syncers<>(
        "fog.sync.formatter",
        data -> new EnumSyncValue<>(Formatters.class, data::getFormatter, data::setFormatter));

    public static final Syncers<BooleanSyncValue> BATTERY_CHARGING = new Syncers<>(
        "fog.sync.battery_charging",
        data -> new BooleanSyncValue(data::isBatteryCharging, data::setBatteryCharging));

    public static final Syncers<IntSyncValue> INTERNAL_BATTERY = new Syncers<>(
        "fog.sync.internal_battery",
        data -> new IntSyncValue(data::getInternalBattery, data::setInternalBattery));

    public static final Syncers<IntSyncValue> MAX_BATTERY_CHARGE = new Syncers<>(
        "fog.sync.max_battery_charge",
        data -> new IntSyncValue(data::getMaxBatteryCharge, data::setMaxBatteryCharge));

    public static final Syncers<BooleanSyncValue> SHARD_EJECTION = new Syncers<>(
        "fog.sync.shard_ejection",
        data -> new BooleanSyncValue(data::isGravitonShardEjection, data::setGravitonShardEjection));

    public static final Syncers<BooleanSyncValue> INVERSION = new Syncers<>(
        "fog.sync.inversion",
        data -> new BooleanSyncValue(data::isInversion, data::setInversion));

    public static final Syncers<EnumSyncValue<Fuels>> SELECTED_FUEL = new Syncers<>(
        "fog.sync.selected_fuel",
        data -> new EnumSyncValue<>(Fuels.class, () -> Fuels.getFromData(data), fuel -> fuel.select(data)));

    public static final Syncers<LongSyncValue> FUEL_CONSUMPTION = new Syncers<>(
        "fog.sync.fuel_consumption",
        data -> new LongSyncValue(data::getFuelConsumption, data::setFuelConsumption));

    public static final Syncers<IntSyncValue> FUEL_FACTOR = new Syncers<>(
        "fog.sync.fuel_factor",
        data -> new IntSyncValue(data::getFuelConsumptionFactor, data::setFuelConsumptionFactor));

    public static final Syncers<IntSyncValue> NEEDED_STARTUP_FUEL = new Syncers<>(
        "fog.sync.needed_startup_fuel",
        data -> new IntSyncValue(data::getNeededStartupFuel, data::setNeededStartupFuel));

    public static final Syncers<IntSyncValue> FUEL_AMOUNT = new Syncers<>(
        "fog.sync.fuel_amount",
        data -> new IntSyncValue(data::getStellarFuelAmount, data::setStellarFuelAmount));

    // ---------- //
    // Milestones //
    // ---------- //

    public static final Syncers<EnumSyncValue<Milestones>> MILESTONE_CLICKED = new Syncers<>(
        "fog.sync.milestone_clicked",
        data -> {
            AtomicInteger i = new AtomicInteger();
            return new EnumSyncValue<>(
                Milestones.class,
                () -> Milestones.VALUES[i.intValue()],
                val -> i.set(val.ordinal()));
        });

    public static final Syncers<BigIntSyncValue> TOTAL_POWER_CONSUMED = new Syncers<>(
        "fog.sync.total_power_consumed",
        data -> new BigIntSyncValue(data::getTotalPowerConsumed, data::setTotalPowerConsumed));

    public static final Syncers<LongSyncValue> TOTAL_RECIPES_PROCESSED = new Syncers<>(
        "fog.sync.total_recipes_processed",
        data -> new LongSyncValue(data::getTotalRecipesProcessed, data::setTotalRecipesProcessed));

    public static final Syncers<LongSyncValue> TOTAL_FUEL_CONSUMED = new Syncers<>(
        "fog.sync.total_fuel_consumed",
        data -> new LongSyncValue(data::getTotalFuelConsumed, data::setTotalFuelConsumed));

    public static final Syncers<IntSyncValue> MILESTONE_CHARGE_LEVEL = new Syncers<>(
        "fog.sync.milestone_charge_level",
        data -> new IntSyncValue(() -> data.getMilestoneProgress(0), val -> data.setMilestoneProgress(0, val)));
    public static final Syncers<DoubleSyncValue> MILESTONE_CHARGE_PROGRESS = new Syncers<>(
        "fog.sync.milestone_charge_progress",
        data -> new DoubleSyncValue(
            data::getPowerMilestonePercentage, val ->
            data.setPowerMilestonePercentage((float) val)));
    public static final Syncers<DoubleSyncValue> MILESTONE_CHARGE_PROGRESS_INVERTED = new Syncers<>(
        "fog.sync.milestone_charge_progress_inverted",
        data -> new DoubleSyncValue(
            data::getInvertedPowerMilestonePercentage,
            val -> data.setInvertedPowerMilestonePercentage((float) val)));

    public static final Syncers<IntSyncValue> MILESTONE_CONVERSION_LEVEL = new Syncers<>(
        "fog.sync.milestone_conversion_level",
        data -> new IntSyncValue(() -> data.getMilestoneProgress(1), val -> data.setMilestoneProgress(1, val)));
    public static final Syncers<DoubleSyncValue> MILESTONE_CONVERSION_PROGRESS = new Syncers<>(
        "fog.sync.milestone_conversion_progress",
        data -> new DoubleSyncValue(
            data::getRecipeMilestonePercentage, val ->
            data.setRecipeMilestonePercentage((float) val)));
    public static final Syncers<DoubleSyncValue> MILESTONE_CONVERSION_PROGRESS_INVERTED = new Syncers<>(
        "fog.sync.milestone_conversion_progress_inverted",
        data -> new DoubleSyncValue(
            data::getInvertedRecipeMilestonePercentage,
            val -> data.setInvertedRecipeMilestonePercentage((float) val)));

    public static final Syncers<IntSyncValue> MILESTONE_CATALYST_LEVEL = new Syncers<>(
            "fog.sync.milestone_catalyst_level",
            data -> new IntSyncValue(() -> data.getMilestoneProgress(2), val -> data.setMilestoneProgress(2, val)));
    public static final Syncers<DoubleSyncValue> MILESTONE_CATALYST_PROGRESS = new Syncers<>(
        "fog.sync.milestone_catalyst_progress",
        data -> new DoubleSyncValue(
            data::getFuelMilestonePercentage, val ->
            data.setFuelMilestonePercentage((float) val)));
    public static final Syncers<DoubleSyncValue> MILESTONE_CATALYST_PROGRESS_INVERTED = new Syncers<>(
        "fog.sync.milestone_catalyst_progress_inverted",
        data -> new DoubleSyncValue(
            data::getInvertedFuelMilestonePercentage,
            val -> data.setInvertedFuelMilestonePercentage((float) val)));

    public static final Syncers<IntSyncValue> MILESTONE_COMPOSITION_LEVEL = new Syncers<>(
            "fog.sync.milestone_composition_level",
            data -> new IntSyncValue(() -> data.getMilestoneProgress(3), val -> data.setMilestoneProgress(3, val)));
    public static final Syncers<DoubleSyncValue> MILESTONE_COMPOSITION_PROGRESS = new Syncers<>(
        "fog.sync.milestone_composition_progress",
        data -> new DoubleSyncValue(
            data::getStructureMilestonePercentage, val ->
            data.setStructureMilestonePercentage((float) val)));
    public static final Syncers<DoubleSyncValue> MILESTONE_COMPOSITION_PROGRESS_INVERTED = new Syncers<>(
        "fog.sync.milestone_composition_progress_inverted",
        data -> new DoubleSyncValue(
            data::getInvertedStructureMilestonePercentage,
            val -> data.setInvertedStructureMilestonePercentage((float) val)));

    // spotless:on

    private final String syncId;
    private final Function<ForgeOfGodsData, T> syncValueSupplier;

    private Syncers(String syncId, Function<ForgeOfGodsData, T> syncValueSupplier) {
        this.syncValueSupplier = syncValueSupplier;
        this.syncId = syncId;
    }

    public T registerFor(Panels forPanel, SyncHypervisor hypervisor) {
        T syncValue = create(hypervisor);
        PanelSyncManager syncManager = hypervisor.getSyncManager(forPanel);
        syncManager.syncValue(getSyncId(forPanel), syncValue);
        return syncValue;
    }

    public T create(SyncHypervisor hypervisor) {
        return syncValueSupplier.apply(hypervisor.getData());
    }

    @SuppressWarnings("unchecked")
    public T lookupFrom(Panels fromPanel, SyncHypervisor hypervisor) {
        PanelSyncManager syncManager = hypervisor.getSyncManager(fromPanel);
        return (T) syncManager.findSyncHandler(getSyncId(fromPanel));
    }

    public String getSyncId(Panels fromPanel) {
        if (fromPanel == Panels.MAIN) {
            return syncId;
        }
        return fromPanel.getPanelId() + "/" + syncId;
    }
}
