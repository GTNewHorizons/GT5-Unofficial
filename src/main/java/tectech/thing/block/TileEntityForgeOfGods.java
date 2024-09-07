package tectech.thing.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.enumerable.Rotation;

public class TileEntityForgeOfGods extends TileEntity {

    private float radius = 32;
    private float rotationSpeed = 10;
    private int ringCount = 1;
    private float colorR = .7f, colorG = .8f, colorB = 1f, gamma = 3f;
    private float rotAngle = 0, rotAxisX = 1, rotAxisY = 0, rotAxisZ = 0;

    private static final String NBT_TAG = "FOG:";
    private static final String ROTATION_SPEED_NBT_TAG = NBT_TAG + "ROTATION";
    private static final String SIZE_NBT_TAG = NBT_TAG + "RADIUS";
    private static final String RINGS_NBT_TAG = NBT_TAG + "RINGS";
    private static final String COLOR_RED_NBT_TAG = NBT_TAG + "COLOR_RED";
    private static final String COLOR_GREEN_NBT_TAG = NBT_TAG + "COLOR_GREEN";
    private static final String COLOR_BLUE_NBT_TAG = NBT_TAG + "COLOR_BLUE";
    private static final String COLOR_GAMMA_NBT_TAG = NBT_TAG + "COLOR_GAMMA";
    private static final String ROT_ANGLE_NBT_TAG = NBT_TAG + "ROT_ANGLE";
    private static final String ROT_AXIS_X_NBT_TAG = NBT_TAG + "ROT_AXIS_X";
    private static final String ROT_AXIS_Y_NBT_TAG = NBT_TAG + "ROT_AXIS_Y";
    private static final String ROT_AXIS_Z_NBT_TAG = NBT_TAG + "ROT_AXIS_Z";

    public static final float BACK_PLATE_DISTANCE = -121.5f, BACK_PLATE_RADIUS = 13f;

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return 51200;
    }

    public void setStarRadius(float size) {
        this.radius = size;
    }

    public float getStarRadius() {
        return radius;
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(float speed) {
        this.rotationSpeed = speed;
    }

    public float getColorR() {
        return colorR;
    }

    public float getColorG() {
        return colorG;
    }

    public float getColorB() {
        return colorB;
    }

    public float getGamma() {
        return gamma;
    }

    public void setColor(float r, float g, float b) {
        setColor(r, g, b, 1);
    }

    public void setColor(float r, float g, float b, float gamma) {
        colorR = r;
        colorG = g;
        colorB = b;
        this.gamma = gamma;
    }

    public int getRingCount() {
        return ringCount;
    }

    public void setRingCount(int count) {
        if (ringCount < 1) return;
        ringCount = count;
    }

    public float getRotAngle() {
        return rotAngle;
    }

    public float getRotAxisX() {
        return rotAxisX;
    }

    public float getRotAxisY() {
        return rotAxisY;
    }

    public float getRotAxisZ() {
        return rotAxisZ;
    }

    public void setRenderRotation(Rotation rotation, ForgeDirection direction) {
        // System.out.println(rotation);
        System.out.println(direction);
        /*
         * switch (rotation) {
         * case NORMAL -> rotAngle = 0;
         * case CLOCKWISE -> rotAngle = 90;
         * case COUNTER_CLOCKWISE -> rotAngle = -90;
         * case UPSIDE_DOWN -> rotAngle = 180;
         * }
         */
        switch (direction) {
            case SOUTH -> rotAngle = 90;
            case NORTH -> rotAngle = 90;
            case WEST -> rotAngle = 0;
            case EAST -> rotAngle = 180;
            case UP -> rotAngle = -90;
            case DOWN -> rotAngle = -90;
        }
        rotAxisX = 0;
        rotAxisY = direction.offsetZ + direction.offsetX;
        rotAxisZ = direction.offsetY;
        System.out.println(direction.offsetX);
        System.out.println(direction.offsetY);
        System.out.println(direction.offsetZ);

        updateToClient();
    }

    public float getLensDistance(int lensID) {
        return switch (lensID) {
            case 0 -> -61.5f;
            case 1 -> -54.5f;
            case 2 -> -44.5f;
            default -> throw new IllegalStateException("Unexpected value: " + lensID);
        };
    }

    public float getLenRadius(int lensID) {
        return switch (lensID) {
            case 0 -> 1.1f;
            case 1 -> 3.5f;
            case 2 -> 5f;
            default -> throw new IllegalStateException("Unexpected value: " + lensID);
        };
    }

    public float getStartAngle() {
        float x = -getLensDistance(getRingCount() - 1);
        float y = getLenRadius(getRingCount() - 1);
        float alpha = (float) Math.atan2(y, x);
        float beta = (float) Math.asin(radius / Math.sqrt(x * x + y * y));
        return alpha + ((float) Math.PI / 2 - beta);
    }

    public static float interpolate(float x0, float x1, float y0, float y1, float x) {
        return y0 + ((x - x0) * (y1 - y0)) / (x1 - x0);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setFloat(ROTATION_SPEED_NBT_TAG, rotationSpeed);
        compound.setFloat(SIZE_NBT_TAG, radius);
        compound.setInteger(RINGS_NBT_TAG, ringCount);
        compound.setFloat(COLOR_RED_NBT_TAG, colorR);
        compound.setFloat(COLOR_GREEN_NBT_TAG, colorG);
        compound.setFloat(COLOR_BLUE_NBT_TAG, colorB);
        compound.setFloat(COLOR_GAMMA_NBT_TAG, gamma);
        compound.setFloat(ROT_ANGLE_NBT_TAG, rotAngle);
        compound.setFloat(ROT_AXIS_X_NBT_TAG, rotAxisX);
        compound.setFloat(ROT_AXIS_Y_NBT_TAG, rotAxisY);
        compound.setFloat(ROT_AXIS_Z_NBT_TAG, rotAxisZ);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        rotationSpeed = compound.getFloat(ROTATION_SPEED_NBT_TAG);
        radius = compound.getFloat(SIZE_NBT_TAG);

        ringCount = compound.getInteger(RINGS_NBT_TAG);
        if (ringCount < 1) ringCount = 1;

        colorR = compound.getFloat(COLOR_RED_NBT_TAG);
        colorG = compound.getFloat(COLOR_GREEN_NBT_TAG);
        colorB = compound.getFloat(COLOR_BLUE_NBT_TAG);
        gamma = compound.getFloat(COLOR_GAMMA_NBT_TAG);
        rotAngle = compound.getFloat(ROT_ANGLE_NBT_TAG);
        rotAxisX = compound.getFloat(ROT_AXIS_X_NBT_TAG);
        rotAxisY = compound.getFloat(ROT_AXIS_Y_NBT_TAG);
        rotAxisZ = compound.getFloat(ROT_AXIS_Z_NBT_TAG);
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

    public void updateToClient() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
}
