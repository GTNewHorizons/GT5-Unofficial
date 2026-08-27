package tectech.voidcraft.multiblock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import tectech.voidcraft.ship.VoidcraftBlueprint;
import tectech.voidcraft.ship.VoidcraftComponent;

/**
 * Registration of the Voidcraft multiblock components — the "allowed components" table the assemblers audit
 * against.
 *
 * <p>
 * Each component is its own controller MTE class carrying its own StructureLib structure definition, its catalog
 * entry (which also carries ALL of the component's stats — the existing catalog workflow), its casing entries,
 * and its structure cell count. Registering is one call: {@link #register(MTEVoidcraftMultiblockBase)}.
 *
 * <p>
 * The assemblers use this registry to (a) whitelist the component blocks when scanning the build volume,
 * (b) force each found controller's own structure check and audit the resulting cell counts
 * ({@link #auditScan}), and (c) recognize the component cells when clearing a digitized ship. The pure audit
 * rules live in {@link MultiblockAudit}.
 */
public final class VoidcraftMultiblockRegistry {

    private static final Map<VoidcraftComponent, Descriptor> BY_CONTROLLER = new EnumMap<>(VoidcraftComponent.class);
    private static final Map<VoidcraftComponent, Descriptor> BY_CASING = new EnumMap<>(VoidcraftComponent.class);

    private VoidcraftMultiblockRegistry() {
        throw new AssertionError("Static helpers");
    }

    /**
     * One registered multiblock component: its controller entry (the stats carrier), its zero-stat casing entries,
     * and the total structure cell count (1 controller + all casings).
     */
    public static final class Descriptor {

        public final VoidcraftComponent controller;
        public final List<VoidcraftComponent> casings;
        public final int expectedCells;

        Descriptor(VoidcraftComponent controller, List<VoidcraftComponent> casings, int expectedCells) {
            this.controller = controller;
            this.casings = casings;
            this.expectedCells = expectedCells;
        }
    }

    /**
     * Register one multiblock component from its controller MTE — the MTE class carries everything (structure,
     * catalog entry, casings, cell count), so this call just wires the entry into the allowed-components table.
     *
     * @throws IllegalStateException on a duplicate controller or casing entry
     */
    public static void register(MTEVoidcraftMultiblockBase controller) {
        VoidcraftComponent entry = controller.getControllerComponent();
        Descriptor descriptor = new Descriptor(entry, controller.getCasingComponents(), controller.getExpectedCells());
        if (BY_CONTROLLER.containsKey(entry)) {
            throw new IllegalStateException("Duplicate multiblock registration: " + entry);
        }
        for (VoidcraftComponent casing : descriptor.casings) {
            if (BY_CASING.containsKey(casing) || BY_CONTROLLER.containsKey(casing)) {
                throw new IllegalStateException("Duplicate multiblock block entry: " + casing);
            }
        }
        BY_CONTROLLER.put(entry, descriptor);
        for (VoidcraftComponent casing : descriptor.casings) {
            BY_CASING.put(casing, descriptor);
        }
    }

    /**
     * @param component the catalog entry
     * @return the descriptor of the registered component this entry controls, or null
     */
    @Nullable
    public static Descriptor byController(VoidcraftComponent component) {
        return component == null ? null : BY_CONTROLLER.get(component);
    }

    /**
     * @return the descriptors of all registered components (in registration order)
     */
    public static Collection<Descriptor> descriptors() {
        return Collections.unmodifiableCollection(BY_CONTROLLER.values());
    }

    /**
     * @param component the catalog entry
     * @return true when the entry is a registered multiblock component block (a controller or a casing of one)
     */
    public static boolean isMultiblockBlock(VoidcraftComponent component) {
        return component != null && (BY_CONTROLLER.containsKey(component) || BY_CASING.containsKey(component));
    }

    /**
     * The catalog entry an in-world multiblock component block MTE represents — the controller entry for a
     * controller MTE, the casing entry for a casing MTE.
     *
     * @param mte the in-world MTE
     * @return the (registered) catalog entry, or null when the MTE is not a multiblock component block
     */
    @Nullable
    public static VoidcraftComponent componentOf(IMetaTileEntity mte) {
        VoidcraftComponent entry = null;
        if (mte instanceof MTEVoidcraftMultiblockBase controller) {
            entry = controller.getControllerComponent();
        } else if (mte instanceof MTEVoidcraftMultiblockCasing casing) {
            entry = casing.getComponent();
        }
        return isMultiblockBlock(entry) ? entry : null;
    }

    /**
     * Audit the multiblock components found in a scanned volume: force each found controller's own structure
     * check, then run the pure cell-count audit over the scan.
     *
     * @param blueprint   the scanned blueprint (its grid carries the per-entry cell counts)
     * @param controllers the multiblock controller MTEs the scan found in the volume
     * @return the error keys (empty when the volume is fine; also empty when the volume holds no component
     *         controllers)
     */
    public static List<String> auditScan(VoidcraftBlueprint blueprint, List<MultiblockControllerRef> controllers) {
        if (controllers == null || controllers.isEmpty()) {
            return Collections.emptyList();
        }
        Map<VoidcraftComponent, Long> cells = new EnumMap<>(VoidcraftComponent.class);
        for (byte value : blueprint.grid) {
            VoidcraftComponent.fromGridValue(value)
                .ifPresent(component -> {
                    if (component.isMultiblock()) {
                        cells.merge(component, 1L, Long::sum);
                    }
                });
        }
        Map<VoidcraftComponent, int[]> formed = new EnumMap<>(VoidcraftComponent.class);
        for (MultiblockControllerRef ref : controllers) {
            boolean ok = ref.mte instanceof MTEMultiBlockBase machine && machine.checkStructure(true, ref.tile);
            int[] counts = formed.computeIfAbsent(ref.entry, component -> new int[2]);
            counts[0] += ok ? 1 : 0;
            counts[1] += 1;
        }
        List<MultiblockAudit.ComponentAudit> audits = new ArrayList<>();
        for (Descriptor descriptor : BY_CONTROLLER.values()) {
            Long controllerCells = cells.get(descriptor.controller);
            if (controllerCells == null) {
                continue;
            }
            long casingCells = 0;
            for (VoidcraftComponent casing : descriptor.casings) {
                casingCells += cells.getOrDefault(casing, 0L);
            }
            int[] counts = formed.get(descriptor.controller);
            int formedCount = counts == null ? 0 : counts[0];
            audits.add(
                new MultiblockAudit.ComponentAudit(
                    descriptor.controller,
                    descriptor.casings,
                    descriptor.expectedCells,
                    controllerCells,
                    casingCells,
                    formedCount));
        }
        return MultiblockAudit.audit(audits);
    }
}
