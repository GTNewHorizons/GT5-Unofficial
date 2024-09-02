package gregtech.common.tileentities.render;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityDrone extends TileEntity {

    public double rotation = 0;

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return 65536;
    }

    @Override
    public void updateEntity() {
        rotation = (rotation + 50) % 360d;
    }
}
