package tectech.voidcraft.render;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.metatileentity.MetaTileEntity;

/**
 * Per-assembler client visuals: an immutable state snapshot published by the assemblers from their
 * {@code onPostTick} (server thread) and consumed by {@link RenderVoidcraftAssembler} (render thread).
 * Snapshots are immutable and the registry is concurrent, so no extra synchronization is needed.
 *
 * <p>
 * The dimension id is stored instead of a world reference: in singleplayer the server MTE lives in the
 * {@code WorldServer} while the render thread reads the {@code WorldClient} — two different objects for the
 * same dimension — so identity checks against the rendered world would always skip the snapshot. The render
 * handler skips snapshots from other dimensions.
 *
 * <p>
 * The geometry helpers ({@link #scanAxes}, {@link #scanCell}, {@link #gridIndex}, {@link #planeDepths},
 * {@link #previewOffset}) are the single source of truth for the scan volume's placement — they mirror the
 * assemblers' own scan basis exactly (the machine's front face spanned by two perpendicular axes, the
 * build volume extending in front of it).
 */
public final class AssemblerVisuals {

    /** Both assemblers are 3 blocks deep (the machine body behind the controller). */
    public static final int MACHINE_DEPTH = 3;

    /**
     * Immutable per-assembler state.
     */
    public static final class Snapshot {

        /** The machine's dimension id (the render handler skips machines of other dimensions). */
        public final int dimensionId;
        /** The machine this snapshot describes (registry key). */
        public final MetaTileEntity machine;
        /** Controller block position — the scan volume origin. */
        public final int x, y, z;
        /** Machine front direction, ForgeDirection ordinal. */
        public final int facing;
        /** Scan volume size in blocks (cross-section width, cross-section height, depth along the front). */
        public final int volumeX, volumeY, volumeZ;
        /** True for the voidbase assembler (15×15×15 volume), false for the voidcraft (5×5×10). */
        public final boolean base;
        /** The assembler's own structure is formed and valid. */
        public final boolean machineValid;
        /** The assembler is actively digitizing (work bar running). */
        public final boolean scanning;

        Snapshot(int dimensionId, MetaTileEntity machine, int x, int y, int z, int facing, int volumeX, int volumeY,
            int volumeZ, boolean base, boolean machineValid, boolean scanning) {
            this.dimensionId = dimensionId;
            this.machine = machine;
            this.x = x;
            this.y = y;
            this.z = z;
            this.facing = facing;
            this.volumeX = volumeX;
            this.volumeY = volumeY;
            this.volumeZ = volumeZ;
            this.base = base;
            this.machineValid = machineValid;
            this.scanning = scanning;
        }

        /** True when other describes the same state (used for change detection in {@link #publish}). */
        public boolean equalsState(Snapshot other) {
            return other != null && dimensionId == other.dimensionId
                && x == other.x
                && y == other.y
                && z == other.z
                && facing == other.facing
                && volumeX == other.volumeX
                && volumeY == other.volumeY
                && volumeZ == other.volumeZ
                && base == other.base
                && machineValid == other.machineValid
                && scanning == other.scanning;
        }
    }

    private static final Map<MetaTileEntity, Snapshot> SNAPSHOTS = new ConcurrentHashMap<>();

    /**
     * Publish this machine's snapshot when its state changed. Called from the assembler's onPostTick
     * (server side only).
     */
    public static void publish(MetaTileEntity machine, int dimensionId, int x, int y, int z, int facing, int volumeX,
        int volumeY, int volumeZ, boolean base, boolean machineValid, boolean scanning) {
        Snapshot next = new Snapshot(
            dimensionId,
            machine,
            x,
            y,
            z,
            facing,
            volumeX,
            volumeY,
            volumeZ,
            base,
            machineValid,
            scanning);
        if (shouldReplace(SNAPSHOTS.get(machine), next)) {
            SNAPSHOTS.put(machine, next);
        }
    }

    /** True when the new snapshot should replace the current one (a missing current one always replaces). */
    static boolean shouldReplace(Snapshot current, Snapshot next) {
        return current == null || !current.equalsState(next);
    }

    /** Remove this machine's snapshot. Called from the assembler's onRemoval. */
    public static void unpublish(MetaTileEntity machine) {
        SNAPSHOTS.remove(machine);
    }

    /** Point-in-time copy of all current snapshots. Called from the render thread. */
    public static List<Snapshot> current() {
        return new ArrayList<>(SNAPSHOTS.values());
    }

