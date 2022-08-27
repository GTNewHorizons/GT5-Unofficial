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

package com.github.bartimaeusnek.bartworks.system.oregen;

import cpw.mods.fml.common.IWorldGenerator;
import gregtech.api.objects.XSTR;
import gregtech.api.util.GT_Log;
import java.util.HashSet;
import java.util.Random;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

/**
 * Original GT File Stripped and adjusted to work with this mod
 */
public class BW_WordGenerator implements IWorldGenerator {

    public BW_WordGenerator() {
        // GT_NH Override... wont be actually registered to force its generation directly in the ChunkProvider
        // GameRegistry.registerWorldGenerator(this, 1073741823);
    }

    public synchronized void generate(
            Random aRandom,
            int aX,
            int aZ,
            World aWorld,
            IChunkProvider aChunkGenerator,
            IChunkProvider aChunkProvider) {
        new BW_WordGenerator.WorldGenContainer(
                        aX * 16, aZ * 16, aWorld.provider.dimensionId, aWorld, aChunkGenerator, aChunkProvider)
                .run();
    }

    public static class WorldGenContainer implements Runnable {
        public static HashSet<ChunkCoordIntPair> mGenerated = new HashSet<>(2000);
        public final int mDimensionType;
        public final World mWorld;
        public final IChunkProvider mChunkGenerator;
        public final IChunkProvider mChunkProvider;
        public int mX;
        public int mZ;

        public WorldGenContainer(
                int aX,
                int aZ,
                int aDimensionType,
                World aWorld,
                IChunkProvider aChunkGenerator,
                IChunkProvider aChunkProvider) {
            this.mX = aX;
            this.mZ = aZ;
            this.mDimensionType = aDimensionType;
            this.mWorld = aWorld;
            this.mChunkGenerator = aChunkGenerator;
            this.mChunkProvider = aChunkProvider;
        }

        // returns a coordinate of a center chunk of 3x3 square; the argument belongs to this square
        public int getVeinCenterCoordinate(int c) {
            c += c < 0 ? 1 : 3;
            return c - c % 3 - 2;
        }

        public boolean surroundingChunksLoaded(int xCenter, int zCenter) {
            return this.mWorld.checkChunksExist(xCenter - 16, 0, zCenter - 16, xCenter + 16, 0, zCenter + 16);
        }

        public XSTR getRandom(int xChunk, int zChunk) {
            long worldSeed = this.mWorld.getSeed();
            XSTR fmlRandom = new XSTR(worldSeed);
            long xSeed = fmlRandom.nextLong() >> 2 + 1L;
            long zSeed = fmlRandom.nextLong() >> 2 + 1L;
            long chunkSeed = (xSeed * xChunk + zSeed * zChunk) ^ worldSeed;
            fmlRandom.setSeed(chunkSeed);
            return new XSTR(fmlRandom.nextInt());
        }

        public void run() {
            int xCenter = this.getVeinCenterCoordinate(this.mX >> 4);
            int zCenter = this.getVeinCenterCoordinate(this.mZ >> 4);
            Random random = this.getRandom(xCenter, zCenter);
            xCenter <<= 4;
            zCenter <<= 4;
            ChunkCoordIntPair centerChunk = new ChunkCoordIntPair(xCenter, zCenter);
            if (!BW_WordGenerator.WorldGenContainer.mGenerated.contains(centerChunk)
                    && this.surroundingChunksLoaded(xCenter, zCenter)) {
                BW_WordGenerator.WorldGenContainer.mGenerated.add(centerChunk);
                if ((BW_OreLayer.sWeight > 0) && (BW_OreLayer.sList.size() > 0)) {
                    boolean temp = true;
                    int tRandomWeight;
                    for (int i = 0; (i < 256) && (temp); i++) {
                        tRandomWeight = random.nextInt(BW_OreLayer.sWeight);
                        for (BW_OreLayer tWorldGen : BW_OreLayer.sList) {
                            if (!tWorldGen.isGenerationAllowed(this.mWorld, this.mDimensionType, this.mDimensionType))
                                continue;
                            tRandomWeight -= tWorldGen.mWeight;
                            if (tRandomWeight <= 0) {
                                try {
                                    boolean placed;
                                    int attempts = 0;
                                    do {
                                        placed = tWorldGen.executeWorldgen(
                                                this.mWorld,
                                                random,
                                                "",
                                                this.mDimensionType,
                                                xCenter,
                                                zCenter,
                                                this.mChunkGenerator,
                                                this.mChunkProvider);
                                        ++attempts;
                                    } while ((!placed) && attempts < 25);
                                    temp = false;
                                    break;
                                } catch (Throwable e) {
                                    e.printStackTrace(GT_Log.err);
                                }
                            }
                        }
                    }
                }
            }
            Chunk tChunk = this.mWorld.getChunkFromBlockCoords(this.mX, this.mZ);
            if (tChunk != null) {
                tChunk.isModified = true;
            }
        }
    }
}
