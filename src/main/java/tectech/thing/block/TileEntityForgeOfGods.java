package tectech.thing.block;

import static tectech.thing.metaTileEntity.multi.godforge.color.ForgeOfGodsStarColor.DEFAULT_BLUE;
import static tectech.thing.metaTileEntity.multi.godforge.color.ForgeOfGodsStarColor.DEFAULT_GAMMA;
import static tectech.thing.metaTileEntity.multi.godforge.color.ForgeOfGodsStarColor.DEFAULT_GREEN;
import static tectech.thing.metaTileEntity.multi.godforge.color.ForgeOfGodsStarColor.DEFAULT_RED;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.enumerable.Rotation;
import com.gtnewhorizons.modularui.api.math.Color;

import tectech.thing.metaTileEntity.multi.godforge.color.ForgeOfGodsStarColor;
import tectech.thing.metaTileEntity.multi.godforge.color.StarColorSetting;

public class TileEntityForgeOfGods extends TileEntity {

    private float radius = 32;
    private float rotationSpeed = 10;
    private int ringCount = 1;
    private float rotAngle = 0, rotAxisX = 1, rotAxisY = 0, rotAxisZ = 0;
    private AxisAlignedBB renderBoundingBox;

    private ForgeOfGodsStarColor starColor = ForgeOfGodsStarColor.DEFAULT;

    // current color data
    private int currentColor = Color.rgb(DEFAULT_RED, DEFAULT_GREEN, DEFAULT_BLUE);
    private float gamma = DEFAULT_GAMMA;
    private long lastColorUpdateTime = 0;

    // interpolation color data
    private float cycleStep;
    private int interpIndex;
    private int interpA;
    private int interpB;
    private float interpGammaA;
    private float interpGammaB;

    private static final String NBT_TAG = "FOG:";
    private static final String ROTATION_SPEED_NBT_TAG = NBT_TAG + "ROTATION";
    private static final String SIZE_NBT_TAG = NBT_TAG + "RADIUS";
    private static final String RINGS_NBT_TAG = NBT_TAG + "RINGS";
    private static final String ROT_ANGLE_NBT_TAG = NBT_TAG + "ROT_ANGLE";
    private static final String ROT_AXIS_X_NBT_TAG = NBT_TAG + "ROT_AXIS_X";
    private static final String ROT_AXIS_Y_NBT_TAG = NBT_TAG + "ROT_AXIS_Y";
    private static final String ROT_AXIS_Z_NBT_TAG = NBT_TAG + "ROT_AXIS_Z";
    private static final String STAR_COLOR_TAG = NBT_TAG + "STAR_COLOR";

    public static final float BACK_PLATE_DISTANCE = -121.5f, BACK_PLATE_RADIUS = 13f;
    private static final double RING_RADIUS = 63;
    private static final double BEAM_LENGTH = 59;

