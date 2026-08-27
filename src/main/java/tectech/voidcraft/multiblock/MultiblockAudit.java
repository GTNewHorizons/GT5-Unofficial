package tectech.voidcraft.multiblock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import tectech.voidcraft.ship.VoidcraftComponent;

/**
 * Pure audit of the multiblock component cells inside an assembler's scan volume (no world/MTE references —
 * testable in a bare JVM).
 *
 * <p>
 * Rules (per user spec):
 *
 * <ul>
 * <li>a component controller present in the volume whose own structure is NOT formed →
 * {@link #ERROR_INCOMPLETE} (the component has not been built as a multiblock — its stats do not apply).</li>
 * <li>a component whose controllers are ALL formed but whose cells in the volume are fewer than
 * {@code expectedCells × controllers} → {@link #ERROR_OUT_OF_VOLUME} (part of the structure sticks out of the
 * volume — digitizing would capture a broken component).</li>
 * <li>stray / extra casings (including casings without any controller) → no error: tolerated as inert mass (the
 * player can use them for decoration); they simply carry their small mass into the digitized stats.</li>
 * </ul>
 */
public final class MultiblockAudit {

    /** Structure of a found component is not formed. */
    public static final String ERROR_INCOMPLETE = "voidcraft_multiblock_incomplete";

    /** A formed component's structure extends beyond the scanned volume. */
    public static final String ERROR_OUT_OF_VOLUME = "voidcraft_multiblock_out_of_volume";

    private MultiblockAudit() {}

    /**
     * One audited component type: its registration data plus the cell counts of a single scan.
     */
    public static final class ComponentAudit {

        public final VoidcraftComponent controller;
        public final List<VoidcraftComponent> casings;
        public final int expectedCells;
        public final long controllerCells;
        public final long casingCells;
        public final int formedControllers;

        public ComponentAudit(VoidcraftComponent controller, List<VoidcraftComponent> casings, int expectedCells,
            long controllerCells, long casingCells, int formedControllers) {
            this.controller = controller;
            this.casings = casings;
            this.expectedCells = expectedCells;
            this.controllerCells = controllerCells;
            this.casingCells = casingCells;
            this.formedControllers = formedControllers;
        }
    }

    /**
     * @param audits one {@link ComponentAudit} per component type found in the volume
     * @return the error keys (empty when the volume is fine); each key appears at most once
     */
    public static List<String> audit(Collection<ComponentAudit> audits) {
        List<String> errors = new ArrayList<>();
        for (ComponentAudit a : audits) {
            if (a.controllerCells <= 0) {
                continue; // no controllers of this type in the volume: stray casings are tolerated (inert mass)
            }
            if (a.formedControllers < a.controllerCells) {
                addOnce(errors, ERROR_INCOMPLETE);
                continue;
            }
            long total = a.controllerCells + a.casingCells;
            long required = (long) a.expectedCells * a.controllerCells;
            if (total < required) {
                addOnce(errors, ERROR_OUT_OF_VOLUME);
            }
        }
        return Collections.unmodifiableList(errors);
    }

    private static void addOnce(List<String> errors, String key) {
        if (!errors.contains(key)) {
            errors.add(key);
        }
    }
}
