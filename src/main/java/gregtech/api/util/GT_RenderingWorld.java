package gregtech.api.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

/**
 * Provide a fake IBlockAccess to support CTM. Facade are supposed to set these when they are placed/received by client.
 */
public class GT_RenderingWorld implements IBlockAccess {

    private static final GT_RenderingWorld INSTANCE = new GT_RenderingWorld();
    /*
     * I do not think this map would ever grow too huge, so I won't go too overcomplicated on this one
     */
    private final Map<ChunkPosition, BlockInfo> infos = new HashMap<>();
    private final Map<ChunkCoordIntPair, Set<ChunkPosition>> index = new HashMap<>();
    private IBlockAccess mWorld = Minecraft.getMinecraft().theWorld;

    private GT_RenderingWorld() {
        new FMLEventHandler();
        new ForgeEventHandler();
    }

    public static GT_RenderingWorld getInstance() {
        return INSTANCE;
    }

    public static GT_RenderingWorld getInstance(IBlockAccess aWorld) {
        if (aWorld == INSTANCE) return INSTANCE;
        if (aWorld == null) INSTANCE.mWorld = Minecraft.getMinecraft().theWorld;
        else INSTANCE.mWorld = aWorld;
        return INSTANCE;
    }

    private void setWorld(IBlockAccess aWorld) {
        if (aWorld == null) mWorld = Minecraft.getMinecraft().theWorld;
        else mWorld = aWorld;
    }

    public void register(int x, int y, int z, Block block, int meta) {
        ChunkPosition key = new ChunkPosition(x, y, z);
        infos.put(key, new BlockInfo(block, meta));
        index.computeIfAbsent(new ChunkCoordIntPair(x >> 4, z >> 4), p -> new HashSet<>())
            .add(key);
    }

    public void unregister(int x, int y, int z, Block block, int meta) {
        ChunkPosition key = new ChunkPosition(x, y, z);
        if (infos.remove(key, new BlockInfo(block, meta))) {
            ChunkCoordIntPair chunkKey = new ChunkCoordIntPair(x >> 4, z >> 4);
            Set<ChunkPosition> set = index.get(chunkKey);
            set.remove(key);
            if (set.isEmpty()) index.remove(chunkKey);
        }
    }

    @Override
    public Block getBlock(int p_147439_1_, int p_147439_2_, int p_147439_3_) {
        BlockInfo blockInfo = infos.get(new ChunkPosition(p_147439_1_, p_147439_2_, p_147439_3_));
        return blockInfo != null ? blockInfo.block : mWorld.getBlock(p_147439_1_, p_147439_2_, p_147439_3_);
    }

    @Override
    public TileEntity getTileEntity(int p_147438_1_, int p_147438_2_, int p_147438_3_) {
        return mWorld.getTileEntity(p_147438_1_, p_147438_2_, p_147438_3_);
    }

    @Override
    public int getLightBrightnessForSkyBlocks(int p_72802_1_, int p_72802_2_, int p_72802_3_, int p_72802_4_) {
        return mWorld.getLightBrightnessForSkyBlocks(p_72802_1_, p_72802_2_, p_72802_3_, p_72802_4_);
    }

    @Override
    public int getBlockMetadata(int p_72805_1_, int p_72805_2_, int p_72805_3_) {
        BlockInfo blockInfo = infos.get(new ChunkPosition(p_72805_1_, p_72805_2_, p_72805_3_));
        return blockInfo != null ? blockInfo.meta : mWorld.getBlockMetadata(p_72805_1_, p_72805_2_, p_72805_3_);
    }

    @Override
    public int isBlockProvidingPowerTo(int p_72879_1_, int p_72879_2_, int p_72879_3_, int p_72879_4_) {
        return mWorld.isBlockProvidingPowerTo(p_72879_1_, p_72879_2_, p_72879_3_, p_72879_4_);
    }

    @Override
    public boolean isAirBlock(int p_147437_1_, int p_147437_2_, int p_147437_3_) {
        return getBlock(p_147437_1_, p_147437_2_, p_147437_3_).isAir(mWorld, p_147437_1_, p_147437_2_, p_147437_3_);
    }

    @Override
    public BiomeGenBase getBiomeGenForCoords(int p_72807_1_, int p_72807_2_) {
        return mWorld.getBiomeGenForCoords(p_72807_1_, p_72807_2_);
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

    public class FMLEventHandler {

        public FMLEventHandler() {
            FMLCommonHandler.instance()
                .bus()
                .register(this);
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public void onRenderTickStart(TickEvent.RenderTickEvent e) {
            if (e.phase == TickEvent.Phase.START) mWorld = Minecraft.getMinecraft().theWorld;
        }
    }

    public class ForgeEventHandler {

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
