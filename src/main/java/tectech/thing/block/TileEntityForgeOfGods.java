package tectech.thing.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityForgeOfGods extends TileEntity {

    private float radius = 32;
    private float rotationSpeed = 10;
    private int ringCount = 3;
    public float colorR = .7f, colorG = .8f, colorB = 1f;

    public static final float backPlateDistance = -121.5f, backPlateRadius = 13f;

    public float rotAngle = 0, rotx = 1, roty = 0, rotz = 0;

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return 25600;
    }

    public void setRenderSize(float size) {
        this.radius = size;
    }

    public void setRenderRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public float getStarRadius() {
        return radius;
    }

    public float getRenderRotationSpeed() {
        return rotationSpeed;
    }

    // Used to track the rotation of the star
    public float angle;

    private static final String FOG_NBT_TAG = "FOG:";
    private static final String ROTATION_SPEED_NBT_TAG = FOG_NBT_TAG + "renderRotationSpeed";
    private static final String SIZE_NBT_TAG = FOG_NBT_TAG + "renderSize";


    public float getLensDistance(int lensID) {
        return switch (lensID) {
            case 0 -> -61.5f;
            case 1 -> -54.5f;
            case 2 -> -44.5f;
            default -> throw new IllegalStateException("Unexpected value: " + lensID);
        };
    }

    public float getLenRadius(int lensID){
        return switch (lensID) {
            case 0 -> 1.1f;
            case 1 -> 3.5f;
            case 2 -> 5f;
            default -> throw new IllegalStateException("Unexpected value: " + lensID);
        };
    }

    public int getRingCount(){
        return ringCount;
    }

    public void setRingCount(int count){
        ringCount = count;
    }

    public float getStartAngle(){
        float x = -getLensDistance(getRingCount()-1);
        float y = getLenRadius(getRingCount()-1);
        float alpha = (float) Math.atan2(y,x);
        float beta = (float) Math.asin(radius/Math.sqrt(x*x+y*y));
        return alpha + ((float)Math.PI/2 - beta);
    }

    public static float interpolate(float x0, float x1, float y0, float y1, float x) {
        return y0 + ((x - x0) * (y1 - y0)) / (x1 - x0);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        // Save stats
        compound.setFloat(ROTATION_SPEED_NBT_TAG, rotationSpeed);
        compound.setFloat(SIZE_NBT_TAG, radius);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        // Load stats
        rotationSpeed = compound.getFloat(ROTATION_SPEED_NBT_TAG);
        //radius = compound.getFloat(SIZE_NBT_TAG);
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
