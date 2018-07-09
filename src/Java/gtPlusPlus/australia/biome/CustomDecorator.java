package gtPlusPlus.australia.biome;

import java.util.Random;

import gtPlusPlus.australia.gen.world.WorldGenAustralianOre;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenCactus;
import net.minecraft.world.gen.feature.WorldGenDeadBush;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraft.world.gen.feature.WorldGenLiquids;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenPumpkin;
import net.minecraft.world.gen.feature.WorldGenReed;
import net.minecraft.world.gen.feature.WorldGenSand;
import net.minecraft.world.gen.feature.WorldGenWaterlily;
import net.minecraft.world.gen.feature.WorldGenerator;

import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.*;
import static net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.*;
import net.minecraftforge.common.*;
import net.minecraftforge.event.terraingen.*;

public class CustomDecorator extends BiomeDecorator {
	

	public CustomDecorator() {
		this.sandGen = new WorldGenSand(Blocks.sand, 10);
		this.gravelAsSandGen = new WorldGenSand(Blocks.gravel, 6);
		this.dirtGen = new WorldGenMinable(Blocks.dirt, 16);
		this.gravelGen = new WorldGenMinable(Blocks.gravel, 16);
		
		this.coalGen = new WorldGenAustralianOre(Blocks.coal_ore, 4);
		this.ironGen = new WorldGenAustralianOre(Blocks.clay, 4);
		this.goldGen = new WorldGenAustralianOre(Blocks.soul_sand, 20);
		this.redstoneGen = new WorldGenAustralianOre(Blocks.bedrock, 8);
		this.diamondGen = new WorldGenAustralianOre(Blocks.diamond_ore, 1);
		this.lapisGen = new WorldGenAustralianOre(Blocks.lava, 16);
		
		this.yellowFlowerGen = new WorldGenFlowers(Blocks.yellow_flower);
		this.mushroomBrownGen = new WorldGenFlowers(Blocks.brown_mushroom);
		this.mushroomRedGen = new WorldGenFlowers(Blocks.red_mushroom);
		this.bigMushroomGen = new WorldGenBigMushroom();
		this.reedGen = new WorldGenReed();
		this.cactusGen = new WorldGenCactus();
		this.waterlilyGen = new WorldGenWaterlily();
		this.flowersPerChunk = 2;
		this.grassPerChunk = 1;
		this.sandPerChunk = 1;
		this.sandPerChunk2 = 3;
		this.clayPerChunk = 2;
		this.generateLakes = true;
	}

	public void decorateChunk(World p_150512_1_, Random p_150512_2_, BiomeGenBase p_150512_3_, int p_150512_4_,
			int p_150512_5_) {
		if (this.currentWorld != null) {
			throw new RuntimeException("Already decorating!!");
		} else {
			this.currentWorld = p_150512_1_;
			this.randomGenerator = p_150512_2_;
			this.chunk_X = p_150512_4_;
			this.chunk_Z = p_150512_5_;
			this.genDecorations(p_150512_3_);
			this.currentWorld = null;
			this.randomGenerator = null;
		}
	}

	protected void genDecorations(BiomeGenBase p_150513_1_) {
		MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Pre(currentWorld, randomGenerator, chunk_X, chunk_Z));
		this.generateOres();
		int i;
		int j;
		int k;

