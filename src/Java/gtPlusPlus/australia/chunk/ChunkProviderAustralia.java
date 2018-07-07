package gtPlusPlus.australia.chunk;

import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ANIMALS;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cpw.mods.fml.common.eventhandler.Event.Result;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.australia.gen.map.MapGenLargeRavine;
import gtPlusPlus.australia.gen.map.MapGenVillageLogging;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.*;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenVillage;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.ChunkProviderEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

public class ChunkProviderAustralia extends ChunkProviderGenerate implements IChunkProvider {
	private Random rand;
	private NoiseGeneratorOctaves noiseGen1;
	private NoiseGeneratorOctaves noiseGen2;
	private NoiseGeneratorOctaves noiseGen3;
	private NoiseGeneratorPerlin noiseGen4;

	/**
	 * A NoiseGeneratorOctaves used in generating terrain
	 */
	public NoiseGeneratorOctaves noiseGen5;
	/**
	 * A NoiseGeneratorOctaves used in generating terrain
	 */
	public NoiseGeneratorOctaves noiseGen6;
	public NoiseGeneratorOctaves mobSpawnerNoise;
	/**
	 * Reference to the World object.
	 */
	private World worldObj;
	private WorldType worldType;
	private final double[] field_147434_q;
	private final float[] parabolicField;
	private double[] stoneNoise = new double[256];
	private MapGenBase caveGenerator = new MapGenCaves();
	/**
	 * Holds Stronghold Generator
	 */
	// private MapGenStronghold strongholdGenerator = new
	// MapGenStronghold();
	/**
	 * Holds Village Generator
	 */
	private MapGenVillageLogging villageGenerator = new MapGenVillageLogging();
	/**
	 * Holds Mineshaft Generator
	 */
	private MapGenMineshaft mineshaftGenerator = new MapGenMineshaft();
	private MapGenScatteredFeature scatteredFeatureGenerator = new MapGenScatteredFeature();
	/**
	 * Holds ravine generator
	 */
	private MapGenBase ravineGenerator = new MapGenLargeRavine();
	/**
	 * The biomes that are used to generate the chunk
	 */
	private BiomeGenBase[] biomesForGeneration;
	double[] doubleArray1;
	double[] doubleArray2;
	double[] doubleArray3;
	double[] doubleArray4;	

	int[][] field_73219_j = new int[32][32];
	Map map;

	//Some Init Field?
	{


		List<BiomeGenBase> y = new ArrayList<BiomeGenBase>();		
		for (Object r : MapGenVillage.villageSpawnBiomes.toArray()) {
			if (r instanceof BiomeGenBase) {
				y.add((BiomeGenBase) r);
			}
		}		
		for (BiomeGenBase h : y) {
			if (!MapGenVillage.villageSpawnBiomes.contains(h)) {
				if (h instanceof BiomeGenBase) {
					y.add(h);
				}
			}
		}
		if (!MapGenVillage.villageSpawnBiomes.toArray().equals(y.toArray())) {
			MapGenVillage.villageSpawnBiomes = y;
		}

		/*if (map == null) {
			map = FlatGeneratorInfo.createFlatGeneratorFromString("abcdefg12345678").getWorldFeatures();
		}*/

		/*if (map != null && map.containsKey("village")){
				Map map1 = (Map)map.get("village");
				if (!map1.containsKey("size"))
				{
					map1.put("size", "10");
				}
				villageGenerator = new MapGenExtendedVillage(map1);
				villageGenerator = (MapGenExtendedVillage) TerrainGen.getModdedMapGen(villageGenerator,
						net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.VILLAGE);
				Logger.INFO("Registered Valid Chunk Provider for Custom Villages.");
		}
		else {
			Logger.INFO("Failed to register Valid Chunk Provider for Custom Villages.");			
		}*/


		villageGenerator = (MapGenVillageLogging) TerrainGen.getModdedMapGen(villageGenerator,
				net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.VILLAGE);
		caveGenerator = TerrainGen.getModdedMapGen(caveGenerator,
				net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.CAVE);
		mineshaftGenerator = (MapGenMineshaft) TerrainGen.getModdedMapGen(mineshaftGenerator,
				net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.MINESHAFT);
		scatteredFeatureGenerator = (MapGenScatteredFeature) TerrainGen.getModdedMapGen(scatteredFeatureGenerator,
				net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.SCATTERED_FEATURE);
		ravineGenerator = TerrainGen.getModdedMapGen(ravineGenerator,
				net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.RAVINE);
	}

