package gregtech.common.blocks.rubbertree;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.event.world.ChunkEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.GregTechAPI;

// TODO Remove this class in next major version after GT rubber tree is implemented
public class RubberTreeWorldEvents {

    @SubscribeEvent
    public void onChunkLoad(ChunkEvent.Load event) {
        World world = event.world;
        if (world == null || world.isRemote) {
            return;
        }

        Chunk chunk = event.getChunk();
        if (chunk == null) {
            return;
        }

        Random random = world.rand;
        ExtendedBlockStorage[] storages = chunk.getBlockStorageArray();
        if (storages == null) {
            return;
        }

        for (ExtendedBlockStorage storage : storages) {
            if (storage == null) {
                continue;
            }

            int yBase = storage.getYLocation();

            for (int lx = 0; lx < 16; lx++) {
                for (int lz = 0; lz < 16; lz++) {
                    for (int ly = 0; ly < 16; ly++) {
                        Block block = storage.getBlockByExtId(lx, ly, lz);
                        if (block != GregTechAPI.sBlockRubberLogNatural) {
                            continue;
                        }

                        int meta = storage.getExtBlockMetadata(lx, ly, lz);
                        if (meta != BlockRubberLogNatural.META_POSTEA_TRANSFORM) {
                            continue;
                        }

                        int wx = chunk.xPosition * 16 + lx;
                        int wy = yBase + ly;
                        int wz = chunk.zPosition * 16 + lz;

                        // small random delay to spread the load
                        world.scheduleBlockUpdate(wx, wy, wz, block, 1 + random.nextInt(20));
                    }
                }
            }
        }
    }
}
