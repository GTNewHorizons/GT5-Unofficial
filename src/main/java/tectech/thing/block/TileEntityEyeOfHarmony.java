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

            // Create a bounding box that extends 'size' blocks in all directions from the block.
            boundingBox = AxisAlignedBB.getBoundingBox(
                x - EOH_STAR_FIELD_RADIUS,
                y - EOH_STAR_FIELD_RADIUS,
                z - EOH_STAR_FIELD_RADIUS,
                x + EOH_STAR_FIELD_RADIUS + 1,
                y + EOH_STAR_FIELD_RADIUS + 1,
                z + EOH_STAR_FIELD_RADIUS + 1);
        }
        return boundingBox;
    }

    public void setStarSize(double size) {
        this.starSize = size;
    }

    private double starSize = 1;

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

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        // Save other stats.
        compound.setDouble(SIZE_NBT_TAG, starSize);
        compound.setLong(TIER_NBT_TAG, tier);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        // Load other stats.
        starSize = compound.getDouble(SIZE_NBT_TAG);
        tier = compound.getLong(TIER_NBT_TAG);
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
