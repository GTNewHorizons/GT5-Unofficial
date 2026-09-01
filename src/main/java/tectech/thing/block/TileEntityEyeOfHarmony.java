package tectech.thing.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import gtneioreplugin.plugin.block.ModBlocks;
import tectech.voidcraft.uss.USSStarColor;
import tectech.voidcraft.uss.USSStarRenderType;

public class TileEntityEyeOfHarmony extends TileEntity {

    private static final double EOH_STAR_FIELD_RADIUS = 13;
    private AxisAlignedBB boundingBox;

    @Override
    public double getMaxRenderDistanceSquared() {
        return Double.MAX_VALUE;
    }

    // Prevent culling when block is out of frame so model can remain active.
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        if (boundingBox == null) {
            // Assuming your block is at (x, y, z)
            double x = this.xCoord;
            double y = this.yCoord;
            double z = this.zCoord;
            // Pass 12: the space-shell radius is per-machine (legacy EoH 12.95, Voidcraft USS 27.1 since pass 15)
            // — the box MUST cover the dome (ceil(27.1) = 28 > 13) or the client culls the shell.
            double radius = Math.max(EOH_STAR_FIELD_RADIUS, Math.ceil(domeRadius));

            // Create a bounding box that extends 'size' blocks in all directions from the block.
            boundingBox = AxisAlignedBB
                .getBoundingBox(x - radius, y - radius, z - radius, x + radius + 1, y + radius + 1, z + radius + 1);
        }
        return boundingBox;
    }

    public void setStarSize(double size) {
        this.starSize = size;
    }

    private double starSize = 1;

    /** The opaque ARGB color the star's render core is tinted with (from the star's registered definition). */
    private int starColor = USSStarColor.DEFAULT;

    public int getStarColor() {
        return starColor;
    }

    public void setStarColor(int color) {
        this.starColor = color;
    }

    /**
     * The opaque ARGB color the star's outer halo layers are tinted with (from the star's registered definition;
     * 0 = unset — the renderer falls back to the core color).
     */
    private int starShellColor = 0;

    public int getStarShellColor() {
        return starShellColor;
    }

    public void setStarShellColor(int color) {
        this.starShellColor = color;
    }

    /**
     * Whether the star's shell layers render outside-in as a glow ring beyond the core's rim (the halo treatment —
     * from the star's registered definition; the legacy star and the other classes keep the solid additive shells).
     */
    private boolean starHalo = false;

    public boolean isStarHalo() {
        return starHalo;
    }

    public void setStarHalo(boolean halo) {
        this.starHalo = halo;
    }

    /**
     * The star's custom render treatment — the ordinal of {@code USSStarRenderType} (STANDARD when unset): the extra
     * geometry the renderer draws on top of the standard star body (the magnetar's magnetic field loops), from the
     * star's registered definition.
     */
    private int starRenderType = USSStarRenderType.STANDARD.ordinal();

    public USSStarRenderType getStarRenderType() {
        return USSStarRenderType.fromOrdinal(starRenderType);
    }

    public void setStarRenderType(USSStarRenderType renderType) {
        this.starRenderType = (renderType == null ? USSStarRenderType.STANDARD : renderType).ordinal();
    }

    /**
     * The Dyson Swarm state (the Voidcraft infrastructure pass): the satellites currently in the star's swarm plus
     * the star's satellite capacity (0 capacity = no swarm — the legacy path and Voidcraft stars before the first
     * launch). The client renders a semi-transparent gray triangle shell at a fill of count/capacity.
     */
    private long swarmCount = 0;
    private long swarmCapacity = 0;

    public long getSwarmCount() {
        return swarmCount;
    }

    public long getSwarmCapacity() {
        return swarmCapacity;
    }

    public void setDysonSwarm(long count, long capacity) {
        this.swarmCount = Math.max(0L, count);
        this.swarmCapacity = Math.max(0L, capacity);
    }

    /**
     * The constructor-built star-scale infrastructure shell (the infrastructure-builder pass): the Stellar
     * Injector / Stellar Gravitational Lens state — the shell type to render
     * ({@link tectech.voidcraft.uss.USSInfraBuild}
     * INJECTOR / LENS; -1 = none, the star's shell is the Dyson Swarm), the structure units built so far and the
     * star's shell capacity. The client renders a triangle shell at a fill of count/capacity (the star hosts at
     * most one of Dyson Swarm / Injector / Lens — they are mutually exclusive).
     */
    private int infraShellType = -1;
    private long infraShellCount = 0;
    private long infraShellCapacity = 0;

    public int getInfraShellType() {
        return infraShellType;
    }

    public long getInfraShellCount() {
        return infraShellCount;
    }

    public long getInfraShellCapacity() {
        return infraShellCapacity;
    }

    public void setInfraShell(int type, long count, long capacity) {
        this.infraShellType = (type >= 0 ? type : -1);
        this.infraShellCount = Math.max(0L, count);
        this.infraShellCapacity = Math.max(0L, capacity);
    }

    /**
     * The USS virtual orbit clock (machine ticks, the server's orbit time base) + the world tick it was sampled
     * at: the client renders orbits at {@code ussOrbitTime + (worldTime − ussSyncedWorldTime) + partialTicks} —
     * advancing at the normal rate from the last sync — so the planet phases keep the server's clock (including a
     * stellar-acceleration second's proportionally faster advance). 0 = not set (legacy star: world time).
     */
    private long ussOrbitTime = 0;
    private long ussSyncedWorldTime = 0;

    public long getUssOrbitTime() {
        return ussOrbitTime;
    }

    public long getUssSyncedWorldTime() {
        return ussSyncedWorldTime;
    }

    public void setUssOrbitTime(long orbitTime, long syncedWorldTime) {
        this.ussOrbitTime = orbitTime;
        this.ussSyncedWorldTime = syncedWorldTime;
    }

    /**
     * Radius of the space-shell dome in blocks. Pass 12 — a per-machine parameter instead of the renderer's old
     * hardcoded scale: legacy Eye of Harmony keeps the historical 12.95 (0.01·17.5·74); the Voidcraft Unstable
     * Solar System sets 27.1 (pass 12's 2× = 25.9, +1.5 blocks in pass 13, −0.3 in pass 15; its structure
     * doubled to 65³ in pass 12) — star and planet sizes are unaffected.
     */
    private double domeRadius = 12.95;

    public double getDomeRadius() {
        return domeRadius;
    }

    public void setDomeRadius(double radius) {
        this.domeRadius = radius;
        // The render bounding box is cached — a radius change must invalidate it or the new dome gets culled.
        boundingBox = null;
    }

    public static List<Block> selectNRandomElements(Collection<Block> input, long n) {
        if (n > input.size()) {
            throw new IllegalArgumentException("n must be <= collection size");
        }

        List<Block> list = new ArrayList<>(input);
        Collections.shuffle(list);
        return list.subList(0, (int) n);
    }

    public static float generateRandomFloat(float a, float b) {
        Random rand = new Random();
        return rand.nextFloat() * (b - a) + a;
    }

    public long getTier() {
        return tier;
    }

    public void setTier(long tier) {
        this.tier = tier;
    }

    private long tier = 9;

    public double getStarSize() {
        return starSize;
    }

    public static class OrbitingObject {

        public OrbitingObject(Block block, float distance, float rotationSpeed, float orbitSpeed, float xAngle,
            float zAngle, float scale) {
            this.block = block;
            this.distance = distance;
            this.rotationSpeed = rotationSpeed;
            this.orbitSpeed = orbitSpeed;
            this.xAngle = xAngle;
            this.zAngle = zAngle;
            this.scale = scale;
        }

        public final Block block;
        public final float distance;
        public final float rotationSpeed;
        public final float orbitSpeed;
        public final float xAngle;
        public final float zAngle;
        public final float scale;
    }

    public ArrayList<OrbitingObject> getOrbitingObjects() {
        return orbitingObjects;
    }

    private final ArrayList<OrbitingObject> orbitingObjects = new ArrayList<>();

    // --- Explicit planet system (Voidcraft, Phase 4 pass 3) ---------------------------------------------
    //
    // The Voidcraft USS owns a DETERMINISTIC planet system (a pure function of the star's type and ignition
    // timestamp — see USSPlanets) that must render identically on every client and match the miner's cargo. It
    // injects that system here via setPlanets(); while an explicit system is present the legacy lazy random
    // generateImportantInfo() path is bypassed entirely. A legacy star (no explicit system) keeps the old
    // behaviour, so the classic Eye of Harmony is unaffected.

    private static final String PLANETS_NBT_TAG = "vc_planets";

    private boolean explicitPlanets = false;

    /** The explicit system as received (kept for the lossless NBT round-trip, even for unresolvable dimension keys). */
    private final List<PlanetSpec> planetSpecs = new ArrayList<>();

    /**
     * One serializable planet description (texture + orbit parameters). The texture is the resource path of the
     * planet's {@code stitched.png} (relative to the mod's texture root); the ring texture is the resource path of its
     * orbit-ring image (relative to the same root, empty when the planet has no ring).
     */
    public static final class PlanetSpec {

        public PlanetSpec(String texture, float distance, float scale, float orbitSpeed, float rotationSpeed,
            float xAngle, float zAngle) {
            this(texture, distance, scale, orbitSpeed, rotationSpeed, xAngle, zAngle, 0, "");
        }

        public PlanetSpec(String texture, float distance, float scale, float orbitSpeed, float rotationSpeed,
            float xAngle, float zAngle, int color) {
            this(texture, distance, scale, orbitSpeed, rotationSpeed, xAngle, zAngle, color, "");
        }

        public PlanetSpec(String texture, float distance, float scale, float orbitSpeed, float rotationSpeed,
            float xAngle, float zAngle, int color, String ringTexture) {
            this.texture = texture;
            this.distance = distance;
            this.scale = scale;
            this.orbitSpeed = orbitSpeed;
            this.rotationSpeed = rotationSpeed;
            this.xAngle = xAngle;
            this.zAngle = zAngle;
            this.color = color;
            this.ringTexture = ringTexture;
        }

        /** The planet's hologram texture (the resource path of its {@code stitched.png}, relative to the mod root). */
        public final String texture;
        public final float distance;
        public final float scale;
        public final float orbitSpeed;
        public final float rotationSpeed;
        public final float xAngle;
        public final float zAngle;

        /**
         * Tint (ARGB) for the USS self-contained tinted-sphere fallback render; 0 = unset (white).
         */
        public final int color;

        /**
         * The orbit-ring texture (the resource path of the ring image, relative to the mod root); empty when the
         * planet has no ring.
         */
        public final String ringTexture;
    }

    /**
     * Install an explicit (Voidcraft) planet system (replacing anything present). A null or empty list clears the
     * explicit system — the legacy lazy-random path applies again.
     *
     * <p>
     * Voidcraft planets are rendered from their bundled textures (see {@link PlanetSpec#texture}), not from the IORE
     * dimension-display blocks, so the legacy {@code orbitingObjects} render list is left empty for an explicit
     * system — the USS render path reads {@link #getPlanetSpecs()} directly.
     *
     * @param specs the explicit planet system (null allowed)
     */
    public void setPlanets(List<PlanetSpec> specs) {
        orbitingObjects.clear();
        planetSpecs.clear();
        this.explicitPlanets = false;
        if (specs == null || specs.isEmpty()) {
            return;
        }
        planetSpecs.addAll(specs);
        this.explicitPlanets = true;
    }

    /**
     * @return the explicit system as installed (empty when none — the legacy lazy path applies).
     */
    public List<PlanetSpec> getPlanetSpecs() {
        return planetSpecs;
    }

    /**
     * @return true when an explicit (Voidcraft) planet system is installed.
     */
    public boolean hasExplicitPlanets() {
        return explicitPlanets;
    }

    private static final Set<String> BLACKLISTED_PLANETS = Collections
        .unmodifiableSet(new HashSet<>(Arrays.asList("Tf", "Ow", "ED", "EA", "VA")));
    // Map of strings to blocks
    private static final Map<String, Block> PLANETS = new HashMap<>();

    static {
        // Initialize the map of planet blocks.
        ModBlocks.blocks.forEach((dimString, dimBlock) -> {
            if (!BLACKLISTED_PLANETS.contains(dimString)) {
                PLANETS.put(dimString, dimBlock);
            }
        });
    }

    private static final float MAX_ANGLE = 30;

    // This must be set last.
    public void generateImportantInfo() {
        // An explicit planet system (Voidcraft) wins over the legacy lazy random fill — and never mix the two.
        if (explicitPlanets || !orbitingObjects.isEmpty()) {
            return;
        }

        int index = 1;
        for (Block block : selectNRandomElements(PLANETS.values(), tier + 1)) {

            float xAngle = generateRandomFloat(-MAX_ANGLE, MAX_ANGLE);
            float zAngle = generateRandomFloat(-MAX_ANGLE, MAX_ANGLE);
            index += 1;
            float distance = index + generateRandomFloat(-0.2f, 0.2f);
            float scale = generateRandomFloat(0.2f, 0.9f);
            float rotationSpeed = generateRandomFloat(0.5f, 1.5f);
            float orbitSpeed = generateRandomFloat(0.5f, 1.5f);
            orbitingObjects.add(new OrbitingObject(block, distance, rotationSpeed, orbitSpeed, xAngle, zAngle, scale));
        }
    }

    // Used to track the rotation of the star/planets.
    public float angle;

    private static final String EOH_NBT_TAG = "EOH:";
    private static final String SIZE_NBT_TAG = EOH_NBT_TAG + "size";
    private static final String TIER_NBT_TAG = EOH_NBT_TAG + "tier";
    private static final String DOME_NBT_TAG = EOH_NBT_TAG + "dome";
    private static final String COLOR_NBT_TAG = EOH_NBT_TAG + "color";
    private static final String SHELL_NBT_TAG = EOH_NBT_TAG + "shell";
    private static final String HALO_NBT_TAG = EOH_NBT_TAG + "halo";
    private static final String RENDER_TYPE_NBT_TAG = EOH_NBT_TAG + "render_type";
    private static final String SWARM_COUNT_NBT_TAG = EOH_NBT_TAG + "swarm_count";
    private static final String SWARM_CAP_NBT_TAG = EOH_NBT_TAG + "swarm_cap";
    private static final String INFRA_SHELL_TYPE_NBT_TAG = EOH_NBT_TAG + "infra_shell_type";
    private static final String INFRA_SHELL_COUNT_NBT_TAG = EOH_NBT_TAG + "infra_shell_count";
    private static final String INFRA_SHELL_CAP_NBT_TAG = EOH_NBT_TAG + "infra_shell_cap";
    private static final String ORBIT_TIME_NBT_TAG = EOH_NBT_TAG + "orbit_time";
    private static final String ORBIT_SYNC_NBT_TAG = EOH_NBT_TAG + "orbit_sync";

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        // Save other stats.
        compound.setDouble(SIZE_NBT_TAG, starSize);
        compound.setLong(TIER_NBT_TAG, tier);
        compound.setDouble(DOME_NBT_TAG, domeRadius);
        compound.setInteger(COLOR_NBT_TAG, starColor);
        if (starShellColor != 0) {
            compound.setInteger(SHELL_NBT_TAG, starShellColor);
        }
        if (starHalo) {
            compound.setBoolean(HALO_NBT_TAG, true);
        }
        if (starRenderType != USSStarRenderType.STANDARD.ordinal()) {
            compound.setInteger(RENDER_TYPE_NBT_TAG, starRenderType);
        }

        // Dyson Swarm state (Voidcraft infrastructure pass) — persisted so chunk reloads and description packets
        // carry it (0 capacity = no swarm; the legacy star and a fresh Voidcraft star write nothing).
        if (swarmCapacity > 0) {
            compound.setLong(SWARM_COUNT_NBT_TAG, swarmCount);
            compound.setLong(SWARM_CAP_NBT_TAG, swarmCapacity);
        }

        // The constructor-built star-scale infrastructure shell (the infrastructure-builder pass) — persisted so
        // chunk reloads and description packets carry it (no shell = nothing written).
        if (infraShellType >= 0 && infraShellCapacity > 0) {
            compound.setInteger(INFRA_SHELL_TYPE_NBT_TAG, infraShellType);
            compound.setLong(INFRA_SHELL_COUNT_NBT_TAG, infraShellCount);
            compound.setLong(INFRA_SHELL_CAP_NBT_TAG, infraShellCapacity);
        }

        // The USS virtual orbit clock (Voidcraft stellar evolution) — persisted so chunk reloads and description
        // packets carry it (0 = not set; the legacy star and a cold USS write nothing).
        if (ussOrbitTime > 0) {
            compound.setLong(ORBIT_TIME_NBT_TAG, ussOrbitTime);
            compound.setLong(ORBIT_SYNC_NBT_TAG, ussSyncedWorldTime);
        }

        // Explicit planet system (Voidcraft) — persisted so chunk reloads and description packets carry it (the
        // tag is omitted entirely for legacy stars, which keep the lazy random path).
        if (explicitPlanets) {
            NBTTagList list = new NBTTagList();
            for (PlanetSpec spec : planetSpecs) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setString("tex", spec.texture);
                tag.setFloat("distance", spec.distance);
                tag.setFloat("scale", spec.scale);
                tag.setFloat("orbitSpeed", spec.orbitSpeed);
                tag.setFloat("rotationSpeed", spec.rotationSpeed);
                tag.setFloat("xAngle", spec.xAngle);
                tag.setFloat("zAngle", spec.zAngle);
                if (spec.color != 0) {
                    tag.setInteger("color", spec.color);
                }
                if (spec.ringTexture != null && !spec.ringTexture.isEmpty()) {
                    tag.setString("ring", spec.ringTexture);
                }
                list.appendTag(tag);
            }
            compound.setTag(PLANETS_NBT_TAG, list);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        // Load other stats.
        starSize = compound.getDouble(SIZE_NBT_TAG);
        tier = compound.getLong(TIER_NBT_TAG);
        if (compound.hasKey(DOME_NBT_TAG)) {
            domeRadius = compound.getDouble(DOME_NBT_TAG);
        }
        if (compound.hasKey(COLOR_NBT_TAG)) {
            starColor = compound.getInteger(COLOR_NBT_TAG);
        }
        if (compound.hasKey(SHELL_NBT_TAG)) {
            starShellColor = compound.getInteger(SHELL_NBT_TAG);
        }
        if (compound.hasKey(HALO_NBT_TAG)) {
            setStarHalo(compound.getBoolean(HALO_NBT_TAG));
        } else if (starHalo) {
            setStarHalo(false);
        }
        if (compound.hasKey(RENDER_TYPE_NBT_TAG)) {
            setStarRenderType(USSStarRenderType.fromOrdinal(compound.getInteger(RENDER_TYPE_NBT_TAG)));
        } else if (starRenderType != USSStarRenderType.STANDARD.ordinal()) {
            setStarRenderType(USSStarRenderType.STANDARD);
        }

        // Dyson Swarm state (Voidcraft): restore it, or clear a stale swarm when a legacy / fresh-star NBT arrives.
        if (compound.hasKey(SWARM_CAP_NBT_TAG)) {
            setDysonSwarm(compound.getLong(SWARM_COUNT_NBT_TAG), compound.getLong(SWARM_CAP_NBT_TAG));
        } else if (swarmCapacity > 0) {
            setDysonSwarm(0, 0);
        }

        // The constructor-built star-scale infrastructure shell (Voidcraft): restore it, or clear a stale shell
        // when a legacy / no-shell NBT arrives over the description-packet wire.
        if (compound.hasKey(INFRA_SHELL_CAP_NBT_TAG)) {
            setInfraShell(
                compound.getInteger(INFRA_SHELL_TYPE_NBT_TAG),
                compound.getLong(INFRA_SHELL_COUNT_NBT_TAG),
                compound.getLong(INFRA_SHELL_CAP_NBT_TAG));
        } else if (infraShellType >= 0) {
            setInfraShell(-1, 0, 0);
        }

        // The USS virtual orbit clock (Voidcraft): restore it, or clear a stale one when a legacy / cold-USS NBT
        // arrives over the description-packet wire.
        if (compound.hasKey(ORBIT_TIME_NBT_TAG)) {
            setUssOrbitTime(compound.getLong(ORBIT_TIME_NBT_TAG), compound.getLong(ORBIT_SYNC_NBT_TAG));
        } else if (ussOrbitTime > 0) {
            setUssOrbitTime(0, 0);
        }

        // Explicit planet system (Voidcraft): restore it (re-resolving the hologram blocks), or clear a stale one
        // when a legacy star NBT arrives over the description-packet wire.
        if (compound.hasKey(PLANETS_NBT_TAG)) {
            NBTTagList list = compound.getTagList(PLANETS_NBT_TAG, 10);
            List<PlanetSpec> specs = new ArrayList<>();
            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound tag = list.getCompoundTagAt(i);
                if (tag == null) {
                    continue;
                }
                specs.add(
                    new PlanetSpec(
                        tag.getString("tex"),
                        tag.getFloat("distance"),
                        tag.getFloat("scale"),
                        tag.getFloat("orbitSpeed"),
                        tag.getFloat("rotationSpeed"),
                        tag.getFloat("xAngle"),
                        tag.getFloat("zAngle"),
                        tag.hasKey("color") ? tag.getInteger("color") : 0,
                        tag.hasKey("ring") ? tag.getString("ring") : ""));
            }
            setPlanets(specs);
        } else if (explicitPlanets) {
            setPlanets(null);
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, nbttagcompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());
    }

}
