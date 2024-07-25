package gregtech.common.tileentities.render;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileLaser extends TileEntity {

    public boolean shouldRender = false;
    public float red = 0, green = 0, blue = 0;

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setFloat("rgb_red", red);
        compound.setFloat("rgb_green", green);
        compound.setFloat("rgb_blue", blue);
        compound.setBoolean("shouldRender", shouldRender);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        red = compound.getFloat("rgb_red");
        blue = compound.getFloat("rgb_blue");
        green = compound.getFloat("rgb_green");
        shouldRender = compound.getBoolean("shouldRender");
    }

    public void setColors(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return 65536;
    }
}