    // ---- scan volume geometry (mirrors the assemblers' scanCell) ----

    /**
     * The two axes spanning the front face for the given front direction (the assembler's scan-plane basis):
     * facing up/down → the build plane is horizontal X-Z (a1 = +X, a2 = +Z); facing east/west → vertical Y-Z
     * (a1 = +Y, a2 = +Z); facing north/south → vertical X-Y (a1 = +X, a2 = +Y).
     *
     * @param frontOrdinal the machine's front direction (ForgeDirection ordinal)
     * @return {a1x, a1y, a1z, a2x, a2y, a2z}
     */
    public static double[] scanAxes(int frontOrdinal) {
        ForgeDirection front = ForgeDirection.getOrientation(frontOrdinal);
        double[] axes = new double[6];
        if (front.offsetY != 0) {
            axes[0] = 1.0;
            axes[5] = 1.0;
        } else if (front.offsetX != 0) {
            axes[1] = 1.0;
            axes[5] = 1.0;
        } else {
            axes[0] = 1.0;
            axes[4] = 1.0;
        }
        return axes;
    }

    /**
     * World position (block corner, integer) of the scan cell at (i, j, depth): i/j in [-half, half] across
     * the front face, depth in [1, volumeZ] along the front, relative to the controller block position.
     *
     * @return {x, y, z}
     */
    public static double[] scanCell(int i, int j, int depth, int bx, int by, int bz, int frontOrdinal, double[] axes) {
        ForgeDirection front = ForgeDirection.getOrientation(frontOrdinal);
        return new double[] { bx + front.offsetX * depth + axes[0] * i + axes[3] * j,
            by + front.offsetY * depth + axes[1] * i + axes[4] * j,
            bz + front.offsetZ * depth + axes[2] * i + axes[5] * j };
    }

    /**
     * Blueprint grid index of the scan cell (i, j, depth) — axis 0 = a1 (i), axis 1 = a2 (j), axis 2 = depth,
     * the same order the assemblers' scan writes.
     *
     * @param half   scan cross-section half size ((volumeSize - 1) / 2)
     * @param width  grid width (a1 axis size)
     * @param height grid height (a2 axis size)
     */
    public static int gridIndex(int i, int j, int depth, int half, int width, int height) {
        return i + half + width * (j + half + height * (depth - 1));
    }

    /**
     * Depth (in blocks from the controller block) of the two scanning planes at sweep phase t ∈ [0, 1]: at
     * t = 0 the planes sit at both volume ends, at t = 1 they meet in the middle, then they return to the
     * ends (smooth ping-pong sweep, t = (1 − cos(2π·phase)) / 2 on the render side).
     *
     * @return {near plane, far plane}
     */
    public static double[] planeDepths(double t, int depth) {
        double halfDepth = depth / 2.0;
        return new double[] { 0.5 + t * halfDepth, depth + 0.5 - t * halfDepth };
    }

    /**
     * World offset of the preview hologram position from the controller block center: behind the machine's
     * back face (the side opposite the construction area).
     * <ul>
     * <li>Horizontal-facing machine (3 deep, controller on the front face): behind the back face and
     * {@code machineHeight / 2 + height} above the controller — i.e. {@code height} above the machine's top
     * (the controller sits at the machine's vertical center).</li>
     * <li>Vertical-facing machine (machineHeight tall along the front axis, controller centered): directly
     * behind the back face, which is {@code machineHeight / 2} from the controller.</li>
     * </ul>
     *
     * @param frontOrdinal  the machine's front direction
     * @param machineHeight machine cross-section height in blocks (5 ship / 15 base)
     * @param distance      blocks behind the machine's back face
     * @param height        blocks above the machine's top (horizontal facing only)
     * @return {dx, dy, dz}
     */
    public static double[] previewOffset(int frontOrdinal, int machineHeight, double distance, double height) {
        ForgeDirection front = ForgeDirection.getOrientation(frontOrdinal);
        boolean vertical = front.offsetY != 0;
        double behind = vertical ? machineHeight / 2.0 + distance : MACHINE_DEPTH - 1 + 0.5 + distance;
        return new double[] { -front.offsetX * behind,
            -front.offsetY * behind + (vertical ? 0.0 : machineHeight / 2.0 + height), -front.offsetZ * behind };
    }

    private AssemblerVisuals() {
        throw new AssertionError("Static helpers");
    }
}
