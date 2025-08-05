package gregtech.common.tileentities.render;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import org.joml.AxisAngle4f;
import org.joml.Matrix4f;

import com.gtnewhorizon.structurelib.alignment.enumerable.Flip;
import com.gtnewhorizon.structurelib.alignment.enumerable.Rotation;

public class TileEntityLaser extends TileEntity {

    public boolean shouldRender = false;
    public float red = 0, green = 0, blue = 0;
    public float counter = 0F;
    public boolean realism = false;
    public double rotAxisX = 0, rotAxisY = 0, rotAxisZ = 0, rotationAngle = 0;

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setFloat("rgb_red", red);
        compound.setFloat("rgb_green", green);
        compound.setFloat("rgb_blue", blue);
        compound.setBoolean("shouldRender", shouldRender);
        compound.setDouble("rotAxisX", rotAxisX);
        compound.setDouble("rotAxisY", rotAxisY);
        compound.setDouble("rotAxisZ", rotAxisZ);
        compound.setDouble("rotationAngle", rotationAngle);
        compound.setBoolean("realism", realism);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        red = compound.getFloat("rgb_red");
        blue = compound.getFloat("rgb_blue");
        green = compound.getFloat("rgb_green");
        shouldRender = compound.getBoolean("shouldRender");
        rotAxisX = compound.getDouble("rotAxisX");
        rotAxisY = compound.getDouble("rotAxisY");
        rotAxisZ = compound.getDouble("rotAxisZ");
        rotationAngle = compound.getDouble("rotationAngle");
        realism = compound.getBoolean("realism");
    }

    public void setColors(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void setRotationFields(ForgeDirection direction, Rotation rotation, Flip flip) {
        Matrix4f rotationMatrix = new Matrix4f().identity();

        float localAngle = switch (rotation) {
            case NORMAL -> 0;
            case CLOCKWISE -> 90;
            case COUNTER_CLOCKWISE -> -90;
            case UPSIDE_DOWN -> 180;
        };
        localAngle *= (flip == Flip.HORIZONTAL || flip == Flip.VERTICAL) ? 1 : -1;
        localAngle = (float) Math.toRadians(localAngle);
        rotationMatrix.rotate(localAngle, direction.offsetX, direction.offsetY, direction.offsetZ);

        float x = 0, y = 0;
        float angle = switch (direction) {
            case DOWN, UP -> {
                x = 1;
                yield -90;
            }
            case EAST, SOUTH -> {
                y = 1;
                yield 90;
            }
            case WEST, NORTH -> {
                y = 1;
                yield -90;
            }
            case UNKNOWN -> 0.0F;
        };
        angle = (float) Math.toRadians(angle);
        rotationMatrix.rotate(angle, x, y, 0);

        AxisAngle4f rotationVector = new AxisAngle4f();
        rotationMatrix.getRotation(rotationVector);

        rotationAngle = rotationVector.angle / (float) Math.PI * 180;
        rotAxisX = rotationVector.x;
        rotAxisY = rotationVector.y;
        rotAxisZ = rotationVector.z;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void setShouldRender(boolean shouldRender) {
        this.shouldRender = shouldRender;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void toggleRealism() {
        realism = !realism;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public Boolean getShouldRender() {
        return shouldRender;
    }

    public float getRed() {
        return red;
    }

    public float getGreen() {
        return green;
    }

    public float getBlue() {
        return blue;
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
        worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return 4096;
    }
}
