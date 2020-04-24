package common.blocks;

import common.tileentities.TE_IchorJar;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import thaumcraft.common.blocks.BlockJar;

public class Block_IchorJar extends BlockJar {
	
	private static Block_IchorJar instance = new Block_IchorJar();
	
	private Block_IchorJar() {
		super();
	}
	
	public static Block_IchorJar getInstance() {
		return instance;
	}
	
	public void registerBlock() {
		final String blockName = "kekztech_ichorjar_block";
		super.setBlockName(blockName);
		GameRegistry.registerBlock(getInstance(), blockName);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister ir) {
		super.iconLiquid = ir.registerIcon("thaumcraft:animatedglow");
		super.iconJarSide = ir.registerIcon("kekztech:ichor_jar_side");
		super.iconJarTop = ir.registerIcon("kekztech:ichor_jar_top");
		super.iconJarTopVoid = ir.registerIcon("kekztech:ichor_jar_top_void");
		super.iconJarSideVoid = ir.registerIcon("kekztech:jar_side_void");
		super.iconJarBottom = ir.registerIcon("kekztech:ichor_jar_bottom");
	}
	
	@Override
	public TileEntity createTileEntity(World world, int meta) {
		return new TE_IchorJar();
	}
	
}
