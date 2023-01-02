package com.github.technus.tectech.thing.block;

import static com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_EyeOfHarmony.errorStar;

import java.awt.*;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

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
    private Color colour = errorStar;

    public Block getOrbitingBody() {
        return orbitingBody;
    }

    public void setOrbitingBody(Block orbitingBody) {
        this.orbitingBody = orbitingBody;
    }

    private Block orbitingBody;

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

    public void setColour(Color colour) {
        this.colour = colour;
    }

    public Color getColour() {
        return colour;
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

    private static final String EOHNBTTag = "EOH:";
    private static final String rotationSpeedNBTTag = EOHNBTTag + "rotationSpeed";
    private static final String sizeNBTTag = EOHNBTTag + "size";
    private static final String sizeRedNBTTag = EOHNBTTag + "red";
    private static final String sizeGreenNBTTag = EOHNBTTag + "green";
    private static final String sizeBlueNBTTag = EOHNBTTag + "blue";
    private static final String orbitingBodyIDNBTTag = EOHNBTTag + "orbitingBodyID";
    private static final String tierNBTTag = EOHNBTTag + "tier";

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        // Save other stats.
        compound.setFloat(rotationSpeedNBTTag, rotationSpeed);
        compound.setFloat(sizeNBTTag, size);
        compound.setLong(tierNBTTag, tier);

        // Save colour info.
        compound.setInteger(sizeRedNBTTag, colour.getRed());
        compound.setInteger(sizeGreenNBTTag, colour.getGreen());
        compound.setInteger(sizeBlueNBTTag, colour.getBlue());

        if (orbitingBody != null) {
            int blockID = Block.getIdFromBlock(orbitingBody);
            compound.setInteger(orbitingBodyIDNBTTag, blockID);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        // Load other stats.
        rotationSpeed = compound.getFloat(rotationSpeedNBTTag);
        size = compound.getFloat(sizeNBTTag);
        tier = compound.getLong(tierNBTTag);

        // Load colour info.
        int red = compound.getInteger(sizeRedNBTTag);
        int green = compound.getInteger(sizeGreenNBTTag);
        int blue = compound.getInteger(sizeBlueNBTTag);
        colour = new Color(red, green, blue);

        if (compound.hasKey(orbitingBodyIDNBTTag)) {
            int blockID = compound.getInteger(orbitingBodyIDNBTTag);
            orbitingBody = Block.getBlockById(blockID);
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
