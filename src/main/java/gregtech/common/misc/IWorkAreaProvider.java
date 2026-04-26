package gregtech.common.misc;

import java.util.List;

import net.minecraft.util.AxisAlignedBB;

public interface IWorkAreaProvider {

    boolean isWorkAreaShown();

    AxisAlignedBB getWorkAreaAABB();

    List<WorkAreaChunk> getWorkAreaChunksInWorkOrder();

    int getCurrentWorkAreaOrder();

    default boolean shouldRenderWorkAreaChunkNumbers() {
        return true;
    }

    default double getWorkAreaNumberY() {
        return 200D;
    }
}
