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

    private static final String NBT_TAG = "NANOFORGE_";

    private static final String TIMER_NBT_TAG = NBT_TAG + "TIMER";
    private static final String RUNNING_NBT_TAG = NBT_TAG + "RUNNING";

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        if (boundingBox == null) {
            boundingBox = AxisAlignedBB
                .getBoundingBox(xCoord - 10, yCoord - 10, zCoord - 10, xCoord + 10, yCoord + 10, zCoord + 10);
        }
        return boundingBox;
    }

    public void setRunning(boolean running) {
        if (!worldObj.isRemote) {
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

    public float getTimer() {
        return timer;
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
        super.writeToNBT(compound);
    }

    public void writeToNBTData(NBTTagCompound compound) {
        compound.setBoolean(RUNNING_NBT_TAG, running);
        super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        running = compound.getBoolean(RUNNING_NBT_TAG);
        timer = compound.getFloat(TIMER_NBT_TAG);
        super.readFromNBT(compound);
    }

    public void readFromNBTData(NBTTagCompound compound) {
        running = compound.getBoolean(RUNNING_NBT_TAG);
        super.readFromNBT(compound);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        writeToNBTData(nbttagcompound);

        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, nbttagcompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBTData(pkt.func_148857_g());
    }

    public void updateToClient() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        markDirty();
    }

}
