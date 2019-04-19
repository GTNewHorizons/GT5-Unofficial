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

package com.github.bartimaeusnek.crossmod.galacticraft.planets.ross128ba;

import gregtech.api.objects.XSTR;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.MapGenBaseMeta;
import micdoodle8.mods.galacticraft.core.world.gen.BiomeGenBaseMoon;
import micdoodle8.mods.galacticraft.core.world.gen.ChunkProviderMoon;
import micdoodle8.mods.galacticraft.core.world.gen.MapGenCavesMoon;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

import java.util.Arrays;

public class ChunkProviderRoss128ba extends ChunkProviderMoon {

    private XSTR rand = new XSTR();
    private World worldObj;
    private BiomeGenBase[] biomesForGeneration;
    private MapGenBaseMeta caveGenerator;

    public ChunkProviderRoss128ba(World world, long seed, boolean mapFeaturesEnabled) {
        super(world, seed, mapFeaturesEnabled);
        this.biomesForGeneration = new BiomeGenBase[]{BiomeGenBaseMoon.moonFlat};
        this.caveGenerator = new MapGenCavesMoon();
        this.worldObj=world;
    }

    public Chunk provideChunk(int cx, int cz) {
        this.rand.setSeed((long)cx * 341873128712L + (long)cz * 132897987541L);
        Block[] ids = new Block[65536];
        byte[] meta = new byte[65536];
        Arrays.fill(ids, Blocks.air);
        this.generateTerrain(cx, cz, ids, meta);
        this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, cx * 16, cz * 16, 16, 16);
        this.createCraters(cx, cz, ids, meta);
        this.replaceBlocksForBiome(cx, cz, ids, meta, this.biomesForGeneration);
        this.caveGenerator.generate(this, this.worldObj, cx, cz, ids, meta);
        Chunk Chunk = new Chunk(this.worldObj, ids, meta, cx, cz);
        Chunk.generateSkylightMap();
        return Chunk;
    }
}
