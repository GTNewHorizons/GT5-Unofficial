package com.github.technus.tectech.thing.block;

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

    private static final String rotationSpeedNBTTag = "EOH:rotationSpeed";
    private static final String sizeNBTTag = "EOH:size";

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setFloat(rotationSpeedNBTTag, rotationSpeed);
        compound.setFloat(sizeNBTTag, size);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        rotationSpeed = compound.getFloat(rotationSpeedNBTTag);
        size = compound.getFloat(sizeNBTTag);
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
