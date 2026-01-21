package gregtech.common.tileentities.render;

import java.util.Optional;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;

import gtneioreplugin.plugin.block.ModBlocks;
import gtneioreplugin.util.DimensionHelper;

public class TileEntityWormhole extends TileEntity {

    public int dimIndex = 0;

    public double targetRadius = 0;

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("dimIndex", dimIndex);
        compound.setDouble("targetRadius", targetRadius);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("dimIndex")) {
            dimIndex = compound.getInteger("dimIndex");
        } else {
            dimIndex = compound.getInteger("dimID");
        }
        targetRadius = compound.getDouble("targetRadius");
    }

    public int getDimFromWorld(World target) {
        if (target == null) return 0;
        String dimName = Optional.ofNullable(target.provider)
            .map(WorldProvider::getDimensionName)
            .orElse(null);
        if (dimName == null) return 0;
        return DimensionHelper.getIndex(dimName);
    }

    public void setDimFromWorld(World target) {
        int newName = getDimFromWorld(target);
        if (target != null & dimIndex != newName) {
            dimIndex = newName;
            this.markDirty();
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }

    public void setRadius(double target) {
        targetRadius = target;
        this.markDirty();
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public Block getBlock() {
        DimensionHelper.Dimension record = DimensionHelper.getByIndex(dimIndex);
        if (record == null) {
            return null;
        }
        return ModBlocks.getBlock(record.abbr());
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return 65536;
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

}
