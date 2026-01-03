package gregtech.common;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

import gregtech.api.objects.XSTR;
import gregtech.common.data.BlockPosMap;

public class GTMockWorld extends World {

    public static class BlockData {

        public Block block = Blocks.air;
        public int meta;
        public TileEntity tile;

        public BlockData() {}

        public BlockData(int x, int y, int z) {}
    }

    public final BlockPosMap<BlockData> blocks = new BlockPosMap<>();

    private boolean createTiles = false, updateTiles = false;

    public GTMockWorld(ISaveHandler saveHandler, String name, WorldProvider worldProvider, WorldSettings worldSettings,
        Profiler profiler) {
        super(saveHandler, name, worldSettings, worldProvider, profiler);
        this.rand = new XSTR();
    }

    public GTMockWorld() {
        this(
            new NoopSaveHandler(),
            "DUMMY_DIMENSION",
            null,
            new WorldSettings(new WorldInfo(new NBTTagCompound())),
            new Profiler());
    }

    public void createTiles(boolean createTiles) {
        this.createTiles = createTiles;
    }

    public void updateTiles(boolean updateTiles) {
        this.updateTiles = updateTiles;
    }

    public void clear() {
        blocks.clear();
    }

    @Override
    protected IChunkProvider createChunkProvider() {
        return null;
    }

    @Override
    public Entity getEntityByID(int aEntityID) {
        return null;
    }

    @Override
    public boolean setBlock(int x, int y, int z, Block block, int meta, int flags) {
        if (block == Blocks.air) {
            blocks.remove(x, y, z);
        } else {
            BlockData data = blocks.computeIfAbsent(x, y, z, BlockData::new);

            data.block = block;
            data.meta = meta;
            data.tile = null;

            if (createTiles) {
                setTileEntity(x, y, z, null);

                if (data.block.hasTileEntity(meta)) {
                    setTileEntity(x, y, z, data.block.createTileEntity(this, meta));
                }
            }
        }

        return true;
    }

    @Override
    public boolean setBlockMetadataWithNotify(int x, int y, int z, int meta, int flags) {
        BlockData data = blocks.get(x, y, z);

        if (data == null) return false;

        data.meta = meta;
        data.tile = null;

        if (createTiles) {
            setTileEntity(x, y, z, null);

            if (data.block.hasTileEntity(meta)) {
                setTileEntity(x, y, z, data.block.createTileEntity(this, meta));
            }
        }

        return true;
    }

    @Override
    public float getSunBrightnessFactor(float p_72967_1_) {
        return 1.0F;
    }

    @Override
    public BiomeGenBase getBiomeGenForCoords(int x, int z) {
        return BiomeGenBase.plains;
    }

    @Override
    public int getFullBlockLightValue(int x, int y, int z) {
        return 15;
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        BlockData block = blocks.get(x, y, z);

        return block == null ? Blocks.air : block.block;
    }

    @Override
    public int getBlockMetadata(int x, int y, int z) {
        BlockData block = blocks.get(x, y, z);

        return block == null ? 0 : block.meta;
    }

    @Override
    public TileEntity getTileEntity(int x, int y, int z) {
        BlockData block = blocks.get(x, y, z);

        return block == null ? null : block.tile;
    }

    @Override
    public void setTileEntity(int x, int y, int z, TileEntity tile) {
        if (tile == null) {
            BlockData block = blocks.get(x, y, z);

            if (block != null && block.tile != null) {
                block.tile.invalidate();
                block.tile = null;
            }
        } else {
            setTileEntity(x, y, z, null);

            BlockData block = blocks.computeIfAbsent(x, y, z, BlockData::new);

            block.tile = tile;

            tile.setWorldObj(this);
            tile.xCoord = x;
            tile.yCoord = y;
            tile.zCoord = z;
            tile.validate();

            if (updateTiles && tile.canUpdate()) {
                tile.updateEntity();
            }
        }
    }

    @Override
    public boolean canBlockSeeTheSky(int x, int y, int z) {
        return true;
    }

    @Override
    protected int func_152379_p() {
        return 0;
    }

    private static class NoopSaveHandler implements ISaveHandler {

        @Override
        public void saveWorldInfoWithPlayer(WorldInfo worldInfo, NBTTagCompound nbtTagCompound) {}

        @Override
        public void saveWorldInfo(WorldInfo worldInfo) {}

        @Override
        public WorldInfo loadWorldInfo() {
            return null;
        }

        @Override
        public IPlayerFileData getSaveHandler() {
            return null;
        }

        @Override
        public File getMapFileFromName(String mapName) {
            return null;
        }

        @Override
        public IChunkLoader getChunkLoader(WorldProvider worldProvider) {
            return null;
        }

        @Override
        public void flush() {}

        @Override
        public void checkSessionLock() {}

        @Override
        public String getWorldDirectoryName() {
            return null;
        }

        @Override
        public File getWorldDirectory() {
            return null;
        }
    }
}
