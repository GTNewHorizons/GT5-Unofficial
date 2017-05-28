package gtPlusPlus.core.world.darkworld;

import java.util.*;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.world.darkworld.biome.Biome_DarkWorld;
import gtPlusPlus.core.world.darkworld.block.*;
import gtPlusPlus.core.world.darkworld.item.itemDarkWorldPortalTrigger;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.*;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.layer.*;
import net.minecraft.world.gen.structure.*;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.*;

@SuppressWarnings("unchecked")
public class Dimension_DarkWorld {

	public Object instance;
	public static int DIMID = 227;

	public static blockDarkWorldPortal portalBlock;
	public static itemDarkWorldPortalTrigger portalItem;
	public static Block blockTopLayer;
	public static Block blockSecondLayer = Blocks.dirt;
	public static Block blockMainFiller = Blocks.stone;
	public static Block blockSecondaryFiller;
	public static Block blockFluidLakes = Blocks.lava;

	public static Block blockPortalFrame;

	static {

		DIMID = DimensionManager.getNextFreeDimId();
		portalBlock = new blockDarkWorldPortal();
		portalItem = (itemDarkWorldPortalTrigger) (new itemDarkWorldPortalTrigger().setUnlocalizedName("dimensionDarkWorld_trigger"));
		Item.itemRegistry.addObject(423, "dimensionDarkWorld_trigger", portalItem);
		blockTopLayer = new blockDarkWorldGround();
		GameRegistry.registerBlock(blockTopLayer, "blockDarkWorldGround");
		blockPortalFrame = new blockDarkWorldPortalFrame();
		GameRegistry.registerBlock(blockPortalFrame, "blockDarkWorldPortalFrame");
	}

	public Dimension_DarkWorld() {
	}

	public void load() {
		GameRegistry.registerBlock(portalBlock, "dimensionDarkWorld_portal");
		DimensionManager.registerProviderType(DIMID, Dimension_DarkWorld.WorldProviderMod.class, false);
		DimensionManager.registerDimension(DIMID, DIMID);
		// GameRegistry.addSmelting(Items.record_11, new ItemStack(block),
		// 1.0f);

	}

	public void registerRenderers() {
	}

	public void generateNether(World world, Random random, int chunkX, int chunkZ) {
	}

	public void generateSurface(World world, Random random, int chunkX, int chunkZ) {
	}

	public int addFuel(ItemStack fuel) {
		return 0;
	}

	public void serverLoad(FMLServerStartingEvent event) {
	}

	public void preInit(FMLPreInitializationEvent event) {
	}

	public static class WorldProviderMod extends WorldProvider {

		@Override
		public void registerWorldChunkManager() {
			this.worldChunkMgr = new WorldChunkManagerCustom(this.worldObj.getSeed(), WorldType.AMPLIFIED);
			this.isHellWorld = false;
			this.hasNoSky = true;
			this.dimensionId = DIMID;
		}

		@Override
		@SideOnly(Side.CLIENT)
		public Vec3 getFogColor(float par1, float par2) {
			return Vec3.createVectorHelper(0.01568627450980392D, 0.09019607843137255D, 0.0D);
		}

		@Override
		public IChunkProvider createChunkGenerator() {
			return new ChunkProviderModded(this.worldObj, this.worldObj.getSeed() - 1278);
		}

		@Override
		public boolean isSurfaceWorld() {
			return true;
		}

		@Override
		public boolean canCoordinateBeSpawn(int par1, int par2) {
			return false;
		}

		@Override
		public boolean canRespawnHere() {
			return true;
		}

		@Override
		@SideOnly(Side.CLIENT)
		public boolean doesXZShowFog(int par1, int par2) {
			return true;
		}

		@Override
		public String getDimensionName() {
			return "dimensionDarkWorld";
		}

	}

	public class DarkWorldPortalPosition extends ChunkCoordinates {
		public long field_85087_d;
		final TeleporterDimensionMod field_85088_e;

		public DarkWorldPortalPosition(TeleporterDimensionMod darkworldTeleporter, int par2, int par3, int par4, long par5) {
			super(par2, par3, par4);
			this.field_85088_e = darkworldTeleporter;
			this.field_85087_d = par5;
		}
	}

	public static class TeleporterDimensionMod extends Teleporter {

		private final WorldServer worldServerInstance;
		/**
		 * A private Random() function in Teleporter
		 */
		private final Random random;
		/**
		 * Stores successful portal placement locations for rapid lookup.
		 */
		private final LongHashMap destinationCoordinateCache = new LongHashMap();
		/**
		 * A list of valid keys for the destinationCoordainteCache. These are
		 * based on the X & Z of the players initial location.
		 */
		@SuppressWarnings("rawtypes")
		private final List destinationCoordinateKeys = new ArrayList();

		public TeleporterDimensionMod(WorldServer par1WorldServer) {
			super(par1WorldServer);
			this.worldServerInstance = par1WorldServer;
			this.random = new Random(par1WorldServer.getSeed());
		}

		/**
		 * Place an entity in a nearby portal, creating one if necessary.
		 */
		@Override
		public void placeInPortal(Entity par1Entity, double par2, double par4, double par6, float par8) {
			if (this.worldServerInstance.provider.dimensionId != 1) {
				if (!this.placeInExistingPortal(par1Entity, par2, par4, par6, par8)) {
					this.makePortal(par1Entity);
					this.placeInExistingPortal(par1Entity, par2, par4, par6, par8);
				}
			}
			else {
				int i = MathHelper.floor_double(par1Entity.posX);
				int j = MathHelper.floor_double(par1Entity.posY) - 1;
				int k = MathHelper.floor_double(par1Entity.posZ);
				byte b0 = 1;
				byte b1 = 0;

				for (int l = -2; l <= 2; ++l) {
					for (int i1 = -2; i1 <= 2; ++i1) {
						for (int j1 = -1; j1 < 3; ++j1) {
							int k1 = i + i1 * b0 + l * b1;
							int l1 = j + j1;
							int i2 = k + i1 * b1 - l * b0;
							boolean flag = j1 < 0;
							this.worldServerInstance.setBlock(k1, l1, i2, flag ? blockPortalFrame : Blocks.air);
						}
					}
				}

				par1Entity.setLocationAndAngles(i, j, k, par1Entity.rotationYaw, 0.0F);
				par1Entity.motionX = par1Entity.motionY = par1Entity.motionZ = 0.0D;
			}
		}

