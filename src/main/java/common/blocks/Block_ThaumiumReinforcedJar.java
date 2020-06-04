package common.blocks;

import common.itemBlocks.IB_ThaumiumReinforcedJar;
import common.tileentities.TE_ThaumiumReinforcedJar;
import common.tileentities.TE_ThaumiumReinforcedVoidJar;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.blocks.BlockJar;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.ItemEssence;
import thaumcraft.common.tiles.TileJarFillable;

import java.util.ArrayList;
import java.util.List;

public class Block_ThaumiumReinforcedJar extends BlockJar {
	
	private static final Block_ThaumiumReinforcedJar INSTANCE = new Block_ThaumiumReinforcedJar();
	
	private Block_ThaumiumReinforcedJar() {
		super();
		
		super.setHardness(6.0F);
		super.setResistance(6.0F);
	}
	
	public static Block registerBlock() {
		final String blockName = "kekztech_thaumiumreinforcedjar_block";
		INSTANCE.setBlockName(blockName);
		INSTANCE.setHarvestLevel("pickaxe", 2);
		GameRegistry.registerBlock(INSTANCE, IB_ThaumiumReinforcedJar.class, blockName);
		
		return INSTANCE;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister ir) {
		super.iconLiquid = ir.registerIcon("thaumcraft:animatedglow");
		super.iconJarSide = ir.registerIcon("kekztech:thaumreinforced_jar_side");
		super.iconJarTop = ir.registerIcon("kekztech:thaumreinforced_jar_top");
		super.iconJarTopVoid = ir.registerIcon("kekztech:thaumreinforced_jar_top_void");
		super.iconJarSideVoid = ir.registerIcon("kekztech:thaumreinforced_jar_side_void");
		super.iconJarBottom = ir.registerIcon("kekztech:thaumreinforced_jar_bottom");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({"unchecked"})
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		par3List.add(new ItemStack(par1, 1, 0)); // Normal jar
		par3List.add(new ItemStack(par1, 1, 3)); // Void jar
	}
	
