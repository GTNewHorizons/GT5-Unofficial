package gregtech.crossmod.visualprospecting;

import java.util.Optional;

import javax.annotation.Nullable;

import net.minecraft.world.ChunkCoordIntPair;

public class VisualProspectingDatabase {

    private static IDatabase database;

    @SuppressWarnings("unused")
    public static void registerDatabase(IDatabase aDatabase) {
        database = aDatabase;
    }

    public static Optional<String> getVeinName(int dimensionId, @Nullable ChunkCoordIntPair coordinates) {
        if (database == null || coordinates == null) {
            return Optional.empty();
        }

        return database.getVeinName(dimensionId, coordinates);
    }
}
