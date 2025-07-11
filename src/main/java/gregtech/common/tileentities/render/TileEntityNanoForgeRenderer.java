package gregtech.common.tileentities.render;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityNanoForgeRenderer extends TileEntity {

    private AxisAlignedBB boundingBox;

    private float timer = 0;
    private long lastSystemTime = 0;
    private boolean running = false;
    private float r = 0.0f, g = 0.0f, b = 0.0f;

    private static final String NBT_TAG = "NANOFORGE_";

    private static final String TIMER_NBT_TAG = NBT_TAG + "TIMER";
    private static final String RUNNING_NBT_TAG = NBT_TAG + "RUNNING";
    private static final String RED_NBT_TAG = NBT_TAG + "RED";
    private static final String GREEN_NBT_TAG = NBT_TAG + "GREEN";
    private static final String BLUE_NBT_TAG = NBT_TAG + "BLUE";

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        if (boundingBox == null) {
            boundingBox = AxisAlignedBB
                .getBoundingBox(xCoord - 10, yCoord - 10, zCoord - 10, xCoord + 10, yCoord + 10, zCoord + 10);
        }
        return boundingBox;
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return Double.MAX_VALUE;
    }

    public void setRunning(boolean running) {
        if (!worldObj.isRemote && this.running != running) {
            this.running = running;
            updateToClient();
        }
    }

    public boolean getRunning() {
        return running;
    }

    public void setTimer(float timer) {
        this.timer = timer;
    }

    public void setTimerServer(float timer) {
        if (!worldObj.isRemote) {
            this.timer = timer;
            updateToClient();
        }
    }

    public float getTimer() {
        return timer;
    }

    public void setColor(float r, float g, float b) {
        if (!worldObj.isRemote) {
            this.r = r;
            this.g = g;
            this.b = b;
            updateToClient();
        }
    }

    public float getRed() {
        return r;
    }

    public float getGreen() {
        return g;
    }

    public float getBlue() {
        return b;
    }

    public void setLastSystemTime(long lastSystemTime) {
        this.lastSystemTime = lastSystemTime;
    }

    public long getLastSystemTime() {
        return lastSystemTime;
    }

    public void writeToNBT(NBTTagCompound compound) {
        compound.setBoolean(RUNNING_NBT_TAG, running);
        compound.setFloat(TIMER_NBT_TAG, timer);
        compound.setFloat(RED_NBT_TAG, r);
        compound.setFloat(GREEN_NBT_TAG, g);
        compound.setFloat(BLUE_NBT_TAG, b);
        super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        running = compound.getBoolean(RUNNING_NBT_TAG);
        timer = compound.getFloat(TIMER_NBT_TAG);
        r = compound.getFloat(RED_NBT_TAG);
        g = compound.getFloat(GREEN_NBT_TAG);
        b = compound.getFloat(BLUE_NBT_TAG);
        super.readFromNBT(compound);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        writeToNBT(nbttagcompound);

        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, nbttagcompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());
    }

    public void updateToClient() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        markDirty();
    }

}