		boolean doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, SAND);
		for (i = 0; doGen && i < this.sandPerChunk2; ++i) {
			j = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
			k = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
			this.sandGen.generate(this.currentWorld, this.randomGenerator, j,
					this.currentWorld.getTopSolidOrLiquidBlock(j, k), k);
		}

		doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, CLAY);
		for (i = 0; doGen && i < this.clayPerChunk; ++i) {
			j = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
			k = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
			this.clayGen.generate(this.currentWorld, this.randomGenerator, j,
					this.currentWorld.getTopSolidOrLiquidBlock(j, k), k);
		}

		doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, SAND_PASS2);
		for (i = 0; doGen && i < this.sandPerChunk; ++i) {
			j = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
			k = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
			this.gravelAsSandGen.generate(this.currentWorld, this.randomGenerator, j,
					this.currentWorld.getTopSolidOrLiquidBlock(j, k), k);
		}

		i = this.treesPerChunk;

		if (this.randomGenerator.nextInt(10) == 0) {
			++i;
		}

		int l;
		int i1;

		doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, TREE);
		for (j = 0; doGen && j < i; ++j) {
			k = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
			l = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
			i1 = this.currentWorld.getHeightValue(k, l);
			WorldGenAbstractTree worldgenabstracttree = p_150513_1_.func_150567_a(this.randomGenerator);
			worldgenabstracttree.setScale(1.0D, 1.0D, 1.0D);

			if (worldgenabstracttree.generate(this.currentWorld, this.randomGenerator, k, i1, l)) {
				worldgenabstracttree.func_150524_b(this.currentWorld, this.randomGenerator, k, i1, l);
			}
		}

		doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, BIG_SHROOM);
		for (j = 0; doGen && j < this.bigMushroomsPerChunk; ++j) {
			k = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
			l = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
			this.bigMushroomGen.generate(this.currentWorld, this.randomGenerator, k,
					this.currentWorld.getHeightValue(k, l), l);
		}

		doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, FLOWERS);
		for (j = 0; doGen && j < this.flowersPerChunk; ++j) {
			k = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
			l = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
			i1 = nextInt(this.currentWorld.getHeightValue(k, l) + 32);
			String s = p_150513_1_.func_150572_a(this.randomGenerator, k, i1, l);
			BlockFlower blockflower = BlockFlower.func_149857_e(s);

			if (blockflower.getMaterial() != Material.air) {
				this.yellowFlowerGen.func_150550_a(blockflower, BlockFlower.func_149856_f(s));
				this.yellowFlowerGen.generate(this.currentWorld, this.randomGenerator, k, i1, l);
			}
		}

		doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, GRASS);
		for (j = 0; doGen && j < this.grassPerChunk; ++j) {
			k = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
			l = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
			i1 = nextInt(this.currentWorld.getHeightValue(k, l) * 2);
			WorldGenerator worldgenerator = p_150513_1_.getRandomWorldGenForGrass(this.randomGenerator);
			worldgenerator.generate(this.currentWorld, this.randomGenerator, k, i1, l);
		}

		doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, DEAD_BUSH);
		for (j = 0; doGen && j < this.deadBushPerChunk; ++j) {
			k = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
			l = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
			i1 = nextInt(this.currentWorld.getHeightValue(k, l) * 2);
			(new WorldGenDeadBush(Blocks.deadbush)).generate(this.currentWorld, this.randomGenerator, k, i1, l);
		}

		doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, LILYPAD);
		for (j = 0; doGen && j < this.waterlilyPerChunk; ++j) {
			k = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
			l = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;

			for (i1 = nextInt(this.currentWorld.getHeightValue(k, l) * 2); i1 > 0
					&& this.currentWorld.isAirBlock(k, i1 - 1, l); --i1) {
				;
			}

			this.waterlilyGen.generate(this.currentWorld, this.randomGenerator, k, i1, l);
		}

		doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, SHROOM);
		for (j = 0; doGen && j < this.mushroomsPerChunk; ++j) {
			if (this.randomGenerator.nextInt(4) == 0) {
				k = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
				l = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
				i1 = this.currentWorld.getHeightValue(k, l);
				this.mushroomBrownGen.generate(this.currentWorld, this.randomGenerator, k, i1, l);
			}

			if (this.randomGenerator.nextInt(8) == 0) {
				k = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
				l = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
				i1 = nextInt(this.currentWorld.getHeightValue(k, l) * 2);
				this.mushroomRedGen.generate(this.currentWorld, this.randomGenerator, k, i1, l);
			}
		}

		if (doGen && this.randomGenerator.nextInt(4) == 0) {
			j = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
			k = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
			l = nextInt(this.currentWorld.getHeightValue(j, k) * 2);
			this.mushroomBrownGen.generate(this.currentWorld, this.randomGenerator, j, l, k);
		}

		if (doGen && this.randomGenerator.nextInt(8) == 0) {
			j = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
			k = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
			l = nextInt(this.currentWorld.getHeightValue(j, k) * 2);
			this.mushroomRedGen.generate(this.currentWorld, this.randomGenerator, j, l, k);
		}

		doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, REED);
		for (j = 0; doGen && j < this.reedsPerChunk; ++j) {
			k = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
			l = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
			i1 = nextInt(this.currentWorld.getHeightValue(k, l) * 2);
			this.reedGen.generate(this.currentWorld, this.randomGenerator, k, i1, l);
		}

		for (j = 0; doGen && j < 10; ++j) {
			k = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
			l = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
			i1 = nextInt(this.currentWorld.getHeightValue(k, l) * 2);
			this.reedGen.generate(this.currentWorld, this.randomGenerator, k, i1, l);
		}

		doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, PUMPKIN);
		if (doGen && this.randomGenerator.nextInt(32) == 0) {
			j = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
			k = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
			l = nextInt(this.currentWorld.getHeightValue(j, k) * 2);
			(new WorldGenPumpkin()).generate(this.currentWorld, this.randomGenerator, j, l, k);
		}

		doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, CACTUS);
		for (j = 0; doGen && j < this.cactiPerChunk; ++j) {
			k = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
			l = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
			i1 = nextInt(this.currentWorld.getHeightValue(k, l) * 2);
			this.cactusGen.generate(this.currentWorld, this.randomGenerator, k, i1, l);
		}

		doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, LAKE);
		if (doGen && this.generateLakes) {
			for (j = 0; j < 50; ++j) {
				k = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
				l = this.randomGenerator.nextInt(this.randomGenerator.nextInt(248) + 8);
				i1 = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
				(new WorldGenLiquids(Blocks.flowing_water)).generate(this.currentWorld, this.randomGenerator, k, l, i1);
			}

			for (j = 0; j < 20; ++j) {
				k = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
				l = this.randomGenerator
						.nextInt(this.randomGenerator.nextInt(this.randomGenerator.nextInt(240) + 8) + 8);
				i1 = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
				(new WorldGenLiquids(Blocks.flowing_lava)).generate(this.currentWorld, this.randomGenerator, k, l, i1);
			}
		}

		MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Post(currentWorld, randomGenerator, chunk_X, chunk_Z));
	}

	/**
	 * Standard ore generation helper. Generates most ores.
	 */
	protected void genStandardOre1(int aAmount, WorldGenerator aOreGenerator, int p_76795_3_, int p_76795_4_) {
		for (int l = 0; l < aAmount; ++l) {
			int i1 = this.chunk_X + this.randomGenerator.nextInt(16);
			int j1 = this.randomGenerator.nextInt(p_76795_4_ - p_76795_3_) + p_76795_3_;
			int k1 = this.chunk_Z + this.randomGenerator.nextInt(16);
			aOreGenerator.generate(this.currentWorld, this.randomGenerator, i1, j1, k1);
		}
	}

	/**
	 * Standard ore generation helper. Generates Lapis Lazuli.
	 */
	protected void genStandardOre2(int p_76793_1_, WorldGenerator p_76793_2_, int p_76793_3_, int p_76793_4_) {
		for (int l = 0; l < p_76793_1_; ++l) {
			int i1 = this.chunk_X + this.randomGenerator.nextInt(16);
			int j1 = this.randomGenerator.nextInt(p_76793_4_) + this.randomGenerator.nextInt(p_76793_4_)
					+ (p_76793_3_ - p_76793_4_);
			int k1 = this.chunk_Z + this.randomGenerator.nextInt(16);
			p_76793_2_.generate(this.currentWorld, this.randomGenerator, i1, j1, k1);
		}
	}

	/**
	 * Generates ores in the current chunk
	 */
	protected void generateOres() {
		MinecraftForge.ORE_GEN_BUS.post(new OreGenEvent.Pre(currentWorld, randomGenerator, chunk_X, chunk_Z));
		if (TerrainGen.generateOre(currentWorld, randomGenerator, dirtGen, chunk_X, chunk_Z, DIRT))
			this.genStandardOre1(20, this.dirtGen, 0, 256);
		if (TerrainGen.generateOre(currentWorld, randomGenerator, gravelGen, chunk_X, chunk_Z, GRAVEL))
			this.genStandardOre1(10, this.gravelGen, 0, 256);
		if (TerrainGen.generateOre(currentWorld, randomGenerator, coalGen, chunk_X, chunk_Z, COAL))
			this.genStandardOre1(20, this.coalGen, 0, 128);
		if (TerrainGen.generateOre(currentWorld, randomGenerator, ironGen, chunk_X, chunk_Z, IRON))
			this.genStandardOre1(10, this.ironGen, 0, 64);
		if (TerrainGen.generateOre(currentWorld, randomGenerator, goldGen, chunk_X, chunk_Z, GOLD))
			this.genStandardOre1(15, this.goldGen, 0, 32);
		if (TerrainGen.generateOre(currentWorld, randomGenerator, redstoneGen, chunk_X, chunk_Z, REDSTONE))
			this.genStandardOre1(10, this.redstoneGen, 0, 16);
		if (TerrainGen.generateOre(currentWorld, randomGenerator, diamondGen, chunk_X, chunk_Z, DIAMOND))
			this.genStandardOre1(1, this.diamondGen, 0, 16);
		if (TerrainGen.generateOre(currentWorld, randomGenerator, lapisGen, chunk_X, chunk_Z, LAPIS))
			this.genStandardOre2(10, this.lapisGen, 16, 16);
		MinecraftForge.ORE_GEN_BUS.post(new OreGenEvent.Post(currentWorld, randomGenerator, chunk_X, chunk_Z));
	}

	private int nextInt(int i) {
		if (i <= 1)
			return 0;
		return this.randomGenerator.nextInt(i);
	}
}