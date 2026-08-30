package tectech.voidcraft.ship;

import net.minecraft.nbt.NBTTagCompound;

/**
 * NBT (de)serialization of a {@link VoidcraftBlueprint} and its derived stats for the {@code ItemVoidcraft}.
 *
 * <p>
 * Format versioned via {@link VoidcraftConstants#NBT_FORMAT_VERSION}; readers must reject unknown versions rather
 * than mis-parsing old/new payloads.
 */
public final class VoidcraftNbt {

    public static final String TAG_FORMAT = "vc_format";
    public static final String TAG_UUID = "vc_uuid";
    public static final String TAG_NAME = "vc_name";
    public static final String TAG_WIDTH = "vc_w";
    public static final String TAG_HEIGHT = "vc_h";
    public static final String TAG_DEPTH = "vc_d";
    public static final String TAG_GRID = "vc_grid";
    public static final String TAG_FACING = "vc_facing";
    public static final String TAG_COVERS = "vc_covers";
    public static final String TAG_CREATED = "vc_created";

    // Derived stats are stored denormalized so the item tooltip never needs to recompute them.
    public static final String TAG_MASS = "vc_mass";
    public static final String TAG_THRUST = "vc_thrust";
    public static final String TAG_SPEED = "vc_speed";
    public static final String TAG_CARGO = "vc_cargo";
    public static final String TAG_MINING = "vc_mining";
    public static final String TAG_SCAN = "vc_scan";
    public static final String TAG_CONSTRUCTION = "vc_construction";
    public static final String TAG_STARLIFTER = "vc_starlifter";
    public static final String TAG_LOGISTICS = "vc_logistics";
    public static final String TAG_ENERGY_BUFFER = "vc_energy_buffer";
    public static final String TAG_ENERGY_DRAW = "vc_energy_draw";
    public static final String TAG_ENERGY_GEN = "vc_energy_gen";
    public static final String TAG_INTEGRITY = "vc_integrity";

    // Programming framework (Phase C):

    /**
     * NBTTagList: the ship's PROGRAM (a {@code USSProgram} node list — the controller's instruction list), carried
     * in the ship item NBT at its top level (the assembler writes it from the controller block's stored program at
     * build time; the ship's pilot runs it in flight). Absent = a ship without a program (it HOLDS at the origin).
     */
    public static final String TAG_PROGRAM = "vc_program";

    // Voidbase construction (written by the gateway at a Constructor launch, read by the USS CONSTRUCT
    // handler in flight):

    /**
     * Compound: the base's blueprint payload — the blueprint ITEM's top-level NBT (a full voidcraft payload for
     * the 15×15×15 base grid + derived stats + program), read with {@link #readBase}. The blueprint item itself
     * stays in the gateway's blueprint slot (reusable — the ship carries a data copy).
     */
    public static final String TAG_BUILD_BLUEPRINT = "vc_build_blueprint";

    /**
     * NBTTagList: the parts loadout — entries {@code {key: String, amount: int}} with the blueprint's
     * {@code partsList()} keys ({@code block.<NAME>} / {@code cover.<NAME>}). The CONSTRUCT handler credits the
     * site part by part; whatever the ship carries beyond the site's remaining needs is discarded.
     */
    public static final String TAG_BUILD_LOADOUT = "vc_build_loadout";

    /** Boolean: this launch is a Voidbase construction mission (the ship creates or fills a construction site). */
    public static final String TAG_BUILD_MISSION = "vc_build_mission";

    private VoidcraftNbt() {
        throw new AssertionError("Static helpers");
    }

