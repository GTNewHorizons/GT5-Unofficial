package gtPlusPlus.core.item.base;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class BasicSpawnEgg extends ItemMonsterPlacer {
	@SideOnly(Side.CLIENT)
	private IIcon			theIcon;
	protected int			colorBase				= 0x000000;
	protected int			colorSpots				= 0xFFFFFF;
	protected String		entityMODID				= "";
	protected String		entityToSpawnName		= "";
	protected String		entityToSpawnNameFull	= "";
	protected EntityLiving	entityToSpawn			= null;

	public BasicSpawnEgg() {
		super();
	}

	public BasicSpawnEgg(final String MODID, final String parEntityToSpawnName, final int parPrimaryColor,
			final int parSecondaryColor) {
		this.setHasSubtypes(false);
		this.maxStackSize = 64;
		this.setCreativeTab(AddToCreativeTab.tabOther);
		this.setEntityToSpawnName(parEntityToSpawnName);
		this.colorBase = parPrimaryColor;
		this.colorSpots = parSecondaryColor;
		this.entityMODID = MODID;

		// DEBUG
		Utils.LOG_WARNING("Spawn egg constructor for " + this.entityToSpawnName);
	}

	public int getColorBase() {
		return this.colorBase;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(final ItemStack par1ItemStack, final int parColorType) {
		return parColorType == 0 ? this.colorBase : this.colorSpots;
	}

	public int getColorSpots() {
		return this.colorSpots;
	}

	/**
	 * Gets an icon index based on an item's damage value and the given render
	 * pass
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamageForRenderPass(final int parDamageVal, final int parRenderPass) {
		return parRenderPass > 0 ? this.theIcon : super.getIconFromDamageForRenderPass(parDamageVal, parRenderPass);
	}

	@Override
	// Doing this override means that there is no localization for language
	// unless you specifically check for localization here and convert
	public String getItemStackDisplayName(final ItemStack par1ItemStack) {
		return "Spawn " + this.entityToSpawnName;
	}

	/**
	 * returns a list of items with the same ID, but different meta (eg: dye
	 * returns 16 items)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(final Item parItem, final CreativeTabs parTab, final List parList) {
		parList.add(new ItemStack(parItem, 1, 0));
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is
	 * pressed.
	 * 
	 * Args: itemStack, world, entityPlayer
	 */
	@Override
	public ItemStack onItemRightClick(final ItemStack par1ItemStack, final World par2World,
			final EntityPlayer par3EntityPlayer) {
		if (par2World.isRemote) {
			return par1ItemStack;
		}
		final MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(par2World,
				par3EntityPlayer, true);

		if (movingobjectposition == null) {
			return par1ItemStack;
		}
		if (movingobjectposition.typeOfHit == MovingObjectPosition

				.MovingObjectType.BLOCK)

		{
			final int i = movingobjectposition.blockX;
			final int j = movingobjectposition.blockY;
			final int k = movingobjectposition.blockZ;

			if (!par2World.canMineBlock(par3EntityPlayer, i, j, k)) {
				return par1ItemStack;
			}

			if (!par3EntityPlayer.canPlayerEdit(i, j, k, movingobjectposition.sideHit, par1ItemStack)) {
				return par1ItemStack;
			}

			if (par2World.getBlock(i, j, k) instanceof BlockLiquid) {
				final Entity entity = this.spawnEntity(par2World, i, j, k);

				if (entity != null) {
					if (entity instanceof EntityLivingBase && par1ItemStack.hasDisplayName()) {
						((EntityLiving) entity).setCustomNameTag(par1ItemStack.getDisplayName());
					}

					if (!par3EntityPlayer.capabilities.isCreativeMode) {
						--par1ItemStack.stackSize;
					}
				}
			}
		}

		return par1ItemStack;
	}

	/**
	 * Callback for item usage. If the item does something special on right
	 * clicking,
	 * 
	 * he will have one of those. Return True if something happen and false if
	 * it don't. This is for ITEMS, not BLOCKS
	 */
	@Override
	public boolean onItemUse(final ItemStack par1ItemStack, final EntityPlayer par2EntityPlayer, final World par3World,
			int par4, int par5, int par6, final int par7, final float par8, final float par9, final float par10) {
		if (par3World.isRemote) {
			return true;
		}
		final Block block = par3World.getBlock(par4, par5, par6);
		par4 += Facing.offsetsXForSide[par7];
		par5 += Facing.offsetsYForSide[par7];
		par6 += Facing.offsetsZForSide[par7];
		double d0 = 0.0D;

		if (par7 == 1 && block.getRenderType() == 11) {
			d0 = 0.5D;
		}

		final Entity entity = this.spawnEntity(par3World, par4 + 0.5D, par5 + d0, par6 + 0.5D);

		if (entity != null) {
			if (entity instanceof EntityLivingBase && par1ItemStack.hasDisplayName()) {
				((EntityLiving) entity).setCustomNameTag(par1ItemStack.getDisplayName());
			}

			if (!par2EntityPlayer.capabilities.isCreativeMode) {
				--par1ItemStack.stackSize;
			}
		}

		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(final IIconRegister par1IconRegister) {
		super.registerIcons(par1IconRegister);
		this.theIcon = par1IconRegister.registerIcon(this.getIconString() + "_overlay");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	public void setColors(final int parColorBase, final int parColorSpots) {
		this.colorBase = parColorBase;
		this.colorSpots = parColorSpots;
	}

	public void setEntityToSpawnName(final String parEntityToSpawnName) {
		this.entityToSpawnName = parEntityToSpawnName;
		this.entityToSpawnNameFull = this.entityMODID + "." + this.entityToSpawnName;
	}

	/**
	 * Spawns the creature specified by the egg's type in the location specified
	 * by
	 * 
	 * the last three parameters. Parameters: world, entityID, x, y, z.
	 */
	public Entity spawnEntity(final World parWorld, final double parX, final double parY, final double parZ) {

		if (!parWorld.isRemote) // never spawn entity on client side
		{
			this.entityToSpawnNameFull = this.entityMODID + "." + this.entityToSpawnName;
			if (EntityList.stringToClassMapping.containsKey(this.entityToSpawnNameFull)) {
				this.entityToSpawn = (EntityLiving) EntityList

						.createEntityByName(this.entityToSpawnNameFull, parWorld);
				this.entityToSpawn.setLocationAndAngles(parX, parY, parZ,

						MathHelper.wrapAngleTo180_float(parWorld.rand.nextFloat()

								* 360.0F),
						0.0F);
				parWorld.spawnEntityInWorld(this.entityToSpawn);
				this.entityToSpawn.onSpawnWithEgg((IEntityLivingData) null);
				this.entityToSpawn.playLivingSound();
			}
			else {
				// DEBUG
				Utils.LOG_WARNING("Entity not found " + this.entityToSpawnName);
			}
		}

		return this.entityToSpawn;
	}

}
