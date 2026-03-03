package gregtech.common.tileentities.render;

import net.minecraft.nbt.NBTTagCompound;

import org.joml.AxisAngle4f;

import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;

import gregtech.api.util.GTUtility;

public class RenderingTileEntityLaser extends AbstractRenderingTileEntity {

    public boolean shouldRender = false;
    public float red = 0, green = 0, blue = 0;
    public boolean realism = false;
    public double rotAxisX = 0, rotAxisY = 0, rotAxisZ = 0, rotationAngle = 0;

    public RenderingTileEntityLaser() {
        super(3);
    }

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

    public void setRotationFields(ExtendedFacing extendedFacing) {

        AxisAngle4f rotationVector = GTUtility.getRotationAxisAngle4f(extendedFacing);
        rotationAngle = rotationVector.angle;
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
}
