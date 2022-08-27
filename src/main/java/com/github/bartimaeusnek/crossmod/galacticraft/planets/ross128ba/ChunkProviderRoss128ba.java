/*
 * Copyright (c) 2018-2020 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.crossmod.galacticraft.planets.ross128ba;

import com.github.bartimaeusnek.bartworks.util.NoiseUtil.BartsNoise;
import com.github.bartimaeusnek.crossmod.galacticraft.planets.ross128b.ChunkProviderRoss128b;
import gregtech.api.objects.XSTR;
import java.util.Arrays;
import java.util.Random;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.MapGenBaseMeta;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.world.gen.BiomeGenBaseMoon;
import micdoodle8.mods.galacticraft.core.world.gen.ChunkProviderMoon;
import micdoodle8.mods.galacticraft.core.world.gen.MapGenCavesMoon;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

public class ChunkProviderRoss128ba extends ChunkProviderMoon {

    private final XSTR rand = new XSTR();
    private final World worldObj;
    private BiomeGenBase[] biomesForGeneration;
    private final MapGenBaseMeta caveGenerator;

    public ChunkProviderRoss128ba(World world, long seed, boolean mapFeaturesEnabled) {
        super(world, seed, mapFeaturesEnabled);
        this.biomesForGeneration = new BiomeGenBase[] {BiomeGenBaseMoon.moonFlat};
        this.caveGenerator = new MapGenCavesMoon();
        this.worldObj = world;
    }

    public Chunk provideChunk(int cx, int cz) {
        this.rand.setSeed((long) cx * 341873128712L + (long) cz * 132897987541L);
        Block[] ids = new Block[65536];
        byte[] meta = new byte[65536];
        Arrays.fill(ids, Blocks.air);
        this.generateTerrain(cx, cz, ids, meta);
        this.biomesForGeneration = this.worldObj
                .getWorldChunkManager()
                .loadBlockGeneratorData(this.biomesForGeneration, cx * 16, cz * 16, 16, 16);
        this.createCraters(cx, cz, ids, meta);
        this.replaceBlocksForBiome(cx, cz, ids, meta, this.biomesForGeneration);
        this.caveGenerator.generate(this, this.worldObj, cx, cz, ids, meta);
        Chunk Chunk = new Chunk(this.worldObj, ids, meta, cx, cz);
        Chunk.generateSkylightMap();
        return Chunk;
    }

    public void decoratePlanet(World par1World, Random par2Random, int par3, int par4) {}

    @Override
    public void populate(IChunkProvider par1IChunkProvider, int par2, int par3) {
        super.populate(par1IChunkProvider, par2, par3);
        BlockFalling.fallInstantly = true;
        ChunkProviderRoss128b.BWOreGen.generate(this.rand, par2, par3, this.worldObj, this, this);
        BlockFalling.fallInstantly = false;
    }

    private int getIndex(int x, int y, int z) {
        return (x * 16 + z) * 256 + y;
    }

    final Block lowerBlockID = GCBlocks.blockMoon;
    final BartsNoise noiseGen = new BartsNoise(2, 0.008F, 1D, System.nanoTime());
    final BartsNoise noiseGen2 = new BartsNoise(2, 0.01F, 1D, System.nanoTime());
    final BartsNoise noiseGen3 = new BartsNoise(2, 0.002F, 1D, System.nanoTime());

    public void generateTerrain(int chunkX, int chunkZ, Block[] idArray, byte[] metaArray) {
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                double d = noiseGen.getNoise(x + chunkX * 16, z + chunkZ * 16);
                double d2 = noiseGen2.getNoise(x + chunkX * 16, z + chunkZ * 16);
                double d3 = noiseGen3.getCosNoise(x + chunkX * 16, z + chunkZ * 16);

                double yDev = d * 4 + d2 * 2 + d3;

                for (int y = 0; y < 128; ++y) {
                    if ((double) y < 60.0D + yDev) {
                        idArray[this.getIndex(x, y, z)] = this.lowerBlockID;
                        int var10001 = this.getIndex(x, y, z);
                        metaArray[var10001] = 4;
                    }
                }
            }
        }
    }
}