		/**
		 * Place an entity in a nearby portal which already exists.
		 */
		@Override
		public boolean placeInExistingPortal(Entity par1Entity, double par2, double par4, double par6, float par8) {
			short short1 = 128;
			double d3 = -1.0D;
			int i = 0;
			int j = 0;
			int k = 0;
			int l = MathHelper.floor_double(par1Entity.posX);
			int i1 = MathHelper.floor_double(par1Entity.posZ);
			long j1 = ChunkCoordIntPair.chunkXZ2Int(l, i1);
			boolean flag = true;
			double d7;
			int l3;

			if (this.destinationCoordinateCache.containsItem(j1)) {
				Teleporter.PortalPosition portalposition = (Teleporter.PortalPosition) this.destinationCoordinateCache
						.getValueByKey(j1);
				d3 = 0.0D;
				i = portalposition.posX;
				j = portalposition.posY;
				k = portalposition.posZ;
				portalposition.lastUpdateTime = this.worldServerInstance.getTotalWorldTime();
				flag = false;
			}
			else {
				for (l3 = l - short1; l3 <= l + short1; ++l3) {
					double d4 = l3 + 0.5D - par1Entity.posX;

					for (int l1 = i1 - short1; l1 <= i1 + short1; ++l1) {
						double d5 = l1 + 0.5D - par1Entity.posZ;

						for (int i2 = this.worldServerInstance.getActualHeight() - 1; i2 >= 0; --i2) {
							if (this.worldServerInstance.getBlock(l3, i2, l1) == portalBlock) {
								while (this.worldServerInstance.getBlock(l3, i2 - 1, l1) == portalBlock) {
									--i2;
								}

								d7 = i2 + 0.5D - par1Entity.posY;
								double d8 = d4 * d4 + d7 * d7 + d5 * d5;

								if (d3 < 0.0D || d8 < d3) {
									d3 = d8;
									i = l3;
									j = i2;
									k = l1;
								}
							}
						}
					}
				}
			}

			if (d3 >= 0.0D) {
				if (flag) {
					this.destinationCoordinateCache.add(j1,
							new Teleporter.PortalPosition(i, j, k, this.worldServerInstance.getTotalWorldTime()));
					this.destinationCoordinateKeys.add(Long.valueOf(j1));
				}

				double d11 = i + 0.5D;
				double d6 = j + 0.5D;
				d7 = k + 0.5D;
				int i4 = -1;

				if (this.worldServerInstance.getBlock(i - 1, j, k) == portalBlock) {
					i4 = 2;
				}

				if (this.worldServerInstance.getBlock(i + 1, j, k) == portalBlock) {
					i4 = 0;
				}

				if (this.worldServerInstance.getBlock(i, j, k - 1) == portalBlock) {
					i4 = 3;
				}

				if (this.worldServerInstance.getBlock(i, j, k + 1) == portalBlock) {
					i4 = 1;
				}

				int j2 = par1Entity.getTeleportDirection();

				if (i4 > -1) {
					int k2 = Direction.rotateLeft[i4];
					int l2 = Direction.offsetX[i4];
					int i3 = Direction.offsetZ[i4];
					int j3 = Direction.offsetX[k2];
					int k3 = Direction.offsetZ[k2];
					boolean flag1 = !this.worldServerInstance.isAirBlock(i + l2 + j3, j, k + i3 + k3)
							|| !this.worldServerInstance.isAirBlock(i + l2 + j3, j + 1, k + i3 + k3);
					boolean flag2 = !this.worldServerInstance.isAirBlock(i + l2, j, k + i3)
							|| !this.worldServerInstance.isAirBlock(i + l2, j + 1, k + i3);

					if (flag1 && flag2) {
						i4 = Direction.rotateOpposite[i4];
						k2 = Direction.rotateOpposite[k2];
						l2 = Direction.offsetX[i4];
						i3 = Direction.offsetZ[i4];
						j3 = Direction.offsetX[k2];
						k3 = Direction.offsetZ[k2];
						l3 = i - j3;
						d11 -= j3;
						int k1 = k - k3;
						d7 -= k3;
						flag1 = !this.worldServerInstance.isAirBlock(l3 + l2 + j3, j, k1 + i3 + k3)
								|| !this.worldServerInstance.isAirBlock(l3 + l2 + j3, j + 1, k1 + i3 + k3);
						flag2 = !this.worldServerInstance.isAirBlock(l3 + l2, j, k1 + i3)
								|| !this.worldServerInstance.isAirBlock(l3 + l2, j + 1, k1 + i3);
					}

					float f1 = 0.5F;
					float f2 = 0.5F;

					if (!flag1 && flag2) {
						f1 = 1.0F;
					}
					else if (flag1 && !flag2) {
						f1 = 0.0F;
					}
					else if (flag1 && flag2) {
						f2 = 0.0F;
					}

					d11 += j3 * f1 + f2 * l2;
					d7 += k3 * f1 + f2 * i3;
					float f3 = 0.0F;
					float f4 = 0.0F;
					float f5 = 0.0F;
					float f6 = 0.0F;

					if (i4 == j2) {
						f3 = 1.0F;
						f4 = 1.0F;
					}
					else if (i4 == Direction.rotateOpposite[j2]) {
						f3 = -1.0F;
						f4 = -1.0F;
					}
					else if (i4 == Direction.rotateRight[j2]) {
						f5 = 1.0F;
						f6 = -1.0F;
					}
					else {
						f5 = -1.0F;
						f6 = 1.0F;
					}

					double d9 = par1Entity.motionX;
					double d10 = par1Entity.motionZ;
					par1Entity.motionX = d9 * f3 + d10 * f6;
					par1Entity.motionZ = d9 * f5 + d10 * f4;
					par1Entity.rotationYaw = par8 - j2 * 90 + i4 * 90;
				}
				else {
					par1Entity.motionX = par1Entity.motionY = par1Entity.motionZ = 0.0D;
				}

				par1Entity.setLocationAndAngles(d11, d6, d7, par1Entity.rotationYaw, par1Entity.rotationPitch);
				return true;
			}
			else {
				return false;
			}
		}

