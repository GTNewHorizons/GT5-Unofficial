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

    private static final String NBT_TAG = "NANOFORGE";

    private static final String TIMER_NBT_TAG = NBT_TAG + "TIMER";

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        if (boundingBox == null) {
            boundingBox = AxisAlignedBB
                .getBoundingBox(xCoord - 10, yCoord - 10, zCoord - 10, xCoord + 10, yCoord + 10, zCoord + 10);
        }
        return boundingBox;
    }

    public void setTimer(long timer) {
        if (!worldObj.isRemote) {
            this.timer = timer;
            updateToClient();
        }
    }

    public long getTimer() {
        return timer;
    }

    public void writeToNBT(NBTTagCompound compound) {
        compound.setLong(TIMER_NBT_TAG, timer);
        super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        timer = compound.getLong(TIMER_NBT_TAG);
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
