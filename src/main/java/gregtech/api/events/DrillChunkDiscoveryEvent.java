package gregtech.api.events;

import java.util.UUID;

import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import cpw.mods.fml.common.eventhandler.Event;

/// Fired when a drill begins mining a new chunk.
public class DrillChunkDiscoveryEvent extends Event {

    /// World containing the chunk being mined.
    public final @NotNull World world;

    /// UUID of the drill's owner.
    public final @NotNull UUID owner;

    /// Coordinates of the chunk being mined.
    public final int chunkX, chunkZ;

    /// Whether this is a chunk discovery or depletion.
    public final boolean discovery;

    public DrillChunkDiscoveryEvent(@NotNull World world, @NotNull UUID owner, int chunkX, int chunkZ,
        boolean discovery) {
        this.world = world;
        this.owner = owner;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.discovery = discovery;
    }
}