		@Override
		public boolean makePortal(Entity par1Entity) {
			byte b0 = 16;
			double d0 = -1.0D;
			int i = MathHelper.floor_double(par1Entity.posX);
			int j = MathHelper.floor_double(par1Entity.posY);
			int k = MathHelper.floor_double(par1Entity.posZ);
			int l = i;
			int i1 = j;
			int j1 = k;
			int k1 = 0;
			int l1 = this.random.nextInt(4);
			int i2;
			double d1;
			double d2;
			int k2;
			int i3;
			int k3;
			int j3;
			int i4;
			int l3;
			int k4;
			int j4;
			int i5;
			int l4;
			double d3;
			double d4;

			for (i2 = i - b0; i2 <= i + b0; ++i2) {
				d1 = i2 + 0.5D - par1Entity.posX;

				for (k2 = k - b0; k2 <= k + b0; ++k2) {
					d2 = k2 + 0.5D - par1Entity.posZ;
					label274:

					for (i3 = this.worldServerInstance.getActualHeight() - 1; i3 >= 0; --i3) {
						if (this.worldServerInstance.isAirBlock(i2, i3, k2)) {
							while (i3 > 0 && this.worldServerInstance.isAirBlock(i2, i3 - 1, k2)) {
								--i3;
							}

							for (j3 = l1; j3 < l1 + 4; ++j3) {
								k3 = j3 % 2;
								l3 = 1 - k3;

								if (j3 % 4 >= 2) {
									k3 = -k3;
									l3 = -l3;
								}

								for (i4 = 0; i4 < 3; ++i4) {
									for (j4 = 0; j4 < 4; ++j4) {
										for (k4 = -1; k4 < 4; ++k4) {
											l4 = i2 + (j4 - 1) * k3 + i4 * l3;
											i5 = i3 + k4;
											int j5 = k2 + (j4 - 1) * l3 - i4 * k3;

											if (k4 < 0
													&& !this.worldServerInstance.getBlock(l4, i5, j5).getMaterial()
															.isSolid()
													|| k4 >= 0 && !this.worldServerInstance.isAirBlock(l4, i5, j5)) {
												continue label274;
											}
										}
									}
								}

								d4 = i3 + 0.5D - par1Entity.posY;
								d3 = d1 * d1 + d4 * d4 + d2 * d2;

								if (d0 < 0.0D || d3 < d0) {
									d0 = d3;
									l = i2;
									i1 = i3;
									j1 = k2;
									k1 = j3 % 4;
								}
							}
						}
					}
				}
			}

			if (d0 < 0.0D) {
				for (i2 = i - b0; i2 <= i + b0; ++i2) {
					d1 = i2 + 0.5D - par1Entity.posX;

					for (k2 = k - b0; k2 <= k + b0; ++k2) {
						d2 = k2 + 0.5D - par1Entity.posZ;
						label222:

						for (i3 = this.worldServerInstance.getActualHeight() - 1; i3 >= 0; --i3) {
							if (this.worldServerInstance.isAirBlock(i2, i3, k2)) {
								while (i3 > 0 && this.worldServerInstance.isAirBlock(i2, i3 - 1, k2)) {
									--i3;
								}

								for (j3 = l1; j3 < l1 + 2; ++j3) {
									k3 = j3 % 2;
									l3 = 1 - k3;

									for (i4 = 0; i4 < 4; ++i4) {
										for (j4 = -1; j4 < 4; ++j4) {
											k4 = i2 + (i4 - 1) * k3;
											l4 = i3 + j4;
											i5 = k2 + (i4 - 1) * l3;

											if (j4 < 0
													&& !this.worldServerInstance.getBlock(k4, l4, i5).getMaterial()
															.isSolid()
													|| j4 >= 0 && !this.worldServerInstance.isAirBlock(k4, l4, i5)) {
												continue label222;
											}
										}
									}

									d4 = i3 + 0.5D - par1Entity.posY;
									d3 = d1 * d1 + d4 * d4 + d2 * d2;

									if (d0 < 0.0D || d3 < d0) {
										d0 = d3;
										l = i2;
										i1 = i3;
										j1 = k2;
										k1 = j3 % 2;
									}
								}
							}
						}
					}
				}
			}

			int k5 = l;
			int j2 = i1;
			k2 = j1;
			int l5 = k1 % 2;
			int l2 = 1 - l5;

			if (k1 % 4 >= 2) {
				l5 = -l5;
				l2 = -l2;
			}

			boolean flag;

			if (d0 < 0.0D) {
				if (i1 < 70) {
					i1 = 70;
				}

				if (i1 > this.worldServerInstance.getActualHeight() - 10) {
					i1 = this.worldServerInstance.getActualHeight() - 10;
				}

				j2 = i1;

				for (i3 = -1; i3 <= 1; ++i3) {
					for (j3 = 1; j3 < 3; ++j3) {
						for (k3 = -1; k3 < 3; ++k3) {
							l3 = k5 + (j3 - 1) * l5 + i3 * l2;
							i4 = j2 + k3;
							j4 = k2 + (j3 - 1) * l2 - i3 * l5;
							flag = k3 < 0;
							this.worldServerInstance.setBlock(l3, i4, j4, flag ? blockPortalFrame : Blocks.air);
						}
					}
				}
			}

			for (i3 = 0; i3 < 4; ++i3) {
				for (j3 = 0; j3 < 4; ++j3) {
					for (k3 = -1; k3 < 4; ++k3) {
						l3 = k5 + (j3 - 1) * l5;
						i4 = j2 + k3;
						j4 = k2 + (j3 - 1) * l2;
						flag = j3 == 0 || j3 == 3 || k3 == -1 || k3 == 3;
						this.worldServerInstance.setBlock(l3, i4, j4, flag ? blockPortalFrame : portalBlock, 0, 2);
					}
				}

				for (j3 = 0; j3 < 4; ++j3) {
					for (k3 = -1; k3 < 4; ++k3) {
						l3 = k5 + (j3 - 1) * l5;
						i4 = j2 + k3;
						j4 = k2 + (j3 - 1) * l2;
						this.worldServerInstance.notifyBlocksOfNeighborChange(l3, i4, j4,
								this.worldServerInstance.getBlock(l3, i4, j4));
					}
				}
			}

			return true;
		}

