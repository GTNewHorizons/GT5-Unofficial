package tectech.voidcraft.ship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * A Voidcraft blueprint: the placed component grid (with per-cell facing), the per-face cover grid, and the stats
 * and role derived from them.
 *
 * <p>
 * Grid encodings (all {@code index = x + width * (y + height * z)}):
 *
 * <ul>
 * <li>{@link #grid}: 0 = empty cell, otherwise {@link VoidcraftComponent#toGridValue()} (1..9).</li>
 * <li>{@link #facingGrid}: 0 = empty cell, otherwise {@code ForgeDirection ordinal + 1} (1..6) — the facing the
 * component block was placed with. A cell without facing data is treated as facing DOWN (the MTE default facing).</li>
 * <li>{@link #coverGrid}: length {@code cells * 6}, {@code index = cell * 6 + side}, 0 = no cover, otherwise
 * {@link VoidcraftCoverComponent#toGridValue()} (1..8).</li>
 * </ul>
 *
 * <p>
 * This is the exact payload stored inside an {@code ItemVoidcraft} (see {@link VoidcraftNbt}).
 *
 * <p>
 * <b>Component model (pass 23).</b> Covers are the primary components: the ONLY placeable full blocks are the
 * {@link VoidcraftComponent#CONTROLLER} and the {@link VoidcraftComponent#FRAME} (Voidcraft Frame); every other
 * catalog entry is cover-only, and a cell holding one fails validation
 * ({@code voidcraft_cover_only_component}). A ship also needs at least one frame hull block
 * ({@code voidcraft_no_frame}) — the block whose faces accept the covers that carry all ship functionality.
 *
 * <p>
 * <b>Thrust model (pass 18/19, pass 23, pass 24 flip).</b> Thrust is a <em>single value</em>, not directional.
 * The assembler scans the volume in FRONT of its machine, so grid +Z is the FAR end (away from the assembler) and
 * grid −Z is the assembler side. A player builds the ship pointing away from the machine, so the FAR end (grid +Z)
 * is the ship's NOSE — in flight the ship travels away from where it was built and the cockpit leads (see
 * {@code RenderVoidcraftShip.headingFor}). The ship's BACK is the assembler side (grid −Z, {@link #BACK_FACE}).
 * Thrust comes ONLY from {@link VoidcraftCoverComponent#THRUSTER_NOZZLE} covers mounted on that back face (exhaust
 * out the rear). Thrust = the plain sum of those magnitudes.
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
        if (width < 1 || width > VoidcraftConstants.MAX_DIM_X
            || height < 1
            || height > VoidcraftConstants.MAX_DIM_Y
            || depth < 1
            || depth > VoidcraftConstants.MAX_DIM_Z) {
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
     * @return the ship's single thrust value (pass 18/20/23): the sum of the thrust of every
     *         {@link VoidcraftCoverComponent#THRUSTER_NOZZLE} cover mounted on the ship's BACK
     *         ({@link #BACK_FACE}) that is not blocked ({@link #isExhaustBlocked(int)}). Nozzles mounted on any
     *         other face, or blocked by the ship's own hull, contribute nothing. (Pass 23: engines are covers
     *         only — there is no block engine.)
     */
    public long totalThrust() {
        long total = 0;
        for (int cell = 0; cell < grid.length; cell++) {
            VoidcraftCoverComponent cover = VoidcraftCoverComponent.fromGridValue(coverGrid[cell * 6 + BACK_FACE])
                .orElse(null);
            if (cover == VoidcraftCoverComponent.THRUSTER_NOZZLE && !isExhaustBlocked(cell)) {
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
     * Compute the ship's stats from the component and cover grids.
     *
     * <p>
     * Thrust (pass 18/20) is a single value: every back-facing engine ({@link #BACK_FACE}) that is not blocked
     * ({@link #isExhaustBlocked(int)}) counts; anything aimed elsewhere or blocked by the hull contributes
     * nothing — see {@link #totalThrust()}.
     */
    public VoidcraftStats computeStats() {
        long mass = 0, cargoSlots = 0, miningPower = 0, scanPower = 0, constructionPower = 0, starlifterPower = 0,
            energyBuffer = 0, energyDraw = 0, integrity = 0;
        long thrust = 0;

        for (int cell = 0; cell < grid.length; cell++) {
            if (grid[cell] != 0) {
                // Pass 23: full blocks are the controller and the frame only — structural mass + integrity.
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
                energyBuffer += component.getEnergyBuffer();
                energyDraw += component.getEnergyDraw();
                integrity += component.getIntegrity();
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
                energyBuffer += cover.getEnergyBuffer();
                energyDraw += cover.getEnergyDraw();
                integrity += cover.getIntegrity();
                if (cover == VoidcraftCoverComponent.THRUSTER_NOZZLE && side == BACK_FACE && !isExhaustBlocked(cell)) {
                    thrust += cover.getThrust();
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
            energyBuffer,
            energyDraw,
            integrity);
    }

    /**
     * @return the active role mask (see {@link VoidcraftRole#computeRoles})
     */
    public int computeRoles() {
        VoidcraftStats stats = computeStats();
        return VoidcraftRole
            .computeRoles(stats.miningPower, stats.scanPower, stats.constructionPower, stats.starlifterPower);
    }

    /**
     * @return the hybrid efficiency multiplier for this ship's role set
     */
    public double computeEfficiency() {
        return VoidcraftRole.efficiencyMultiplier(VoidcraftRole.countRoles(computeRoles()));
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
     * Pass 23 rules: only the controller and the frame are placeable full blocks — a cell holding any cover-only
     * component fails with {@code voidcraft_cover_only_component}, and the ship needs at least one frame hull
     * block ({@code voidcraft_no_frame}).
     *
     * <p>
     * Thruster rules (pass 18/20, pass 24 flip): every thruster — a {@link VoidcraftCoverComponent#THRUSTER_NOZZLE}
     * cover — must be mounted on the ship's BACK face ({@link #BACK_FACE}, the assembler side), and a back-facing
     * thruster needs the {@link #EXHAUST_CLEARANCE} blocks directly on its exhaust side (grid −Z, toward the
     * assembler) free of Voidcraft blocks. A violation breaks the digitization and is reported in the assembler
     * GUI:
     *
     * <ul>
     * <li>{@code voidcraft_no_engine} — no thruster at all</li>
     * <li>{@code voidcraft_thruster_wrong_facing} — a thruster does not face the back</li>
     * <li>{@code voidcraft_engine_blocked} — a Voidcraft block sits on the exhaust side of a back-facing thruster
     * (within 5 cells, i.e. the hull is between the nozzle and the open exhaust toward the assembler)</li>
     * <li>{@code voidcraft_cover_only_component} — a full-block cell holds a cover-only part (pass 23)</li>
     * <li>{@code voidcraft_no_frame} — no Voidcraft Frame hull block (pass 23)</li>
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

        // Pass 23: covers are primary — only the controller and the frame are placeable full blocks.
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

        if (count(VoidcraftComponent.FRAME) < 1) {
            errors.add("voidcraft_no_frame");
            ok = false;
        }

        if (maxTier() > maxComponentTier) {
            errors.add("voidcraft_tier_too_high");
            ok = false;
        }

        // Thruster audit (pass 18/19, pass 23 covers-only): every nozzle cover must be on the back face with a
        // clear exhaust path.
        boolean hasThruster = false;
        boolean wrongFacing = false;
        boolean blocked = false;
        for (int cell = 0; cell < grid.length; cell++) {
            for (int side = 0; side < 6; side++) {
                int value = coverGrid[cell * 6 + side];
                if (value == 0) {
                    continue;
                }
                if (VoidcraftCoverComponent.fromGridValue(value)
                    .orElseThrow() == VoidcraftCoverComponent.THRUSTER_NOZZLE) {
                    hasThruster = true;
                    if (side != BACK_FACE) {
                        wrongFacing = true;
                    } else if (isExhaustBlocked(cell)) {
                        blocked = true;
                    }
                }
            }
        }

        if (!hasThruster) {
            errors.add("voidcraft_no_engine");
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

        return ok;
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
