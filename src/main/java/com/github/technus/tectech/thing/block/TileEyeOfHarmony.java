package com.github.technus.tectech.thing.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import pers.gwyog.gtneioreplugin.plugin.block.ModBlocks;

public class TileEyeOfHarmony extends TileEntity {

    // Prevent culling when block is out of frame so model can remain active.
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    private float size = 10;
    private float rotationSpeed = 10;

    // Fun fact, these methods were entirely written by ChatGPT3... Take that as you will.
    public static <T> ArrayList<T> selectNRandomElements(Collection<T> inputList, long n) {
        ArrayList<T> randomElements = new ArrayList<>((int) n);
        ArrayList<T> inputArray = new ArrayList<>(inputList);
        Random rand = new Random();
        IntStream.range(0, (int) n).forEach(i -> {
            int randomIndex = rand.nextInt(inputArray.size());
            randomElements.add(inputArray.get(randomIndex));
            inputArray.remove(randomIndex);
        });
        return randomElements;
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

    public float getSize() {
        return size;
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    @Override
    public void updateEntity() {
        angle += 10.0f;
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
    private static final Set<String> BLACKLISTED_BLOCKS = Collections
            .unmodifiableSet(new HashSet<>(Arrays.asList("Tf", "Ow", "ED", "EA", "VA")));
    // Map of strings to blocks
    private static final Map<String, Block> BLOCKS = new HashMap<>();

    static {
        // Initialize the map of blocks
        ModBlocks.blocks.forEach((dimString, dimBlock) -> {
            if (!BLACKLISTED_BLOCKS.contains(dimString)) {
                BLOCKS.put(dimString, dimBlock);
            }
        });
    }

    private static final float MAX_ANGLE = 30;

    // This must be set last.
    public void generateImportantInfo() {

        int index = 0;
        for (Block block : selectNRandomElements(BLOCKS.values(), tier + 1)) {

            float xAngle = generateRandomFloat(-MAX_ANGLE, MAX_ANGLE);
            float zAngle = generateRandomFloat(-MAX_ANGLE, MAX_ANGLE);
            index += 1.0;
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
    private static final String ROTATION_SPEED_NBT_TAG = EOH_NBT_TAG + "rotationSpeed";
    private static final String SIZE_NBT_TAG = EOH_NBT_TAG + "size";
    private static final String TIER_NBT_TAG = EOH_NBT_TAG + "tier";

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        // Save other stats.
        compound.setFloat(ROTATION_SPEED_NBT_TAG, rotationSpeed);
        compound.setFloat(SIZE_NBT_TAG, size);
        compound.setLong(TIER_NBT_TAG, tier);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        // Load other stats.
        rotationSpeed = compound.getFloat(ROTATION_SPEED_NBT_TAG);
        size = compound.getFloat(SIZE_NBT_TAG);
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
