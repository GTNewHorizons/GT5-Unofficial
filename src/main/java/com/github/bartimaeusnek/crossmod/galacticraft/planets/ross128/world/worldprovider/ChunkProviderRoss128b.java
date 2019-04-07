/*
 * Copyright (c) 2019 bartimaeusnek
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

package com.github.bartimaeusnek.crossmod.galacticraft.planets.ross128.world.worldprovider;

import com.github.bartimaeusnek.crossmod.galacticraft.planets.ross128.world.oregen.BW_WordGenerator;
import gregtech.api.objects.XSTR;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.MapGenCaves;
import net.minecraft.world.gen.MapGenRavine;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.List;

import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ICE;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAKE;

public class ChunkProviderRoss128b extends ChunkProviderGenerate {
    XSTR rand = new XSTR();
    private BiomeGenBase[] biomesForGeneration;
    private BW_WordGenerator BWOreGen = new BW_WordGenerator();
    private World worldObj;
    private MapGenBase caveGenerator = new MapGenCaves();
    private MapGenBase ravineGenerator = new MapGenRavine();

    public ChunkProviderRoss128b(World par1World, long seed, boolean mapFeaturesEnabled) {
        super(par1World, seed, mapFeaturesEnabled);
        worldObj = par1World;
    }

    @Override
    public List getPossibleCreatures(EnumCreatureType p_73155_1_, int p_73155_2_, int p_73155_3_, int p_73155_4_) {
        return null;
    }

    public Chunk provideChunk(int p_73154_1_, int p_73154_2_) {
        this.rand.setSeed((long) p_73154_1_ * 341873128712L + (long) p_73154_2_ * 132897987541L);
        Block[] ablock = new Block[65536];
        byte[] abyte = new byte[65536];
        this.func_147424_a(p_73154_1_, p_73154_2_, ablock);
        this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, p_73154_1_ * 16, p_73154_2_ * 16, 16, 16);
        for (int i = 0; i < biomesForGeneration.length; i++) {
            BiomeGenBase biomeGenBase = biomesForGeneration[i];
            if (biomeGenBase.biomeID == BiomeGenBase.mushroomIsland.biomeID) {
                this.biomesForGeneration[i] = BiomeGenBase.taiga;
            } else if (biomeGenBase.biomeID == BiomeGenBase.mushroomIslandShore.biomeID) {
                this.biomesForGeneration[i] = BiomeGenBase.stoneBeach;
            }
        }
        this.replaceBlocksForBiome(p_73154_1_, p_73154_2_, ablock, abyte, this.biomesForGeneration);
        this.caveGenerator.func_151539_a(this, this.worldObj, p_73154_1_, p_73154_2_, ablock);
        this.ravineGenerator.func_151539_a(this, this.worldObj, p_73154_1_, p_73154_2_, ablock);

        Chunk chunk = new Chunk(this.worldObj, ablock, abyte, p_73154_1_, p_73154_2_);
        byte[] abyte1 = chunk.getBiomeArray();

        for (int k = 0; k < abyte1.length; ++k) {
            abyte1[k] = (byte) this.biomesForGeneration[k].biomeID;
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    @Override
    public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_) {
        BlockFalling.fallInstantly = true;
        int k = p_73153_2_ * 16;
        int l = p_73153_3_ * 16;
        BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(k + 16, l + 16);
        this.rand.setSeed(this.worldObj.getSeed());
        if (p_73153_2_ % 4 == 0 || p_73153_3_ % 4 == 0) {
            long i1 = this.rand.nextLong() / 2L * 2L + 1L;
            long j1 = this.rand.nextLong() / 2L * 2L + 1L;
            this.rand.setSeed((long) p_73153_2_ * i1 + (long) p_73153_3_ * j1 ^ this.worldObj.getSeed());
        }
        boolean flag = false;

        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Pre(p_73153_1_, worldObj, rand, p_73153_2_, p_73153_3_, flag));

        int k1;
        int l1;
        int i2;

        if (biomegenbase != BiomeGenBase.desert && biomegenbase != BiomeGenBase.desertHills && !flag && this.rand.nextInt(4) == 0
                && TerrainGen.populate(p_73153_1_, worldObj, rand, p_73153_2_, p_73153_3_, flag, LAKE)) {
            k1 = k + this.rand.nextInt(16) + 8;
            l1 = this.rand.nextInt(256);
            i2 = l + this.rand.nextInt(16) + 8;
            (new WorldGenLakes(Blocks.water)).generate(this.worldObj, this.rand, k1, l1, i2);
        }

        biomegenbase.decorate(this.worldObj, this.rand, k, l);

        k += 8;
        l += 8;

        boolean doGen = TerrainGen.populate(p_73153_1_, worldObj, rand, p_73153_2_, p_73153_3_, flag, ICE);
        for (k1 = 0; doGen && k1 < 16; ++k1) {
            for (l1 = 0; l1 < 16; ++l1) {
                i2 = this.worldObj.getPrecipitationHeight(k + k1, l + l1);

                if (this.worldObj.isBlockFreezable(k1 + k, i2 - 1, l1 + l)) {
                    this.worldObj.setBlock(k1 + k, i2 - 1, l1 + l, Blocks.ice, 0, 2);
                }

                if (this.worldObj.func_147478_e(k1 + k, i2, l1 + l, true)) {
                    this.worldObj.setBlock(k1 + k, i2, l1 + l, Blocks.snow_layer, 0, 2);
                }
            }
        }
        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Post(p_73153_1_, worldObj, rand, p_73153_2_, p_73153_3_, flag));

        BWOreGen.generate(rand, p_73153_2_, p_73153_3_, worldObj, this, this);

        BlockFalling.fallInstantly = false;
    }

    @Override
    public void recreateStructures(int p_82695_1_, int p_82695_2_) {
    }

    @Override
    public void replaceBlocksForBiome(int p_147422_1_, int p_147422_2_, Block[] blocks, byte[] metas, BiomeGenBase[] p_147422_5_) {
        super.replaceBlocksForBiome(p_147422_1_, p_147422_2_, blocks, metas, p_147422_5_);
        for (int i = 0; i < blocks.length; i++) {
//            if (blocks[i] == Blocks.stone) {
//                blocks[i] = Ross128.Ross128bStone.getBlock();
//                metas[i] = Ross128.Ross128bStone.getMetadata();
//            }else
            if (blocks[i] == Blocks.grass) {
                blocks[i] = Blocks.dirt;
                metas[i] = 2;
            }
//            else if (blocks[i] == Blocks.dirt) {
//                blocks[i] = Ross128.Ross128bDirt.getBlock();
//                metas[i] = Ross128.Ross128bDirt.getMetadata();
//            }
        }
    }
}