	@Override
	public TileEntity createTileEntity(World world, int meta) {
		if(meta == 3) {
			return new TE_ThaumiumReinforcedVoidJar();
		} else {
			return new TE_ThaumiumReinforcedJar();
		}
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block par5, int par6) {
		final TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof TE_ThaumiumReinforcedJar) {
			final TE_ThaumiumReinforcedJar ite = (TE_ThaumiumReinforcedJar) te;
			breakBlockWarpy(world, x, y, z, ite.amount, 50, 1.0F);
		} else if(te instanceof  TE_ThaumiumReinforcedVoidJar) {
			final TE_ThaumiumReinforcedVoidJar ite = (TE_ThaumiumReinforcedVoidJar) te;
			breakBlockWarpy(world, x, y, z, ite.amount, 50, 1.0F);
		}
		super.breakBlock(world, x, y, z, par5, par6);
	}

	private void breakBlockWarpy(World world, int x, int y, int z, int fillAmount, int iterations, float explosionStrength){
		if(fillAmount > 0) {
			// Create a decent explosion in the center of the block (TNT has strength 4.0F)
			world.createExplosion(null, x + 0.5D, y + 0.5D, z + 0.5D, explosionStrength, false);

			// Place a lot of Flux in the area
			final int limit = fillAmount / 16;
			int created = 0;
			for(int i = 0; i < iterations; i++) {
				final int xf = x + world.rand.nextInt(7) - world.rand.nextInt(7);
				final int yf = x + world.rand.nextInt(7) - world.rand.nextInt(7);
				final int zf = x + world.rand.nextInt(7) - world.rand.nextInt(7);
				if(world.isAirBlock(xf, yf, zf)) {
					if(yf > y) {
						world.setBlock(xf, yf, zf, ConfigBlocks.blockFluxGas, 8, 3);
					} else {
						world.setBlock(xf, yf, zf, ConfigBlocks.blockFluxGoo, 8, 3);
					}

					if(created++ > limit) {
						break;
					}
				}
			}
		}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float f1, float f2, float f3) {
		// Call parent method to handle jar emptying, labels stuff etc
		super.onBlockActivated(world, x, y, z, player, side, f1, f2, f3);
		// Interact with Essentia Phials if the player holds one
		final ItemStack heldItem = player.getHeldItem();
		if(heldItem != null && heldItem.getItem() == ConfigItems.itemEssence) {
			final TileEntity te = world.getTileEntity(x, y, z);
			if(te instanceof TE_ThaumiumReinforcedJar) {
				return dealWithPhial(world, player, x, y, z);
			} else if(te instanceof  TE_ThaumiumReinforcedVoidJar) {
				return dealWithPhial(world, player, x, y, z);
			}
		}

		return true;
	}

	/**
	 * Handle compatibility with Essentia Phials
	 * @param world
	 * 			Pass through from onBlockActivated()
	 * @param player
	 * 			Pass through from onBlockActivated()
	 * @param x
	 * 			Pass through from onBlockActivated()
	 * @param y
	 * 			Pass through from onBlockActivated()
	 * @param z
	 * 			Pass through from onBlockActivated()
	 * @return Not sure tbh
	 */
	private boolean dealWithPhial(World world, EntityPlayer player, int x, int y, int z) {
		final TileJarFillable kte = (TileJarFillable) world.getTileEntity(x, y, z);
		final ItemStack heldItem = player.getHeldItem();
		// Check whether to fill or to drain the phial
		if(heldItem.getItemDamage() == 0) {
			if(kte.amount >= 8){
				if (world.isRemote) {
					player.swingItem();
					return false;
				}

				final Aspect jarAspect = Aspect.getAspect(kte.aspect.getTag());
				if(kte.takeFromContainer(jarAspect, 8)) {
					// Take an empty phial from the player's inventory
					heldItem.stackSize--;
					// Fill a new phial
					final ItemStack filledPhial = new ItemStack(ConfigItems.itemEssence, 1, 1);
					final AspectList phialContent = new AspectList().add(jarAspect, 8);
					((ItemEssence) ConfigItems.itemEssence).setAspects(filledPhial, phialContent);
					// Drop on ground if there's no inventory space
					if (!player.inventory.addItemStackToInventory(filledPhial)) {
						world.spawnEntityInWorld(new EntityItem(world, (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, filledPhial));
					}

					world.playSoundAtEntity(player, "game.neutral.swim", 0.25F, 1.0F);
					player.inventoryContainer.detectAndSendChanges();
					return true;
				}
			}
		} else {
			final AspectList phialContent = ((ItemEssence) ConfigItems.itemEssence).getAspects(heldItem);
			if(phialContent != null && phialContent.size() == 1) {
				final Aspect phialAspect = phialContent.getAspects()[0];
				if(kte.amount + 8 <= kte.maxAmount && kte.doesContainerAccept(phialAspect)) {
					if (world.isRemote) {
						player.swingItem();
						return false;
					}

					if(kte.addToContainer(phialAspect, 8) == 0) {
						world.markBlockForUpdate(x, y, z);
						kte.markDirty();
						heldItem.stackSize--;
						// Drop on ground if there's no inventory space
						if (!player.inventory.addItemStackToInventory(new ItemStack(ConfigItems.itemEssence, 1, 0))) {
							world.spawnEntityInWorld(new EntityItem(world, (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, new ItemStack(ConfigItems.itemEssence, 1, 0)));
						}

						world.playSoundAtEntity(player, "game.neutral.swim", 0.25F, 1.0F);
						player.inventoryContainer.detectAndSendChanges();
						return true;
					}
				}
			}
		}

		return true;
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
		final ArrayList<ItemStack> drops = new ArrayList<>();
		drops.add(new ItemStack(this, 1, (meta == 3) ? 3 : 0));
		final TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof TE_ThaumiumReinforcedJar) {
			final TE_ThaumiumReinforcedJar ite = (TE_ThaumiumReinforcedJar) te;
			if(ite.aspectFilter != null){
				final ItemStack droppedLabel = new ItemStack(ConfigItems.itemResource, 1, 13);
				droppedLabel.setTagCompound(new NBTTagCompound());
				final AspectList aspect = new AspectList().add(ite.aspectFilter,0);
				aspect.writeToNBT(droppedLabel.getTagCompound());
				drops.add(droppedLabel);
			}
		} else if(te instanceof TE_ThaumiumReinforcedVoidJar) {
			final TE_ThaumiumReinforcedVoidJar ite = (TE_ThaumiumReinforcedVoidJar) te;
			if(ite.aspectFilter != null) {
				final ItemStack droppedLabel = new ItemStack(ConfigItems.itemResource, 1, 13);
				droppedLabel.setTagCompound(new NBTTagCompound());
				final AspectList aspect = new AspectList().add(ite.aspectFilter,0);
				aspect.writeToNBT(droppedLabel.getTagCompound());
				drops.add(droppedLabel);
			}
		}
		return drops;
	}

	@Override
	public void onBlockHarvested(World par1World, int par2, int par3, int par4, int par5, EntityPlayer par6EntityPlayer) {
	}
	
	@Override
	public boolean canDropFromExplosion(Explosion e) {
		return false;
	}
}
