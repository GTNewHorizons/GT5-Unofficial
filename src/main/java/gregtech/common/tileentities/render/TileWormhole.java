package gregtech.common.tileentities.render;

import java.util.List;
import java.util.Optional;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;

import cpw.mods.fml.relauncher.Side;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAdvanced;
import micdoodle8.mods.galacticraft.core.util.Annotations;
import pers.gwyog.gtneioreplugin.plugin.block.ModBlocks;
import pers.gwyog.gtneioreplugin.util.DimensionHelper;

public class TileWormhole extends TileEntityAdvanced {

    @Annotations.NetworkedField(targetSide = Side.CLIENT)
    public boolean shouldRender = false;
    @Annotations.NetworkedField(targetSide = Side.CLIENT)
    public int dimID = 0;

    @Annotations.NetworkedField(targetSide = Side.CLIENT)
    public double targetRadius = 0;

    @Override
    public void addExtraNetworkedData(List<Object> networkedList) {
        super.addExtraNetworkedData(networkedList);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("dimID", dimID);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        dimID = compound.getInteger("dimID");
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
        }
    }

    public void setRadius(double target) {
        targetRadius = target;
    }

    public Block getBlock() {
        return ModBlocks.getBlock(DimensionHelper.DimNameDisplayed[dimID]);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (worldObj != null && worldObj.isRemote) {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return 65536;
    }

    @Override
    public double getPacketRange() {
        return 128;
    }

    @Override
    public int getPacketCooldown() {
        return 20;
    }

    @Override
    public boolean isNetworkedTile() {
        return true;
    }
}
