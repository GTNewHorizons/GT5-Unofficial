package gregtech.crossmod.visualprospecting;

import java.util.Optional;

import net.minecraft.world.ChunkCoordIntPair;

public interface IDatabase {

    Optional<String> getVeinName(int dimensionId, ChunkCoordIntPair coordinates);
}
