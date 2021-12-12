package gtPlusPlus.australia.world;

import cpw.mods.fml.common.IWorldGenerator;
import gtPlusPlus.api.interfaces.IGeneratorWorld;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.australia.GTplusplus_Australia;
import gtPlusPlus.australia.gen.map.MapGenExtendedVillage;
import gtPlusPlus.australia.gen.map.component.ComponentHut.WorldHandlerHut;
import gtPlusPlus.australia.gen.map.component.ComponentShack.WorldHandlerShack;
import gtPlusPlus.core.lib.CORE;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;

public class AustraliaWorldGenerator implements IWorldGenerator {
	private LinkedList<ChunkCoordIntPair> structuresList = new LinkedList<ChunkCoordIntPair>();
	//private final WorldHandlerCoven covenGen;
	//private final WorldHandlerWickerMan wickerManGen;
	private final WorldHandlerShack shackGen;
	private final WorldHandlerHut hutGen;
	private final List<IGeneratorWorld> generators;
	private int midX;
	private int midZ;
	int field_82665_g;
	int field_82666_h = 8;

	public static final AutoMap<Integer> SHACK_ALLOWED_BIOMES = new AutoMap<Integer>();
	public static final AutoMap<Integer> HUT_ALLOWED_BIOMES = new AutoMap<Integer>();
	public static final AutoMap<Integer> ALLOWED_BIOMES = new AutoMap<Integer>();

	public AustraliaWorldGenerator() {

		SHACK_ALLOWED_BIOMES.put(GTplusplus_Australia.Australian_Plains_Biome.biomeID);
		SHACK_ALLOWED_BIOMES.put(GTplusplus_Australia.Australian_Forest_Biome.biomeID);
		HUT_ALLOWED_BIOMES.put(GTplusplus_Australia.Australian_Desert_Biome_3.biomeID);
		HUT_ALLOWED_BIOMES.put(GTplusplus_Australia.Australian_Outback_Biome.biomeID);

		ALLOWED_BIOMES.put(GTplusplus_Australia.Australian_Plains_Biome.biomeID);
		ALLOWED_BIOMES.put(GTplusplus_Australia.Australian_Forest_Biome.biomeID);
		ALLOWED_BIOMES.put(GTplusplus_Australia.Australian_Desert_Biome_3.biomeID);
		ALLOWED_BIOMES.put(GTplusplus_Australia.Australian_Outback_Biome.biomeID);


		this.shackGen = new WorldHandlerShack(3);
		this.hutGen = new WorldHandlerHut(5);

		//IGeneratorWorld goblinHut = new WorldHandlerClonedStructure(ComponentGoblinHut.class, 1.0D, 400, 7, 7, 7);
		this.generators = Arrays
				.asList(new IGeneratorWorld[] { this.shackGen, this.hutGen });

		this.field_82665_g = (8 + Math.max(gtPlusPlus.core.util.math.MathUtils.randInt(/*Config.instance().worldGenFrequency*/32, 64), 1));

		this.midX = 0;
		this.midZ = 0;
		for (IGeneratorWorld gen : this.generators) {
			this.midX = Math.max(this.midX, gen.getExtentX() / 2);
			this.midZ = Math.max(this.midZ, gen.getExtentZ() / 2);
		}
	}

	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator,
			IChunkProvider chunkProvider) {
		if (world.provider.dimensionId == CORE.AUSTRALIA_ID) {
			generateOverworld(world, world.rand, chunkX * 16, chunkZ * 16);
		}
	}

	private void generateOverworld(World world, Random random, int x, int z) {
		boolean gen = false;
		try {
			if (ALLOWED_BIOMES.containsValue(Integer.valueOf(world.getBiomeGenForCoords(x + this.midX, z + this.midZ).biomeID))) {
				Collections.shuffle(this.generators, random);
				for (IGeneratorWorld generator : this.generators) {
					boolean canGenerate = false;
					
					if (generator instanceof WorldHandlerShack) {
						if (SHACK_ALLOWED_BIOMES.containsValue(Integer.valueOf(world.getBiomeGenForCoords(x + this.midX, z + this.midZ).biomeID))) {
							canGenerate = true;
						}
					}
					else if (generator instanceof WorldHandlerHut) {
						if (HUT_ALLOWED_BIOMES.containsValue(Integer.valueOf(world.getBiomeGenForCoords(x + this.midX, z + this.midZ).biomeID))) {
							canGenerate = true;
						}
					}	

					if (canGenerate) {
						//Logger.WORLD("Running World Generator on Australia.");
						boolean a1, a2;
						a1 = generator.generate(world, random, x, z);
						a2 = nonInRange(world, x, z, generator.getRange());
						//Logger.INFO("A1: "+a1+" | A2: "+a2);
						if (a1 && a2) {
							this.structuresList.add(new ChunkCoordIntPair(x, z));
							gen = true;
							//Logger.INFO("Generated a structure");
							break;
						}
					}					
				}
			}
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
	}

	protected boolean nonInRange(World worldObj, int x, int z, int range) {
		int par1 = x / 16;
		int par2 = z / 16;

		int k = par1;
		int l = par2;
		if (par1 < 0) {
			par1 -= this.field_82665_g - 1;
		}
		if (par2 < 0) {
			par2 -= this.field_82665_g - 1;
		}
		int i1 = par1 / this.field_82665_g;
		int j1 = par2 / this.field_82665_g;
		Random random = worldObj.setRandomSeed(i1, j1, 10387312);
		i1 *= this.field_82665_g;
		j1 *= this.field_82665_g;
		i1 += random.nextInt(this.field_82665_g - this.field_82666_h);
		j1 += random.nextInt(this.field_82665_g - this.field_82666_h);

		return (k == i1) && (l == j1);
	}

	public void initiate() {
		this.structuresList.clear();
	}
}
