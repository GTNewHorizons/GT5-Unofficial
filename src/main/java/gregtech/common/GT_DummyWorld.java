package gregtech.common;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

public class GT_DummyWorld extends World {

    public GT_IteratorRandom mRandom = new GT_IteratorRandom();
    public ItemStack mLastSetBlock = null;

    public GT_DummyWorld(ISaveHandler saveHandler, String name, WorldProvider worldProvider,
        WorldSettings worldSettings, Profiler profiler) {
        super(saveHandler, name, worldSettings, worldProvider, profiler);
        this.rand = this.mRandom;
    }

    public GT_DummyWorld() {
        this(new ISaveHandler() {

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
        }, "DUMMY_DIMENSION", null, new WorldSettings(new WorldInfo(new NBTTagCompound())), new Profiler());
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
    public boolean setBlock(int aX, int aY, int aZ, Block aBlock, int aMeta, int aFlags) {
        this.mLastSetBlock = new ItemStack(aBlock, 1, aMeta);
        return true;
    }

    @Override
    public float getSunBrightnessFactor(float p_72967_1_) {
        return 1.0F;
    }

    @Override
    public BiomeGenBase getBiomeGenForCoords(int aX, int aZ) {
        if ((aX >= 16) && (aZ >= 16) && (aX < 32) && (aZ < 32)) {
            return BiomeGenBase.plains;
        }
        return BiomeGenBase.ocean;
    }

    @Override
    public int getFullBlockLightValue(int aX, int aY, int aZ) {
        return 10;
    }

    @Override
    public Block getBlock(int aX, int aY, int aZ) {
        if ((aX >= 16) && (aZ >= 16) && (aX < 32) && (aZ < 32)) {
            return aY == 64 ? Blocks.grass : Blocks.air;
        }
        return Blocks.air;
    }

    @Override
    public int getBlockMetadata(int aX, int aY, int aZ) {
        return 0;
    }

    @Override
    public boolean canBlockSeeTheSky(int aX, int aY, int aZ) {
        if ((aX >= 16) && (aZ >= 16) && (aX < 32) && (aZ < 32)) {
            return aY > 64;
        }
        return true;
    }

    @Override
    protected int func_152379_p() {
        return 0;
    }
}
