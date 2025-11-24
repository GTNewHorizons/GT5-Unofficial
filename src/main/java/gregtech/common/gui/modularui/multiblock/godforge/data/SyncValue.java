package gregtech.common.gui.modularui.multiblock.godforge.data;

import java.util.function.Function;

import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.ValueSyncHandler;

import gregtech.common.gui.modularui.multiblock.godforge.util.SyncHypervisor;
import tectech.thing.metaTileEntity.multi.godforge.MTEBaseModule;
import tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsData;

public abstract class SyncValue<U extends ValueSyncHandler<?>> {

    protected final String syncId;
    protected final boolean inherited;

    private SyncValue(String syncId, boolean inherited) {
        this.syncId = syncId;
        this.inherited = inherited;
    }

    public void registerFor(Panels forPanel, SyncHypervisor hypervisor) {
        U syncValue = create(hypervisor);
        PanelSyncManager syncManager = hypervisor.getSyncManager(forPanel);
        syncManager.syncValue(getSyncId(forPanel), syncValue);
    }

    public abstract U create(SyncHypervisor hypervisor);

    @SuppressWarnings("unchecked")
    public U lookupFrom(Panels fromPanel, SyncHypervisor hypervisor) {
        PanelSyncManager syncManager = hypervisor.getSyncManager(fromPanel);
        return (U) syncManager.findSyncHandler(getSyncId(fromPanel));
    }

    public void notifyUpdateFrom(Panels fromPanel, SyncHypervisor hypervisor) {
        U syncer = lookupFrom(fromPanel, hypervisor);
        syncer.notifyUpdate();
    }

    public String getSyncId(Panels fromPanel) {
        if (inherited) {
            return syncId;
        }
        return fromPanel.getPanelId() + "/" + syncId;
    }

    /** Sync values exclusive to the main Godforge controller. */
    public static class ForgeOfGodsSyncValue<U extends ValueSyncHandler<?>> extends SyncValue<U> {

        private final Function<ForgeOfGodsData, U> syncValueSupplier;

        public ForgeOfGodsSyncValue(String syncId) {
            super(syncId, true);
            this.syncValueSupplier = null;
        }

        public ForgeOfGodsSyncValue(String syncId, Function<ForgeOfGodsData, U> syncValueSupplier) {
            super(syncId, false);
            this.syncValueSupplier = syncValueSupplier;
        }

        @Override
        public U create(SyncHypervisor hypervisor) {
            if (inherited || syncValueSupplier == null) {
                throw new IllegalStateException("Cannot create SyncValue for inherited syncer! ID: " + syncId);
            }

            return syncValueSupplier.apply(hypervisor.getData());
        }
    }

    /** Sync values exclusive to a Godforge module. */
    public static class ModuleSyncValue<U extends ValueSyncHandler<?>> extends SyncValue<U> {

        private final Function<MTEBaseModule, U> syncValueSupplier;

        public ModuleSyncValue(String syncId) {
            super(syncId, true);
            this.syncValueSupplier = null;
        }

        public ModuleSyncValue(String syncId, Function<MTEBaseModule, U> syncValueSupplier) {
            super(syncId, false);
            this.syncValueSupplier = syncValueSupplier;
        }

        @Override
        public U create(SyncHypervisor hypervisor) {
            if (inherited || syncValueSupplier == null) {
                throw new IllegalStateException("Cannot create SyncValue for inherited syncer! ID: " + syncId);
            }

            if (hypervisor.getModule() == null) {
                throw new IllegalStateException("Cannot create sync value for module with no module present");
            }

            return syncValueSupplier.apply(hypervisor.getModule());
        }
    }

    /**
     * Sync values that could be from the main Godforge or a module depending on hypervisor state.
     * Will prioritize the main Godforge if both are available.
     */
    public static class HybridSyncValue<U extends ValueSyncHandler<?>> extends SyncValue<U> {

        private final Function<ForgeOfGodsData, U> dataSupplier;
        private final Function<MTEBaseModule, U> moduleSupplier;

        public HybridSyncValue(String syncId) {
            super(syncId, true);
            this.dataSupplier = null;
            this.moduleSupplier = null;
        }

        public HybridSyncValue(String syncId, Function<ForgeOfGodsData, U> dataSupplier,
            Function<MTEBaseModule, U> moduleSupplier) {
            super(syncId, false);
            this.dataSupplier = dataSupplier;
            this.moduleSupplier = moduleSupplier;
        }

        @Override
        public U create(SyncHypervisor hypervisor) {
            if (inherited) {
                throw new IllegalStateException("Cannot create SyncValue for inherited syncer! ID: " + syncId);
            }

            if (hypervisor.getData() != null && dataSupplier != null) {
                return dataSupplier.apply(hypervisor.getData());
            }

            if (hypervisor.getModule() != null && moduleSupplier != null) {
                return moduleSupplier.apply(hypervisor.getModule());
            }

            throw new IllegalStateException("Cannot create sync value as hypervisor has no applicable state");
        }
    }
}
