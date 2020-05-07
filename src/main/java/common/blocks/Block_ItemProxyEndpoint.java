package common.blocks;

import java.util.UUID;

import common.itemBlocks.IB_ItemProxyEndpoint;
import common.tileentities.TE_ItemProxyEndpoint;
import cpw.mods.fml.common.registry.GameRegistry;
import items.Item_Configurator;
import kekztech.GuiHandler;
import kekztech.KekzCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class Block_ItemProxyEndpoint extends Block {
	
	private static Block_ItemProxyEndpoint instance = new Block_ItemProxyEndpoint();
	
	private Block_ItemProxyEndpoint() {
		super(Material.glass);
	}
	
	public static Block registerBlock() {
		final String blockName = "kekztech_itemproxyendpoint_block";
		instance.setBlockName(blockName);
		instance.setCreativeTab(CreativeTabs.tabMisc);
		instance.setBlockTextureName(KekzCore.MODID + ":" + "ItemProxyEndpoint");
		instance.setHardness(3.0f);
		instance.setResistance(2.0f);
		GameRegistry.registerBlock(instance, IB_ItemProxyEndpoint.class, blockName);
		
		return instance;
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float lx, float ly, float lz) {
		if(world.isRemote) {
			return true;
		}
		
		final TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof TE_ItemProxyEndpoint) {
			final TE_ItemProxyEndpoint endpoint = (TE_ItemProxyEndpoint) te;
			if(player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem() instanceof Item_Configurator) {
				
				final ItemStack held = player.inventory.getCurrentItem();
				if(held.hasTagCompound() && held.getTagCompound().hasKey("config")) {
					endpoint.setChannel(UUID.fromString(held.getTagCompound().getString("config")));
				}
			} else {
				player.openGui(KekzCore.instance, GuiHandler.ITEM_PROXY_ENDPOINT, world, x, y, z);				
			}
			return true;
		}
		return false;
	}
	
	@Override
	public TileEntity createTileEntity(World world, int p_149915_2_) {
		return new TE_ItemProxyEndpoint();
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}

}