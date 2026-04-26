package gregtech.common.misc;

import java.util.List;

import net.minecraft.util.AxisAlignedBB;

public interface IWorkAreaProvider {

    enum WorkAreaChunkState {
        MINED,
        CURRENT,
        PENDING,
        ACTIVE,
        INACTIVE
    }

    boolean isWorkAreaShown();

    AxisAlignedBB getWorkAreaAABB();

    List<WorkAreaChunk> getWorkAreaChunksInWorkOrder();

    int getCurrentWorkAreaOrder();

    default boolean shouldRenderWorkAreaChunkNumbers() {
        return true;
    }

    default double getWorkAreaNumberY() {
        return 255D;
    }

    default WorkAreaChunkState getWorkAreaChunkState(WorkAreaChunk chunk) {
        int currentOrder = getCurrentWorkAreaOrder();

        if (currentOrder <= 0) {
            return WorkAreaChunkState.PENDING;
        }

        if (chunk.order() < currentOrder) {
            return WorkAreaChunkState.MINED;
        }

        if (chunk.order() == currentOrder) {
            return WorkAreaChunkState.CURRENT;
        }

        return WorkAreaChunkState.PENDING;
    }
}
