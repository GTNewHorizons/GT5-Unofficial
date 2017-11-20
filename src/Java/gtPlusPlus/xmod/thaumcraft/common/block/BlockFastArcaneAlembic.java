package gtPlusPlus.xmod.thaumcraft.common.block;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.xmod.thaumcraft.common.ItemBlockThaumcraft;
import gtPlusPlus.xmod.thaumcraft.common.tile.TileFastArcaneAlembic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.common.blocks.ItemJarFilled;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.lib.utils.InventoryUtils;

public class BlockFastArcaneAlembic extends BlockContainer {
	public IIcon[] icon;
	public IIcon iconGlow;
	private final int delay;

	public BlockFastArcaneAlembic() {
		super(Material.iron);
		this.icon = new IIcon[23];
		this.delay = 0;
		this.setStepSound(Block.soundTypeMetal);
		this.setCreativeTab(AddToCreativeTab.tabMachines);
		this.setBlockName("blockFastArcaneAlembic");
		this.setHardness(3.0f);
		this.setResistance(25.0f);
		this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
		GameRegistry.registerBlock(this, ItemBlockThaumcraft.class, "blockFastArcaneAlembic");
		LanguageRegistry.addName(this, "Upgraded Arcane Alembic");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(final IIconRegister ir) {
		this.icon[0] = ir.registerIcon("thaumcraft:metalbase");
		for (int a = 1; a <= 6; ++a) {
			this.icon[a] = ir.registerIcon("thaumcraft:crucible" + a);
		}
		this.icon[7] = ir.registerIcon("thaumcraft:goldbase");
		this.iconGlow = ir.registerIcon("thaumcraft:animatedglow");
	}

	@Override
	public IIcon getIcon(final int i, final int md) {
		return ((md == 0) || (md == 1) || (md == 5) || (md == 6)) ? this.icon[0] : this.icon[7];
	}

	@Override
	public IIcon getIcon(final IBlockAccess iblockaccess, final int i, final int j, final int k, final int side) {
		if (side == 1) {
			return this.icon[1];
		}
		if (side == 0) {
			return this.icon[2];
		}
		return this.icon[3];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(final Item par1, final CreativeTabs par2CreativeTabs, final List par3List) {
		par3List.add(new ItemStack(par1, 1, 1));
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public void onEntityCollidedWithBlock(final World world, final int i, final int j, final int k,
			final Entity entity) {
		if (!world.isRemote) {

		}
	}

	@Override
	public void addCollisionBoxesToList(final World world, final int i, final int j, final int k,
			final AxisAlignedBB axisalignedbb, final List arraylist, final Entity par7Entity) {
		this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
		super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(final World w, final int i, final int j, final int k, final Random r) {

	}

	@Override
	public int damageDropped(final int metadata) {
		return metadata;
	}

	@Override
	public TileEntity createTileEntity(final World world, final int metadata) {

		if (metadata == 1) {
			return new TileFastArcaneAlembic();
		}
		return super.createTileEntity(world, metadata);
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@Override
	public int getComparatorInputOverride(final World world, final int x, final int y, final int z, final int rs) {
		final TileEntity te = world.getTileEntity(x, y, z);
		if ((te != null) && (te instanceof TileFastArcaneAlembic)) {
			final float r = ((TileFastArcaneAlembic) te).amount / ((TileFastArcaneAlembic) te).maxAmount;
			return MathHelper.floor_float(r * 14.0f) + ((((TileFastArcaneAlembic) te).amount > 0) ? 1 : 0);
		}
		return 0;
	}

	@Override
	public TileEntity createNewTileEntity(final World var1, final int md) {
		return null;
	}

	@Override
	public void onNeighborBlockChange(final World world, final int x, final int y, final int z, final Block nbid) {
		final TileEntity te = world.getTileEntity(x, y, z);
		if (!world.isRemote) {
			if ((te != null) && (te instanceof TileFastArcaneAlembic)) {
				world.markBlockForUpdate(x, y, z);
			}
		}
		super.onNeighborBlockChange(world, x, y, z, nbid);
	}

	@Override
	public void breakBlock(final World par1World, final int par2, final int par3, final int par4, final Block par5,
			final int par6) {
		InventoryUtils.dropItems(par1World, par2, par3, par4);
		final TileEntity te = par1World.getTileEntity(par2, par3, par4);
		if ((te != null) && (te instanceof TileFastArcaneAlembic) && (((TileFastArcaneAlembic) te).aspectFilter != null)) {
			par1World.spawnEntityInWorld(new EntityItem(par1World, par2 + 0.5f,
					par3 + 0.5f, par4 + 0.5f, new ItemStack(ConfigItems.itemResource, 1, 13)));
		}
		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}

	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player,
			final int side, final float par7, final float par8, final float par9) {
		final int metadata = world.getBlockMetadata(x, y, z);

		if ((metadata == 1) && !world.isRemote && !player.isSneaking() && (player.getHeldItem() == null)) {
			final TileEntity te2 = world.getTileEntity(x, y, z);
			if ((te2 != null) && (te2 instanceof TileFastArcaneAlembic)) {
				final TileFastArcaneAlembic tile2 = (TileFastArcaneAlembic) te2;
				String msg = "";
				if ((tile2.aspect == null) || (tile2.amount == 0)) {
					msg = StatCollector.translateToLocal("tile.alembic.msg.1");
				} else if (tile2.amount < (tile2.maxAmount * 0.4)) {
					msg = StatCollector.translateToLocal("tile.alembic.msg.2");
				} else if (tile2.amount < (tile2.maxAmount * 0.8)) {
					msg = StatCollector.translateToLocal("tile.alembic.msg.3");
				} else if (tile2.amount < tile2.maxAmount) {
					msg = StatCollector.translateToLocal("tile.alembic.msg.4");
				} else if (tile2.amount == tile2.maxAmount) {
					msg = StatCollector.translateToLocal("tile.alembic.msg.5");
				}
				player.addChatMessage(new ChatComponentTranslation("§3" + msg, new Object[0]));
				world.playSoundEffect(x, y, z, "thaumcraft:alembicknock", 0.2f, 1.0f);
			}
		}
		if (metadata == 1) {
			final TileEntity te2 = world.getTileEntity(x, y, z);
			if ((te2 != null) && (te2 instanceof TileFastArcaneAlembic)) {
				if (player.isSneaking() && (((TileFastArcaneAlembic) te2).aspectFilter != null)) {
					((TileFastArcaneAlembic) te2).aspectFilter = null;
					world.markBlockForUpdate(x, y, z);
					te2.markDirty();
					if (world.isRemote) {
						world.playSound(x + 0.5f, y + 0.5f, z + 0.5f,
								"thaumcraft:page", 1.0f, 1.1f, false);
					} else {
						final ForgeDirection fd = ForgeDirection.getOrientation(side);
						world.spawnEntityInWorld(new EntityItem(world, x + 0.5f + (fd.offsetX / 3.0f),
								y + 0.5f, z + 0.5f + (fd.offsetZ / 3.0f),
								new ItemStack(ConfigItems.itemResource, 1, 13)));
					}
					return true;
				}
				if (player.isSneaking() && (player.getHeldItem() == null)) {
					((TileFastArcaneAlembic) te2).amount = 0;
					((TileFastArcaneAlembic) te2).aspect = null;
					if (world.isRemote) {
						world.playSound(x + 0.5f, y + 0.5f, z + 0.5f,
								"thaumcraft:alembicknock", 0.2f, 1.0f, false);
						world.playSound(x + 0.5f, y + 0.5f, z + 0.5f,
								"game.neutral.swim", 0.5f,
								1.0f + ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.3f), false);
					}
				} else if ((player.getHeldItem() != null) && (((TileFastArcaneAlembic) te2).aspectFilter == null)
						&& (player.getHeldItem().getItem() == ConfigItems.itemResource)
						&& (player.getHeldItem().getItemDamage() == 13)) {
					if ((((TileFastArcaneAlembic) te2).amount == 0) && (((IEssentiaContainerItem) player.getHeldItem().getItem())
							.getAspects(player.getHeldItem()) == null)) {
						return true;
					}
					if ((((TileFastArcaneAlembic) te2).amount == 0) && (((IEssentiaContainerItem) player.getHeldItem().getItem())
							.getAspects(player.getHeldItem()) != null)) {
						((TileFastArcaneAlembic) te2).aspect = ((IEssentiaContainerItem) player.getHeldItem().getItem())
								.getAspects(player.getHeldItem()).getAspects()[0];
					}
					final ItemStack heldItem = player.getHeldItem();
					--heldItem.stackSize;
					((TileFastArcaneAlembic) te2).aspectFilter = ((TileFastArcaneAlembic) te2).aspect;
					world.markBlockForUpdate(x, y, z);
					te2.markDirty();
					if (world.isRemote) {
						world.playSound(x + 0.5f, y + 0.5f, z + 0.5f,
								"thaumcraft:page", 1.0f, 0.9f, false);
					}
					return true;
				} else if ((player.getHeldItem() != null) && (((TileFastArcaneAlembic) te2).amount > 0)
						&& ((player.getHeldItem().getItem() == ConfigItems.itemJarFilled)
								|| player.getHeldItem().isItemEqual(new ItemStack(ConfigBlocks.blockJar, 1, 0))
								|| player.getHeldItem().isItemEqual(new ItemStack(ConfigBlocks.blockJar, 1, 3)))) {
					boolean doit = false;
					ItemStack drop = null;
					if (player.getHeldItem().isItemEqual(new ItemStack(ConfigBlocks.blockJar, 1, 0))
							|| player.getHeldItem().isItemEqual(new ItemStack(ConfigBlocks.blockJar, 1, 3))) {
						drop = new ItemStack(ConfigItems.itemJarFilled, 1, player.getHeldItem().getItemDamage());
						doit = true;
						((ItemJarFilled) drop.getItem()).setAspects(drop,
								new AspectList().add(((TileFastArcaneAlembic) te2).aspect, ((TileFastArcaneAlembic) te2).amount));
						((TileFastArcaneAlembic) te2).amount = 0;
						((TileFastArcaneAlembic) te2).aspect = null;
						final ItemStack heldItem2 = player.getHeldItem();
						--heldItem2.stackSize;
						if (!player.inventory.addItemStackToInventory(drop) && !world.isRemote) {
							world.spawnEntityInWorld(
									new EntityItem(world, player.posX, player.posY, player.posZ, drop));
						}
					} else {
						drop = player.getHeldItem();
						if (((((ItemJarFilled) drop.getItem()).getAspects(drop) == null)
								|| (((ItemJarFilled) drop.getItem()).getAspects(drop).visSize() == 0)
								|| (((ItemJarFilled) drop.getItem()).getAspects(drop)
										.getAmount(((TileFastArcaneAlembic) te2).aspect) > 0))
								&& ((((ItemJarFilled) drop.getItem()).getFilter(drop) == null)
										|| (((ItemJarFilled) drop.getItem())
												.getFilter(drop) == ((TileFastArcaneAlembic) te2).aspect))) {
							int amount = Math.min(
									(((ItemJarFilled) drop.getItem()).getAspects(drop) == null)
									? 64
											: (64 - ((ItemJarFilled) drop.getItem()).getAspects(drop).visSize()),
											((TileFastArcaneAlembic) te2).amount);
							if (drop.getItemDamage() == 3) {
								amount = ((TileFastArcaneAlembic) te2).amount;
							}
							if (amount > 0) {
								final TileFastArcaneAlembic TileFastArcaneAlembic = (TileFastArcaneAlembic) te2;
								TileFastArcaneAlembic.amount -= amount;
								AspectList as = ((ItemJarFilled) drop.getItem()).getAspects(drop);
								if (as == null) {
									as = new AspectList();
								}
								as.add(((TileFastArcaneAlembic) te2).aspect, amount);
								if (as.getAmount(((TileFastArcaneAlembic) te2).aspect) > 64) {
									final int q = as.getAmount(((TileFastArcaneAlembic) te2).aspect) - 64;
									as.reduce(((TileFastArcaneAlembic) te2).aspect, q);
								}
								((ItemJarFilled) drop.getItem()).setAspects(drop, as);
								if (((TileFastArcaneAlembic) te2).amount <= 0) {
									((TileFastArcaneAlembic) te2).aspect = null;
								}
								doit = true;
								player.setCurrentItemOrArmor(0, drop);
							}
						}
					}
					if (doit) {
						te2.markDirty();
						world.markBlockForUpdate(x, y, z);
						if (world.isRemote) {
							world.playSound(x + 0.5f, y + 0.5f, z + 0.5f,
									"game.neutral.swim", 0.5f,
									1.0f + ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.3f), false);
						}
					}
					return true;
				}
			}
		}

		if (world.isRemote) {
			return true;
		}

		return super.onBlockActivated(world, x, y, z, player, side, par7, par8, par9);
	}

	public void onPoweredBlockChange(final World par1World, final int par2, final int par3, final int par4,
			final boolean flag) {
		final int l = par1World.getBlockMetadata(par2, par3, par4);
		if ((l == 5) && flag) {
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 6, 2);
			par1World.playAuxSFXAtEntity((EntityPlayer) null, 1003, par2, par3, par4, 0);
		} else if ((l == 6) && !flag) {
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 5, 2);
			par1World.playAuxSFXAtEntity((EntityPlayer) null, 1003, par2, par3, par4, 0);
		}
	}

	@Override
	public void onBlockPlacedBy(final World world, final int par2, final int par3, final int par4,
			final EntityLivingBase ent, final ItemStack stack) {
		final int l = MathHelper.floor_double(((ent.rotationYaw * 4.0f) / 360.0f) + 0.5) & 0x3;
		if (stack.getItemDamage() == 1) {
			final TileEntity tile = world.getTileEntity(par2, par3, par4);
			if (tile instanceof TileFastArcaneAlembic) {
				if (l == 0) {
					((TileFastArcaneAlembic) tile).facing = 2;
				}
				if (l == 1) {
					((TileFastArcaneAlembic) tile).facing = 5;
				}
				if (l == 2) {
					((TileFastArcaneAlembic) tile).facing = 3;
				}
				if (l == 3) {
					((TileFastArcaneAlembic) tile).facing = 4;
				}
			}
		}
	}

	@Override
	public int getLightValue(final IBlockAccess world, final int x, final int y, final int z) {
		return super.getLightValue(world, x, y, z);
	}
}