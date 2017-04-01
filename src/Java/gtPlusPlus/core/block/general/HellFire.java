package gtPlusPlus.core.block.general;

import static net.minecraftforge.common.util.ForgeDirection.*;

import java.util.IdentityHashMap;
import java.util.Map.Entry;
import java.util.Random;

import com.google.common.collect.Maps;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.objects.XSTR;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class HellFire extends BlockFire {
	@Deprecated
	private final int[] field_149849_a = new int[4096];
	@Deprecated
	private final int[] field_149848_b = new int[4096];
	@SideOnly(Side.CLIENT)
	private IIcon[] IIconArray;

	public HellFire() {
		this.setTickRandomly(true);
		this.setBlockTextureName(CORE.MODID + "hellfire/blockHellFire");
		this.setBlockName("blockHellFire");
		this.setCreativeTab(AddToCreativeTab.tabBlock);
		GameRegistry.registerBlock(this, "blockHellFire");
		LanguageRegistry.addName(this, "Hellish Fire");
		enableBrutalFire();
		this.setLightLevel(15);
	}

	private void enableBrutalFire() {
		for (Object o : Block.blockRegistry.getKeys())
	      {
	        String name = (String)o;
	        Block b = Block.getBlockFromName(name);
	        if (b != Blocks.air)
	        {
	          int spread = Blocks.fire.getEncouragement(b);
	          int flamm = Blocks.fire.getFlammability(b);
	          this.setFireInfo(b, spread * 4, flamm * 4);
	        }
	      }
		
	}

	/**
	 * How many world ticks before ticking
	 */
	@Override
	public int tickRate(final World world) {
		return 5;
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	@Override
	public void updateTick(final World world, final int x, final int y, final int z, Random random) {
		
		random = new XSTR();

		if (world.getGameRules().getGameRuleBooleanValue("doFireTick")) {
			final boolean flag = world.getBlock(x, y - 1, z).isFireSource(world, x, y - 1, z, UP);

			if (!this.canPlaceBlockAt(world, x, y, z)) {
				world.setBlockToAir(x, y, z);
			}

			if (!flag && world.isRaining()
					&& (world.canLightningStrikeAt(x, y, z) || world.canLightningStrikeAt(x - 1, y, z)
							|| world.canLightningStrikeAt(x + 1, y, z) || world.canLightningStrikeAt(x, y, z - 1)
							|| world.canLightningStrikeAt(x, y, z + 1))) {
				
				if (MathUtils.randInt(0, 100) >= 90){
					world.setBlockToAir(x, y, z);
				}
			}
			else {
				final int blockMeta = world.getBlockMetadata(x, y, z);

				if (blockMeta < 15) {
					world.setBlockMetadataWithNotify(x, y, z, blockMeta + (random.nextInt(3) / 2), 4);
				}

				world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world) + random.nextInt(10));

				if (!flag && !this.canNeighborBurn(world, x, y, z)) {
					if (!World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) || (blockMeta > 3)) {
						world.setBlockToAir(x, y, z);
					}
				}
				else if (!flag && !this.canCatchFire(world, x, y - 1, z, UP) && (blockMeta == 15) && (random.nextInt(4) == 0)) {
					world.setBlockToAir(x, y, z);
				}
				else {
					final boolean flag1 = world.isBlockHighHumidity(x, y, z);
					byte b0 = 0;

					if (flag1) {
						b0 = -50;
					}

					this.tryCatchFire(world, x + 1, y, z, 300 + b0, random, blockMeta, WEST);
					this.tryCatchFire(world, x - 1, y, z, 300 + b0, random, blockMeta, EAST);
					this.tryCatchFire(world, x, y - 1, z, 250 + b0, random, blockMeta, UP);
					this.tryCatchFire(world, x, y + 1, z, 250 + b0, random, blockMeta, DOWN);
					this.tryCatchFire(world, x, y, z - 1, 300 + b0, random, blockMeta, SOUTH);
					this.tryCatchFire(world, x, y, z + 1, 300 + b0, random, blockMeta, NORTH);

					for (int i1 = x - 1; i1 <= (x + 1); ++i1) {
						for (int j1 = z - 1; j1 <= (z + 1); ++j1) {
							for (int k1 = y - 1; k1 <= (y + 4); ++k1) {
								if ((i1 != x) || (k1 != y) || (j1 != z)) {
									int l1 = 100;

									if (k1 > (y + 1)) {
										l1 += (k1 - (y + 1)) * 100;
									}

									final int neighbourFireChance = this.getChanceOfNeighborsEncouragingFire(world, i1, k1, j1);

									if (neighbourFireChance > 0) {
										int j2 = (neighbourFireChance + 40 + (world.difficultySetting.getDifficultyId() * 14)) / (blockMeta + 30);

										if (flag1) {
											j2 /= 2;
										}

										if ((j2 > 0) && (random.nextInt(l1) <= j2)
												&& (!world.isRaining() || !world.canLightningStrikeAt(i1, k1, j1))
												&& !world.canLightningStrikeAt(i1 - 1, k1, z)
												&& !world.canLightningStrikeAt(i1 + 1, k1, j1)
												&& !world.canLightningStrikeAt(i1, k1, j1 - 1)
												&& !world.canLightningStrikeAt(i1, k1, j1 + 1)) {
											int k2 = blockMeta + (random.nextInt(5) / 4);

											if (k2 > 15) {
												k2 = 15;
											}

											world.setBlock(i1, k1, j1, this, k2, 3);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private void tryCatchFire(final World world, final int p_149841_2_, final int p_149841_3_, final int p_149841_4_, final int p_149841_5_, final Random p_149841_6_, final int p_149841_7_, final ForgeDirection face) {
		final int j1 = world.getBlock(p_149841_2_, p_149841_3_, p_149841_4_).getFlammability(world, p_149841_2_,
				p_149841_3_, p_149841_4_, face);

		if (p_149841_6_.nextInt(p_149841_5_) < j1) {
			final boolean flag = world.getBlock(p_149841_2_, p_149841_3_, p_149841_4_) == Blocks.tnt;

			if ((p_149841_6_.nextInt(p_149841_7_ + 10) < 5)
					&& !world.canLightningStrikeAt(p_149841_2_, p_149841_3_, p_149841_4_)) {
				int k1 = p_149841_7_ + (p_149841_6_.nextInt(5) / 4);

				if (k1 > 15) {
					k1 = 15;
				}

				world.setBlock(p_149841_2_, p_149841_3_, p_149841_4_, this, k1, 3);
			}
			else {
				world.setBlockToAir(p_149841_2_, p_149841_3_, p_149841_4_);
			}

			if (flag) {
				Blocks.tnt.onBlockDestroyedByPlayer(world, p_149841_2_, p_149841_3_, p_149841_4_, 1);
			}
		}
	}

	/**
	 * Returns true if at least one block next to this one can burn.
	 */
	private boolean canNeighborBurn(final World world, final int x, final int y, final int z) {
		return this.canCatchFire(world, x + 1, y, z, WEST)
				|| this.canCatchFire(world, x - 1, y, z, EAST)
				|| this.canCatchFire(world, x, y - 1, z, UP)
				|| this.canCatchFire(world, x, y + 1, z, DOWN)
				|| this.canCatchFire(world, x, y, z - 1, SOUTH)
				|| this.canCatchFire(world, x, y, z + 1, NORTH);
	}

	/**
	 * Gets the highest chance of a neighbor block encouraging this block to
	 * catch fire
	 */
	private int getChanceOfNeighborsEncouragingFire(final World world, final int x, final int y, final int z) {
		final byte b0 = 0;

		if (!world.isAirBlock(x, y, z)) {
			return 0;
		}
		else {
			int l = b0;
			l = this.getChanceToEncourageFire(world, x + 1, y, z, l, WEST);
			l = this.getChanceToEncourageFire(world, x - 1, y, z, l, EAST);
			l = this.getChanceToEncourageFire(world, x, y - 1, z, l, UP);
			l = this.getChanceToEncourageFire(world, x, y + 1, z, l, DOWN);
			l = this.getChanceToEncourageFire(world, x, y, z - 1, l, SOUTH);
			l = this.getChanceToEncourageFire(world, x, y, z + 1, l, NORTH);
			return l;
		}
	}

	/**
	 * Checks the specified block coordinate to see if it can catch fire. Args:
	 * blockAccess, x, y, z
	 */
	@Override
	@Deprecated
	public boolean canBlockCatchFire(final IBlockAccess p_149844_1_, final int p_149844_2_, final int p_149844_3_, final int p_149844_4_) {
		return this.canCatchFire(p_149844_1_, p_149844_2_, p_149844_3_, p_149844_4_, UP);
	}

	/**
	 * Checks to see if its valid to put this block at the specified
	 * coordinates. Args: world, x, y, z
	 */
	@Override
	public boolean canPlaceBlockAt(final World worldObj, final int x, final int y, final int z) {
		return World.doesBlockHaveSolidTopSurface(worldObj, x, y - 1, z)
				|| this.canNeighborBurn(worldObj, x, y, z);
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which
	 * neighbor changed (coordinates passed are their own) Args: x, y, z,
	 * neighbor Block
	 */
	@Override
	public void onNeighborBlockChange(final World worldObj, final int x, final int y, final int z, final Block blockObj) {
		if (!World.doesBlockHaveSolidTopSurface(worldObj, x, y - 1, z)
				&& !this.canNeighborBurn(worldObj, x, y, z)) {
			worldObj.setBlockToAir(x, y, z);
		}
	}

	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
	@Override
	public void onBlockAdded(final World world, final int x, final int y, final int z) {
		if ((world.provider.dimensionId > 0)
				|| !Blocks.portal.func_150000_e(world, x, y, z)) {
			if (!World.doesBlockHaveSolidTopSurface(world, x, y - 1, z)
					&& !this.canNeighborBurn(world, x, y, z)) {
				world.setBlockToAir(x, y, z);
			}
			else {
				world.scheduleBlockUpdate(x, y, z, this,
						this.tickRate(world) + world.rand.nextInt(10));
			}
		}
	}
	
	//Burn
	@Override
	public void onEntityWalking(World world, int i, int j, int k, Entity entity) {
		entity.setFire(10);
	}
	
	//Burn
	@Override
	public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
		entity.setFire(10);
	}

	/**
	 * A randomly called display update to be able to add particles or other
	 * items for display
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(final World world, final int x, final int y, final int z, Random randomObj) {
		
		randomObj = new XSTR();
		
		if (randomObj.nextInt(24) == 0) {
			world.playSound(x + 0.5F, y + 0.5F, z + 0.5F, "fire.fire",
					1.0F + randomObj.nextFloat(), (randomObj.nextFloat() * 0.7F) + 0.3F, false);
		}

		int l;
		float f;
		float f1;
		float f2;

		if (!World.doesBlockHaveSolidTopSurface(world, x, y - 1, z)
				&& !Blocks.fire.canCatchFire(world, x, y - 1, z, UP)) {
			if (Blocks.fire.canCatchFire(world, x - 1, y, z, EAST)) {
				for (l = 0; l < 2; ++l) {
					f = x + (randomObj.nextFloat() * 0.1F);
					f1 = y + randomObj.nextFloat();
					f2 = z + randomObj.nextFloat();
					world.spawnParticle("hugesmoke", f, f1, f2, 0.0D, 0.0D, 0.0D);
				}
			}

			if (Blocks.fire.canCatchFire(world, x + 1, y, z, WEST)) {
				for (l = 0; l < 2; ++l) {
					f = (x + 1) - (randomObj.nextFloat() * 0.1F);
					f1 = y + randomObj.nextFloat();
					f2 = z + randomObj.nextFloat();
					world.spawnParticle("hugesmoke", f, f1, f2, 0.0D, 0.0D, 0.0D);
				}
			}

			if (Blocks.fire.canCatchFire(world, x, y, z - 1, SOUTH)) {
				for (l = 0; l < 2; ++l) {
					f = x + randomObj.nextFloat();
					f1 = y + randomObj.nextFloat();
					f2 = z + (randomObj.nextFloat() * 0.1F);
					world.spawnParticle("hugesmoke", f, f1, f2, 0.0D, 0.0D, 0.0D);
				}
			}

			if (Blocks.fire.canCatchFire(world, x, y, z + 1, NORTH)) {
				for (l = 0; l < 2; ++l) {
					f = x + randomObj.nextFloat();
					f1 = y + randomObj.nextFloat();
					f2 = (z + 1) - (randomObj.nextFloat() * 0.1F);
					world.spawnParticle("hugesmoke", f, f1, f2, 0.0D, 0.0D, 0.0D);
				}
			}

			if (Blocks.fire.canCatchFire(world, x, y + 1, z, DOWN)) {
				for (l = 0; l < 2; ++l) {
					f = x + randomObj.nextFloat();
					f1 = (y + 1) - (randomObj.nextFloat() * 0.1F);
					f2 = z + randomObj.nextFloat();
					world.spawnParticle("hugesmoke", f, f1, f2, 0.0D, 0.0D, 0.0D);
				}
			}
		}
		else {
			for (l = 0; l < 5; ++l) {
				f = x + randomObj.nextFloat();
				f1 = y + (randomObj.nextFloat() * 0.5F) + 0.5F;
				f2 = z + randomObj.nextFloat();
				world.spawnParticle("hugesmoke", f, f1, f2, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(final IIconRegister IIconRegister) {
		this.IIconArray = new IIcon[] { 
				IIconRegister.registerIcon(CORE.MODID + ":" + "hellfire/" + "blockHellFire" + "_layer_0"),
				IIconRegister.registerIcon(CORE.MODID + ":" + "hellfire/" + "blockHellFire" + "_layer_1") };
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getFireIcon(final int p_149840_1_) {
		return this.IIconArray[p_149840_1_];
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(final int p_149691_1_, final int p_149691_2_) {
		return this.IIconArray[0];
	}

	@Override
	public MapColor getMapColor(final int p_149728_1_) {
		return MapColor.snowColor;
	}

	/*
	 * ================================= Forge Start
	 * ======================================
	 */
	private static class FireInfo {
		private int encouragement = 0;
		private int flammibility = 0;
	}

	private final IdentityHashMap<Block, FireInfo> blockInfo = Maps.newIdentityHashMap();

	@Override
	public void setFireInfo(final Block block, final int encouragement, final int flammibility) {
		if (block == Blocks.air) {
			throw new IllegalArgumentException("Tried to set air on fire... This is bad.");
		}
		final int id = Block.getIdFromBlock(block);
		this.field_149849_a[id] = encouragement;
		this.field_149848_b[id] = flammibility;

		final FireInfo info = this.getInfo(block, true);
		info.encouragement = encouragement;
		info.flammibility = flammibility;
	}

	private FireInfo getInfo(final Block block, final boolean garentee) {
		FireInfo ret = this.blockInfo.get(block);
		if ((ret == null) && garentee) {
			ret = new FireInfo();
			this.blockInfo.put(block, ret);
		}
		return ret;
	}

	@Override
	public void rebuildFireInfo() {
		for (int x = 0; x < 4096; x++) {
			// If we care.. we could detect changes in here and make sure we
			// keep them, however
			// it's my thinking that anyone who hacks into the private variables
			// should DIAF and we don't care about them.
			this.field_149849_a[x] = 0;
			this.field_149848_b[x] = 0;
		}

		for (final Entry<Block, FireInfo> e : this.blockInfo.entrySet()) {
			final int id = Block.getIdFromBlock(e.getKey());
			if ((id >= 0) && (id < 4096)) {
				this.field_149849_a[id] = e.getValue().encouragement;
				this.field_149848_b[id] = e.getValue().flammibility;
			}
		}
	}

	@Override
	public int getFlammability(final Block block) {
		final int id = Block.getIdFromBlock(block);
		return (id >= 0) && (id < 4096) ? this.field_149848_b[id] : 0;
	}

	@Override
	public int getEncouragement(final Block block) {
		final int id = Block.getIdFromBlock(block);
		return (id >= 0) && (id < 4096) ? this.field_149849_a[id] : 0;
	}

	/**
	 * Side sensitive version that calls the block function.
	 *
	 * @param world
	 *            The current world
	 * @param x
	 *            X Position
	 * @param y
	 *            Y Position
	 * @param z
	 *            Z Position
	 * @param face
	 *            The side the fire is coming from
	 * @return True if the face can catch fire.
	 */
	@Override
	public boolean canCatchFire(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection face) {
		return world.getBlock(x, y, z).isFlammable(world, x, y, z, face);
	}

	/**
	 * Side sensitive version that calls the block function.
	 *
	 * @param world
	 *            The current world
	 * @param x
	 *            X Position
	 * @param y
	 *            Y Position
	 * @param z
	 *            Z Position
	 * @param oldChance
	 *            The previous maximum chance.
	 * @param face
	 *            The side the fire is coming from
	 * @return The chance of the block catching fire, or oldChance if it is
	 *         higher
	 */
	@Override
	public int getChanceToEncourageFire(final IBlockAccess world, final int x, final int y, final int z, final int oldChance, final ForgeDirection face) {
		final int newChance = world.getBlock(x, y, z).getFireSpreadSpeed(world, x, y, z, face);
		return (newChance > oldChance ? newChance : oldChance);
	}
	/*
	 * ================================= Forge Start
	 * ======================================
	 */
}