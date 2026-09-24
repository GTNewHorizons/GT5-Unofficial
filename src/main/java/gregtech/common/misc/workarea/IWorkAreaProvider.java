package gregtech.common.misc.workarea;

import java.util.List;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import org.jetbrains.annotations.Nullable;

import gregtech.common.misc.WorkAreaChunk;

public interface IWorkAreaProvider {

    enum WorkAreaChunkState {
        MINED,
        CURRENT,
        PENDING,
        ACTIVE,
        INACTIVE
    }

    boolean isWorkAreaShown();

    @Nullable
    World getWorkAreaWorld();

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
