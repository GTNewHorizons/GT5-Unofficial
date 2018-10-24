package gtPlusPlus.xmod.galacticraft.system.core.dim;

import java.util.Random;

import gtPlusPlus.api.objects.random.XSTR;
import gtPlusPlus.xmod.galacticraft.system.BaseGalacticDimension;
import gtPlusPlus.xmod.galacticraft.system.core.world.gen.GalacticBiomeGenBase;
import gtPlusPlus.xmod.galacticraft.system.objects.BiomeSettings;
import gtPlusPlus.xmod.galacticraft.system.objects.DimensionSettings;
import gtPlusPlus.xmod.galacticraft.system.objects.PlanetGenerator;
import gtPlusPlus.xmod.galacticraft.system.objects.WorldProviderSettings;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Post;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Pre;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType;

public class BaseWorldProviderGalactic {
	
	private final PlanetGenerator mThisPlanet;
	private final BaseGalacticDimension mDim;
	public final BiomeGenBase mBiome;
	
	//new DimensionSettings(b, aCP, 5, true, 1, false, 240f, 0.1f, 0.2f, false, 48000L)
	
	public BaseWorldProviderGalactic(WorldProviderSettings b) {		
		this(b.getPlanet(), b.getDimSettings(), b.getBiomeSettings());
	}
	
	private BaseWorldProviderGalactic(PlanetGenerator b, DimensionSettings aDimSettings, BiomeSettings aBiomeSettings) {
		mThisPlanet = b;
		Class<? extends IChunkProvider> aCP = aDimSettings.getChunkProvider();
		mBiome = tryCreateBiome(aBiomeSettings);
		mDim = new BaseGalacticDimension(b, mBiome, aCP, aDimSettings);
	}

	public synchronized final BaseGalacticDimension getDim() {
		return mDim;
	}
	
	public BiomeGenBase tryCreateBiome(BiomeSettings aSettings) {		
		BiomeGenBase x = (new BiomeGenMain(aSettings.getID())).setBiomeName(aSettings.getName()).setHeight(aSettings.getHeight());		
		return x;
	}
		
	public class BiomeGenBasePlanetSurface extends GalacticBiomeGenBase {
		public BiomeGenBase aBiome;

		public BiomeGenBasePlanetSurface(int id) {
			super(id);
			this.enableRain = true;
			this.enableSnow = true;
			this.topBlock = Blocks.stone;
			this.fillerBlock = Blocks.stone;
		}

		public BiomeDecorator createBiomeDecorator() {
			return new BiomeDecoratorGalactic();
		}

		protected BiomeDecoratorGalactic getBiomeDecorator() {
			return (BiomeDecoratorGalactic) this.theBiomeDecorator;
		}

	}
	
	public class BiomeGenMain extends BiomeGenBasePlanetSurface {
		public BiomeGenMain(int aID) {
			super(aID);
			this.enableRain = false;
			this.enableSnow = false;
			this.topBlock = mThisPlanet.getTask().getTopLayer();
			this.topMeta = 0;
			this.fillerBlock = mThisPlanet.getTask().getSoil();
			this.fillerMeta = 0;
			this.stoneBlock = mThisPlanet.getTask().getStone();
			this.stoneMeta = 0;
			this.spawnableCaveCreatureList.clear();
			this.spawnableCreatureList.clear();
			this.spawnableMonsterList.clear();
			this.spawnableWaterCreatureList.clear();
			if (!ConfigManagerCore.disableBiomeTypeRegistrations) {
				BiomeDictionary.registerBiomeType(this, new Type[]{Type.COLD, Type.DRY, Type.DEAD, Type.SPOOKY});
			}

		}
	}
	
	public class BiomeDecoratorGalactic extends BiomeDecorator {
		
		public void decorateChunk(World world, Random rand, BiomeGenBase biome, int x, int z) {
			if (this.currentWorld != null) {
				throw new RuntimeException("Already decorating!!");
			} else {
				this.currentWorld = world;
				this.randomGenerator = new XSTR(rand.nextLong());
				this.chunk_X = x;
				this.chunk_Z = z;
				this.genDecorations(biome);
				this.currentWorld = null;
				this.randomGenerator = null;
			}
		}

		protected void genDecorations(BiomeGenBase biome) {
			MinecraftForge.EVENT_BUS.post(new Pre(this.currentWorld, this.randomGenerator, this.chunk_X, this.chunk_Z));
			MinecraftForge.EVENT_BUS.post(new Post(this.currentWorld, this.randomGenerator, this.chunk_X, this.chunk_Z));
		}

		protected boolean getGen(EventType event) {
			return TerrainGen.decorate(this.currentWorld, this.randomGenerator, this.chunk_X, this.chunk_Z, event);
		}
	}
	
}