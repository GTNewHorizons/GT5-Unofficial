package common.tileentities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import org.joml.Vector3i;
import org.joml.Vector3ic;

public class TE_BeamTransmitter extends TileEntity {

    private final Vector3ic position;

    private Vector3ic target = new Vector3i(10, 20, 10);
    private double distanceCache;
    private boolean distanceCacheValid = false;

    public TE_BeamTransmitter() {
        position = new Vector3i(super.xCoord, super.yCoord, super.zCoord);
    }

    public Vector3ic getTargetPosition() {
        return target;
    }

    public double getDistanceFromTarget() {
        if (!distanceCacheValid) {
            distanceCache = position.distance(target);
            distanceCacheValid = true;
        }
        return distanceCache;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public double getMaxRenderDistanceSquared() {
        // 4k is standard, 65k is what the vanilla beacon uses
        return 65536.0D;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        // Make it so the beam is still rendered even when the source block is out of sight
        return INFINITE_EXTENT_AABB;
    }
}
