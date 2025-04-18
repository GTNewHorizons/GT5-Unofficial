package gregtech.common.tileentities.render;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityNanoForgeRenderer extends TileEntity {

    private AxisAlignedBB boundingBox;

    private long timer = 0;
    private boolean running = false;
    private long startTime = 0;

    private static final String NBT_TAG = "NANOFORGE";

    private static final String TIMER_NBT_TAG = NBT_TAG + "TIMER";
    private static final String START_TIME_NBT_TAG = NBT_TAG + "START_TIME";
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
            this.startTime = timer;
            updateToClient();
        }
    }

    public boolean getRunning() {
        return running;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getTimer() {
        long time = timer;
        timer += 1;
        if (timer / 36000 == 1) {
            if (startTime < timer) {
                startTime = 0;
            }
            timer = 0;
        }
        return time;
    }

    public void writeToNBT(NBTTagCompound compound) {
        compound.setLong(TIMER_NBT_TAG, timer);
        compound.setLong(START_TIME_NBT_TAG, startTime);
        compound.setBoolean(RUNNING_NBT_TAG, running);
        super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        timer = compound.getLong(TIMER_NBT_TAG);
        startTime = compound.getLong(START_TIME_NBT_TAG);
        running = compound.getBoolean(RUNNING_NBT_TAG);
        super.readFromNBT(compound);
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
