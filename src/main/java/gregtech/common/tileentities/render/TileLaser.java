package gregtech.common.tileentities.render;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileLaser extends TileEntity {

    public boolean shouldRender = false;
    public byte r = 0;
    public byte g = 0;
    public byte b = 0;

    public void setColors(byte red, byte green, byte blue) {
        r = red; g = green; b = blue;
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
