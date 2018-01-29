package gtPlusPlus.core.world.darkworld.gen.gt;

import java.util.*;

import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.util.GT_Log;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.XSTR;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.world.darkworld.Dimension_DarkWorld;
import gtPlusPlus.xmod.gregtech.HANDLER_GT;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class WorldGen_GT_Base implements IWorldGenerator {

	/**
	 * Class Variables
	 */

	/**
	 * Control percentage of filled 3x3 chunks. Lower number means less oreveins
	 * spawn
	 */
	public static int oreveinPercentage;
	/**
	 * Control number of attempts to find a valid orevein. Generally this
	 * maximum limit isn't hit, selecting a vein is cheap
	 */
	public static int oreveinAttempts;
	/**
	 * Control number of attempts to place a valid orevein. If a vein wasn't
	 * placed due to height restrictions, completely in the water, etc, another
	 * attempt is tried.
	 */
	public static int oreveinMaxPlacementAttempts;
	/**
	 * Debug parameter for world generation. Tracks chunks added/removed from
	 * run queue.
	 */
	public static boolean debugWorldGen = false;
	/**
	 * Try re-implement Richard Hendrick's Chunk by Chunk Ore Generation from
	 * his GT5u fork.
	 */

	public static List<Runnable> mList = new ArrayList<Runnable>();
	public static HashSet<Long> ProcChunks = new HashSet<Long>();
	// This is probably not going to work. Trying to create a fake orevein to
	// put into hashtable when there will be no ores in a vein.
	public static WorldGen_GT_Ore_Layer noOresInVein = new WorldGen_GT_Ore_Layer("vein0", 0, 255, 0, 0,
			0, ELEMENT.getInstance().ALUMINIUM, ELEMENT.getInstance().ALUMINIUM, ELEMENT.getInstance().ALUMINIUM,	ELEMENT.getInstance().ALUMINIUM);
	
	public static Hashtable<Long, WorldGen_GT_Ore_Layer> validOreveins = new Hashtable<Long, WorldGen_GT_Ore_Layer>(
			1024);
	
	public boolean mIsGenerating = false;
	public static final Object listLock = new Object();
	// private static boolean gcAsteroids = true;

	public WorldGen_GT_Base() {
		if (debugWorldGen) {
			GT_Log.out.println("GTPP_Worldgenerator created");
		}
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator,
			IChunkProvider chunkProvider) {
		if (world.provider.dimensionId == Dimension_DarkWorld.DIMID) {
			generateSafely(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
		}
	}

	public synchronized void generateSafely(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		int xDim = Dimension_DarkWorld.DIMID;
		switch (world.provider.dimensionId) {
			case -1: // Nether
				// generateNether(world, random, chunkX * 16, chunkZ * 16);
				break;
			case 0: // Overworld
				// generateSurface(world, random, chunkX * 16, chunkZ * 16);
				break;
			case 1: // End
				// generateEnd(world, random, chunkX * 16, chunkZ * 16);
				break;
			default: // Any other dimension
				if (world.provider.dimensionId != xDim) {
					break;
				}
				else {
					generateDarkWorld(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
					break;
				}
		}
	}

	private synchronized void generateDarkWorld(Random aRandom, int aX, int aZ, World aWorld,
			IChunkProvider aChunkGenerator, IChunkProvider aChunkProvider) {
		Logger.WORLD("Trying to Generate Dimension.");
		synchronized (listLock) {
			Logger.WORLD("Locked List addition.");
			if (WorldGen_GT_Base.mList.add(new WorldGenContainer(new XSTR(Math.abs(aRandom.nextInt()) + 1), aX, aZ,
					Dimension_DarkWorld.DIMID,
					aWorld, aChunkGenerator, aChunkProvider,
					aWorld.getBiomeGenForCoords(aX * 16 + 8, aZ * 16 + 8).biomeName))){
				Logger.WORLD("Locked List addition. Success.");				
			}
			else {
				Logger.WORLD("Locked List addition. Fail.");				
			}
			if (debugWorldGen)
				GT_Log.out.println("ADD WorldSeed:" + aWorld.getSeed() + " DimId" + aWorld.provider.dimensionId
						+ " chunk x:" + aX + " z:" + aZ + " SIZE: " + WorldGen_GT_Base.mList.size());
		}

		if (!this.mIsGenerating) {
			Logger.WORLD("Is not generating.");
			this.mIsGenerating = true;
			Logger.WORLD("Setting Generation to true.");
			int mList_sS = WorldGen_GT_Base.mList.size();
			mList_sS = Math.min(mList_sS, 3); // Run a maximum of 3 chunks at a
												// time through worldgen. Extra
												// chunks get done later.
			for (int i = 0; i < mList_sS; i++) {
				WorldGenContainer toRun = (WorldGenContainer) WorldGen_GT_Base.mList.get(0);
				if (debugWorldGen)
					GT_Log.out.println("RUN WorldSeed:" + aWorld.getSeed() + " DimId" + aWorld.provider.dimensionId
							+ " chunk x:" + toRun.mX + " z:" + toRun.mZ + " SIZE: " + WorldGen_GT_Base.mList.size()
							+ " i: " + i);
				synchronized (listLock) {
					Logger.WORLD("Locked List Removal.");
					WorldGen_GT_Base.mList.remove(0);
				}
				toRun.run();
			}
			this.mIsGenerating = false;
			Logger.WORLD("Is Generating now set to false..");
		}
	}

	public void generateOre(Block block, World world, Random random, int chunk_x, int chunk_z, int maxX, int maxZ,
			int maxVeinSize, int chancesToSpawn, int minY, int maxY, Block generateIn) {
		int heightRange = maxY - minY;
		WorldGenMinable worldgenminable = new WorldGenMinable(block, maxVeinSize, generateIn);
		for (int k1 = 0; k1 < chancesToSpawn; ++k1) {
			int xrand = random.nextInt(16);
			int yrand = random.nextInt(heightRange) + minY;
			int zrand = random.nextInt(16);
			worldgenminable.generate(world, random, chunk_x + xrand, yrand, chunk_z + zrand);
		}
	}

	public static class WorldGenContainer implements Runnable {
		public final Random mRandom;
		public final int mX;
		public final int mZ;
		public final int mDimensionType;
		public final World mWorld;
		public final IChunkProvider mChunkGenerator;
		public final IChunkProvider mChunkProvider;
		public final String mBiome;

		// Local class to track which orevein seeds must be checked when doing
		// chunkified worldgen
		class NearbySeeds {
			public int mX;
			public int mZ;

			NearbySeeds(int x, int z) {
				this.mX = x;
				this.mZ = z;
			}
		};

		public static ArrayList<NearbySeeds> seedList = new ArrayList<NearbySeeds>();

		// aX and aZ are now the by-chunk X and Z for the chunk of interest
		public WorldGenContainer(Random aRandom, int aX, int aZ, int aDimensionType, World aWorld,
				IChunkProvider aChunkGenerator, IChunkProvider aChunkProvider, String aBiome) {
			this.mRandom = aRandom;
			this.mX = aX;
			this.mZ = aZ;
			this.mDimensionType = aDimensionType;
			this.mWorld = aWorld;
			this.mChunkGenerator = aChunkGenerator;
			this.mChunkProvider = aChunkProvider;
			this.mBiome = aBiome;
		}

		public void worldGenFindVein(int oreseedX, int oreseedZ) {
			// Explanation of oreveinseed implementation.
			// (long)this.mWorld.getSeed()<<16) Deep Dark does two oregen
			// passes, one with getSeed set to +1 the original world seed. This
			// pushes that +1 off the low bits of oreseedZ, so that the hashes
			// are far apart for the two passes.
			// ((this.mWorld.provider.dimensionId & 0xffL)<<56) Puts the
			// dimension in the top bits of the hash, to make sure to get unique
			// hashes per dimension
			// ((long)oreseedX & 0x000000000fffffffL) << 28) Puts the chunk X in
			// the bits 29-55. Cuts off the top few bits of the chunk so we have
			// bits for dimension.
			// ( (long)oreseedZ & 0x000000000fffffffL )) Puts the chunk Z in the
			// bits 0-27. Cuts off the top few bits of the chunk so we have bits
			// for dimension.
			long oreveinSeed = (this.mWorld.getSeed() << 16) ^ ((this.mWorld.provider.dimensionId & 0xffL) << 56
					| ((oreseedX & 0x000000000fffffffL) << 28) | (oreseedZ & 0x000000000fffffffL)); // Use
																									// an
																									// RNG
																									// that
																									// is
																									// identical
																									// every
																									// time
																									// it
																									// is
																									// called
																									// for
																									// this
																									// oreseed.
			XSTR oreveinRNG = new XSTR(oreveinSeed);
			int oreveinPercentageRoll = oreveinRNG.nextInt(100); // Roll the
																	// dice, see
																	// if we get
																	// an
																	// orevein
																	// here at
																	// all
			int noOrePlacedCount = 0;
			String tDimensionName = "";
			if (debugWorldGen) {
				tDimensionName = this.mWorld.provider.getDimensionName();
			}

			if (debugWorldGen){
				GT_Log.out.println(" Finding oreveins for oreveinSeed=" + oreveinSeed + " mX=" + this.mX + " mZ="
						+ this.mZ + " oreseedX=" + oreseedX + " oreseedZ=" + oreseedZ + " worldSeed="
						+ this.mWorld.getSeed());
			}

			Logger.INFO("[World Generation Debug] !validOreveins.containsKey(oreveinSeed) | oreveinSeed: "+oreveinSeed);
			// Search for a valid orevein for this dimension
			if (!validOreveins.containsKey(oreveinSeed)) {
				
				
				Logger.INFO("[World Generation Debug] oreveinPercentageRoll < oreveinPercentage? "+((oreveinPercentageRoll < oreveinPercentage)));
				Logger.INFO("[World Generation Debug] WorldGen_GT_Ore_Layer.sWeight > 0? "+(WorldGen_GT_Ore_Layer.sWeight > 0));
				Logger.INFO("[World Generation Debug] WorldGen_GT_Ore_Layer.sList.size() > 0? "+(WorldGen_GT_Ore_Layer.sList.size() > 0));
				if ((oreveinPercentageRoll < oreveinPercentage) && (WorldGen_GT_Ore_Layer.sWeight > 0)
						&& (WorldGen_GT_Ore_Layer.sList.size() > 0)) {
					int placementAttempts = 0;
					boolean oreveinFound = false;
					int i;
					for (i = 0; (i < oreveinAttempts) && (!oreveinFound)
							&& (placementAttempts < oreveinMaxPlacementAttempts); i++) {
						Logger.INFO("[World Generation Debug] i: "+i);
						Logger.INFO("[World Generation Debug] placementAttempts: "+placementAttempts);
						Logger.INFO("[World Generation Debug] oreveinAttempts: "+oreveinAttempts);
						Logger.INFO("[World Generation Debug] (placementAttempts < oreveinMaxPlacementAttempts): "+(placementAttempts < oreveinMaxPlacementAttempts));
						Logger.INFO("[World Generation Debug] oreveinFound: "+oreveinFound);
						int tRandomWeight = oreveinRNG.nextInt(WorldGen_GT_Ore_Layer.sWeight);
						for (WorldGen_GT_Ore_Layer tWorldGen : WorldGen_GT_Ore_Layer.sList) {
							Logger.INFO("[World Generation Debug] Iterating sList - Size: "+WorldGen_GT_Ore_Layer.sList.size());
							tRandomWeight -= (tWorldGen).mWeight;
							if (tRandomWeight <= 0) {
								try {
									// Adjust the seed so that this layer has a
									// series of unique random numbers.
									// Otherwise multiple attempts at this same
									// oreseed will get the same offset and X/Z
									// values. If an orevein failed, any orevein
									// with the
									// same minimum heights would fail as well.
									// This prevents that, giving each orevein a
									// unique height each pass through here.
									int placementResult = tWorldGen.executeWorldgenChunkified(this.mWorld,
											new XSTR(oreveinSeed ^ (Block.getIdFromBlock(tWorldGen.mPrimaryMeta))), this.mBiome,
											this.mDimensionType, this.mX * 16, this.mZ * 16, oreseedX * 16,
											oreseedZ * 16, this.mChunkGenerator, this.mChunkProvider);
									switch (placementResult) {
										case WorldGen_GT_Ore_Layer.ORE_PLACED:
											if (debugWorldGen)
												GT_Log.out.println(" Added oreveinSeed=" + oreveinSeed
														+ " tries at oremix=" + i + " placementAttempts="
														+ placementAttempts + " dimensionName=" + tDimensionName);
											validOreveins.put(oreveinSeed, tWorldGen);
											oreveinFound = true;
											Logger.INFO("[World Generation Debug] ORE_PLACED");
											break;
										case WorldGen_GT_Ore_Layer.NO_ORE_IN_BOTTOM_LAYER:
											placementAttempts++;
											Logger.INFO("[World Generation Debug] NO_ORE_IN_BOTTOM_LAYER | Attempts: "+placementAttempts);
											// SHould do retry in this case
											// until out of chances
											break;
										case WorldGen_GT_Ore_Layer.NO_OVERLAP:
											// Orevein didn't reach this chunk,
											// can't add it yet to the hash
											Logger.INFO("[World Generation Debug] NO_OVERLAP");
											break;
									}
									break; // Try the next orevein
								}
								catch (Throwable e) {
									if (debugWorldGen)
										GT_Log.out.println("Exception occurred on oreVein" + tWorldGen + " oreveinSeed="
												+ oreveinSeed + " mX=" + this.mX + " mZ=" + this.mZ + " oreseedX="
												+ oreseedX + " oreseedZ=" + oreseedZ);
									e.printStackTrace(GT_Log.err);
								}
							}
						}
					}
					// Only add an empty orevein if are unable to place a vein
					// at the oreseed chunk.
					if ((!oreveinFound) && (this.mX == oreseedX) && (this.mZ == oreseedZ)) {
						if (debugWorldGen)
							GT_Log.out.println(" Empty oreveinSeed=" + oreveinSeed + " mX=" + this.mX + " mZ=" + this.mZ
									+ " oreseedX=" + oreseedX + " oreseedZ=" + oreseedZ + " tries at oremix=" + i
									+ " placementAttempts=" + placementAttempts + " dimensionName=" + tDimensionName);
						validOreveins.put(oreveinSeed, noOresInVein);
					}
				}
				else if (oreveinPercentageRoll >= oreveinPercentage) {
					if (debugWorldGen)
						GT_Log.out.println(" Skipped oreveinSeed=" + oreveinSeed + " mX=" + this.mX + " mZ=" + this.mZ
								+ " oreseedX=" + oreseedX + " oreseedZ=" + oreseedZ + " RNG=" + oreveinPercentageRoll
								+ " %=" + oreveinPercentage + " dimensionName=" + tDimensionName);
					validOreveins.put(oreveinSeed, noOresInVein);
				}
			}
			else {
				// oreseed is located in the previously processed table
				if (debugWorldGen)
					GT_Log.out.print(" Valid oreveinSeed=" + oreveinSeed + " validOreveins.size()="
							+ validOreveins.size() + " ");
				WorldGen_GT_Ore_Layer tWorldGen = validOreveins.get(oreveinSeed);
				oreveinRNG.setSeed(oreveinSeed ^ (Block.getIdFromBlock(tWorldGen.mPrimaryMeta))); // Reset
																			// RNG
																			// to
																			// only
																			// be
																			// based
																			// on
																			// oreseed
																			// X/Z
																			// and
																			// type
																			// of
																			// vein
				int placementResult = tWorldGen.executeWorldgenChunkified(this.mWorld, oreveinRNG, this.mBiome,
						this.mDimensionType, this.mX * 16, this.mZ * 16, oreseedX * 16, oreseedZ * 16,
						this.mChunkGenerator, this.mChunkProvider);
				switch (placementResult) {
					case WorldGen_GT_Ore_Layer.NO_ORE_IN_BOTTOM_LAYER:
						if (debugWorldGen)
							GT_Log.out.println(" No ore in bottom layer");
						break;
					case WorldGen_GT_Ore_Layer.NO_OVERLAP:
						if (debugWorldGen)
							GT_Log.out.println(" No overlap");
						break;
				}
			}
		}

		@Override
		public void run() {
			long startTime = System.nanoTime();
			int oreveinMaxSize;

			// Determine bounding box on how far out to check for oreveins
			// affecting this chunk
			// For now, manually reducing oreveinMaxSize when not in the
			// Underdark for performance
			if (this.mWorld.provider.getDimensionName().equals("Underdark")) {
				oreveinMaxSize = 24; // Leave Deep Dark/Underdark max oregen at
										// 32, instead of 64
			}
			else {
				oreveinMaxSize = 48;
			}

			int wXbox = this.mX - (oreveinMaxSize / 16);
			int eXbox = this.mX + (oreveinMaxSize / 16 + 1); // Need to add 1
																// since it is
																// compared
																// using a <
			int nZbox = this.mZ - (oreveinMaxSize / 16);
			int sZbox = this.mZ + (oreveinMaxSize / 16 + 1);

			// Search for orevein seeds and add to the list;
			for (int x = wXbox; x < eXbox; x++) {
				for (int z = nZbox; z < sZbox; z++) {
					// Determine if this X/Z is an orevein seed
					if (((Math.abs(x) % 3) == 1) && ((Math.abs(z) % 3) == 1)) {
						if (debugWorldGen)
							GT_Log.out.println("Adding seed x=" + x + " z=" + z);
						seedList.add(new NearbySeeds(x, z));
					}
				}
			}

			// Now process each oreseed vs this requested chunk
			for (; seedList.size() != 0; seedList.remove(0)) {
				if (debugWorldGen)
					GT_Log.out.println("Processing seed x=" + seedList.get(0).mX + " z=" + seedList.get(0).mZ);
				worldGenFindVein(seedList.get(0).mX, seedList.get(0).mZ);
			}

			long oregenTime = System.nanoTime();

			// Do leftover worldgen for this chunk (GT_Stones and GT_small_ores)
			try {
				for (WorldGen_GT tWorldGen : HANDLER_GT.sWorldgenList) {
					/*
					 * if (debugWorldGen) GT_Log.out.println(
					 * "tWorldGen.mWorldGenName="+tWorldGen.mWorldGenName );
					 */
					tWorldGen.executeWorldgen(this.mWorld, this.mRandom, this.mBiome, this.mDimensionType, this.mX * 16,
							this.mZ * 16, this.mChunkGenerator, this.mChunkProvider);
				}
			}
			catch (Throwable e) {
				e.printStackTrace(GT_Log.err);
			}

			long leftOverTime = System.nanoTime();

			Chunk tChunk = this.mWorld.getChunkFromBlockCoords(this.mX, this.mZ);
			if (tChunk != null) {
				tChunk.isModified = true;
			}
			long endTime = System.nanoTime();
			long duration = (endTime - startTime);
			if (debugWorldGen) {
				GT_Log.out.println(" Oregen took " + (oregenTime - startTime) + " Leftover gen took "
						+ (leftOverTime - oregenTime) + " Worldgen took " + duration + " nanoseconds");
			}
		}
	}

}