		/**
		 * called periodically to remove out-of-date portal locations from the
		 * cache list. Argument par1 is a WorldServer.getTotalWorldTime() value.
		 */
		@Override
		public void removeStalePortalLocations(long par1) {
			if (par1 % 100L == 0L) {
				@SuppressWarnings("rawtypes")
				Iterator iterator = this.destinationCoordinateKeys.iterator();
				long j = par1 - 600L;

				while (iterator.hasNext()) {
					Long olong = (Long) iterator.next();
					Teleporter.PortalPosition portalposition = (Teleporter.PortalPosition) this.destinationCoordinateCache
							.getValueByKey(olong.longValue());

					if (portalposition == null || portalposition.lastUpdateTime < j) {
						iterator.remove();
						this.destinationCoordinateCache.remove(olong.longValue());
					}
				}
			}
		}

		public class PortalPosition extends ChunkCoordinates {
			/**
			 * The worldtime at which this PortalPosition was last verified
			 */
			public long lastUpdateTime;

			public PortalPosition(int par2, int par3, int par4, long par5) {
				super(par2, par3, par4);
				this.lastUpdateTime = par5;
			}
		}
	}

	public static class ChunkProviderModded implements IChunkProvider {
		private Random rand;
		private NoiseGeneratorOctaves field_147431_j;
		private NoiseGeneratorOctaves field_147432_k;
		private NoiseGeneratorOctaves field_147429_l;
		private NoiseGeneratorPerlin field_147430_m;
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
		private WorldType field_147435_p;
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
		private MapGenVillage villageGenerator = new MapGenVillage();
		/**
		 * Holds Mineshaft Generator
		 */
		private MapGenMineshaft mineshaftGenerator = new MapGenMineshaft();
		private MapGenScatteredFeature scatteredFeatureGenerator = new MapGenScatteredFeature();
		/**
		 * Holds ravine generator
		 */
		private MapGenBase ravineGenerator = new MapGenRavine();
		/**
		 * The biomes that are used to generate the chunk
		 */
		private BiomeGenBase[] biomesForGeneration;
		double[] field_147427_d;
		double[] field_147428_e;
		double[] field_147425_f;
		double[] field_147426_g;
		int[][] field_73219_j = new int[32][32];
		{
			caveGenerator = TerrainGen.getModdedMapGen(caveGenerator,
					net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.CAVE);
			/*
			 * strongholdGenerator = (MapGenStronghold)
			 * TerrainGen.getModdedMapGen(strongholdGenerator,
			 * net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.
			 * STRONGHOLD);
			 */
			villageGenerator = (MapGenVillage) TerrainGen.getModdedMapGen(villageGenerator,
					net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.VILLAGE);
			mineshaftGenerator = (MapGenMineshaft) TerrainGen.getModdedMapGen(mineshaftGenerator,
					net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.MINESHAFT);
			scatteredFeatureGenerator = (MapGenScatteredFeature) TerrainGen.getModdedMapGen(scatteredFeatureGenerator,
					net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.SCATTERED_FEATURE);
			ravineGenerator = TerrainGen.getModdedMapGen(ravineGenerator,
					net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.RAVINE);
		}

