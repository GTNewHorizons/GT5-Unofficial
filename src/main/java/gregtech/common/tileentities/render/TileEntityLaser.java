package gregtech.common.tileentities.render;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

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
    }

    public void setColors(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void setRotationFields(ForgeDirection direction, Rotation rotation, Flip flip) {
        setRotationAngle(rotation, flip);
        setRotationAxis(direction);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    private void setRotationAngle(Rotation rotation, Flip flip) {
        int invert = (flip == Flip.HORIZONTAL || flip == Flip.VERTICAL) ? 1 : -1;
        switch (rotation) {
            case NORMAL -> rotationAngle = 0;
            case CLOCKWISE -> rotationAngle = 90 * invert;
            case COUNTER_CLOCKWISE -> rotationAngle = -90 * invert;
            case UPSIDE_DOWN -> rotationAngle = 180;
        }
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void setRotationAxis(ForgeDirection direction) {
        rotAxisX = direction.offsetX;
        rotAxisY = direction.offsetY;
        rotAxisZ = direction.offsetZ;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void setShouldRender(boolean shouldRender) {
        this.shouldRender = shouldRender;
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