    private static final float COLOR_CYCLE_SPEED = 16f;

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        if (renderBoundingBox == null) {
            double x = this.xCoord;
            double y = this.yCoord;
            double z = this.zCoord;

            // This could possibly be made smaller by figuring out the beam direction,
            // but since this is not always known (set dynamically by the MTE), this
            // currently just bounds as if the beam is in all 4 directions.
            renderBoundingBox = AxisAlignedBB.getBoundingBox(
                x - RING_RADIUS - BEAM_LENGTH,
                y - RING_RADIUS - BEAM_LENGTH,
                z - RING_RADIUS - BEAM_LENGTH,
                x + RING_RADIUS + BEAM_LENGTH + 1,
                y + RING_RADIUS + BEAM_LENGTH + 1,
                z + RING_RADIUS + BEAM_LENGTH + 1);
        }
        return renderBoundingBox;
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return Double.MAX_VALUE;
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
        return Color.getRedF(currentColor);
    }

    public float getColorG() {
        return Color.getGreenF(currentColor);
    }

    public float getColorB() {
        return Color.getBlueF(currentColor);
    }

    public float getGamma() {
        return gamma;
    }

    public void setColor(ForgeOfGodsStarColor color) {
        this.starColor = color;
        if (this.starColor == null) {
            this.starColor = ForgeOfGodsStarColor.DEFAULT;
        }

        StarColorSetting colorSetting = starColor.getColor(0);
        currentColor = Color.rgb(colorSetting.getColorR(), colorSetting.getColorG(), colorSetting.getColorB());
        gamma = colorSetting.getGamma();

        if (starColor.numColors() > 1) {
            cycleStep = 0;
            interpA = currentColor;
            interpGammaA = gamma;
            colorSetting = starColor.getColor(1);
            interpB = Color.rgb(colorSetting.getColorR(), colorSetting.getColorG(), colorSetting.getColorB());
            interpGammaB = colorSetting.getGamma();
        }
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
        switch (direction) {
            case SOUTH, NORTH -> rotAngle = 90;
            case WEST -> rotAngle = 0;
            case EAST -> rotAngle = 180;
            case UP, DOWN -> rotAngle = -90;
        }
        rotAxisX = 0;
        rotAxisY = direction.offsetZ + direction.offsetX;
        rotAxisZ = direction.offsetY;

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

    public void incrementColors() {
        if (starColor.numColors() <= 1) {
            return;
        }

        long currentTime = System.currentTimeMillis();

        if (lastColorUpdateTime == 0) {
            lastColorUpdateTime = currentTime;
            return;
        }

        long deltaTime = currentTime - lastColorUpdateTime;
        lastColorUpdateTime = currentTime;

        float increment = starColor.getCycleSpeed() * (deltaTime / COLOR_CYCLE_SPEED);
        cycleStep += increment;

        while (cycleStep >= 255.0f) {
            cycleStep -= 255.0f;
            cycleStarColors();
            currentColor = interpA;
            gamma = interpGammaA;
        }

        interpolateColors();
    }

    private void interpolateColors() {
        float position = cycleStep / 255.0f;
        currentColor = Color.interpolate(interpA, interpB, position);
        gamma = interpGammaA + (interpGammaB - interpGammaA) * position;
    }

    private void cycleStarColors() {
        interpA = interpB;
        interpGammaA = interpGammaB;

        interpIndex++;
        if (interpIndex >= starColor.numColors()) {
            interpIndex = 0;
        }
        StarColorSetting nextColor = starColor.getColor(interpIndex);

        interpB = Color.rgb(nextColor.getColorR(), nextColor.getColorG(), nextColor.getColorB());
        interpGammaB = nextColor.getGamma();
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setFloat(ROTATION_SPEED_NBT_TAG, rotationSpeed);
        compound.setFloat(SIZE_NBT_TAG, radius);
        compound.setInteger(RINGS_NBT_TAG, ringCount);
        compound.setFloat(ROT_ANGLE_NBT_TAG, rotAngle);
        compound.setFloat(ROT_AXIS_X_NBT_TAG, rotAxisX);
        compound.setFloat(ROT_AXIS_Y_NBT_TAG, rotAxisY);
        compound.setFloat(ROT_AXIS_Z_NBT_TAG, rotAxisZ);
        compound.setTag(STAR_COLOR_TAG, starColor.serializeToNBT());
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        rotationSpeed = compound.getFloat(ROTATION_SPEED_NBT_TAG);
        radius = compound.getFloat(SIZE_NBT_TAG);

        ringCount = compound.getInteger(RINGS_NBT_TAG);
        if (ringCount < 1) ringCount = 1;

        rotAngle = compound.getFloat(ROT_ANGLE_NBT_TAG);
        rotAxisX = compound.getFloat(ROT_AXIS_X_NBT_TAG);
        rotAxisY = compound.getFloat(ROT_AXIS_Y_NBT_TAG);
        rotAxisZ = compound.getFloat(ROT_AXIS_Z_NBT_TAG);

        if (compound.hasKey(STAR_COLOR_TAG)) {
            setColor(ForgeOfGodsStarColor.deserialize(compound.getCompoundTag(STAR_COLOR_TAG)));
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

    public void updateToClient() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
}
