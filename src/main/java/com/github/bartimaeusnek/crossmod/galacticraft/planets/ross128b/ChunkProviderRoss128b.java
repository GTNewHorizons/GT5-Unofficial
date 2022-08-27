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

package com.github.bartimaeusnek.crossmod.galacticraft.planets.ross128b;

import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ICE;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAKE;

import com.github.bartimaeusnek.bartworks.API.LoaderReference;
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.system.oregen.BW_WordGenerator;
import com.github.bartimaeusnek.bartworks.system.worldgen.MapGenRuins;
import com.github.bartimaeusnek.crossmod.thaumcraft.util.ThaumcraftHandler;
import gregtech.api.objects.XSTR;
import java.util.List;
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

public class ChunkProviderRoss128b extends ChunkProviderGenerate {
    XSTR rand = new XSTR();
    private BiomeGenBase[] biomesForGeneration;
    public static final BW_WordGenerator BWOreGen = new BW_WordGenerator();
    private final World worldObj;
    private final MapGenBase caveGenerator = new MapGenCaves();
    private final MapGenBase ravineGenerator = new MapGenRavine();
    private final MapGenRuins.RuinsBase ruinsBase = new MapGenRuins.RuinsBase();

    public ChunkProviderRoss128b(World par1World, long seed, boolean mapFeaturesEnabled) {
        super(par1World, seed, mapFeaturesEnabled);
        this.worldObj = par1World;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public List getPossibleCreatures(EnumCreatureType p_73155_1_, int p_73155_2_, int p_73155_3_, int p_73155_4_) {
        return null;
    }

    public Chunk provideChunk(int p_73154_1_, int p_73154_2_) {
        this.rand.setSeed((long) p_73154_1_ * 341873128712L + (long) p_73154_2_ * 132897987541L);
        Block[] ablock = new Block[65536];
        byte[] abyte = new byte[65536];
        this.func_147424_a(p_73154_1_, p_73154_2_, ablock);
        this.biomesForGeneration = this.worldObj
                .getWorldChunkManager()
                .loadBlockGeneratorData(this.biomesForGeneration, p_73154_1_ * 16, p_73154_2_ * 16, 16, 16);
        for (int i = 0; i < this.biomesForGeneration.length; i++) {
            BiomeGenBase biomeGenBase = this.biomesForGeneration[i];
            if (biomeGenBase.biomeID == BiomeGenBase.mushroomIsland.biomeID) {
                this.biomesForGeneration[i] = BiomeGenBase.taiga;
            } else if (biomeGenBase.biomeID == BiomeGenBase.mushroomIslandShore.biomeID) {
                this.biomesForGeneration[i] = BiomeGenBase.stoneBeach;
            }
            if (LoaderReference.Thaumcraft) {
                if (ThaumcraftHandler.isTaintBiome(biomeGenBase.biomeID))
                    this.biomesForGeneration[i] = BiomeGenBase.taiga;
                else if (ConfigHandler.disableMagicalForest
                        && ThaumcraftHandler.isMagicalForestBiome(biomeGenBase.biomeID))
                    this.biomesForGeneration[i] = BiomeGenBase.birchForest;
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

        MinecraftForge.EVENT_BUS.post(
                new PopulateChunkEvent.Pre(p_73153_1_, this.worldObj, this.rand, p_73153_2_, p_73153_3_, false));

        int x1;
        int y1;
        int z1;
        if (biomegenbase != BiomeGenBase.desert
                && biomegenbase != BiomeGenBase.desertHills
                && TerrainGen.populate(p_73153_1_, this.worldObj, this.rand, p_73153_2_, p_73153_3_, false, LAKE)) {
            x1 = k + this.rand.nextInt(16) + 8;
            y1 = this.rand.nextInt(256);
            z1 = l + this.rand.nextInt(16) + 8;
            int rni = this.rand.nextInt(8);
            if (rni == 0) (new WorldGenLakes(Blocks.ice)).generate(this.worldObj, this.rand, x1, y1, z1);
            else if (rni == 4) (new WorldGenLakes(Blocks.water)).generate(this.worldObj, this.rand, x1, y1, z1);
        }
        if (biomegenbase != BiomeGenBase.ocean
                && biomegenbase != BiomeGenBase.deepOcean
                && biomegenbase != BiomeGenBase.river
                && biomegenbase != BiomeGenBase.frozenOcean
                && biomegenbase != BiomeGenBase.frozenRiver
                && this.rand.nextInt(ConfigHandler.ross128bRuinChance) == 0) {
            x1 = k + this.rand.nextInt(16) + 3;
            y1 = this.rand.nextInt(256);
            z1 = l + this.rand.nextInt(16) + 3;
            this.ruinsBase.generate(this.worldObj, this.rand, x1, y1, z1);
        }

        biomegenbase.decorate(this.worldObj, this.rand, k, l);

        k += 8;
        l += 8;

        boolean doGen = TerrainGen.populate(p_73153_1_, this.worldObj, this.rand, p_73153_2_, p_73153_3_, false, ICE);
        for (x1 = 0; doGen && x1 < 16; ++x1) {
            for (y1 = 0; y1 < 16; ++y1) {
                z1 = this.worldObj.getPrecipitationHeight(k + x1, l + y1);

                if (this.worldObj.isBlockFreezable(x1 + k, z1 - 1, y1 + l)) {
                    this.worldObj.setBlock(x1 + k, z1 - 1, y1 + l, Blocks.ice, 0, 2);
                }

                if (this.worldObj.func_147478_e(x1 + k, z1, y1 + l, true)) {
                    this.worldObj.setBlock(x1 + k, z1, y1 + l, Blocks.snow_layer, 0, 2);
                }
            }
        }

        BWOreGen.generate(this.rand, p_73153_2_, p_73153_3_, this.worldObj, this, this);
        MinecraftForge.EVENT_BUS.post(
                new PopulateChunkEvent.Post(p_73153_1_, this.worldObj, this.rand, p_73153_2_, p_73153_3_, false));

        BlockFalling.fallInstantly = false;
    }

    @Override
    public void recreateStructures(int p_82695_1_, int p_82695_2_) {}

    @Override
    public void replaceBlocksForBiome(
            int p_147422_1_, int p_147422_2_, Block[] blocks, byte[] metas, BiomeGenBase[] p_147422_5_) {
        super.replaceBlocksForBiome(p_147422_1_, p_147422_2_, blocks, metas, p_147422_5_);
        for (int i = 0; i < blocks.length; i++) {
            if (blocks[i] == Blocks.grass) {
                blocks[i] = Blocks.dirt;
                metas[i] = 2;
            }
        }
    }
}
