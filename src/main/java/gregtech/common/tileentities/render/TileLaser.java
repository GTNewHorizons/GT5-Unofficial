package gregtech.common.tileentities.render;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileLaser extends TileEntity {

    public boolean shouldRender = false;
    public float red = 0, green = 0, blue = 0;

    public void setColors(float red, float green, float blue) {
        this.red = red; this.green = green; this.blue = blue;
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
