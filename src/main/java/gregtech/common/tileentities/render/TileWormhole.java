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

import pers.gwyog.gtneioreplugin.plugin.block.ModBlocks;
import pers.gwyog.gtneioreplugin.util.DimensionHelper;

public class TileWormhole extends TileEntity {

    public int dimID = 0;

    public double targetRadius = 0;

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("dimID", dimID);
        compound.setDouble("targetRadius", targetRadius);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        dimID = compound.getInteger("dimID");
        targetRadius = compound.getDouble("targetRadius");
    }

    public int getDimFromWorld(World target) {
        if (target == null) return 0;
        String dimName = Optional.ofNullable(target.provider)
            .map(WorldProvider::getDimensionName)
            .orElse(null);
        if (dimName == null) return 0;
        for (int i = 0; i < DimensionHelper.DimName.length; i++) {
            if (dimName.equals(DimensionHelper.DimName[i])) return i;
        }
        return 0;
    }

    public void setDimFromWorld(World target) {
        int newName = getDimFromWorld(target);
        if (target != null & dimID != newName) {
            dimID = newName;
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
        return ModBlocks.getBlock(DimensionHelper.DimNameDisplayed[dimID]);
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