		public ChunkProviderModded(World par1World, long par2) {
			this.worldObj = par1World;
			this.field_147435_p = par1World.getWorldInfo().getTerrainType();
			this.rand = new Random(par2);
			this.field_147431_j = new NoiseGeneratorOctaves(this.rand, 16);
			this.field_147432_k = new NoiseGeneratorOctaves(this.rand, 16);
			this.field_147429_l = new NoiseGeneratorOctaves(this.rand, 8);
			this.field_147430_m = new NoiseGeneratorPerlin(this.rand, 4);
			this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 10);
			this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 16);
			this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.rand, 8);
			this.field_147434_q = new double[825];
			this.parabolicField = new float[25];

			for (int j = -2; j <= 2; ++j) {
				for (int k = -2; k <= 2; ++k) {
					float f = 10.0F / MathHelper.sqrt_float(j * j + k * k + 0.2F);
					this.parabolicField[j + 2 + (k + 2) * 5] = f;
				}
			}

			NoiseGenerator[] noiseGens = { field_147431_j, field_147432_k, field_147429_l, field_147430_m, noiseGen5,
					noiseGen6, mobSpawnerNoise };
			noiseGens = TerrainGen.getModdedNoiseGenerators(par1World, this.rand, noiseGens);
			this.field_147431_j = (NoiseGeneratorOctaves) noiseGens[0];
			this.field_147432_k = (NoiseGeneratorOctaves) noiseGens[1];
			this.field_147429_l = (NoiseGeneratorOctaves) noiseGens[2];
			this.field_147430_m = (NoiseGeneratorPerlin) noiseGens[3];
			this.noiseGen5 = (NoiseGeneratorOctaves) noiseGens[4];
			this.noiseGen6 = (NoiseGeneratorOctaves) noiseGens[5];
			this.mobSpawnerNoise = (NoiseGeneratorOctaves) noiseGens[6];
		}

		public void func_147424_a(int p_147424_1_, int p_147424_2_, Block[] p_147424_3_) {
			byte b0 = 63;
			this.biomesForGeneration = this.worldObj.getWorldChunkManager()
					.getBiomesForGeneration(this.biomesForGeneration, p_147424_1_ * 4 - 2, p_147424_2_ * 4 - 2, 10, 10);
			this.func_147423_a(p_147424_1_ * 4, 0, p_147424_2_ * 4);

			for (int k = 0; k < 4; ++k) {
				int l = k * 5;
				int i1 = (k + 1) * 5;

				for (int j1 = 0; j1 < 4; ++j1) {
					int k1 = (l + j1) * 33;
					int l1 = (l + j1 + 1) * 33;
					int i2 = (i1 + j1) * 33;
					int j2 = (i1 + j1 + 1) * 33;

					for (int k2 = 0; k2 < 32; ++k2) {
						double d0 = 0.125D;
						double d1 = this.field_147434_q[k1 + k2];
						double d2 = this.field_147434_q[l1 + k2];
						double d3 = this.field_147434_q[i2 + k2];
						double d4 = this.field_147434_q[j2 + k2];
						double d5 = (this.field_147434_q[k1 + k2 + 1] - d1) * d0;
						double d6 = (this.field_147434_q[l1 + k2 + 1] - d2) * d0;
						double d7 = (this.field_147434_q[i2 + k2 + 1] - d3) * d0;
						double d8 = (this.field_147434_q[j2 + k2 + 1] - d4) * d0;

						for (int l2 = 0; l2 < 8; ++l2) {
							double d9 = 0.25D;
							double d10 = d1;
							double d11 = d2;
							double d12 = (d3 - d1) * d9;
							double d13 = (d4 - d2) * d9;

							for (int i3 = 0; i3 < 4; ++i3) {
								int j3 = i3 + k * 4 << 12 | 0 + j1 * 4 << 8 | k2 * 8 + l2;
								short short1 = 256;
								j3 -= short1;
								double d14 = 0.25D;
								double d16 = (d11 - d10) * d14;
								double d15 = d10 - d16;

								for (int k3 = 0; k3 < 4; ++k3) {
									if ((d15 += d16) > 0.0D) {
										p_147424_3_[j3 += short1] = blockMainFiller;
									}
									else if (k2 * 8 + l2 < b0) {
										p_147424_3_[j3 += short1] = blockFluidLakes;
									}
									else {
										p_147424_3_[j3 += short1] = null;
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
			@SuppressWarnings("deprecation")
			ChunkProviderEvent.ReplaceBiomeBlocks event = new ChunkProviderEvent.ReplaceBiomeBlocks(this, p_147422_1_,
					p_147422_2_, p_147422_3_, p_147422_5_);
			MinecraftForge.EVENT_BUS.post(event);
			if (event.getResult() == cpw.mods.fml.common.eventhandler.Event.Result.DENY)
				return;

			double d0 = 0.03125D;
			this.stoneNoise = this.field_147430_m.func_151599_a(this.stoneNoise, p_147422_1_ * 16, p_147422_2_ * 16, 16,
					16, d0 * 2.0D, d0 * 2.0D, 1.0D);

			for (int k = 0; k < 16; ++k) {
				for (int l = 0; l < 16; ++l) {
					BiomeGenBase biomegenbase = p_147422_5_[l + k * 16];
					biomegenbase.genTerrainBlocks(this.worldObj, this.rand, p_147422_3_, p_147422_4_,
							p_147422_1_ * 16 + k, p_147422_2_ * 16 + l, this.stoneNoise[l + k * 16]);
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
		 * Will return back a chunk, if it doesn't exist and its not a MP client
		 * it will generates all the blocks for the specified chunk from the map
		 * seed and chunk seed
		 */
		@Override
		public Chunk provideChunk(int par1, int par2) {
			this.rand.setSeed(par1 * 341873128712L + par2 * 132897987541L);
			Block[] ablock = new Block[65536];
			byte[] abyte = new byte[65536];
			this.func_147424_a(par1, par2, ablock);
			this.biomesForGeneration = this.worldObj.getWorldChunkManager()
					.loadBlockGeneratorData(this.biomesForGeneration, par1 * 16, par2 * 16, 16, 16);
			this.replaceBlocksForBiome(par1, par2, ablock, abyte, this.biomesForGeneration);

			Chunk chunk = new Chunk(this.worldObj, ablock, abyte, par1, par2);
			byte[] abyte1 = chunk.getBiomeArray();

			for (int k = 0; k < abyte1.length; ++k) {
				abyte1[k] = (byte) this.biomesForGeneration[k].biomeID;
			}

			chunk.generateSkylightMap();
			return chunk;
		}

		private void func_147423_a(int p_147423_1_, int p_147423_2_, int p_147423_3_) {
			this.field_147426_g = this.noiseGen6.generateNoiseOctaves(this.field_147426_g, p_147423_1_, p_147423_3_, 5,
					5, 200.0D, 200.0D, 0.5D);
			this.field_147427_d = this.field_147429_l.generateNoiseOctaves(this.field_147427_d, p_147423_1_,
					p_147423_2_, p_147423_3_, 5, 33, 5, 8.555150000000001D, 4.277575000000001D, 8.555150000000001D);
			this.field_147428_e = this.field_147431_j.generateNoiseOctaves(this.field_147428_e, p_147423_1_,
					p_147423_2_, p_147423_3_, 5, 33, 5, 684.412D, 684.412D, 684.412D);
			this.field_147425_f = this.field_147432_k.generateNoiseOctaves(this.field_147425_f, p_147423_1_,
					p_147423_2_, p_147423_3_, 5, 33, 5, 684.412D, 684.412D, 684.412D);
			int l = 0;
			int i1 = 0;
			for (int j1 = 0; j1 < 5; ++j1) {
				for (int k1 = 0; k1 < 5; ++k1) {
					float f = 0.0F;
					float f1 = 0.0F;
					float f2 = 0.0F;
					byte b0 = 2;
					BiomeGenBase biomegenbase = this.biomesForGeneration[j1 + 2 + (k1 + 2) * 10];

					for (int l1 = -b0; l1 <= b0; ++l1) {
						for (int i2 = -b0; i2 <= b0; ++i2) {
							BiomeGenBase biomegenbase1 = this.biomesForGeneration[j1 + l1 + 2 + (k1 + i2 + 2) * 10];
							float f3 = biomegenbase1.rootHeight;
							float f4 = biomegenbase1.heightVariation;

							if (this.field_147435_p == WorldType.AMPLIFIED && f3 > 0.0F) {
								f3 = 1.0F + f3 * 2.0F;
								f4 = 1.0F + f4 * 4.0F;
							}

							float f5 = this.parabolicField[l1 + 2 + (i2 + 2) * 5] / (f3 + 2.0F);

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
					double d13 = this.field_147426_g[i1] / 8000.0D;

					if (d13 < 0.0D) {
						d13 = -d13 * 0.3D;
					}

					d13 = d13 * 3.0D - 2.0D;

					if (d13 < 0.0D) {
						d13 /= 2.0D;

						if (d13 < -1.0D) {
							d13 = -1.0D;
						}

						d13 /= 1.4D;
						d13 /= 2.0D;
					}
					else {
						if (d13 > 1.0D) {
							d13 = 1.0D;
						}

						d13 /= 8.0D;
					}

					++i1;
					double d12 = f1;
					double d14 = f;
					d12 += d13 * 0.2D;
					d12 = d12 * 8.5D / 8.0D;
					double d5 = 8.5D + d12 * 4.0D;

					for (int j2 = 0; j2 < 33; ++j2) {
						double d6 = (j2 - d5) * 12.0D * 128.0D / 256.0D / d14;

						if (d6 < 0.0D) {
							d6 *= 4.0D;
						}

						double d7 = this.field_147428_e[l] / 512.0D;
						double d8 = this.field_147425_f[l] / 512.0D;
						double d9 = (this.field_147427_d[l] / 10.0D + 1.0D) / 2.0D;
						double d10 = MathHelper.denormalizeClamp(d7, d8, d9) - d6;

						if (j2 > 29) {
							double d11 = (j2 - 29) / 3.0F;
							d10 = d10 * (1.0D - d11) + -10.0D * d11;
						}

						this.field_147434_q[l] = d10;
						++l;
					}
				}
			}
		}

		/**
		 * Checks to see if a chunk exists at x, y
		 */
		@Override
		public boolean chunkExists(int par1, int par2) {
			return true;
		}

		/**
		 * Populates chunk with ores etc etc
		 */
		@Override
		public void populate(IChunkProvider par1IChunkProvider, int par2, int par3) {
			BlockFalling.fallInstantly = true;
			int k = par2 * 16;
			int l = par3 * 16;
			BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(k + 16, l + 16);
			this.rand.setSeed(this.worldObj.getSeed());
			long i1 = this.rand.nextLong() / 2L * 2L + 1L;
			long j1 = this.rand.nextLong() / 2L * 2L + 1L;
			this.rand.setSeed(par2 * i1 + par3 * j1 ^ this.worldObj.getSeed());
			boolean flag = false;

			MinecraftForge.EVENT_BUS
					.post(new PopulateChunkEvent.Pre(par1IChunkProvider, worldObj, rand, par2, par3, flag));

			int k1;
			int l1;
			int i2;

			if (biomegenbase != BiomeGenBase.desert && biomegenbase != BiomeGenBase.desertHills && !flag
					&& this.rand.nextInt(4) == 0 && TerrainGen.populate(par1IChunkProvider, worldObj, rand, par2, par3,
							flag, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAKE)) {
				k1 = k + this.rand.nextInt(16) + 8;
				l1 = this.rand.nextInt(256);
				i2 = l + this.rand.nextInt(16) + 8;
				(new WorldGenLakes(blockFluidLakes)).generate(this.worldObj, this.rand, k1, l1, i2);
			}

			if (TerrainGen.populate(par1IChunkProvider, worldObj, rand, par2, par3, flag,
					net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAVA) && !flag
					&& this.rand.nextInt(8) == 0) {
				k1 = k + this.rand.nextInt(16) + 8;
				l1 = this.rand.nextInt(this.rand.nextInt(248) + 8);
				i2 = l + this.rand.nextInt(16) + 8;

				if (l1 < 63 || this.rand.nextInt(10) == 0) {
					(new WorldGenLakes(blockFluidLakes)).generate(this.worldObj, this.rand, k1, l1, i2);
				}
			}
			biomegenbase.decorate(this.worldObj, this.rand, k, l);
			SpawnerAnimals.performWorldGenSpawning(this.worldObj, biomegenbase, k + 8, l + 8, 16, 16, this.rand);
			k += 8;
			l += 8;

			MinecraftForge.EVENT_BUS
					.post(new PopulateChunkEvent.Post(par1IChunkProvider, worldObj, rand, par2, par3, flag));

			BlockFalling.fallInstantly = false;
		}

		/**
		 * Two modes of operation: if passed true, save all Chunks in one go. If
		 * passed false, save up to two chunks. Return true if all chunks have
		 * been saved.
		 */
		@Override
		public boolean saveChunks(boolean par1, IProgressUpdate par2IProgressUpdate) {
			return true;
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
			return false;
		}

		/**
		 * Returns if the IChunkProvider supports saving.
		 */
		@Override
		public boolean canSave() {
			return true;
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
			return par1EnumCreatureType == EnumCreatureType.monster
					&& this.scatteredFeatureGenerator.func_143030_a(par2, par3, par4)
							? this.scatteredFeatureGenerator.getScatteredFeatureSpawnList()
							: biomegenbase.getSpawnableList(par1EnumCreatureType);
		}

		@Override
		public ChunkPosition func_147416_a(World p_147416_1_, String p_147416_2_, int p_147416_3_, int p_147416_4_, int p_147416_5_) {
			return /*
					 * "Stronghold".equals(p_147416_2_) &&
					 * this.strongholdGenerator != null ?
					 * this.strongholdGenerator.func_151545_a(p_147416_1_,
					 * p_147416_3_, p_147416_4_, p_147416_5_) :
					 */ null;
		}

		@Override
		public int getLoadedChunkCount() {
			return 0;
		}

		@Override
		public void recreateStructures(int par1, int par2) {

		}
	}

	public static class WorldChunkManagerCustom extends WorldChunkManager {

		private GenLayer genBiomes;
		/** A GenLayer containing the indices into BiomeGenBase.biomeList[] */
		private GenLayer biomeIndexLayer;
		/** The BiomeCache object for this world. */
		private BiomeCache biomeCache;
		/** A list of biomes that the player can spawn in. */
		private List<BiomeGenBase> biomesToSpawnIn;

		@SuppressWarnings({ "rawtypes" })
		public WorldChunkManagerCustom() {
			this.biomeCache = new BiomeCache(this);
			this.biomesToSpawnIn = new ArrayList();
			this.biomesToSpawnIn.addAll(allowedBiomes); // TODO
		}

		public WorldChunkManagerCustom(long seed, WorldType worldType) {
			this();
			// i changed this to my GenLayerDarkWorld
			GenLayer[] agenlayer = GenLayerDarkWorld.makeTheWorld(seed, worldType);

			agenlayer = getModdedBiomeGenerators(worldType, seed, agenlayer);
			this.genBiomes = agenlayer[0];
			this.biomeIndexLayer = agenlayer[1];

		}

		public WorldChunkManagerCustom(World world) {
			this(world.getSeed(), world.getWorldInfo().getTerrainType());

		}

		/**
		 * Gets the list of valid biomes for the player to spawn in.
		 */
		@Override
		public List<BiomeGenBase> getBiomesToSpawnIn() {
			return this.biomesToSpawnIn;
		}

		/**
		 * Returns a list of rainfall values for the specified blocks. Args:
		 * listToReuse, x, z, width, length.
		 */
		@Override
		public float[] getRainfall(float[] listToReuse, int x, int z, int width, int length) {
			IntCache.resetIntCache();

			if (listToReuse == null || listToReuse.length < width * length) {
				listToReuse = new float[width * length];
			}

			int[] aint = this.biomeIndexLayer.getInts(x, z, width, length);

			for (int i1 = 0; i1 < width * length; ++i1) {
				try {
					float f = BiomeGenBase.getBiome(aint[i1]).getIntRainfall() / 65536.0F;

					if (f > 1.0F) {
						f = 1.0F;
					}

					listToReuse[i1] = f;
				}
				catch (Throwable throwable) {
					CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
					CrashReportCategory crashreportcategory = crashreport.makeCategory("DownfallBlock");
					crashreportcategory.addCrashSection("biome id", Integer.valueOf(i1));
					crashreportcategory.addCrashSection("downfalls[] size", Integer.valueOf(listToReuse.length));
					crashreportcategory.addCrashSection("x", Integer.valueOf(x));
					crashreportcategory.addCrashSection("z", Integer.valueOf(z));
					crashreportcategory.addCrashSection("w", Integer.valueOf(width));
					crashreportcategory.addCrashSection("h", Integer.valueOf(length));
					throw new ReportedException(crashreport);
				}
			}

			return listToReuse;
		}

		/**
		 * Return an adjusted version of a given temperature based on the y
		 * height
		 */
		@Override
		@SideOnly(Side.CLIENT)
		public float getTemperatureAtHeight(float par1, int par2) {
			return par1;
		}

		/**
		 * Returns an array of biomes for the location input.
		 */
		@Override
		public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] par1ArrayOfBiomeGenBase, int par2, int par3, int par4, int par5) {
			IntCache.resetIntCache();

			if (par1ArrayOfBiomeGenBase == null || par1ArrayOfBiomeGenBase.length < par4 * par5) {
				par1ArrayOfBiomeGenBase = new BiomeGenBase[par4 * par5];
			}

			int[] aint = this.genBiomes.getInts(par2, par3, par4, par5);

			try {
				for (int i = 0; i < par4 * par5; ++i) {
					par1ArrayOfBiomeGenBase[i] = BiomeGenBase.getBiome(aint[i]);
				}

				return par1ArrayOfBiomeGenBase;
			}
			catch (Throwable throwable) {
				CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
				CrashReportCategory crashreportcategory = crashreport.makeCategory("RawBiomeBlock");
				crashreportcategory.addCrashSection("biomes[] size", Integer.valueOf(par1ArrayOfBiomeGenBase.length));
				crashreportcategory.addCrashSection("x", Integer.valueOf(par2));
				crashreportcategory.addCrashSection("z", Integer.valueOf(par3));
				crashreportcategory.addCrashSection("w", Integer.valueOf(par4));
				crashreportcategory.addCrashSection("h", Integer.valueOf(par5));
				throw new ReportedException(crashreport);
			}
		}

		/**
		 * Returns biomes to use for the blocks and loads the other data like
		 * temperature and humidity onto the WorldChunkManager Args:
		 * oldBiomeList, x, z, width, depth
		 */
		@Override
		public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] oldBiomeList, int x, int z, int width, int depth) {
			return this.getBiomeGenAt(oldBiomeList, x, z, width, depth, true);
		}

		/**
		 * Return a list of biomes for the specified blocks. Args: listToReuse,
		 * x, y, width, length, cacheFlag (if false, don't check biomeCache to
		 * avoid infinite loop in BiomeCacheBlock)
		 */
		@Override
		public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] listToReuse, int x, int y, int width, int length, boolean cacheFlag) {
			IntCache.resetIntCache();

			if (listToReuse == null || listToReuse.length < width * length) {
				listToReuse = new BiomeGenBase[width * length];
			}

			if (cacheFlag && width == 16 && length == 16 && (x & 15) == 0 && (y & 15) == 0) {
				BiomeGenBase[] abiomegenbase1 = this.biomeCache.getCachedBiomes(x, y);
				System.arraycopy(abiomegenbase1, 0, listToReuse, 0, width * length);
				return listToReuse;
			}
			else {
				int[] aint = this.biomeIndexLayer.getInts(x, y, width, length);

				for (int i = 0; i < width * length; ++i) {
					listToReuse[i] = BiomeGenBase.getBiome(aint[i]);
				}
				return listToReuse;
			}
		}

		/**
		 * checks given Chunk's Biomes against List of allowed ones
		 */
		@Override
		@SuppressWarnings("rawtypes")
		public boolean areBiomesViable(int x, int y, int z, List par4List) {
			IntCache.resetIntCache();
			int l = x - z >> 2;
			int i1 = y - z >> 2;
			int j1 = x + z >> 2;
			int k1 = y + z >> 2;
			int l1 = j1 - l + 1;
			int i2 = k1 - i1 + 1;
			int[] aint = this.genBiomes.getInts(l, i1, l1, i2);

			try {
				for (int j2 = 0; j2 < l1 * i2; ++j2) {
					BiomeGenBase biomegenbase = BiomeGenBase.getBiome(aint[j2]);

					if (!par4List.contains(biomegenbase)) {
						return false;
					}
				}

				return true;
			}
			catch (Throwable throwable) {
				CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
				CrashReportCategory crashreportcategory = crashreport.makeCategory("Layer");
				crashreportcategory.addCrashSection("Layer", this.genBiomes.toString());
				crashreportcategory.addCrashSection("x", Integer.valueOf(x));
				crashreportcategory.addCrashSection("z", Integer.valueOf(y));
				crashreportcategory.addCrashSection("radius", Integer.valueOf(z));
				crashreportcategory.addCrashSection("allowed", par4List);
				throw new ReportedException(crashreport);
			}
		}

		/**
		 * Finds a valid position within a range, that is in one of the listed
		 * biomes. Searches {par1,par2} +-par3 blocks. Strongly favors positive
		 * y positions.
		 */
		@Override
		@SuppressWarnings("rawtypes")
		public ChunkPosition findBiomePosition(int p_150795_1_, int p_150795_2_, int p_150795_3_, List p_150795_4_, Random p_150795_5_) {
			IntCache.resetIntCache();
			int l = p_150795_1_ - p_150795_3_ >> 2;
			int i1 = p_150795_2_ - p_150795_3_ >> 2;
			int j1 = p_150795_1_ + p_150795_3_ >> 2;
			int k1 = p_150795_2_ + p_150795_3_ >> 2;
			int l1 = j1 - l + 1;
			int i2 = k1 - i1 + 1;
			int[] aint = this.genBiomes.getInts(l, i1, l1, i2);
			ChunkPosition chunkposition = null;
			int j2 = 0;

			for (int k2 = 0; k2 < l1 * i2; ++k2) {
				int l2 = l + k2 % l1 << 2;
				int i3 = i1 + k2 / l1 << 2;
				BiomeGenBase biomegenbase = BiomeGenBase.getBiome(aint[k2]);

				if (p_150795_4_.contains(biomegenbase) && (chunkposition == null || p_150795_5_.nextInt(j2 + 1) == 0)) {
					chunkposition = new ChunkPosition(l2, 0, i3);
					++j2;
				}
			}

			return chunkposition;
		}

		/**
		 * Calls the WorldChunkManager's biomeCache.cleanupCache()
		 */
		@Override
		public void cleanupCache() {
			this.biomeCache.cleanupCache();
		}
	}

	public static class GenLayerDarkWorld extends GenLayer {

		public GenLayerDarkWorld(long seed) {
			super(seed);
		}

		public static GenLayer[] makeTheWorld(long seed, WorldType type) {
			GenLayer biomes = new GenLayerBiomes(1L);
			biomes = new GenLayerZoom(1000L, biomes);
			biomes = new GenLayerZoom(1001L, biomes);
			biomes = new GenLayerZoom(1002L, biomes);
			biomes = new GenLayerZoom(1003L, biomes);
			biomes = new GenLayerZoom(1004L, biomes);
			biomes = new GenLayerZoom(1005L, biomes);
			GenLayer genlayervoronoizoom = new GenLayerVoronoiZoom(10L, biomes);
			biomes.initWorldGenSeed(seed);
			genlayervoronoizoom.initWorldGenSeed(seed);
			return new GenLayer[] { biomes, genlayervoronoizoom };
		}

		@Override
		public int[] getInts(int p_75904_1_, int p_75904_2_, int p_75904_3_, int p_75904_4_) {
			return null;
		}
	}

	public static class GenLayerBiomes extends GenLayer {

		protected BiomeGenBase[] allowedBiomes = { Biome_DarkWorld.biome, };

		public GenLayerBiomes(long seed) {
			super(seed);
		}

		public GenLayerBiomes(long seed, GenLayer genlayer) {
			super(seed);
			this.parent = genlayer;
		}

		@Override
		public int[] getInts(int x, int z, int width, int depth) {
			int[] dest = IntCache.getIntCache(width * depth);
			for (int dz = 0; dz < depth; dz++) {
				for (int dx = 0; dx < width; dx++) {
					this.initChunkSeed(dx + x, dz + z);
					dest[(dx + dz * width)] = this.allowedBiomes[nextInt(this.allowedBiomes.length)].biomeID;
				}
			}
			return dest;
		}
	}

}
