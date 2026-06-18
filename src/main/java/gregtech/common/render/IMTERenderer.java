package gregtech.common.render;

import net.minecraft.util.AxisAlignedBB;

public interface IMTERenderer {

    void renderTESR(double x, double y, double z, float timeSinceLastTick);

    default AxisAlignedBB getRenderBoundingBox(int x, int y, int z) {
        return AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1);
    }
}
