package gregtech.common.gui.modularui.multiblock.godforge.sync;

import java.util.function.Function;

import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.ValueSyncHandler;

import tectech.thing.metaTileEntity.multi.godforge.MTEBaseModule;
import tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsData;

/**
 * Sync handler helper primarily written to solve the issue of multiple panels needing
 * sync handlers for the same data, resulting in issues with sync value ID collisions.
 * Also is just a lot cleaner to use, especially when many are used in multiple panels.
 */
public abstract class SyncValue<T extends ValueSyncHandler<?>> {

    protected final String syncId;
    protected final boolean inherited;

    private SyncValue(String syncId, boolean inherited) {
        this.syncId = syncId;
        this.inherited = inherited;
    }

    public void registerFor(Modules<?> forModule, Panels forPanel, SyncHypervisor hypervisor) {
        T syncValue = create(forModule, hypervisor);
        PanelSyncManager syncManager = hypervisor.getSyncManager(forModule, forPanel);
        syncManager.syncValue(getSyncId(forModule, forPanel, hypervisor), syncValue);
    }

    public abstract T create(Modules<?> forModule, SyncHypervisor hypervisor);

    @SuppressWarnings("unchecked")
    public T lookupFrom(Modules<?> fromModule, Panels fromPanel, SyncHypervisor hypervisor) {
        PanelSyncManager syncManager = hypervisor.getSyncManager(fromModule, fromPanel);
        return (T) syncManager.findSyncHandler(getSyncId(fromModule, fromPanel, hypervisor));
    }

    public void notifyUpdateFrom(Modules<?> fromModule, Panels fromPanel, SyncHypervisor hypervisor) {
        T syncer = lookupFrom(fromModule, fromPanel, hypervisor);
        syncer.notifyUpdate();
    }

    protected String getSyncId(Modules<?> fromModule, Panels fromPanel, SyncHypervisor hypervisor) {
        if (inherited) {
            return syncId;
        }
        return fromPanel.getPanelId(fromModule, hypervisor) + "/" + syncId;
    }

    /** Sync values exclusive to the main Godforge controller. */
    public static class ForgeOfGodsSyncValue<T extends ValueSyncHandler<?>> extends SyncValue<T> {

        private final Function<ForgeOfGodsData, T> syncValueSupplier;

        public ForgeOfGodsSyncValue(String syncId) {
            super(syncId, true);
            this.syncValueSupplier = null;
        }

        public ForgeOfGodsSyncValue(String syncId, Function<ForgeOfGodsData, T> syncValueSupplier) {
            super(syncId, false);
            this.syncValueSupplier = syncValueSupplier;
        }

        @Override
        public T create(Modules<?> forModule, SyncHypervisor hypervisor) {
            if (inherited || syncValueSupplier == null) {
                throw new IllegalStateException("Cannot create SyncValue for inherited syncer! ID: " + syncId);
            }

            return syncValueSupplier.apply(hypervisor.getData());
        }

        // Shortcuts for convenience
        public void registerFor(Panels forPanel, SyncHypervisor hypervisor) {
            registerFor(Modules.CORE, forPanel, hypervisor);
        }

        public T create(SyncHypervisor hypervisor) {
            return create(Modules.CORE, hypervisor);
        }

        public T lookupFrom(Panels fromPanel, SyncHypervisor hypervisor) {
            return lookupFrom(Modules.CORE, fromPanel, hypervisor);
        }

        public void notifyUpdateFrom(Panels fromPanel, SyncHypervisor hypervisor) {
            notifyUpdateFrom(Modules.CORE, fromPanel, hypervisor);
        }
    }

    /** Sync values exclusive to a Godforge module. */
    public static class ModuleSyncValue<T extends ValueSyncHandler<?>, U extends MTEBaseModule> extends SyncValue<T> {

        private final Function<U, T> syncValueSupplier;

        public ModuleSyncValue(String syncId, Function<U, T> syncValueSupplier) {
            super(syncId, false);
            this.syncValueSupplier = syncValueSupplier;
        }

        @Override
        public T create(Modules<?> forModule, SyncHypervisor hypervisor) {
            if (inherited || syncValueSupplier == null) {
                throw new IllegalStateException("Cannot create SyncValue for inherited syncer! ID: " + syncId);
            }

            // noinspection unchecked
            U module = hypervisor.getModule((Modules<U>) forModule);
            if (module == null) {
                throw new IllegalStateException("Cannot create sync value for module with no module present");
            }

            return syncValueSupplier.apply(module);
        }
    }

    /**
     * Sync values that could be from the main Godforge or a module depending on hypervisor state.
     * Will prioritize the main Godforge if both are available.
     */
    public static class HybridSyncValue<T extends ValueSyncHandler<?>> extends SyncValue<T> {

        private final Function<ForgeOfGodsData, T> dataSupplier;
        private final Function<MTEBaseModule, T> moduleSupplier;

        public HybridSyncValue(String syncId, Function<ForgeOfGodsData, T> dataSupplier,
            Function<MTEBaseModule, T> moduleSupplier) {
            super(syncId, false);
            this.dataSupplier = dataSupplier;
            this.moduleSupplier = moduleSupplier;
        }

        @Override
        public T create(Modules<?> forModule, SyncHypervisor hypervisor) {
            if (inherited) {
                throw new IllegalStateException("Cannot create SyncValue for inherited syncer! ID: " + syncId);
            }

            if (hypervisor.getData() != null && dataSupplier != null) {
                return dataSupplier.apply(hypervisor.getData());
            }

            MTEBaseModule module = hypervisor.getModule(forModule);
            if (module != null && moduleSupplier != null) {
                return moduleSupplier.apply(module);
            }

            throw new IllegalStateException("Cannot create sync value as hypervisor has no applicable state");
        }
    }
}
