package blocks;

import java.util.UUID;

import cpw.mods.fml.common.registry.GameRegistry;
import itemBlocks.IB_ItemProxyEndpoint;
import items.Item_Configurator;
import kekztech.GuiHandler;
import kekztech.KekzCore;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tileentities.TE_ItemProxyEndpoint;

public class Block_ItemProxyEndpoint extends BlockContainer {
	
	private static Block_ItemProxyEndpoint instance = new Block_ItemProxyEndpoint();
	
	private Block_ItemProxyEndpoint() {
		super(Material.glass);
	}
	
	public static Block_ItemProxyEndpoint getInstance() {
		return instance;
	}
	
	public void registerBlock() {
		final String blockName = "kekztech_itemproxyendpoint_block";
		super.setBlockName(blockName);
		super.setCreativeTab(CreativeTabs.tabMisc);
		super.setBlockTextureName(KekzCore.MODID + ":" + "ItemProxyEndpoint");
		super.setHardness(3.0f);
		super.setResistance(2.0f);
		GameRegistry.registerBlock(getInstance(), IB_ItemProxyEndpoint.class, blockName);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float lx, float ly, float lz) {
		if(world.isRemote) {
			return true;
		}
		
		final TileEntity te = world.getTileEntity(x, y, z);
		if(te != null && te instanceof TE_ItemProxyEndpoint) {
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
	public TileEntity createNewTileEntity(World world, int p_149915_2_) {
		return new TE_ItemProxyEndpoint();
	}

}