package gtPlusPlus.core.block.machine.heliumgen.block;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.core.block.machine.heliumgen.tileentity.TileEntityHeliumGenerator;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class HeliumGenerator extends BlockContainer {

	private IIcon			top, sides, front;
	private final Random	randy	= new Random();

	public HeliumGenerator() {
		super(Material.iron);
		this.setStepSound(Block.soundTypeMetal);
		this.setHardness(20.0F);
		this.setBlockName("helium_collector");
		this.setHarvestLevel("pickaxe", 3);
		this.setCreativeTab(AddToCreativeTab.tabMachines);
	}

	@Override
	public void breakBlock(final World world, final int x, final int y, final int z, final Block block, final int wut) {
		final TileEntityHeliumGenerator collector = (TileEntityHeliumGenerator) world.getTileEntity(x, y, z);

		if (collector != null) {
			int i = 0;
			for (i = 0; i < collector.getSizeInventory(); i++) {

				final ItemStack itemstack = collector.getStackInSlot(i);

				if (itemstack != null) {
					final float f = this.randy.nextFloat() * 0.8F + 0.1F;
					final float f1 = this.randy.nextFloat() * 0.8F + 0.1F;
					final float f2 = this.randy.nextFloat() * 0.8F + 0.1F;

					while (itemstack.stackSize > 0) {
						int j1 = this.randy.nextInt(21) + 10;

						if (j1 > itemstack.stackSize) {
							j1 = itemstack.stackSize;
						}

						itemstack.stackSize -= j1;
						final EntityItem entityitem = new EntityItem(world, x + f, y + f1, z + f2,
								new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

						if (itemstack.hasTagCompound()) {
							entityitem.getEntityItem()
									.setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
						}

						final float f3 = 0.05F;
						entityitem.motionX = (float) this.randy.nextGaussian() * f3;
						entityitem.motionY = (float) this.randy.nextGaussian() * f3 + 0.2F;
						entityitem.motionZ = (float) this.randy.nextGaussian() * f3;
						world.spawnEntityInWorld(entityitem);
					}
				}

				world.func_147453_f(x, y, z, block);
			}
		}

		super.breakBlock(world, x, y, z, block, wut);
	}

	@Override
	public TileEntity createNewTileEntity(final World world, final int meta) {
		return new TileEntityHeliumGenerator();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(final IBlockAccess world, final int x, final int y, final int z, final int side) {
		if (side == 1) {
			return this.top;
		}
		int facing = 2;
		final TileEntityHeliumGenerator machine = (TileEntityHeliumGenerator) world.getTileEntity(x, y, z);
		if (machine != null) {
			facing = machine.getFacing();
		}
		if (side == facing) {
			return this.front;
		}
		else {
			return this.sides;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(final int side, final int metadata) {
		if (side == 1) {
			return this.top;
		}
		if (side == 3) {
			return this.front;
		}
		return this.sides;
	}

	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player,
			final int par6, final float par7, final float par8, final float par9) {
		if (world.isRemote) {
			return true;
		}
		player.openGui(GTplusplus.instance, 2, world, x, y, z);
		return true;
	}

	@Override
	public void onBlockPlacedBy(final World world, final int x, final int y, final int z, final EntityLivingBase player,
			final ItemStack item) {
		final TileEntity tile = world.getTileEntity(x, y, z);
		if (tile instanceof TileEntityHeliumGenerator) {
			final TileEntityHeliumGenerator machine = (TileEntityHeliumGenerator) tile;
			final int l = MathHelper.floor_double(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

			if (l == 0) {
				machine.setFacing((short) 2);
			}

			if (l == 1) {
				machine.setFacing((short) 5);
			}

			if (l == 2) {
				machine.setFacing((short) 3);
			}

			if (l == 3) {
				machine.setFacing((short) 4);
			}
		}

	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(final IIconRegister iconRegister) {
		this.top = iconRegister.registerIcon(CORE.MODID + ":blockHeliumCollector_SIDE");
		this.sides = iconRegister.registerIcon(CORE.MODID + ":blockHeliumCollector_SIDE");
		this.front = iconRegister.registerIcon(CORE.MODID + ":blockHeliumCollector_FRONT");
	}
}
