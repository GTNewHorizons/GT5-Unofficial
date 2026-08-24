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
 * <b>Thrust model.</b> Every engine — a full block or a thruster cover — fires out of its mounting face (the block
 * facing, or the face the cover is mounted on) and therefore pushes the ship in the opposite direction. The ship's
 * net thrust is the vector sum {@code Σ -face · magnitude}; {@code stats.thrust} is the best thrust along any single
 * axis. Opposing thrusters cancel out — a fully-cancelled ship fails validation.
 *
 * <p>
 * This class is pure Java (no Minecraft types) so the stat math stays unit-testable outside a game world.
 */
public final class VoidcraftBlueprint {

    /** ForgeDirection ordinal → outward vector (Minecraft axes: +X east, +Y up, +Z south). */
    private static final int[][] SIDE_VECTORS = { { 0, -1, 0 }, // 0 = down
        { 0, 1, 0 }, // 1 = up
        { 0, 0, -1 }, // 2 = north
        { 0, 0, 1 }, // 3 = south
        { -1, 0, 0 }, // 4 = west
        { 1, 0, 0 } // 5 = east
    };

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
     * @return the sum of engine magnitudes (block engines + thruster covers), regardless of direction
     */
    public long totalThrust() {
        long total = 0;
        for (int cell = 0; cell < grid.length; cell++) {
            if (grid[cell] != 0) {
                VoidcraftComponent component = VoidcraftComponent.fromGridValue(grid[cell])
                    .orElseThrow();
                if (component == VoidcraftComponent.ENGINE) {
                    total += component.getThrust();
                }
            }
            for (int side = 0; side < 6; side++) {
                VoidcraftCoverComponent cover = VoidcraftCoverComponent.fromGridValue(coverGrid[cell * 6 + side])
                    .orElse(null);
                if (cover == VoidcraftCoverComponent.THRUSTER_NOZZLE) {
                    total += cover.getThrust();
                }
            }
        }
        return total;
    }

    /**
     * Compute the ship's stats from the component and cover grids.
     *
     * <p>
     * Thrust is vectorial: a block engine fires along its facing, a thruster cover along its mounted face, each
     * pushing the ship opposite the exhaust. The net vector is summed per axis; {@code stats.thrust} is
     * {@code max(|thrustX|, |thrustY|, |thrustZ|)}.
     */
    public VoidcraftStats computeStats() {
        long mass = 0, cargoSlots = 0, miningPower = 0, scanPower = 0, constructionPower = 0, starlifterPower = 0,
            energyBuffer = 0, energyDraw = 0, integrity = 0;
        long thrustX = 0, thrustY = 0, thrustZ = 0;

        for (int cell = 0; cell < grid.length; cell++) {
            if (grid[cell] != 0) {
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
                if (component == VoidcraftComponent.ENGINE && component.getThrust() > 0) {
                    long[] delta = thrustDelta(facingOf(cell), component.getThrust());
                    thrustX += delta[0];
                    thrustY += delta[1];
                    thrustZ += delta[2];
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
                energyBuffer += cover.getEnergyBuffer();
                energyDraw += cover.getEnergyDraw();
                integrity += cover.getIntegrity();
                if (cover == VoidcraftCoverComponent.THRUSTER_NOZZLE && cover.getThrust() > 0) {
                    long[] delta = thrustDelta(side, cover.getThrust());
                    thrustX += delta[0];
                    thrustY += delta[1];
                    thrustZ += delta[2];
                }
            }
        }

        long thrust = Math.max(Math.abs(thrustX), Math.max(Math.abs(thrustY), Math.abs(thrustZ)));
        return new VoidcraftStats(
            mass,
            thrust,
            thrustX,
            thrustY,
            thrustZ,
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
     * Thrust delta pushed on the ship by an engine mounted on the given face.
     * The exhaust leaves the face; the ship is pushed the opposite way.
     *
     * @param face      ForgeDirection ordinal (0..5)
     * @param magnitude engine thrust
     * @return signed [dx, dy, dz]
     */
    private static long[] thrustDelta(int face, long magnitude) {
        int[] v = SIDE_VECTORS[face];
        return new long[] { -v[0] * magnitude, -v[1] * magnitude, -v[2] * magnitude };
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

        if (maxTier() > maxComponentTier) {
            errors.add("voidcraft_tier_too_high");
            ok = false;
        }

        if (count(VoidcraftComponent.ENGINE) == 0 && countCover(VoidcraftCoverComponent.THRUSTER_NOZZLE) == 0) {
            errors.add("voidcraft_no_engine");
            ok = false;
        }

        // Engines present but every bit of thrust cancels out (e.g. opposing thrusters)
        if (totalThrust() > 0 && computeStats().thrust == 0) {
            errors.add("voidcraft_thrusters_cancelled");
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