    /**
     * Write the blueprint (plus derived stats, name and identity) into a compound tag.
     *
     * @param nbt       target tag (overwrites any previous voidcraft payload)
     * @param blueprint the blueprint to serialize
     * @param uuid      stable identity, e.g. a UUID string
     * @param name      display name
     * @param createdAt epoch millis
     */
    public static void write(NBTTagCompound nbt, VoidcraftBlueprint blueprint, String uuid, String name,
        long createdAt) {
        VoidcraftStats stats = blueprint.computeStats();

        nbt.setInteger(TAG_FORMAT, VoidcraftConstants.NBT_FORMAT_VERSION);
        nbt.setString(TAG_UUID, uuid);
        nbt.setString(TAG_NAME, name);
        nbt.setInteger(TAG_WIDTH, blueprint.width);
        nbt.setInteger(TAG_HEIGHT, blueprint.height);
        nbt.setInteger(TAG_DEPTH, blueprint.depth);
        nbt.setByteArray(TAG_GRID, blueprint.copyGrid());
        nbt.setByteArray(TAG_FACING, blueprint.copyFacingGrid());
        nbt.setByteArray(TAG_COVERS, blueprint.copyCoverGrid());
        nbt.setLong(TAG_CREATED, createdAt);

        nbt.setLong(TAG_MASS, stats.mass);
        nbt.setLong(TAG_THRUST, stats.thrust);
        nbt.setDouble(TAG_SPEED, stats.speed);
        nbt.setLong(TAG_CARGO, stats.cargoSlots);
        nbt.setLong(TAG_MINING, stats.miningPower);
        nbt.setLong(TAG_SCAN, stats.scanPower);
        nbt.setLong(TAG_CONSTRUCTION, stats.constructionPower);
        nbt.setLong(TAG_STARLIFTER, stats.starlifterPower);
        nbt.setLong(TAG_LOGISTICS, stats.logisticsPower);
        nbt.setLong(TAG_ENERGY_BUFFER, stats.energyBuffer);
        nbt.setLong(TAG_ENERGY_DRAW, stats.energyDraw);
        nbt.setLong(TAG_ENERGY_GEN, stats.energyGen);
        nbt.setLong(TAG_INTEGRITY, stats.integrity);
    }

    /**
     * Read a blueprint back from a compound tag.
     *
     * @param nbt source tag
     * @return the blueprint, or null if the tag is not a valid voidcraft payload (wrong version, missing keys,
     *         corrupt grid)
     */
    public static VoidcraftBlueprint read(NBTTagCompound nbt) {
        if (nbt == null || !nbt.hasKey(TAG_FORMAT)) {
            return null;
        }
        if (nbt.getInteger(TAG_FORMAT) != VoidcraftConstants.NBT_FORMAT_VERSION) {
            return null;
        }
        int width = nbt.getInteger(TAG_WIDTH);
        int height = nbt.getInteger(TAG_HEIGHT);
        int depth = nbt.getInteger(TAG_DEPTH);
        byte[] grid = nbt.getByteArray(TAG_GRID);
        if (width < 1 || height < 1 || depth < 1 || grid.length != width * height * depth) {
            return null;
        }
        int cells = width * height * depth;
        byte[] facing = null;
        if (nbt.hasKey(TAG_FACING)) {
            facing = nbt.getByteArray(TAG_FACING);
            if (facing.length != cells) {
                return null;
            }
        }
        byte[] covers = null;
        if (nbt.hasKey(TAG_COVERS)) {
            covers = nbt.getByteArray(TAG_COVERS);
            if (covers.length != cells * 6) {
                return null;
            }
        }
        try {
            return VoidcraftBlueprint.of(width, height, depth, grid, facing, covers);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Read a Voidbase blueprint back from a compound tag (the 15×15×15 base bounds, not the ship's 5×5×10).
     *
     * @param nbt source tag
     * @return the blueprint, or null if the tag is not a valid base payload (wrong version, missing keys, corrupt
     *         grid)
     */
    public static VoidcraftBlueprint readBase(NBTTagCompound nbt) {
        if (nbt == null || !nbt.hasKey(TAG_FORMAT)) {
            return null;
        }
        if (nbt.getInteger(TAG_FORMAT) != VoidcraftConstants.NBT_FORMAT_VERSION) {
            return null;
        }
        int width = nbt.getInteger(TAG_WIDTH);
        int height = nbt.getInteger(TAG_HEIGHT);
        int depth = nbt.getInteger(TAG_DEPTH);
        byte[] grid = nbt.getByteArray(TAG_GRID);
        if (width < 1 || height < 1 || depth < 1 || grid.length != width * height * depth) {
            return null;
        }
        int cells = width * height * depth;
        byte[] facing = null;
        if (nbt.hasKey(TAG_FACING)) {
            facing = nbt.getByteArray(TAG_FACING);
            if (facing.length != cells) {
                return null;
            }
        }
        byte[] covers = null;
        if (nbt.hasKey(TAG_COVERS)) {
            covers = nbt.getByteArray(TAG_COVERS);
            if (covers.length != cells * 6) {
                return null;
            }
        }
        try {
            return VoidcraftBlueprint.ofBase(width, height, depth, grid, facing, covers);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Read a stat back from a compound tag (null-safe; falls back to 0 for missing keys).
     */
    public static long readLong(NBTTagCompound nbt, String key) {
        return nbt == null ? 0L : nbt.getLong(key);
    }

    public static double readDouble(NBTTagCompound nbt, String key) {
        return nbt == null ? 0.0 : nbt.getDouble(key);
    }

    public static int readInt(NBTTagCompound nbt, String key) {
        return nbt == null ? 0 : nbt.getInteger(key);
    }
}
