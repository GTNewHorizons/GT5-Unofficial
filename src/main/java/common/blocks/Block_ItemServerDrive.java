package common.blocks;

import common.itemBlocks.IB_ItemServerDrive;
import cpw.mods.fml.common.registry.GameRegistry;
import kekztech.KekzCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;

public class Block_ItemServerDrive extends BaseGTUpdateableBlock{

	private static Block_ItemServerDrive instance = new Block_ItemServerDrive();
	
	private IIcon[] faces = new IIcon[6];
	
	private Block_ItemServerDrive() {
		super(Material.iron);
	}
	
	public static Block registerBlock() {
		final String blockName = "kekztech_itemserverdrive_block";
		instance.setBlockName(blockName);
		instance.setCreativeTab(CreativeTabs.tabMisc);
		instance.setHardness(5.0f);
		instance.setResistance(6.0f);
		GameRegistry.registerBlock(instance, IB_ItemServerDrive.class, blockName);
		
		return instance;
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
