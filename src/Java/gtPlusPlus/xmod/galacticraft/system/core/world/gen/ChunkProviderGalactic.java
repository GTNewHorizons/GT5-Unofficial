package gtPlusPlus.xmod.galacticraft.system.core.world.gen;

import java.util.List;

import com.google.common.collect.Lists;

import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import micdoodle8.mods.galacticraft.api.prefab.core.BlockMetaPair;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.BiomeDecoratorSpace;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.MapGenBaseMeta;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraft.world.chunk.IChunkProvider;

public abstract class ChunkProviderGalactic extends ChunkProviderGalaxyLakes {
	
	private List<MapGenBaseMeta> worldGenerators;
	private BiomeGenBase[] biomesForGeneration = this.getBiomesForGeneration();
	private final GalacticMapGenCavesBase caveGenerator = new GalacticMapGenCavesBase();

	protected List<MapGenBaseMeta> getWorldGenerators() {
		List<MapGenBaseMeta> generators = Lists.newArrayList();
		return generators;
	}		

	public ChunkProviderGalactic(World par1World, long seed, boolean mapFeaturesEnabled) {
		super(par1World, seed, mapFeaturesEnabled);
	}

	public abstract BiomeDecoratorSpace getBiomeGenerator();

	protected BiomeGenBase[] getBiomesForGeneration() {
		return new BiomeGenBase[] { GalacticBiomeGenBase.mGalaxy };
	}

	protected SpawnListEntry[] getCreatures() {
		return new SpawnListEntry[0];
	}

	public double getHeightModifier() {
		return 24.0D;
	}

	protected SpawnListEntry[] getMonsters() {

		Class aSkele = ReflectionUtils.getClass("micdoodle8.mods.galacticraft.core.entities.EntityEvolvedSkeleton");
		Class aCreeper = ReflectionUtils.getClass("micdoodle8.mods.galacticraft.core.entities.EntityEvolvedCreeper");
		Class aSpider = ReflectionUtils.getClass("micdoodle8.mods.galacticraft.core.entities.EntityEvolvedSpider");
		Class aEnderman = ReflectionUtils.getClass("galaxyspace.SolarSystem.planets.pluto.entities.EntityEvolvedEnderman");

		SpawnListEntry skele;
		SpawnListEntry creeper;
		SpawnListEntry spider;
		SpawnListEntry enderman;
		
		AutoMap<SpawnListEntry> aMobs = new AutoMap<SpawnListEntry>();
		
		if (aSkele != null) {
			skele = new SpawnListEntry(aSkele, 100, 4, 4);
			aMobs.put(skele);
		}
		if (aCreeper != null) {
			creeper = new SpawnListEntry(aCreeper, 100, 4, 4);
			aMobs.put(creeper);
		}
		if (aSpider != null) {
			spider = new SpawnListEntry(aSpider, 100, 4, 4);
			aMobs.put(spider);
		}
		if (aEnderman != null) {
			enderman = new SpawnListEntry(aEnderman, 100, 4, 4);
			aMobs.put(enderman);
		}

		return aMobs.toArray();
	}

	public void onPopulate(IChunkProvider arg0, int arg1, int arg2) {
	}

	public boolean chunkExists(int x, int y) {
		return false;
	}

	protected SpawnListEntry[] getWaterCreatures() {
		return new SpawnListEntry[0];
	}

	public BlockMetaPair getGrassBlock() {
		return new BlockMetaPair(null, (byte) 0);
	}

	public BlockMetaPair getDirtBlock() {
		return new BlockMetaPair(null, (byte) 0);
	}

	public BlockMetaPair getStoneBlock() {
		return new BlockMetaPair(null, (byte) 0);
	}

	protected boolean enableBiomeGenBaseBlock() {
		return false;
	}

	public void onChunkProvider(int cX, int cZ, Block[] blocks, byte[] metadata) {
	}

	public int getWaterLevel() {
		return 110;
	}

	public boolean canGenerateWaterBlock() {
		return true;
	}

	protected BlockMetaPair getWaterBlock() {
		return new BlockMetaPair(null, (byte) 0);
	}

	public boolean canGenerateIceBlock() {
		return false;
	}

}
