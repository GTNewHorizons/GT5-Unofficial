package items;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import kekztech.KekzCore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Item_Configurator extends Item {
	
	private static final Item_Configurator instance = new Item_Configurator();
	
	private Item_Configurator() {
		// I am a singleton
	}
	
	public static Item_Configurator getInstance() {
		return instance;
	}
	
	public void registerItem() {
		super.setHasSubtypes(false);
		final String unlocalizedName = "kekztech_configurator";
		super.setUnlocalizedName(unlocalizedName);
		super.setCreativeTab(CreativeTabs.tabMisc);
		super.setMaxStackSize(1);
		super.setTextureName(KekzCore.MODID + ":" + "DiamondHeatPipe");
		GameRegistry.registerItem(getInstance(), unlocalizedName);
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
		list.add("Used to configure Item Proxy Networks");
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("config")) {
			list.add("Channel: " + stack.getTagCompound().getString("config"));
		}
	}
	
}
