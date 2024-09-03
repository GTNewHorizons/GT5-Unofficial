package tectech.thing.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityForgeOfGods extends TileEntity {

    private float size = 10;
    private float rotationSpeed = 10;

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return 25600;
    }

    public void setRenderSize(float size) {
        this.size = size;
    }

    public void setRenderRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public float getRenderSize() {
        return size;
    }

    public float getRenderRotationSpeed() {
        return rotationSpeed;
    }

    @Override
    public void updateEntity() {
        angle += 10.0f;
    }

    // Used to track the rotation of the star
    public float angle;

    private static final String FOG_NBT_TAG = "FOG:";
    private static final String ROTATION_SPEED_NBT_TAG = FOG_NBT_TAG + "renderRotationSpeed";
    private static final String SIZE_NBT_TAG = FOG_NBT_TAG + "renderSize";

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        // Save stats
        compound.setFloat(ROTATION_SPEED_NBT_TAG, rotationSpeed);
        compound.setFloat(SIZE_NBT_TAG, size);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        // Load stats
        rotationSpeed = compound.getFloat(ROTATION_SPEED_NBT_TAG);
        size = compound.getFloat(SIZE_NBT_TAG);
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
