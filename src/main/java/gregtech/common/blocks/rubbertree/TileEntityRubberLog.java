package gregtech.common.blocks.rubbertree;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import org.jetbrains.annotations.NotNull;

public class TileEntityRubberLog extends TileEntity {

    private static final long RESIN_TICK_INTERVAL = 100L; // Check only every 5 seconds is ok and cost less
    private byte resinSide = -1; // -1 = none, 2/3/4/5 = N/S/W/E
    private long nextRefillAt = 0L;

    public boolean hasResin() {
        return resinSide >= 2 && resinSide <= 5;
    }

    public int getResinSide() {
        return resinSide;
    }

    public void setResinSide(int side) {
        this.resinSide = (byte) side;
        this.nextRefillAt = 0L;
        sync();
    }

    public void clearResin() {
        this.resinSide = -1;
        sync();
    }

    public long getNextRefillAt() {
        return nextRefillAt;
    }

    public void setNextRefillAt(long nextRefillAt) {
        this.nextRefillAt = nextRefillAt;
        sync();
    }

    public void clearNextRefillAt() {
        this.nextRefillAt = 0L;
        sync();
    }

    private void sync() {
        markDirty();
        if (worldObj != null) {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }

    @Override
    public void updateEntity() {
        if (worldObj == null || worldObj.isRemote) {
            return;
        }

        if (!(worldObj.getBlock(xCoord, yCoord, zCoord) instanceof BlockRubberLog)) {
            return;
        }

        int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        if (!BlockRubberLog.isNatural(meta)) {
            return;
        }

        // One check per RESIN_TICK_INTERVAL / 20 seconds, staggered in time
        long now = worldObj.getTotalWorldTime();
        long offset = xCoord * 31L + yCoord * 17L + zCoord * 13L;
        if ((now + offset) % RESIN_TICK_INTERVAL != 0L) {
            return;
        }

        RubberTreeResinLogic.tickNaturalLog(worldObj, xCoord, yCoord, zCoord);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setByte("ResinSide", resinSide);
        nbt.setLong("NextRefillAt", nextRefillAt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        resinSide = nbt.getByte("ResinSide");
        nextRefillAt = nbt.getLong("NextRefillAt");
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, @NotNull S35PacketUpdateTileEntity packet) {
        boolean hadResinBefore = hasResin();

        readFromNBT(packet.func_148857_g());

        boolean hasResinNow = hasResin();

        if (worldObj != null) {
            worldObj.func_147479_m(xCoord, yCoord, zCoord);

            if (!hadResinBefore && hasResinNow) {
                RubberTreeClientEffects.spawnResinRefillParticles(worldObj, xCoord, yCoord, zCoord, resinSide);
            }
        }
    }
}
