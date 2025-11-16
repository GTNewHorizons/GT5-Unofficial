package gregtech.common.gui.modularui.multiblock.godforge.data;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import com.cleanroommc.modularui.value.sync.BigIntSyncValue;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.FloatSyncValue;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.value.sync.ValueSyncHandler;

import gregtech.common.gui.modularui.multiblock.godforge.util.SyncHypervisor;
import tectech.thing.metaTileEntity.multi.godforge.color.ForgeOfGodsStarColor;
import tectech.thing.metaTileEntity.multi.godforge.upgrade.ForgeOfGodsUpgrade;
import tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsData;

/**
 * Sync handler helper primarily written to solve the issue of multiple panels needing
 * sync handlers for the same data, resulting in issues with sync value ID collisions.
 * Also is just a lot cleaner to use, especially when many are used in multiple panels.
 */
public class SyncValues<T extends ValueSyncHandler<?>> {

    // spotless:off

    // ----------------- //
    // Inherited Syncers //
    // ----------------- //

    public static final SyncValues<BooleanSyncValue> STRUCTURE_UPDATE = new SyncValues<>("structureUpdateButton");

    // --------------- //
    // General Syncers //
    // --------------- //

    public static final SyncValues<EnumSyncValue<Formatters>> FORMATTER = new SyncValues<>(
        "fog.sync.formatter",
        data -> new EnumSyncValue<>(Formatters.class, data::getFormatter, data::setFormatter));

    public static final SyncValues<BooleanSyncValue> INVERSION = new SyncValues<>(
        "fog.sync.inversion",
        data -> new BooleanSyncValue(data::isInversion, data::setInversion));

    // ---- //
    // Fuel //
    // ---- //

    public static final SyncValues<EnumSyncValue<Fuels>> SELECTED_FUEL = new SyncValues<>(
        "fog.sync.selected_fuel",
        data -> new EnumSyncValue<>(Fuels.class, () -> Fuels.getFromData(data), fuel -> fuel.select(data)));

    public static final SyncValues<LongSyncValue> FUEL_CONSUMPTION = new SyncValues<>(
        "fog.sync.fuel_consumption",
        data -> new LongSyncValue(data::getFuelConsumption, data::setFuelConsumption));

    public static final SyncValues<IntSyncValue> FUEL_FACTOR = new SyncValues<>(
        "fog.sync.fuel_factor",
        data -> new IntSyncValue(data::getFuelConsumptionFactor, data::setFuelConsumptionFactor));

    public static final SyncValues<IntSyncValue> NEEDED_STARTUP_FUEL = new SyncValues<>(
        "fog.sync.needed_startup_fuel",
        data -> new IntSyncValue(data::getNeededStartupFuel, data::setNeededStartupFuel));

    public static final SyncValues<IntSyncValue> FUEL_AMOUNT = new SyncValues<>(
        "fog.sync.fuel_amount",
        data -> new IntSyncValue(data::getStellarFuelAmount, data::setStellarFuelAmount));

    // ------- //
    // Battery //
    // ------  //

    public static final SyncValues<BooleanSyncValue> BATTERY_CHARGING = new SyncValues<>(
        "fog.sync.battery_charging",
        data -> new BooleanSyncValue(data::isBatteryCharging, data::setBatteryCharging));

    public static final SyncValues<IntSyncValue> INTERNAL_BATTERY = new SyncValues<>(
        "fog.sync.internal_battery",
        data -> new IntSyncValue(data::getInternalBattery, data::setInternalBattery));

    public static final SyncValues<IntSyncValue> MAX_BATTERY_CHARGE = new SyncValues<>(
        "fog.sync.max_battery_charge",
        data -> new IntSyncValue(data::getMaxBatteryCharge, data::setMaxBatteryCharge));

    // --------------- //
    // Graviton Shards //
    // --------------- //

    public static final SyncValues<BooleanSyncValue> SHARD_EJECTION = new SyncValues<>(
        "fog.sync.shard_ejection",
        data -> new BooleanSyncValue(data::isGravitonShardEjection, data::setGravitonShardEjection));

    public static final SyncValues<IntSyncValue> AVAILABLE_GRAVITON_SHARDS = new SyncValues<>(
        "fog.sync.available_graviton_shards",
        data -> new IntSyncValue(data::getGravitonShardsAvailable, data::setGravitonShardsAvailable));

