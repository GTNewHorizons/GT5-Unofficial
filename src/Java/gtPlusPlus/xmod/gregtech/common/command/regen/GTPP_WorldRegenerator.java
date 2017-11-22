package gtPlusPlus.xmod.gregtech.common.command.regen;

import java.util.HashSet;
import java.util.Random;

import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.objects.XSTR;
import gregtech.api.util.GT_Log;
import gregtech.api.world.GT_Worldgen;
import gregtech.common.GT_Worldgen_GT_Ore_Layer;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

public class GTPP_WorldRegenerator implements IWorldGenerator {
	private static int mEndAsteroidProbability = 300;
	private static int mGCAsteroidProbability = 50;
	private static int mSize = 100;
	private static int endMinSize = 50;
	private static int endMaxSize = 200;
	private static int gcMinSize = 100;
	private static int gcMaxSize = 400;
	private static boolean endAsteroids = true;
	private static boolean gcAsteroids = true;


	public GTPP_WorldRegenerator() {
		GameRegistry.registerWorldGenerator(this, 1073741823);
	}

	@Override
	public synchronized void generate(final Random aRandom, final int aX, final int aZ, final World aWorld, final IChunkProvider aChunkGenerator, final IChunkProvider aChunkProvider) {
		int tempDimensionId = aWorld.provider.dimensionId;
		if ((tempDimensionId != -1) && (tempDimensionId != 1) && !aChunkGenerator.getClass().getName().contains("galacticraft")) {
			tempDimensionId = 0;
		}
		new WorldGenContainer(aX * 16, aZ * 16, tempDimensionId, aWorld, aChunkGenerator, aChunkProvider, aWorld.getBiomeGenForCoords((aX * 16) + 8, (aZ * 16) + 8).biomeName).run();
	}

	public static class WorldGenContainer implements Runnable {
		public int mX;
		public int mZ;
		public final int mDimensionType;
		public final World mWorld;
		public final IChunkProvider mChunkGenerator;
		public final IChunkProvider mChunkProvider;
		public final String mBiome;
		public static HashSet<ChunkCoordIntPair> mGenerated = new HashSet<>(2000);

		public WorldGenContainer(final int aX, final int aZ, final int aDimensionType, final World aWorld, final IChunkProvider aChunkGenerator, final IChunkProvider aChunkProvider, final String aBiome) {
			this.mX = aX;
			this.mZ = aZ;
			this.mDimensionType = aDimensionType;
			this.mWorld = aWorld;
			this.mChunkGenerator = aChunkGenerator;
			this.mChunkProvider = aChunkProvider;
			this.mBiome = aBiome;
		}

		//returns a coordinate of a center chunk of 3x3 square; the argument belongs to this square
		public int getVeinCenterCoordinate(int c) {
			c += c < 0 ? 1 : 3;
			return c - (c % 3) - 2;
		}

		public boolean surroundingChunksLoaded(final int xCenter, final int zCenter) {
			return this.mWorld.checkChunksExist(xCenter - 16, 0, zCenter - 16, xCenter + 16, 0, zCenter + 16);
		}

		public Random getRandom(final int xChunk, final int zChunk) {
			final long worldSeed = this.mWorld.getSeed();
			final Random fmlRandom = new Random(worldSeed);
			final long xSeed = fmlRandom.nextLong() >> (2 + 1L);
			final long zSeed = fmlRandom.nextLong() >> (2 + 1L);
			final long chunkSeed = ((xSeed * xChunk) + (zSeed * zChunk)) ^ worldSeed;
			fmlRandom.setSeed(chunkSeed);
			return new XSTR(fmlRandom.nextInt());
		}

		@Override
		public void run() {
			int xCenter = this.getVeinCenterCoordinate(this.mX >> 4);
			int zCenter = this.getVeinCenterCoordinate(this.mZ >> 4);
			final Random random = this.getRandom(xCenter, zCenter);
			xCenter <<= 4;
			zCenter <<= 4;
			final ChunkCoordIntPair centerChunk = new ChunkCoordIntPair(xCenter, zCenter);
			if (!mGenerated.contains(centerChunk) && this.surroundingChunksLoaded(xCenter, zCenter)) {
				mGenerated.add(centerChunk);
				if ((GT_Worldgen_GT_Ore_Layer.sWeight > 0) && (GT_Worldgen_GT_Ore_Layer.sList.size() > 0)) {
					boolean temp = true;
					int tRandomWeight;
					for (int i = 0; (i < 256) && (temp); i++) {
						tRandomWeight = random.nextInt(GT_Worldgen_GT_Ore_Layer.sWeight);
						for (final GT_Worldgen tWorldGen : GT_Worldgen_GT_Ore_Layer.sList) {
							tRandomWeight -= ((GT_Worldgen_GT_Ore_Layer) tWorldGen).mWeight;
							if (tRandomWeight <= 0) {
								try {
									if (tWorldGen.executeWorldgen(this.mWorld, random, this.mBiome, this.mDimensionType, xCenter, zCenter, this.mChunkGenerator, this.mChunkProvider)) {
										temp = false;
									}
									break;
								} catch (final Throwable e) {
									e.printStackTrace(GT_Log.err);
								}
							}
						}
					}
				}
				int i = 0;
				for (int tX = xCenter - 16; i < 3; tX += 16) {
					int j = 0;
					for (int tZ = zCenter - 16; j < 3; tZ += 16) {
						try {
							for (final GT_Worldgen tWorldGen : GregTech_API.sWorldgenList) {
								tWorldGen.executeWorldgen(this.mWorld, random, this.mBiome, this.mDimensionType, tX, tZ, this.mChunkGenerator, this.mChunkProvider);
							}
						} catch (final Throwable e) {
							e.printStackTrace(GT_Log.err);
						}
						j++;
					}
					i++;
				}
			}
			final Chunk tChunk = this.mWorld.getChunkFromBlockCoords(this.mX, this.mZ);
			if (tChunk != null) {
				tChunk.isModified = true;
			}
		}
	}
}