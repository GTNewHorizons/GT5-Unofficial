package tectech.voidcraft.ship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * A Voidcraft blueprint: the placed component grid (with per-cell facing), the per-face cover grid, and the stats
 * derived from them.
 *
 * <p>
 * Grid encodings (all {@code index = x + width * (y + height * z)}):
 *
 * <ul>
 * <li>{@link #grid}: 0 = empty cell, otherwise {@link VoidcraftComponent#toGridValue()} (component meta + 1).</li>
 * <li>{@link #facingGrid}: 0 = empty cell, otherwise {@code ForgeDirection ordinal + 1} (1..6) — the facing the
 * component block was placed with. A cell without facing data is treated as facing DOWN (the MTE default facing).</li>
 * <li>{@link #coverGrid}: length {@code cells * 6}, {@code index = cell * 6 + side}, 0 = no cover, otherwise
 * {@link VoidcraftCoverComponent#toGridValue()} (cover id + 1).</li>
 * </ul>
 *
 * <p>
 * This is the exact payload stored inside an {@code ItemVoidcraft} (see {@link VoidcraftNbt}).
 *
 * <p>
 * <b>Component model (pass 23).</b> Covers are the primary components: the ONLY placeable full blocks are the
 * {@link VoidcraftComponent#CONTROLLER} and the frame tiers ({@link VoidcraftComponent#FRAME} through
 * {@link VoidcraftComponent#FRAME_4}); every other catalog entry is cover-only, and a cell holding one fails
 * validation ({@code voidcraft_cover_only_component}). A ship needs at least one frame hull block
 * ({@code voidcraft_no_frame}) — the blocks whose faces accept the covers that carry all ship functionality. All
 * frames of a ship must be the SAME tier ({@code voidcraft_frame_tier_mismatch}); a frame tier only accepts covers
 * of its own tier or lower ({@code voidcraft_cover_tier_too_high}).
 *
 * <p>
 * <b>Integrity model (frame rework).</b> Each frame block contributes its tier's base integrity plus a per-face
 * term: a frame side facing another frame side adds {@code FRAME_FACE_INTEGRITY_BONUS}, a side exposed to air
 * (outside the volume or facing an empty cell) removes {@code FRAME_FACE_INTEGRITY_PENALTY}, a side facing the
 * controller or a multiblock block contributes nothing. A cover mounted on a frame side that faces a frame is an
 * "internal component" — the ship is invalid ({@code voidcraft_internal_component}). A ship with total integrity
 * &lt;= 0 is invalid ({@code voidcraft_integrity_too_low}).
 *
 * <p>
 * <b>Thrust model (pass 18/19, pass 23, pass 24 flip).</b> Thrust is a <em>single value</em>, not directional.
 * The assembler scans the volume in FRONT of its machine, so grid +Z is the FAR end (away from the assembler) and
 * grid −Z is the assembler side. A player builds the ship pointing away from the machine, so the FAR end (grid +Z)
 * is the ship's NOSE — in flight the ship travels away from where it was built and the cockpit leads (see
 * {@code RenderVoidcraftShip.headingFor}). The ship's BACK is the assembler side (grid −Z, {@link #BACK_FACE}).
 * Thrust comes ONLY from ENGINE covers (all four families: the fuel-less baseline nozzles plus the fuel-burning
 * Ion / Fusion Torch / Antimatter types) mounted on that back face (exhaust out the rear). Thrust = the plain sum
 * of those magnitudes. A ship may mount exactly ONE engine family ({@code voidcraft_engine_mismatch}).
 *
 * <p>
 * (Pass 18/20 had this inverted — nose on the assembler side, nozzle on the far end — which playtested as "the
 * nozzle has to face the front of the ship". Pass 24 flips the whole convention.)
 *
 * <p>
 * A thruster that does NOT face the back fails validation ({@code voidcraft_thruster_wrong_facing}), and a
 * back-facing thruster needs the {@link #EXHAUST_CLEARANCE} blocks directly on its exhaust side (grid −Z, toward
 * the assembler) free of Voidcraft blocks — a Voidcraft block there (the hull between the nozzle and the open
 * exhaust) blocks the thruster (zero thrust from it) and fails validation ({@code voidcraft_engine_blocked}).
 * The assembler machine and air on that side are not Voidcraft blocks, so a nozzle on the near end is always clear.
 * Both reasons surface in the assembler GUI.
 *
 * <p>
 * This class is pure Java (no Minecraft types) so the stat math stays unit-testable outside a game world.
 */
public final class VoidcraftBlueprint {

    /**
     * The ship's BACK face, as a ForgeDirection ordinal: NORTH (ordinal 2, −Z — the assembler side).
     *
     * <p>
     * Pass 24 (flipped): the assembler scans the volume in front of its machine, so grid +Z is the FAR end (away
     * from the assembler) and grid −Z is the assembler side. A player builds the ship pointing away from the
     * machine, so the FAR end (grid +Z) is the ship's NOSE — in flight the ship travels away from where it was
     * built (see {@code RenderVoidcraftShip.headingFor}). The rear is the assembler side, and a thruster fires
     * the ship forward when mounted on a cell's NORTH (−Z) face.
     */
    public static final int BACK_FACE = 2;

    /**
     * Thruster clearance: a back-facing thruster fires out its BACK face (grid −Z, the assembler side). It needs
     * the {@code EXHAUST_CLEARANCE} cells directly on that exhaust side (grid −Z, toward the assembler) free of
     * Voidcraft blocks, otherwise the thruster is blocked and contributes nothing. The assembler machine and air
     * out there are not Voidcraft blocks, so a nozzle on the near end (z=0) is always clear.
     */
    public static final int EXHAUST_CLEARANCE = 5;

    public final int width;
    public final int height;
    public final int depth;
    /** Component grid, length == width * height * depth. */
    public final byte[] grid;
    /** Per-cell facing, length == grid.length (0 = empty cell, 1..6 = ForgeDirection ordinal + 1). */
    public final byte[] facingGrid;
    /** Per-face covers, length == grid.length * 6 (0 = no cover, 1..8 = cover id + 1). */
    public final byte[] coverGrid;

    private VoidcraftBlueprint(int width, int height, int depth, byte[] grid, byte[] facingGrid, byte[] coverGrid) {
        int cells = width * height * depth;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.grid = grid;
        this.facingGrid = facingGrid == null ? new byte[cells] : facingGrid;
        this.coverGrid = coverGrid == null ? new byte[cells * 6] : coverGrid;
    }

    /**
     * Create a blueprint from a raw grid (no facing, no covers).
     *
     * @param width  grid width (1..MAX_DIM_X)
     * @param height grid height (1..MAX_DIM_Y)
     * @param depth  grid depth (1..MAX_DIM_Z)
     * @param grid   grid values (0 = empty, 1..9 = component); must be exactly width*height*depth long
     * @throws IllegalArgumentException if dimensions are out of range or the grid length mismatches
     */
    public static VoidcraftBlueprint of(int width, int height, int depth, byte[] grid) {
        return of(width, height, depth, grid, null, null);
    }

    /**
     * Create a blueprint from raw grids.
     *
     * @param width      grid width (1..MAX_DIM_X)
     * @param height     grid height (1..MAX_DIM_Y)
     * @param depth      grid depth (1..MAX_DIM_Z)
     * @param grid       grid values (0 = empty, 1..9 = component); must be exactly width*height*depth long
     * @param facingGrid per-cell facing (null = none, otherwise length cells; 0 = empty cell, 1..6 = ordinal + 1)
     * @param coverGrid  per-face covers (null = none, otherwise length cells * 6; 0 = none, 1..8 = cover id + 1)
     * @throws IllegalArgumentException if dimensions, lengths or values are invalid
     */
    public static VoidcraftBlueprint of(int width, int height, int depth, byte[] grid, byte[] facingGrid,
        byte[] coverGrid) {
        return create(
            width,
            height,
            depth,
            grid,
            facingGrid,
            coverGrid,
            VoidcraftConstants.MAX_DIM_X,
            VoidcraftConstants.MAX_DIM_Y,
            VoidcraftConstants.MAX_DIM_Z);
    }

    /**
     * Create a Voidbase blueprint from a raw grid (no facing, no covers).
     *
     * @param width  grid width (1..MAX_BASE_DIM)
     * @param height grid height (1..MAX_BASE_DIM)
     * @param depth  grid depth (1..MAX_BASE_DIM)
     * @param grid   grid values (0 = empty, 1..11 = component); must be exactly width*height*depth long
     * @throws IllegalArgumentException if dimensions are out of range or the grid length mismatches
     */
    public static VoidcraftBlueprint ofBase(int width, int height, int depth, byte[] grid) {
        return ofBase(width, height, depth, grid, null, null);
    }

    /**
     * Create a Voidbase blueprint from raw grids (the 15×15×15 volume —
     * {@link VoidcraftConstants#MAX_BASE_DIM}). A base is an immobile station: the ship's structural rules apply,
     * but the thruster rules do not (see {@link #validateForBase}).
     *
     * @param width      grid width (1..MAX_BASE_DIM)
     * @param height     grid height (1..MAX_BASE_DIM)
     * @param depth      grid depth (1..MAX_BASE_DIM)
     * @param grid       grid values (0 = empty, 1..11 = component); must be exactly width*height*depth long
     * @param facingGrid per-cell facing (null = none, otherwise length cells; 0 = empty cell, 1..6 = ordinal + 1)
     * @param coverGrid  per-face covers (null = none, otherwise length cells * 6; 0 = none, 1..10 = cover id + 1)
     * @throws IllegalArgumentException if dimensions, lengths or values are invalid
     */
    public static VoidcraftBlueprint ofBase(int width, int height, int depth, byte[] grid, byte[] facingGrid,
        byte[] coverGrid) {
        return create(
            width,
            height,
            depth,
            grid,
            facingGrid,
            coverGrid,
            VoidcraftConstants.MAX_BASE_DIM,
            VoidcraftConstants.MAX_BASE_DIM,
            VoidcraftConstants.MAX_BASE_DIM);
    }

    /**
     * Map a cover's WORLD-facing ordinal into the blueprint's GRID-side ordinal.
     *
     * <p>
     * The grid's +Z axis is the assembler's FRONT direction, while cover ordinals are world directions — so a
     * cover's meaning relative to the ship/station depends on how the assembler faces. This maps it into grid
     * space with the same basis as the assembler scan (grid +X / grid +Y = the scan's in-plane axes, grid +Z =
     * the front itself), so a cover pointing TOWARD the assembler becomes grid side 2 ({@link #BACK_FACE}) for
     * every assembler orientation.
     *
     * @param frontX    the assembler front direction, x component (one of the six unit axes)
     * @param frontY    the assembler front direction, y component
     * @param frontZ    the assembler front direction, z component
     * @param worldSide the cover's world-facing ordinal (ForgeDirection: 0 DOWN, 1 UP, 2 NORTH, 3 SOUTH, 4
     *                  WEST, 5 EAST)
     * @return the grid-side ordinal (0..5) of the same direction
     */
    public static int toGridSide(int frontX, int frontY, int frontZ, int worldSide) {
        // grid basis in world coordinates (mirrors the assembler scan's a1/a2; grid +Z is the front itself)
        final int[] ex, ey;
        if (frontY != 0) {
            ex = new int[] { 1, 0, 0 };
            ey = new int[] { 0, 0, 1 };
        } else if (frontX != 0) {
            ex = new int[] { 0, 1, 0 };
            ey = new int[] { 0, 0, 1 };
        } else {
            ex = new int[] { 1, 0, 0 };
            ey = new int[] { 0, 1, 0 };
        }
        final int[] ez = { frontX, frontY, frontZ };
        // MC world direction of the cover's facing ordinal
        final int[] d;
        switch (worldSide) {
            case 0:
                d = new int[] { 0, -1, 0 }; // DOWN
                break;
            case 1:
                d = new int[] { 0, 1, 0 }; // UP
                break;
            case 2:
                d = new int[] { 0, 0, -1 }; // NORTH
                break;
            case 3:
                d = new int[] { 0, 0, 1 }; // SOUTH
                break;
            case 4:
                d = new int[] { -1, 0, 0 }; // WEST
                break;
            default:
                d = new int[] { 1, 0, 0 }; // EAST
                break;
        }
        int gx = d[0] * ex[0] + d[1] * ex[1] + d[2] * ex[2];
        int gy = d[0] * ey[0] + d[1] * ey[1] + d[2] * ey[2];
        int gz = d[0] * ez[0] + d[1] * ez[1] + d[2] * ez[2];
        if (gz != 0) {
            return gz > 0 ? 3 : 2; // +Z (away) / -Z (toward the assembler = the ship's back)
        }
        if (gy != 0) {
            return gy > 0 ? 1 : 0;
        }
        return gx > 0 ? 5 : 4;
    }

    private static VoidcraftBlueprint create(int width, int height, int depth, byte[] grid, byte[] facingGrid,
        byte[] coverGrid, int maxW, int maxH, int maxD) {
        if (width < 1 || width > maxW || height < 1 || height > maxH || depth < 1 || depth > maxD) {
            throw new IllegalArgumentException("Grid dimensions out of bounds: " + width + "x" + height + "x" + depth);
        }
        int cells = width * height * depth;
        if (grid == null || grid.length != cells) {
            throw new IllegalArgumentException("Grid length " + (grid == null ? -1 : grid.length) + " != " + cells);
        }
        for (byte b : grid) {
            if (b == 0) {
                continue; // empty cell
            }
            if (VoidcraftComponent.fromGridValue(b)
                .isEmpty()) {
                throw new IllegalArgumentException("Unknown grid value: " + b);
            }
        }
        if (facingGrid != null && facingGrid.length != cells) {
            throw new IllegalArgumentException("Facing grid length " + facingGrid.length + " != " + cells);
        }
        for (byte b : facingGrid == null ? new byte[0] : facingGrid) {
            if (b == 0) {
                continue; // empty cell
            }
            if (b < 1 || b > 6) {
                throw new IllegalArgumentException("Unknown facing value: " + b);
            }
        }
        if (coverGrid != null && coverGrid.length != cells * 6) {
            throw new IllegalArgumentException("Cover grid length " + coverGrid.length + " != " + cells * 6);
        }
        for (byte b : coverGrid == null ? new byte[0] : coverGrid) {
            if (b == 0) {
                continue; // no cover
            }
            if (VoidcraftCoverComponent.fromGridValue(b)
                .isEmpty()) {
                throw new IllegalArgumentException("Unknown cover grid value: " + b);
            }
        }
        return new VoidcraftBlueprint(
            width,
            height,
            depth,
            grid.clone(),
            facingGrid == null ? null : facingGrid.clone(),
            coverGrid == null ? null : coverGrid.clone());
    }

    /** @return index of a cell in the base grid */
    public int cellIndex(int x, int y, int z) {
        return x + width * (y + height * z);
    }

    /** @return index of a face in the cover grid */
    public int coverIndex(int cell, int side) {
        return cell * 6 + side;
    }

    /** @return the facing ordinal (0..5) stored for a cell; 0 (DOWN) when the cell is empty or has no facing data */
    public int facingOf(int cell) {
        byte b = facingGrid[cell];
        return b == 0 ? 0 : b - 1;
    }

    /** @return the cover mounted on a cell's face (empty when none) */
    public Optional<VoidcraftCoverComponent> coverOf(int cell, int side) {
        return VoidcraftCoverComponent.fromGridValue(coverGrid[cell * 6 + side]);
    }

    /**
     * Trim fully-empty outer layers down to the minimal bounding box of the placed components.
     * Facing and cover data travel with their cell.
     *
     * @return the trimmed blueprint (this instance if nothing changed)
     */
    public VoidcraftBlueprint trim() {
        int minX = width, maxX = -1, minY = height, maxY = -1, minZ = depth, maxZ = -1;
        for (int z = 0; z < depth; z++) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (grid[x + width * (y + height * z)] != 0) {
                        minX = Math.min(minX, x);
                        maxX = Math.max(maxX, x);
                        minY = Math.min(minY, y);
                        maxY = Math.max(maxY, y);
                        minZ = Math.min(minZ, z);
                        maxZ = Math.max(maxZ, z);
                    }
                }
            }
        }
        if (maxX < 0) {
            return this; // completely empty — keep as-is, validation will reject it
        }
        int tw = maxX - minX + 1, th = maxY - minY + 1, td = maxZ - minZ + 1;
        byte[] trimmed = new byte[tw * th * td];
        byte[] trimmedFacing = new byte[tw * th * td];
        byte[] trimmedCovers = new byte[tw * th * td * 6];
        for (int z = 0; z < td; z++) {
            for (int y = 0; y < th; y++) {
                for (int x = 0; x < tw; x++) {
                    int src = (x + minX) + width * ((y + minY) + height * (z + minZ));
                    int dst = x + tw * (y + th * z);
                    trimmed[dst] = grid[src];
                    trimmedFacing[dst] = facingGrid[src];
                    System.arraycopy(coverGrid, src * 6, trimmedCovers, dst * 6, 6);
                }
            }
        }
        return new VoidcraftBlueprint(tw, th, td, trimmed, trimmedFacing, trimmedCovers);
    }

    /**
     * @return total number of parts: occupied cells + mounted covers
     */
    public int componentCount() {
        int count = 0;
        for (byte b : grid) {
            if (b != 0) count++;
        }
        for (byte b : coverGrid) {
            if (b != 0) count++;
        }
        return count;
    }

    /**
     * @param component the component to count (base grid only)
     * @return how many of it are in the grid
     */
    public int count(VoidcraftComponent component) {
        int count = 0;
        byte target = (byte) component.toGridValue();
        for (byte b : grid) {
            if (b == target) count++;
        }
        return count;
    }

    /**
     * @param cover the cover to count
     * @return how many of it are mounted on faces
     */
    public int countCover(VoidcraftCoverComponent cover) {
        int count = 0;
        byte target = (byte) cover.toGridValue();
        for (byte b : coverGrid) {
            if (b == target) count++;
        }
        return count;
    }

    /**
     * Sum of one stat over all components in the base grid.
     *
     * @param stat one of the {@link VoidcraftComponent} stat getters
     * @return total
     */
    public long total(java.util.function.ToLongFunction<VoidcraftComponent> stat) {
        long total = 0;
        for (byte b : grid) {
            if (b == 0) continue;
            total += stat.applyAsLong(
                VoidcraftComponent.fromGridValue(b)
                    .orElseThrow());
        }
        return total;
    }

    /**
     * @return the ship's single thrust value (pass 18/20/23): the sum of the thrust of every ENGINE cover (all four
     *         families) mounted on the ship's BACK ({@link #BACK_FACE}) that is not blocked
     *         ({@link #isExhaustBlocked(int)}). Engines mounted on any other face, or blocked by the ship's own
     *         hull, contribute nothing. (Pass 23: engines are covers only — there is no block engine.)
     */
    public long totalThrust() {
        long total = 0;
        for (int cell = 0; cell < grid.length; cell++) {
            VoidcraftCoverComponent cover = VoidcraftCoverComponent.fromGridValue(coverGrid[cell * 6 + BACK_FACE])
                .orElse(null);
            if (cover != null && cover.isEngine() && !isExhaustBlocked(cell)) {
                total += cover.getThrust();
            }
        }
        return total;
    }

    /**
     * Pass 24: is the thruster at {@code cell} blocked?
     *
     * <p>
     * The thruster fires out its BACK face (grid −Z, the assembler side — {@link #BACK_FACE}). The
     * {@link #EXHAUST_CLEARANCE} cells directly on that EXHAUST side (grid −Z: z−1…z−5 from the thruster cell —
     * toward the assembler) must be free of Voidcraft component blocks. "Empty" = no Voidcraft block; the assembler
     * machine and any air out there are NOT Voidcraft blocks, so they never block the exhaust. A Voidcraft block in
     * any of those 5 cells (i.e. the hull is between the nozzle and the open exhaust) blocks the thruster.
     *
     * <p>
     * A nozzle on the ship's near end (grid z=0, the assembler end) always has a clear exhaust — its exhaust cells
     * are outside the hull (toward the assembler), so it contributes thrust with no clearance requirement.
     *
     * @param cell the thruster's cell index
     * @return true if any of the 5 blocks on the thruster's exhaust side is a Voidcraft block
     */
    private boolean isExhaustBlocked(int cell) {
        int x = cell % width;
        int y = (cell / width) % height;
        int z = cell / (width * height);
        for (int dz = 1; dz <= EXHAUST_CLEARANCE; dz++) {
            int z2 = z - dz;
            if (z2 < 0) {
                break; // toward the assembler = air / the assembler machine, which never blocks the exhaust
            }
            if (grid[x + width * (y + height * z2)] != 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * The cell directly on the given face of {@code cell}, or -1 when that face is on the bounding volume (air).
     *
     * @param cell the cell index
     * @param side the face side (ForgeDirection ordinal: 0 DOWN, 1 UP, 2 NORTH, 3 SOUTH, 4 WEST, 5 EAST)
     * @return the neighbouring cell index, or -1
     */
    private int neighborCell(int cell, int side) {
        int x = cell % width;
        int y = (cell / width) % height;
        int z = cell / (width * height);
        int nx = x + (side == 4 ? -1 : side == 5 ? 1 : 0);
        int ny = y + (side == 0 ? -1 : side == 1 ? 1 : 0);
        int nz = z + (side == 2 ? -1 : side == 3 ? 1 : 0);
        if (nx < 0 || nx >= width || ny < 0 || ny >= height || nz < 0 || nz >= depth) {
            return -1;
        }
        return nx + width * (ny + height * nz);
    }

    /**
     * @return the frame tier in use by the grid's frame blocks (all frames one tier — validation enforces it),
     *         or 0 when the grid holds no frame.
     */
    private int frameTierInUse() {
        for (VoidcraftComponent frame : new VoidcraftComponent[] { VoidcraftComponent.FRAME, VoidcraftComponent.FRAME_2,
            VoidcraftComponent.FRAME_3, VoidcraftComponent.FRAME_4 }) {
            if (count(frame) > 0) {
                return frame.getTier();
            }
        }
        return 0;
    }

    /**
     * @return how many frame blocks (of ANY tier) are in the grid.
     */
    public int countFrames() {
        int count = 0;
        for (byte b : grid) {
            if (b != 0 && VoidcraftComponent.fromGridValue(b)
                .orElseThrow()
                .isFrame()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Compute the ship's stats from the component and cover grids.
     *
     * <p>
     * Thrust is a single value: every back-facing engine cover ({@link #BACK_FACE}) that is not blocked
     * ({@link #isExhaustBlocked(int)}) counts; anything aimed elsewhere or blocked by the hull contributes
     * nothing — see {@link #totalThrust()}.
     *
     * <p>
     * Integrity: flat sums for the controller, covers and multiblock blocks, plus the per-FACE model for frames —
     * each frame block adds its tier's base integrity, then per side: facing a frame adds
     * {@code FRAME_FACE_INTEGRITY_BONUS}, facing air (out of bounds or an empty cell) removes
     * {@code FRAME_FACE_INTEGRITY_PENALTY}, facing the controller or a multiblock block contributes nothing.
     *
     * <p>
     * Fuel / engines: {@code engineType} is the single engine family present (NONE id when there are no engine
     * covers), {@code thrusterCount} the number of its engine covers (drives the per-thruster travel draws),
     * {@code fuelCapacity} the sum of the Fuel Storage covers' mB capacities.
     */
    public VoidcraftStats computeStats() {
        long mass = 0, cargoSlots = 0, miningPower = 0, scanPower = 0, constructionPower = 0, starlifterPower = 0,
            logisticsPower = 0, energyBuffer = 0, energyDraw = 0, energyGen = 0, integrity = 0;
        long thrust = 0;
        long fuelCapacity = 0, thrusterCount = 0;
        int engineType = VoidcraftEngineType.NONE.id();
        boolean engineTypeSet = false;

        for (int cell = 0; cell < grid.length; cell++) {
            if (grid[cell] != 0) {
                // Pass 23: full blocks are the controller and the frame tiers — structural mass.
                // ALL function stats (thrust, cargo, mining, scan, construction, starlifter, energy) come from
                // the covers mounted on their faces.
                VoidcraftComponent component = VoidcraftComponent.fromGridValue(grid[cell])
                    .orElseThrow();
                mass += component.getMass();
                cargoSlots += component.getCargoSlots();
                miningPower += component.getMiningPower();
                scanPower += component.getScanPower();
                constructionPower += component.getConstructionPower();
                starlifterPower += component.getStarlifterPower();
                logisticsPower += component.getLogisticsPower();
                energyBuffer += component.getEnergyBuffer();
                energyDraw += component.getEnergyDraw();
                energyGen += component.getEnergyGen();
                if (component.isFrame()) {
                    // Frame integrity = tier base + the per-face model (frame side vs air / frame / other block).
                    integrity += component.getIntegrity();
                    for (int side = 0; side < 6; side++) {
                        int neighbour = neighborCell(cell, side);
                        if (neighbour < 0 || grid[neighbour] == 0) {
                            integrity -= VoidcraftConstants.FRAME_FACE_INTEGRITY_PENALTY;
                        } else if (VoidcraftComponent.fromGridValue(grid[neighbour])
                            .orElseThrow()
                            .isFrame()) {
                                integrity += VoidcraftConstants.FRAME_FACE_INTEGRITY_BONUS;
                            }
                    }
                } else {
                    integrity += component.getIntegrity();
                }
            }
            for (int side = 0; side < 6; side++) {
                int value = coverGrid[cell * 6 + side];
                if (value == 0) {
                    continue;
                }
                VoidcraftCoverComponent cover = VoidcraftCoverComponent.fromGridValue(value)
                    .orElseThrow();
                mass += cover.getMass();
                cargoSlots += cover.getCargoSlots();
                miningPower += cover.getMiningPower();
                scanPower += cover.getScanPower();
                constructionPower += cover.getConstructionPower();
                starlifterPower += cover.getStarlifterPower();
                logisticsPower += cover.getLogisticsPower();
                energyBuffer += cover.getEnergyBuffer();
                energyDraw += cover.getEnergyDraw();
                energyGen += cover.getEnergyGen();
                integrity += cover.getIntegrity();
                fuelCapacity += cover.getFuelCapacity();
                if (cover.isEngine()) {
                    thrusterCount++;
                    int type = cover.getEngineType()
                        .id();
                    if (!engineTypeSet) {
                        engineType = type;
                        engineTypeSet = true;
                    }
                    if (side == BACK_FACE && !isExhaustBlocked(cell)) {
                        thrust += cover.getThrust();
                    }
                }
            }
        }

        return new VoidcraftStats(
            mass,
            thrust,
            cargoSlots,
            miningPower,
            scanPower,
            constructionPower,
            starlifterPower,
            logisticsPower,
            energyBuffer,
            energyDraw,
            energyGen,
            integrity,
            fuelCapacity,
            engineType,
            thrusterCount,
            frameTierInUse());
    }

    /**
     * Highest tier in use (base components and covers).
     */
    public int maxTier() {
        int max = 0;
        for (byte b : grid) {
            if (b != 0) {
                max = Math.max(
                    max,
                    VoidcraftComponent.fromGridValue(b)
                        .orElseThrow()
                        .getTier());
            }
        }
        for (byte b : coverGrid) {
            if (b != 0) {
                max = Math.max(
                    max,
                    VoidcraftCoverComponent.fromGridValue(b)
                        .orElseThrow()
                        .getTier());
            }
        }
        return max;
    }

    /**
     * Validate the blueprint as a digitizable ship.
     *
     * <p>
     * Pass 23 rules: only the controller and the frame tiers are placeable full blocks — a cell holding any
     * cover-only component fails with {@code voidcraft_cover_only_component}, and the ship needs at least one
     * frame hull block ({@code voidcraft_no_frame}). All frames of a ship must be the same tier
     * ({@code voidcraft_frame_tier_mismatch}); a frame tier only accepts covers of its own tier or lower
     * ({@code voidcraft_cover_tier_too_high}). A cover on a frame side that faces a frame is an internal component
     * — invalid ({@code voidcraft_internal_component}).
     *
     * <p>
     * Engine rules: every engine cover (all four families) must be mounted on the ship's BACK face
     * ({@link #BACK_FACE}, the assembler side), and a back-facing engine needs the {@link #EXHAUST_CLEARANCE}
     * blocks directly on its exhaust side (grid −Z, toward the assembler) free of Voidcraft blocks. A ship may
     * carry exactly ONE engine family ({@code voidcraft_engine_mismatch}). A violation breaks the digitization and
     * is reported in the assembler GUI:
     *
     * <ul>
     * <li>{@code voidcraft_no_engine} — no engine cover at all</li>
     * <li>{@code voidcraft_thruster_wrong_facing} — an engine does not face the back</li>
     * <li>{@code voidcraft_engine_blocked} — a Voidcraft block sits on the exhaust side of a back-facing engine
     * (within 5 cells, i.e. the hull is between the engine and the open exhaust toward the assembler)</li>
     * <li>{@code voidcraft_engine_mismatch} — the ship carries covers of more than one engine family</li>
     * <li>{@code voidcraft_cover_only_component} — a full-block cell holds a cover-only part (pass 23)</li>
     * <li>{@code voidcraft_no_frame} — no frame hull block (pass 23)</li>
     * <li>{@code voidcraft_frame_tier_mismatch} — frames of more than one tier on one ship</li>
     * <li>{@code voidcraft_cover_tier_too_high} — a cover on a frame face exceeds that frame's tier</li>
     * <li>{@code voidcraft_internal_component} — a cover on a frame face that faces a frame</li>
     * <li>{@code voidcraft_integrity_too_low} — the ship's total integrity is &lt;= 0 (the frame face model
     * penalizes exposed sides; hollow frames can bleed to nothing)</li>
     * <li>{@code voidcraft_launcher_station_only} — a station-only infrastructure controller (the Satellite Rail
     * Launcher or one of the four star-infrastructure components) is on the grid (bases, not ships)</li>
     * </ul>
     *
     * @param maxComponentTier highest component/cover tier the assembler (circuit) may digitize
     * @param errorsOut        receives failure reason keys (e.g. {@code voidcraft_no_engine}); may be null
     * @return true if the blueprint is a valid ship
     */
    public boolean validate(int maxComponentTier, List<String> errorsOut) {
        List<String> errors = errorsOut == null ? new ArrayList<>() : errorsOut;
        boolean ok = true;

        if (componentCount() < VoidcraftConstants.MIN_COMPONENT_COUNT) {
            errors.add("voidcraft_too_small");
            ok = false;
        }

        if (count(VoidcraftComponent.CONTROLLER) != 1) {
            errors.add("voidcraft_controller_count");
            ok = false;
        }

        // Pass 23: covers are primary — only the controller and the frame tiers are placeable full blocks.
        boolean coverOnlyPresent = false;
        for (byte b : grid) {
            if (b != 0 && VoidcraftComponent.fromGridValue(b)
                .orElseThrow()
                .isCoverOnly()) {
                coverOnlyPresent = true;
                break;
            }
        }
        if (coverOnlyPresent) {
            errors.add("voidcraft_cover_only_component");
            ok = false;
        }

        if (countFrames() < 1) {
            errors.add("voidcraft_no_frame");
            ok = false;
        }

        // Frame tier uniformity: all frames of a ship the same tier (multiblock blocks and the controller are
        // exempt — only frame entries take part).
        int frameTier = -1;
        boolean frameTierMismatch = false;
        for (byte b : grid) {
            if (b == 0) {
                continue;
            }
            VoidcraftComponent component = VoidcraftComponent.fromGridValue(b)
                .orElseThrow();
            if (component.isFrame()) {
                if (frameTier == -1) {
                    frameTier = component.getTier();
                } else if (component.getTier() != frameTier) {
                    frameTierMismatch = true;
                    break;
                }
            }
        }
        if (frameTierMismatch) {
            errors.add("voidcraft_frame_tier_mismatch");
            ok = false;
        }

        if (maxTier() > maxComponentTier) {
            errors.add("voidcraft_tier_too_high");
            ok = false;
        }

        // Station-only infrastructure (the Satellite Rail Launcher and the four star-infrastructure components)
        // operates from a base anchored to the star — a ship build containing it is rejected outright.
        if (count(VoidcraftComponent.SATELLITE_LAUNCHER) > 0 || count(VoidcraftComponent.STELLAR_INJECTOR) > 0
            || count(VoidcraftComponent.CONTINUUM_STABILIZER) > 0
            || count(VoidcraftComponent.STELLAR_LENS) > 0
            || count(VoidcraftComponent.STABILIZATION_MATRIX) > 0) {
            errors.add("voidcraft_launcher_station_only");
            ok = false;
        }

        // Frame face audit: a cover may only mount on a frame face of a tier the frame can host, and never on a
        // frame face that faces another frame (an "internal" component).
        boolean coverTierTooHigh = false;
        boolean internalComponent = false;
        for (int cell = 0; cell < grid.length; cell++) {
            int value = grid[cell];
            if (value == 0) {
                continue;
            }
            VoidcraftComponent component = VoidcraftComponent.fromGridValue(value)
                .orElseThrow();
            if (!component.isFrame()) {
                continue; // covers on the controller face are not tier-gated by a frame
            }
            for (int side = 0; side < 6; side++) {
                int coverValue = coverGrid[cell * 6 + side];
                if (coverValue == 0) {
                    continue;
                }
                VoidcraftCoverComponent cover = VoidcraftCoverComponent.fromGridValue(coverValue)
                    .orElseThrow();
                if (cover.getTier() > component.getTier()) {
                    coverTierTooHigh = true;
                }
                int neighbour = neighborCell(cell, side);
                if (neighbour >= 0 && grid[neighbour] != 0
                    && VoidcraftComponent.fromGridValue(grid[neighbour])
                        .orElseThrow()
                        .isFrame()) {
                    internalComponent = true;
                }
            }
        }
        if (coverTierTooHigh) {
            errors.add("voidcraft_cover_tier_too_high");
            ok = false;
        }
        if (internalComponent) {
            errors.add("voidcraft_internal_component");
            ok = false;
        }

        // Engine audit: every engine cover (all four families) on the back face with a clear exhaust path, and
        // exactly one engine family per ship.
        boolean hasEngine = false;
        boolean wrongFacing = false;
        boolean blocked = false;
        boolean engineMismatch = false;
        int engineFamily = -1;
        for (int cell = 0; cell < grid.length; cell++) {
            for (int side = 0; side < 6; side++) {
                int value = coverGrid[cell * 6 + side];
                if (value == 0) {
                    continue;
                }
                VoidcraftCoverComponent cover = VoidcraftCoverComponent.fromGridValue(value)
                    .orElseThrow();
                if (cover.isEngine()) {
                    hasEngine = true;
                    int family = cover.getEngineType()
                        .id();
                    if (engineFamily == -1) {
                        engineFamily = family;
                    } else if (family != engineFamily) {
                        engineMismatch = true;
                    }
                    if (side != BACK_FACE) {
                        wrongFacing = true;
                    } else if (isExhaustBlocked(cell)) {
                        blocked = true;
                    }
                }
            }
        }

        if (!hasEngine) {
            errors.add("voidcraft_no_engine");
            ok = false;
        }

        if (engineMismatch) {
            errors.add("voidcraft_engine_mismatch");
            ok = false;
        }

        if (wrongFacing) {
            errors.add("voidcraft_thruster_wrong_facing");
            ok = false;
        }

        if (blocked) {
            errors.add("voidcraft_engine_blocked");
            ok = false;
        }

        if (computeStats().integrity <= 0L) {
            errors.add("voidcraft_integrity_too_low");
            ok = false;
        }

        return ok;
    }

    /**
     * Validate the blueprint as a digitizable Voidbase (an immobile station): the ship's structural rules — part
     * count, exactly one controller, cover-only parts, at least one frame, frame tier uniformity, the per-frame
     * cover tier gate and the internal-component rule, tier — WITHOUT the thruster rules, but with engine covers
     * FORBIDDEN outright (a base cannot fly; any thruster on the grid is rejected, not inert).
     *
     * <p>
     * Failure reason keys (assembler GUI):
     *
     * <ul>
     * <li>{@code voidcraft_too_small} — fewer than {@link VoidcraftConstants#MIN_COMPONENT_COUNT} parts</li>
     * <li>{@code voidcraft_controller_count} — not exactly one controller</li>
     * <li>{@code voidcraft_cover_only_component} — a full-block cell holds a cover-only part</li>
     * <li>{@code voidcraft_no_frame} — no frame hull block</li>
     * <li>{@code voidcraft_frame_tier_mismatch} — frames of more than one tier on one station</li>
     * <li>{@code voidcraft_cover_tier_too_high} — a cover on a frame face exceeds that frame's tier</li>
     * <li>{@code voidcraft_internal_component} — a cover on a frame face that faces a frame</li>
     * <li>{@code voidcraft_integrity_too_low} — the station's total integrity is &lt;= 0</li>
     * <li>{@code voidcraft_tier_too_high} — a part exceeds the assembler circuit tier</li>
     * <li>{@code voidbase_thruster_forbidden} — an engine cover of any family is on the grid (bases are immobile)
     * </li>
     * </ul>
     *
     * @param maxComponentTier highest component/cover tier the assembler (circuit) may digitize
     * @param errorsOut        receives failure reason keys; may be null
     * @return true if the blueprint is a valid base
     */
    public boolean validateForBase(int maxComponentTier, List<String> errorsOut) {
        List<String> errors = errorsOut == null ? new ArrayList<>() : errorsOut;
        boolean ok = true;

        if (componentCount() < VoidcraftConstants.MIN_COMPONENT_COUNT) {
            errors.add("voidcraft_too_small");
            ok = false;
        }

        if (count(VoidcraftComponent.CONTROLLER) != 1) {
            errors.add("voidcraft_controller_count");
            ok = false;
        }

        boolean coverOnlyPresent = false;
        for (byte b : grid) {
            if (b != 0 && VoidcraftComponent.fromGridValue(b)
                .orElseThrow()
                .isCoverOnly()) {
                coverOnlyPresent = true;
                break;
            }
        }
        if (coverOnlyPresent) {
            errors.add("voidcraft_cover_only_component");
            ok = false;
        }

        if (countFrames() < 1) {
            errors.add("voidcraft_no_frame");
            ok = false;
        }

        int frameTier = -1;
        boolean frameTierMismatch = false;
        for (byte b : grid) {
            if (b == 0) {
                continue;
            }
            VoidcraftComponent component = VoidcraftComponent.fromGridValue(b)
                .orElseThrow();
            if (component.isFrame()) {
                if (frameTier == -1) {
                    frameTier = component.getTier();
                } else if (component.getTier() != frameTier) {
                    frameTierMismatch = true;
                    break;
                }
            }
        }
        if (frameTierMismatch) {
            errors.add("voidcraft_frame_tier_mismatch");
            ok = false;
        }

        if (maxTier() > maxComponentTier) {
            errors.add("voidcraft_tier_too_high");
            ok = false;
        }

        // Frame face audit (same rule as ships): covers only on tier-permitted frame faces, never facing a frame.
        boolean coverTierTooHigh = false;
        boolean internalComponent = false;
        for (int cell = 0; cell < grid.length; cell++) {
            int value = grid[cell];
            if (value == 0) {
                continue;
            }
            VoidcraftComponent component = VoidcraftComponent.fromGridValue(value)
                .orElseThrow();
            if (!component.isFrame()) {
                continue;
            }
            for (int side = 0; side < 6; side++) {
                int coverValue = coverGrid[cell * 6 + side];
                if (coverValue == 0) {
                    continue;
                }
                VoidcraftCoverComponent cover = VoidcraftCoverComponent.fromGridValue(coverValue)
                    .orElseThrow();
                if (cover.getTier() > component.getTier()) {
                    coverTierTooHigh = true;
                }
                int neighbour = neighborCell(cell, side);
                if (neighbour >= 0 && grid[neighbour] != 0
                    && VoidcraftComponent.fromGridValue(grid[neighbour])
                        .orElseThrow()
                        .isFrame()) {
                    internalComponent = true;
                }
            }
        }
        if (coverTierTooHigh) {
            errors.add("voidcraft_cover_tier_too_high");
            ok = false;
        }
        if (internalComponent) {
            errors.add("voidcraft_internal_component");
            ok = false;
        }

        boolean hasEngine = false;
        for (int cell = 0; cell < grid.length && !hasEngine; cell++) {
            for (int side = 0; side < 6; side++) {
                int value = coverGrid[cell * 6 + side];
                if (value != 0 && VoidcraftCoverComponent.fromGridValue(value)
                    .orElseThrow()
                    .isEngine()) {
                    hasEngine = true;
                    break;
                }
            }
        }
        if (hasEngine) {
            errors.add("voidbase_thruster_forbidden");
            ok = false;
        }

        if (computeStats().integrity <= 0L) {
            errors.add("voidcraft_integrity_too_low");
            ok = false;
        }

        return ok;
    }

    /**
     * The base's complete parts list: exactly how many of each part go into constructing it — the placeable blocks
     * (controller/frame) from the base grid and every cover from the cover grid.
     *
     * @return a stable-ordered map (blocks in component meta order, then covers in cover id order);
     *         key = {@code "block.<component name>"} or {@code "cover.<cover name>"} (the enum name — the stable
     *         string identity convention), value = required count (≥ 1)
     */
    public java.util.Map<String, Long> partsList() {
        java.util.LinkedHashMap<String, Long> map = new java.util.LinkedHashMap<>();
        for (VoidcraftComponent component : VoidcraftComponent.ALL) {
            int count = count(component);
            if (count > 0) {
                map.put("block." + component.name(), (long) count);
            }
        }
        for (VoidcraftCoverComponent cover : VoidcraftCoverComponent.ALL) {
            int count = countCover(cover);
            if (count > 0) {
                map.put("cover." + cover.name(), (long) count);
            }
        }
        return java.util.Collections.unmodifiableMap(map);
    }

    /**
     * The reactor launch fuel this ship must pay at the Gateway before it may launch: per reactor cover type, the
     * count of mounted units times the cover's per-unit fuel (in mB). Reactor types mix freely (a ship with 2
     * fusion + 1 antimatter reactors pays both). Ships without reactors yield an empty map.
     *
     * @return a stable-ordered map (cover catalog order): key = the reactor cover, value = required mB (≥ 1). The
     *         fluid identity of each cover's fuel lives in the runtime registry (the blueprint is plain Java).
     */
    public java.util.Map<VoidcraftCoverComponent, Long> reactorLaunchFuel() {
        java.util.LinkedHashMap<VoidcraftCoverComponent, Long> map = new java.util.LinkedHashMap<>();
        for (VoidcraftCoverComponent cover : VoidcraftCoverComponent.ALL) {
            if (!cover.isReactor()) {
                continue;
            }
            int count = countCover(cover);
            if (count > 0) {
                map.put(cover, (long) count * cover.getLaunchFuel());
            }
        }
        return java.util.Collections.unmodifiableMap(map);
    }

    /**
     * Copy of the base grid (safe to mutate).
     */
    public byte[] copyGrid() {
        return grid.clone();
    }

    /**
     * Copy of the facing grid (safe to mutate).
     */
    public byte[] copyFacingGrid() {
        return facingGrid.clone();
    }

    /**
     * Copy of the cover grid (safe to mutate).
     */
    public byte[] copyCoverGrid() {
        return coverGrid.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VoidcraftBlueprint)) return false;
        VoidcraftBlueprint that = (VoidcraftBlueprint) o;
        return width == that.width && height == that.height
            && depth == that.depth
            && Arrays.equals(grid, that.grid)
            && Arrays.equals(facingGrid, that.facingGrid)
            && Arrays.equals(coverGrid, that.coverGrid);
    }

    @Override
    public int hashCode() {
        int result = width;
        result = 31 * result + height;
        result = 31 * result + depth;
        result = 31 * result + Arrays.hashCode(grid);
        result = 31 * result + Arrays.hashCode(facingGrid);
        result = 31 * result + Arrays.hashCode(coverGrid);
        return result;
    }
}