    // -------- //
    // Upgrades //
    // -------- //

    public static final SyncValues<EnumSyncValue<ForgeOfGodsUpgrade>> UPGRADE_CLICKED = new SyncValues<>(
        "fog.sync.upgrade_clicked",
        data -> {
            // Integer for 0 value instead of null value at init. Sync values crash if you try to sync a null
            AtomicInteger i = new AtomicInteger();
            return new EnumSyncValue<>(
                ForgeOfGodsUpgrade.class,
                () -> ForgeOfGodsUpgrade.VALUES[i.intValue()],
                val -> i.set(val.ordinal()));
        });

    public static final SyncValues<GenericListSyncHandler<?>> UPGRADES_LIST = new SyncValues<>(
        "fog.sync.upgrades_list",
        data -> data.getUpgrades().getFullSyncer());

    // ---------- //
    // Milestones //
    // ---------- //

    public static final SyncValues<EnumSyncValue<Milestones>> MILESTONE_CLICKED = new SyncValues<>(
        "fog.sync.milestone_clicked",
        data -> {
            // Integer for 0 value instead of null value at init. Sync values crash if you try to sync a null
            AtomicInteger i = new AtomicInteger();
            return new EnumSyncValue<>(
                Milestones.class,
                () -> Milestones.VALUES[i.intValue()],
                val -> i.set(val.ordinal()));
        });

    public static final SyncValues<BigIntSyncValue> TOTAL_POWER_CONSUMED = new SyncValues<>(
        "fog.sync.total_power_consumed",
        data -> new BigIntSyncValue(data::getTotalPowerConsumed, data::setTotalPowerConsumed));

    public static final SyncValues<LongSyncValue> TOTAL_RECIPES_PROCESSED = new SyncValues<>(
        "fog.sync.total_recipes_processed",
        data -> new LongSyncValue(data::getTotalRecipesProcessed, data::setTotalRecipesProcessed));

    public static final SyncValues<LongSyncValue> TOTAL_FUEL_CONSUMED = new SyncValues<>(
        "fog.sync.total_fuel_consumed",
        data -> new LongSyncValue(data::getTotalFuelConsumed, data::setTotalFuelConsumed));

    public static final SyncValues<IntSyncValue> MILESTONE_CHARGE_LEVEL = new SyncValues<>(
        "fog.sync.milestone_charge_level",
        data -> new IntSyncValue(() -> data.getMilestoneProgress(0), val -> data.setMilestoneProgress(0, val)));
    public static final SyncValues<FloatSyncValue> MILESTONE_CHARGE_PROGRESS = new SyncValues<>(
        "fog.sync.milestone_charge_progress",
        data -> new FloatSyncValue(data::getPowerMilestonePercentage, data::setPowerMilestonePercentage));
    public static final SyncValues<FloatSyncValue> MILESTONE_CHARGE_PROGRESS_INVERTED = new SyncValues<>(
        "fog.sync.milestone_charge_progress_inverted",
        data -> new FloatSyncValue(data::getInvertedPowerMilestonePercentage, data::setInvertedPowerMilestonePercentage));

    public static final SyncValues<IntSyncValue> MILESTONE_CONVERSION_LEVEL = new SyncValues<>(
        "fog.sync.milestone_conversion_level",
        data -> new IntSyncValue(() -> data.getMilestoneProgress(1), val -> data.setMilestoneProgress(1, val)));
    public static final SyncValues<FloatSyncValue> MILESTONE_CONVERSION_PROGRESS = new SyncValues<>(
        "fog.sync.milestone_conversion_progress",
        data -> new FloatSyncValue(data::getRecipeMilestonePercentage, data::setRecipeMilestonePercentage));
    public static final SyncValues<FloatSyncValue> MILESTONE_CONVERSION_PROGRESS_INVERTED = new SyncValues<>(
        "fog.sync.milestone_conversion_progress_inverted",
        data -> new FloatSyncValue(data::getInvertedRecipeMilestonePercentage, data::setInvertedRecipeMilestonePercentage));

