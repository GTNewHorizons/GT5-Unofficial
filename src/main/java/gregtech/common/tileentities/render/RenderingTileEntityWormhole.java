package gregtech.common.tileentities.render;

import java.util.Optional;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;

import gtneioreplugin.plugin.block.ModBlocks;
import gtneioreplugin.util.DimensionHelper;

public class RenderingTileEntityWormhole extends AbstractRenderingTileEntity {

    public int dimIndex = 0;

    public double targetRadius = 0;

    public RenderingTileEntityWormhole() {
        super(1);
    }

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

}
