package gregtech.api.interfaces;

import net.minecraft.world.World;

/**
 * Allows blocks to store one meta value in the world, while sending another meta value to the client. This is useful
 * for blocks that have special rendering requirements.
 */
public interface IBlockWithClientMeta {

    /**
     * Gets the metadata that should be sent to the client for this block. This metadata is not saved in the world
     * anywhere, and will only ever be sent to the client.
     *
     * @return The metadata.
     */
    int getClientMeta(World world, int x, int y, int z);
}