    public static final SyncValues<IntSyncValue> MILESTONE_CATALYST_LEVEL = new SyncValues<>(
        "fog.sync.milestone_catalyst_level",
        data -> new IntSyncValue(() -> data.getMilestoneProgress(2), val -> data.setMilestoneProgress(2, val)));
    public static final SyncValues<FloatSyncValue> MILESTONE_CATALYST_PROGRESS = new SyncValues<>(
        "fog.sync.milestone_catalyst_progress",
        data -> new FloatSyncValue(data::getFuelMilestonePercentage, data::setFuelMilestonePercentage));
    public static final SyncValues<FloatSyncValue> MILESTONE_CATALYST_PROGRESS_INVERTED = new SyncValues<>(
        "fog.sync.milestone_catalyst_progress_inverted",
        data -> new FloatSyncValue(data::getInvertedFuelMilestonePercentage, data::setInvertedFuelMilestonePercentage));

    public static final SyncValues<IntSyncValue> MILESTONE_COMPOSITION_LEVEL = new SyncValues<>(
        "fog.sync.milestone_composition_level",
        data -> new IntSyncValue(() -> data.getMilestoneProgress(3), val -> data.setMilestoneProgress(3, val)));
    public static final SyncValues<FloatSyncValue> MILESTONE_COMPOSITION_PROGRESS = new SyncValues<>(
        "fog.sync.milestone_composition_progress",
        data -> new FloatSyncValue(data::getStructureMilestonePercentage, data::setStructureMilestonePercentage));
    public static final SyncValues<FloatSyncValue> MILESTONE_COMPOSITION_PROGRESS_INVERTED = new SyncValues<>(
        "fog.sync.milestone_composition_progress_inverted",
        data -> new FloatSyncValue(data::getInvertedStructureMilestonePercentage, data::setInvertedStructureMilestonePercentage));

    // ---------- //
    // Star Color //
    // ---------- //

    public static final SyncValues<IntSyncValue> STAR_COLOR_CLICKED = new SyncValues<>(
        "fog.sync.star_color_clicked",
        data -> {
            AtomicInteger i = new AtomicInteger();
            return new IntSyncValue(i::intValue, i::set);
        });

    public static final SyncValues<StringSyncValue> SELECTED_STAR_COLOR = new SyncValues<>(
        "fog.sync.selected_star_color",
        data -> new StringSyncValue(data::getSelectedStarColor, data::setSelectedStarColor));

    public static final SyncValues<GenericListSyncHandler<ForgeOfGodsStarColor>> STAR_COLORS = new SyncValues<>(
        "fog.sync.star_colors",
        data -> data.getStarColors().getSyncer());

    // spotless:on

    private final String syncId;
    private final Function<ForgeOfGodsData, T> syncValueSupplier;
    private final boolean inherited;

    private SyncValues(String syncId) {
        this.syncId = syncId;
        this.syncValueSupplier = null;
        this.inherited = true;
    }

    private SyncValues(String syncId, Function<ForgeOfGodsData, T> syncValueSupplier) {
        this.syncId = syncId;
        this.syncValueSupplier = syncValueSupplier;
        this.inherited = false;
    }

    public void registerFor(Panels forPanel, SyncHypervisor hypervisor) {
        T syncValue = create(hypervisor);
        PanelSyncManager syncManager = hypervisor.getSyncManager(forPanel);
        syncManager.syncValue(getSyncId(forPanel), syncValue);
    }

    public T create(SyncHypervisor hypervisor) {
        if (inherited || syncValueSupplier == null) {
            throw new IllegalStateException("Cannot create SyncValue for inherited syncer! ID: " + syncId);
        }

        return syncValueSupplier.apply(hypervisor.getData());
    }

    @SuppressWarnings("unchecked")
    public T lookupFrom(Panels fromPanel, SyncHypervisor hypervisor) {
        PanelSyncManager syncManager = hypervisor.getSyncManager(fromPanel);
        return (T) syncManager.findSyncHandler(getSyncId(fromPanel));
    }

    public void notifyUpdateFrom(Panels fromPanel, SyncHypervisor hypervisor) {
        T syncer = lookupFrom(fromPanel, hypervisor);
        syncer.notifyUpdate();
    }

    public String getSyncId(Panels fromPanel) {
        if (fromPanel == Panels.MAIN || inherited) {
            return syncId;
        }
        return fromPanel.getPanelId() + "/" + syncId;
    }
}
