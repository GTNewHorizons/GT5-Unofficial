package common.blocks;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.blocks.BlockJar;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;

public class Block_ThaumiumReinforcedJar extends BlockJar {
	
	private static Block_ThaumiumReinforcedJar instance = new Block_ThaumiumReinforcedJar();
	
	private Block_ThaumiumReinforcedJar() {
		super();
		
		super.setHardness(6.0F);
		super.setResistance(6.0F);
	}
	
	public static Block registerBlock() {
		final String blockName = "kekztech_thaumiumreinforcedjar_block";
		instance.setBlockName(blockName);
		GameRegistry.registerBlock(instance, blockName);
		
		return instance;
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
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
		final ArrayList<ItemStack> drops = new ArrayList<>();
		drops.add(new ItemStack(this, 1, (meta == 3) ? 3 : 0));
		final TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof  TE_ThaumiumReinforcedJar) {
			final TE_ThaumiumReinforcedJar ite = (TE_ThaumiumReinforcedJar) te;
			if(ite.aspectFilter != null){
				final ItemStack droppedLabel = new ItemStack(ConfigItems.itemResource, 1, 13);
				droppedLabel.setTagCompound(new NBTTagCompound());
				final AspectList aspect = new AspectList().add(ite.aspectFilter,0);
				aspect.writeToNBT(droppedLabel.getTagCompound());
				drops.add(droppedLabel);
			}
		} else if(te instanceof  TE_ThaumiumReinforcedVoidJar) {
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
