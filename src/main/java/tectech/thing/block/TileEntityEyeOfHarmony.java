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
     * One serializable planet description (dimension key + orbit parameters). The dimension key is the
     * {@code gtneioreplugin.ModBlocks} abbreviation of the dimension-display block drawn as the hologram.
     */
    public static final class PlanetSpec {

        public PlanetSpec(String dimension, float distance, float scale, float orbitSpeed, float rotationSpeed,
            float xAngle, float zAngle) {
            this(dimension, distance, scale, orbitSpeed, rotationSpeed, xAngle, zAngle, 0);
        }

        public PlanetSpec(String dimension, float distance, float scale, float orbitSpeed, float rotationSpeed,
            float xAngle, float zAngle, int color) {
            this.dimension = dimension;
            this.distance = distance;
            this.scale = scale;
            this.orbitSpeed = orbitSpeed;
            this.rotationSpeed = rotationSpeed;
            this.xAngle = xAngle;
            this.zAngle = zAngle;
            this.color = color;
        }

        public final String dimension;
        public final float distance;
        public final float scale;
        public final float orbitSpeed;
        public final float rotationSpeed;
        public final float xAngle;
        public final float zAngle;

        /**
         * Tint (ARGB) for the USS self-contained tinted-sphere orbit render; 0 = unset (the legacy block-hologram
         * path draws the dimension block instead).
         */
        public final int color;
    }

    /**
     * Install an explicit planet system (replacing anything present, including the legacy lazy list). A null or
     * empty list clears the explicit system — the legacy lazy-random path applies again.
     *
     * <p>
     * Dimension keys that do not resolve to a registered block (mod not loaded / renamed) are skipped for the
     * render list but kept in the specs (the NBT round-trip stays lossless).
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
        for (PlanetSpec spec : specs) {
            Block block = ModBlocks.blocks.get(spec.dimension);
            if (block != null) {
                orbitingObjects.add(
                    new OrbitingObject(
                        block,
                        spec.distance,
                        spec.rotationSpeed,
                        spec.orbitSpeed,
                        spec.xAngle,
                        spec.zAngle,
                        spec.scale));
            }
        }
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

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        // Save other stats.
        compound.setDouble(SIZE_NBT_TAG, starSize);
        compound.setLong(TIER_NBT_TAG, tier);
        compound.setDouble(DOME_NBT_TAG, domeRadius);

        // Explicit planet system (Voidcraft) — persisted so chunk reloads and description packets carry it (the
        // tag is omitted entirely for legacy stars, which keep the lazy random path).
        if (explicitPlanets) {
            NBTTagList list = new NBTTagList();
            for (PlanetSpec spec : planetSpecs) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setString("dim", spec.dimension);
                tag.setFloat("distance", spec.distance);
                tag.setFloat("scale", spec.scale);
                tag.setFloat("orbitSpeed", spec.orbitSpeed);
                tag.setFloat("rotationSpeed", spec.rotationSpeed);
                tag.setFloat("xAngle", spec.xAngle);
                tag.setFloat("zAngle", spec.zAngle);
                if (spec.color != 0) {
                    tag.setInteger("color", spec.color);
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
                        tag.getString("dim"),
                        tag.getFloat("distance"),
                        tag.getFloat("scale"),
                        tag.getFloat("orbitSpeed"),
                        tag.getFloat("rotationSpeed"),
                        tag.getFloat("xAngle"),
                        tag.getFloat("zAngle"),
                        tag.hasKey("color") ? tag.getInteger("color") : 0));
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
