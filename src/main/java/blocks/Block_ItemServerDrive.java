package blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import itemBlocks.IB_ItemServerDrive;
import kekztech.KekzCore;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;

public class Block_ItemServerDrive extends BaseGTUpdateableBlock{

	private static Block_ItemServerDrive instance = new Block_ItemServerDrive();
	
	private IIcon[] faces = new IIcon[6];
	
	private Block_ItemServerDrive() {
		// I am a singleton
		super(Material.iron);
	}
	
	public static Block_ItemServerDrive getInstance() {
		return instance;
	}
	
	public void registerBlock() {
		final String blockName = "kekztech_itemserverdrive_block";
		super.setBlockName(blockName);
		super.setCreativeTab(CreativeTabs.tabMisc);
		super.setHardness(5.0f);
		super.setResistance(6.0f);
		GameRegistry.registerBlock(getInstance(), IB_ItemServerDrive.class, blockName);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister reg) {
		for(int i = 0; i < 6; i++) {
			if(i == 0) {
				faces[i] = reg.registerIcon(KekzCore.MODID + ":" + "ItemServerDrive_BOTTOM");
			} else if(i == 1) {
				faces[i] = reg.registerIcon(KekzCore.MODID + ":" + "ItemServerDrive_TOP");
			} else {
				faces[i] = reg.registerIcon(KekzCore.MODID + ":" + "ItemServerDrive");
			}
			
		}
	}
	
	@Override
	public IIcon getIcon(int side, int meta) {
		return faces[side];
	}
	
	@Override
	public int getLightValue() {
		return 7;
	}
	
}