	public ChunkProviderAustralia(World par1World, long par2) {		
		super(par1World, par2, true);
		this.worldObj = par1World;
		this.worldType = par1World.getWorldInfo().getTerrainType();
		this.rand = new Random(par2);
		this.noiseGen1 = new NoiseGeneratorOctaves(this.rand, 16);
		this.noiseGen2 = new NoiseGeneratorOctaves(this.rand, 16);
		this.noiseGen3 = new NoiseGeneratorOctaves(this.rand, 8);
		this.noiseGen4 = new NoiseGeneratorPerlin(this.rand, 4);
		this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 10);
		this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 16);
		this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.rand, 8);
		this.field_147434_q = new double[2500];
		this.parabolicField = new float[25];
		for (int j = -2; j <= 2; j++) {
			for (int k = -2; k <= 2; k++)
			{
				float f = 10.0F / MathHelper.sqrt_float(j * j + k * k + 0.2F);
				this.parabolicField[(j + 2 + (k + 2) * 5)] = f;
			}
		}
		NoiseGenerator[] noiseGens = { this.noiseGen1, this.noiseGen2, this.noiseGen3, this.noiseGen4, this.noiseGen5, this.noiseGen6, this.mobSpawnerNoise };
		noiseGens = TerrainGen.getModdedNoiseGenerators(par1World, this.rand, noiseGens);
		this.noiseGen1 = ((NoiseGeneratorOctaves)noiseGens[0]);
		this.noiseGen2 = ((NoiseGeneratorOctaves)noiseGens[1]);
		this.noiseGen3 = ((NoiseGeneratorOctaves)noiseGens[2]);
		this.noiseGen4 = ((NoiseGeneratorPerlin)noiseGens[3]);
		this.noiseGen5 = ((NoiseGeneratorOctaves)noiseGens[4]);
		this.noiseGen6 = ((NoiseGeneratorOctaves)noiseGens[5]);
		this.mobSpawnerNoise = ((NoiseGeneratorOctaves)noiseGens[6]);
	}

	public void func_147424_a(int p_147424_1_, int p_147424_2_, Block[] p_147424_3_) {
		byte b0 = 63;
		this.biomesForGeneration = this.worldObj.getWorldChunkManager().getBiomesForGeneration(this.biomesForGeneration, p_147424_1_ * 4 - 2, p_147424_2_ * 4 - 2, 10, 10);

		func_147423_a(p_147424_1_ * 4, 0, p_147424_2_ * 4);
		for (int k = 0; k < 4; k++)
		{
			int l = k * 5;
			int i1 = (k + 1) * 5;
			for (int j1 = 0; j1 < 4; j1++)
			{
				int k1 = (l + j1) * 33;
				int l1 = (l + j1 + 1) * 33;
				int i2 = (i1 + j1) * 33;
				int j2 = (i1 + j1 + 1) * 33;
				for (int k2 = 0; k2 < 32; k2++)
				{
					double d0 = 0.125D;
					double d1 = this.field_147434_q[(k1 + k2)];
					double d2 = this.field_147434_q[(l1 + k2)];
					double d3 = this.field_147434_q[(i2 + k2)];
					double d4 = this.field_147434_q[(j2 + k2)];
					double d5 = (this.field_147434_q[(k1 + k2 + 1)] - d1) * d0;
					double d6 = (this.field_147434_q[(l1 + k2 + 1)] - d2) * d0;
					double d7 = (this.field_147434_q[(i2 + k2 + 1)] - d3) * d0;
					double d8 = (this.field_147434_q[(j2 + k2 + 1)] - d4) * d0;
					for (int l2 = 0; l2 < 8; l2++)
					{
						double d9 = 0.25D;
						double d10 = d1;
						double d11 = d2;
						double d12 = (d3 - d1) * d9;
						double d13 = (d4 - d2) * d9;
						for (int i3 = 0; i3 < 4; i3++)
						{
							int j3 = i3 + k * 4 << 12 | 0 + j1 * 4 << 8 | k2 * 8 + l2;
							short short1 = 256;
							j3 -= short1;
							double d14 = 0.25D;
							double d16 = (d11 - d10) * d14;
							double d15 = d10 - d16;
							for (int k3 = 0; k3 < 4; k3++) {
								if ((d15 += d16) > 0.0D) {
									p_147424_3_[(j3 += short1)] = Blocks.stone;
								} else if (k2 * 8 + l2 < b0) {
									p_147424_3_[(j3 += short1)] = Blocks.water;
								} else {
									p_147424_3_[(j3 += short1)] = null;
								}
							}
							d10 += d12;
							d11 += d13;
						}
						d1 += d5;
						d2 += d6;
						d3 += d7;
						d4 += d8;
					}
				}
			}
		}
	}

	public void replaceBlocksForBiome(int p_147422_1_, int p_147422_2_, Block[] p_147422_3_, byte[] p_147422_4_, BiomeGenBase[] p_147422_5_) {
		ChunkProviderEvent.ReplaceBiomeBlocks event = new ChunkProviderEvent.ReplaceBiomeBlocks(this, p_147422_1_, p_147422_2_, p_147422_3_, p_147422_5_);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.getResult() == Result.DENY) {
			return;
		}
		double d0 = 0.03125D;
		this.stoneNoise = this.noiseGen4.func_151599_a(this.stoneNoise, p_147422_1_ * 16, p_147422_2_ * 16, 16, 16, d0 * 2.0D, d0 * 2.0D, 1.0D);
		for (int k = 0; k < 16; k++) {
			for (int l = 0; l < 16; l++)
			{
				BiomeGenBase biomegenbase = p_147422_5_[(l + k * 16)];
				biomegenbase.genTerrainBlocks(this.worldObj, this.rand, p_147422_3_, p_147422_4_, p_147422_1_ * 16 + k, p_147422_2_ * 16 + l, this.stoneNoise[(l + k * 16)]);
			}
		}
	}

	/**
	 * loads or generates the chunk at the chunk location specified
	 */
	@Override
	public Chunk loadChunk(int par1, int par2) {
		return this.provideChunk(par1, par2);
	}

	/**
	 * Will return back a chunk, if it doesn't exist and its not a MP client it will generates all the blocks for the
	 * specified chunk from the map seed and chunk seed
	 */
	@Override
	public Chunk provideChunk(int x, int z)
	{
		rand.setSeed(x * 341873128712L + z * 132897987541L);
		Block[] ablock = new Block[65536];
		byte[] abyte = new byte[65536];
		generateTerrain(x, z, ablock);
		biomesForGeneration = worldObj.getWorldChunkManager().loadBlockGeneratorData(biomesForGeneration, x * 16, z * 16, 16, 16);
		replaceBlocksForBiome(x, z, ablock, abyte, biomesForGeneration);

		caveGenerator.func_151539_a(this, worldObj, x, z, ablock);
		caveGenerator.func_151539_a(this, worldObj, x, z, ablock);
		ravineGenerator.func_151539_a(this, worldObj, x, z, ablock);
		ravineGenerator.func_151539_a(this, worldObj, x, z, ablock);
		ravineGenerator.func_151539_a(this, worldObj, x, z, ablock);
		ravineGenerator.func_151539_a(this, worldObj, x, z, ablock);
		villageGenerator.func_151539_a(this, worldObj, x, z, ablock);
		scatteredFeatureGenerator.func_151539_a(this, worldObj, x, z, ablock);
		scatteredFeatureGenerator.func_151539_a(this, worldObj, x, z, ablock);
		scatteredFeatureGenerator.func_151539_a(this, worldObj, x, z, ablock);
		scatteredFeatureGenerator.func_151539_a(this, worldObj, x, z, ablock);
		scatteredFeatureGenerator.func_151539_a(this, worldObj, x, z, ablock);
		mineshaftGenerator.func_151539_a(this, worldObj, x, z, ablock);

		Chunk chunk = new Chunk(worldObj, ablock, abyte, x, z);
		byte[] abyte1 = chunk.getBiomeArray();

		for (int k = 0; k < abyte1.length; ++k)
			abyte1[k] = (byte)biomesForGeneration[k].biomeID;

		chunk.generateSkylightMap();
		return chunk;
	}

	public void generateTerrain(int x, int z, Block[] par3BlockArray)
	{
		byte b0 = 63;
		biomesForGeneration = worldObj.getWorldChunkManager().getBiomesForGeneration(biomesForGeneration, x * 4 - 2, z * 4 - 2, 10, 10);
		generateNoise(x * 4, 0, z * 4);

		for (int k = 0; k < 4; ++k)
		{
			int l = k * 5;
			int i1 = (k + 1) * 5;

			for (int j1 = 0; j1 < 4; ++j1)
			{
				int k1 = (l + j1) * 33;
				int l1 = (l + j1 + 1) * 33;
				int i2 = (i1 + j1) * 33;
				int j2 = (i1 + j1 + 1) * 33;

				for (int k2 = 0; k2 < 32; ++k2)
				{
					double d0 = 0.125D;
					double d1 = field_147434_q[k1 + k2];
					double d2 = field_147434_q[l1 + k2];
					double d3 = field_147434_q[i2 + k2];
					double d4 = field_147434_q[j2 + k2];
					double d5 = (field_147434_q[k1 + k2 + 1] - d1) * d0;
					double d6 = (field_147434_q[l1 + k2 + 1] - d2) * d0;
					double d7 = (field_147434_q[i2 + k2 + 1] - d3) * d0;
					double d8 = (field_147434_q[j2 + k2 + 1] - d4) * d0;

					for (int l2 = 0; l2 < 8; ++l2)
					{
						double d9 = 0.25D;
						double d10 = d1;
						double d11 = d2;
						double d12 = (d3 - d1) * d9;
						double d13 = (d4 - d2) * d9;

						for (int i3 = 0; i3 < 4; ++i3)
						{
							int j3 = i3 + k * 4 << 12 | 0 + j1 * 4 << 8 | k2 * 8 + l2;
							short short1 = 256;
							j3 -= short1;
							double d14 = 0.25D;
							double d16 = (d11 - d10) * d14;
							double d15 = d10 - d16;

							for (int k3 = 0; k3 < 4; ++k3)
								if ((d15 += d16) > 0.0D)
									par3BlockArray[j3 += short1] = Blocks.stone;
								else if (k2 * 8 + l2 < b0)
									par3BlockArray[j3 += short1] = Blocks.water;
								else
									par3BlockArray[j3 += short1] = null;

							d10 += d12;
							d11 += d13;
						}

						d1 += d5;
						d2 += d6;
						d3 += d7;
						d4 += d8;
					}
				}
			}
		}
	}

	private void generateNoise(int x, int y, int z)
	{
		doubleArray4 = noiseGen6.generateNoiseOctaves(doubleArray4, x, z, 5, 5, 200.0D, 200.0D, 0.5D);
		doubleArray1 = noiseGen3.generateNoiseOctaves(doubleArray1, x, y, z, 5, 33, 5, 8.555150000000001D, 4.277575000000001D, 8.555150000000001D);
		doubleArray2 = noiseGen1.generateNoiseOctaves(doubleArray2, x, y, z, 5, 33, 5, 684.412D, 684.412D, 684.412D);
		doubleArray3 = noiseGen2.generateNoiseOctaves(doubleArray3, x, y, z, 5, 33, 5, 684.412D, 684.412D, 684.412D);
		int l = 0;
		int i1 = 0;
		for (int j1 = 0; j1 < 5; ++j1)
			for (int k1 = 0; k1 < 5; ++k1)
			{
				float f = 0.0F;
				float f1 = 0.0F;
				float f2 = 0.0F;
				byte b0 = 2;
				BiomeGenBase biomegenbase = biomesForGeneration[j1 + 2 + (k1 + 2) * 10];

				for (int l1 = -b0; l1 <= b0; ++l1)
					for (int i2 = -b0; i2 <= b0; ++i2)
					{
						BiomeGenBase biomegenbase1 = biomesForGeneration[j1 + l1 + 2 + (k1 + i2 + 2) * 10];
						float f3 = biomegenbase1.rootHeight;
						float f4 = biomegenbase1.heightVariation;

						if (worldType == WorldType.AMPLIFIED && f3 > 0.0F)
						{
							f3 = 1.0F + f3 * 2.0F;
							f4 = 1.0F + f4 * 4.0F;
						}

						float f5 = parabolicField[l1 + 2 + (i2 + 2) * 5] / (f3 + 2.0F);

						if (biomegenbase1.rootHeight > biomegenbase.rootHeight)
							f5 /= 2.0F;

						f += f4 * f5;
						f1 += f3 * f5;
						f2 += f5;
					}

				f /= f2;
				f1 /= f2;
				f = f * 0.9F + 0.1F;
				f1 = (f1 * 4.0F - 1.0F) / 8.0F;
				double d13 = doubleArray4[i1] / 8000.0D;

				if (d13 < 0.0D)
					d13 = -d13 * 0.3D;

				d13 = d13 * 3.0D - 2.0D;

				if (d13 < 0.0D)
				{
					d13 /= 2.0D;

					if (d13 < -1.0D)
						d13 = -1.0D;

					d13 /= 1.4D;
					d13 /= 2.0D;
				}
				else
				{
					if (d13 > 1.0D)
						d13 = 1.0D;

					d13 /= 8.0D;
				}

				++i1;
				double d12 = f1;
				double d14 = f;
				d12 += d13 * 0.2D;
				d12 = d12 * 8.5D / 8.0D;
				double d5 = 8.5D + d12 * 4.0D;

				for (int j2 = 0; j2 < 33; ++j2)
				{
					double d6 = (j2 - d5) * 12.0D * 128.0D / 256.0D / d14;

					if (d6 < 0.0D)
						d6 *= 4.0D;

					double d7 = doubleArray2[l] / 512.0D;
					double d8 = doubleArray3[l] / 512.0D;
					double d9 = (doubleArray1[l] / 10.0D + 1.0D) / 2.0D;
					double d10 = MathHelper.denormalizeClamp(d7, d8, d9) - d6;

					if (j2 > 29)
					{
						double d11 = (j2 - 29) / 3.0F;
						d10 = d10 * (1.0D - d11) + -10.0D * d11;
					}

					field_147434_q[l] = d10;
					++l;
				}
			}
	}

	private void func_147423_a(int p_147423_1_, int p_147423_2_, int p_147423_3_) {
		double d0 = 684.412D;
		double d1 = 684.412D;
		double d2 = 512.0D;
		double d3 = 512.0D;
		this.doubleArray4 = this.noiseGen6.generateNoiseOctaves(this.doubleArray4, p_147423_1_, p_147423_3_, 5, 5, 200.0D, 200.0D, 0.5D);
		this.doubleArray1 = this.noiseGen3.generateNoiseOctaves(this.doubleArray1, p_147423_1_, p_147423_2_, p_147423_3_, 5, 33, 5, 8.555150000000001D, 4.277575000000001D, 8.555150000000001D);

		this.doubleArray2 = this.noiseGen1.generateNoiseOctaves(this.doubleArray2, p_147423_1_, p_147423_2_, p_147423_3_, 5, 33, 5, 684.412D, 684.412D, 684.412D);

		this.doubleArray3 = this.noiseGen2.generateNoiseOctaves(this.doubleArray3, p_147423_1_, p_147423_2_, p_147423_3_, 5, 33, 5, 684.412D, 684.412D, 684.412D);

		boolean flag1 = false;
		boolean flag = false;
		int l = 0;
		int i1 = 0;
		double d4 = 8.5D;
		for (int j1 = 0; j1 < 5; j1++) {
			for (int k1 = 0; k1 < 5; k1++)
			{
				float f = 0.0F;
				float f1 = 0.0F;
				float f2 = 0.0F;
				byte b0 = 2;
				BiomeGenBase biomegenbase = this.biomesForGeneration[(j1 + 2 + (k1 + 2) * 10)];
				for (int l1 = -b0; l1 <= b0; l1++) {
					for (int i2 = -b0; i2 <= b0; i2++)
					{
						BiomeGenBase biomegenbase1 = this.biomesForGeneration[(j1 + l1 + 2 + (k1 + i2 + 2) * 10)];
						float f3 = biomegenbase1.rootHeight;
						float f4 = biomegenbase1.heightVariation;
						if ((this.worldType == WorldType.AMPLIFIED) && (f3 > 0.0F))
						{
							f3 = 1.0F + f3 * 2.0F;
							f4 = 1.0F + f4 * 4.0F;
						}
						float f5 = this.parabolicField[(l1 + 2 + (i2 + 2) * 5)] / (f3 + 2.0F);
						if (biomegenbase1.rootHeight > biomegenbase.rootHeight) {
							f5 /= 2.0F;
						}
						f += f4 * f5;
						f1 += f3 * f5;
						f2 += f5;
					}
				}
				f /= f2;
				f1 /= f2;
				f = f * 0.9F + 0.1F;
				f1 = (f1 * 4.0F - 1.0F) / 8.0F;
				double d13 = this.doubleArray4[i1] / 8000.0D;
				if (d13 < 0.0D) {
					d13 = -d13 * 0.3D;
				}
				d13 = d13 * 3.0D - 2.0D;
				if (d13 < 0.0D)
				{
					d13 /= 2.0D;
					if (d13 < -1.0D) {
						d13 = -1.0D;
					}
					d13 /= 1.4D;
					d13 /= 2.0D;
				}
				else
				{
					if (d13 > 1.0D) {
						d13 = 1.0D;
					}
					d13 /= 8.0D;
				}
				i1++;
				double d12 = f1;
				double d14 = f;
				d12 += d13 * 0.2D;
				d12 = d12 * 8.5D / 8.0D;
				double d5 = 8.5D + d12 * 4.0D;
				for (int j2 = 0; j2 < 33; j2++)
				{
					double d6 = (j2 - d5) * 12.0D * 128.0D / 256.0D / d14;
					if (d6 < 0.0D) {
						d6 *= 4.0D;
					}
					double d7 = this.doubleArray2[l] / 512.0D;
					double d8 = this.doubleArray3[l] / 512.0D;
					double d9 = (this.doubleArray1[l] / 10.0D + 1.0D) / 2.0D;
					double d10 = MathHelper.denormalizeClamp(d7, d8, d9) - d6;
					if (j2 > 29)
					{
						double d11 = (j2 - 29) / 3.0F;
						d10 = d10 * (1.0D - d11) + -10.0D * d11;
					}
					this.field_147434_q[l] = d10;
					l++;
				}
			}
		}
	}

	/**
	 * Checks to see if a chunk exists at x, y
	 */
	@Override
	public boolean chunkExists(int par1, int par2) {
		return super.chunkExists(par1, par2);
	}

	/**
	 * Populates chunk with ores etc etc
	 */
	@Override
	public void populate(IChunkProvider par1IChunkProvider, int par2, int par3) {
		
		boolean generateStructures = true;
		
        BlockFalling.fallInstantly = true;
        int x = par2 * 16;
        int z = par3 * 16;
        BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(x + 16, z + 16);
        this.rand.setSeed(this.worldObj.getSeed());
        long i1 = this.rand.nextLong() / 2L * 2L + 1L;
        long j1 = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed(par2 * i1 + par3 * j1 ^ this.worldObj.getSeed());
        boolean flag = false;

        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Pre(par1IChunkProvider, worldObj, rand, par2, par3, flag));

        if (generateStructures) {
            this.mineshaftGenerator.generateStructuresInChunk(this.worldObj, this.rand, par2, par3);        
            flag = this.villageGenerator.generateStructuresInChunk(this.worldObj, this.rand, par2, par3);
            this.scatteredFeatureGenerator.generateStructuresInChunk(this.worldObj, this.rand, par2, par3);
        }

        int k1;
        int l1;
        int i2;


        if (generateStructures) {
            if (generateStructures) {
                // No specific liquid dimlets specified: we generate default lakes (water and lava were appropriate).
                if (biomegenbase != BiomeGenBase.desert && biomegenbase != BiomeGenBase.desertHills && !flag && this.rand.nextInt(4) == 0
                        && TerrainGen.populate(par1IChunkProvider, worldObj, rand, par2, par3, flag, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAKE)) {
                    k1 = x + this.rand.nextInt(16) + 8;
                    l1 = this.rand.nextInt(256);
                    i2 = z + this.rand.nextInt(16) + 8;
                    (new WorldGenLakes(Blocks.water)).generate(this.worldObj, this.rand, k1, l1, i2);
                }

                if (TerrainGen.populate(par1IChunkProvider, worldObj, rand, par2, par3, flag, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAVA) && !flag && this.rand.nextInt(8) == 0) {
                    k1 = x + this.rand.nextInt(16) + 8;
                    l1 = this.rand.nextInt(this.rand.nextInt(248) + 8);
                    i2 = z + this.rand.nextInt(16) + 8;

                    if (l1 < 63 || this.rand.nextInt(10) == 0) {
                        (new WorldGenLakes(Blocks.lava)).generate(this.worldObj, this.rand, k1, l1, i2);
                    }
                }
            } /*else {
                // Generate lakes for the specified biomes.
                for (Block liquid : dimensionInformation.getFluidsForLakes()) {
                    if (!flag && this.rand.nextInt(4) == 0
                            && TerrainGen.populate(par1IChunkProvider, worldObj, rand, par2, par3, flag, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAKE)) {
                        k1 = x + this.rand.nextInt(16) + 8;
                        l1 = this.rand.nextInt(256);
                        i2 = z + this.rand.nextInt(16) + 8;
                        (new WorldGenLakes(liquid)).generate(this.worldObj, this.rand, k1, l1, i2);
                    }
                }
            }*/
        }

        boolean doGen = false;
        if (generateStructures) {
            doGen = TerrainGen.populate(par1IChunkProvider, worldObj, rand, par2, par3, flag, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.DUNGEON);
            for (k1 = 0; doGen && k1 < 8; ++k1) {
                l1 = x + this.rand.nextInt(16) + 8;
                i2 = this.rand.nextInt(256);
                int j2 = z + this.rand.nextInt(16) + 8;
                (new WorldGenDungeons()).generate(this.worldObj, this.rand, l1, i2, j2);
            }
        }

        biomegenbase.decorate(this.worldObj, this.rand, x, z);
        if (TerrainGen.populate(par1IChunkProvider, worldObj, rand, par2, par3, flag, ANIMALS)) {
            SpawnerAnimals.performWorldGenSpawning(this.worldObj, biomegenbase, x + 8, z + 8, 16, 16, this.rand);
        }
        x += 8;
        z += 8;

        doGen = TerrainGen.populate(par1IChunkProvider, worldObj, rand, par2, par3, flag, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ICE);
        for (k1 = 0; doGen && k1 < 16; ++k1) {
            for (l1 = 0; l1 < 16; ++l1) {
                i2 = this.worldObj.getPrecipitationHeight(x + k1, z + l1);

                if (this.worldObj.isBlockFreezable(k1 + x, i2 - 1, l1 + z)) {
                    this.worldObj.setBlock(k1 + x, i2 - 1, l1 + z, Blocks.ice, 0, 2);
                }

                if (this.worldObj.func_147478_e(k1 + x, i2, l1 + z, true)) {
                    this.worldObj.setBlock(k1 + x, i2, l1 + z, Blocks.snow_layer, 0, 2);
                }
            }
        }

        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Post(par1IChunkProvider, worldObj, rand, par2, par3, flag));

        BlockFalling.fallInstantly = false;
		//super.populate(par1IChunkProvider, par2, par3);
		/*net.minecraft.block.BlockFalling.fallInstantly = false;
		int k = par2 * 16;
		int l = par3 * 16;
		BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(k + 16, l + 16);
		this.rand.setSeed(this.worldObj.getSeed());
		long i1 = this.rand.nextLong() / 2L * 2L + 1L;
		long j1 = this.rand.nextLong() / 2L * 2L + 1L;
		this.rand.setSeed(par2 * i1 + par3 * j1 ^ this.worldObj.getSeed());
		boolean flag = false;

		MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Pre(par1IChunkProvider, this.worldObj, this.rand, par2, par3, flag));


		this.mineshaftGenerator.generateStructuresInChunk(this.worldObj, this.rand, par2, par3);
		flag = this.villageGenerator.generateStructuresInChunk(this.worldObj, this.rand, par2, par3);
		this.scatteredFeatureGenerator.generateStructuresInChunk(this.worldObj, this.rand, par2, par3);
		if (flag) {
			Logger.INFO("Did Generate? "+flag);
		}
		else {
			//Logger.INFO("Can village spawn here? "+villageGenerator.villageSpawnBiomes.contains(biomegenbase));
		}


		if ((biomegenbase != BiomeGenBase.desert) && (biomegenbase != BiomeGenBase.desertHills) && (!flag)) {
			if ((this.rand.nextInt(4) == 0) && 
					(TerrainGen.populate(par1IChunkProvider, this.worldObj, this.rand, par2, par3, flag, PopulateChunkEvent.Populate.EventType.LAKE)))
			{
				int k1 = k + this.rand.nextInt(16) + 8;
				int l1 = this.rand.nextInt(256);
				int i2 = l + this.rand.nextInt(16) + 8;
				new WorldGenLakes(Blocks.water).generate(this.worldObj, this.rand, k1, l1, i2);
			}
		}
		if ((TerrainGen.populate(par1IChunkProvider, this.worldObj, this.rand, par2, par3, flag, PopulateChunkEvent.Populate.EventType.LAVA)) && (!flag) && 
				(this.rand.nextInt(8) == 0))
		{
			int k1 = k + this.rand.nextInt(16) + 8;
			int l1 = this.rand.nextInt(this.rand.nextInt(248) + 8);
			int i2 = l + this.rand.nextInt(16) + 8;
			if ((l1 < 63) || (this.rand.nextInt(10) == 0)) {
				new WorldGenLakes(Blocks.water).generate(this.worldObj, this.rand, k1, l1, i2);
			}
		}
		int var4 = par2 * 16;
		int var5 = par3 * 16;

		biomegenbase.decorate(this.worldObj, this.rand, k, l);
		SpawnerAnimals.performWorldGenSpawning(this.worldObj, biomegenbase, k + 8, l + 8, 16, 16, this.rand);
		if (TerrainGen.populate(this, worldObj, rand, par2, par3, flag, ANIMALS))
		{
			SpawnerAnimals.performWorldGenSpawning(this.worldObj, biomegenbase, k + 8, l + 8, 16, 16, this.rand);
		}
		k += 8;
		l += 8;

		MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Post(par1IChunkProvider, this.worldObj, this.rand, par2, par3, flag));

		net.minecraft.block.BlockFalling.fallInstantly = false;*/
	}

	/**
	 * Two modes of operation: if passed true, save all Chunks in one go. If
	 * passed false, save up to two chunks. Return true if all chunks have
	 * been saved.
	 */
	@Override
	public boolean saveChunks(boolean par1, IProgressUpdate par2IProgressUpdate) {
		return super.saveChunks(par1, par2IProgressUpdate);
	}

	/**
	 * Save extra data not associated with any Chunk. Not saved during
	 * autosave, only during world unload. Currently unimplemented.
	 */
	@Override
	public void saveExtraData() {
	}

	/**
	 * Unloads chunks that are marked to be unloaded. This is not guaranteed
	 * to unload every such chunk.
	 */
	@Override
	public boolean unloadQueuedChunks() {
		return super.unloadQueuedChunks();
	}

	/**
	 * Returns if the IChunkProvider supports saving.
	 */
	@Override
	public boolean canSave() {
		return super.canSave();
	}

	/**
	 * Converts the instance data to a readable string.
	 */
	@Override
	public String makeString() {
		return "RandomLevelSource";
	}

	/**
	 * Returns a list of creatures of the specified type that can spawn at
	 * the given location.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List getPossibleCreatures(EnumCreatureType par1EnumCreatureType, int par2, int par3, int par4) {
		BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(par2, par4);
		return (par1EnumCreatureType == EnumCreatureType.monster) && (this.scatteredFeatureGenerator.func_143030_a(par2, par3, par4)) ? this.scatteredFeatureGenerator
				.getScatteredFeatureSpawnList() : biomegenbase
				.getSpawnableList(par1EnumCreatureType);
	}

	@Override
	public ChunkPosition func_147416_a(World p_147416_1_, String p_147416_2_, int p_147416_3_, int p_147416_4_, int p_147416_5_) {
		Logger.INFO("func_147416_a: "+p_147416_2_);
		//return super.func_147416_a(p_147416_1_, p_147416_2_, p_147416_3_, p_147416_4_, p_147416_5_);
		return 
				"ExtendedVillage".equals(p_147416_2_) &&
				this.villageGenerator != null ?
						this.villageGenerator.func_151545_a(p_147416_1_,
								p_147416_3_, p_147416_4_, p_147416_5_) :
									null;
	}

	@Override
	public int getLoadedChunkCount() {
		return super.getLoadedChunkCount();
	}

	@Override
	public void recreateStructures(int par1, int par2) {
		//super.recreateStructures(par1, par2);
		this.mineshaftGenerator.func_151539_a(this, this.worldObj, par1, par2, (Block[])null);
		this.villageGenerator.func_151539_a(this, this.worldObj, par1, par2, (Block[])null);
		this.scatteredFeatureGenerator.func_151539_a(this, this.worldObj, par1, par2, (Block[])null);

	}
}