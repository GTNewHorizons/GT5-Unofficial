package common.blocks;

import common.tileentities.TE_IchorJar;
import common.tileentities.TE_IchorVoidJar;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import thaumcraft.common.blocks.BlockJar;
import thaumcraft.common.config.ConfigBlocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Block_IchorJar extends BlockJar {
	
	private static final Block_IchorJar INSTANCE = new Block_IchorJar();
	
	private Block_IchorJar() {
		super();
		
		super.setHardness(20.0F);
		super.setResistance(3.0f);
	}

	public static Block registerBlock() {
		final String blockName = "kekztech_ichorjar_block";
		INSTANCE.setBlockName(blockName);
		GameRegistry.registerBlock(INSTANCE, blockName);
		
		return INSTANCE;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister ir) {
		super.iconLiquid = ir.registerIcon("thaumcraft:animatedglow");
		super.iconJarSide = ir.registerIcon("kekztech:ichor_jar_side");
		super.iconJarTop = ir.registerIcon("kekztech:ichor_jar_top");
		super.iconJarTopVoid = ir.registerIcon("kekztech:ichor_jar_top_void");
		super.iconJarSideVoid = ir.registerIcon("kekztech:ichor_jar_side_void");
		super.iconJarBottom = ir.registerIcon("kekztech:ichor_jar_bottom");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		par3List.add(new ItemStack(par1, 1, 0)); // Normal jar
		par3List.add(new ItemStack(par1, 1, 3)); // Void jar
	}
	
	@Override
	public TileEntity createTileEntity(World world, int meta) {
		if(meta == 0) {
			return new TE_IchorJar();
		} else if (meta == 3) {
			return new TE_IchorVoidJar();
		} else {
			return null;
		}
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block par5, int par6) {
		final TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof TE_IchorJar) {
			TE_IchorJar ite = (TE_IchorJar) te;
			if(ite.amount > 0) {
				// Create a decent explosion in the center of the block (TNT has strength 4.0F)
				world.createExplosion(null, x + 0.5D, y + 0.5D, z + 0.5D, 6.0F, false);
				
				// Place a lot of Flux in the area
				final int limit = ite.amount / 16;
				int created = 0;
				for(int i = 0; i < 200; i++) {
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
		
		super.breakBlock(world, x, y, z, par5, par6);
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
		return new ArrayList<>(Collections.singleton(new ItemStack(this, 1, (meta == 3) ? 3 : 0)));
	}

	@Override
	public void onBlockHarvested(World par1World, int par2, int par3, int par4, int par5, EntityPlayer par6EntityPlayer) {
	}

	@Override
	public boolean canDropFromExplosion(Explosion e) {
		return false;
	}
}
