package com.github.technus.tectech.thing.block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
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

    private float size = 1;
    private float rotationSpeed = 0;

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

    private long tier = -1;

    public void incrementSize() {
        size += 1.5f;
    }

    public void increaseRotationSpeed() {
        rotationSpeed++;
    }

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
        public OrbitingObject(
                Block block,
                float distance,
                float rotationSpeed,
                float orbitSpeed,
                float xAngle,
                float zAngle,
                float scale) {
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

    private static final float maxAngle = 30;

    // This must be set last.
    public void generateImportantInfo() {

        int index = 0;
        for (Block block : selectNRandomElements(ModBlocks.blocks.values(), tier + 1)) {

            float xAngle = generateRandomFloat(-maxAngle, maxAngle);
            float zAngle = generateRandomFloat(-maxAngle, maxAngle);
            index += 1.0;
            float distance = index + generateRandomFloat(-0.2f, 0.2f);
            float scale = generateRandomFloat(0.2f, 0.9f);
            float rotationSpeed = generateRandomFloat(0.5f, 1.5f);
            float orbitSpeed = generateRandomFloat(0.5f, 1.5f);
            orbitingObjects.add(new OrbitingObject(block, distance, rotationSpeed, orbitSpeed, xAngle, zAngle, scale));
        }
    }

    public float angle;
    private static final String EOHNBTTag = "EOH:";
    private static final String rotationSpeedNBTTag = EOHNBTTag + "rotationSpeed";
    private static final String sizeNBTTag = EOHNBTTag + "size";
    private static final String tierNBTTag = EOHNBTTag + "tier";

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        // Save other stats.
        compound.setFloat(rotationSpeedNBTTag, rotationSpeed);
        compound.setFloat(sizeNBTTag, size);
        compound.setLong(tierNBTTag, tier);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        // Load other stats.
        rotationSpeed = compound.getFloat(rotationSpeedNBTTag);
        size = compound.getFloat(sizeNBTTag);
        tier = compound.getLong(tierNBTTag);
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
