package gregtech.api.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

/**
 * Provide a fake IBlockAccess to support CTM. Facade are supposed to set these when they are placed/received by client.
 */
public class GTRenderingWorld implements IBlockAccess {

    private static final ThreadLocal<GTRenderingWorld> INSTANCE = ThreadLocal.withInitial(GTRenderingWorld::new);
    /*
     * I do not think this map would ever grow too huge, so I won't go too overcomplicated on this one
     */
    private static final Map<ChunkPosition, BlockInfo> infos = new ConcurrentHashMap<>();
    private static final Map<ChunkCoordIntPair, Set<ChunkPosition>> index = new ConcurrentHashMap<>();
    private IBlockAccess mWorld = Minecraft.getMinecraft().theWorld;

    /**
     * Per-face meta override used by CTM textures during rendering to ensure each facade side
     * reports its own meta to the CTM icon lookup, regardless of what is stored in {@link #infos}.
     * ThreadLocal to support Angelica's multi-threaded rendering. Set/cleared around each icon query.
     * Layout: [x, y, z, meta]; x == Integer.MIN_VALUE means no override is active.
     */
    private static final ThreadLocal<int[]> META_OVERRIDE = ThreadLocal
        .withInitial(() -> new int[] { Integer.MIN_VALUE, 0, 0, 0 });

    public static void setMetaOverride(int x, int y, int z, int meta) {
        int[] o = META_OVERRIDE.get();
        o[0] = x;
        o[1] = y;
        o[2] = z;
        o[3] = meta;
    }

    public static void clearMetaOverride() {
        META_OVERRIDE.get()[0] = Integer.MIN_VALUE;
    }

    static {
        new ForgeEventHandler();
    }

    public static GTRenderingWorld getInstance(IBlockAccess aWorld) {
        if (aWorld instanceof GTRenderingWorld thiz) return thiz;
        if (aWorld == null) INSTANCE.get().mWorld = Minecraft.getMinecraft().theWorld;
        else INSTANCE.get().mWorld = aWorld;
        return INSTANCE.get();
    }

    public static void register(int x, int y, int z, Block block, int meta) {
        ChunkPosition key = new ChunkPosition(x, y, z);
        infos.put(key, new BlockInfo(block, meta));
        index.computeIfAbsent(new ChunkCoordIntPair(x >> 4, z >> 4), p -> new HashSet<>())
            .add(key);
    }

    public static void unregister(int x, int y, int z, Block block, int meta) {
        ChunkPosition key = new ChunkPosition(x, y, z);
        if (infos.remove(key, new BlockInfo(block, meta))) {
            ChunkCoordIntPair chunkKey = new ChunkCoordIntPair(x >> 4, z >> 4);
            Set<ChunkPosition> set = index.get(chunkKey);
            set.remove(key);
            if (set.isEmpty()) index.remove(chunkKey);
        }
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        BlockInfo blockInfo = infos.get(new ChunkPosition(x, y, z));
        return blockInfo != null ? blockInfo.block : mWorld.getBlock(x, y, z);
    }

    @Override
    public TileEntity getTileEntity(int x, int y, int z) {
        return mWorld.getTileEntity(x, y, z);
    }

    @Override
    public int getLightBrightnessForSkyBlocks(int x, int y, int z, int minLight) {
        return mWorld.getLightBrightnessForSkyBlocks(x, y, z, minLight);
    }

    @Override
    public int getBlockMetadata(int x, int y, int z) {
        final int[] o = META_OVERRIDE.get();
        if (x == o[0] && y == o[1] && z == o[2]) {
            return o[3];
        }
        BlockInfo blockInfo = infos.get(new ChunkPosition(x, y, z));
        return blockInfo != null ? blockInfo.meta : mWorld.getBlockMetadata(x, y, z);
    }

    @Override
    public int isBlockProvidingPowerTo(int x, int y, int z, int side) {
        return mWorld.isBlockProvidingPowerTo(x, y, z, side);
    }

    @Override
    public boolean isAirBlock(int x, int y, int z) {
        return getBlock(x, y, z).isAir(mWorld, x, y, z);
    }

    @Override
    public BiomeGenBase getBiomeGenForCoords(int x, int z) {
        return mWorld.getBiomeGenForCoords(x, z);
    }

    @Override
    public int getHeight() {
        return mWorld.getHeight();
    }

    @Override
    public boolean extendedLevelsInChunkCache() {
        return mWorld.extendedLevelsInChunkCache();
    }

    @Override
    public boolean isSideSolid(int x, int y, int z, ForgeDirection side, boolean _default) {
        return getBlock(x, y, z).isSideSolid(this, x, y, z, side);
    }

    public static class ForgeEventHandler {

        private ForgeEventHandler() {
            MinecraftForge.EVENT_BUS.register(this);
        }

        @SubscribeEvent
        public void onChunkUnloaded(ChunkEvent.Unload e) {
            if (!e.world.isRemote) return;
            Set<ChunkPosition> set = index.remove(
                e.getChunk()
                    .getChunkCoordIntPair());
            if (set != null) infos.keySet()
                .removeAll(set);
        }

        @SubscribeEvent
        public void onWorldUnloaded(WorldEvent.Unload e) {
            if (!e.world.isRemote) return;
            infos.clear();
            index.clear();
        }
    }

    private static class BlockInfo {

        private final Block block;
        private final int meta;

        public BlockInfo(Block block, int meta) {
            this.block = block;
            this.meta = meta;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            BlockInfo blockInfo = (BlockInfo) o;

            if (meta != blockInfo.meta) return false;
            return Objects.equals(block, blockInfo.block);
        }

        @Override
        public int hashCode() {
            int result = block != null ? block.hashCode() : 0;
            result = 31 * result + meta;
            return result;
        }
    }
}
