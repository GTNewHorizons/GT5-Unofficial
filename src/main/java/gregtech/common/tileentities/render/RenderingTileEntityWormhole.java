package gregtech.common.tileentities.render;

import java.util.Optional;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;

import gtneioreplugin.plugin.block.ModBlocks;
import gtneioreplugin.util.DimensionHelper;

public class RenderingTileEntityWormhole extends AbstractRenderingTileEntity {

    public int dimID = 0;

    public double targetRadius = 0;

    public RenderingTileEntityWormhole() {
        super(1);
    }

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

}
