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
    public static final String TAG_THRUST_X = "vc_thrust_x";
    public static final String TAG_THRUST_Y = "vc_thrust_y";
    public static final String TAG_THRUST_Z = "vc_thrust_z";
    public static final String TAG_SPEED = "vc_speed";
    public static final String TAG_CARGO = "vc_cargo";
    public static final String TAG_MINING = "vc_mining";
    public static final String TAG_SCAN = "vc_scan";
    public static final String TAG_CONSTRUCTION = "vc_construction";
    public static final String TAG_STARLIFTER = "vc_starlifter";
    public static final String TAG_ENERGY_BUFFER = "vc_energy_buffer";
    public static final String TAG_ENERGY_DRAW = "vc_energy_draw";
    public static final String TAG_INTEGRITY = "vc_integrity";
    public static final String TAG_ROLES = "vc_roles";
    public static final String TAG_EFFICIENCY = "vc_efficiency";

    // Phase 4 pass 2 — Constructor missions (written by the gateway at launch, read by the USS at completion):

    /** Boolean: this launch is a Constructor mission (the loadout below is applied to the project, not delivered). */
    public static final String TAG_CONSTRUCTOR_MISSION = "vc_constructor_mission";

    /** Integer: the {@code USSProject#id} the loadout belongs to (first-incomplete project at launch). */
    public static final String TAG_PROJECT = "vc_project";

    /**
     * Compound: the mission loadout — a {@code vc_items} list (dust) + {@code vc_fluids} list (Stellar Plasma) in
     * {@code USSShipCargo} abstract entry format.
     */
    public static final String TAG_LOADOUT = "vc_loadout";

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
        int roles = blueprint.computeRoles();
        double efficiency = blueprint.computeEfficiency();

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
        nbt.setLong(TAG_THRUST_X, stats.thrustX);
        nbt.setLong(TAG_THRUST_Y, stats.thrustY);
        nbt.setLong(TAG_THRUST_Z, stats.thrustZ);
        nbt.setDouble(TAG_SPEED, stats.speed);
        nbt.setLong(TAG_CARGO, stats.cargoSlots);
        nbt.setLong(TAG_MINING, stats.miningPower);
        nbt.setLong(TAG_SCAN, stats.scanPower);
        nbt.setLong(TAG_CONSTRUCTION, stats.constructionPower);
        nbt.setLong(TAG_STARLIFTER, stats.starlifterPower);
        nbt.setLong(TAG_ENERGY_BUFFER, stats.energyBuffer);
        nbt.setLong(TAG_ENERGY_DRAW, stats.energyDraw);
        nbt.setLong(TAG_INTEGRITY, stats.integrity);
        nbt.setInteger(TAG_ROLES, roles);
        nbt.setDouble(TAG_EFFICIENCY, efficiency);
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
