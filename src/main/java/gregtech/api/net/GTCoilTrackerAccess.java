package gregtech.api.net;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;

import gregtech.common.data.GTCoilTracker;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongSet;

public class GTCoilTrackerAccess implements IClientMetaTracker {

    @Override
    public Long2IntOpenHashMap getTrackedBlocks(World world) {
        GTCoilTracker coilTracker = GTCoilTracker.getTracker(world);
        if (coilTracker == null) return null;
        return coilTracker.activeBlocks;
    }

    @Override
    public LongSet getTrackedBlocksByChunk(Chunk chunk) {
        GTCoilTracker coilTracker = GTCoilTracker.getTracker(chunk.worldObj);
        if (coilTracker == null) return null;
        return coilTracker.activeBlocksByChunk.get(CoordinatePacker.pack(chunk.xPosition, 0, chunk.zPosition));
    }
}
