package common.blocks;

import java.util.ArrayList;
import java.util.List;

import common.tileentities.TE_ThaumiumReinforcedJar;
import common.tileentities.TE_ThaumiumReinforcedVoidJar;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import items.Item_ThaumiumReinforcedJarFilled;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.blocks.BlockJar;

public class Block_ThaumiumReinforcedJar extends BlockJar {
	
	private static Block_ThaumiumReinforcedJar instance;
	
	private Block_ThaumiumReinforcedJar() {
		super();
	}
	
	public static Block registerBlock() {
		if(instance == null) {
			instance = new Block_ThaumiumReinforcedJar();
		}
		
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		par3List.add(new ItemStack(par1, 1, 0)); // Normal jar
		par3List.add(new ItemStack(par1, 1, 3)); // Void jar
	}
	
	@Override
	public TileEntity createTileEntity(World world, int meta) {
		if(meta == 0) {
			return new TE_ThaumiumReinforcedJar();
		} else if (meta == 3) {
			return new TE_ThaumiumReinforcedVoidJar();
		} else {
			return null;
		}
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
		final ArrayList<ItemStack> drops = new ArrayList<>();
		
		ItemStack drop;
		
		final TileEntity te = world.getTileEntity(x, y, z);
		if(te != null && te instanceof TE_ThaumiumReinforcedJar) {
			drop = new ItemStack(Item_ThaumiumReinforcedJarFilled.getInstance());
			// Empty and no label
			if(((TE_ThaumiumReinforcedJar) te).amount <= 0 && ((TE_ThaumiumReinforcedJar) te).aspectFilter == null) {
				drop = new ItemStack(this);
			}
			// If is void jar, set meta
			if(te instanceof TE_ThaumiumReinforcedVoidJar) {
				drop.setItemDamage(3);
			}
			// Non empty, generate filled jar item with contents
			if(((TE_ThaumiumReinforcedJar) te).amount > 0) {
				((Item_ThaumiumReinforcedJarFilled) drop.getItem()).setAspects(drop,
						(new AspectList()).add(((TE_ThaumiumReinforcedJar) te).aspect, ((TE_ThaumiumReinforcedJar) te).amount));
			}
			// has label
			if(((TE_ThaumiumReinforcedJar) te).aspectFilter != null) {
				if(!drop.hasTagCompound()) {
					drop.setTagCompound(new NBTTagCompound());
				}
				drop.stackTagCompound.setString("AspectFilter", ((TE_ThaumiumReinforcedJar) te).aspectFilter.getTag());
			}
			drops.add(drop);
		}
		return drops;
	}
}
