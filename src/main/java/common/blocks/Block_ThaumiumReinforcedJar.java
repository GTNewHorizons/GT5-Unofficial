package common.blocks;

import java.util.ArrayList;

import common.tileentities.TE_ThaumiumReinforcedJar;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import items.Item_ThaumiumReinforcedJarFilled;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.blocks.BlockJar;
import thaumcraft.common.blocks.ItemJarFilled;
import thaumcraft.common.tiles.TileJarFillable;
import thaumcraft.common.tiles.TileJarFillableVoid;

public class Block_ThaumiumReinforcedJar extends BlockJar {
	
	private static Block_ThaumiumReinforcedJar instance = new Block_ThaumiumReinforcedJar();
	
	private Block_ThaumiumReinforcedJar() {
		super();
	}
	
	public static Block_ThaumiumReinforcedJar getInstance() {
		return instance;
	}
	
	public void registerBlock() {
		final String blockName = "kekztech_thaumiumreinforcedjar_block";
		super.setBlockName(blockName);
		GameRegistry.registerBlock(getInstance(), blockName);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister ir) {
		super.iconLiquid = ir.registerIcon("thaumcraft:animatedglow");
		super.iconJarSide = ir.registerIcon("kekztech:jar_side");
		super.iconJarTop = ir.registerIcon("kekztech:jar_top");
		super.iconJarTopVoid = ir.registerIcon("kekztech:jar_top_void");
		super.iconJarSideVoid = ir.registerIcon("kekztech:jar_side_void");
		super.iconJarBottom = ir.registerIcon("kekztech:jar_bottom");
	}
	
	@Override
	public TileEntity createTileEntity(World world, int meta) {
		return new TE_ThaumiumReinforcedJar();
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
		final ArrayList<ItemStack> drops = new ArrayList<>();
		
		TileEntity te;
		ItemStack drop;
		
		te = world.getTileEntity(x, y, z);
		if(te != null && te instanceof TileJarFillable) {
			drop = new ItemStack(Item_ThaumiumReinforcedJarFilled.getInstance());
			// Empty and no label
			if(((TileJarFillable) te).amount <= 0 && ((TileJarFillable) te).aspectFilter == null) {
				drop = new ItemStack(this);
			}
			// If is void jar, set meta
			if(te instanceof TileJarFillableVoid) {
				drop.setItemDamage(3);
			}
			// Non empty, generate filled jar item with contents
			if(((TileJarFillable) te).amount > 0) {
				((ItemJarFilled) drop.getItem()).setAspects(drop,
						(new AspectList()).add(((TileJarFillable) te).aspect, ((TileJarFillable) te).amount));
			}
			// has label
			if(((TileJarFillable) te).aspectFilter != null) {
				if(!drop.hasTagCompound()) {
					drop.setTagCompound(new NBTTagCompound());
				}
				drop.stackTagCompound.setString("AspectFilter", ((TileJarFillable) te).aspectFilter.getTag());
			}
			drops.add(drop);
		}
		return drops;
	}
	
}
